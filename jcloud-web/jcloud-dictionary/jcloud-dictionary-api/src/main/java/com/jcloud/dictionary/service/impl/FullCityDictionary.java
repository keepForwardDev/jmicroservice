package com.jcloud.dictionary.service.impl;

import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.BooleanUtil;
import com.jcloud.common.util.TreeNodeUtil;
import com.jcloud.dictionary.consts.DictionaryConst;
import com.jcloud.dictionary.entity.FullCityEntity;
import com.jcloud.dictionary.mapper.FullCityEntityMapper;
import com.jcloud.dictionary.vo.PyRegionVo;
import com.jcloud.orm.model.RegionBaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 、
 * 全国城市
 *
 * @author jiaxm
 * @date 2021/4/12
 */
@Service(value = DictionaryConst.FULL_CITY)
public class FullCityDictionary extends AbstractDictionaryService<FullCityEntity> {

    public static final String PY_FULL_CITY = DictionaryConst.FULL_CITY + "- py";

    public static final String FULL_CITY_LEVEL_3 = DictionaryConst.FULL_CITY + "-level-3";

    @Autowired
    private FullCityEntityMapper fullCityEntityMapper;

    @Override
    public List<FullCityEntity> getData() {
        return fullCityEntityMapper.selectList(null);
    }

    @Override
    public String getDictionaryKey() {
        return DictionaryConst.FULL_CITY;
    }

    /**
     * 获取地点下级树
     * @param id
     * @return
     */
    public TreeNode getChildrenCity(Long id, Integer lazyLoad) {
        RegionBaseModel regionBaseModel = RegionBaseModelBuilder.create(id);
        return getChildrenTreeNode(regionBaseModel, lazyLoad);
    }

    /**
     * 获取下级树
     * @param regionBaseModel
     * @param lazyLoad 是否懒加载
     * @return
     */
    public TreeNode getChildrenTreeNode(RegionBaseModel regionBaseModel, Integer lazyLoad) {
        // 获取树
        List<TreeNode> treeNodeList = getTreeNode();
        TreeNode treeNode;
        TreeNode provinceNode = treeNodeList.stream().filter(r -> r.getId().longValue() == regionBaseModel.getProvinceId()).findFirst().orElse(null);
        if (regionBaseModel.getCityId() == null || regionBaseModel.getCityId() == 0) { // 省
            treeNode = provinceNode;
        } else if (regionBaseModel.getAreaId() == null || regionBaseModel.getAreaId() == 0) { // 市
            treeNode = provinceNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getCityId().longValue()).findFirst().orElse(null);
        } else if (regionBaseModel.getStreetId() == null || regionBaseModel.getStreetId() == 0) { // 区
            TreeNode cityNode = provinceNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getCityId().longValue()).findFirst().orElse(null);
            TreeNode areaNode = cityNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getAreaId().longValue()).findFirst().orElse(null);
            treeNode = areaNode;
        } else { // 街道
            TreeNode cityNode = provinceNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getCityId().longValue()).findFirst().orElse(null);
            TreeNode areaNode = cityNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getAreaId().longValue()).findFirst().orElse(null);
            treeNode = areaNode.getChildren().stream().filter(r -> r.getId().longValue() == regionBaseModel.getStreetId().longValue()).findFirst().orElse(null);
        }
        if (BooleanUtil.numberToBoolean(lazyLoad) && treeNode != null) {
            treeNode.setLeaf(CollectionUtils.isEmpty(treeNode.getChildren()));
            treeNode.setChildren(null);
        }
        return treeNode;
    }

    public RegionBaseModel toRegionBaseModel(Long regionId) {
        RegionBaseModel regionBaseModel = new RegionBaseModel();
        FullCityEntity fullCityEntity = getById(regionId);
        if (fullCityEntity == null) {
            return regionBaseModel;
        }
        if (fullCityEntity.getLevel().intValue() == 1) {
            regionBaseModel.setProvinceId(regionId);
        } else if (fullCityEntity.getLevel().intValue() == 2) {
            regionBaseModel.setCityId(regionId);
            regionBaseModel.setProvinceId(getById(fullCityEntity.getParentId()).getId());
        } else if (fullCityEntity.getLevel().intValue() == 3) {
            regionBaseModel.setAreaId(regionId);
            FullCityEntity cityEntity = getById(fullCityEntity.getParentId());
            regionBaseModel.setCityId(cityEntity.getId());
            regionBaseModel.setProvinceId(getById(cityEntity.getParentId()).getId());
        } else if (fullCityEntity.getLevel().intValue() == 4) {
            regionBaseModel.setStreetId(regionId);
            FullCityEntity areaEntity = getById(fullCityEntity.getParentId());
            regionBaseModel.setAreaId(areaEntity.getId());
            FullCityEntity cityEntity = getById(areaEntity.getParentId());
            regionBaseModel.setCityId(cityEntity.getId());
            regionBaseModel.setProvinceId(getById(cityEntity.getParentId()).getId());
        }
        return regionBaseModel;
    }


    @Override
    protected void dataToRedis(List<FullCityEntity> list, String key) {
        super.dataToRedis(list, key);
        addPyFullCity(list, key);
        addFullCityLevel(list);
    }

    private void addFullCityLevel(List<FullCityEntity> list) {
        List<FullCityEntity> resultList = new ArrayList<>();
         list.stream().forEach(item->{
            if(item.getLevel()<=3){
                resultList.add(item);
            }
        });
        dictionaryRedisTemplate.opsForList().leftPushAll(FULL_CITY_LEVEL_3, resultList);
    }

    /**
     * 获取py省市区
     * @return
     */
    public PyRegionVo getPyRegionVo() {
        PyRegionVo regionVo = (PyRegionVo) dictionaryRedisTemplate.opsForValue().get(PY_FULL_CITY);
        return regionVo;
    }


    /**
     * 添加py支持
     * @param list
     * @param key
     */
    private void addPyFullCity(List<FullCityEntity> list, String key) {
        PyRegionVo vo = new PyRegionVo();
        // 热门城市
        vo.setHotCity(list.stream().filter(r -> r.getHotFlag().intValue() == 1).map(r -> {
            LabelNode labelNode = new LabelNode();
            labelNode.setValue(r.getId());
            labelNode.setName(r.getName());
            labelNode.setLabel(r.getPinyin());
            return labelNode;
        }).collect(Collectors.toList()));
        // 省，根据字母分组
        Map<String, List<FullCityEntity>> province = list.stream().filter(r -> r.getLevel() == 1).collect(Collectors.groupingBy(r -> r.getPyFirstCharacter()));
        // 市 根据父级id 分组
        Map<Long, List<FullCityEntity>> cityGroup = list.stream().filter(r -> r.getLevel() == 2).collect(Collectors.groupingBy(r -> r.getParentId()));
        TreeMap<String, List<PyRegionVo.PyNode>> treeMap = new TreeMap<>();
        province.forEach((k, v) -> {
            List<PyRegionVo.PyNode> pyNodes = new ArrayList<>();
            for (FullCityEntity fullCityEntity : v) {
                PyRegionVo.PyNode pyNode = new PyRegionVo.PyNode();
                pyNode.setId(fullCityEntity.getId());
                pyNode.setName(fullCityEntity.getName());
                pyNode.setPinyin(fullCityEntity.getPinyin());
                pyNode.setLevel(fullCityEntity.getLevel());
                List<FullCityEntity> cityList = cityGroup.get(fullCityEntity.getId());
                if (!CollectionUtils.isEmpty(cityList)) {
                    for (FullCityEntity cityEntity : cityList) {
                        PyRegionVo.PyNode pyNode1 = new PyRegionVo.PyNode();
                        pyNode1.setId(cityEntity.getId());
                        pyNode1.setName(cityEntity.getName());
                        pyNode1.setPinyin(cityEntity.getPinyin());
                        pyNode1.setLevel(cityEntity.getLevel());
                        pyNode.getCityList().add(pyNode1);
                    }
                }
                pyNodes.add(pyNode);
            }
            treeMap.put(k, pyNodes);
        });
        vo.getAllCharacter().addAll(treeMap.keySet());
        vo.setCharacterProvinceGroup(treeMap);
        dictionaryRedisTemplate.opsForValue().set(PY_FULL_CITY, vo);
    }

    public ResponseData getFullCityLevel3(){
        ResponseData responseData = ResponseData.getSuccessInstance();
        List<FullCityEntity> fullCityEntities = (List<FullCityEntity>) (dictionaryRedisTemplate.opsForList().range(FullCityDictionary.FULL_CITY_LEVEL_3,0,-1)).get(0);
        List<TreeNode> treeNodeList = TreeNodeUtil.buildTree(fullCityEntities, "name", false, 0l);
        responseData.setData(treeNodeList);
        return responseData;
    }

}

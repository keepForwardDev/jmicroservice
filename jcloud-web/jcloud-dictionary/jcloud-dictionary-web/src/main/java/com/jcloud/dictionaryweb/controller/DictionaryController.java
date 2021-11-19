package com.jcloud.dictionaryweb.controller;

import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.TreeNodeUtil;
import com.jcloud.dictionary.api.DictionaryProvider;
import com.jcloud.dictionary.consts.DictionaryConst;
import com.jcloud.dictionary.entity.DictionaryBase;
import com.jcloud.dictionary.entity.FullCityEntity;
import com.jcloud.dictionary.entity.SimpleDictionaryEntity;
import com.jcloud.dictionary.service.DictionaryCrudOperation;
import com.jcloud.dictionary.service.DictionaryService;
import com.jcloud.dictionary.service.impl.FullCityDictionary;
import com.jcloud.dictionary.vo.PyRegionVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 字典web输出
 *
 * @author jiaxm
 * @date 2021/4/13
 */
@Api(tags = "字典管理")
@RestController
public class DictionaryController {


    @Autowired
    private DictionaryProvider dictionaryProvider;

    @Autowired
    private FullCityDictionary fullCityDictionary;


    @ApiOperation(value = "根据批定字典的名称取得名称所对应的对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在  full-city【省市区】simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "name", value = "字典中的名称", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/dictionaryNameHashObj/{key}/{name}", method = RequestMethod.GET)
    public ResponseData<DictionaryBase> dictionaryNameHashObj(@PathVariable String key, @PathVariable String name) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(getDictionaryService(key).get().getNameForObject(key, name));
        });
        return responseData;
    }

    /**
     * 根据id 获取字典
     *
     * @param key
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在  full-city【省市区】simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataTypeClass = Long.class)})
    @RequestMapping(value = "/getById/{key}/{id}", method = RequestMethod.GET)
    public ResponseData<DictionaryBase> getById(@PathVariable String key, @PathVariable Long id) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getById(id));
        });
        return responseData;
    }

    /**
     * 根据id 获取字典名称
     *
     * @param key
     * @param id
     * @return
     */
    @ApiOperation("根据id取字典名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在  full-city【省市区】simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(name = "id", value = "字典-ID", required = true, dataTypeClass = Long.class)})
    @RequestMapping(value = "/getNameById/{key}/{id}", method = RequestMethod.GET)
    public ResponseData<String> getNameById(@PathVariable String key, @PathVariable Long id) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getNameById(id));
        });
        return responseData;
    }


    /**
     * 根据名称获取
     *
     * @param nameKey
     * @param name
     * @return
     */
    @ApiOperation(value = "普通字典-根据名称获取字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "name", value = "字典的名称", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/getByName/{nameKey}", method = RequestMethod.GET)
    public ResponseData<DictionaryBase> getByName(@PathVariable String nameKey, @RequestParam(required = true) String name) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getByName(nameKey, name));
        });
        return responseData;
    }

    @ApiOperation(value = "普通字典 根据nameKey获取字典 ui选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/selectList/{nameKey}", method = RequestMethod.GET)
    public ResponseData<List<LabelNode>> getSelectList(@PathVariable String nameKey) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getSelectListByNameKey(nameKey));
        });
        return responseData;
    }

    @ApiOperation(value = "根据nameKey获取字典树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameKey", value = "普通字典-nameKey分组", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/treeNodeByNameKey/{nameKey}", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getTreeNodeByNameKey(@PathVariable String nameKey) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getTreeNodeByNameKey(nameKey));
        });
        return responseData;
    }

    /**
     * 获取树
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "获取字典ui树")
    @ApiImplicitParams({            @ApiImplicitParam(name = "key", value = "字典的名称，目前已存在  full-city【省市区】simple-dictionary【标准字典】", dataTypeClass = String.class, required = true),
    })
    @RequestMapping(value = "/getTree/{key}", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getTree(@PathVariable String key) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(r.getTreeNode());
        });
        return responseData;
    }

    /**
     * 获取树
     *
     * @return
     */
    @ApiOperation(value = "获取城市  省市区3级数据")
    @RequestMapping(value = "/getFullCityTreeLevel3", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getFullCityTreeLevel3() {
        return fullCityDictionary.getFullCityLevel3();
    }


    /**
     * 获取字典full-city树指定需要获取的level层级 内网使用
     *
     * @param level
     * @return
     */
    @ApiOperation(value = "获取字典full-city树指定需要获取的level层级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "level", value = "层级", required = true, dataTypeClass = String.class)})
    @RequestMapping(value = "/getFullCityTreeByLevle/{level}", method = RequestMethod.GET)
    public ResponseData<List<TreeNode>> getFullCityTreeByLevle(@PathVariable String level) {
        ResponseData responseData = new ResponseData();
        getDictionaryService("full-city").ifPresent(r -> {
            List<FullCityEntity> fullCityEntities = new ArrayList<>();
            for (Object o : r.redisToList()) {
                FullCityEntity fullCityEntity = (FullCityEntity) o;
                if (fullCityEntity.getLevel() <= Long.parseLong(level)) {
                    fullCityEntities.add(fullCityEntity);
                }
            }
            List<TreeNode> treeNodeList = TreeNodeUtil.buildTree(fullCityEntities, "name", false, 0l);
            responseData.setCode(Const.CODE_SUCCESS);
            responseData.setMsg(Const.CODE_SUCCESS_STR);
            responseData.setData(treeNodeList);
        });
        return responseData;
    }

    @ApiOperation(value = "获取城市下级树")
    @RequestMapping(value = "/getChildrenRegion/{id}", method = RequestMethod.GET)
    public ResponseData getChildrenRegion(@ApiParam(name = "省市区ID", required = true) @PathVariable Long id, @ApiParam(name = "是否懒加载", defaultValue = "0") Integer lazyLoad) {
        ResponseData responseData = ResponseData.getSuccessInstance();
        responseData.setData(fullCityDictionary.getChildrenCity(id, lazyLoad));
        return responseData;
    }

    @ApiOperation(value = "获取城市拼音分组")
    @RequestMapping(value = "/getPyRegion", method = RequestMethod.GET)
    public ResponseData<PyRegionVo> getPyRegion() {
        ResponseData responseData = ResponseData.getSuccessInstance();
        responseData.setData(fullCityDictionary.getPyRegionVo());
        return responseData;
    }


    @ApiOperation(value = "刷新字典")
    @RequestMapping(value = "/refresh/{key}", method = RequestMethod.GET)
    public ResponseData refresh(@PathVariable String key) {
        dictionaryProvider.refresh(key);
        return ResponseData.getSuccessInstance();
    }

    private Optional<DictionaryService> getDictionaryService(String key) {
        return Optional.ofNullable(DictionaryProvider.service(key));
    }


    @ApiOperation(value = "根据父级id获取列表")
    @RequestMapping(value = "/getByParentId/{key}/{parentId}", method = RequestMethod.GET)
    public ResponseData getByParentId(@PathVariable String key, @PathVariable Long parentId) {
        ResponseData responseData = new ResponseData();
        getDictionaryService(key).ifPresent(r -> {
            if (r instanceof DictionaryCrudOperation) {
                DictionaryCrudOperation dictionaryCrudOperation = (DictionaryCrudOperation) r;
                responseData.setData(dictionaryCrudOperation.getByParentId(parentId));
                responseData.setCode(Const.CODE_SUCCESS);
                responseData.setMsg(Const.CODE_SUCCESS_STR);
            }
        });
        return responseData;
    }

    @ApiOperation(value = "新增或更新 删除简单字典")
    @RequestMapping(value = "/cudOperation", method = RequestMethod.POST)
    public ResponseData cudOperation(SimpleDictionaryEntity simpleDictionary) {
        getDictionaryService(DictionaryConst.SIMPLE_DICTIONARY).ifPresent(r -> {
            DictionaryCrudOperation dictionaryCrudOperation = (DictionaryCrudOperation) r;
            dictionaryCrudOperation.cudOperation(simpleDictionary);
        });
        return ResponseData.getSuccessInstance();
    }
}

package com.jcloud.dictionary.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.dictionary.entity.FullCityEntity;
import com.jcloud.orm.model.RegionBaseModel;

/**
 * 构建regionBaseModel
 * @author jiaxm
 * @date 2021/4/13
 */
public class RegionBaseModelBuilder {

    /**
     * 末级id
     * @param id
     * @return
     */
    public static RegionBaseModel create(Long id) {
        RegionBaseModel regionBaseModel = new RegionBaseModel();
        FullCityDictionary fullCityDictionary = SpringUtil.getBean(FullCityDictionary.class);
        FullCityEntity fullCityEntity = fullCityDictionary.getById(id);
        if (fullCityEntity != null) {
            if (fullCityEntity.getLevel().intValue() == 1) {
                regionBaseModel.setProvinceId(id);
            } else if (fullCityEntity.getLevel().intValue() == 2) {
                regionBaseModel.setCityId(id);
                regionBaseModel.setProvinceId(fullCityEntity.getParentId());
            } else if (fullCityEntity.getLevel().intValue() == 3) {
                regionBaseModel.setAreaId(id);
                FullCityEntity cityEntity = fullCityDictionary.getById(fullCityEntity.getParentId());
                regionBaseModel.setCityId(cityEntity.getId());
                regionBaseModel.setProvinceId(cityEntity.getParentId());
            } else if (fullCityEntity.getLevel().intValue() == 4) {
                regionBaseModel.setStreetId(id);
                FullCityEntity areaEntity = fullCityDictionary.getById(fullCityEntity.getParentId());
                regionBaseModel.setAreaId(areaEntity.getId());
                FullCityEntity cityEntity = fullCityDictionary.getById(areaEntity.getParentId());
                regionBaseModel.setCityId(cityEntity.getId());
                regionBaseModel.setProvinceId(cityEntity.getParentId());
            }
        }
        return regionBaseModel;
    }

}

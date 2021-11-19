package com.jcloud.dictionary.vo;

import com.jcloud.common.bean.LabelNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 省市区拼音输出
 * @author jiaxm
 * @date 2021/6/9
 */
@ApiModel(value = "拼音地区检索")
@Data
public class PyRegionVo implements Serializable {

    @ApiModelProperty(value = "热门城市")
    private List<LabelNode> hotCity = null;

    @ApiModelProperty(value = "全国省首字母")
    private List<String> allCharacter = new ArrayList<>();

    /**
     * 字母分组
     */
    @ApiModelProperty(value = "字母省分组")
    private Map<String, List<PyNode>> characterProvinceGroup;

    @Data
    public static class PyNode {

        @ApiModelProperty(value = "id")
        private Long id;

        /**
         * 名称
         */
        @ApiModelProperty(value = "名称")
        private String name;

        /**
         * 全拼
         */
        @ApiModelProperty(value = "全拼")
        private String pinyin;

        @ApiModelProperty(value = "级别")
        private Integer level;

        /**
         * 城市列表
         */
        @ApiModelProperty(value = "城市列表")
        private List<PyNode> cityList = new ArrayList<>();

    }
}

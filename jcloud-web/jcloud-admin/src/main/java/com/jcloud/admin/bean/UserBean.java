package com.jcloud.admin.bean;

import com.jcloud.dictionary.consts.DictionaryConst;
import com.jcloud.dictionary.support.JacksonDicMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "用户输入输出实体")
public class UserBean  {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "唯一账户")
    private String account;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像地址")
    private String avatar;

    /**
     * 真实名字
     */
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "角色-用于查询")
    private Long roleId;

    @ApiModelProperty(value = "角色-用于修改")
    private List<Long> roleIds = new ArrayList<>();

    /**
     * 性别（1：男 2：女）
     */
    @ApiModelProperty(value = "性别")
    private Integer sex = 1;

    /**
     * 电话
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 所属部门
     */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /**
     * 所属部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    /**
     * 是否启用 0 否 1 是
     */
    @ApiModelProperty(value = "是否启用，0否 1是")
    private Integer enabled = 0;

    @ApiModelProperty(value = "openId")
    private String openid;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "数据来源")
    @JacksonDicMapping(dictionaryConst = DictionaryConst.SIMPLE_DICTIONARY, useId = false)
    private Long sourceFrom;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户类型 0 系统用户 1外部用户")
    private Integer type;
}

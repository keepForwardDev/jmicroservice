package com.jcloud.admin.bean;

import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.bean.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Data
public class PrivilegesSaveBean extends PrivilegeBean{

    @ApiModelProperty(value = "关联资源ID")
    private List<Long> resourceIds = new ArrayList<>();

    @ApiModelProperty(value = "api配置")
    private List<ApiLimit> apiConfig = new ArrayList<>();

    @ApiModelProperty(value = "api树")
    private Collection<TreeNode> apiTree = null;
}

package com.jcloud.swagger;

import com.jcloud.common.bean.TreeNode;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Data
public class SwaggerResourceBean {

    /**
     * 路径地址和index id
     */
    private Map<Object, TreeNode> pathMap;

    /**
     * 组装成的树结构，按照tag分组
     */
    private Collection<TreeNode> resourceTree;

}

package com.jcloud.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable {
    private Long id;

    private String label;

    private Boolean disabled = false;

    private String extra;

    private List<TreeNode> children = null;

    private Object data;

    public Long getId() {
        return id;
    }

    public TreeNode() {
        children = new ArrayList<>();
    }

    /**
     * 是否为叶子节点
     */
    private Boolean leaf = false;

    public TreeNode(boolean initChild) {
        if (initChild) {
            children = new ArrayList<>();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<TreeNode> children() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public Boolean getLeaf() {
        if (children == null) {
            leaf = true;
        }
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

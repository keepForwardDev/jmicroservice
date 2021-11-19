package com.jcloud.common.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeLabelNode {

    private String label;

    private Long value;

    private List<TreeLabelNode> children = new ArrayList<>();

}

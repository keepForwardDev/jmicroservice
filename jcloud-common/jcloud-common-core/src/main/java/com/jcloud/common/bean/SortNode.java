package com.jcloud.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiaxm
 * @date 2021/7/30
 */
@Data
public class SortNode implements Serializable, Comparable<SortNode> {

    private String name;

    private Double value;

    public SortNode() {

    }

    public SortNode(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(SortNode o) {
        if (this.getValue() > o.getValue()) {
            return -1;
        } else if (this.getValue() < o.getValue()) {
            return 1;
        }
        return 0;
    }
}

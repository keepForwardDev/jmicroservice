package com.jcloud.common.bean;

import java.io.Serializable;

/**
 * name value 记录， 实现Comparable 接口，可以排序 collections.sort，默认为降序，转为升序可以用 collections.reverse
 *
 */
public class LabelNode implements Serializable, Comparable<LabelNode> {
    public LabelNode() {

    }

    public LabelNode(String label, Long value) {
        this.label = label;
        this.value = value;
    }

    public LabelNode(String label, Long value,String name) {
        this.label = label;
        this.value = value;
        this.name = name;
    }

    private String label;

    private Long  value;

    private String name;



    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public int compareTo(LabelNode o) { // 倒序排列
        if (this.getValue() > o.getValue()) {
            return -1;
        } else if (this.getValue() < o.getValue()) {
            return 1;
        }
        return 0;
    }
}

package com.jcloud.common.bean;

import lombok.Data;

/**
 * 可以将字段的id附上，应用与字典转换
 * @author jiaxm
 * @date 2021/5/7
 */
@Data
public class ItemNode extends LabelNode {

    private Long id;

    private Object extra;

}

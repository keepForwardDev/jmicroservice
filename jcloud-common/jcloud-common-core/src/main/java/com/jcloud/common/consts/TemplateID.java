package com.jcloud.common.consts;

/**
 * 短信id
 * @author jiaxm
 * @date 2021/4/1
 */
public enum TemplateID {

    //博士云维护短信
    template_defend("595611","维护短信"),
    template_mobile_code("563610","短信验证码");


    private String id;
    private String name;

    TemplateID(String _id, String name){
        this.id=_id;
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.id;
    }
}

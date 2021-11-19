package com.jcloud.configure;

import org.springframework.web.context.request.RequestAttributes;

/**
 * 我们在一个非web方法里边调用了微服务，RequestAttributes为null会报错
 * @author laiguowei
 * @date 2021/8/25/025 17:03
 */
public class NonWebRequestAttributes implements RequestAttributes {

    public NonWebRequestAttributes(){

    }
    @Override
    public Object getAttribute(String name, int scope){return null;}

    @Override
    public void setAttribute(String name, Object value, int scope) {

    }
    @Override
    public void removeAttribute(String name, int scope){}
    @Override
    public String[] getAttributeNames(int scope){return new String[0];}

    @Override
    public void registerDestructionCallback(String name, Runnable callback, int scope) {

    }

    @Override
    public Object resolveReference(String key) {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Object getSessionMutex() {
        return null;
    }
}


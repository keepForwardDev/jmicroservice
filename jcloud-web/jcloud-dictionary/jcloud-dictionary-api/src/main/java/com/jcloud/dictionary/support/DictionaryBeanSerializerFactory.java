package com.jcloud.dictionary.support;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;

/**
 * 字典输出转换
 * @author jiaxm
 * @date 2021/4/29
 */
public class DictionaryBeanSerializerFactory extends BeanSerializerFactory {

    /**
     * Constructor for creating instances with specified configuration.
     *
     * @param config
     */
    public DictionaryBeanSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }


    @Override
    protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder) throws JsonMappingException {
        List<BeanPropertyWriter> list = super.findBeanProperties(prov, beanDesc, builder);
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        ListIterator<BeanPropertyWriter> iterator = list.listIterator();
        while (iterator.hasNext()) {
            BeanPropertyWriter propertyWriter = iterator.next();
            BeanPropertyDefinition propertyDefinition = properties.stream().filter(r -> r.getName().equals(propertyWriter.getName())).findFirst().orElse(null);
            if (propertyDefinition == null || propertyDefinition.getField() == null || propertyDefinition.getField().getAnnotated() == null) continue;
            Field field = propertyDefinition.getField().getAnnotated();
            JacksonDicMapping dictionaryTrans = field.getAnnotation(JacksonDicMapping.class);
            if (dictionaryTrans != null) { // 如果是字典参数的
                if (!dictionaryTrans.useId()) {
                    iterator.remove();
                }
                BeanPropertyWriter dicPropertyWriter = new DictionaryPropertyWriter(StringUtils.defaultIfBlank(dictionaryTrans.fieldName(), field.getName() + "Str"), propertyWriter, dictionaryTrans.dictionaryConst(), dictionaryTrans.separator());
                iterator.add(dicPropertyWriter);
            }
        }
        return list;
    }

    @Override
    public SerializerFactory withConfig(SerializerFactoryConfig config) {
        if (_factoryConfig == config) {
            return this;
        }
        return new DictionaryBeanSerializerFactory(config);
    }
}

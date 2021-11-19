package com.jcloud.dictionary.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.jcloud.dictionary.api.DictionaryProvider;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/4/29
 */
public class DictionaryPropertyWriter extends BeanPropertyWriter {

    /**
     * 字典字段
     */
    private BeanPropertyWriter delegated;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字典常量
     */
    private String dictionaryConst;

    public DictionaryPropertyWriter(String name, BeanPropertyWriter delegated, String dictionaryConst) {
        Assert.notNull(name, "name is require");
        Assert.notNull(name, "dictionaryConst is require");
        this.delegated = delegated;
        this.name = name;
        this.dictionaryConst = dictionaryConst;
    }


    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        final Long value = (Long) delegated.get(bean);
        String dictionaryValue = null;
        if (value != null) {
            dictionaryValue = DictionaryProvider.service(dictionaryConst).getNameById(value);
        }
        if (value == null || dictionaryValue == null) {
            if (_nullSerializer != null) {
                gen.writeFieldName(name);
                _nullSerializer.serialize(null, gen, prov);
            }
            return;
        }
        gen.writeStringField(name, dictionaryValue);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyName getFullName() {
        return new PropertyName(name);
    }

    public <A extends Annotation> A findAnnotation(Class<A> acls) {
        return delegated.findAnnotation(acls);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return delegated.getAnnotation(acls);
    }

    @Override
    public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
        return delegated.getContextAnnotation(acls);
    }

    public void serializeAsOmittedField(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws Exception {
        if (!jgen.canOmitFields()) {
            jgen.writeOmittedField(name);
        }
    }

    public void serializeAsElement(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws Exception {
        delegated.serializeAsElement(value, jgen, provider);
    }


    public void serializeAsPlaceholder(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws Exception {
        delegated.serializeAsPlaceholder(value, jgen, provider);
    }


    @Override
    public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor,
                                      SerializerProvider provider)
            throws JsonMappingException {
        delegated.depositSchemaProperty(objectVisitor, provider);
    }

    @Override
    public void fixAccess(SerializationConfig config) {
        delegated.fixAccess(config);
    }

    @Override
    public BeanPropertyWriter rename(NameTransformer transformer) {
        return delegated.rename(transformer);
    }

    @Override
    public void assignTypeSerializer(TypeSerializer typeSer) {
        delegated.assignTypeSerializer(typeSer);
    }

    @Override
    public void assignSerializer(JsonSerializer<Object> ser) {
        delegated.assignSerializer(ser);
    }

    @Override
    public void assignNullSerializer(JsonSerializer<Object> nullSer) {
        delegated.assignNullSerializer(nullSer);
    }

    @Override
    public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
        return delegated.unwrappingWriter(unwrapper);
    }

    @Override
    public void setNonTrivialBaseType(JavaType t) {
        delegated.setNonTrivialBaseType(t);
    }

    @Override
    public JavaType getType() {
        return delegated.getType();
    }

    @Override
    public PropertyName getWrapperName() {
        return delegated.getWrapperName();
    }

    @Override
    public AnnotatedMember getMember() {
        return delegated.getMember();
    }

    @Override
    public Object getInternalSetting(Object key) {
        return delegated.getInternalSetting(key);
    }

    @Override
    public Object setInternalSetting(Object key, Object value) {
        return delegated.setInternalSetting(key, value);
    }

    @Override
    public Object removeInternalSetting(Object key) {
        return delegated.removeInternalSetting(key);
    }

    @Override
    public SerializableString getSerializedName() {
        return delegated.getSerializedName();
    }

    @Override
    public boolean hasSerializer() {
        return delegated.hasSerializer();
    }

    @Override
    public boolean hasNullSerializer() {
        return delegated.hasNullSerializer();
    }

    @Override
    public TypeSerializer getTypeSerializer() {
        return delegated.getTypeSerializer();
    }

    @Override
    public boolean isUnwrapping() {
        return delegated.isUnwrapping();
    }

    @Override
    public boolean willSuppressNulls() {
        return delegated.willSuppressNulls();
    }

    @Override
    public boolean wouldConflictWithName(PropertyName name) {
        return delegated.wouldConflictWithName(name);
    }

    @Override
    public JsonSerializer<Object> getSerializer() {
        return delegated.getSerializer();
    }

    @Override
    public JavaType getSerializationType() {
        return delegated.getSerializationType();
    }

    @Override
    public boolean isRequired() {
        return delegated.isRequired();
    }

    @Override
    public PropertyMetadata getMetadata() {
        return delegated.getMetadata();
    }

    @Override
    public boolean isVirtual() {
        return delegated.isVirtual();
    }

    @Override
    public JsonFormat.Value findPropertyFormat(MapperConfig<?> config, Class<?> baseType) {
        return delegated.findPropertyFormat(config, baseType);
    }

    @Override
    public JsonInclude.Value findPropertyInclusion(MapperConfig<?> config, Class<?> baseType) {
        return delegated.findPropertyInclusion(config, baseType);
    }

    @Override
    public List<PropertyName> findAliases(MapperConfig<?> config) {
        return delegated.findAliases(config);
    }

    @Override
    public Class<?> getRawSerializationType() {
        return delegated.getRawSerializationType();
    }

    @Override
    public Class<?> getPropertyType() {
        return delegated.getPropertyType();
    }

    @Override
    public Type getGenericPropertyType() {
        return delegated.getGenericPropertyType();
    }

    @Override
    public Class<?>[] getViews() {
        return delegated.getViews();
    }

    @Override
    public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider) throws JsonMappingException {
        delegated.depositSchemaProperty(propertiesNode, provider);
    }
}

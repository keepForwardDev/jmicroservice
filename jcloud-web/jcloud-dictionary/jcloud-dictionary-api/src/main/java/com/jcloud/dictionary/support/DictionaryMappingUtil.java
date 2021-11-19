package com.jcloud.dictionary.support;

import cn.hutool.cache.impl.LRUCache;
import com.jcloud.common.bean.ItemNode;
import com.jcloud.common.util.ReflectUtil;
import com.jcloud.common.util.TypeUtil;
import com.jcloud.dictionary.api.DictionaryProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典映射解析工具
 * @author jiaxm
 * @date 2021/4/30
 */
public class DictionaryMappingUtil {

    /**
     * 缓存类的 字典字段
     */
    private static final LRUCache<String, Map<String, List<DictionaryField>>> cacheMap = new LRUCache<>(20);


    /**
     * 解析字典映射
     *
     * @param list
     * @param <T>
     */
    public static <T> void dictionaryMapping(List<T> list) {
        Assert.notNull(list, "list is required");
        Map<String, Set<Long>> dictionaryValueMap = new HashMap<>();
        // 聚合字典值
        for (T t : list) {
            addDictionaryValue(t, dictionaryValueMap);
        }
        dictionaryMapping(list, dictionaryValueMap);
    }

    /**
     * 解析字典映射
     *
     * @param list
     * @param dictionaryValueMap 字典分组下需要查询的字典值
     * @param <T>
     */
    public static <T> void dictionaryMapping(List<T> list, Map<String, Set<Long>> dictionaryValueMap) {
        Assert.notNull(list, "list is required");
        if(list.size()>0){
            Class clazz = list.get(0).getClass();
            Map<String, List<DictionaryField>> dictionaryFieldMap = getDictionaryFields(clazz);
            // 返回的结果映射
            Map<String, Map<Long, String>> dictionaryMap = new HashMap<>();
            // 查询字段值
            dictionaryValueMap.forEach((k, v) -> {
                dictionaryMap.put(k, DictionaryProvider.service(k).getNameMap(v));
            });
            // 赋值
            for (T t : list) {
                dictionaryFieldMap.forEach((dictionaryConst, dictionaryFields) -> {
                    Map<Long, String> dictionary = dictionaryMap.get(dictionaryConst);
                    for (DictionaryField dictionaryField : dictionaryFields) {
                        try {
                            if (StringUtils.isBlank(dictionaryField.getSourceSeparator())) {
                                Long value = TypeUtil.toLon(dictionaryField.getSource().get(t));
                                dictionaryField.getTarget().set(t, dictionary.get(value));
                            } else {
                                String value = TypeUtil.toStr(dictionaryField.getSource().get(t));
                                if (StringUtils.isNotBlank(value)) {
                                    String[] valueArray = value.split(dictionaryField.getSourceSeparator());
                                    String values = StringUtils.EMPTY;
                                    for (String s : valueArray) {
                                        values += StringUtils.defaultString(dictionary.get(Long.valueOf(s)));
                                    }
                                    dictionaryField.getTarget().set(t, dictionary.get(StringUtils.defaultIfBlank(values, null)));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
    }

    /**
     * 构建字典value
     *
     * @param t
     * @param dictionaryValueMap
     * @param <T>
     */
    public static <T> void addDictionaryValue(T t, Map<String, Set<Long>> dictionaryValueMap) {
        Map<String, List<DictionaryField>> dictionaryFieldMap = getDictionaryFields(t.getClass());
        dictionaryFieldMap.forEach((dictionaryConst, dictionaryFields) -> {
            Set<Long> idSet = dictionaryValueMap.get(dictionaryConst);
            if (idSet == null) {
                idSet = new HashSet<>();
                dictionaryValueMap.put(dictionaryConst, idSet);
            }
            for (DictionaryField dictionaryField : dictionaryFields) {
                try {
                    if (StringUtils.isNotBlank(dictionaryField.getSourceSeparator())) { // 多选文本支持
                        String value = TypeUtil.toStr(dictionaryField.getSource().get(t));
                        if (StringUtils.isNotBlank(value)) {
                            String[] valueArray = value.split(dictionaryField.getSourceSeparator());
                            for (String s : valueArray) {
                                idSet.add(Long.valueOf(s));
                            }
                        }
                    } else {
                        Long value = TypeUtil.toLon(dictionaryField.getSource().get(t));
                        idSet.add(value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取类上标注有 DictionaryMapping 注解的field，并且根据字典分组
     * @param clazz
     * @return
     */
    public static Map<String, List<DictionaryField>> getDictionaryFields(Class clazz) {
        Map<String, List<DictionaryField>> mappingField = cacheMap.get(clazz.getName());
        if (mappingField == null) {
            List<Field> fields = ReflectUtil.getEntityAllField(clazz);
            Map<String, Field> fieldMap = fields.stream().collect(Collectors.toMap(r -> r.getName(), r -> {
                return r;
            }));
            mappingField = new HashMap<>();
            for (Field field : fields) {
                DictionaryMapping dictionaryMapping = field.getAnnotation(DictionaryMapping.class);
                if (dictionaryMapping != null) {
                    String sourceFieldName = dictionaryMapping.value();
                    String dictionaryConst = dictionaryMapping.dictionaryConst();
                    String sourceSeparator = dictionaryMapping.separator();
                    Field sourceField = fieldMap.get(sourceFieldName);
                    sourceField.setAccessible(true);
                    field.setAccessible(true);
                    if (sourceField == null) continue;
                    DictionaryField dictionaryField = new DictionaryField(sourceField, field, sourceSeparator);
                    List<DictionaryField> fieldList = mappingField.get(dictionaryConst);
                    if (fieldList == null) {
                        fieldList = new ArrayList<>();
                        mappingField.put(dictionaryConst, fieldList);
                    }
                    fieldList.add(dictionaryField);
                }
            }
            cacheMap.put(clazz.getName(), mappingField);
            return mappingField;
        }
        return cacheMap.get(clazz.getName());
    }

    /**
     *
     * @param itemNodes
     * @param dictionaryConst
     */
    public void itemNodeMapping(List<ItemNode> itemNodes, String dictionaryConst) {
        Set<Long> list = itemNodes.stream().map(r -> r.getId()).collect(Collectors.toSet());
        Map<Long,String> map = DictionaryProvider.service(dictionaryConst).getNameMap(list);
        itemNodes.forEach(r -> {
            r.setName(map.get(r.getId()));
        });
    }



    public static class DictionaryField {

        private Field source;

        private Field target;

        private String sourceSeparator;

        public DictionaryField(Field source, Field target, String sourceSeparator) {
            this.source = source;
            this.target = target;
            this.sourceSeparator = sourceSeparator;
        }


        public Field getSource() {
            return source;
        }


        public Field getTarget() {
            return target;
        }

        public String getSourceSeparator() {
            return sourceSeparator;
        }

    }

}

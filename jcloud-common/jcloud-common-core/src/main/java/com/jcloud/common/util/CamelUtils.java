package com.jcloud.common.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class CamelUtils {

	private static final String SPLIT_SYMBOL = "_";

	public static String toCamelCase(String fieldName) {
        return toCamelCaseSimple(fieldName);
	}
	/**
	 * 下划线变成驼峰
	 * @param fieldName
	 * @return
	 */
	private static String toCamelCaseSimple(String fieldName){
		if(StringUtils.isBlank(fieldName))return "";
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName.trim());
	}

	/**
	 * 去驼峰，转为下划线
	 * @param fieldNames
	 */
	public static void deallCamelCase(List<String> fieldNames) {

	    if (CollectionUtils.isEmpty(fieldNames)) {
	        return;
	    }
	    for (int i = 0; i < fieldNames.size(); i++) {
	        String fieldName = fieldNames.get(i);
	        if (StringUtils.isNotBlank(fieldName)) {
	            //去空格
	            fieldName = fieldName.replace(" ", "");
	            //逐个字符遍历
	            StringBuilder sb = new StringBuilder(16);
	            for (Character chr : fieldName.toCharArray()) {
	                if (Character.isLetter(chr)) {
	                    if (Character.isUpperCase(chr)) {
	                        chr = Character.toLowerCase(chr);
	                        sb.append(SPLIT_SYMBOL).append(chr);
	                    } else {
	                        sb.append(chr);
	                    }
	                } else {
	                    sb.append(chr);
	                }
	            }
	            fieldName = sb.toString().toUpperCase();
	            //使用驼峰格式替换下划线格式
	            fieldNames.set(i, fieldName);
	        }
	    }
	}

	/**
	 * 去驼峰，转为下划线
	 * @param fieldName
	 * @return
	 */
	public static String dealCamelCase(String fieldName) {
        if (StringUtils.isNotBlank(fieldName)) {
            //去空格
            fieldName = fieldName.replace(" ", "");
            //逐个字符遍历
            StringBuilder sb = new StringBuilder(16);
            for (Character chr : fieldName.toCharArray()) {
                if (Character.isLetter(chr)) {
                    if (Character.isUpperCase(chr)) {
                        chr = Character.toLowerCase(chr);
                        sb.append(SPLIT_SYMBOL).append(chr);
                    } else {
                        sb.append(chr);
                    }
                } else {
                    sb.append(chr);
                }
            }
//            fieldName = sb.toString().toUpperCase();
            return sb.toString();
        }
        return null;
    }


}

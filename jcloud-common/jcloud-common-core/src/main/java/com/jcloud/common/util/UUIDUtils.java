package com.jcloud.common.util;



import java.util.UUID;


public class UUIDUtils {

	public static String genUUid(){
		String uuid=UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}





}


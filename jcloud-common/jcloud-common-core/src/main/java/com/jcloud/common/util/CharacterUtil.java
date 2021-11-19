package com.jcloud.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符集识别工具类
 */
public class CharacterUtil {
    public static final int CHAR_USELESS=0;
    public static final int CHAR_ARABIC=0X00000001;
    public static final int CHAR_ENGLISH=0X00000002;
    public static final int CHAR_CHINESE=0X00000004;
    public static final int CHAR_OTHER_CJK=0X00000008;


    /***
     * 进行字符规格化(全角转半角,大写转小写处理）
     * @param input
     * @param lowercase
     * @return
     */
    public static char regularize(char input,boolean lowercase){
        if(input == 12288){
            input=(char)32;
        }else if(input > 65280 && input < 65375){
            input = (char)(input - 65248);
        }else if(input >='A'&&input <='Z'&&lowercase){
            input +=32;
        }
        return input;
    }


    public static String cleanStr(String str) {
		if(str==null || str.equals(""))return "" ;
		char[] c=str.toCharArray();
		List<Character> list=new ArrayList<Character>();
		for(int i=0;i<c.length;i++) {
			if(check(c[i])) {
				list.add(regularize(c[i],false));
			}
		}
		char[] tt=new char[list.size()];
		int j=0 ;
		for(char ch:list) {
			tt[j]=ch ;
			j++ ;
		}
		return  new String(tt);
	}


	public static boolean check(char input) {
		 if(input >='0' && input <='9'){
             return true;
         }else  if((input >= 'a'&& input <='z')||(input >='A'&&input <='Z')){
             return true;
         }else if(input=='-'||input=='_'||input=='—') {
        	 return true ;
         }else{
        	 Character.UnicodeBlock ub=Character.UnicodeBlock.of(input);
        	 if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
        		 return true;
        	 }
         }
		return false ;
	}


	/**
     * 过滤掉超过3个字节的UTF8字符
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String filterOffUtf8Mb4(String text)  {

		try {
			 byte[] bytes= text.getBytes("utf-8");
			 ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		        int i = 0;
		        while (i < bytes.length) {
		            short b = bytes[i];
		            if (b > 0) {
		                buffer.put(bytes[i++]);
		                continue;
		            }

		            b += 256; // 去掉符号位

		            if (((b >> 5) ^ 0x6) == 0) {
		                buffer.put(bytes, i, 2);
		                i += 2;
		            } else if (((b >> 4) ^ 0xE) == 0) {
		                buffer.put(bytes, i, 3);
		                i += 3;
		            } else if (((b >> 3) ^ 0x1E) == 0) {
		                i += 4;
		            } else if (((b >> 2) ^ 0x3E) == 0) {
		                i += 5;
		            } else if (((b >> 1) ^ 0x7E) == 0) {
		                i += 6;
		            } else {
		                buffer.put(bytes[i++]);
		            }
		        }
		        buffer.flip();
		        return new String(buffer.array(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
        	return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
        	return true;
        }else{
        	return false;
        }
    }

    /**
     * 验证只能输入数字
     * @param orginal
     * @return
     */
    public static boolean isNumeric(String orginal) {
		return isMatch("^\\d+$", orginal);
	}

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) { //$NON-NLS-1$
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}




//    public static String filterOffUtf8Mb4_2(String text) throws UnsupportedEncodingException {
//    	byte[] bytes = text.getBytes("utf-8");
//    	ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
//    	int i = 0;
//    	while (i < bytes.length) {
//    	short b = bytes[i];
//    	if (b > 0) {
//    	buffer.put(bytes[i++]);
//    	continue;
//    	}
//
//    	b += 256; //去掉符号位
//
//    	if (((b >> 5) ^ 0x06) == 0) {
//    	buffer.put(bytes, i, 2);
//    	i += 2;
//    	System.out.println("2");
//    	} else if (((b >> 4) ^ 0x0E) == 0) {
//    	System.out.println("3");
//    	buffer.put(bytes, i, 3);
//    	i += 3;
//    	} else if (((b >> 3) ^ 0x1E) == 0) {
//    	i += 4;
//    	System.out.println("4");
//    	} else if (((b >> 2) ^ 0xBE) == 0) {
//    	i += 5;
//    	System.out.println("5");
//    	} else {
//    	i += 6;
//    	System.out.println("6");
//    	}
//    	}
//    	buffer.flip();
//    	return new String(buffer.array(), "utf-8");
//    	}


	public static void main(String[] args) {
		System.out.println(isEmail("/"));
		System.out.println(isNumeric("0208735111"));


	}

}

package com.jcloud.common.util;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.jcloud.common.consts.TemplateID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


/**
 * 容联短信发送
 */
public class SendMsgUtil {


    private static Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);



    /**
     * 发送短信验证码
     * @param phone
     * @param content
     * @return
     */
    public static boolean sendMobileCode(String phone ,String[] content){
        return sendMsgTemplate(phone, TemplateID.template_mobile_code.getValue(),content);
    }


    public static boolean send(String appId,String phoneNumber,String templateId,String[] content){
        boolean success = false;
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(Config.serverIp, Config.serverPort);
        sdk.setAccount(Config.accountSId, Config.accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        HashMap<String, Object>  result = sdk.sendTemplateSMS(phoneNumber, templateId, content);
        success=doResult(result);
        return success;

    }
    /**
     * 发送短信
     * @param phoneNumber
     * @param templateId
     * @param content
     * @return
     */
    public static boolean sendKchMobileCode(String phoneNumber,String templateId,String[] content){
        boolean success = false;
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(Config.serverIp, Config.serverPort);
        sdk.setAccount(Config.accountSId, Config.accountToken);
        sdk.setAppId(Config.kchAppId);
        sdk.setBodyType(BodyType.Type_JSON);
        HashMap<String, Object>  result = sdk.sendTemplateSMS(phoneNumber, templateId, content);
        success=doResult(result);
        return success;
    }

    /**
     * 其他系统发送短信
     * @param templateId
     * @param content
     * @return
     */
    public static boolean sendOtherSystemCode(String appId,String templateId,String phoneNumber,String[] content){
        boolean success = false;
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(Config.serverIp, Config.serverPort);
        sdk.setAccount(Config.accountSId, Config.accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        HashMap<String, Object>  result = sdk.sendTemplateSMS(phoneNumber, templateId, content);
        success=doResult(result);
        return success;
    }


    /**
     * 发送运维短信
     * @param phone
     * @param content
     * @return
     */
    public static boolean sendDefend(String phone ,String content){
        String [] c = {content};
        return sendMsgTemplate(phone, TemplateID.template_defend.getValue(),c);
    }

    /**
     * @param phone  手机号码
     * @param template 模板
     * @param content  发送内容参数{1}、{2}、{3}
     */
    public static boolean sendMsgTemplate(String phone,String template,String ...content){
        HashMap<String,Object> result=send(phone,template,content);
        boolean send=doResult(result);
        return send;
    }

    public static boolean send(String appId,String phoneNumber,String templateId,String[] content,boolean product){
		boolean success = false;
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(Config.serverIp, Config.serverPort);
        sdk.setAccount(Config.accountSId, Config.accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        HashMap<String, Object>  result = sdk.sendTemplateSMS(phoneNumber, templateId, content);
        success=doResult(result);
		return success;

    }

    private static HashMap<String, Object> send(String phone, String template, String ...content){
        HashMap<String,Object> result=null;
        try {
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(Config.serverIp, Config.serverPort);
            sdk.setAccount(Config.accountSId, Config.accountToken);
            sdk.setAppId(Config.appId);
            sdk.setBodyType(BodyType.Type_JSON);
            result = sdk.sendTemplateSMS(phone, template, content);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发送短信异常",e.getMessage());
        }
        return result;
    }

    private static boolean doResult(HashMap<String,Object> result){
        boolean success=false;
        if(result!=null){
            String statusCode=(String)result.get("statusCode");
            if(StringUtils.equalsIgnoreCase(statusCode, Config.statusCode)){
                success=true;
                //正常返回输出data包体信息（map）
                /*HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for(String key:keySet){
                    Object object = data.get(key);
                    System.out.println(key +" = "+object);
                }*/
            }else{
                //异常返回输出错误码和错误信息
               String statusMsg=(String)result.get("statusMsg");
               logger.error("发送短信失败","错误码=" + statusCode +" 错误信息= "+statusMsg);
               success=false;
            }
        }
        System.out.println(success);
        return success;
    }


    public final class Config {

        public final static  String serverIp="app.cloopen.com";
        public final static String serverPort="8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        public final static String accountSId = "";
        public final static String accountToken = "";
        public final static String appId = "";
        public final static String kchAppId = "";



        //状态码
        public final static String statusCode="000000";
    }
}

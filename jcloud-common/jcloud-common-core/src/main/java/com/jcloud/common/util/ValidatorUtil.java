package com.jcloud.common.util;

import com.jcloud.common.domain.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.regex.Pattern;

public class ValidatorUtil {


    public static final Pattern EMAIL_PATTERN =Pattern.compile("\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b");

    public static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("(\\d{3}-\\d{8})|(\\d{8})|(\\d{11})|(\\d{14})");

    public static final Pattern URL_PATTERN = Pattern.compile ("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");

    /**
     * 是否Email
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {

        if (!StringUtils.isBlank(value))
        {
            if (EMAIL_PATTERN.matcher(value).matches())
            {
                return true ;
            }
        }

        return false ;
    }

    /**
     * 是否手机号
     * @param value
     * @return
     */
    public static boolean isMobilePhone(String value) {

        if (!StringUtils.isBlank(value))
        {
            if (MOBILE_PHONE_PATTERN.matcher(value).matches())
            {
                return true ;
            }
        }

        return false ;
    }
    /**
     * 是否地址
     * @param value
     * @return
     */
    public static boolean isUrl(String value) {

        if (!StringUtils.isBlank(value))
        {
            if (URL_PATTERN.matcher(value).matches())
            {
                return true ;
            }
        }

        return false ;
    }

    public static ResponseData formValidateResult(BindingResult bindingResult) {
        List<ObjectError> error = bindingResult.getAllErrors();
        ResponseData responseResult = new ResponseData();
        if (error.size() > 0) {
            responseResult.setMsg(((ObjectError)error.get(0)).getDefaultMessage());
            return responseResult;
        } else {
            return null;
        }
    }
}

package com.jcloud.common.bean;

import lombok.Data;

/**
 * @author jiaxm
 * @date 2021/9/6
 */
@Data
public class ApiLimit {

    private Long apiLimit = Integer.valueOf(Integer.MAX_VALUE).longValue();

    private String apiPath;

    private Integer apiLimitStrategy = 0;

    private String serviceId;

}

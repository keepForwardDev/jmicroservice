package com.jcloud.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 获取swagger 资源文档
 * @author jiaxm
 * @date 2021/9/3
 */
@ApiIgnore
@RequestMapping("document")
@RestController
public class SwaggerResourceController {

    @Autowired
    private SwaggerResourceService swaggerResourceService;

    @RequestMapping("resource")
    public SwaggerResourceBean getResource(String swaggerGroup) {
        return swaggerResourceService.getSwaggerResource(swaggerGroup);
    }
}

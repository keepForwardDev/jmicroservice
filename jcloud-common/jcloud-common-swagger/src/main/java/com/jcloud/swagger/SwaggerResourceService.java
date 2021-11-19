package com.jcloud.swagger;

import com.jcloud.common.bean.TreeNode;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Service
public class SwaggerResourceService {

    @Autowired
    private DocumentationCache documentationCache;

    @Autowired
    private ServiceModelToSwagger2Mapper mapper;

    public SwaggerResourceBean getSwaggerResource(String swaggerGroup) {
        SwaggerResourceBean bean = new SwaggerResourceBean();
        String groupName = (String) Optional.ofNullable(swaggerGroup).orElse("default");
        Documentation documentation = this.documentationCache.documentationByGroup(groupName);
        Swagger swagger = this.mapper.mapDocumentation(documentation);
        // 将所有的api 归类到 tag组下
        Map<String, TreeNode> parentNodeMap = new LinkedHashMap<>();
        //Map<String, TreeNode> pathNodeMap= new LinkedHashMap<>();
        long index = 0;
        for (Tag tag : swagger.getTags()) {
            TreeNode treeNode = new TreeNode();
            treeNode.setLabel(tag.getName());
            treeNode.setId(index);
            parentNodeMap.put(tag.getName(), treeNode);
            index++;
        }
        for (String path : swagger.getPaths().keySet()) {
            Path p = swagger.getPaths().get(path);
            Operation operation = p.getOperations().get(0);
            for (String tag : operation.getTags()) {
                TreeNode parentNode = parentNodeMap.get(tag);
                TreeNode treeNode = new TreeNode(false);
                treeNode.setLabel(operation.getSummary());
                treeNode.setId(index);
                treeNode.setExtra(path);
                parentNode.getChildren().add(treeNode);
                //pathNodeMap.put(path, treeNode);
                index++;
            }
        }
        bean.setResourceTree(parentNodeMap.values());
        // bean.setPathMap(pathNodeMap);
        return bean;
    }
}

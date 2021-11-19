package com.jcloud.admin.service.impl;

import cn.hutool.core.io.IoUtil;
import com.jcloud.common.bean.ApiLimit;
import com.jcloud.common.bean.TreeNode;
import com.jcloud.common.exception.BizException;
import com.jcloud.common.util.JsonUtils;
import com.jcloud.common.util.TreeNodeUtil;
import com.jcloud.swagger.SwaggerResourceBean;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author jiaxm
 * @date 2021/9/3
 */
@Service
public class RemoteSwaggerService {

    @Autowired
    private Client client;

    public SwaggerResourceBean getResource(String serviceId) {
        String url = "http://" + serviceId + "/document/resource";
        Request request = Request.create(Request.HttpMethod.GET, url, new HashMap<>(), null, Charset.forName("UTF-8"), null);
        try {
            Response response = client.execute(request, new Request.Options());
            String result = IoUtil.read(response.body().asInputStream(),  Charset.forName("UTF-8"));
            if (result.startsWith("Load balancer")) {
                throw new BizException("服务未开启！");
            }
            SwaggerResourceBean swaggerResourceBean = JsonUtils.readObject(result, SwaggerResourceBean.class);
            Map<Object, TreeNode> pathMap = new LinkedHashMap<>();
            TreeNodeUtil.treeToMap(swaggerResourceBean.getResourceTree(), ReflectionUtils.findField(TreeNode.class, "extra"), pathMap);
            swaggerResourceBean.setPathMap(pathMap);
            return swaggerResourceBean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 设置api limit,排除不存在得api
     * @param apiLimit
     * @param swaggerResourceBean
     */
    public List<Long> formatApiLimit(List<ApiLimit> apiLimit, SwaggerResourceBean swaggerResourceBean) {
        Map<Object, TreeNode> pathMap = swaggerResourceBean.getPathMap();
        pathMap.forEach((k, v) -> {
            ApiLimit apiLimit1 = new ApiLimit();
            apiLimit1.setApiPath(k.toString());
            v.setData(apiLimit1);
        });
        ListIterator<ApiLimit> listIterator = apiLimit.listIterator();
        List<Long> selectIndex = new ArrayList<>();
        while (listIterator.hasNext()) {
            ApiLimit labelNode = listIterator.next();
            TreeNode resourceNode = pathMap.get(labelNode.getApiPath());
            if (resourceNode != null) {
                resourceNode.setData(labelNode); // 设置api limit
                selectIndex.add(resourceNode.getId());
            } else {
                listIterator.remove();
            }
        }

        return selectIndex;
    }

}

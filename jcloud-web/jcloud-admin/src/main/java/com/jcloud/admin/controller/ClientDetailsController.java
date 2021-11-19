package com.jcloud.admin.controller;

import com.jcloud.admin.bean.AppDetail;
import com.jcloud.admin.entity.ClientDetails;
import com.jcloud.admin.service.ClientDetailsService;
import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.orm.domain.DataBasePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author jiaxm
 * @date 2021/3/26
 */
@Api(tags = "应用")
@RestController
@RequestMapping("clientDetails")
public class ClientDetailsController {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @ApiOperation("根据app_key获取凭证")
    @RequestMapping(value = "getClientDetails/{clientId}", method = RequestMethod.GET)
    public ResponseData<ClientDetails> getClientDetails(@PathVariable String clientId) {
        ResponseData commonRespon = new ResponseData();
        ClientDetails details = clientDetailsService.getClientDetails(clientId);
        if (details == null) {
            return commonRespon;
        } else {
            commonRespon.setCode(Const.CODE_SUCCESS);
            commonRespon.setMsg(Const.CODE_SUCCESS_STR);
            commonRespon.setData(details);
        }
        return commonRespon;
    }

    @ApiOperation("获取应用列表")
    @RequestMapping(value = "pageList", method = RequestMethod.GET)
    public ResponseData<CommonPage<AppDetail>> pageList(@ApiIgnore AppDetail detail, DataBasePage commonPage) {
        return clientDetailsService.pageList(commonPage, detail);
    }

    @ApiOperation("保存")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseData save(AppDetail detail) {
        clientDetailsService.saveEntity(detail);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation("禁用")
    @RequestMapping(value = "disable/{id}", method = RequestMethod.GET)
    public ResponseData disable(@PathVariable Long id) {
        clientDetailsService.logicDelete(id);
        return ResponseData.getSuccessInstance();
    }

    @ApiOperation("删除")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public ResponseData delete(@PathVariable Long id) {
        clientDetailsService.physicsDelete(id);
        return ResponseData.getSuccessInstance();
    }
}

package com.hywx.authservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hywx.authservice.dao.OAuthClientDetails;
import com.hywx.authservice.service.OAuthClientDetailsService;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.results.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @program: auth-service
 * @description: 客户端管理
 * @author: tangjing
 * @create: 2020-03-09 09:26
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("client")
public class OAuthClientDetailsController {

    @Autowired
    private OAuthClientDetailsService oAuthClientDetailsService;

    @GetMapping("check/{clientId}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OAuthClientDetails client = this.oAuthClientDetailsService.findById(clientId);
        return client == null;
    }

    @GetMapping("secret/{clientId}")
    @PreAuthorize("hasAuthority('client:decrypt')")
    public Resp getOriginClientSecret(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OAuthClientDetails client = this.oAuthClientDetailsService.findById(clientId);
        String origin = client != null ? client.getOriginSecret() : StringUtils.EMPTY;
        return  Resp.getInstantiationSuccess("分页查看", Resp.LIST, origin);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('client:view')")
    public Resp oauthCliendetailsList(QueryRequest queryRequest, OAuthClientDetails oAuthClientDetails) {
        //查询列表数据
        long startTime = System.currentTimeMillis();
        if (queryRequest.getPageNo() < 1 || queryRequest.getPageNo() < 1) {
            return Resp.getInstantiationError("分页查看失败，分页页数或分页大小不合法", null, null);
        }
        try {
            IPage<OAuthClientDetails> userList = oAuthClientDetailsService.getListByPage(queryRequest, oAuthClientDetails);
            long endTime = System.currentTimeMillis();
            log.info("分页列举成功，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationSuccess("分页查看", Resp.LIST, userList);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("分页列举失败，原因：" + e.getMessage() + "，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError("分页查看失败" + e.getMessage(), Resp.LIST, null);
        }
    }


    @PostMapping
    @PreAuthorize("hasAuthority('client:add')")
    public Resp addOauthCliendetails(@RequestBody @Valid OAuthClientDetails oAuthClientDetails){
        long startTime = System.currentTimeMillis();
        try {
            this.oAuthClientDetailsService.createOAuthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "新增客户端失败";
            log.error(message, e);
            long endTime = System.currentTimeMillis();
            // throw new FebsException(message);
            // LOGGER.error("分页列举失败，原因："+ e.getMessage()+"，用时" + (endTime - startTime) + "ms");
            return Resp.getInstantiationError(message, null, null);
        }
        return  Resp.getInstantiationSuccess("新增客户端成功",null, null);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('client:delete')")
    public Resp deleteOauthCliendetails(@NotBlank(message = "{required}") String clientIds)  {
        try {
            this.oAuthClientDetailsService.deleteOAuthClientDetails(clientIds);

        } catch (Exception e) {
            String message = "删除客户端失败";
            log.error(message, e);
            return Resp.getInstantiationError(message, null, null);
        }
        return  Resp.getInstantiationSuccess("删除客户端成功",null, null);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    public Resp updateOauthCliendetails(@RequestBody @Valid OAuthClientDetails oAuthClientDetails)  {
        try {
            this.oAuthClientDetailsService.updateOAuthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "修改客户端失败";
            log.error(message, e);
            return Resp.getInstantiationError(message, null, null);
        }
        return  Resp.getInstantiationSuccess("修改客户端成功",null, null);
    }
}

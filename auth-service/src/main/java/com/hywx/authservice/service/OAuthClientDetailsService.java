package com.hywx.authservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.authservice.dao.OAuthClientDetails;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.Exception.CommonException;

/**
 * @program: auth-service
 * @description:
 * @author: tangjing
 * @create: 2020-03-09 09:27
 **/
public interface OAuthClientDetailsService extends IService<OAuthClientDetails> {

    /**
     * 查询（分页）
     *
     * @param request            QueryRequest
     * @param oauthClientDetails oauthClientDetails
     * @return IPage<OAuthClientDetails>
     */
    IPage<OAuthClientDetails> getListByPage(QueryRequest request, OAuthClientDetails oauthClientDetails);

    /**
     * 根据主键查询
     *
     * @param clientId clientId
     * @return OAuthClientDetails
     */
    OAuthClientDetails findById(String clientId);

    /**
     * 新增
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void createOAuthClientDetails(OAuthClientDetails oauthClientDetails) throws CommonException;

    /**
     * 修改
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void updateOAuthClientDetails(OAuthClientDetails oauthClientDetails);

    /**
     * 删除
     *
     * @param clientIds clientIds
     */
    void deleteOAuthClientDetails(String clientIds);
}


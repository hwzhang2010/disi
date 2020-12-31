package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.userservice.bo.VueRouter;
import com.hywx.userservice.dao.Resource;

import java.util.List;

/**
 * @program: user-serice
 * @description:  service 接口
 * @author tangjing
 * @date 2020-03-05
 */
public interface ResourceService extends IService<Resource> {


    List<Resource> list(QueryWrapper<Resource> resource);

    IPage<Resource> getListByPage(int pageNo, int pageSize, QueryWrapper<Resource> resource);

    int saveData(Resource resource);

    int updateData(Resource resource);

    int deleteById(String id);

    int batchDelete(String[] ids);

    Object getResourceTree(Resource resource);

    List<VueRouter<Resource>> getUserRouters(String username);

    String findUserPermissions(String username);

    List<VueRouter<Resource>> getUserRouters(String username, String parentId);

    String findUserPermissions(String username, String parentId);

    String findUserSystemPermissions(String username, String type);
}
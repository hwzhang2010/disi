package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.common.core.util.UuidTool;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.bo.ResourceTree;
import com.hywx.userservice.bo.RouterMeta;
import com.hywx.userservice.bo.Tree;
import com.hywx.userservice.bo.VueRouter;
import com.hywx.userservice.dao.Resource;
import com.hywx.userservice.mapper.ResourceMapper;
import com.hywx.userservice.service.ResourceService;
import com.hywx.userservice.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: user-serice
 * @description:  service 接口实现类
 * @author tangjing
 * @date 2020-03-05
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

        @Autowired
        private ResourceMapper resourceMapper;

    @Override
    public String findUserPermissions(String username) {
        List<Resource> userPermissions = resourceMapper.findUserPermissions(username,"");
        return userPermissions.stream().map(Resource::getCode).collect(Collectors.joining(","));
    }

    @Override
    public String findUserSystemPermissions(String username,String type) {
        List<Resource> userPermissions = resourceMapper.findUserPermissions(username,type);
        return userPermissions.stream().map(Resource::getCode).collect(Collectors.joining(","));
    }

    @Override
    public String findUserPermissions(String username,String parentId) {
        List<Resource> userPermissions = resourceMapper.findUserPermissionsByParentId(username,parentId);
        return userPermissions.stream().map(Resource::getCode).collect(Collectors.joining(","));
    }

    @Override
    public List<VueRouter<Resource>> getUserRouters(String username) {
        List<VueRouter<Resource>> routes = new ArrayList<>();
        List<Resource> menus = resourceMapper.findUserMenus(username);
        menus.forEach(menu -> {
            VueRouter<Resource> route = new VueRouter<>();
            route.setId(menu.getId());
            route.setParentId(menu.getParentId());
            route.setPath(menu.getUrl());
            route.setComponent(menu.getComponent());
            route.setName(menu.getName());
            route.setMeta(new RouterMeta(menu.getName(), menu.getIcon(), true));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }

    @Override
    public List<VueRouter<Resource>> getUserRouters(String username,String parentId) {
        List<VueRouter<Resource>> routes = new ArrayList<>();
        List<Resource> menus = resourceMapper.findUserMenusByParentId(username,parentId);
        menus.forEach(menu -> {
            VueRouter<Resource> route = new VueRouter<>();
            route.setId(menu.getId());
            if(menu.getParentId().equals(parentId)) {
                route.setParentId("0");
            }else
            {
                route.setParentId(menu.getParentId());
            }
            route.setPath(menu.getUrl());
            route.setComponent(menu.getComponent());
            route.setName(menu.getName());
            route.setMeta(new RouterMeta(menu.getName(), menu.getIcon(), true));
            routes.add(route);
        });
        return TreeUtil.buildVueRouter(routes);
    }

    @Override
        public Object getResourceTree(Resource resource){
            Map<String, Object> result = new HashMap<>();
            try {
                LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.orderByAsc(Resource::getOrderNum);
                List<Resource> menus = baseMapper.selectList(queryWrapper);

                List<ResourceTree> trees = new ArrayList<>();
                buildTrees(trees, menus);

                List<? extends Tree<?>> menuTree = TreeUtil.build(trees);

                return menuTree;
            } catch (NumberFormatException e) {
                log.error("查询菜单失败", e);
                return null;
            }

        }

        @Override
        public List<Resource> list(QueryWrapper<Resource> resource){
            return resourceMapper.selectList(resource);
        }

        @Override
        public IPage<Resource> getListByPage(int pageNo, int pageSize,QueryWrapper<Resource> resource){
            IPage<Resource>  list = resourceMapper.selectPage(new Page<>(pageNo, pageSize),resource);
            return list;
        }

        @Override
        public int saveData(Resource resource){
            if(StringUtils.isEmpty(resource.getParentId())){
                resource.setParentId("0");
            }
            resource.setId(UuidTool.getUUID());
            resource.setCreateTime(new Date());
            resource.setCreateUser(SecurityUtil.getCurrentUser().getId());
            return resourceMapper.insert(resource);
        }

        @Override
        public int updateData(Resource resource){
            resource.setUpdateTime(new Date());
            resource.setUpdateUser(SecurityUtil.getCurrentUser().getId());
            return resourceMapper.updateById(resource);
        }

        @Override
        public int deleteById(String id){
            return resourceMapper.deleteById(id);
        }

        @Override
        public int batchDelete(String[] ids){
            List<String> list = Arrays.asList(ids);
            return resourceMapper.deleteBatchIds(list);
        }

        private void buildTrees(List<ResourceTree> trees, List<Resource> menus) {
            menus.forEach(menu -> {
                ResourceTree tree = new ResourceTree();
                tree.setId(menu.getId());
                tree.setParentId(menu.getParentId().toString());
                tree.setLabel(menu.getName());
                tree.setComponent(menu.getComponent());
                tree.setIcon(menu.getIcon());
                tree.setOrderNum(menu.getOrderNum());
                tree.setPath(menu.getUrl());
                tree.setType(menu.getType());
                tree.setCode(menu.getCode());
                trees.add(tree);
            });
        }
}


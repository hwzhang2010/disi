package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.SortUtil;
import com.hywx.common.core.util.UuidTool;
import com.hywx.common.security.utils.SecurityUtil;
import com.hywx.userservice.bo.OrganizationTree;
import com.hywx.userservice.bo.Tree;
import com.hywx.userservice.dao.Organization;
import com.hywx.userservice.mapper.OrganizationMapper;
import com.hywx.userservice.service.OrganizationService;
import com.hywx.userservice.util.*;
import com.hywx.userservice.vo.OrganizationSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: user-serice
 * @description:  service 接口实现类
 * @author tangjing
 * @date 2020-03-05
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

        @Autowired
        private OrganizationMapper organizationMapper;


        @Override
        public List<Organization> getOrganizationlist(OrganizationSearchVo searchVo){
            QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotBlank(searchVo.getOrgName()))
                queryWrapper.lambda().like(Organization::getOrganizationName, searchVo.getOrgName());
            if (StringUtils.isNotBlank(searchVo.getStartTime()) && StringUtils.isNotBlank(searchVo.getStopTime()))
                queryWrapper.lambda()
                        .ge(Organization::getCreateTime, searchVo.getStartTime())
                        .le(Organization::getCreateTime, searchVo.getStopTime());
            QueryRequest request=new QueryRequest();
            SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", CommonConstant.ORDER_ASC, true);
            return organizationMapper.selectList(queryWrapper);
        }

        @Override
        public IPage<Organization> getListByPage(int pageNo, int pageSize,QueryWrapper<Organization> organization){
            IPage<Organization>  list = organizationMapper.selectPage(new Page<>(pageNo, pageSize),organization);
            return list;
        }


        @Override
        public int saveData(Organization organization){
            organization.setId(UuidTool.getUUID());
            organization.setCreateTime(new Date());
            organization.setCreateUser(SecurityUtil.getCurrentUser().getId());
            return organizationMapper.insert(organization);
        }

        @Override
        public int updateData(Organization organization){
            if(StringUtils.isNotBlank(organization.getParentId())){
                organization.setParentId("0");
            }
            organization.setUpdateUser(SecurityUtil.getCurrentUser().getId());
            organization.setUpdateTime(new Date());
            return organizationMapper.updateById(organization);
        }

        @Override
        public int deleteById(String id){
            return organizationMapper.deleteById(id);
        }

        @Override
        public int batchDelete(String[] ids){
            List<String> list = Arrays.asList(ids);
            return organizationMapper.deleteBatchIds(list);
        }

        @Override
        public Object getOrganizationTree(OrganizationSearchVo searchVo) {
            try {
                List<Organization> orgs = getOrganizationlist(searchVo);
                List<OrganizationTree> trees = new ArrayList<>();
                buildTrees(trees, orgs);
                List<? extends Tree<?>> orgTree = TreeUtil.build(trees);
                return  orgTree;
            } catch (Exception e) {
                log.error("获取部门列表失败", e);
                return  null;
            }
        }

        private void buildTrees(List<OrganizationTree> trees, List<Organization> orgs) {
            orgs.forEach(org -> {
                OrganizationTree tree = new OrganizationTree();
                tree.setId(org.getId().toString());
                tree.setParentId(org.getParentId().toString());
                tree.setLabel(org.getOrganizationName());
                tree.setOrderNum(org.getOrderNum());
                trees.add(tree);
            });
        }
}


package com.hywx.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hywx.userservice.dao.Organization;
import com.hywx.userservice.vo.OrganizationSearchVo;

import java.util.List;

/**
 * @program: user-serice
 * @description:  service 接口
 * @author tangjing
 * @date 2020-03-05
 */
public interface OrganizationService extends IService<Organization> {



    IPage<Organization> getListByPage(int pageNo, int pageSize, QueryWrapper<Organization> organization);

    int saveData(Organization organization);

    int updateData(Organization organization);

    int deleteById(String id);

    int batchDelete(String[] ids);

    List<Organization> getOrganizationlist(OrganizationSearchVo searchVo);

    Object getOrganizationTree(OrganizationSearchVo searchVo);
}
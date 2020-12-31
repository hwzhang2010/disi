package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hywx.userservice.dao.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: user-serice
 * @description: 角色表 Mapper 接口
 * @author tangjing
 * @date 2020-02-24
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查找角色详情
     *
     * @param page 分页
     * @param role 角色
     * @return IPage<User>
     */
    IPage<Role> findRolePage(Page page, @Param("role") Role role);

}


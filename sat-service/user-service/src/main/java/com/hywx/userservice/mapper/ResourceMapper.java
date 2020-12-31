package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.userservice.dao.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: user-serice
 * @description:  Mapper 接口
 * @author tangjing
 * @date 2020-03-05
 */
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> findUserPermissions(@Param("userName") String userName, @Param("type") String type);

    List<Resource> findUserMenus(String userName);

    List<Resource> findUserPermissionsByParentId(@Param("userName") String userName, @Param("parentId") String parentId);

    List<Resource> findUserMenusByParentId(@Param("userName") String userName, @Param("parentId") String parentId);

}


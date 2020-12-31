package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hywx.userservice.dao.User;
import com.hywx.userservice.vo.UserSearchVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: user-serice
 * @description: UserMapper接口类
 * @author: tangjing
 * @create: 2020-02-24 09:49
 */
@Repository
public interface UserMapper extends BaseMapper<User> {


    IPage<User> getUserListPage(Page page, @Param("searchVo") UserSearchVo searchVo);

    List<User> getUserListPage(@Param("searchVo") UserSearchVo searchVo);
}


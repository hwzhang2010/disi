package com.hywx.zuulservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.zuulservice.dao.User;
import org.apache.ibatis.annotations.Param;
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

}


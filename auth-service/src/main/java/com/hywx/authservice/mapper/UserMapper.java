package com.hywx.authservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.authservice.dao.Resource;
import com.hywx.authservice.dao.User;
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
    /**
     * 根据用户名查询用户
     * @param uname
     * @return
     */
    User findUserByUname(String uname);

    /**根据用户id查询拥有的页面资源*/
    List<Resource> getResourceListByUserId(@Param("userId") String userId);
}


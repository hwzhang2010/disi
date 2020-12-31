package com.hywx.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hywx.userservice.dao.UserConnection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: user-serice
 * @description: 第三方用户登录 Mapper 接口
 * @author lxy
 * @date 2020-03-12
 */
@Repository
public interface UserConnectionMapper extends BaseMapper<UserConnection> {

    /**
     * 根据系统用户名修改
     * @param userConnection
     * @return
     */
    int updateByUserName(@Param("userConnection") UserConnection userConnection);

    /**
     * 根据系统用户名删除
     * @param userName
     * @return
     */
    int deleteByUserName(@Param("userName") String userName);
}


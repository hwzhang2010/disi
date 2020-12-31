package com.hywx.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hywx.common.core.entity.QueryRequest;
import com.hywx.common.core.util.CommonConstant;
import com.hywx.common.core.util.CommonUtil;
import com.hywx.common.core.util.SortUtil;
import com.hywx.common.core.util.UuidTool;
import com.hywx.userservice.dao.LoginLog;
import com.hywx.userservice.mapper.LoginLogMapper;
import com.hywx.userservice.service.LoginLogService;
import com.hywx.userservice.util.AddressUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: user-serice
 * @description: 登录日志表 service 接口实现类
 * @author tangjing
 * @date 2020-03-06
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

        @Autowired
        private LoginLogMapper loginLogMapper;


        @Override
        public List<LoginLog> list(QueryWrapper<LoginLog> loginLog){
            return loginLogMapper.selectList(loginLog);
        }

        @Override
        public IPage<LoginLog> getListByPage(QueryRequest queryRequest, LoginLog loginLog){
            QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(loginLog.getUsername()))
                queryWrapper.lambda().like(LoginLog::getUsername, loginLog.getUsername());
            if (StringUtils.isNotBlank(loginLog.getLoginTimeFrom()) && StringUtils.isNotBlank(loginLog.getLoginTimeTo()))
                queryWrapper.lambda()
                        .ge(LoginLog::getLoginTime, loginLog.getLoginTimeFrom())
                        .le(LoginLog::getLoginTime, loginLog.getLoginTimeTo());
            Page<LoginLog> page = new Page<>(queryRequest.getPageNo(), queryRequest.getPageSize());
            SortUtil.handlePageSort(queryRequest, page, "loginTime", CommonConstant.ORDER_DESC, true);

            return this.page(page, queryWrapper);
        }


        @Override
        public boolean saveData(LoginLog loginLog){
             loginLog.setId(UuidTool.getUUID());
             loginLog.setLoginTime(new Date());
             String ip = CommonUtil.getHttpServletRequestIpAddress();
             loginLog.setIp(ip);
             loginLog.setLocation(AddressUtil.getCityInfo(ip));
            return  save(loginLog);
        }

        @Override
        public int updateData(LoginLog loginLog){
            return loginLogMapper.updateById(loginLog);
        }

        @Override
        public int deleteById(String id){
            return loginLogMapper.deleteById(id);
        }

        @Override
        public int batchDelete(List<String> ids){
        return loginLogMapper.deleteBatchIds(ids);
        }
}


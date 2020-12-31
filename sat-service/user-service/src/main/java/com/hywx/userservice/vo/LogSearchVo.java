package com.hywx.userservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @program: gw-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-03-17 09:07
 **/
@Data
public class LogSearchVo {
    /**
     * 操作用户
     */
    private String username;

    /**
     * 操作内容
     */
    private String operation;

    //时间
    //开始时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeFrom;

    //结束时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeTo;
}

package com.hywx.common.core.util;

import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

/**
 * @program: gw-cloud
 * @description: Execl导出时间类型字段格式化
 * @author: tangjing
 * @create: 2020-03-17 15:33
 **/
@Slf4j
public class TimeConverter implements WriteConverter {
    @Override
    public String convert(Object value) {
        if (value == null)
            return "";
        else {
            try {
//                System.out.println("日期"+ TimeUtil.formatCSTTime(value.toString(), TimeUtil.FORMAT_STR_LONG));
                return TimeUtil.formatCSTTime(value.toString(), TimeUtil.FORMAT_STR_LONG);
            } catch (ParseException e) {
                String message = "时间转换异常";
                log.error(message, e);
                throw new ExcelKitWriteConverterException(message);
            }
        }
    }
}
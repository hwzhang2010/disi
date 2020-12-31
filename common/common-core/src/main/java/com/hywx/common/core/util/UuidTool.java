package com.hywx.common.core.util;

import java.util.UUID;

/**
 * @program: gw-cloud
 * @description: 生成唯一id
 * @author: tangjing
 * @create: 2020-03-06 13:30
 **/
public class UuidTool {
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    public static String[] getUUID(int number){
        if(number < 1){
            return null;
        }
        String[] retArray = new String[number];
        for(int i=0;i<number;i++){
            retArray[i] = getUUID();
        }
        return retArray;
    }
}
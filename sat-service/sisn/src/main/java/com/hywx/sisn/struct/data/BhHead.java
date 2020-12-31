package com.hywx.sisn.struct.data;

import com.hywx.sisn.struct.StructClass;
import com.hywx.sisn.struct.StructField;

/**
 * 协议帧头
 * ver : 版本号
 * mid : 当前任务标识
 * sid ：  信源
 * did :  信宿
 * bid :  数据类型
 * no :  包序号
 * flag :  信息标志
 * res :  保留
 * jd :  积日
 * js : 积秒
 * len : 数据包长度
 */

@StructClass
public class BhHead {

    @StructField(order = 0)
    public  byte      bVer=(byte)(0x80);               // 协议版本，用b7、b6"10"表示PDXP,其余位保留
    @StructField(order = 1)
    public  short    wMid;               // 当前任务标识，由系统指定(卫星ID)
    @StructField(order = 2)
    public  int      dwSid=Integer.parseInt("00010000", 16);              // 数据发送的源地址
    @StructField(order = 3)
    public  int     dwDid;              // 数据发送的目的地址
    @StructField(order = 4)
    public  int      dwBid;              // 数据类型标识
    @StructField(order = 5)
    public  int      dwNo;               // 包序号，循环计数
    @StructField(order = 6)
    public  byte      wFlag;              // 标志区
    @StructField(order = 7)
    public  int      iRes;               // 保留字
    @StructField(order = 8)
    public  short    wJd;                // 积日
    @StructField(order = 9)
    public  int      dwJs;               // 积秒
    @StructField(order = 10)
    public  short    wLen;               // 数据包的长度
}

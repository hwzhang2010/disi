package com.hywx.sirs.bo.bid;

import com.hywx.sirs.bo.StationData;

/**
 * 不同数据所属的BID：目前对应5种类型 
 * 1.0x00200004 -- 轨道根数
 * 2.0x00007600 -- 外测测距测速
 * 3.0x00007E00 -- 外测测角
 * 4.0x0000f100 -- GNSS
 * 5.0x00009000 -- 遥测
 * 6.0x00000030 -- 故障
 * 7.0x00000601 -- 远控
 * 8.0x00000605 -- 站网计划
 * 
 * @author zhang.huawei
 *
 */
public abstract class AbstractBidOperation {
    //不同BID对数据的处理
	public abstract void operate(StationData stationData);
}

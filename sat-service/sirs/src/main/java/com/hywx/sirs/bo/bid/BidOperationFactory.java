package com.hywx.sirs.bo.bid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hywx.sirs.global.GlobalConstant;

/**
 *  简单工厂模式(静态工厂模式)：根据注册的参数，动态决定使用哪个BID
 * 不同数据所属的BID：
 * 1.0x00200004 -- 轨道根数
 * 2.0x00000076 -- 外测测距测速
 * 3.0x0000007E -- 外测测角
 * 4.0x0000f100 -- GNSS
 * 5.0x00000090 -- 遥测
 * 6.0x00000601 -- 远控
 * 
 * @author zhang.huawei
 *
 */
@Component
public class BidOperationFactory {
	private static Map<Long, AbstractBidOperation> bidMap = new HashMap<>();
	private static void register(Long key, AbstractBidOperation bid) {
		bidMap.put(key, bid);
	}
	
	/**
	 * 注册BID类型
	 */
	static {
        // BID, 遥测源码
        register(GlobalConstant.BID_TM, new TmBidOperation());
        // BID, 测距测速
        register(GlobalConstant.BID_RANGE, new ExtRangeBidOperation());
        // BID, 测角
        register(GlobalConstant.BID_ANGLE, new ExtAngleBidOperation());
    	// BID, 轨道根数
        register(GlobalConstant.BID_ORBIT_ELEM, new OrbitElemBidOperation());
        // BID, 远控指令
        register(GlobalConstant.BID_RC, new RemoteControlBidOperation());
    }
	
	public static AbstractBidOperation getInstance(Long key) {
		//Assert.notNull(key, "bid can't be null");
		//return bidMap.get(key);
		
		if (bidMap.containsKey(key))
		    return bidMap.get(key);
		
		return null;
	}

}

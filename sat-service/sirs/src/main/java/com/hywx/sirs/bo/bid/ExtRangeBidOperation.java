package com.hywx.sirs.bo.bid;

import com.hywx.sirs.bo.StationData;
import com.hywx.sirs.global.GlobalMap;

/**
 * BID：外测测距测速
 * @author zhang.hauwei
 *
 */
public class ExtRangeBidOperation extends AbstractBidOperation {

	@Override
	public void operate(StationData data) {
		//当数据交互TCP客户端已经连接
		if (GlobalMap.getStationConnection(data.getStation())) {
		    //放入站的缓冲区
		    GlobalMap.putStationData(data);
		    //GlobalMap.getStationData(data.getStation());
		}
	}

}

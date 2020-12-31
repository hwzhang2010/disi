package com.hywx.sisl.util;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

import com.hywx.sisl.global.GlobalConstant;

/**
 * (发送)帧处理工具类
 * @author zhang.huawei
 *
 */
public class FrameUtil {
	// 帧头字节长度
	//private static final int FRAME_HEADER_LENGTH = 32;

	/**
	 * 根据地面站ID, 卫星ID, BID和字节长度填充外测数据帧头
	 * @param groundStationId
	 * @param satelliteId
	 * @param bid
	 * @param len
	 * @param isNet
	 * @return
	 */
	public static byte[] produceFrameHeader(String groundStationId, String satelliteId, long bid, int len, boolean isNet) {
		byte[] data = new byte[GlobalConstant.FRAME_HEADER_LENGTH];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//ver
		buffer.put((byte)0x0);
		//mid
		int mid = Integer.parseUnsignedInt(satelliteId, 16);
		buffer.put(ByteUtil.fromUShort2Bytes(mid, isNet));
		//sid
		groundStationId = groundStationId.startsWith("0x") ? groundStationId.substring(2) : groundStationId;
		long sid = Long.parseLong(groundStationId, 16);
		buffer.put(ByteUtil.fromUInt2Bytes(sid, isNet));
		//did
		buffer.put(ByteUtil.fromUInt2Bytes(GlobalConstant.DID, isNet));
		//bid
		buffer.put(ByteUtil.fromUInt2Bytes(bid, isNet));
		//no
		buffer.putInt(0);
		//flag
		buffer.put((byte)0x7E);
		//reserve
		buffer.putInt(0);
		//jd
		int jd = (int) TimeUtil.calJD(LocalDateTime.now());
		buffer.put(ByteUtil.fromUShort2Bytes(jd, isNet));
		//js
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromUInt2Bytes(js, isNet));
		//len
		buffer.put(ByteUtil.fromUShort2Bytes(len, isNet));
		
		return data;
	}
	
	public static ByteBuffer produceFrameOrbitElem(String groundStationId, String satelliteId, double[] orbitElem, boolean isNet) {
		ByteBuffer buffer = ByteBuffer.allocate(GlobalConstant.FRAME_HEADER_LENGTH + GlobalConstant.FRAME_ORBIT_ELEM_LENGTH);
		// 填充帧头
		buffer.put(produceFrameHeader(groundStationId, satelliteId, GlobalConstant.BID_ORBIT_ELEM, GlobalConstant.FRAME_ORBIT_ELEM_LENGTH, isNet));
		
		// 积日
		int jd = (int) TimeUtil.calJD(LocalDateTime.now());
		buffer.put(ByteUtil.fromUShort2Bytes(jd, isNet));
		// 积秒
		long js = TimeUtil.calJS(LocalDateTime.now());
		buffer.put(ByteUtil.fromDouble2Bytes(js / 10000.0, isNet));
		
		// 轨道半长轴 
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[0], isNet));
		// 偏心率
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[1], isNet));
		// 轨道倾角
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[2], isNet));
		// 升交点赤经
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[3], isNet));
		// 近地点幅角
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[4], isNet));
		// 平近点角
		buffer.put(ByteUtil.fromDouble2Bytes(orbitElem[5], isNet));
		
		// 大气阻尼摄动系数
		buffer.put(ByteUtil.fromDouble2Bytes(0, isNet));
		// 扩展字
		buffer.put(new byte[] { (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0 });
		// 光压摄动系数
		buffer.put(ByteUtil.fromDouble2Bytes(0, isNet));
		// 扩展字
		buffer.put(new byte[] { (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0 });
		
		return buffer;
	}
	
	
	
}

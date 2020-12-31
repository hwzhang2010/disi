package com.hywx.sisn.struct.data;

import com.hywx.sisn.struct.data.BhHead;

import java.nio.ByteBuffer;

import com.hywx.sisn.global.GlobalConstant;
import com.hywx.sisn.struct.StructClass;
import com.hywx.sisn.struct.StructField;

/**
 * 远程数据
 */
@StructClass
public class RemoteControlData {
    @StructField(order = 0)
    public BhHead bhHead;

    @StructField(order = 1)
    public byte[] data = new byte[GlobalConstant.FRAME_RC_LENGTH];
    
    public RemoteControlData() {
    }
    
    public RemoteControlData(BhHead bhHead, byte[] data){
        this.bhHead = bhHead;
        this.data = data;
    }

	public BhHead getBhHead() {
		return bhHead;
	}

	public void setBhHead(BhHead bhHead) {
		this.bhHead = bhHead;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
    
    

}

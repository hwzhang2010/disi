package com.hywx.sirs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConfig {
	//本地目录：信关站配置表目录
	@Value("${localpath.station}")
    private String localpathStation;
	
	//本地目录：UDP组播配置目录
	@Value("${localpath.multicast}")
	private String localpathMulticast;
	
	//本地文件名：信关站配置表文件
	@Value("${filename.station}")
	private String filenameStation;
	
	//本地文件名：UDP组播配置文件
	@Value("${filename.multicast}")
	private String filenameMulticast;
	
	public FileConfig() {
	}

	public String getLocalpathStation() {
		return localpathStation;
	}

	public String getLocalpathMulticast() {
		return localpathMulticast;
	}

	public String getFilenameStation() {
		return filenameStation;
	}

	public String getFilenameMulticast() {
		return filenameMulticast;
	}
	
	

}

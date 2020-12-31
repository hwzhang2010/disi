package com.hywx.siin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConfig {
	
	//本地目录：UDP组播配置目录
	@Value("${localpath.gps}")
	private String localpathGps;
	
	//本地文件名：UDP组播配置文件
	@Value("${filename.gps}")
	private String filenameGps;
	
	public FileConfig() {
	}

	public String getLocalpathGps() {
		return localpathGps;
	}

	public String getFilenameGps() {
		return filenameGps;
	}
	
	

}

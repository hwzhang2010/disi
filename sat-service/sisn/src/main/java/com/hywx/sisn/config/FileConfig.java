package com.hywx.sisn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConfig {
	//本地目录：UDP组播配置目录
	@Value("${localpath.multicast}")
	private String localpathMulticast;
	
	//本地文件名：UDP组播配置文件
	@Value("${filename.multicast}")
	private String filenameMulticast;
	
	
	public FileConfig() {
	}

	public String getLocalpathMulticast() {
		return localpathMulticast;
	}

	public void setLocalpathMulticast(String localpathMulticast) {
		this.localpathMulticast = localpathMulticast;
	}

	public String getFilenameMulticast() {
		return filenameMulticast;
	}

	public void setFilenameMulticast(String filenameMulticast) {
		this.filenameMulticast = filenameMulticast;
	}
	

}

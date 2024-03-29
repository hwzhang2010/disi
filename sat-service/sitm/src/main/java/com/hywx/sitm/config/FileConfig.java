package com.hywx.sitm.config;

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
	
	//本地目录：遥测配置目录
	@Value("${localpath.tm}")
	private String localpathTm;
	
	//本地文件名：遥测配置文件
	@Value("${filename.tm}")
	private String filenameTm;
	
	//本地目录：UDP组播配置目录
	@Value("${localpath.gps}")
	private String localpathGps;
	
	//本地文件名：UDP组播配置文件
	@Value("${filename.gps}")
	private String filenameGps;
	
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

	public String getLocalpathTm() {
		return localpathTm;
	}

	public void setLocalpathTm(String localpathTm) {
		this.localpathTm = localpathTm;
	}

	public String getFilenameTm() {
		return filenameTm;
	}

	public void setFilenameTm(String filenameTm) {
		this.filenameTm = filenameTm;
	}

	public String getLocalpathGps() {
		return localpathGps;
	}

	public void setLocalpathGps(String localpathGps) {
		this.localpathGps = localpathGps;
	}

	public String getFilenameGps() {
		return filenameGps;
	}

	public void setFilenameGps(String filenameGps) {
		this.filenameGps = filenameGps;
	}
	
	

}

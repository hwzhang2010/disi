package com.hywx.sisl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "myconfig")
public class MyConfig {
	//网络传输字节序
    private boolean isNet;
    
    public MyConfig() {
    }

	public boolean isNet() {
		return isNet;
	}

	public void setIsNet(boolean isNet) {
		this.isNet = isNet;
	}
    
    
    
}

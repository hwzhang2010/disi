package com.hywx.sirs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.util.ResourceUtils;

/**
 * 文件信息工具类
 * @author zhang.huawei
 *
 */
public class FileUtil {
	/**
	 * jar包类所在目录路径
	 * @return target/classes
	 * @throws FileNotFoundException
	 */
	public static String getClassPath() throws FileNotFoundException {
		return ResourceUtils.getURL("classpath:").getPath();
	}
	
	/**
	 * jar包所在目录路径
	 * @return target
	 * @throws FileNotFoundException
	 */
	public static String getJarPath() throws FileNotFoundException {
		return new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getPath();
	}
	
	/**
	 * jar包所在目录路径配置
	 * @return target/config
	 * @throws FileNotFoundException
	 */
	public static String getJarConfigPath() throws FileNotFoundException {
		return new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getPath().concat(File.separator).concat("config");
	}
	
	/**
	 * 读取Json文件内容
	 * @param filename
	 * @return Json String
	 */
	public static String getJson(String filename) {
		String jsonStr = "";
		try {
			File file = new File(filename);
			File fileParent = file.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			if (!file.exists()) {
				return jsonStr;
			}
			
			FileReader fileReader = new FileReader(file);
			Reader reader = new InputStreamReader(new FileInputStream(file),"Utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			
			return jsonStr;
		} catch (Exception e) {
			return null;
		}
	}
}

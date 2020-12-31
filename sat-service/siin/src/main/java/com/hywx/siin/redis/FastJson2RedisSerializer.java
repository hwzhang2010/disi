package com.hywx.siin.redis;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
 * FastJson序列化，springboot默认使用Jackson序列化
 */
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	//配置白名单 地址是实体类包地址
    static {
    	//全局设置
    	ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //ParserConfig.getGlobalInstance().addAccept("com.hywx.siin.bo.Message");
    }
	
	private Class<T> clazz;
	
	public FastJson2RedisSerializer(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		if (t == null)
			return new byte[0];
		
		return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null || bytes.length <= 0) 
		    return null;
		
		String str = new String(bytes, DEFAULT_CHARSET);
		return (T)JSON.parseObject(str, clazz);	
	}

}

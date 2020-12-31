package com.hywx.siin.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
 * 注册FastJson序列化
 */
@Configuration
public class RedisConfiguration {
	
	/*
	 * fastjson
	 */
	@Bean(name = "redisTemplate")
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		//使用fastjson序列化
	    FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<Object>(Object.class);
		
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		//设置连接工厂
		template.setConnectionFactory(factory);
		//开启事务
		template.setEnableTransactionSupport(true);
		
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(fastJson2RedisSerializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(fastJson2RedisSerializer);
		
		template.setDefaultSerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		
		return template;
		
	}

}
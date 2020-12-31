package com.hywx.sisl.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.hywx.sisl.redis.RedisFind;
import com.hywx.sisl.service.DateTimeService;
import com.hywx.sisl.vo.DateTimeVO;

@Service("dateTimeService")
public class DateTimeServiceImpl implements DateTimeService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public DateTimeVO getDateTime() {
		String datetimeKey = RedisFind.keyBuilder("datetime", "set", "");
		if (redisTemplate.hasKey(datetimeKey)) {
			DateTimeVO vo = (DateTimeVO) redisTemplate.opsForValue().get(datetimeKey);
			return vo;
		}
		
		
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 1);
        DateTimeVO vo = new DateTimeVO(now, calendar.getTime());
        redisTemplate.opsForValue().set(datetimeKey, vo);
        
		return vo;
	}

	@Override
	public DateTimeVO updateDateTimeStart(String start) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate = format.parse(start);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE, 1);
			Date endDate = calendar.getTime();
			
			DateTimeVO vo = new DateTimeVO(startDate, endDate);
			String datetimeKey = RedisFind.keyBuilder("datetime", "set", "");
			redisTemplate.opsForValue().set(datetimeKey, vo);
			
			return vo;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String updateTimeStamp(Long timeStamp) {
		String timeStampKey = RedisFind.keyBuilder("datetime", "timestamp", "");
		redisTemplate.opsForValue().set(timeStampKey, timeStamp);
		
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		return format.format(new Date(timeStamp));
	}

	@Override
	public String requestTimeStamp(Long timeStamp, Double multiplier) {
		String timeStampKey = RedisFind.keyBuilder("datetime", "timestamp", "");
		if (redisTemplate.hasKey(timeStampKey)) {
			Long lastTimeStamp = (Long) redisTemplate.opsForValue().get(timeStampKey);
			if (multiplier >= 0) {
				Long max = timeStamp >= lastTimeStamp ? timeStamp : lastTimeStamp;
				timeStamp = max + Math.round(multiplier * 1000);
			} else {
				Long min = timeStamp < lastTimeStamp ? timeStamp : lastTimeStamp;
				timeStamp = min + Math.round(multiplier * 1000);
			}
		}
		redisTemplate.opsForValue().set(timeStampKey, timeStamp);
		
		String multiplierKey = RedisFind.keyBuilder("datetime", "multiplier", "");
		redisTemplate.opsForValue().set(multiplierKey, multiplier);
		
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		return format.format(new Date(timeStamp));
	}
	
	
	
	

}

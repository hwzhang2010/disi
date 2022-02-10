package com.hywx.sitm.quartz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.hywx.sitm.bo.SitmSave;
import com.hywx.sitm.global.GlobalQueue;
import com.hywx.sitm.service.TcService;

public class ScheduledJob4Save implements Job {
	
	@Autowired
	private TcService tcService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//格式化输出时间
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
        //System.out.println("*************************SAVE localdatetime: " + formatter.format(LocalDateTime.now()));
        //System.out.println("*************************SAVE threadid: " + Thread.currentThread().getId());
		
		if (GlobalQueue.getSaveQueue().isEmpty())
			return;
		
		try {
			int size = GlobalQueue.getSaveQueue().size();
			for (int i = 0; i < size; i++) {
				SitmSave save = GlobalQueue.getSaveQueue().take();
				switch (save.getType()) {
				case CMD:  // 遥控指令
					saveTc(save.getData());
					break;
				case INJECTION:  // 数据注入
					saveInjection(save.getData());
				default:
					break;
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void saveTc(byte[] data) {
		// 保存指令
		tcService.saveTc(data);
		
		// 删除1个月之前的指令
		tcService.deleteTc();
	}

	
	private void saveInjection(byte[] data) {
		// 保存数据注入
		tcService.saveInjection(data);
		
		// 删除1个月之前的数据注入
		tcService.deleteInjection();
		
	}
	
	
	
	
	

}

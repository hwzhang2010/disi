package com.hywx.sitm.quartz;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.hywx.sitm.global.GlobalAccess;

@Component
public class CronSchedulerJob {

	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	
	private void scheduledJob4Tm(Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob4Tm.class)
				                        .withIdentity("job4tm", "group4tm")
				                        .build();
		//Cron表达式：每秒的频率
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				                                .withIdentity("trigger4tm", "group4tm")
				                                .withSchedule(scheduleBuilder)
				                                .build();
		scheduler.scheduleJob(jobDetail, cronTrigger);	
		
		
//		Vector<String> satelliteIdVector = GlobalAccess.getGroupSatellites();
//		if (satelliteIdVector != null) {
//			for (String satelliteId : satelliteIdVector) {		
//				JobDetail jobDetail = JobBuilder.newJob(ScheduledJob4Tm.class)
//						                        .withIdentity("job4".concat(satelliteId), "jobgroup4tm")
//						                        .usingJobData("satelliteId", satelliteId)
//						                        //.usingJobData("timeStamp", System.currentTimeMillis())
//						                        .build();
//					
//				//Cron表达式：频率 1s
//				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
//				CronTrigger cronTrigger = TriggerBuilder.newTrigger()
//						                                .withIdentity("trigger4".concat(satelliteId), "triggergroup4tm")
//						                                .withSchedule(scheduleBuilder)
//						                                .build();
//				
//				scheduler.scheduleJob(jobDetail, cronTrigger);	
//			}
//		}

	}
	
	
	private void scheduledJob4Save(Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob4Save.class)
				                        .withIdentity("job4save", "group4save")
				                        .build();
		//Cron表达式：每秒的频率
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				                                .withIdentity("trigger4save", "group4save")
				                                .withSchedule(scheduleBuilder)
				                                .build();
		scheduler.scheduleJob(jobDetail, cronTrigger);	

	}
	
	/**
	 * 启动定时任务
	 * @throws SchedulerException
	 */
	public void scheduleJobs() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduledJob4Tm(scheduler);
		scheduledJob4Save(scheduler);
	}

}

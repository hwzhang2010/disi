package com.hywx.sisn.quartz;

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

@Component
public class CronSchedulerJob {
	
	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	
	private void scheduledJob4Send(Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob4Send.class)
				                        .withIdentity("job4send", "group4send")
				                        .build();
		//Cron表达式：每秒的频率
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				                                .withIdentity("trigger4send", "group4send")
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
		scheduledJob4Send(scheduler);
	}
	
	

}

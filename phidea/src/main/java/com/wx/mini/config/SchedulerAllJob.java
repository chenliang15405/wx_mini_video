package com.wx.mini.config;

import com.wx.mini.apiprocess.EarthlySweetSentenceApi;
import com.wx.mini.apiprocess.RainbowFartApi;
import com.wx.mini.job.AriticleCrawlerJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 设置job触发器及cron表达式
 */
@Component
public class SchedulerAllJob {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private JobTime jobTime;


    /**
     * 该方法用来启动所有的定时任务
     */
    void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduleJob1(scheduler);
        scheduleJob2(scheduler);
        scheduleJob3(scheduler);
    }


    private void scheduleJob1(Scheduler scheduler) throws SchedulerException {
        System.out.println(jobTime.getArticleCarwler());
        JobDetail jobDetail = JobBuilder.newJob(AriticleCrawlerJob.class).withIdentity("AriticleCrawlerJob", "Crawler-Job").build();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(jobTime.getArticleCarwler());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("AriticleCrawlerJob", "Crawler-Job").withSchedule(cronSchedule).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private void scheduleJob2(Scheduler scheduler) throws SchedulerException {
        System.out.println(jobTime.getEarthlySweetSentenceApi());
        JobDetail jobDetail = JobBuilder.newJob(EarthlySweetSentenceApi.class).withIdentity("EarthlySweetSentenceApi", "EarthlySweetSentenceApi-Job").build();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(jobTime.getEarthlySweetSentenceApi());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("EarthlySweetSentenceApi", "EarthlySweetSentenceApi-Job").withSchedule(cronSchedule).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private void scheduleJob3(Scheduler scheduler) throws SchedulerException {
        System.out.println(jobTime.getRainbowFartApi());
        JobDetail jobDetail = JobBuilder.newJob(RainbowFartApi.class).withIdentity("RainbowFartApi", "RainbowFartApi-Job").build();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(jobTime.getRainbowFartApi());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("RainbowFartApi", "RainbowFartApi-Job").withSchedule(cronSchedule).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


}

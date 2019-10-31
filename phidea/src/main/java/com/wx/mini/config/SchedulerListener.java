package com.wx.mini.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *  程序启动，监听 scheduler配置，用户注入scheduler的factory bean然后调用quartz启动Job，注入cron表达式
 **/
@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SchedulerAllJob schedulerAllJob;
    @Autowired
    private JobBeanJobFactory jobBeanJobFactory;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            schedulerAllJob.scheduleJobs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    多任务时的Scheduler，动态设置Trigger。一个SchedulerFactoryBean可能会有多个Trigger
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 自定义Job Factory，用于Spring注入
        schedulerFactoryBean.setJobFactory(jobBeanJobFactory);
        return schedulerFactoryBean;
    }

}

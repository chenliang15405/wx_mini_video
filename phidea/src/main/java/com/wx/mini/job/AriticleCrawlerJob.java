package com.wx.mini.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther alan.chen
 * @time 2019/10/10 2:41 PM
 */
@Component
public class AriticleCrawlerJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "----------------------");

    }
}

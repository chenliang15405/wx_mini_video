package com.wx.mini.apiprocess;

import com.wx.mini.service.apiprocessor.EarthlySweetApiService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alan.chen
 * @date 2019/10/30 4:24 PM
 */
@Component
public class EarthlySweetSentenceApi implements Job {


    @Autowired
    private EarthlySweetApiService earthlySweetApiService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        earthlySweetApiService.handlerApi();

    }


}

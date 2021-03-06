package com.wx.mini.apiprocess;

import com.wx.mini.service.apiprocessor.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author alan.chen
 * @date 2019/10/30 4:24 PM
 */
@Slf4j
@Component
public class EarthlySweetSentenceApi implements Job {


    @Qualifier("earthlySweetApiServiceImpl")
    @Autowired
    private ApiService apiService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("ApiCollData Job Start");
        apiService.handlerApi();
        log.info("ApiCollData Job End");
    }


}

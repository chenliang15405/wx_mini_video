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
 * 彩虹屁api
 *
 * @author alan.chen
 * @date 2019/11/3 10:38 PM
 */
@Slf4j
@Component
public class RainbowFartApi implements Job {

    @Qualifier("rainbowFartApiServiceImpl")
    @Autowired
    private ApiService apiService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("RainbowFartApi Job Start");
        apiService.handlerApi();
        log.info("RainbowFartApi Job End");
    }

}

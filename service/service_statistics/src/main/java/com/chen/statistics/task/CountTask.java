package com.chen.statistics.task;

import com.chen.statistics.service.DailyService;
import com.chen.statistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CountTask {
    @Autowired
    private DailyService dailyService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void execute(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        dailyService.countRegister(day);
    }
}

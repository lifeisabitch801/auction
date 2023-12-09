package com.yxq.auction.job;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yxq.auction.model.Auction;
import com.yxq.auction.service.AuctionService;
import com.yxq.auction.service.DealrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyJob {
    @Autowired
    private AuctionService auservice;
    @Autowired
    private DealrecordService drservice;

    @Scheduled(cron = "0 0/10 8-20 * * ?")
    public void releaseAuction(){
        UpdateWrapper<Auction> wrapper = new UpdateWrapper<>();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        wrapper.set("astate",4);
        wrapper.eq("astate",2);
        wrapper.eq("stime",now);
        auservice.update(wrapper);
    }

    @Scheduled(cron = "0 0/15 8-20 * * ?")
    public void closeAuction() throws MessagingException {
        auservice.closeAuction();
    }
    @Scheduled(cron = "0 0/5 8-20 * * ?")
    public void IllegalDeal() throws ParseException {
        System.out.println("pre====结束交易===定时任务");
        drservice.IlleagalDeal();
        System.out.println("after====结束交易===定时任务");
    }
}

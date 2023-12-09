package com.yxq.auction.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxq.auction.model.Auction;
import com.yxq.auction.model.Dealrecord;
import com.yxq.auction.model.Dealstate;
import com.yxq.auction.service.AuctionService;
import com.yxq.auction.service.DealrecordService;
import com.yxq.auction.service.DealstateService;
import com.yxq.auction.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/auction/dealrecord")
public class DealrecordController {
    @Autowired
    private DealrecordService drservice;
    @Autowired
    private DealstateService dsservice;
    @Autowired
    private AuctionService auservice;
    @RequestMapping("getMyOrder")
    public Page<Dealrecord> drpage(String vid,@RequestParam(defaultValue = "{1,2,3,4,5,6}") String[] dstates,@RequestParam(defaultValue = "1")Integer pageno) throws ParseException {
        Page<Dealrecord> page = new Page<>(pageno,3);
        LambdaQueryWrapper<Dealrecord> queryWrapper
                = new QueryWrapper<Dealrecord>().lambda().in(Dealrecord::getDid, dstates);
        queryWrapper.eq(Dealrecord::getVid,vid);
        Page<Dealrecord> result = drservice.page(page,queryWrapper);
        List<Dealrecord> list = result.getRecords();
        for(Dealrecord dr:list){

            Auction au = auservice.getById(dr.getAid());
            dr.setAuction(au);

            Dealstate ds = dsservice.getById(dr.getDid());
            dr.setDealstate(ds);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date=dr.getDetime();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR,+3);//
            Date dt1=rightNow.getTime();
            String endTime = sdf.format(dt1);
            String beginTime = (sdf.format(date)).substring(0,10);
            dr.setBeginDate(beginTime);
            dr.setEndDate(endTime);
        }
        return result;
    }
    @RequestMapping("paymoney")
    public ResponseResult payMoney(String vid,Integer deid){
        drservice.payMoney(vid,deid);
        ResponseResult rr = new ResponseResult();
        rr.setMsg("付款成功，交易完成");
        rr.setCode(1);
        return rr;
    }
}


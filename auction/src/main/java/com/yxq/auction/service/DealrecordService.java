package com.yxq.auction.service;

import com.yxq.auction.model.Dealrecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
public interface DealrecordService extends IService<Dealrecord> {

    void payMoney(String vid, Integer deid);
    public void IlleagalDeal() throws ParseException;
}

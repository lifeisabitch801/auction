package com.yxq.auction.service;

import com.yxq.auction.model.Auction;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.mail.MessagingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
public interface AuctionService extends IService<Auction> {


    boolean updateAuction(Integer aid, String vid, Integer nprice) throws Exception;
    public void closeAuction() throws MessagingException;
}

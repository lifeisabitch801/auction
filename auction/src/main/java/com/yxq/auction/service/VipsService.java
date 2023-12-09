package com.yxq.auction.service;

import com.yxq.auction.model.Vips;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
public interface VipsService extends IService<Vips> {

    void updateVbalance(String vid,int addmoney);
    void outVbalance(String vid,int outmoney);
}

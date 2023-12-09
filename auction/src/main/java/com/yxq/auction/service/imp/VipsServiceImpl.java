package com.yxq.auction.service.imp;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxq.auction.dao.MoneyrecordDao;
import com.yxq.auction.dao.VipsDao;
import com.yxq.auction.model.Moneyrecord;
import com.yxq.auction.model.Vips;
import com.yxq.auction.service.VipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@Service
public class VipsServiceImpl extends ServiceImpl<VipsDao, Vips> implements VipsService {
    @Autowired
    private VipsDao vdao;
    @Autowired
    private MoneyrecordDao mrdao;
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void updateVbalance(String vid,int addmoney) {
        //充值 VIP--update===moneyrecord--insert--mtype=0
        //upate==vip
        Vips vip = vdao.selectById(vid);
        UpdateWrapper<Vips> uw = new UpdateWrapper<>();
        uw.set("vbalance",addmoney+vip.getVbalance());
        System.out.println(addmoney+vip.getVbalance());
        uw.eq("vid",vid);
        vdao.update(vip,uw);
        //生成一条资金记录 交易类型 存款0
        Moneyrecord mr = new Moneyrecord();
        mr.setDemoney(addmoney);
        mr.setVid(vid);
        mr.setMtype(0);
        mr.setVname(vip.getVname());
        mrdao.insert(mr);
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public void outVbalance(String vid, int outmoney) {
        //提现 VIP--update===moneyrecord--insert--mtype=1
        //upate==vip
        Vips vip = vdao.selectById(vid);
        UpdateWrapper<Vips> uw = new UpdateWrapper<>();
        uw.set("vbalance",vip.getVbalance()-outmoney);
        //System.out.println(outmoney+vip.getVbalance());
        uw.eq("vid",vid);
        vdao.update(vip,uw);
        //生成一条资金记录 交易类型 存款1
        Moneyrecord mr = new Moneyrecord();
        mr.setDemoney(-outmoney);
        mr.setVid(vid);
        mr.setMtype(1);
        mr.setVname(vip.getVname());
        mrdao.insert(mr);
    }
}

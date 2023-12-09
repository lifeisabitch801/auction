package com.yxq.auction.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxq.auction.dao.*;
import com.yxq.auction.model.Arecord;
import com.yxq.auction.model.Auction;
import com.yxq.auction.model.Dealrecord;
import com.yxq.auction.model.Vips;
import com.yxq.auction.service.AuctionService;
import com.yxq.auction.util.MailUtil;
import com.yxq.auction.util.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@Service
public class AuctionServiceImpl extends ServiceImpl<AuctionDao, Auction> implements AuctionService {
    @Autowired
    private AuctionDao audao;
    @Autowired
    private ArecordDao ardao;
    @Autowired
    private MoneyrecordDao mrdao;
    @Autowired
    private VipsDao vdao;
    @Autowired
    private DealrecordDao drdao;

    @Autowired
    private MailUtil mailutil;
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean updateAuction(Integer aid, String vid, Integer nprice) throws Exception {
        Auction au = audao.selectById(aid);
        Vips oldv = new Vips();
        int num = au.getAnum();//乐观锁
        //判断是否为第一个竞拍者
        if (au.getAnum() != 0) {
            oldv = vdao.selectById(au.getVid());
            UpdateWrapper<Vips> olduw = new UpdateWrapper<Vips>();
            olduw.eq("vid", oldv.getVid());
            olduw.set("vicemoney", oldv.getVicemoney() - au.getAbmoney());
            olduw.set("vbalance", oldv.getVbalance() + au.getAbmoney());
            vdao.update(null, olduw);//退还保证金
            //不需要生成一条资金记录
        }
        //更新拍品信息表 当前竞拍者id 当前竞拍价格 update 竞拍次数
        UpdateWrapper<Auction> newuw = new UpdateWrapper<Auction>();
        newuw.eq("aid", au.getAid());
        newuw.eq("anum", num);
        newuw.set("nprice", nprice);
        newuw.set("vid", vid);
        newuw.set("anum", num + 1);
        //Thread.sleep(5000);
        int res = audao.update(null, newuw);
        System.out.println("====执行了" + res + "条" + vid);
        if (res == 0) {
            System.out.println("======手慢了======");
            throw new Exception("手慢了。。。");

        }
        //arecord 竞拍记录 insert
        Arecord ar = new Arecord();
        ar.setAid(aid);
        ar.setArprice(nprice);
        ar.setVid(vid);
        ardao.insert(ar);
        //当前竞拍者aid 保证金 update
        Vips v2 = vdao.selectById(vid);
        UpdateWrapper<Vips> uw2 = new UpdateWrapper<>();
        uw2.eq("vid", vid);
        uw2.set("vbalance", v2.getVbalance() - au.getAbmoney());
        uw2.set("vicemoney", v2.getVicemoney() + au.getAbmoney());
        vdao.update(null, uw2);

        return true;

    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void closeAuction() throws MessagingException {
        //AUCTION
        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //wrapper.set("astate", 5);
        wrapper.eq("astate", 4);
        wrapper.eq("etime", now);
        //audao.update(null, wrapper);
        //DEALRECORD
       // QueryWrapper<Auction> wrapper2 = new QueryWrapper<>();
        //wrapper2.eq("astate", 5);
        List<Auction> alist = audao.selectList(wrapper);
        Dealrecord dr = new Dealrecord();
        for (Auction auc : alist) {
            auc.setAstate(5);
            audao.updateById(auc);
            if (auc.getVid() == null) {
                dr.setAid(auc.getAid());
                dr.setGname(auc.getGname());
                dr.setDid(1);
                drdao.insert(dr);
                //最终状态auction
                auc.setEstate(1);//流拍
                audao.updateById(auc);
            } else {
                dr.setAid(auc.getAid());
                dr.setGname(auc.getGname());
                dr.setDid(2);
                dr.setVid(auc.getVid());
                Vips vip = vdao.selectById(auc.getVid());
                dr.setVname(vip.getVname());
                dr.setDemoney(auc.getNprice());
                drdao.insert(dr);
            }
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        mailutil.sendMailForClose(auc.getCreaterid(),auc.getVid(),auc.getAid());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            ThreadUtil.getThreadPool().execute(r);

        }
    }
}
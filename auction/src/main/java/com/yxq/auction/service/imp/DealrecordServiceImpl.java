package com.yxq.auction.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxq.auction.dao.AuctionDao;
import com.yxq.auction.dao.DealrecordDao;
import com.yxq.auction.dao.MoneyrecordDao;
import com.yxq.auction.dao.VipsDao;
import com.yxq.auction.model.Auction;
import com.yxq.auction.model.Dealrecord;
import com.yxq.auction.model.Moneyrecord;
import com.yxq.auction.model.Vips;
import com.yxq.auction.service.DealrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class DealrecordServiceImpl extends ServiceImpl<DealrecordDao, Dealrecord> implements DealrecordService {
    @Autowired
    private DealrecordDao drdao;
    @Autowired
    private VipsDao vdao;
    @Autowired
    private AuctionDao audao;
    @Autowired
    private MoneyrecordDao mrdao;
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void payMoney(String vid,Integer deid) {
        Dealrecord dr = drdao.selectById(deid);
        Auction au = audao.selectById(dr.getAid());
        Vips buyer = vdao.selectById(vid);
        Vips saler = vdao.selectById(au.getCreaterid());
        //冻结金额+剩余金额
        //buyer 扣钱
        UpdateWrapper<Vips> buyerwr = new UpdateWrapper<>();
        buyerwr.set("vicemoney",buyer.getVicemoney()-au.getAbmoney());
        buyerwr.eq("vid",vid);
        buyerwr.set("vbalance",buyer.getVbalance()-au.getNprice()+au.getAbmoney());
        vdao.update(null,buyerwr);
        //saler 加钱
        UpdateWrapper<Vips> salerwr = new UpdateWrapper<>();
        salerwr.eq("vid",saler.getVid());
        salerwr.set("vbalance",saler.getVbalance()+au.getNprice());
        vdao.update(null,salerwr);
        //资金记录insert===2条  2.消费 3.收益
        Moneyrecord mr = new Moneyrecord();
        mr.setVname(buyer.getVname());
        mr.setVid(buyer.getVid());
        mr.setDemoney(-au.getNprice());
        mr.setMtype(2);
        mrdao.insert(mr);
        Moneyrecord mr2 = new Moneyrecord();
        mr2.setVname(saler.getVname());
        mr2.setVid(saler.getVid());
        mr2.setDemoney(au.getNprice());
        mr2.setMtype(3);
        mrdao.insert(mr2);
        //修改dealrecord = 3
        UpdateWrapper<Dealrecord> drwr = new UpdateWrapper<>();
        drwr.eq("deid",deid);
        drwr.set("did",3);
        drwr.set("detime",new Date());
        drdao.update(null,drwr);
        //修改auction astate = 6
        UpdateWrapper<Auction> auwr = new UpdateWrapper<>();
        auwr.eq("aid",au.getAid());
        auwr.set("astate",6);
        auwr.set("estate",2);
        audao.update(null,auwr);
        //update aucyion estate
        System.out.println("update===aucyion===estate===2");
    }
    //违约
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public void IlleagalDeal() throws ParseException {
        System.out.println("service====结束交易===定时任务===start");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sdf.format(new Date());//获取系统当前时间
        Date date=sdf.parse(datestr);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR,-2);//
        Date dt1=rightNow.getTime();
        String beginTime = sdf.format(dt1);
        QueryWrapper<Dealrecord> qw = new QueryWrapper<>();
        qw.eq("did",2);
        qw.lt("detime",beginTime);
        System.out.println("=========="+beginTime+"========");
        List<Dealrecord> list = drdao.selectList(qw);
        for(Dealrecord dr:list){
            //Dealrecord---update
            dr.setDid(4);
            dr.setDetime(new Date());
            System.out.println("===before==update===did");
            drdao.updateById(dr);
            System.out.println("===after==update===did");
            //Auction--update
            Auction au = audao.selectById(dr.getAid());
            au.setAstate(6);
            au.setEstate(3);
            audao.updateById(au);
            System.out.println("update====auction======astate");
            //insert moneyrecord==罚款
            Moneyrecord mr = new Moneyrecord();
            Vips buyer = vdao.selectById(dr.getVid());
            mr.setMtype(4);
            mr.setVid(dr.getVid());
            mr.setDemoney(-au.getAbmoney());
            mr.setVname(buyer.getVname());
            mrdao.insert(mr);
            System.out.println("=======insert===mr===buyer");
            //saler insert
            Moneyrecord mr2 = new Moneyrecord();
            Vips saler = vdao.selectById(au.getCreaterid());
            mr2.setMtype(3);
            mr2.setVid(saler.getVid());
            mr2.setDemoney(au.getAbmoney());
            mr2.setVname(saler.getVname());
            mrdao.insert(mr2);
            System.out.println("=======insert===mr===saler");
            //update vips
            buyer.setVicemoney(buyer.getVicemoney()-au.getAbmoney());
            vdao.updateById(buyer);
            System.out.println("=======update===vbalance===buyer");
            saler.setVbalance(saler.getVbalance()+au.getAbmoney());
            vdao.updateById(saler);
            System.out.println("=======update===vbalance===saler");
        }

    }
}

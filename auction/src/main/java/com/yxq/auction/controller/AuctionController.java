package com.yxq.auction.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxq.auction.model.Astate;
import com.yxq.auction.model.Auction;
import com.yxq.auction.model.Ptypes;
import com.yxq.auction.service.AstateService;
import com.yxq.auction.service.AuctionService;
import com.yxq.auction.service.DealstateService;
import com.yxq.auction.service.PtypesService;
import com.yxq.auction.web.AuctionNowPriceService;
import com.yxq.auction.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/auction/auction")
public class AuctionController {
    @Autowired
    private AuctionService auservice;
    @Autowired
    private PtypesService pservice;
    @Autowired
    private DealstateService dsservice;
    @Autowired
    private AstateService asservice;

    //商品类型列表
    @RequestMapping("getplist")
    public List<Ptypes> getPlist(){
        return pservice.list();
    }
    //拍品状态列表
    //拍卖状态 1、审核中 2、审核通过 3、审核失败 4、竞拍中 5、竞拍结束 6、交易结束*/
    @RequestMapping("getaslist")
    public List<Astate> getAslist(){
        return asservice.list();
    }
    //发布
    @RequestMapping("insertauction")
    public ResponseResult insertAuction(Auction auctionform, MultipartFile pic) throws IOException {
        ResponseResult rr = new ResponseResult();
        if(auctionform != null){
            String filename = UUID.randomUUID().toString();
            int index = pic.getOriginalFilename().lastIndexOf(".");
            String ext = pic.getOriginalFilename().substring(index);
            File dest = new File("D:\\nginx-1.10.1\\html\\images",filename+ext);
            pic.transferTo(dest);
            String fileurl = "/"+filename+ext;
            auctionform.setGpic(fileurl);
            auctionform.setAstate(1);//竞拍状态
            auctionform.setCflag(0);//发布人身份--会员
            auctionform.setAnum(0);//竞拍次数
            auctionform.setGbuzz(0);//热度
            auservice.save(auctionform);
            rr.setMsg("发布成功，请等待审核结果！");
            rr.setCode(1);
            return rr;
        }else{
            rr.setMsg("发布失败");
            rr.setCode(0);
            return rr;
        }

    }
    //拍品列表--翻页---vip
    @RequestMapping("aupage")
    public Page<Auction> aupage(@RequestParam(defaultValue = "1")Integer pageno,Integer tid, String gname){
        Page<Auction> p1 = new Page<Auction>(pageno,3);
        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
        if(tid != null&& tid!=0){
            wrapper.eq("tid",tid);
        }
        if(gname != null&& !gname.equals("")){
            wrapper.like("gname",gname);
        }
        wrapper.eq("astate",4);
        Page<Auction> result = auservice.page(p1,wrapper);
        List<Auction> list = result.getRecords();
        for (Auction auc : list){
            Ptypes pt = pservice.getById(auc.getTid());
            auc.setPtypes(pt);
        }
        return result;
    }
    //拍品列表 ---翻页---getrecords---admin
    @RequestMapping("getaupage")
    public Page<Auction> getAupage(Integer astate,@RequestParam(defaultValue = "1") Integer pageno){
        Page<Auction> page = new Page<Auction>(pageno,3);
        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
        wrapper.eq("astate",astate);
        Page<Auction> result = auservice.page(page,wrapper);
        List<Auction> list = result.getRecords();
        for(Auction auc:list){
            Ptypes pt = pservice.getById(auc.getTid());
            auc.setPtypes(pt);
        }
        return result;
    }
    //未审核拍品列表=====admin
    @RequestMapping("preaulist")
    public Page<Auction> getPreaupage(int pageno,int pagesize){
        Page<Auction> p1 = new Page<Auction>(pageno,pagesize);
        QueryWrapper<Auction> qw = new QueryWrapper<Auction>();
        qw.eq("astate",1);
        Page<Auction> res = auservice.page(p1,qw);
        return res;
    }
    //审核
    @RequestMapping("reviewau")
    public ResponseResult reviewAu(String aid,int astate){
        ResponseResult rr = new ResponseResult();
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("aid",aid);
        wrapper.set("astate",astate);
        auservice.update(wrapper);
        rr.setMsg("完成审核");
        rr.setCode(1);
        return rr;
    }
    @RequestMapping("getoneau")
    public Auction getOneAuction(Integer aid){
       return  auservice.getById(aid);
    }
    @RequestMapping("joinauction")
    public ResponseResult joinAuction(Integer aid,String vid,Integer nprice) throws Exception {
        ResponseResult rr = new ResponseResult();
        boolean flag1 = auservice.updateAuction(aid,vid,nprice);
        AuctionNowPriceService.sendMessage(aid,vid,nprice);
        rr.setCode(1);
        rr.setMsg("参与竞拍成功");

        return rr;
    }
    //vip发布拍品查询
    @RequestMapping("getmyauction")
    public Page<Auction> getMyAuction(String createrid,@RequestParam(defaultValue = "{1,2,3,4,5,6}") String[] astates,@RequestParam(defaultValue = "1") Integer pageno){
        System.out.println("vip发布拍品查询");
        Page<Auction> page = new Page<Auction>(pageno,3);
        LambdaQueryWrapper<Auction> queryWrapper
                = new QueryWrapper<Auction>().lambda().in(Auction::getAstate, astates);
        queryWrapper.eq(Auction::getCreaterid,createrid);
        Page<Auction> result = auservice.page(page,queryWrapper);
        List<Auction> list = result.getRecords();
        for(Auction auc:list){
            Ptypes pt = pservice.getById(auc.getTid());
            Astate ass = asservice.getById(auc.getAstate());
            auc.setPtypes(pt);
            auc.setAstateClass(ass);
        }
        return result;
    }
}


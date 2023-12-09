package com.yxq.auction.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxq.auction.model.Moneyrecord;
import com.yxq.auction.model.Vips;
import com.yxq.auction.service.MoneyrecordService;
import com.yxq.auction.service.VipsService;
import com.yxq.auction.util.CacheUtil;
import com.yxq.auction.util.TokenUtil;
import com.yxq.auction.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/auction/vips")
public class VipsController {
    @Autowired
    private VipsService vservice;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private MoneyrecordService mrservice;
   /* @Autowired
    JavaMailSender javaMailSender;*/
    //发送复杂邮件
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;
    //validvid
    @RequestMapping("validvid")
    public boolean validVid(String vid){
        if(vservice.getById(vid)!=null){
            return false;//vid重复
        }else{
            return true;//vid可用
        }
    }
    //register
    @RequestMapping("register")
    public boolean registerVips(@RequestBody Vips vip) throws Exception{
        vip.setVbalance(0);
        vip.setVicemoney(0);
        vip.setRid(2);
        boolean flag = vservice.save(vip);

        MimeMessage message = javaMailSender.createMimeMessage();
//再使用这个api设置邮件
//这里true代表你要发附件邮件，只要这里为true才可以发文件
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(from);
        helper.setTo(vip.getVemail());
        helper.setSubject("欢迎注册卓越拍卖网站");
        String context="尊敬的"+vip.getVname()+
                "<a href='http://localhost:8088/auction/vips/activation?vid="+vip.getVid()
                +"'>请点击激活账户</a>";
        helper.setText(context,true);

        javaMailSender.send(message);
        return flag;
    }

    //激活用户
    @RequestMapping("activation")
    public String activationVip(String vid){
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.set("rid",1);
        wrapper.eq("vid",vid);
        vservice.update(wrapper);
        return "恭喜您激活成功！";
    }
        // 构建一个邮件对象
       /* SimpleMailMessage message=new SimpleMailMessage();
// 设置邮件主题
        message.setSubject("这是一封测试邮件");
// 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom("1991878989@qq.com");
// 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
       //message.setTo("10*****16@qq.com","12****32*qq.com");
        //message.setTo("1179606113@qq.com");
        message.setTo(vip.getVemail());
// 设置邮件抄送人，可以有多个抄送人
        message.setCc("1991878989@qq.com");
// 设置隐秘抄送人，可以有多个
        //message.setBcc("7******9@qq.com");
// 设置邮件发送日期
        message.setSentDate(new Date());
// 设置邮件的正文
        message.setText("这是测试邮件的正文");
// 发送邮件
        javaMailSender.send(message);*/


    @Autowired
    HttpSession session;

    @RequestMapping("login")
    public ResponseResult login(String vid, String vpwd) throws Exception {
        ResponseResult rr = new ResponseResult();
        Vips vip = vservice.getById(vid);
        if(vip == null){
            rr.setCode(0);
            rr.setMsg("账号不存在");
            return rr;//"账号不存在";
        } else if(vip!= null && !vip.getVpwd().equals(vpwd)){
            rr.setCode(0);
            rr.setMsg("密码错误");
            return rr;//"密码错误";
        }else if(vip!= null && vip.getVpwd().equals(vpwd) && vip.getRid()!=1){
            rr.setCode(0);
            rr.setMsg("用户未激活，请前往邮箱激活用户");
            return rr;//"密码错误";
        }else{
            String token =  TokenUtil.sign(vid);
            
            rr.setCode(1);
            rr.setMsg(token);
            //session.setAttribute("vid",vid);
            System.out.println(session.getAttribute("vid"));
            //session.setAttribute("vname",vip.getVname());
            return rr;//"登录成功";
        }
    }

    @RequestMapping("tovipinfo")
    public Vips vipInfo(String vid){
        //return vservice.getById((String)session.getAttribute("vid"));
        return vservice.getById(vid);
    }
    @RequestMapping("updatevipinfo")
    public int UpdateVipInfo(@RequestBody Vips vip){
        if(1==1){
            UpdateWrapper wrapper = new UpdateWrapper();
            wrapper.set("vtel",vip.getVtel());
            wrapper.set("vemail",vip.getVemail());
            wrapper.set("vname",vip.getVname());
            wrapper.set("vaddress",vip.getVaddress());
            wrapper.set("vbackup",vip.getVbackup());
            wrapper.eq("vid",vip.getVid());
            vservice.update(wrapper);
            return 1;
        }
        else{
            return 0;
        }
    }
    @RequestMapping("validold")
    public int validOldpwd(String oldpwd,String vid){
        System.out.println("============oldpwd"+oldpwd);
        String pwd = vservice.getById(vid).getVpwd();
        if(oldpwd.equals(pwd)){
            System.out.println("=====valid old======");
            return 1;
        }else{
            System.out.println("====fail===validpwd");
            return 0;
        }
    }
    @RequestMapping("updatepwd")
    public int updatePwd(String newpwd,String vid){
        System.out.println("=====update======0000========");
        if(true){
            UpdateWrapper wrapper = new UpdateWrapper();
            wrapper.set("vpwd",newpwd);
            wrapper.eq("vid",vid);
            System.out.println("======update===pwd===");
            vservice.update(wrapper);
            return 1;
        }
        else{
            return 0;
        }
    }
    @RequestMapping("getonevip")
    public Vips getOneVip(String vid){
        return vservice.getById(vid);
    }
    @RequestMapping("chargemoney")
    public ResponseResult chargeMoney(String vid,int addmoney){
        ResponseResult rr = new ResponseResult();
        vservice.updateVbalance(vid,addmoney);
        rr.setMsg("充值成功");
        rr.setCode(1);
        return rr;
    }
    @RequestMapping("outmoney")
    public ResponseResult outMoney(String vid,int outmoney){
        ResponseResult rr = new ResponseResult();
        vservice.outVbalance(vid,outmoney);
        rr.setMsg("提现成功");
        rr.setCode(1);
        return rr;
    }
    //my money record
    @RequestMapping("getmymoneyrecord")
    public Page<Moneyrecord> getMyMoneyRecord(String vid,
                                              @RequestParam(defaultValue = "bt") String beginTime,
                                              @RequestParam(defaultValue = "et") String endTime,
                                              @RequestParam(defaultValue = "{0,1,2,3,4,}") String[] mrstates,
                                              @RequestParam(defaultValue = "1")Integer pageno) throws ParseException {
        Page<Moneyrecord> page = new Page<>(pageno,5);
        LambdaQueryWrapper<Moneyrecord> queryWrapper
                = new QueryWrapper<Moneyrecord>().lambda().in(Moneyrecord::getMtype, mrstates);
        queryWrapper.eq(Moneyrecord::getVid,vid);
        if(!beginTime.equals("bt")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1=sdf.parse(beginTime);
            Date date2=sdf.parse(endTime);
            Calendar rightNow = Calendar.getInstance();
            Calendar rightNow2 = Calendar.getInstance();
            rightNow.setTime(date1);
            rightNow2.setTime(date2);
            rightNow.add(Calendar.DAY_OF_YEAR,-1);//
            rightNow2.add(Calendar.DAY_OF_YEAR,+1);//
            Date dt1=rightNow.getTime();
            Date dt2=rightNow2.getTime();
            String bTime = sdf.format(dt1);
            String eTime = sdf.format(dt2);
            queryWrapper.gt(Moneyrecord::getDetime,bTime);
            queryWrapper.lt(Moneyrecord::getDetime,eTime);
        }
        Page<Moneyrecord> result = mrservice.page(page,queryWrapper);
        List<Moneyrecord> list = result.getRecords();
        for(Moneyrecord mr:list){
           int mytype = mr.getMtype();
            if(mytype==0){
                mr.setMrtype("充值");
            }else if(mytype==1){
                mr.setMrtype("提现");
            }else if(mytype==2){
                mr.setMrtype("消费");
            }else if(mytype==3){
                mr.setMrtype("收益");
            }else{
                mr.setMrtype("违约罚款");
            }
            //时间格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=mr.getDetime();
            String myTime = sdf.format(date);
            mr.setRightTime(myTime);
        }
        return result;
    }
}


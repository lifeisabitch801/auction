package com.yxq.auction.util;

import com.yxq.auction.dao.AuctionDao;
import com.yxq.auction.dao.VipsDao;
import com.yxq.auction.model.Auction;
import com.yxq.auction.model.Vips;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
@Component
public class MailUtilImp implements MailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JavaMailSender javaMailSender2;
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private VipsDao vdao;
    @Autowired
    private AuctionDao audao;

    public void sendMailForClose(String vid1,String vid2,Integer aid) throws MessagingException {

        Auction auc = audao.selectById(aid);

        Vips vip1 = vdao.selectById(vid1);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom(from);
        helper.setTo(vip1.getVemail());
        helper.setSubject("卓越拍卖网站,竞拍结束通知");
        String context="尊敬的"+vip1.getVname()+
                "您的"+auc.getGname()+"已经结束竞拍"+"<a href='http://127.0.0.1:8848/paimaiclient/auctioning.html'>请点击前往拍拍网站查看详情</a>";
        helper.setText(context,true);

        javaMailSender.send(message);

        if(auc.getAnum()!=0){
            Vips vip2 = vdao.selectById(auc.getVid());
            MimeMessage message2 = javaMailSender2.createMimeMessage();
            MimeMessageHelper helper2 = new MimeMessageHelper(message2,true);
            helper2.setFrom(from);
            System.out.println("====pre===="+vip2.getVemail()+"=======");
            helper2.setTo(vip2.getVemail());
            System.out.println("====after===="+vip2.getVemail()+"=======");
            helper2.setSubject("卓越拍卖网站,竞拍结束通知");
            String context2="尊敬的"+vip2.getVname()+"您竞拍的"+auc.getGname()+"已经结束竞拍，恭喜您竞拍到该商品，"+
                    "<a href='http://127.0.0.1:8848/paimaiclient/auctioning.html'>请点击前往拍拍网站查看详情</a>";
            helper2.setText(context2,true);

            javaMailSender2.send(message2);
        }

    }
}

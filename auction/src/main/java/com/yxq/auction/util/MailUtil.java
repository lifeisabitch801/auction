package com.yxq.auction.util;

import javax.mail.MessagingException;

public interface MailUtil {
    public void sendMailForClose(String vid1,String vid2,Integer aid)throws MessagingException;
}

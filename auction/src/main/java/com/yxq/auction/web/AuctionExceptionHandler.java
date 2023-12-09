package com.yxq.auction.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AuctionExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseResult handle(HttpServletRequest request,Exception e){
        //开发人员
        e.printStackTrace();
        //客户端
        ResponseResult rr = new ResponseResult();
        rr.setCode(0);
        rr.setMsg(e.getMessage());
        return rr;
    }
}

package com.yxq.auction.web;

import lombok.Data;

@Data
public class ResponseResult {
    private int code;//1===ok   0==error
    private String msg;
}

package com.yxq.auction.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxq.auction.model.Admin;
import com.yxq.auction.service.AdminService;
import com.yxq.auction.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/auction/admin")
public class AdminController {
    @Autowired
    private AdminService adservice;

    @RequestMapping("adlogin")
    public ResponseResult adLogin(@RequestBody Admin ad){
        ResponseResult rr = new ResponseResult();
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("aid",ad.getAid());
        qw.eq("apwd",ad.getApwd());
        Admin admin = adservice.getOne(qw);
        if(admin != null){
            rr.setCode(1);
            rr.setMsg("登录成功");
            return rr;
        }else{
            rr.setCode(0);
            rr.setMsg("账号或密码错误");
            return rr;
        }
    }
}


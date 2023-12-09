package com.yxq.auction.service.imp;

import com.yxq.auction.model.Admin;
import com.yxq.auction.dao.AdminDao;
import com.yxq.auction.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {

}

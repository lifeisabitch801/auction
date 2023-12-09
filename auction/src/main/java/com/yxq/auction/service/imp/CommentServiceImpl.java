package com.yxq.auction.service.imp;

import com.yxq.auction.model.Comment;
import com.yxq.auction.dao.CommentDao;
import com.yxq.auction.service.CommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

}

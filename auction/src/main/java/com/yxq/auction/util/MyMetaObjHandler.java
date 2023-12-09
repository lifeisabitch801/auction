package com.yxq.auction.util;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"createtime", Date.class,new Date());
        this.strictInsertFill(metaObject,"detime", Date.class,new Date());
        //this.strictInsertFill(metaObject,"detime", Date.class,new Date());
        this.strictInsertFill(metaObject,"artime", Date.class,new Date());



    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"detime", Date.class,new Date());
    }
}

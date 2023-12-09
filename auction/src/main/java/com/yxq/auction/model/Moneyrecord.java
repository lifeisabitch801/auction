package com.yxq.auction.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yxq
 * @since 2023-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Moneyrecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "mid", type = IdType.AUTO)
    private Integer mid;

    private String vid;

    private String vname;
    @TableField(value = "detime",fill = FieldFill.INSERT)
    private Date detime;

    private Integer demoney;

    private Integer mtype;

    private String mbackup;
    @TableField(exist = false)
    private String mrtype;
    @TableField(exist = false)
    private String rightTime;
}

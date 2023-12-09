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
public class Auction implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "aid", type = IdType.AUTO)
    private Integer aid;

    private String gname;

    private Integer tid;

    private String gpic;

    private Integer gbuzz;

    private Integer anum;

    private Integer bprice;

    private Integer increase;

    private Integer abmoney;

    private String stime;

    private String etime;

    private Integer astate;

    private String createrid;

    private Integer cflag;
    @TableField(value = "createtime",fill = FieldFill.INSERT)
    private Date createtime;

    private String vid;

    private Integer nprice;

    private Integer estate;

    @TableField(exist = false)
    private Ptypes ptypes;

    @TableField(exist = false)
    private Astate astateClass;

    @TableField(exist = false)
    private Dealstate dealstate;
}

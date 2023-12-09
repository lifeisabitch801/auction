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
public class Dealrecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "deid", type = IdType.AUTO)
    private Integer deid;

    private Integer aid;

    private String gname;

    private Integer demoney;
    @TableField(value = "detime",fill = FieldFill.INSERT_UPDATE )
    private Date detime;

    private String vid;

    private String vname;

    private Integer did;

    private String dbackup;

    @TableField(exist = false)
    private Auction auction;

    @TableField(exist = false)
    private String endDate;

    @TableField(exist = false)
    private Dealstate dealstate;

    @TableField(exist = false)
    private String beginDate;
}

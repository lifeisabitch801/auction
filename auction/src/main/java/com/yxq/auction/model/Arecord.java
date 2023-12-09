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
public class Arecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "arid", type = IdType.AUTO)
    private Integer arid;

    private Integer aid;

    private Integer arprice;
    @TableField(value = "artime",fill = FieldFill.INSERT)
    private Date artime;

    private String vid;

    private String artalk;

    private String arbackup;


}

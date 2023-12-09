package com.yxq.auction.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Vips implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "vid", type = IdType.ASSIGN_ID)
    private String vid;

    private String vpwd;

    private String vname;

    private String vtel;

    private String vaddress;

    private Integer rid;

    private Integer vbalance;

    private Integer vicemoney;

    private String vemail;

    private String vbackup;


}

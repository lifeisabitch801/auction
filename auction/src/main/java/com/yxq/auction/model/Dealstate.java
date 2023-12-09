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
public class Dealstate implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "did", type = IdType.ASSIGN_ID)
    private Integer did;

    private String dname;

    private String dbackup;


}

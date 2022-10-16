package com.code.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
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
 * @author xiaoshuai
 * @since 2022-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSutdentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sutdent_item_id", type = IdType.AUTO)
    private Integer sutdentItemId;

    private Integer itemId;

    private Integer studentId;

    private String content;

    private Integer score;

    private Date fillTime;


}

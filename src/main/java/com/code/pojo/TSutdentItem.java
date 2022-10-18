package com.code.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @author xiaoshuai
 * @since 2022-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSutdentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sutdent_item_id", type = IdType.AUTO)
    private Long sutdentItemId;

    private Long itemId;

    private Integer studentId;

    private String content;

    private Integer score;

    private Date fillTime;


}

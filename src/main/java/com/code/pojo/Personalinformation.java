package com.code.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class Personalinformation implements Serializable {

    private Long teacherId;

    private Long studentId;

    private String piEmail;

    private String piSchool;

    @TableId(value = "pi_uid",type = IdType.INPUT)
    private String piUid;

    private String piRole;

    private String piPhone;


}

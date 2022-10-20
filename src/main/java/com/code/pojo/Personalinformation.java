package com.code.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Personalinformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer teacherId;

    private Integer studentId;

    private String piEmail;

    private String piSchool;

    private String piUid;

    private String piRole;

    private String piPhone;


}

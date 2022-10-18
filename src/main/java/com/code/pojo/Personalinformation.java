package com.code.pojo;

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

    private long serialVersionUID;

    private Integer teacherId;

    private Integer studentId;

    private String piEmail;

    private String piSchool;

    private String piUid;

    private String piRole;

    private String piPhone;


}

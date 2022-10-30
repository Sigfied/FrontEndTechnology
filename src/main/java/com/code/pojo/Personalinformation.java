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
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Personalinformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private long teacherId;

    private long studentId;

    private String piEmail;

    private String piSchool;

    private String piUid;

    private String piRole;

    private String piPhone;

    private String clazzNo;

}

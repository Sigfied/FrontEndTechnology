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
public class Testcase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "testcase_id", type = IdType.AUTO)
    private Long testcaseId;

    private Long itemId;

    private String testcaseInput;

    private String testcaseOutput;

    private String testcaseName;

    private Integer testcaseGrade;

    private Integer flag;


}

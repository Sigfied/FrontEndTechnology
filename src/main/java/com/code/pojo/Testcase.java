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
 * @since 2022-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Testcase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "testcase_id", type = IdType.AUTO)
    private Integer testcaseId;

    private Integer itemId;

    private String testcaseInput;

    private String testcaseOutput;

    private String testcaseName;

    private Integer testcaseGrade;

    private Integer flag;


}

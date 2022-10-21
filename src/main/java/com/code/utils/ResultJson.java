package com.code.utils;

import lombok.Data;

/**
 * 统一返回信息包装类
 *
 * @author 笑小枫
 * @date 2022/07/15
 */
@Data
public class ResultJson {

    private static final String SUCCESS_CODE = "200";
    private static final String FAILURE_CODE = "100";
    /**
     * 状态码 正确为200
     */
    private String code;

    /**
     * 返回提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    public ResultJson() {
        this.code = SUCCESS_CODE;
        this.msg = "";
    }

    public ResultJson(Object data) {
        this.code = SUCCESS_CODE;
        this.msg = "";
        this.data = data;
    }

    public ResultJson(String msg) {
        this.code = FAILURE_CODE;
        this.msg = msg;

    }

    /**
     * 如果返回状态码非0000，且接口状态为成功，请使用这个
     *
     * @param code 返回code值
     * @param msg 返回消息
     * @param data 返回数据
     */
    public ResultJson(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}

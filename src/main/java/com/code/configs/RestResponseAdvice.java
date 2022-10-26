package com.code.configs;

import com.alibaba.fastjson.JSON;
import com.code.utils.ResultJson;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对返回结果统一进行处理，包括返回结果格式统一包装，返回异常统一处理
 *
 * @author GYJ
 * @date 2022/10/17
 */

@RestControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {


    private static final Logger log = LoggerFactory.getLogger(RestResponseAdvice.class);
    @Override
    public boolean supports(@NotNull MethodParameter returnType,
                            @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 返回结果包装统一返回格式
     *
     * @return 包装后的返回结果
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NotNull MethodParameter returnType,
                                  @NotNull MediaType selectedContentType,
                                  @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NotNull ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 指定返回的结果为application/json格式
        // 不指定，String类型转json后返回Content-Type是text/plain;charset=UTF-8\
        log.info("beforeBodyWrite: {}",body);
        if (body==null){
            return null;
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

//        if(body == null){
//            return new ResultJson("The result is null");
//        }
//        else
            if (body instanceof ResultJson) {
            return body;
        } else if (body instanceof String) {
            return JSON.toJSONString(body);
        }
        else if (body instanceof Exception){
            return new ResultJson(((Exception) body).getMessage());
        }
        return new ResultJson(body);
    }
}

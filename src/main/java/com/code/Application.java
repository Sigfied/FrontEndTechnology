package com.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lenovo
 */
@SpringBootApplication
@MapperScan("com.code.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);

    }
}

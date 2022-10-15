package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.pojo.Msg;
import com.code.pojo.TStudent;
import com.code.service.TStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-14
 */
@RestController
@RequestMapping("/user")
public class TStudentController {

    @Autowired
    private TStudentService tStudentService;


    @ResponseBody
    @RequestMapping("/login")
    public Msg login(@RequestBody Map<String,Object> map){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("student_no",map.get("no").toString());
        queryWrapper.eq("student_password",map.get("password").toString());
        TStudent t = tStudentService.getOne(queryWrapper);
        if(t==null)
        {
            return Msg.fail().add("user",null);
        }
        return Msg.success().add("user", t);
    }






}


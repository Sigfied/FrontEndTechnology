package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.pojo.Msg;
import com.code.pojo.TStudent;
import com.code.pojo.Teacher;
import com.code.service.TStudentService;
import com.code.service.TeacherService;
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
    @Autowired
    private TeacherService teacherService;


    @ResponseBody
    @RequestMapping("/login")
    public Msg login(@RequestBody Map<String,Object> map){

        String no = map.get("no").toString();
        String pwd = map.get("pwd").toString();

        LambdaQueryWrapper<TStudent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TStudent::getStudentNo,no);
        lambdaQueryWrapper.eq(TStudent::getStudentPassword,pwd);

        LambdaQueryWrapper<Teacher> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Teacher::getTeacherNo,no);
        lambdaQueryWrapper1.eq(Teacher::getTeacherPassword,pwd);

        TStudent tStudent = tStudentService.getOne(lambdaQueryWrapper);
        Teacher teacher = teacherService.getOne(lambdaQueryWrapper1);

        if(tStudent==null){
            if(teacher==null){
                return Msg.fail();
            }
            return Msg.success().add("teacher",teacher);
        }
        return Msg.success().add("student",tStudent);
    }






}


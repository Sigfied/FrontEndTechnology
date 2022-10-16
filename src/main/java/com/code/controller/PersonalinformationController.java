package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.pojo.Msg;
import com.code.pojo.Personalinformation;
import com.code.pojo.TStudent;
import com.code.service.PersonalinformationService;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-15
 */
@RestController
@RequestMapping("/user")
public class PersonalinformationController {

    @Autowired
    private PersonalinformationService personalinformationService;

    @ResponseBody
    @RequestMapping("account")
    public Msg getPersonalInformation(@RequestBody Map<String,Object> map) {

        String teacher_id = map.get("teacher_id").toString();
        String student_id = map.get("student_id").toString();

        LambdaQueryWrapper<Personalinformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(teacher_id.equals(""))
        {

            lambdaQueryWrapper.eq(Personalinformation::getStudentId,student_id);
            Personalinformation personalinformation = personalinformationService.getOne(lambdaQueryWrapper);

            if(personalinformation==null){
                return Msg.fail();
            }
            return Msg.success().add("pi",personalinformation);

        }
        else{
            lambdaQueryWrapper.eq(Personalinformation::getTeacherId,teacher_id);
            Personalinformation personalinformation = personalinformationService.getOne(lambdaQueryWrapper);
            if(personalinformation==null){
                return Msg.fail();
            }
            return Msg.success().add("pi",personalinformation);
        }

    }

}


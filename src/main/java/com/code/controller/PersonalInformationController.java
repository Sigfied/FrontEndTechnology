package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.Personalinformation;
import com.code.service.PersonalinformationService;
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
public class PersonalInformationController {


    private final PersonalinformationService personalinformationService;

    @Autowired
    public PersonalInformationController(PersonalinformationService personalinformationService) {
        this.personalinformationService = personalinformationService;
    }

    @ResponseBody
    @RequestMapping("account")
    public Personalinformation getPersonalInformation(@RequestBody Map<String,Object> map) {

        String teacherId = map.get("teacher_id").toString();
        String studentId = map.get("student_id").toString();

        LambdaQueryWrapper<Personalinformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if("".equals(teacherId))
        {
            lambdaQueryWrapper.eq(Personalinformation::getStudentId,studentId);

        }
        else{
            lambdaQueryWrapper.eq(Personalinformation::getTeacherId,teacherId);
        }
        return personalinformationService.getOne(lambdaQueryWrapper);

    }

}


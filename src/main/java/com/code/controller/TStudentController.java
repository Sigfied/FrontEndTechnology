package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.Msg;
import com.code.pojo.Personalinformation;
import com.code.pojo.TStudent;
import com.code.pojo.Teacher;
import com.code.service.PersonalinformationService;
import com.code.service.TStudentService;
import com.code.service.TeacherService;
import com.code.utils.MailUtils;
import com.code.utils.MathUtils;
import com.code.utils.VerCodeGenerateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
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
@CacheConfig(cacheNames = "user")
public class TStudentController {


    private TStudentService tStudentService;

    private TeacherService teacherService;

    private final RedisTemplate redisTemplate;

    private final PersonalinformationService personService;

    private Logger logger = LoggerFactory.getLogger(TStudentController.class);

    @Autowired
    public TStudentController(TStudentService tStudentService, TeacherService teacherService, RedisTemplate redisTemplate, PersonalinformationService personService) {
        this.tStudentService = tStudentService;
        this.teacherService = teacherService;
        this.redisTemplate = redisTemplate;
        this.personService = personService;
    }

    @RequestMapping("/login")
    public Object login(@RequestBody Map<String,Object> map){

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
            return teacher;
        }
        return tStudent;
    }


    @PostMapping("/register")
    public TStudent register(@RequestBody Map<String,String> params){
        String email = params.get("email");
        String pwd = params.get("password");
        String captcha = params.get("captcha");
        TStudent student = null;
        if(redisTemplate.opsForValue().get(email) != null && captcha.equals(redisTemplate.opsForValue().get(email))){
            student = new TStudent();
            //这里使用UUID作为唯一id
            student.setStudentId(Long.valueOf(MathUtils.getPrimaryKey()));
            student.setStudentPassword(pwd);
            logger.info("Student:\t{}",student);
            tStudentService.saveOrUpdate(student);
            Personalinformation ps = new Personalinformation();
            ps.setPiEmail(email);
            ps.setPiUid(MathUtils.getPrimaryKey());
            personService.saveOrUpdate(ps);
            return student;
        }
        else{
            return null;
        }
    }

    @GetMapping("/captcha")
    public String getCaptcha(@RequestBody Map<String,String> params) throws Exception {
       String email = params.get("email");
       String code = VerCodeGenerateUtil.generateVerCode();
       String rs = "Successfully";
       try {
           MailUtils.sendMail(email,code);
       }
       catch (Exception e){
           rs = "Error: " + e.getMessage();
       }
       redisTemplate.opsForValue().set(email,code);
       return rs;
    }


}


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
import java.util.Objects;
import java.util.Optional;

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

    private final PersonalinformationService personalinformationService;
    private final TStudentService tStudentService;

    private final TeacherService teacherService;

    private final RedisTemplate<String,Object> redisTemplate;

    private final PersonalinformationService personService;

    private final Logger logger = LoggerFactory.getLogger(TStudentController.class);

    @Autowired
    public TStudentController(TStudentService tStudentService, TeacherService teacherService, RedisTemplate<String,Object> redisTemplate, PersonalinformationService personService,PersonalinformationService personalinformationService) {
        this.tStudentService = tStudentService;
        this.teacherService = teacherService;
        this.redisTemplate = redisTemplate;
        this.personService = personService;
        this.personalinformationService = personalinformationService;
    }

    @ResponseBody
    @RequestMapping("/login")
    public Object login(@RequestBody Map<String,Object> map){
        //学号、教师号、邮箱
        String no = map.get("no").toString();
        String pwd = map.get("pwd").toString();

        LambdaQueryWrapper<TStudent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TStudent::getStudentNo,no);
        lambdaQueryWrapper.eq(TStudent::getStudentPassword,pwd);

        LambdaQueryWrapper<Teacher> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Teacher::getTeacherNo,no);
        lambdaQueryWrapper1.eq(Teacher::getTeacherPassword,pwd);

        LambdaQueryWrapper<Personalinformation> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Personalinformation::getPiEmail,no);

        TStudent tStudent = tStudentService.getOne(lambdaQueryWrapper);
        Teacher teacher = teacherService.getOne(lambdaQueryWrapper1);
        Personalinformation personalinformation = personalinformationService.getOne(lambdaQueryWrapper2);

        if(tStudent==null){
            if(teacher==null){
                if(personalinformation!=null){
                    long studentId = personalinformation.getStudentId();
                    LambdaQueryWrapper<TStudent> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper3.eq(TStudent::getStudentPassword,pwd);
                    lambdaQueryWrapper3.eq(TStudent::getStudentId,studentId);
                    TStudent tStudent1 = tStudentService.getOne(lambdaQueryWrapper3);
                    return Objects.requireNonNullElseGet(tStudent1, Msg::fail);
                }
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
        TStudent student;
        if(redisTemplate.opsForValue().get(email) != null && captcha.equals(redisTemplate.opsForValue().get(email))){
            student = new TStudent();
            //这里使用UUID作为唯一id
            student.setStudentId(Integer.parseInt(MathUtils.getPrimaryKey()));
            student.setStudentPassword(pwd);
            logger.info("Student:\t{}",student);
            tStudentService.saveOrUpdate(student);
            student.setStudentPassword(null);
            Personalinformation ps = new Personalinformation();
            ps.setPiEmail(email);
            ps.setPiUid(MathUtils.getPrimaryKey());
            ps.setStudentId(student.getStudentId());
            personService.saveOrUpdate(ps);
            return student;
        }
        else{
            return null;
        }
    }

    @GetMapping("/captcha")
    public String getCaptcha(@RequestBody Map<String,String> params) {
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

    @PutMapping("/update")
    public String update(@RequestBody Map<String,String> params){
        String studentId = params.get("studentId");
        String studentName = params.get("studentName");
        String studentNo = params.get("studentNo");
        String school = params.get("school");
        String phone = params.get("phone");

        LambdaQueryWrapper<TStudent>  lmS = new LambdaQueryWrapper<>();
        lmS.eq(TStudent::getStudentId, studentId);
        TStudent student = tStudentService.getOne(lmS);
        //使用 Optional 进行判空
        student = Optional.ofNullable(student).orElse(new TStudent());
        student.setStudentNo(studentNo);
        student.setStudentName(studentName);
        tStudentService.saveOrUpdate(student);

        LambdaQueryWrapper<Personalinformation> lmP = new LambdaQueryWrapper<>();
        lmP.eq(Personalinformation::getStudentId, studentId);
        Personalinformation one = personService.getOne(lmP);
        one = Optional.ofNullable(one).orElse(new Personalinformation());
        one.setPiPhone(phone);
        one.setPiSchool(school);

        personService.saveOrUpdate(one);
        return "success";
    }
}


package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.Msg;
import com.code.pojo.TStudent;
import com.code.pojo.Teacher;
import com.code.service.TStudentService;
import com.code.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Autowired
    public TStudentController(TStudentService tStudentService, TeacherService teacherService, RedisTemplate redisTemplate) {
        this.tStudentService = tStudentService;
        this.teacherService = teacherService;
        this.redisTemplate = redisTemplate;
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
        String pwd = params.get("pwd");
        String captcha = params.get("captcha");
        TStudent student = null;
        if(redisTemplate.opsForValue().get(email) != null && captcha.equals(redisTemplate.opsForValue().get(email))){
            student = new TStudent();
            //这里使用UUID作为唯一id
            student.setStudentId(generateShortId().intValue());
            student.setStudentPassword(pwd);
            return student;
        }
        else{
            return null;
        }
    }

    @GetMapping("/captcha")
    public Object getCaptcha(@RequestBody Map<String,String> params){
       String email = params.get("email");
       return redisTemplate.opsForValue().get(email);
    }

    private static Long generateShortId() {
        // 2 位年份后两位 22001 后五位走随机  每天清一次缓存 99999 10
        StringBuilder idSb = new StringBuilder();
        /// 年份后两位  和  一年中的第几天
        LocalDate now = LocalDate.now();
        String year = now.getYear() + "";
        year = year.substring(2);
        String day = now.getDayOfYear() + "";
        /// 补0
        if (day.length() < 3) {
            StringBuilder sb = new StringBuilder();
            sb.append("0".repeat(3 - day.length()));
            day = sb.append(day).toString();
        }
        idSb.append(year).append(day);
        /// 后五位补随机数
        for (int i = idSb.length(); i < 10; i++) {
            idSb.append((int) (Math.random() * 10));
        }
        return Long.parseLong(idSb.toString());
    }

}


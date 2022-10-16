package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.pojo.*;
import com.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/itemSet")
public class ItemsetController {

    @Autowired
    private ItemsetService itemsetService;
    @Autowired
    private ClazzStudentService clazzStudentService;
    @Autowired
    private ClazzTeacherService clazzTeacherService;
    @Autowired
    private TeacherTopicsetService teacherTopicsetService;
    @Autowired
    private TSutdentItemService tSutdentItemService;

    @ResponseBody
    @RequestMapping("my_set")
    public Msg getMySet(@RequestBody Map<String,Object> map){

        String student_id = map.get("student_id").toString();
        LambdaQueryWrapper<ClazzStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClazzStudent::getStudentId,student_id);
        ClazzStudent clazzStudent = clazzStudentService.getOne(queryWrapper);

        String clazz_no = clazzStudent.getClazzNo();
        LambdaQueryWrapper<ClazzTeacher> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ClazzTeacher::getClazzNo,clazz_no);
        ClazzTeacher clazzTeacher = clazzTeacherService.getOne(queryWrapper1);

        int teacher_id = clazzTeacher.getTeacherId();
        LambdaQueryWrapper<TeacherTopicset> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(TeacherTopicset::getTeacherId,teacher_id);
        List<TeacherTopicset> list = teacherTopicsetService.list(queryWrapper2);

        if(list.equals(null))
        {
            return Msg.fail().add("mySet",null);
        }


        LambdaQueryWrapper<Itemset> queryWrapper3 = new LambdaQueryWrapper<>();

        List<Integer> num = new ArrayList<>();
        for (TeacherTopicset node : list) {
            num.add(node.getItemsetId());
        }
        queryWrapper3.in(Itemset::getItemsetId,num);
        List<Itemset> itemSetList = itemsetService.list(queryWrapper3);

        return Msg.success().add("public_set",itemSetList);


    }


    @ResponseBody
    @RequestMapping("public_set")
    public Msg getPublicSet(){

        LambdaQueryWrapper<Itemset> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Itemset::getItemsetStatus,1);
        List<Itemset> itemSet = itemsetService.list(lambdaQueryWrapper);

        return Msg.success().add("public_set",itemSet);

    }


    @ResponseBody
    @RequestMapping("itemSet_info")
    public Msg getItemSetInfo(@RequestBody Map<String,Object> map){
        String itemSet_id = map.get("itemSet_id").toString();
        LambdaQueryWrapper<Itemset> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Itemset::getItemsetId,itemSet_id);
        Itemset itemset = itemsetService.getOne(lambdaQueryWrapper);

        String student_id = map.get("student_id").toString();
        LambdaQueryWrapper<TSutdentItem> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(TSutdentItem::getStudentId,student_id);

        List<TSutdentItem> list = tSutdentItemService.list(lambdaQueryWrapper1);

        return Msg.success().add("itemSet",itemset).add("student",list);

    }



}


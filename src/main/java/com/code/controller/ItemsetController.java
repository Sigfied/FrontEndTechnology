package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.*;
import com.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final ItemsetService itemsetService;

    private final ClazzStudentService clazzStudentService;

    private final ClazzTeacherService clazzTeacherService;

    private final TeacherTopicsetService teacherTopicsetService;

    private final TSutdentItemService tSutdentItemService;

    @Autowired
    public ItemsetController(ItemsetService itemsetService, ClazzStudentService clazzStudentService,
                             ClazzTeacherService clazzTeacherService, TeacherTopicsetService teacherTopicsetService,
                             TSutdentItemService tSutdentItemService) {
        this.itemsetService = itemsetService;
        this.clazzStudentService = clazzStudentService;
        this.clazzTeacherService = clazzTeacherService;
        this.teacherTopicsetService = teacherTopicsetService;
        this.tSutdentItemService = tSutdentItemService;
    }

    @ResponseBody
    @RequestMapping("my_set")
    public List<Itemset> getMySet(@RequestBody Map<String,Object> map){

        String studentId = map.get("student_id").toString();
        LambdaQueryWrapper<ClazzStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClazzStudent::getStudentId,studentId);
        ClazzStudent clazzStudent = clazzStudentService.getOne(queryWrapper);

        String clazzNo = clazzStudent.getClazzNo();
        LambdaQueryWrapper<ClazzTeacher> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ClazzTeacher::getClazzNo,clazzNo);
        ClazzTeacher clazzTeacher = clazzTeacherService.getOne(queryWrapper1);

        Long teacherId = clazzTeacher.getTeacherId();
        LambdaQueryWrapper<TeacherTopicset> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(TeacherTopicset::getTeacherId,teacherId);
        List<TeacherTopicset> list = teacherTopicsetService.list(queryWrapper2);

        if(list == null)
        {
            return null;
        }


        LambdaQueryWrapper<Itemset> queryWrapper3 = new LambdaQueryWrapper<>();

        List<Long> num = new ArrayList<>();
        for (TeacherTopicset node : list) {
            num.add(node.getItemsetId());
        }
        queryWrapper3.in(Itemset::getItemsetId,num);
        return itemsetService.list(queryWrapper3);


    }


    @ResponseBody
    @RequestMapping("public_set")
    public List<Itemset> getPublicSet(){

        LambdaQueryWrapper<Itemset> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Itemset::getItemsetStatus,1);
       return itemsetService.list(lambdaQueryWrapper);


    }

    @ResponseBody
    @RequestMapping("itemSet_info")
    public Map<String, Object> getItemSetInfo(@RequestBody Map<String,Object> map){
        String itemSetId = map.get("itemSet_id").toString();
        LambdaQueryWrapper<Itemset> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Itemset::getItemsetId,itemSetId);
        Itemset itemset = itemsetService.getOne(lambdaQueryWrapper);

        String studentId = map.get("student_id").toString();
        LambdaQueryWrapper<TSutdentItem> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(TSutdentItem::getStudentId,studentId);

        List<TSutdentItem> list = tSutdentItemService.list(lambdaQueryWrapper1);
        Map<String,Object> hashMap = new HashMap<>(2);
        hashMap.put("itemSet",itemset);
        hashMap.put("student",list);
        return hashMap;
    }

}


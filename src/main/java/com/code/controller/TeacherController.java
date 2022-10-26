package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.*;
import com.code.service.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-16
 */
@CrossOrigin(origins = {"*", "null"})
@RestController
@RequestMapping("/teacher")
public class TeacherController {


    private final Logger log = LoggerFactory.getLogger(TeacherController.class);
    private final ClazzTeacherService clazzTeacherService;

    private final ItemService itemService;

    private final ClazzService clazzService;
    private final TeacherTopicsetService teacherTopicsetService;

    private final ItemsetService itemsetService;

    @Autowired
    public TeacherController(ClazzTeacherService clazzTeacherService, ItemService itemService, ClazzService clazzService, TeacherTopicsetService teacherTopicsetService, ItemsetService itemsetService) {
        this.clazzTeacherService = clazzTeacherService;
        this.itemService = itemService;
        this.clazzService = clazzService;
        this.teacherTopicsetService = teacherTopicsetService;
        this.itemsetService = itemsetService;
    }


    @GetMapping("/info")
    public Map<String, Object> getTeacherInfo(@RequestBody Map<String, Object> params) throws Exception {
        String teacherId = (String) params.get("teacherId");
        if (teacherId == null || teacherId.isEmpty()) {
            throw new Exception("TeacherId is requireNonNullElseGet");
        }
        Map<String, Object> infoMap = new HashMap<>(8);
        List<Clazz> clazzList = getClazzList(teacherId);


        infoMap.put("clazzList", clazzList);
        infoMap.put("clazzNum", clazzList.size());

        List<Itemset> itemSetList = getItemSetList(teacherId);

        infoMap.put("itemSetList", itemSetList);
        infoMap.put("itemSetNum", itemSetList.size());

        List<Item> itemList = getItemList(teacherId);

        infoMap.put("itemList", itemList);
        infoMap.put("itemNum", itemList.size());

        return infoMap;
    }



    @NotNull
    private List<Itemset> getItemSetList(String teacherId) {
        LambdaQueryWrapper<TeacherTopicset> wrapperOfTeacherTopicSet = new LambdaQueryWrapper<>();
        wrapperOfTeacherTopicSet.eq(TeacherTopicset::getTeacherId, teacherId);
        List<TeacherTopicset> itemIdList = teacherTopicsetService.list(wrapperOfTeacherTopicSet);
        itemIdList = Optional.ofNullable(itemIdList).orElse(new ArrayList<>());
        List<Long> list = itemIdList.stream().map(TeacherTopicset::getItemsetId).toList();
        LambdaQueryWrapper<Itemset> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Itemset::getItemsetId, list);
        List<Itemset> itemSetList = itemsetService.list(wrapper);
        itemSetList = Optional.ofNullable(itemSetList).orElse(new ArrayList<>());
        return itemSetList;
    }

    @NotNull
    private List<Clazz> getClazzList(String teacherId) {
        LambdaQueryWrapper<ClazzTeacher> wrapperOfClazzTeacher = new LambdaQueryWrapper<>();
        wrapperOfClazzTeacher.eq(ClazzTeacher::getTeacherId, teacherId);
        var clazzNoList = clazzTeacherService.list(wrapperOfClazzTeacher);
        //可能报空指针异常
        clazzNoList = Optional.ofNullable(clazzNoList).orElse(new ArrayList<>());
        List<String> list = clazzNoList.stream().map(ClazzTeacher::getClazzNo).toList();
        LambdaQueryWrapper<Clazz> wrapperOfClazz = new LambdaQueryWrapper<>();
        wrapperOfClazz.in(Clazz::getClazzNo, list);
        List<Clazz> clazzList = clazzService.list(wrapperOfClazz);
        clazzList = Optional.ofNullable(clazzList).orElse(new ArrayList<>());
        return clazzList;
    }

    @NotNull
    private List<Item> getItemList(String teacherId){
        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<>();
        List<Item> list = itemService.list(wrapper);
        list = Optional.ofNullable(list).orElse(new ArrayList<>());
        return list;
    }

}


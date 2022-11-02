package com.code.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.*;
import com.code.service.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    private final PersonalinformationService infoService;

    private final ItemItemsetService itemItemSetService;

    private final TeacherService teacherService;

    private final TStudentService studentService;

    @Autowired
    public TeacherController(ClazzTeacherService clazzTeacherService, ItemService itemService, ClazzService clazzService, TeacherTopicsetService teacherTopicsetService, ItemsetService itemsetService, PersonalinformationService infoService, ItemItemsetService itemItemSetService, TeacherService teacherService, TStudentService studentService) {
        this.clazzTeacherService = clazzTeacherService;
        this.itemService = itemService;
        this.clazzService = clazzService;
        this.teacherTopicsetService = teacherTopicsetService;
        this.itemsetService = itemsetService;
        this.infoService = infoService;
        this.itemItemSetService = itemItemSetService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }


    @PostMapping("/info")
    public Map<String, Object> getTeacherInfo(@RequestBody Map<String, Object> params) throws Exception {
        String teacherId = String.valueOf(params.get("teacherId"));
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

        Personalinformation teacherInfo = getTeacherInfo(teacherId);
        infoMap.put("info", teacherInfo);

        LambdaQueryWrapper<Teacher> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(Teacher::getTeacherId, teacherId);
        Teacher teacher = teacherService.getOne(teacherWrapper);
        teacher = Optional.ofNullable(teacher).orElse(new Teacher());
        infoMap.put("teacherName", teacher.getTeacherName());
        return infoMap;
    }


    @PostMapping("/clazzInfo")
    public Map<String, Object> getClassInfo(@RequestBody Map<String, Object> params) throws Exception {
        String teacherId = String.valueOf(params.get("teacherId"));
        if (teacherId == null || teacherId.isEmpty()) {
            throw new Exception("TeacherId is requireNonNullElseGet");
        }
        List<Clazz> clazzList = getClazzList(teacherId);
        List<String> clazzNoList = clazzList.stream().map(Clazz::getClazzNo).collect(Collectors.toList());

        LambdaQueryWrapper<Personalinformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Personalinformation::getClazzNo, clazzNoList);
        List<Personalinformation> infoList = infoService.list(queryWrapper);
        infoList = Optional.ofNullable(infoList).orElse(new ArrayList<>());
        List<Long> studentIdList = infoList.stream().map(Personalinformation::getStudentId).collect(Collectors.toList());

        LambdaQueryWrapper<TStudent> queryStudent = new LambdaQueryWrapper<>();
        queryStudent.in(TStudent::getStudentId, studentIdList);
        List<TStudent> studentList = studentService.list(queryStudent);

        Map<String, Object> res = new HashMap<>(16);
        List<Object> list = new ArrayList<>();
        clazzList.forEach(v -> {
            List<JSONObject> cl = new ArrayList<>();
            studentList.forEach(s -> {
                if (v.getClazzNo().equals(s.getClazzNo())) {
                    JSONObject r = new JSONObject();
                    r.put("studentId", s.getStudentId());
                    r.put("studentName", s.getStudentName());
                    r.put("clazzNo", s.getClazzNo());
                    cl.add(r);
                }
            });
            list.add(cl);
        });
        res.put("students", list);
        return res;
    }


    private Personalinformation getTeacherInfo(String teacherId) {
        LambdaQueryWrapper<Personalinformation> teacherInfoWrapper = new LambdaQueryWrapper<>();
        teacherInfoWrapper.eq(Personalinformation::getTeacherId, teacherId);
        return infoService.getOne(teacherInfoWrapper);
    }

    @NotNull
    private List<Itemset> getItemSetList(String teacherId) {
        LambdaQueryWrapper<TeacherTopicset> wrapperOfTeacherTopicSet = new LambdaQueryWrapper<>();
        wrapperOfTeacherTopicSet.eq(TeacherTopicset::getTeacherId, teacherId);
        List<TeacherTopicset> itemIdList = teacherTopicsetService.list(wrapperOfTeacherTopicSet);
        itemIdList = Optional.ofNullable(itemIdList).orElse(new ArrayList<>());
        List<Long> list = itemIdList.stream().map(TeacherTopicset::getItemsetId).collect(Collectors.toList());
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
        List<ClazzTeacher> clazzNoList = clazzTeacherService.list(wrapperOfClazzTeacher);
        //可能报空指针异常
        clazzNoList = Optional.ofNullable(clazzNoList).orElse(new ArrayList<>());
        List<String> list = clazzNoList.stream().map(ClazzTeacher::getClazzNo).collect(Collectors.toList());
        LambdaQueryWrapper<Clazz> wrapperOfClazz = new LambdaQueryWrapper<>();
        wrapperOfClazz.in(Clazz::getClazzNo, list);
        List<Clazz> clazzList = clazzService.list(wrapperOfClazz);
        clazzList = Optional.ofNullable(clazzList).orElse(new ArrayList<>());
        return clazzList;
    }

    @NotNull
    private List<Item> getItemList(String teacherId) {
        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ItemItemset> wrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TeacherTopicset> teacherTopicSetLambdaQueryWrapper = new LambdaQueryWrapper<>();

        teacherTopicSetLambdaQueryWrapper.eq(TeacherTopicset::getTeacherId, teacherId);
        wrapper1.in(ItemItemset::getItemsetId, teacherTopicsetService.list(teacherTopicSetLambdaQueryWrapper));

        wrapper.in(Item::getItemId, itemItemSetService.list(wrapper1).stream().map(ItemItemset::getItemId).collect(Collectors.toList()));

        List<Item> list = itemService.list(wrapper);
        list = Optional.ofNullable(list).orElse(new ArrayList<>());
        return list;
    }


    @PostMapping("/getItemInfo")
    public Map<String, Object> getItemListBySetId(@RequestBody Map<String, Object> params) {
        Long itemSetId = Long.parseLong(String.valueOf(params.get("itemSetId")));
        LambdaQueryWrapper<ItemItemset> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ItemItemset::getItemsetId, itemSetId);
        List<Long> itemIdList = itemItemSetService.list(wrapper1).stream().map(ItemItemset::getItemId).collect(Collectors.toList());

        LambdaQueryWrapper<Item> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(Item::getItemId,itemIdList);
        List<Item> itemList = itemService.list(itemWrapper);
        Map<String, Object> result = new HashMap<>(2);

        result.put("itemList",itemList);
        return result;
    }
}


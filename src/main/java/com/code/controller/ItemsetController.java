package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.*;
import com.code.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-15
 */
@CrossOrigin(origins = {"*","null"})
@RestController
@RequestMapping("/itemSet")
public class ItemsetController {

    private final ItemsetService itemsetService;

    private final TStudentService tStudentService;

    private final ClazzTeacherService clazzTeacherService;

    private final TeacherTopicsetService teacherTopicsetService;

    private final TSutdentItemService tSutdentItemService;

    private final ItemItemsetService itemItemsetService;

    private final ItemService itemService;

    @Autowired
    public ItemsetController(ItemsetService itemsetService,TStudentService tStudentService,ItemItemsetService itemItemsetService,
                             ClazzTeacherService clazzTeacherService, TeacherTopicsetService teacherTopicsetService,
                             TSutdentItemService tSutdentItemService,ItemService itemService) {
        this.itemsetService = itemsetService;
        this.tStudentService = tStudentService;
        this.clazzTeacherService = clazzTeacherService;
        this.teacherTopicsetService = teacherTopicsetService;
        this.tSutdentItemService = tSutdentItemService;
        this.itemItemsetService = itemItemsetService;
        this.itemService = itemService;
    }

    @ResponseBody
    @RequestMapping("my_set")
    public List<Itemset> getMySet(@RequestBody Map<String,Object> map){

        String studentId = map.get("student_id").toString();
        LambdaQueryWrapper<TStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TStudent::getStudentId,studentId);
        TStudent tStudent = tStudentService.getOne(queryWrapper);


        String clazzNo = tStudent.getClazzNo();
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
        //去重
        List<TSutdentItem> newList = list.stream().collect(Collectors
                .collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TSutdentItem::getItemId))),
                        ArrayList::new));
        Map<String,Object> hashMap = new HashMap<>(2);
        hashMap.put("itemSet",itemset);
        hashMap.put("student",newList);
        return hashMap;
    }


    @ResponseBody
    @RequestMapping("itemSet_list")
    public Map<String,Object> getItemSet_list(@RequestBody Map<String, Object>map){
//        String student_id = map.get("student_id").toString();

        String itemSet_id = map.get("itemSet_id").toString();

        LambdaQueryWrapper<ItemItemset> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ItemItemset::getItemsetId,itemSet_id);
        List<ItemItemset> itemItemsetList = itemItemsetService.list(lambdaQueryWrapper);

        List<Item> itemList = new ArrayList<>();

        for (ItemItemset itemItemset:itemItemsetList) {
            LambdaQueryWrapper<Item> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Item::getItemId,itemItemset.getItemId());
            itemList.add(itemService.getOne(lambdaQueryWrapper1));

        }

        Map<String,Object> hashMap = new HashMap<>(2);
        hashMap.put("itemItemsetList",itemItemsetList);
        hashMap.put("itemList",itemList);

        return hashMap;

    }

}


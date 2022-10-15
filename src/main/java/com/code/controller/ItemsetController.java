package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.code.pojo.Itemset;
import com.code.pojo.Msg;
import com.code.service.ItemsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;
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
@RequestMapping("/itemset")
public class ItemsetController {

    @Autowired
    private ItemsetService itemsetService;

    @ResponseBody
    @RequestMapping("public_set")
    public Msg getPublicSet(){

        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("itemset_status","1");
        List<Itemset> itemset = itemsetService.list(queryWrapper);

        return Msg.success().add("public_set",itemset);


    }



}


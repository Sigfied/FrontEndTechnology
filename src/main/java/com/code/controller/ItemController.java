package com.code.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.Item;
import com.code.pojo.TSutdentItem;
import com.code.pojo.Testcase;
import com.code.service.ItemService;
import com.code.service.TSutdentItemService;
import com.code.service.TestcaseService;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-16
 */
@CrossOrigin(origins = {"*","null"},allowedHeaders = "*")
@RestController
@RequestMapping("/item")
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ItemService itemService;

    private final TestcaseService testcaseService;


    private final TSutdentItemService tSutdentItemService;


    @Autowired
    public ItemController(TSutdentItemService tSutdentItemService,TestcaseService testcaseService,
                          ItemService itemService){

        this.itemService = itemService;
        this.testcaseService = testcaseService;
        this.tSutdentItemService = tSutdentItemService;
    }

    @ResponseBody
    @RequestMapping("item_info")
    public Item getItem(@RequestBody Map<String, Object>map){
        String itemId = map.get("item_id").toString();
        LambdaQueryWrapper<Item> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Item::getItemId,itemId);
        return itemService.getOne(lambdaQueryWrapper);
    }


    @PostMapping("submissions")
    public List<Testcase> sumbit(@RequestBody Map<String, Object>map){
        log.info("submit{}",map);
        String student_id = map.get("student_id").toString();
        String item_id = map.get("item_id").toString();
        String code = map.get("code").toString();
        String lang = map.get("lang").toString();

        LambdaQueryWrapper<Testcase> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Testcase::getItemId,item_id);
        List<Testcase> testcaseList = testcaseService.list(lambdaQueryWrapper);
        //总得分
        int sum=0;
        for (Testcase testcase :testcaseList) {
//            跑代码,得分放在testcase中测试点的testcase.setTestcaseGrade(),并算总分sum
            boolean flag = run(lang,code,testcase.getTestcaseInput(),testcase.getTestcaseOutput());
            if(flag){
                sum+=testcase.getTestcaseGrade();
            }
        }

        TSutdentItem tSutdentItem = new TSutdentItem();

        tSutdentItem.setItemId(Long.parseLong(item_id));
        tSutdentItem.setStudentId(Integer.parseInt(student_id));
        tSutdentItem.setContent(code);
        tSutdentItem.setScore(sum);
        tSutdentItem.setFillTime(new Date());
        tSutdentItemService.save(tSutdentItem);
        return testcaseList;

    }

    public boolean run(String lang,String code,String input,String output){

        input = input.replace("\\n","\n");
        output = output.replace("\\n","\n");


        JSONObject json = new JSONObject();
        json.put("lang",lang);
        json.put("code",code);
        json.put("input",input);
        log.info(String.valueOf(json));
        String res = "";

        res = Unirest.post("http://114.132.64.132:8000/run/code")
                .header("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .header("Host", "114.132.64.132:8000")
                .header("Connection", "keep-alive")
                .body(json)
                .asString().getBody();

        JSONObject jsonObject = JSON.parseObject(res);
        log.info(res);
        String data = (String) jsonObject.get("data");
        String time = (String) jsonObject.get("time");
        if(data == null){
            return false;
        }
        return data.equals(output);


    }


}


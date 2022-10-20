package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.Item;
import com.code.pojo.ItemItemset;
import com.code.pojo.TSutdentItem;
import com.code.pojo.Testcase;
import com.code.service.ItemItemsetService;
import com.code.service.ItemService;
import com.code.service.TSutdentItemService;
import com.code.service.TestcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/item")
public class ItemController {


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




    @ResponseBody
    @RequestMapping("submissons")
    public List<Testcase> sumbit(@RequestBody Map<String, Object>map){

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
//            run(code,testcase.getTestcaseInput(),testcase.getTestcaseOutput());

        }

        TSutdentItem tSutdentItem = new TSutdentItem();

        tSutdentItem.setItemId(Long.valueOf(item_id));
        tSutdentItem.setStudentId(Integer.parseInt(student_id));
        tSutdentItem.setContent(code);
        tSutdentItem.setScore(sum);
        tSutdentItem.setFillTime(new Date());


        tSutdentItemService.save(tSutdentItem);

        return testcaseList;

    }


}


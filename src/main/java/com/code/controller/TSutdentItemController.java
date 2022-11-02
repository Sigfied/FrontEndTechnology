package com.code.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.code.pojo.TSutdentItem;
import com.code.pojo.Testcase;
import com.code.service.TSutdentItemService;
import com.code.service.TestcaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaoshuai
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/StudentItem")
@CrossOrigin
public class TSutdentItemController {
        private static final Logger log = LoggerFactory.getLogger(TSutdentItemController.class);
        private final TSutdentItemService tSutdentItemService;

        private TestcaseService testcaseService;

        @Autowired
        public TSutdentItemController(TSutdentItemService tSutdentItemService, TestcaseService testcaseService) {
                this.tSutdentItemService = tSutdentItemService;
                this.testcaseService = testcaseService;
        }


        @PostMapping("/StudentItemInfo")
        public Map<String, Object> getStudentItemInfo(@RequestBody Map<String, String> map){
                long studentId = Long.parseLong(map.get("studentId"));
                long itemId = Long.parseLong(map.get("itemId"));
                LambdaQueryWrapper<TSutdentItem> query = new LambdaQueryWrapper<>();
                query.eq(TSutdentItem::getStudentId,studentId).eq(TSutdentItem::getItemId,itemId);
                TSutdentItem res = tSutdentItemService.list(query)
                        .stream()
                        .max(Comparator.comparing(TSutdentItem::getScore)).get();
                log.info("Student{}",res);
                Map<String, Object> hashMap = new HashMap<>(2);
                hashMap.put("reset",res);

                LambdaQueryWrapper<Testcase> testcaseWrapper = new LambdaQueryWrapper<>();
                testcaseWrapper.eq(Testcase::getItemId,itemId);
                List<Testcase> list = testcaseService.list(testcaseWrapper);
                list.forEach(testcase ->{
                        testcase.setTestcaseOutput("");
                        testcase.setTestcaseInput("");
                });
                hashMap.put("testcaseList",list);
                return hashMap;
        }
}


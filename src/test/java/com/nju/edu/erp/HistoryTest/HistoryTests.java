package com.nju.edu.erp.HistoryTest;

import com.nju.edu.erp.model.vo.businesshistory.Condition;
import com.nju.edu.erp.service.viewsheetservice.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class HistoryTests {

    @Autowired
    HistoryService historyService;

    @Test
    @Transactional
    @Rollback
    public void testSaleSheet() {
        Condition condition = Condition.builder()
                .type(0)
                .operator("xsjl")
                .build();
        System.out.println(historyService.findSheetByCondition(condition));

    }

    @Test
    @Transactional
    @Rollback
    public void testSaleReturnSheet() {
        Condition condition = Condition.builder()
                .type(1)
                .operator("xsjl")
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }

    @Test
    @Transactional
    @Rollback
    public void testPurchaseSheet() {
        Condition condition = Condition.builder()
                .type(2)
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }

    @Test
    @Transactional
    @Rollback
    public void testPurchaseReturnSheet() {
        Condition condition = Condition.builder()
                .type(3)
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }

    @Test
    @Transactional
    @Rollback
    public void testCollectionSheet() {
        Condition condition = Condition.builder()
                .type(4)
                .cid(2)
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }

    @Test
    @Transactional
    @Rollback
    public void testPaymentSheet() {
        Condition condition = Condition.builder()
                .type(5)
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }

    @Test
    @Transactional
    @Rollback
    public void testSalarySheet() {
        Condition condition = Condition.builder()
                .type(6)
                .build();
        System.out.println(historyService.findSheetByCondition(condition));
    }
}

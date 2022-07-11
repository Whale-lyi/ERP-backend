package com.nju.edu.erp.SaleDetailTest;

import com.nju.edu.erp.model.vo.saleDetail.ConditionVO;
import com.nju.edu.erp.model.vo.saleDetail.SaleDetailTableVO;
import com.nju.edu.erp.service.viewsheetservice.SaleDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class SaleDetailTests {

    @Autowired
    SaleDetailService service;

    @Test
    @Transactional
    @Rollback
    public void testSearchSaleDetail() {
        ConditionVO conditionVO = ConditionVO.builder()
                .pid("0000000000400000")
                .cid(1)
                .build();
        List<SaleDetailTableVO> table = service.searchSaleDetail(conditionVO);
        System.out.println(table);
    }
}

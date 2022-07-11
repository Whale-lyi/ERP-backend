package com.nju.edu.erp.paymentTest;

import com.nju.edu.erp.model.vo.payment.PaymentSheetContentVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PaymentTests {

    @Qualifier("paymentServiceImpl")
    @Autowired
    SheetService sheetService;

    @Test
    @Transactional
    @Rollback
    public void testMakeSheet() {
        UserVO userVO = UserVO.builder()
                .name("test_hoyt")
                .build();
        PaymentSheetVO paymentSheetVO = PaymentSheetVO.builder()
                .cid(2)
                .build();

        List<PaymentSheetContentVO> paymentSheetContent = new ArrayList<>();
        PaymentSheetContentVO contentVO1 = PaymentSheetContentVO.builder()
                .accountId(1)
                .money(BigDecimal.valueOf(111))
                .build();
        PaymentSheetContentVO contentVO2 = PaymentSheetContentVO.builder()
                .accountId(2)
                .money(BigDecimal.valueOf(222))
                .build();
        paymentSheetContent.add(contentVO1);
        paymentSheetContent.add(contentVO2);
        paymentSheetVO.setPaymentSheetContent(paymentSheetContent);

        sheetService.makeSheet(userVO, paymentSheetVO);
    }
}

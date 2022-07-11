package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

@Deprecated
public interface PaymentService {
    /**
     * 制定付款单
     */
    void makePaymentSheet(UserVO userVO, PaymentSheetVO paymentSheetVO);

    /**
     * 根据状态获取付款单
     */
    List<PaymentSheetVO> getPaymentSheetByState(PaymentSheetState state);

    /**
     * 审批单据
     */
    void approval(String paymentSheetId, PaymentSheetState state);

    /**
     * 根据付款单id搜索付款单信息
     */
    PaymentSheetVO getPaymentSheetById(String paymentSheetId);
}

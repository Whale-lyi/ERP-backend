package com.nju.edu.erp.model.po.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSheetContentPO {

    /**
     * id
     */
    private Integer id;

    /**
     * 付款单id
     */
    private String paymentSheetId;

    /**
     * 银行账户id
     */
    private Integer accountId;

    /**
     * 转账金额
     */
    private BigDecimal money;

    /**
     * 备注
     */
    private String remark;
}

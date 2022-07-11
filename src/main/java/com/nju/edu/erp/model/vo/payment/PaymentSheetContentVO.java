package com.nju.edu.erp.model.vo.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSheetContentVO {

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

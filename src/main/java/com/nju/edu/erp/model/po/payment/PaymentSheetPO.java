package com.nju.edu.erp.model.po.payment;

import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSheetPO {

    /**
     * 付款单id, 格式为FKD-yyyyMMdd-xxxxx
     */
    private String id;

    /**
     * 客户id
     */
    private Integer cid;

    /**
     * 操作员(当前登录用户)
     */
    private String operator;

    /**
     * 总额汇总
     */
    private BigDecimal totalAmount;

    /**
     * 单据状态
     */
    private PaymentSheetState state;

    /**
     * 创建时间
     */
    private Date createTime;
}

package com.nju.edu.erp.model.po.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionPO {
    /**
     * 级别（五级，一级普通用户，五级VIP客户）
     */
    private Integer level;
    /**
     * 折扣
     */
    private BigDecimal discount;
    /**
     * 代金券
     */
    private BigDecimal voucher;
}

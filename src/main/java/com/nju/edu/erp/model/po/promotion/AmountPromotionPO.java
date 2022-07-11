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
public class AmountPromotionPO {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 总价下界
     */
    private BigDecimal price;

    /**
     * 代金券金额
     */
    private BigDecimal voucher;
}

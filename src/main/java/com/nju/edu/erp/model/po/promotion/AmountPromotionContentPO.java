package com.nju.edu.erp.model.po.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmountPromotionContentPO {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 对应总价策略id
     */
    private Integer amountPromotionId;

    /**
     * 商品id
     */
    private String pid;

    /**
     * 商品数量
     */
    private Integer quantity;
}

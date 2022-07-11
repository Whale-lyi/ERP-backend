package com.nju.edu.erp.model.po.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftPO {
    /**
     * 客户等级
     */
    private Integer level;

    /**
     * 商品id
     */
    private String pid;

    /**
     * 商品数量
     */
    private Integer quantity;
}

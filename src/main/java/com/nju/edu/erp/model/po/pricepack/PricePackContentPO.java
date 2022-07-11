package com.nju.edu.erp.model.po.pricepack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricePackContentPO {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 特价包id
     */
    private Integer pricePackId;

    /**
     * 商品id
     */
    private String pid;

    /**
     * 商品数量
     */
    private Integer quantity;
}

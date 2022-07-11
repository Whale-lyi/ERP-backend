package com.nju.edu.erp.model.vo.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftVO {
    /**
     * 商品id
     */
    private String pid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品数量
     */
    private Integer quantity;
}

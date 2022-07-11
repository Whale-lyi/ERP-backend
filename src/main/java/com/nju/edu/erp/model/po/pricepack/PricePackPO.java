package com.nju.edu.erp.model.po.pricepack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PricePackPO {
    /**
     * 特价包id
     */
    private Integer id;

    /**
     * 特价包总价
     */
    private BigDecimal amount;
}

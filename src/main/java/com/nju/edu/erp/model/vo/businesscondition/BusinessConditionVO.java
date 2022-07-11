package com.nju.edu.erp.model.vo.businesscondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessConditionVO {
    /**
     * 销售收入
     */
    private BigDecimal saleIncome;
    /**
     * 商品类收入
     */
    private BigDecimal commodityIncome;
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    /**
     * 销售成本
     */
    private BigDecimal saleCost;
    /**
     * 商品成本
     */
    private BigDecimal commodityCost;
    /**
     * 人力成本
     */
    private BigDecimal humanCost;
    /**
     * 总成本
     */
    private BigDecimal totalCost;
    /**
     * 利润
     */
    private BigDecimal profit;

}

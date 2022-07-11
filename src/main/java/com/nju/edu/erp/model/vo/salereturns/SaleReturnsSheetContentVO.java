package com.nju.edu.erp.model.vo.salereturns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnsSheetContentVO {
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 销售退货单id
     */
    private String saleReturnsSheetId;
    /**
     * 商品id,前端传
     */
    private String pid;
    /**
     * 数量,前端传
     */
    private Integer quantity;
    /**
     * 原单价
     */
    private BigDecimal rawUnitPrice;
    /**
     * 减去折扣后新单价
     */
    private BigDecimal newUnitPrice;
    /**
     * 总金额
     */
    private BigDecimal totalPrice;
    /**
     * 备注,前端传
     */
    private String remark;
}

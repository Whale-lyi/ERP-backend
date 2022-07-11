package com.nju.edu.erp.model.vo.saleDetail;

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
public class SaleDetailTableVO {
    /**
     * 销售or退货
     * 销售为0, 退货为1
     */
    private Integer type;

    /**
     * 时间
     */
    private Date time;

    /**
     * 商品名
     */
    private String productName;

    /**
     * 商品型号
     */
    private String productType;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 总额
     */
    private BigDecimal totalAmount;
}

package com.nju.edu.erp.model.vo.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmountPromotionVO {
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

    /**
     * 赠品列表
     */
    private List<AmountPromotionContentVO> amountPromotionContent;
}

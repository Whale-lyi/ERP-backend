package com.nju.edu.erp.model.vo.pricepack;

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
public class PricePackVO {
    /**
     * 特价包id,后端确定
     */
    private Integer id;

    /**
     * 特价包总价
     */
    private BigDecimal amount;

    /**
     * 特价包内容
     */
    private List<PricePackContentVO> pricePackContent;
}

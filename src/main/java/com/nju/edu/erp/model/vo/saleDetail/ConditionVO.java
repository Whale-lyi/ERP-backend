package com.nju.edu.erp.model.vo.saleDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConditionVO {
    /**
     * 起始时间
     */
    private String beginDateStr;

    /**
     * 终止时间
     */
    private String endDateStr;

    /**
     * 商品ID
     */
    private String pid;

    /**
     * 客户ID
     */
    private Integer cid;

    /**
     * 业务员姓名
     */
    private String operator;
}

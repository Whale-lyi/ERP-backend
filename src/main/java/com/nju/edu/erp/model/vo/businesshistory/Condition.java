package com.nju.edu.erp.model.vo.businesshistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Condition {
    /**
     * 起始时间
     */
    private String beginDateStr;

    /**
     * 终止时间
     */
    private String endDateStr;

    /**
     * 单据类型
     * 0：销售单  1：销售退货单  2：进货单  3：进货退货单  4：收款单  5：付款单  6：工资单
     */
    private Integer type;

    /**
     * 客户ID
     */
    private Integer cid;

    /**
     * 业务员姓名
     */
    private String operator;
}

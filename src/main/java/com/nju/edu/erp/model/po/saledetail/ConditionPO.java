package com.nju.edu.erp.model.po.saledetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConditionPO {
    /**
     * 起始时间
     */
    private Date beginDate;

    /**
     * 终止时间
     */
    private Date endDate;

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

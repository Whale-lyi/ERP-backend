package com.nju.edu.erp.model.po.collection;

import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
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
public class CollectionSheetPO {

    /**
     * 收款单id, 格式为SKD-yyyyMMdd-xxxxx
     */
    private String id;

    /**
     * 客户id
     */
    private Integer cid;

    /**
     * 操作员(当前登录用户)
     */
    private String operator;

    /**
     * 总额汇总
     */
    private BigDecimal totalAmount;

    /**
     * 单据状态
     */
    private CollectionSheetState state;

    /**
     * 创建时间
     */
    private Date createTime;
}

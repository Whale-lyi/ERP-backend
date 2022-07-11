package com.nju.edu.erp.model.po.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionSheetContentPO {

    /**
     * id
     */
    private Integer id;

    /**
     * 收款单id
     */
    private String collectionSheetId;

    /**
     * 银行账户id
     */
    private Integer accountId;

    /**
     * 转账金额
     */
    private BigDecimal money;

    /**
     * 备注
     */
    private String remark;
}

package com.nju.edu.erp.model.vo.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionSheetContentVO {
    /**
     * id
     */
//    private Integer id;

    /**
     * 收款单id
     */
//    private String collectionSheetId;

    /**
     * 银行账户id,前端传
     */
    private Integer accountId;

    /**
     * 转账金额,前端传
     */
    private BigDecimal money;

    /**
     * 备注,前端传
     */
    private String remark;
}

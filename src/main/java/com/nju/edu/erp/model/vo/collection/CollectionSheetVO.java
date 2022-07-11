package com.nju.edu.erp.model.vo.collection;

import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
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
public class CollectionSheetVO extends SheetVO {
    /**
     * 收款单id, 格式为SKD-yyyyMMdd-xxxxx,后端填
     */
    private String id;

    /**
     * 客户id
     */
    private Integer cid;

    /**
     * 操作员(当前登录用户),后端填
     */
    private String operator;

    /**
     * 总额汇总,后端填
     */
    private BigDecimal totalAmount;

    /**
     * 单据状态,后端填
     */
    private CollectionSheetState state;

    /**
     * 转账列表
     */
    private List<CollectionSheetContentVO> collectionSheetContent;
}

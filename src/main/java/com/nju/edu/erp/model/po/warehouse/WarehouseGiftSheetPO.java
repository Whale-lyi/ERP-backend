package com.nju.edu.erp.model.po.warehouse;

import com.nju.edu.erp.enums.sheetState.WarehouseGiftSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGiftSheetPO {
    /**
     * 库存赠送单单据编号（格式为：KCZSD-yyyyMMdd-xxxxx)
     */
    private String id;

    /**
     * 销售单id
     */
    private String saleSheetId;

    /**
     * 单据状态
     */
    private WarehouseGiftSheetState state;

    /**
     * 创建时间
     */
    private Date createTime;
}

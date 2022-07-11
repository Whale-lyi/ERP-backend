package com.nju.edu.erp.model.vo.warehouse;

import com.nju.edu.erp.enums.sheetState.WarehouseGiftSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseGiftSheetVO {
    /**
     * 库存赠送单单据编号（格式为：KCZSD-yyyyMMdd-xxxxx),制定时确定
     */
    private String id;

    /**
     * 销售单id
     */
    private String saleSheetId;

    /**
     * 单据状态,制定时确定
     */
    private WarehouseGiftSheetState state;

    /**
     * 具体内容
     */
    List<WarehouseGiftSheetContentVO> warehouseGiftSheetContent;
}

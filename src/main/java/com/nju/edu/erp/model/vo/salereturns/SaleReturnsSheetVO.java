package com.nju.edu.erp.model.vo.salereturns;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleReturnsSheetVO {
    /**
     * 销售退货单单据编号（格式为：XSTHD-yyyyMMdd-xxxxx
     */
    private String id;
    /**
     * 关联的销售单id,前端传
     */
    private String saleSheetId;
    /**
     * 业务员,前端传
     */
    private String salesman;
    /**
     * 操作员
     */
    private String operator;
    /**
     * 备注,前端传
     */
    private String remark;
    /**
     * 退货的总额合计
     */
    private BigDecimal totalAmount;
    /**
     * 单据状态
     */
    private SaleReturnsSheetState state;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 销售退货单具体内容,前端传
     */
    List<SaleReturnsSheetContentVO> saleReturnsSheetContent;
}

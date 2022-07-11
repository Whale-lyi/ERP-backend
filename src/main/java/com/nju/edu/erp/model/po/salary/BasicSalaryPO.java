package com.nju.edu.erp.model.po.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicSalaryPO {
    /**
     *     INVENTORY_MANAGER, //库存管理人员
     *     SALE_STAFF, // 进货销售人员
     *     FINANCIAL_STAFF, // 财务人员
     *     SALE_MANAGER, //销售经理
     *     HR, // 人力资源人员
     *     GM, // 总经理
     *     ADMIN // 最高权限
     */
    private BigDecimal inventoryManagerSalary;

    private BigDecimal saleStaffSalary;

    private BigDecimal financialStaffSalary;

    private BigDecimal saleManagerSalary;

    private BigDecimal hrSalary;

    private BigDecimal gmSalary;
}

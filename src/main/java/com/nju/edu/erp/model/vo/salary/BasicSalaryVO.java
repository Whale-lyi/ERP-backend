package com.nju.edu.erp.model.vo.salary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicSalaryVO {

    /**
     * INVENTORY_MANAGER, //库存管理人员
     */
    private BigDecimal inventoryManagerSalary;

    /**
     * SALE_STAFF, // 进货销售人员
     */
    private BigDecimal saleStaffSalary;

    /**
     * FINANCIAL_STAFF, // 财务人员
     */
    private BigDecimal financialStaffSalary;

    /**
     * SALE_MANAGER, //销售经理
     */
    private BigDecimal saleManagerSalary;

    /**
     * HR, // 人力资源人员
     */
    private BigDecimal hrSalary;

    /**
     * GM, // 总经理
     */
    private BigDecimal gmSalary;
}

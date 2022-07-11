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
public class SalaryAndTax {
    /**
     * 应发工资
     */
    private BigDecimal rawSalary;
    /**
     * 扣除税款(个人所得税)
     */
    private BigDecimal selfTax;

    /**
     * 扣除税款(失业保险)
     */
    private BigDecimal insuranceTax;

    /**
     * 扣除税款(住房公积金)
     */
    private BigDecimal houseTax;
}

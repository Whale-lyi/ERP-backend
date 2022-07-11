package com.nju.edu.erp.model.vo.salary;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetVO extends SheetVO {
    /**
     * 单据编号,格式GZD-yyyyMMdd-xxxxx
     */
    private String id;

    /**
     * 员工编号,前端传
     */
    private Integer uid;

    /**
     * 用户名,前端传
     */
    private String name;

    /**
     * 银行账户,前端传
     */
    private Integer accountId;

    /**
     * 应发工资,前端传
     */
    private BigDecimal rawSalary;

    /**
     * 扣除税款(个人所得税),前端传
     */
    private BigDecimal selfTax;

    /**
     * 扣除税款(失业保险),前端传
     */
    private BigDecimal insuranceTax;

    /**
     * 扣除税款(住房公积金),前端传
     */
    private BigDecimal houseTax;

    /**
     * 实发工资
     */
    private BigDecimal finalSalary;

    /**
     * 单据状态
     */
    private SalarySheetState state;
}

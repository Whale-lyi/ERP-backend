package com.nju.edu.erp.model.po.salary;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalarySheetPO {
    /**
     * 单据编号,格式GZD-yyyyMMdd-xxxxx
     */
    private String id;

    /**
     * 员工编号
     */
    private Integer uid;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 银行账户
     */
    private Integer accountId;

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

    /**
     * 实发工资
     */
    private BigDecimal finalSalary;

    /**
     * 单据状态
     */
    private SalarySheetState state;

    /**
     * 创建时间
     */
    private Date createTime;

}

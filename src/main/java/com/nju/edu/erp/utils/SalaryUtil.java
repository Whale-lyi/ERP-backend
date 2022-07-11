package com.nju.edu.erp.utils;

import com.nju.edu.erp.model.vo.salary.SalaryAndTax;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryUtil {

    /**
     * 使用表驱动根据工资计算税
     * @param salary
     * @return
     */
    public static SalaryAndTax calculateTax(BigDecimal salary) {
        SalaryAndTax salaryAndTax = new SalaryAndTax();
        salaryAndTax.setRawSalary(salary);
        BigDecimal selfTax = BigDecimal.ZERO;
        BigDecimal insuranceTax = BigDecimal.ZERO;
        BigDecimal houseTax = BigDecimal.ZERO;

        int[] preSalary = {5000, 6000, 7000, 8000, 10000};
        int[] postSalary = {6000, 7000, 8000, 10000, Integer.MAX_VALUE};
        double[] self = {0.1, 0.20, 0.25, 0.3, 0.4};
        double[] insurance = {0.05, 0.06, 0.07, 0.08, 0.1};
        double[] house = {0.05, 0.06, 0.07, 0.08, 0.1};

        int rawSalary = salary.intValue();
        for (int i = 0; i < 5; i++) {
            if (rawSalary >= preSalary[i] && rawSalary < postSalary[i]) {
                selfTax = BigDecimal.valueOf(rawSalary * self[i]).setScale(2, RoundingMode.HALF_UP);
                insuranceTax = BigDecimal.valueOf(rawSalary * insurance[i]).setScale(2, RoundingMode.HALF_UP);
                houseTax = BigDecimal.valueOf(rawSalary * house[i]).setScale(2, RoundingMode.HALF_UP);
            }
        }

        salaryAndTax.setSelfTax(selfTax);
        salaryAndTax.setInsuranceTax(insuranceTax);
        salaryAndTax.setHouseTax(houseTax);

        return salaryAndTax;
    }
}

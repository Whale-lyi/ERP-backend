package com.nju.edu.erp.service.hrservice;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.salary.BasicSalaryVO;
import com.nju.edu.erp.model.vo.salary.SalaryAndTax;

import java.math.BigDecimal;

public interface HRService {

    /**
     *根据员工获取工资
     * @param user
     * @return
     */
    SalaryAndTax getSalaryAndTax(User user);

    /**
     *设置基本工资
     * @param role
     * @param salary
     */
    void setSalary(Role role, BigDecimal salary);

    /**
     * 获取所有职位基本工资
     * @return
     */
    BasicSalaryVO getSalary();
}

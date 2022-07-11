package com.nju.edu.erp.service.hrservice;

import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.salary.SalaryAndTax;

public interface SalaryStrategy {

    /**
     * 获取工资和税
     * @param user
     * @return
     */
    SalaryAndTax getSalaryAndTax(User user);

}

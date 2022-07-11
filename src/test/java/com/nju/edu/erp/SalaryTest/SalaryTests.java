package com.nju.edu.erp.SalaryTest;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.salary.BasicSalaryVO;
import com.nju.edu.erp.model.vo.salary.SalaryAndTax;
import com.nju.edu.erp.service.hrservice.HRService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SalaryTests {

    @Autowired
    HRService hrService;

    @Test
    @Transactional
    @Rollback
    public void getSalaryAndTax() {
        User user = new User();
        user.setId(6);
        user.setName("67");
        user.setRole(Role.GM);
        SalaryAndTax salaryAndTax = hrService.getSalaryAndTax(user);
        System.out.println(salaryAndTax.toString());
    }

    @Test
    @Transactional
    @Rollback
    public void setSalary() {
        BasicSalaryVO basicSalaryVO = new BasicSalaryVO();
    }
}

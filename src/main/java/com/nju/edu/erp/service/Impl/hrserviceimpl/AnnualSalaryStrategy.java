package com.nju.edu.erp.service.Impl.hrserviceimpl;

import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.employee.EmployeeVO;
import com.nju.edu.erp.model.vo.salary.SalaryAndTax;
import com.nju.edu.erp.service.employeeservice.EmployeeService;
import com.nju.edu.erp.service.hrservice.SalaryStrategy;
import com.nju.edu.erp.utils.SalaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AnnualSalaryStrategy implements SalaryStrategy {

    private final EmployeeService employeeService;
    private final SalaryDao salaryDao;

    @Autowired
    public AnnualSalaryStrategy(EmployeeService employeeService, SalaryDao salaryDao) {
        this.employeeService = employeeService;
        this.salaryDao = salaryDao;
    }

    @Override
    public SalaryAndTax getSalaryAndTax(User user) {
        //role == Role.ADMIN || role == Role.GM
        BigDecimal salary;
        EmployeeVO employeeVO = employeeService.findEmployeeByName(user.getName());
        if (employeeVO != null) {
            salary = employeeVO.getSalary().multiply(BigDecimal.valueOf(12));
            assert salary.compareTo(BigDecimal.ZERO) >=0;
        } else {
            BigDecimal gmSalary = salaryDao.getSalary().getGmSalary();
            salary = gmSalary.multiply(BigDecimal.valueOf(12));
        }
        return SalaryUtil.calculateTax(salary);
    }
}

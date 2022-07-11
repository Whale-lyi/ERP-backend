package com.nju.edu.erp.service.Impl.hrserviceimpl;

import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.salary.BasicSalaryPO;
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
public class MonthSalaryStrategy implements SalaryStrategy {

    private final EmployeeService employeeService;
    private final SalaryDao salaryDao;

    @Autowired
    public MonthSalaryStrategy(EmployeeService employeeService, SalaryDao salaryDao) {
        this.employeeService = employeeService;
        this.salaryDao = salaryDao;
    }

    @Override
    public SalaryAndTax getSalaryAndTax(User user) {
        Role role = user.getRole();
        //role == Role.INVENTORY_MANAGER || role == Role.HR || role == Role.FINANCIAL_STAFF
        BigDecimal salary = BigDecimal.ZERO;
        EmployeeVO employeeVO = employeeService.findEmployeeByName(user.getName());
        if (employeeVO != null) {
            salary = employeeVO.getSalary();
            assert salary.compareTo(BigDecimal.ZERO) >=0;
        } else {
            BasicSalaryPO basicSalaryPO = salaryDao.getSalary();
            switch (role) {
                case INVENTORY_MANAGER:
                    salary = basicSalaryPO.getInventoryManagerSalary();
                    break;
                case HR:
                    salary = basicSalaryPO.getHrSalary();
                    break;
                case FINANCIAL_STAFF:
                    salary = basicSalaryPO.getFinancialStaffSalary();
                    break;
            }
        }
        return SalaryUtil.calculateTax(salary);
    }
}

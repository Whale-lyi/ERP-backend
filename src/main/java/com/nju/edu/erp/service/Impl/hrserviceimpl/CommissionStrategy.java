package com.nju.edu.erp.service.Impl.hrserviceimpl;

import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.dao.saledao.SaleSheetDao;
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
public class CommissionStrategy implements SalaryStrategy {

    private final SaleSheetDao saleSheetDao;
    private final EmployeeService employeeService;
    private final SalaryDao salaryDao;

    @Autowired
    public CommissionStrategy(SaleSheetDao saleSheetDao, EmployeeService employeeService, SalaryDao salaryDao) {
        this.saleSheetDao = saleSheetDao;
        this.employeeService = employeeService;
        this.salaryDao = salaryDao;
    }

    @Override
    public SalaryAndTax getSalaryAndTax(User user) {
        Role role = user.getRole();
        //role == Role.SALE_MANAGER || role == Role.SALE_STAFF
        BigDecimal salary = BigDecimal.ZERO;
        EmployeeVO employeeVO = employeeService.findEmployeeByName(user.getName());
        if (employeeVO != null) {
            salary = employeeVO.getSalary();
            assert salary.compareTo(BigDecimal.ZERO) >=0;
        } else {
            BasicSalaryPO basicSalaryPO = salaryDao.getSalary();
            switch (role) {
                case SALE_MANAGER:
                    salary = basicSalaryPO.getSaleManagerSalary();
                    break;
                case SALE_STAFF:
                    salary = basicSalaryPO.getSaleStaffSalary();
                    break;
            }
        }
        salary = salary.add(calculateCommission(user));
        return SalaryUtil.calculateTax(salary);
    }

    /**
     * 计算提成
     * @param user
     * @return
     */
    public BigDecimal calculateCommission(User user) {
        BigDecimal base = BigDecimal.valueOf(100);
        BigDecimal count = BigDecimal.valueOf(saleSheetDao.getCountByUser(user.getName()));
        return base.multiply(count);
    }
}

package com.nju.edu.erp.service.Impl.hrserviceimpl;

import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.po.salary.BasicSalaryPO;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.salary.BasicSalaryVO;
import com.nju.edu.erp.model.vo.salary.SalaryAndTax;
import com.nju.edu.erp.service.hrservice.HRService;
import com.nju.edu.erp.service.hrservice.SalaryStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class HRServiceImpl implements HRService {

    private final Map<String, SalaryStrategy> strategyMap;
    private final SalaryDao salaryDao;

    @Autowired
    public HRServiceImpl(Map<String, SalaryStrategy> strategyMap, SalaryDao salaryDao) {
        this.strategyMap = strategyMap;
        this.salaryDao = salaryDao;
    }

    @Override
    public SalaryAndTax getSalaryAndTax(User user) {
        Role role = user.getRole();
        SalaryStrategy strategy;
        //月薪制
        if (role == Role.INVENTORY_MANAGER || role == Role.HR || role == Role.FINANCIAL_STAFF) {
            strategy = strategyMap.get("monthSalaryStrategy");
        }
        //提成
        else if (role == Role.SALE_MANAGER || role == Role.SALE_STAFF) {
            strategy = strategyMap.get("commissionStrategy");
        }
        //年薪
        else {
            strategy = strategyMap.get("annualSalaryStrategy");
        }

        return strategy.getSalaryAndTax(user);
    }

    @Override
    public void setSalary(Role role, BigDecimal salary) {
        BasicSalaryPO basicSalaryPO = salaryDao.getSalary();
        switch (role) {
            case GM:
                basicSalaryPO.setGmSalary(salary);
                break;
            case INVENTORY_MANAGER:
                basicSalaryPO.setInventoryManagerSalary(salary);
                break;
            case SALE_MANAGER:
                basicSalaryPO.setSaleManagerSalary(salary);
                break;
            case FINANCIAL_STAFF:
                basicSalaryPO.setFinancialStaffSalary(salary);
                break;
            case SALE_STAFF:
                basicSalaryPO.setSaleStaffSalary(salary);
                break;
            case HR:
                basicSalaryPO.setHrSalary(salary);
                break;
        }
        salaryDao.updateSalary(basicSalaryPO);
    }

    @Override
    public BasicSalaryVO getSalary() {
        BasicSalaryPO basicSalaryPO = salaryDao.getSalary();
        BasicSalaryVO basicSalaryVO = new BasicSalaryVO();
        BeanUtils.copyProperties(basicSalaryPO, basicSalaryVO);
        return basicSalaryVO;
    }
}

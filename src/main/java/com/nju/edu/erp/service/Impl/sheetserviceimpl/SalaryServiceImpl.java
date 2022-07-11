package com.nju.edu.erp.service.Impl.sheetserviceimpl;

import com.nju.edu.erp.dao.accountdao.AccountDao;
import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.account.AccountPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SalaryServiceImpl implements SheetService {

    private final SalaryDao salaryDao;
    private final AccountDao accountDao;

    @Autowired
    public SalaryServiceImpl(SalaryDao salaryDao, AccountDao accountDao) {
        this.salaryDao = salaryDao;
        this.accountDao = accountDao;
    }

    @Override
    public void makeSheet(UserVO userVO, SheetVO sheetVO) {
        SalarySheetVO salarySheetVO = (SalarySheetVO) sheetVO;
        SalarySheetPO salarySheetPO = new SalarySheetPO();
        BeanUtils.copyProperties(salarySheetVO, salarySheetPO);
        //填写工资单PO信息
        salarySheetPO.setCreateTime(new Date());
        BigDecimal tax = BigDecimal.ZERO;
        tax = tax.add(salarySheetVO.getHouseTax()).add(salarySheetVO.getSelfTax()).add(salarySheetVO.getInsuranceTax());
        salarySheetPO.setFinalSalary(salarySheetPO.getRawSalary().subtract(tax));
        salarySheetPO.setState(SalarySheetState.PENDING);
        SalarySheetPO latest = salaryDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZD");
        salarySheetPO.setId(id);
        salaryDao.saveSheet(salarySheetPO);
    }

    @Override
    public List<SheetVO> getSheetByState(BaseEnum state) {
        List<SheetVO> res = new ArrayList<>();
        List<SalarySheetPO> all;
        if (state == null) {
            all = salaryDao.findAllSheet();
        } else {
            all = salaryDao.findAllSheetByState((SalarySheetState) state);
        }
        for (SalarySheetPO po : all) {
            SalarySheetVO vo = new SalarySheetVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void approval(String salarySheetId, BaseEnum state) {
        if (state.equals(SalarySheetState.FAILURE)) {
            SalarySheetPO salarySheet = salaryDao.findSheetById(salarySheetId);
            if (salarySheet.getState() == SalarySheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = salaryDao.updateSheetState(salarySheetId, (SalarySheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            if (!state.equals(SalarySheetState.SUCCESS)) {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = salaryDao.updateSheetState(salarySheetId, (SalarySheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            //更改账户金额
            SalarySheetPO salarySheet = salaryDao.findSheetById(salarySheetId);
            AccountPO account = accountDao.searchAccountById(salarySheet.getAccountId());
            BigDecimal money = account.getMoney();
            money = money.subtract(salarySheet.getFinalSalary());
            account.setMoney(money);
            accountDao.updateAccount(account);
        }
    }

    @Override
    public SalarySheetVO getSheetById(String salarySheetId) {
        SalarySheetPO salarySheetPO = salaryDao.findSheetById(salarySheetId);
        if (salarySheetPO == null) return null;
        SalarySheetVO salarySheetVO = new SalarySheetVO();
        BeanUtils.copyProperties(salarySheetPO, salarySheetVO);
        return salarySheetVO;
    }
}

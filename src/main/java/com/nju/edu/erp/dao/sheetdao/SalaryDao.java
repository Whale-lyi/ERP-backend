package com.nju.edu.erp.dao.sheetdao;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.salary.BasicSalaryPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SalaryDao {

    SalarySheetPO getLatestSheet();

    Integer saveSheet(SalarySheetPO toSave);

    List<SalarySheetPO> findAllSheet();

    List<SalarySheetPO> findAllSheetByState(SalarySheetState state);

    List<SalarySheetPO> findSheetByCondition(Condition condition);

    SalarySheetPO findSheetById(String id);

    Integer updateSheetState(String sheetId, SalarySheetState state);

    void updateSalary(BasicSalaryPO basicSalaryPO);

    BasicSalaryPO getSalary();
}

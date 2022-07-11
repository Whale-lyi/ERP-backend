package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

@Deprecated
public interface SalaryService {
    /**
     * 制定工资单
     */
    void makeSalarySheet(UserVO userVO, SalarySheetVO salarySheetVO);

    /**
     * 根据状态获取工资单
     */
    List<SalarySheetVO> getSalarySheetByState(SalarySheetState state);

    /**
     * 审批单据
     */
    void approval(String salarySheetId, SalarySheetState state);

    /**
     * 根据工资单id搜索工资单信息
     */
    SalarySheetVO getSalarySheetById(String salarySheetId);
}

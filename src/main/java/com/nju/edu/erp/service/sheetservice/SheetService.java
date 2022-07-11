package com.nju.edu.erp.service.sheetservice;

import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

public interface SheetService {

    /**
     * 制定单据
     */
    void makeSheet(UserVO userVO, SheetVO sheetVO);

    /**
     * 根据状态获取单据
     */
    List<SheetVO> getSheetByState(BaseEnum state);

    /**
     * 审批单据
     */
    void approval(String SheetId, BaseEnum state);

    /**
     * 根据单据id搜索单据信息
     */
    SheetVO getSheetById(String SheetId);
}

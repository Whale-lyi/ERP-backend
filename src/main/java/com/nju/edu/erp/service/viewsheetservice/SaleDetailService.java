package com.nju.edu.erp.service.viewsheetservice;

import com.nju.edu.erp.model.vo.saleDetail.ConditionVO;
import com.nju.edu.erp.model.vo.saleDetail.SaleDetailTableVO;

import java.util.List;

public interface SaleDetailService {

    /**
     * 查看销售明细表
     * @param conditionVO
     * @return
     */
    List<SaleDetailTableVO> searchSaleDetail(ConditionVO conditionVO);
}

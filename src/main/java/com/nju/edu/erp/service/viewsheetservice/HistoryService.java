package com.nju.edu.erp.service.viewsheetservice;

import com.nju.edu.erp.model.vo.businesshistory.Condition;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchasereturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.salereturns.SaleReturnsSheetVO;

import java.util.List;

public interface HistoryService {

    /**
     * 通过条件筛选表单
     * @param condition
     * @return
     */
    Object findSheetByCondition(Condition condition);

    List<SaleSheetVO> getSaleSheet(Condition condition);

    List<SaleReturnsSheetVO> getSaleReturnsSheet(Condition condition);

    List<PurchaseSheetVO> getPurchaseSheet(Condition condition);

    List<PurchaseReturnsSheetVO> getPurchaseReturnsSheet(Condition condition);

    List<CollectionSheetVO> getCollectionSheet(Condition condition);

    List<PaymentSheetVO> getPaymentSheet(Condition condition);

    List<SalarySheetVO> getSalarySheet(Condition condition);

    void hongchong(Integer type, String id);
}

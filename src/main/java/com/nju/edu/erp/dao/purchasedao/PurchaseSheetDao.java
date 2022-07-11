package com.nju.edu.erp.dao.purchasedao;

import com.nju.edu.erp.enums.sheetState.PurchaseSheetState;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetContentPO;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PurchaseSheetDao {
    /**
     * 获取最近一条进货单
     * @return 最近一条进货单
     */
    PurchaseSheetPO getLatest();

    /**
     * 存入一条进货单记录
     * @param toSave 一条进货单记录
     * @return 影响的行数
     */
    int save(PurchaseSheetPO toSave);

    /**
     * 把进货单上的具体内容存入数据库
     * @param purchaseSheetContent 进货单上的具体内容
     */
    void saveBatch(List<PurchaseSheetContentPO> purchaseSheetContent);

    /**
     * 返回所有进货单
     * @return 进货单列表
     */
    List<PurchaseSheetPO> findAll();

    /**
     * 根据state返回进货单
     * @param state 进货单状态
     * @return 进货单列表
     */
    List<PurchaseSheetPO> findAllByState(PurchaseSheetState state);

    /**
     * 根据条件(时间、客户、业务员)查找
     */
    List<PurchaseSheetPO> findSheetByCondition(Condition condition);

    int updateState(String purchaseSheetId, PurchaseSheetState state);

    int updateStateV2(String purchaseSheetId, PurchaseSheetState prevState, PurchaseSheetState state);

    PurchaseSheetPO findOneById(String purchaseSheetId);

    List<PurchaseSheetContentPO> findContentByPurchaseSheetId(String purchaseSheetId);
}
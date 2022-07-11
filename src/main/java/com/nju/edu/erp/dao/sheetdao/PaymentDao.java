package com.nju.edu.erp.dao.sheetdao;

import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import com.nju.edu.erp.model.po.payment.PaymentSheetContentPO;
import com.nju.edu.erp.model.po.payment.PaymentSheetPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PaymentDao {
    /**
     * 获取最近一条付款单
     * @return
     */
    PaymentSheetPO getLatestSheet();

    /**
     * 存入一条付款单记录
     * @param toSave 一条付款单记录
     * @return 影响的行数
     */
    Integer saveSheet(PaymentSheetPO toSave);

    /**
     * 把付款单上的具体内容存入数据库
     * @param paymentSheetContent 付款单上的转账列表
     */
    Integer saveBatchSheetContent(List<PaymentSheetContentPO> paymentSheetContent);

    /**
     * 查找所有付款单
     */
    List<PaymentSheetPO> findAllSheet();

    /**
     * 根据state返回付款单
     * @param state 付款单状态
     * @return 付款单列表
     */
    List<PaymentSheetPO> findAllByState(@Param("state") PaymentSheetState state);

    /**
     * 根据条件(时间、客户、业务员)查找
     */
    List<PaymentSheetPO> findSheetByCondition(Condition condition);

    /**
     * 查找指定id的付款单
     * @param id
     * @return
     */
    PaymentSheetPO findSheetById(String id);

    /**
     * 查找指定付款单下转账列表
     * @param sheetId
     */
    List<PaymentSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 更新指定付款单的状态
     * @param sheetId
     * @param state
     * @return
     */
    Integer updateSheetState(String sheetId, PaymentSheetState state);
}

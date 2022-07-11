package com.nju.edu.erp.dao.saledao;

import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.saledetail.ConditionPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import com.nju.edu.erp.model.vo.saleDetail.ConditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SaleReturnsSheetDao {
    /**
     * 获取最近一条销售退货单
     * @return 最近一条销售退货单
     */
    SaleReturnsSheetPO getLatest();

    /**
     * 存入一条销售退货单记录
     * @param toSave 一条销售退货单记录
     * @return 影响的行数
     */
    int save(SaleReturnsSheetPO toSave);

    /**
     * 把销售退货单上的具体内容存入数据库
     * @param saleReturnsSheetContent 进货退货单上的具体内容
     */
    void saveBatch(List<SaleReturnsSheetContentPO> saleReturnsSheetContent);

    /**
     * 返回所有销售退货单
     * @return 销售退货单列表
     */
    List<SaleReturnsSheetPO> findAll();

    /**
     * 根据state返回销售退货单
     * @param state 销售退货单状态
     * @return 销售退货单列表
     */
    List<SaleReturnsSheetPO> findAllByState(SaleReturnsSheetState state);

    /**
     * 通过saleReturnsSheetId找到对应的content条目
     * @param saleReturnsSheetId
     * @return
     */
    List<SaleReturnsSheetContentPO> findContentBySaleReturnsSheetId(@Param("saleReturnsSheetId") String saleReturnsSheetId);

    /**
     * 通过saleReturnsSheetId找到对应条目
     * @param saleReturnsSheetId 销售退货单id
     * @return
     */
    SaleReturnsSheetPO findOneById(@Param("saleReturnsSheetId") String saleReturnsSheetId);

    /**
     * 通过销售单id查找
     * @param saleSheetId
     * @return
     */
    List<SaleReturnsSheetPO> findBySaleSheetId(@Param("id") String saleSheetId);

    /**
     * 根据条件(时间、业务员)查找
     * @param conditionPO
     * @return
     */
    List<SaleReturnsSheetPO> findSheetByConditionPO(ConditionPO conditionPO);

    List<SaleReturnsSheetPO> findSheetByCondition(Condition condition);

    /**
     * 根据 saleReturnsSheetId 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId 销售退货单id
     * @param state 销售退货单状态
     * @return 影响的条目数
     */
    int updateState(@Param("saleReturnsSheetId") String saleReturnsSheetId, @Param("state") SaleReturnsSheetState state);

    /**
     * 根据 saleReturnsSheetId 和 prevState 找到条目， 并更新其状态为state
     * @param saleReturnsSheetId 销售退货单id
     * @param prevState 销售退货单之前的状态
     * @param state 销售退货单状态
     * @return 影响的条目数
     */
    int updateStateV2(@Param("saleReturnsSheetId") String saleReturnsSheetId, @Param("prevState") SaleReturnsSheetState prevState, @Param("state") SaleReturnsSheetState state);

    /**
     * 通过warehouseOutputSheetId和pid找到退的货的对应批次
     * @param warehouseOutputSheetId, pid
     * @return 批次号
     */
    List<Integer> findBatchId(@Param("warehouseOutputSheetId") String warehouseOutputSheetId, @Param("pid") String pid);

    /**
     * 通过warehouseOutputSheetId和pid找到出货单的content
     * @param warehouseOutputSheetId, pid
     * @return 出货单content
     */
    List<WarehouseOutputSheetContentPO> findWarehouseOutSheetContent(@Param("warehouseOutputSheetId") String warehouseOutputSheetId, @Param("pid") String pid);
}

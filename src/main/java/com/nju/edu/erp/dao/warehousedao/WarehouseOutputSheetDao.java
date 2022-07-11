package com.nju.edu.erp.dao.warehousedao;

import com.nju.edu.erp.enums.sheetState.WarehouseOutputSheetState;
import com.nju.edu.erp.model.po.warehouse.WarehouseIODetailPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface WarehouseOutputSheetDao {
    /**
     * 获取最近一条出库单
     * @return 最近一条出库单
     */
    WarehouseOutputSheetPO getLatest();

    /**
     * 存入一条出库单记录
     * @param toSave 一条出库单记录
     * @return
     */
    void save(WarehouseOutputSheetPO toSave);

    /**
     * 把出库单上的具体内容存入数据库
     * @param warehouseOutputListPOSheetContent 出库单上的具体内容
     */
    void saveBatch(List<WarehouseOutputSheetContentPO> warehouseOutputListPOSheetContent);

    /**
     * 获取所有出库单记录
     * @return
     */
    List<WarehouseOutputSheetPO> getAllSheets();

    /**
     * 获取指定状态的出库单记录
     * @param state
     * @return
     */
    List<WarehouseOutputSheetPO> getDraftSheets(WarehouseOutputSheetState state);

    /**
     * 根据id获取单据
     * @param sheetId
     * @return
     */
    WarehouseOutputSheetPO getSheet(String sheetId);

    /**
     * 更新PO
     * @param warehouseOutputSheetPO
     */
    void updateById(WarehouseOutputSheetPO warehouseOutputSheetPO);

    /**
     * 获取出库单具体内容
     * @param sheetId
     * @return
     */
    List<WarehouseOutputSheetContentPO> getAllContentById(String sheetId);


    /**
     * 删除无批次的初始内容
     * @param sheetId
     */
    void deleteContent(String sheetId);

    Integer getWarehouseOutputProductQuantityByTime(Date beginTime,Date endTime);

    /**
     * 查询指定时间段内出/入库数量/金额/商品信息/分类信息
     * @param beginTime
     * @param endTime
     */
    List<WarehouseIODetailPO> getWarehouseIODetailByTime(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 通过销售单id获取出库单
     * @param saleSheetId 销售单id
     * @return 出库单
     */
    WarehouseOutputSheetPO getWarehouseOutSheetBySaleSheetId(@Param("saleSheetId") String saleSheetId);
}

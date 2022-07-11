package com.nju.edu.erp.dao.warehousedao;

import com.nju.edu.erp.enums.sheetState.WarehouseGiftSheetState;
import com.nju.edu.erp.model.po.warehouse.WarehouseGiftSheetContentPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseGiftSheetPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface WarehouseGiftSheetDao {
    /**
     * 获取最近一条库存赠送单
     * @return 最近一条库存赠送单
     */
    WarehouseGiftSheetPO getLatestSheet();

    /**
     * 存入一条库存赠送单
     * @param toSave 一条库存赠送单
     * @return 影响行数
     */
    Integer saveSheet(WarehouseGiftSheetPO toSave);

    /**
     * 将库存赠送单上的内容存入数据库
     * @param warehouseGiftSheetContent 赠送单上的具体内容
     * @return 影响行数
     */
    Integer saveBatchSheetContent(List<WarehouseGiftSheetContentPO> warehouseGiftSheetContent);

    /**
     * 查找所有库存赠送单
     * @return 所有库存赠送单
     */
    List<WarehouseGiftSheetPO> findAllSheet();

    /**
     * 根据state返回库存赠送单
     * @param state 单据状态
     * @return state状态的库存赠送单
     */
    List<WarehouseGiftSheetPO> findAllByState(WarehouseGiftSheetState state);

    /**
     * 查找指定id的库存赠送单
     * @param id 库存赠送单id
     * @return 指定id的库存赠送单
     */
    WarehouseGiftSheetPO findSheetById(String id);

    /**
     * 查找指定库存赠送单下具体的商品内容
     * @param sheetId 库存赠送单id
     * @return 指定库存赠送单下具体的商品内容
     */
    List<WarehouseGiftSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 更新指定库存赠送单的状态
     * @param sheetId 库存赠送单id
     * @param state 库存赠送单的状态
     * @return 影响行数
     */
    Integer updateSheetState(String sheetId, WarehouseGiftSheetState state);

}

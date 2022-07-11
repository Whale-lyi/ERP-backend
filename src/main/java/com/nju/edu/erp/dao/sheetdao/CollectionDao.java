package com.nju.edu.erp.dao.sheetdao;

import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.po.collection.CollectionSheetContentPO;
import com.nju.edu.erp.model.po.collection.CollectionSheetPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CollectionDao {

    /**
     * 获取最近一条收款单
     * @return
     */
    CollectionSheetPO getLatestSheet();

    /**
     * 存入一条收款单记录
     * @param toSave 一条收款单记录
     * @return 影响的行数
     */
    Integer saveSheet(CollectionSheetPO toSave);

    /**
     * 把收款单上的具体内容存入数据库
     * @param collectionSheetContent 收款单上的转账列表
     */
    Integer saveBatchSheetContent(List<CollectionSheetContentPO> collectionSheetContent);

    /**
     * 查找所有收款单
     */
    List<CollectionSheetPO> findAllSheet();

    /**
     * 根据state返回收款单
     * @param state 收款单状态
     * @return 收款单列表
     */
    List<CollectionSheetPO> findAllByState(@Param("state")CollectionSheetState state);

    /**
     * 根据条件(时间、客户、业务员)查找
     */
    List<CollectionSheetPO> findSheetByCondition(Condition condition);

    /**
     * 查找指定id的收款单
     * @param id
     * @return
     */
    CollectionSheetPO findSheetById(String id);

    /**
     * 查找指定收款单下转账列表
     * @param sheetId
     */
    List<CollectionSheetContentPO> findContentBySheetId(String sheetId);

    /**
     * 更新指定收款单的状态
     * @param sheetId
     * @param state
     * @return
     */
    Integer updateSheetState(String sheetId, CollectionSheetState state);
}

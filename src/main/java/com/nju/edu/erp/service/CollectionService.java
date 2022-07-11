package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

@Deprecated
public interface CollectionService {

    /**
     * 制定收款单
     */
    void makeCollectionSheet(UserVO userVO, CollectionSheetVO collectionSheetVO);

    /**
     * 根据状态获取收款单
     */
    List<CollectionSheetVO> getCollectionSheetByState(CollectionSheetState state);

    /**
     * 审批单据
     */
    void approval(String collectionSheetId, CollectionSheetState state);

    /**
     * 根据收款单id搜索收款单信息
     */
    CollectionSheetVO getCollectionSheetById(String collectionSheetId);
}

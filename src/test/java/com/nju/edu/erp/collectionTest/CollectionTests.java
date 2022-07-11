package com.nju.edu.erp.collectionTest;

import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.vo.collection.CollectionSheetContentVO;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CollectionTests {

    @Qualifier("collectionServiceImpl")
    @Autowired
    SheetService sheetService;

    @Test
    @Transactional
    @Rollback
    public void testMakeSheet() {
        UserVO userVO = UserVO.builder()
                .name("test_hoyt")
                .build();
        CollectionSheetVO collectionSheetVO = CollectionSheetVO.builder()
                .cid(2)
                .build();

        List<CollectionSheetContentVO> collectionSheetContent = new ArrayList<>();
        CollectionSheetContentVO contentVO1 = CollectionSheetContentVO.builder()
                .accountId(1)
                .money(BigDecimal.valueOf(111))
                .build();
        CollectionSheetContentVO contentVO2 = CollectionSheetContentVO.builder()
                .accountId(2)
                .money(BigDecimal.valueOf(222))
                .build();
        collectionSheetContent.add(contentVO1);
        collectionSheetContent.add(contentVO2);
        collectionSheetVO.setCollectionSheetContent(collectionSheetContent);

        sheetService.makeSheet(userVO, collectionSheetVO);
    }

    @Test
    @Transactional
    @Rollback
    public void testApproval() {
        sheetService.approval("SKD-20220628-00000", CollectionSheetState.SUCCESS);
    }
}

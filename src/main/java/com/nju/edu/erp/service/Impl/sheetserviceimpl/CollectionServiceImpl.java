package com.nju.edu.erp.service.Impl.sheetserviceimpl;

import com.nju.edu.erp.dao.accountdao.AccountDao;
import com.nju.edu.erp.dao.sheetdao.CollectionDao;
import com.nju.edu.erp.dao.customerdao.CustomerDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.po.account.AccountPO;
import com.nju.edu.erp.model.po.collection.CollectionSheetContentPO;
import com.nju.edu.erp.model.po.collection.CollectionSheetPO;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.collection.CollectionSheetContentVO;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CollectionServiceImpl implements SheetService {

    private final CollectionDao collectionDao;
    private final AccountDao accountDao;
    private final CustomerDao customerDao;

    @Autowired
    public CollectionServiceImpl(CollectionDao collectionDao, AccountDao accountDao, CustomerDao customerDao) {
        this.collectionDao = collectionDao;
        this.accountDao = accountDao;
        this.customerDao = customerDao;
    }

    @Override
    @Transactional
    public void makeSheet(UserVO userVO, SheetVO sheetVO) {
        CollectionSheetVO collectionSheetVO = (CollectionSheetVO) sheetVO;
        CollectionSheetPO collectionSheetPO = new CollectionSheetPO();
        BeanUtils.copyProperties(collectionSheetVO, collectionSheetPO);
        // 此处填写收款单各种信息
        collectionSheetPO.setOperator(userVO.getName());
        collectionSheetPO.setCreateTime(new Date());
        CollectionSheetPO latest = collectionDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
        collectionSheetPO.setId(id);
        collectionSheetPO.setState(CollectionSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        //遍历content
        List<CollectionSheetContentPO> cContentPOList = new ArrayList<>();
        for (CollectionSheetContentVO content : collectionSheetVO.getCollectionSheetContent()) {
            assert content.getMoney().compareTo(BigDecimal.ZERO) >= 0;
            CollectionSheetContentPO cContentPO = new CollectionSheetContentPO();
            BeanUtils.copyProperties(content, cContentPO);
            cContentPO.setCollectionSheetId(id);
            cContentPOList.add(cContentPO);
            totalAmount = totalAmount.add(cContentPO.getMoney());
        }
        collectionDao.saveBatchSheetContent(cContentPOList);
        collectionSheetPO.setTotalAmount(totalAmount);
        collectionDao.saveSheet(collectionSheetPO);
    }

    @Override
    @Transactional
    public List<SheetVO> getSheetByState(BaseEnum state) {
        List<SheetVO> res = new ArrayList<>();
        List<CollectionSheetPO> all;
        if (state == null) {
            all = collectionDao.findAllSheet();
        } else {
            all = collectionDao.findAllByState((CollectionSheetState) state);
        }
        for (CollectionSheetPO po : all) {
            CollectionSheetVO vo = new CollectionSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<CollectionSheetContentPO> alll = collectionDao.findContentBySheetId(po.getId());
            List<CollectionSheetContentVO> vos = new ArrayList<>();
            for (CollectionSheetContentPO p : alll) {
                CollectionSheetContentVO v = new CollectionSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setCollectionSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据收款单id进行审批(state == "审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param collectionSheetId 收款单id
     * @param state       收款单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String collectionSheetId, BaseEnum state) {
        if (state.equals(CollectionSheetState.FAILURE)) {
            CollectionSheetPO collectionSheet = collectionDao.findSheetById(collectionSheetId);
            if (collectionSheet.getState() == CollectionSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = collectionDao.updateSheetState(collectionSheetId, (CollectionSheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            if (!state.equals(CollectionSheetState.SUCCESS)) {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = collectionDao.updateSheetState(collectionSheetId, (CollectionSheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            //更改账户金额
            List<CollectionSheetContentPO> poList = collectionDao.findContentBySheetId(collectionSheetId);
            for (CollectionSheetContentPO po : poList) {
                AccountPO account = accountDao.searchAccountById(po.getAccountId());
                BigDecimal money = account.getMoney();
                money = money.add(po.getMoney());
                account.setMoney(money);
                accountDao.updateAccount(account);
            }
            //更改客户的应收
            CollectionSheetPO collectionSheet = collectionDao.findSheetById(collectionSheetId);
            CustomerPO customer = customerDao.findOneById(collectionSheet.getCid());
            BigDecimal receivable = customer.getReceivable();
            receivable = receivable.subtract(collectionSheet.getTotalAmount());
            customer.setReceivable(receivable);
            customerDao.updateOne(customer);
        }
    }

    /**
     * 根据收款单Id搜索收款单信息
     * @param collectionSheetId 收款单Id
     * @return 收款单全部信息
     */
    @Override
    public CollectionSheetVO getSheetById(String collectionSheetId) {
        CollectionSheetPO collectionSheetPO = collectionDao.findSheetById(collectionSheetId);
        if (collectionSheetPO == null) return null;
        List<CollectionSheetContentPO> contentPO = collectionDao.findContentBySheetId(collectionSheetId);
        CollectionSheetVO cVO = new CollectionSheetVO();
        BeanUtils.copyProperties(collectionSheetPO, cVO);
        List<CollectionSheetContentVO> collectionSheetContentVOList = new ArrayList<>();
        for (CollectionSheetContentPO content : contentPO) {
            CollectionSheetContentVO contentVO = new CollectionSheetContentVO();
            BeanUtils.copyProperties(content, contentVO);
            collectionSheetContentVOList.add(contentVO);
        }
        cVO.setCollectionSheetContent(collectionSheetContentVOList);
        return cVO;
    }
}

package com.nju.edu.erp.service.Impl.sheetserviceimpl;

import com.nju.edu.erp.dao.accountdao.AccountDao;
import com.nju.edu.erp.dao.customerdao.CustomerDao;
import com.nju.edu.erp.dao.sheetdao.PaymentDao;
import com.nju.edu.erp.enums.BaseEnum;
import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import com.nju.edu.erp.model.po.account.AccountPO;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.po.payment.PaymentSheetContentPO;
import com.nju.edu.erp.model.po.payment.PaymentSheetPO;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetContentVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements SheetService {

    private final PaymentDao paymentDao;
    private final AccountDao accountDao;
    private final CustomerDao customerDao;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao, AccountDao accountDao, CustomerDao customerDao) {
        this.paymentDao = paymentDao;
        this.accountDao = accountDao;
        this.customerDao = customerDao;
    }

    @Override
    public void makeSheet(UserVO userVO, SheetVO sheetVO) {
        PaymentSheetVO paymentSheetVO = (PaymentSheetVO) sheetVO;
        PaymentSheetPO paymentSheetPO = new PaymentSheetPO();
        BeanUtils.copyProperties(paymentSheetVO, paymentSheetPO);
        // 此处填写收款单各种信息
        paymentSheetPO.setOperator(userVO.getName());
        paymentSheetPO.setCreateTime(new Date());
        PaymentSheetPO latest = paymentDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "FKD");
        paymentSheetPO.setId(id);
        paymentSheetPO.setState(PaymentSheetState.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        //遍历content
        List<PaymentSheetContentPO> cContentPOList = new ArrayList<>();
        for (PaymentSheetContentVO content : paymentSheetVO.getPaymentSheetContent()) {
            assert content.getMoney().compareTo(BigDecimal.ZERO) >= 0;
            PaymentSheetContentPO cContentPO = new PaymentSheetContentPO();
            BeanUtils.copyProperties(content, cContentPO);
            cContentPO.setPaymentSheetId(id);
            cContentPOList.add(cContentPO);
            totalAmount = totalAmount.add(cContentPO.getMoney());
        }
        paymentDao.saveBatchSheetContent(cContentPOList);
        paymentSheetPO.setTotalAmount(totalAmount);
        paymentDao.saveSheet(paymentSheetPO);
    }

    @Override
    public List<SheetVO> getSheetByState(BaseEnum state) {
        List<SheetVO> res = new ArrayList<>();
        List<PaymentSheetPO> all;
        if (state == null) {
            all = paymentDao.findAllSheet();
        } else {
            all = paymentDao.findAllByState((PaymentSheetState) state);
        }
        for (PaymentSheetPO po : all) {
            PaymentSheetVO vo = new PaymentSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PaymentSheetContentPO> alll = paymentDao.findContentBySheetId(po.getId());
            List<PaymentSheetContentVO> vos = new ArrayList<>();
            for (PaymentSheetContentPO p : alll) {
                PaymentSheetContentVO v = new PaymentSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPaymentSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public void approval(String paymentSheetId, BaseEnum state) {
        if (state.equals(PaymentSheetState.FAILURE)) {
            PaymentSheetPO paymentSheet = paymentDao.findSheetById(paymentSheetId);
            if (paymentSheet.getState() == PaymentSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = paymentDao.updateSheetState(paymentSheetId, (PaymentSheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            if (!state.equals(PaymentSheetState.SUCCESS)) {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = paymentDao.updateSheetState(paymentSheetId, (PaymentSheetState) state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            //更改账户金额
            List<PaymentSheetContentPO> poList = paymentDao.findContentBySheetId(paymentSheetId);
            for (PaymentSheetContentPO po : poList) {
                AccountPO account = accountDao.searchAccountById(po.getAccountId());
                BigDecimal money = account.getMoney();
                money = money.subtract(po.getMoney());
                account.setMoney(money);
                accountDao.updateAccount(account);
            }
            //更改客户的应付
            PaymentSheetPO paymentSheet = paymentDao.findSheetById(paymentSheetId);
            CustomerPO customer = customerDao.findOneById(paymentSheet.getCid());
            BigDecimal payable = customer.getPayable();
            payable = payable.subtract(paymentSheet.getTotalAmount());
            customer.setPayable(payable);
            customerDao.updateOne(customer);
        }
    }

    @Override
    public PaymentSheetVO getSheetById(String paymentSheetId) {
        PaymentSheetPO paymentSheetPO = paymentDao.findSheetById(paymentSheetId);
        if (paymentSheetPO == null) return null;
        List<PaymentSheetContentPO> contentPO = paymentDao.findContentBySheetId(paymentSheetId);
        PaymentSheetVO cVO = new PaymentSheetVO();
        BeanUtils.copyProperties(paymentSheetPO, cVO);
        List<PaymentSheetContentVO> paymentSheetContentVOList = new ArrayList<>();
        for (PaymentSheetContentPO content : contentPO) {
            PaymentSheetContentVO contentVO = new PaymentSheetContentVO();
            BeanUtils.copyProperties(content, contentVO);
            paymentSheetContentVOList.add(contentVO);
        }
        cVO.setPaymentSheetContent(paymentSheetContentVOList);
        return cVO;
    }
}

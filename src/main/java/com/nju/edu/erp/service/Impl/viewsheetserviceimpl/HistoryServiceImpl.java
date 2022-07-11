package com.nju.edu.erp.service.Impl.viewsheetserviceimpl;

import com.nju.edu.erp.dao.purchasedao.PurchaseReturnsSheetDao;
import com.nju.edu.erp.dao.purchasedao.PurchaseSheetDao;
import com.nju.edu.erp.dao.saledao.SaleReturnsSheetDao;
import com.nju.edu.erp.dao.saledao.SaleSheetDao;
import com.nju.edu.erp.dao.sheetdao.CollectionDao;
import com.nju.edu.erp.dao.sheetdao.PaymentDao;
import com.nju.edu.erp.dao.sheetdao.SalaryDao;
import com.nju.edu.erp.model.po.collection.CollectionSheetContentPO;
import com.nju.edu.erp.model.po.collection.CollectionSheetPO;
import com.nju.edu.erp.model.po.payment.PaymentSheetContentPO;
import com.nju.edu.erp.model.po.payment.PaymentSheetPO;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetContentPO;
import com.nju.edu.erp.model.po.purchase.PurchaseSheetPO;
import com.nju.edu.erp.model.po.purchasereturns.PurchaseReturnsSheetContentPO;
import com.nju.edu.erp.model.po.purchasereturns.PurchaseReturnsSheetPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetPO;
import com.nju.edu.erp.model.vo.businesshistory.Condition;
import com.nju.edu.erp.model.vo.collection.CollectionSheetContentVO;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetContentVO;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetContentVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchasereturns.PurchaseReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.purchasereturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.salereturns.SaleReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.salereturns.SaleReturnsSheetVO;
import com.nju.edu.erp.service.viewsheetservice.HistoryService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final SaleSheetDao saleSheetDao;
    private final SaleReturnsSheetDao saleReturnsSheetDao;
    private final PurchaseSheetDao purchaseSheetDao;
    private final PurchaseReturnsSheetDao purchaseReturnsSheetDao;
    private final CollectionDao collectionDao;
    private final PaymentDao paymentDao;
    private final SalaryDao salaryDao;

    @Autowired
    public HistoryServiceImpl(SaleSheetDao saleSheetDao,
                              SaleReturnsSheetDao saleReturnsSheetDao,
                              PurchaseSheetDao purchaseSheetDao,
                              PurchaseReturnsSheetDao purchaseReturnsSheetDao,
                              CollectionDao collectionDao,
                              PaymentDao paymentDao,
                              SalaryDao salaryDao) {
        this.saleSheetDao = saleSheetDao;
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.purchaseSheetDao = purchaseSheetDao;
        this.purchaseReturnsSheetDao = purchaseReturnsSheetDao;
        this.collectionDao = collectionDao;
        this.paymentDao = paymentDao;
        this.salaryDao = salaryDao;
    }

    @Override
    public Object findSheetByCondition(Condition condition) {
        Object res = null;
        switch (condition.getType()) {
            case 0:
                res = getSaleSheet(condition);
                break;
            case 1:
                res = getSaleReturnsSheet(condition);
                break;
            case 2:
                res = getPurchaseSheet(condition);
                break;
            case 3:
                res = getPurchaseReturnsSheet(condition);
                break;
            case 4:
                res = getCollectionSheet(condition);
                break;
            case 5:
                res = getPaymentSheet(condition);
                break;
            case 6:
                res = getSalarySheet(condition);
                break;
            default:break;
        }
        return res;
    }

    @Override
    public void hongchong(Integer type, String id) {
        switch (type) {
            case 0:
                SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(id);
                SaleSheetPO latest = saleSheetDao.getLatestSheet();
                String id0 = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
                saleSheetPO.setId(id0);
                saleSheetPO.setCreateTime(new Date());
                List<SaleSheetContentPO> saleSheetContentPOS = saleSheetDao.findContentBySheetId(id);
                for (SaleSheetContentPO saleSheetContentPO : saleSheetContentPOS) {
                    saleSheetContentPO.setSaleSheetId(id0);
                    saleSheetContentPO.setQuantity(-saleSheetContentPO.getQuantity());
                }
                saleSheetDao.saveSheet(saleSheetPO);
                saleSheetDao.saveBatchSheetContent(saleSheetContentPOS);
                break;
            case 1:
                SaleReturnsSheetPO saleReturnsSheetPO = saleReturnsSheetDao.findOneById(id);
                SaleReturnsSheetPO latest1 = saleReturnsSheetDao.getLatest();
                String id1 = IdGenerator.generateSheetId(latest1 == null ? null : latest1.getId(), "XSTHD");
                saleReturnsSheetPO.setId(id1);
                saleReturnsSheetPO.setCreateTime(new Date());
                List<SaleReturnsSheetContentPO> saleReturnsSheetContentPOS = saleReturnsSheetDao.findContentBySaleReturnsSheetId(id);
                for (SaleReturnsSheetContentPO saleReturnsSheetContentPO : saleReturnsSheetContentPOS) {
                    saleReturnsSheetContentPO.setQuantity(-saleReturnsSheetContentPO.getQuantity());
                    saleReturnsSheetContentPO.setSaleReturnsSheetId(id1);
                }
                saleReturnsSheetDao.save(saleReturnsSheetPO);
                saleReturnsSheetDao.saveBatch(saleReturnsSheetContentPOS);
                break;
            case 2:
                PurchaseSheetPO purchaseSheetPO = purchaseSheetDao.findOneById(id);
                PurchaseSheetPO latest2 = purchaseSheetDao.getLatest();
                String id2 = IdGenerator.generateSheetId(latest2 == null ? null : latest2.getId(), "JHD");
                purchaseSheetPO.setId(id2);
                purchaseSheetPO.setCreateTime(new Date());
                List<PurchaseSheetContentPO> purchaseSheetContentPOS = purchaseSheetDao.findContentByPurchaseSheetId(id);
                for (PurchaseSheetContentPO purchaseSheetContentPO : purchaseSheetContentPOS) {
                    purchaseSheetContentPO.setPurchaseSheetId(id2);
                    purchaseSheetContentPO.setQuantity(-purchaseSheetContentPO.getQuantity());
                }
                purchaseSheetDao.save(purchaseSheetPO);
                purchaseSheetDao.saveBatch(purchaseSheetContentPOS);
                break;
            case 3:
                PurchaseReturnsSheetPO purchaseReturnsSheetPO = purchaseReturnsSheetDao.findOneById(id);
                PurchaseReturnsSheetPO latest3 = purchaseReturnsSheetDao.getLatest();
                String id3 = IdGenerator.generateSheetId(latest3 == null ? null : latest3.getId(), "JHTHD");
                purchaseReturnsSheetPO.setId(id3);
                purchaseReturnsSheetPO.setCreateTime(new Date());
                List<PurchaseReturnsSheetContentPO> purchaseReturnsSheetContentPOS = purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(id);
                for (PurchaseReturnsSheetContentPO purchaseReturnsSheetContentPO : purchaseReturnsSheetContentPOS) {
                    purchaseReturnsSheetContentPO.setPurchaseReturnsSheetId(id3);
                    purchaseReturnsSheetContentPO.setQuantity(-purchaseReturnsSheetContentPO.getQuantity());
                }
                purchaseReturnsSheetDao.save(purchaseReturnsSheetPO);
                purchaseReturnsSheetDao.saveBatch(purchaseReturnsSheetContentPOS);
                break;
            case 4:
                CollectionSheetPO collectionSheetPO = collectionDao.findSheetById(id);
                CollectionSheetPO latest4 = collectionDao.getLatestSheet();
                String id4 = IdGenerator.generateSheetId(latest4 == null ? null : latest4.getId(), "SKD");
                collectionSheetPO.setId(id4);
                collectionSheetPO.setTotalAmount(collectionSheetPO.getTotalAmount().negate());
                collectionSheetPO.setCreateTime(new Date());
                List<CollectionSheetContentPO> collectionSheetContentPOS = collectionDao.findContentBySheetId(id);
                for (CollectionSheetContentPO collectionSheetContentPO : collectionSheetContentPOS) {
                    collectionSheetContentPO.setCollectionSheetId(id4);
                    collectionSheetContentPO.setMoney(collectionSheetContentPO.getMoney().negate());
                }
                collectionDao.saveSheet(collectionSheetPO);
                collectionDao.saveBatchSheetContent(collectionSheetContentPOS);
                break;
            case 5:
                PaymentSheetPO paymentSheetPO = paymentDao.findSheetById(id);
                PaymentSheetPO latest5 = paymentDao.getLatestSheet();
                String id5 = IdGenerator.generateSheetId(latest5 == null ? null : latest5.getId(), "FKD");
                paymentSheetPO.setId(id5);
                paymentSheetPO.setTotalAmount(paymentSheetPO.getTotalAmount().negate());
                paymentSheetPO.setCreateTime(new Date());
                List<PaymentSheetContentPO> paymentSheetContentPOS = paymentDao.findContentBySheetId(id);
                for (PaymentSheetContentPO paymentSheetContentPO : paymentSheetContentPOS) {
                    paymentSheetContentPO.setPaymentSheetId(id5);
                    paymentSheetContentPO.setMoney(paymentSheetContentPO.getMoney().negate());
                }
                paymentDao.saveSheet(paymentSheetPO);
                paymentDao.saveBatchSheetContent(paymentSheetContentPOS);
                break;
            case 6:
                SalarySheetPO salarySheetPO = salaryDao.findSheetById(id);
                SalarySheetPO latest6 = salaryDao.getLatestSheet();
                String id6 = IdGenerator.generateSheetId(latest6 == null ? null : latest6.getId(), "GZD");
                salarySheetPO.setId(id6);
                salarySheetPO.setFinalSalary(salarySheetPO.getFinalSalary().negate());
                salarySheetPO.setCreateTime(new Date());
                salaryDao.saveSheet(salarySheetPO);
                break;
            default:break;
        }
    }

    @Override
    public List<SaleSheetVO> getSaleSheet(Condition condition) {
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all = saleSheetDao.findSheetByCondition(condition);
        for (SaleSheetPO po : all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : alll) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<SaleReturnsSheetVO> getSaleReturnsSheet(Condition condition) {
        List<SaleReturnsSheetVO> res = new ArrayList<>();
        List<SaleReturnsSheetPO> all = saleReturnsSheetDao.findSheetByCondition(condition);
        if (condition.getCid() != null) {
            List<SaleReturnsSheetPO> temp = new ArrayList<>();
            for (SaleReturnsSheetPO saleReturnsSheet : all) {
                SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnsSheet.getSaleSheetId());
                if (saleSheetPO.getSupplier().intValue() == condition.getCid().intValue()) {
                    temp.add(saleReturnsSheet);
                }
            }
            all = temp;
        }

        for (SaleReturnsSheetPO po : all) {
            SaleReturnsSheetVO vo = new SaleReturnsSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleReturnsSheetContentPO> alll = saleReturnsSheetDao.findContentBySaleReturnsSheetId(po.getId());
            List<SaleReturnsSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnsSheetContentPO p : alll) {
                SaleReturnsSheetContentVO v = new SaleReturnsSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<PurchaseSheetVO> getPurchaseSheet(Condition condition) {
        List<PurchaseSheetVO> res = new ArrayList<>();
        List<PurchaseSheetPO> all = purchaseSheetDao.findSheetByCondition(condition);
        for(PurchaseSheetPO po: all) {
            PurchaseSheetVO vo = new PurchaseSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PurchaseSheetContentPO> alll = purchaseSheetDao.findContentByPurchaseSheetId(po.getId());
            List<PurchaseSheetContentVO> vos = new ArrayList<>();
            for (PurchaseSheetContentPO p : alll) {
                PurchaseSheetContentVO v = new PurchaseSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPurchaseSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<PurchaseReturnsSheetVO> getPurchaseReturnsSheet(Condition condition) {
        List<PurchaseReturnsSheetVO> res = new ArrayList<>();
        List<PurchaseReturnsSheetPO> all = purchaseReturnsSheetDao.findSheetByCondition(condition);
        if (condition.getCid() != null) {
            List<PurchaseReturnsSheetPO> temp = new ArrayList<>();
            for (PurchaseReturnsSheetPO purchaseReturnsSheet : all) {
                PurchaseSheetPO purchaseSheetPO = purchaseSheetDao.findOneById(purchaseReturnsSheet.getPurchaseSheetId());
                if (purchaseSheetPO.getSupplier().intValue() == condition.getCid().intValue()) {
                    temp.add(purchaseReturnsSheet);
                }
            }
            all = temp;
        }
        for(PurchaseReturnsSheetPO po: all) {
            PurchaseReturnsSheetVO vo = new PurchaseReturnsSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PurchaseReturnsSheetContentPO> alll = purchaseReturnsSheetDao.findContentByPurchaseReturnsSheetId(po.getId());
            List<PurchaseReturnsSheetContentVO> vos = new ArrayList<>();
            for (PurchaseReturnsSheetContentPO p : alll) {
                PurchaseReturnsSheetContentVO v = new PurchaseReturnsSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPurchaseReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<CollectionSheetVO> getCollectionSheet(Condition condition) {
        List<CollectionSheetVO> res = new ArrayList<>();
        List<CollectionSheetPO> all = collectionDao.findSheetByCondition(condition);
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

    @Override
    public List<PaymentSheetVO> getPaymentSheet(Condition condition) {
        List<PaymentSheetVO> res = new ArrayList<>();
        List<PaymentSheetPO> all = paymentDao.findSheetByCondition(condition);
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
    public List<SalarySheetVO> getSalarySheet(Condition condition) {
        List<SalarySheetVO> res = new ArrayList<>();
        List<SalarySheetPO> all = salaryDao.findSheetByCondition(condition);
        for (SalarySheetPO po : all) {
            SalarySheetVO vo = new SalarySheetVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }
        return res;
    }
}

package com.nju.edu.erp.service.Impl.viewsheetserviceimpl;

import com.nju.edu.erp.dao.productdao.ProductDao;
import com.nju.edu.erp.dao.saledao.SaleReturnsSheetDao;
import com.nju.edu.erp.dao.saledao.SaleSheetDao;
import com.nju.edu.erp.model.po.product.ProductPO;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.saledetail.ConditionPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetPO;
import com.nju.edu.erp.model.vo.saleDetail.ConditionVO;
import com.nju.edu.erp.model.vo.saleDetail.SaleDetailTableVO;
import com.nju.edu.erp.service.viewsheetservice.SaleDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleDetailServiceImpl implements SaleDetailService {

    private final SaleSheetDao saleSheetDao;
    private final SaleReturnsSheetDao saleReturnsSheetDao;
    private final ProductDao productDao;

    @Autowired
    public SaleDetailServiceImpl(SaleSheetDao saleSheetDao, SaleReturnsSheetDao saleReturnsSheetDao, ProductDao productDao) {
        this.saleSheetDao = saleSheetDao;
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.productDao = productDao;
    }

    @Override
    public List<SaleDetailTableVO> searchSaleDetail(ConditionVO conditionVO) {
        ConditionPO conditionPO = new ConditionPO();
        BeanUtils.copyProperties(conditionVO, conditionPO);
        if (StringUtils.hasLength(conditionVO.getBeginDateStr()) && StringUtils.hasLength(conditionVO.getEndDateStr())) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date beginTime =dateFormat.parse(conditionVO.getBeginDateStr());
                Date endTime=dateFormat.parse(conditionVO.getEndDateStr());
                conditionPO.setBeginDate(beginTime);
                conditionPO.setEndDate(endTime);
            }catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<SaleSheetPO> saleSheetPOList = saleSheetDao.findSheetByConditionPO(conditionPO);
        List<SaleReturnsSheetPO> saleReturnsSheetPOList = saleReturnsSheetDao.findSheetByConditionPO(conditionPO);
        List<SaleDetailTableVO> res = new ArrayList<>();

        for (SaleSheetPO saleSheetPO : saleSheetPOList) {
            List<SaleSheetContentPO> contentPOList = saleSheetDao.findContentBySheetId(saleSheetPO.getId());
            for (SaleSheetContentPO contentPO : contentPOList) {
                if (!StringUtils.hasLength(conditionPO.getPid()) || conditionPO.getPid().equals(contentPO.getPid())) {
                    ProductPO product = productDao.findById(contentPO.getPid());
                    SaleDetailTableVO table = new SaleDetailTableVO();
                    table.setType(0);
                    table.setTime(saleSheetPO.getCreateTime());
                    table.setProductName(product.getName());
                    table.setProductType(product.getType());
                    table.setUnitPrice(contentPO.getUnitPrice());
                    table.setQuantity(contentPO.getQuantity());
                    table.setTotalAmount(contentPO.getTotalPrice());
                    res.add(table);
                }
            }
        }

        for (SaleReturnsSheetPO saleReturnsSheetPO : saleReturnsSheetPOList) {
            SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnsSheetPO.getSaleSheetId());
            if (conditionPO.getCid() == null || conditionPO.getCid().intValue() == saleSheetPO.getSupplier().intValue()) {
                List<SaleReturnsSheetContentPO> contentPOList = saleReturnsSheetDao.findContentBySaleReturnsSheetId(saleReturnsSheetPO.getId());
                for (SaleReturnsSheetContentPO contentPO : contentPOList) {
                    if (!StringUtils.hasLength(conditionPO.getPid()) || conditionPO.getPid().equals(contentPO.getPid())) {
                        ProductPO product = productDao.findById(contentPO.getPid());
                        SaleDetailTableVO table = new SaleDetailTableVO();
                        table.setType(1);
                        table.setTime(saleReturnsSheetPO.getCreateTime());
                        table.setProductName(product.getName());
                        table.setProductType(product.getType());
                        table.setUnitPrice(contentPO.getNewUnitPrice());
                        table.setQuantity(contentPO.getQuantity());
                        table.setTotalAmount(contentPO.getTotalPrice());
                        res.add(table);
                    }
                }
            }
        }

        return res;
    }
}

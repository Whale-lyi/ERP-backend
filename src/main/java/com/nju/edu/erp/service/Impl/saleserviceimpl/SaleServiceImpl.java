package com.nju.edu.erp.service.Impl.saleserviceimpl;

import com.nju.edu.erp.dao.customerdao.CustomerDao;
import com.nju.edu.erp.dao.productdao.ProductDao;
import com.nju.edu.erp.dao.saledao.SaleSheetDao;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.po.customer.CustomerPurchaseAmountPO;
import com.nju.edu.erp.model.po.product.ProductPO;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.vo.pricepack.PricePackContentVO;
import com.nju.edu.erp.model.vo.pricepack.PricePackVO;
import com.nju.edu.erp.model.vo.product.ProductInfoVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionContentVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionVO;
import com.nju.edu.erp.model.vo.promotion.GiftVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGiftSheetContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGiftSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.customerservice.CustomerService;
import com.nju.edu.erp.service.productservice.ProductService;
import com.nju.edu.erp.service.promotionservice.PromotionService;
import com.nju.edu.erp.service.saleservice.SaleService;
import com.nju.edu.erp.service.warehouseservice.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final PromotionService promotionService;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao,
                           ProductDao productDao,
                           CustomerDao customerDao,
                           ProductService productService,
                           CustomerService customerService,
                           WarehouseService warehouseService,
                           PromotionService promotionService) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.promotionService = promotionService;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // TODO
        // 需要持久化销售单（SaleSheet）和销售单content（SaleSheetContent），其中总价或者折后价格的计算需要在后端进行
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO, saleSheetPO);
        // 此处根据制定单据人员确定操作员
        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal rawTotalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for (SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()) {
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content, sContentPO);
            sContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            if(unitPrice == null) {
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getPurchasePrice();
                sContentPO.setUnitPrice(unitPrice);
            }
            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            rawTotalAmount = rawTotalAmount.add(sContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(sContentPOList);
        PricePackVO pricePackVO = promotionService.getPricePackById(saleSheetVO.getPricePackId());
        saleSheetPO.setRawTotalAmount(rawTotalAmount.add(pricePackVO == null ? BigDecimal.ZERO : pricePackVO.getAmount()));
        BigDecimal finalAmount = rawTotalAmount.multiply(saleSheetPO.getDiscount()).subtract(saleSheetPO.getVoucherAmount());
        saleSheetPO.setFinalAmount(finalAmount.add(pricePackVO == null ? BigDecimal.ZERO : pricePackVO.getAmount()));
        AmountPromotionVO amountPromotionByPrice = promotionService.getAmountPromotionByPrice(finalAmount);
        if (amountPromotionByPrice != null) {
            saleSheetPO.setVoucherAmount(saleSheetPO.getVoucherAmount().add(amountPromotionByPrice.getVoucher()));
            saleSheetPO.setFinalAmount(saleSheetPO.getFinalAmount().subtract(amountPromotionByPrice.getVoucher()));
        }
        saleSheetDao.saveSheet(saleSheetPO);
    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        // 根据单据状态获取销售单（注意：VO包含SaleSheetContent）
        // 依赖的dao层部分方法未提供，需要自己实现
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if (state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllByState(state);
        }
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

    /**
     * 根据销售单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleSheetId 销售单id
     * @param state       销售单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        /* 一些注意点：
            1. 二级审批成功之后需要进行
                 1. 修改单据状态
                 2. 更新商品表
                 3. 更新客户表
                 4. 新建出库草稿
            2. 一级审批状态不能直接到审批完成状态； 二级审批状态不能回到一级审批状态
         */
        if (state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if (saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleSheetState prevState;
            if (state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            if (state.equals(SaleSheetState.SUCCESS)) {
                //更新商品表
                List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS = new ArrayList<>();

                for (SaleSheetContentPO content : saleSheetContent) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());
                    productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
                    woContentVO.setSalePrice(content.getUnitPrice());
                    woContentVO.setQuantity(content.getQuantity());
                    woContentVO.setRemark(content.getRemark());
                    woContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(woContentVO);
                }
                //更新客户表
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                //处理特价包
                PricePackVO pricePackVO = promotionService.getPricePackById(saleSheet.getPricePackId());
                if (pricePackVO != null) {
                    int count = 0;
                    for (PricePackContentVO contentVO : pricePackVO.getPricePackContent()) {
                        count += contentVO.getQuantity();
                    }
                    for (PricePackContentVO contentVO : pricePackVO.getPricePackContent()) {
                        WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
                        woContentVO.setSalePrice(pricePackVO.getAmount().divide(BigDecimal.valueOf(count), RoundingMode.HALF_DOWN));
                        woContentVO.setQuantity(contentVO.getQuantity());
                        woContentVO.setRemark(null);
                        woContentVO.setPid(contentVO.getPid());
                        warehouseOutputFormContentVOS.add(woContentVO);
                    }
                }

                //制定出库单草稿
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null);
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);

                //制定库存赠送单,按等级
                List<GiftVO> giftList = promotionService.getPromotion(customerPO.getLevel()).getGiftList();
                if (giftList.size() > 0) {
                    List<WarehouseGiftSheetContentVO> warehouseGiftSheetContentVOList = new ArrayList<>();
                    WarehouseGiftSheetVO warehouseGiftSheetVO = new WarehouseGiftSheetVO();
                    for (GiftVO gift : giftList) {
                        WarehouseGiftSheetContentVO warehouseGiftSheetContentVO = new WarehouseGiftSheetContentVO();
                        BeanUtils.copyProperties(gift, warehouseGiftSheetContentVO);
                        warehouseGiftSheetContentVOList.add(warehouseGiftSheetContentVO);
                    }
                    warehouseGiftSheetVO.setWarehouseGiftSheetContent(warehouseGiftSheetContentVOList);
                    warehouseGiftSheetVO.setSaleSheetId(saleSheetId);
                    warehouseService.makeGiftSheet(warehouseGiftSheetVO);
                }
                //制定库存赠送单,按总价
                AmountPromotionVO amountPromotionByPrice = promotionService.getAmountPromotionByPrice(saleSheet.getFinalAmount());
                if (amountPromotionByPrice != null) {
                    List<AmountPromotionContentVO> contentList = amountPromotionByPrice.getAmountPromotionContent();
                    if (contentList.size() > 0) {
                        List<WarehouseGiftSheetContentVO> warehouseGiftSheetContentVOList = new ArrayList<>();
                        WarehouseGiftSheetVO warehouseGiftSheetVO = new WarehouseGiftSheetVO();
                        for (AmountPromotionContentVO gift : contentList) {
                            WarehouseGiftSheetContentVO warehouseGiftSheetContentVO = new WarehouseGiftSheetContentVO();
                            BeanUtils.copyProperties(gift, warehouseGiftSheetContentVO);
                            warehouseGiftSheetContentVOList.add(warehouseGiftSheetContentVO);
                        }
                        warehouseGiftSheetVO.setWarehouseGiftSheetContent(warehouseGiftSheetContentVOList);
                        warehouseGiftSheetVO.setSaleSheetId(saleSheetId);
                        warehouseService.makeGiftSheet(warehouseGiftSheetVO);

                    }
                }
            }
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     * @param salesman 销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr 结束时间字符串
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman, String beginDateStr, String endDateStr){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman,beginTime,endTime);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据销售单Id搜索销售单信息
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content: contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }
}

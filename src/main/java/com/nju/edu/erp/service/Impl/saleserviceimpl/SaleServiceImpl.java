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
        // ???????????????????????????SaleSheet???????????????content???SaleSheetContent??????????????????????????????????????????????????????????????????
        // ?????????service???dao???????????????????????????????????????????????????????????????
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO, saleSheetPO);
        // ?????????????????????????????????????????????
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
        // ?????????????????????????????????????????????VO??????SaleSheetContent???
        // ?????????dao?????????????????????????????????????????????
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
     * ???????????????id????????????(state == "???????????????"/"????????????"/"????????????")
     * ???controller?????????????????????
     *
     * @param saleSheetId ?????????id
     * @param state       ???????????????????????????
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // ?????????service???dao???????????????????????????????????????????????????????????????
        /* ??????????????????
            1. ????????????????????????????????????
                 1. ??????????????????
                 2. ???????????????
                 3. ???????????????
                 4. ??????????????????
            2. ?????????????????????????????????????????????????????? ????????????????????????????????????????????????
         */
        if (state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if (saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if (effectLines == 0) throw new RuntimeException("??????????????????");
        } else {
            SaleSheetState prevState;
            if (state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("??????????????????");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("??????????????????");
            if (state.equals(SaleSheetState.SUCCESS)) {
                //???????????????
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
                //???????????????
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                //???????????????
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

                //?????????????????????
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null);
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);

                //?????????????????????,?????????
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
                //?????????????????????,?????????
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
     * ?????????????????????????????????????????????????????????????????????(?????????????????????,??????????????????????????????,????????????????????????????????????????????????)
     * @param salesman ?????????????????????
     * @param beginDateStr ?????????????????????
     * @param endDateStr ?????????????????????
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
     * ???????????????Id?????????????????????
     * @param saleSheetId ?????????Id
     * @return ?????????????????????
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

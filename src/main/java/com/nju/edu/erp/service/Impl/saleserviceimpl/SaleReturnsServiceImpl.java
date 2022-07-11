package com.nju.edu.erp.service.Impl.saleserviceimpl;

import com.nju.edu.erp.dao.customerdao.CustomerDao;
import com.nju.edu.erp.dao.saledao.SaleReturnsSheetDao;
import com.nju.edu.erp.dao.saledao.SaleSheetDao;
import com.nju.edu.erp.dao.warehousedao.WarehouseDao;
import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.salereturns.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehousePO;
import com.nju.edu.erp.model.vo.product.ProductInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.salereturns.SaleReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.salereturns.SaleReturnsSheetVO;
import com.nju.edu.erp.service.customerservice.CustomerService;
import com.nju.edu.erp.service.productservice.ProductService;
import com.nju.edu.erp.service.saleservice.SaleReturnsService;
import com.nju.edu.erp.service.warehouseservice.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SaleReturnsServiceImpl implements SaleReturnsService {

    SaleReturnsSheetDao saleReturnsSheetDao;

    SaleSheetDao saleSheetDao;

    WarehouseDao warehouseDao;

    CustomerDao customerDao;

    ProductService productService;

    CustomerService customerService;

    WarehouseService warehouseService;

    @Autowired
    public SaleReturnsServiceImpl(SaleReturnsSheetDao saleReturnsSheetDao, SaleSheetDao saleSheetDao, WarehouseDao warehouseDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService) {
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.saleSheetDao = saleSheetDao;
        this.warehouseDao = warehouseDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
    }

    /**
     * 制定销售退货单
     *
     * @param saleReturnsSheetVO 销售退货单
     */
    @Override
    @Transactional
    public void makeSaleReturnsSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO) {
        SaleReturnsSheetPO saleReturnsSheetPO = new SaleReturnsSheetPO();
        BeanUtils.copyProperties(saleReturnsSheetVO, saleReturnsSheetPO);

        saleReturnsSheetPO.setOperator(userVO.getName());
        saleReturnsSheetPO.setSalesman(userVO.getName());
        saleReturnsSheetPO.setCreateTime(new Date());
        SaleReturnsSheetPO latest = saleReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSTHD");
        saleReturnsSheetPO.setId(id);
        saleReturnsSheetPO.setState(SaleReturnsSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleReturnsSheetPO.getSaleSheetId());
        Map<String, SaleSheetContentPO> map = new HashMap<>();
        for (SaleSheetContentPO item : saleSheetContent) {
            map.put(item.getPid(), item);
        }
        List<SaleReturnsSheetContentPO> sContentPOList = new ArrayList<>();
        //获取折扣和代金券
        SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleReturnsSheetPO.getSaleSheetId());
        BigDecimal discount = saleSheet.getDiscount();
        BigDecimal voucher = saleSheet.getVoucherAmount();
        BigDecimal afterDiscountTotal = saleSheet.getRawTotalAmount().multiply(discount);
        for (SaleReturnsSheetContentVO content : saleReturnsSheetVO.getSaleReturnsSheetContent()) {
            SaleReturnsSheetContentPO sContentPO = new SaleReturnsSheetContentPO();
            BeanUtils.copyProperties(content, sContentPO);
            sContentPO.setSaleReturnsSheetId(id);
            SaleSheetContentPO item = map.get(sContentPO.getPid());
            sContentPO.setRawUnitPrice(item.getUnitPrice());

            BigDecimal discountUnitPrice = item.getUnitPrice().multiply(discount);
            BigDecimal newUnitPrice = discountUnitPrice.subtract(discountUnitPrice.divide(afterDiscountTotal, new MathContext(2, RoundingMode.HALF_UP)).multiply(voucher));
            sContentPO.setNewUnitPrice(newUnitPrice);
            sContentPO.setTotalPrice(newUnitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }
        saleReturnsSheetDao.saveBatch(sContentPOList);
        saleReturnsSheetPO.setTotalAmount(totalAmount);
        saleReturnsSheetDao.save(saleReturnsSheetPO);
    }

    /**
     * 根据状态获取销售退货单(state == null 则获取所有进货退货单)
     *
     * @param state 销售退货单状态
     * @return 销售退货单
     */
    @Override
    public List<SaleReturnsSheetVO> getSaleReturnsSheetByState(SaleReturnsSheetState state) {
        List<SaleReturnsSheetVO> res = new ArrayList<>();
        List<SaleReturnsSheetPO> all;
        if (state == null) {
            all = saleReturnsSheetDao.findAll();
        } else {
            all = saleReturnsSheetDao.findAllByState(state);
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

    /**
     * 根据销售退货单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleReturnsSheetId 销售退货单id
     * @param state           销售退货单要达到的状态
     */
    @Override
    public void approval(String saleReturnsSheetId, SaleReturnsSheetState state) {
        SaleReturnsSheetPO saleReturnsSheet = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if (state.equals(SaleReturnsSheetState.FAILURE)) {
            if (saleReturnsSheet.getState() == SaleReturnsSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleReturnsSheetDao.updateState(saleReturnsSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleReturnsSheetState prevState;
            if (state.equals(SaleReturnsSheetState.SUCCESS)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleReturnsSheetState.PENDING_LEVEL_2)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleReturnsSheetDao.updateStateV2(saleReturnsSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            if (state.equals(SaleReturnsSheetState.SUCCESS)) {
                //审批完成
                //获取出库单 -> 获取content -> 获取batchId
                WarehouseOutputSheetPO warehouseOutputSheet = warehouseService.getWarehouseOutSheetBySaleSheetId(saleReturnsSheet.getSaleSheetId());
                List<SaleReturnsSheetContentPO> contents = saleReturnsSheetDao.findContentBySaleReturnsSheetId(saleReturnsSheetId);
                BigDecimal receivableToDeduct = BigDecimal.ZERO;
                for (SaleReturnsSheetContentPO content : contents) {
                    String pid = content.getPid();
                    Integer quantity = content.getQuantity();
                    List<WarehouseOutputSheetContentPO> wContents = saleReturnsSheetDao.findWarehouseOutSheetContent(warehouseOutputSheet.getId(), pid);
                    for (WarehouseOutputSheetContentPO wContent : wContents) {
                        Integer batchId = wContent.getBatchId();
                        WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid, batchId);
                        if (warehousePO == null) throw new RuntimeException("单据发生错误! 请联系管理员");
                        if (wContent.getQuantity() >= quantity) {
                            //一个批次即可
                            warehousePO.setQuantity(quantity);
                            warehouseDao.addQuantity(warehousePO);
                            ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                            productInfoVO.setQuantity(productInfoVO.getQuantity() + quantity);
                            productService.updateProduct(productInfoVO);
                            receivableToDeduct = receivableToDeduct.add(content.getNewUnitPrice().multiply(BigDecimal.valueOf(quantity)));
                            break;
                        } else {
                            //需要退多个批次
                            quantity -= wContent.getQuantity();
                            warehousePO.setQuantity(wContent.getQuantity());
                            warehouseDao.addQuantity(warehousePO);
                            ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                            productInfoVO.setQuantity(productInfoVO.getQuantity() + wContent.getQuantity());
                            productService.updateProduct(productInfoVO);
                            receivableToDeduct = receivableToDeduct.add(content.getNewUnitPrice().multiply(BigDecimal.valueOf(wContent.getQuantity())));
                        }
                    }
                    SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnsSheet.getSaleSheetId());
                    Integer supplier = saleSheetPO.getSupplier();
                    CustomerPO customer = customerService.findCustomerById(supplier);

                    customer.setReceivable(customer.getReceivable().subtract(receivableToDeduct));
                    customerService.updateCustomer(customer);
                }
            }
        }
    }
}

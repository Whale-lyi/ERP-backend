package com.nju.edu.erp.service.Impl.viewsheetserviceimpl;

import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.model.vo.SheetVO;
import com.nju.edu.erp.model.vo.businesscondition.BusinessConditionVO;
import com.nju.edu.erp.model.vo.product.ProductInfoVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchasereturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGiftSheetContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseGiftSheetVO;
import com.nju.edu.erp.service.Impl.sheetserviceimpl.SalaryServiceImpl;
import com.nju.edu.erp.service.productservice.ProductService;
import com.nju.edu.erp.service.purchaseservice.PurchaseReturnsService;
import com.nju.edu.erp.service.purchaseservice.PurchaseService;
import com.nju.edu.erp.service.saleservice.SaleService;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.service.viewsheetservice.BusinessConditionService;
import com.nju.edu.erp.service.warehouseservice.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BusinessConditionServiceImpl implements BusinessConditionService {

    private final SaleService saleService;
    private final PurchaseService purchaseService;
    private final PurchaseReturnsService purchaseReturnsService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final SheetService salaryService;

    @Autowired
    public BusinessConditionServiceImpl(SaleService saleService,
                                        PurchaseService purchaseService,
                                        PurchaseReturnsService purchaseReturnsService,
                                        WarehouseService warehouseService,
                                        ProductService productService,
                                        @Qualifier("salaryServiceImpl") SheetService salaryService) {
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.salaryService = salaryService;
    }

    @Override
    public BusinessConditionVO search() {
        BusinessConditionVO condition = new BusinessConditionVO();

        BigDecimal saleIncome = BigDecimal.ZERO;
        List<SaleSheetVO> saleSheetByState = saleService.getSaleSheetByState(SaleSheetState.SUCCESS);
        for (SaleSheetVO saleSheetVO : saleSheetByState) {
            saleIncome = saleIncome.add(saleSheetVO.getFinalAmount());
        }
        condition.setSaleIncome(saleIncome);

        BigDecimal commodityIncome = BigDecimal.ZERO;
        List<PurchaseSheetVO> purchaseSheetByState = purchaseService.getPurchaseSheetByState(PurchaseSheetState.SUCCESS);
        for (PurchaseSheetVO purchaseSheetVO : purchaseSheetByState) {
            List<PurchaseReturnsSheetVO> purchaseReturnsSheetByState = purchaseReturnsService.getPurchaseReturnsSheetByState(PurchaseReturnsSheetState.SUCCESS);
            BigDecimal returnAmount = BigDecimal.ZERO;
            for (PurchaseReturnsSheetVO purchaseReturnsSheetVO : purchaseReturnsSheetByState) {
                if (purchaseReturnsSheetVO.getPurchaseSheetId().equals(purchaseSheetVO.getId())) {
                    returnAmount = returnAmount.add(purchaseReturnsSheetVO.getTotalAmount());
                }
            }
            if (!returnAmount.equals(BigDecimal.ZERO))
                commodityIncome = commodityIncome.add(purchaseSheetVO.getTotalAmount().subtract(returnAmount));
        }
        condition.setCommodityIncome(commodityIncome);
        condition.setTotalIncome(condition.getSaleIncome().add(condition.getCommodityIncome()));

        BigDecimal saleCost = BigDecimal.ZERO;
        for (PurchaseSheetVO purchaseSheetVO : purchaseSheetByState) {
            saleCost = saleCost.add(purchaseSheetVO.getTotalAmount());
        }
        condition.setSaleCost(saleCost);

        BigDecimal commodityCost = BigDecimal.ZERO;
        List<WarehouseGiftSheetVO> giftSheetByState = warehouseService.getGiftSheetByState(WarehouseGiftSheetState.SUCCESS);
        for (WarehouseGiftSheetVO warehouseGiftSheetVO : giftSheetByState) {
            List<WarehouseGiftSheetContentVO> warehouseGiftSheetContent = warehouseGiftSheetVO.getWarehouseGiftSheetContent();
            for (WarehouseGiftSheetContentVO warehouseGiftSheetContentVO : warehouseGiftSheetContent) {
                ProductInfoVO oneProductByPid = productService.getOneProductByPid(warehouseGiftSheetContentVO.getPid());
                BigDecimal temp = oneProductByPid.getPurchasePrice().multiply(BigDecimal.valueOf(warehouseGiftSheetContentVO.getQuantity()));
                commodityCost = commodityCost.add(temp);
            }
        }
        condition.setCommodityCost(commodityCost);

        BigDecimal humanCost = BigDecimal.ZERO;
        List<SheetVO> sheetByState = salaryService.getSheetByState(SalarySheetState.SUCCESS);
        for (SheetVO sheetVO : sheetByState) {
            SalarySheetVO salarySheetVO = (SalarySheetVO) sheetVO;
            humanCost = humanCost.add(salarySheetVO.getFinalSalary());
        }
        condition.setHumanCost(humanCost);
        condition.setTotalCost(condition.getSaleCost().add(condition.getCommodityCost()).add(condition.getHumanCost()));
        condition.setProfit(condition.getTotalIncome().subtract(condition.getTotalCost()));
        return condition;
    }

}

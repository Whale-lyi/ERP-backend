package com.nju.edu.erp.web.controller.viewsheetcontroller;

import com.nju.edu.erp.model.vo.saleDetail.ConditionVO;
import com.nju.edu.erp.service.viewsheetservice.SaleDetailService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/saleDetail")
public class SaleDetailController {

    private final SaleDetailService saleDetailService;

    @Autowired
    public SaleDetailController(SaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }

    /**
     * 查看销售明细表
     * @param conditionVO
     * @return
     */
    @PostMapping(value = "/search")
    public Response searchSaleDetail(@RequestBody ConditionVO conditionVO) {
        return Response.buildSuccess(saleDetailService.searchSaleDetail(conditionVO));
    }
}

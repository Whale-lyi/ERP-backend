package com.nju.edu.erp.web.controller.sheetcontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payment")
public class PaymentController {

    private final SheetService sheetService;

    @Autowired
    public PaymentController(@Qualifier("paymentServiceImpl") SheetService sheetService) {
        this.sheetService = sheetService;
    }

    /**
     * 财务人员制定付款单
     */
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF, Role.GM})
    @PostMapping(value = "/sheet-make")
    public Response makePaymentSheet(UserVO userVO, @RequestBody PaymentSheetVO paymentSheetVO) {
        sheetService.makeSheet(userVO, paymentSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看付款单
     */
    @GetMapping("/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) PaymentSheetState state) {
        return Response.buildSuccess(sheetService.getSheetByState(state));
    }

    /**
     * 总经理审批
     * @param id 付款单id;
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.ADMIN, Role.GM})
    @GetMapping(value = "/approval")
    public Response approval(@RequestParam("paymentSheetId") String id,
                             @RequestParam("state") PaymentSheetState state) {
        if (state.equals(PaymentSheetState.FAILURE) || state.equals(PaymentSheetState.SUCCESS)) {
            sheetService.approval(id, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败");
        }
    }

    /**
     * 根据付款单id搜索付款单信息
     */
    @GetMapping("/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id) {
        return Response.buildSuccess(sheetService.getSheetById(id));
    }
}

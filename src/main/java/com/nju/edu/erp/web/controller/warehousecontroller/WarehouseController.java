package com.nju.edu.erp.web.controller.warehousecontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.warehouse.*;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.GetWareProductInfoParamsVO;
import com.nju.edu.erp.service.warehouseservice.WarehouseService;
import com.nju.edu.erp.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/warehouse")
public class WarehouseController {

    public WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/product/count")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseOutput(@RequestBody GetWareProductInfoParamsVO getWareProductInfoParamsVO){
        return Response.buildSuccess(warehouseService.getWareProductInfo(getWareProductInfoParamsVO));
    }

    @GetMapping("/inputSheet/approve")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseInputSheetApprove(UserVO user,
                                               @RequestParam(value = "sheetId") String sheetId,
                                               @RequestParam(value = "state") WarehouseInputSheetState state) {
        if (state.equals(WarehouseInputSheetState.FAILURE) || state.equals(WarehouseInputSheetState.SUCCESS)) {
            warehouseService.approvalInputSheet(user, sheetId, state);
        }
        else {
            throw new MyServiceException("C00001", "越权访问！");
        }
        return Response.buildSuccess();
    }

    @GetMapping("/inputSheet/state")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER})
    public Response getWarehouseInputSheet(@RequestParam(value = "state", required = false) WarehouseInputSheetState state) {
        List<WarehouseInputSheetPO> ans = warehouseService.getWareHouseInputSheetByState(state);
        return Response.buildSuccess(ans);
    }


    @GetMapping("/outputSheet/state")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseOutSheet(@RequestParam(value = "state", required = false) WarehouseOutputSheetState state) {
        List<WarehouseOutputSheetPO> ans = warehouseService.getWareHouseOutSheetByState(state);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/inputSheet/content")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseInputSheetContent(@RequestParam(value = "sheetId") String sheetId) {
        List<WarehouseInputSheetContentPO> ans = warehouseService.getWHISheetContentById(sheetId);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/outputSheet/content")
    @Authorized(roles = {Role.ADMIN, Role.INVENTORY_MANAGER, Role.GM})
    public Response getWarehouseOutputSheetContent(@RequestParam(value = "sheetId") String sheetId) {
        List<WarehouseOutputSheetContentPO> ans = warehouseService.getWHOSheetContentById(sheetId);
        return Response.buildSuccess(ans);
    }

    @GetMapping("/outputSheet/approve")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.INVENTORY_MANAGER})
    public Response warehouseOutputSheetApprove(UserVO user,
                                                @RequestParam(value = "sheetId") String sheetId,
                                                @RequestParam(value = "state") WarehouseOutputSheetState state) {
        if (state.equals(WarehouseOutputSheetState.FAILURE) || state.equals(WarehouseOutputSheetState.SUCCESS)) {
            warehouseService.approvalOutputSheet(user, sheetId, state);
        }
        else {
            throw new MyServiceException("C00001", "越权访问！");
        }
        return Response.buildSuccess();
    }



    /**
     *库存查看：查询指定时间段内出/入库数量/金额/商品信息/分类信息
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/sheetContent/time")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseIODetailByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException {
        List<WarehouseIODetailPO> ans=warehouseService.getWarehouseIODetailByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(ans);
    }

    /**
     *库存查看：一个时间段内的入库数量合计
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/inputSheet/time/quantity")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseInputProductQuantityByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException{
        int quantity= warehouseService.getWarehouseInputProductQuantityByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(quantity);
    }

    /**
     *库存查看：一个时间段内的出库数量合计
     * @param beginDateStr 格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @param endDateStr   格式：“yyyy-MM-dd HH:mm:ss”，如“2022-05-12 11:38:30”
     * @return
     */
    @GetMapping("/outputSheet/time/quantity")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseOutputProductQuantityByTime(@RequestParam String beginDateStr,@RequestParam String endDateStr) throws ParseException{
        int quantity= warehouseService.getWarehouseOutProductQuantityByTime(beginDateStr,endDateStr);
        return Response.buildSuccess(quantity);
    }

    /**
     * 库存盘点
     * 盘点的是当天的库存快照，包括当天的各种商品的
     * 名称，型号，库存数量，库存均价，批次，批号，出厂日期，并且显示行号。
     * 要求可以导出Excel
     *
     */
    @GetMapping("/warehouse/counting")
    @Authorized(roles = {Role.ADMIN,Role.INVENTORY_MANAGER})
    public Response getWarehouseCounting() {
        return Response.buildSuccess(warehouseService.warehouseCounting());
    }

    @GetMapping("/warehouse/export")
    public void exportExcel(HttpServletResponse response) {
        warehouseService.exportExcel(response);
    }

    /**
     * 总经理审批库存赠送单
     * @param id 库存赠送单id;
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.ADMIN, Role.GM})
    @GetMapping(value = "/giftSheet/approval")
    public Response approval(@RequestParam("warehouseGiftSheetId") String id,
                             @RequestParam("state") WarehouseGiftSheetState state) {
        if (state.equals(WarehouseGiftSheetState.FAILURE) || state.equals(WarehouseGiftSheetState.SUCCESS)) {
            warehouseService.approvalGiftSheet(id, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败");
        }
    }

    /**
     * 根据状态查看库存赠送单
     * @param state 库存赠送单状态
     * @return List<WarehouseGiftSheetVO>
     */
    @GetMapping("/giftSheet/show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) WarehouseGiftSheetState state) {
        return Response.buildSuccess(warehouseService.getGiftSheetByState(state));
    }
}

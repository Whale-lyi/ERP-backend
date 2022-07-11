package com.nju.edu.erp.web.controller.sheetcontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.CollectionSheetState;
import com.nju.edu.erp.model.vo.collection.CollectionSheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/collection")
public class CollectionController {

    private final SheetService sheetService;

    @Autowired
    public CollectionController(@Qualifier("collectionServiceImpl") SheetService sheetService) {
        this.sheetService = sheetService;
    }

    /**
     * 财务人员制定收款单
     */
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF, Role.GM})
    @PostMapping(value = "/sheet-make")
    public Response makeCollectionSheet(UserVO userVO, @RequestBody CollectionSheetVO collectionSheetVO) {
        sheetService.makeSheet(userVO, collectionSheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看收款单
     */
    @GetMapping("/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false)CollectionSheetState state) {
        return Response.buildSuccess(sheetService.getSheetByState(state));
    }

    /**
     * 总经理审批
     * @param id 收款单id;
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.ADMIN, Role.GM})
    @GetMapping(value = "/approval")
    public Response approval(@RequestParam("collectionSheetId") String id,
                             @RequestParam("state") CollectionSheetState state) {
        if (state.equals(CollectionSheetState.FAILURE) || state.equals(CollectionSheetState.SUCCESS)) {
            sheetService.approval(id, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败");
        }
    }

    /**
     * 根据收款单id搜索收款单信息
     */
    @GetMapping("/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id) {
        return Response.buildSuccess(sheetService.getSheetById(id));
    }
}

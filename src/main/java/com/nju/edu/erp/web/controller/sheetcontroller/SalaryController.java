package com.nju.edu.erp.web.controller.sheetcontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.hrservice.HRService;
import com.nju.edu.erp.service.sheetservice.SheetService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {

    private final SheetService sheetService;
    private final HRService hrService;

    @Autowired
    public SalaryController(@Qualifier("salaryServiceImpl") SheetService sheetService, HRService hrService) {
        this.sheetService = sheetService;
        this.hrService = hrService;
    }

    /**
     * 财务人员制定工资单
     */
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF, Role.GM})
    @PostMapping(value = "/sheet-make")
    public Response makeSalarySheet(@RequestBody SalarySheetVO salarySheetVO) {
        sheetService.makeSheet(new UserVO(), salarySheetVO);
        return Response.buildSuccess();
    }

    /**
     * 根据状态查看工资单
     */
    @GetMapping("/sheet-show")
    public Response showSheetByState(@RequestParam(value = "state", required = false) SalarySheetState state) {
        return Response.buildSuccess(sheetService.getSheetByState(state));
    }

    /**
     * 总经理审批
     * @param id 工资单id;
     * @param state 修改后的状态("审批失败"/"审批完成")
     */
    @Authorized(roles = {Role.ADMIN, Role.GM})
    @GetMapping(value = "/approval")
    public Response approval(@RequestParam("salarySheetId") String id,
                             @RequestParam("state") SalarySheetState state) {
        if (state.equals(SalarySheetState.FAILURE) || state.equals(SalarySheetState.SUCCESS)) {
            sheetService.approval(id, state);
            return Response.buildSuccess();
        } else {
            return Response.buildFailed("000000", "操作失败");
        }
    }

    /**
     * 根据工资单id搜索工资单信息
     */
    @GetMapping("/find-sheet")
    public Response findBySheetId(@RequestParam(value = "id") String id) {
        return Response.buildSuccess(sheetService.getSheetById(id));
    }

    /**
     * 根据员工获取工资
     */
    @PostMapping("/getSalaryAndTax")
    public Response getSalaryAndTax(@RequestBody User user) {
        return Response.buildSuccess(hrService.getSalaryAndTax(user));
    }

    /**
     * 设置基本工资
     */
    @GetMapping("/setSalary")
    public Response setSalary(@RequestParam("role")Role role, @RequestParam("salary") BigDecimal salary) {
        hrService.setSalary(role, salary);
        return Response.buildSuccess();
    }

    /**
     * 获取所有职位基本工资
     */
    @GetMapping("/getSalary")
    public Response getSalary() {
        return Response.buildSuccess(hrService.getSalary());
    }
}

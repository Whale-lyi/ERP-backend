package com.nju.edu.erp.web.controller.employeecontroller;

import com.nju.edu.erp.model.vo.employee.EmployeeVO;
import com.nju.edu.erp.service.employeeservice.EmployeeService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 注册新员工
     * @param employeeVO
     * @return
     */
    @PostMapping("/register")
    public Response register(@RequestBody EmployeeVO employeeVO) {
        employeeService.register(employeeVO);
        return Response.buildSuccess();
    }

    /**
     * 查找所有员工
     * @return
     */
    @GetMapping("/findAll")
    public Response findAll() {
        return Response.buildSuccess(employeeService.findAll());
    }

    /**
     * 员工打卡
     * @param name  当前登录用户的姓名
     * @return
     */
    @GetMapping("/clock-in")
    public Response clockIn(@RequestParam("name") String name) {
        employeeService.clockIn(name);
        return Response.buildSuccess();
    }

    /**
     * 返回全部员工打卡记录(姓名+总数)
     * @return 全部员工的打卡记录List<ClockInCountVO>,参考vo.clockin.ClockInCountVO
     */
    @GetMapping("/findAllRecord")
    public Response findAllRecord() {
        return Response.buildSuccess(employeeService.findAllRecord());
    }

    /**
     * 返回当前登录用户的打卡信息（姓名+每次打卡时间）
     * @param name 当前登录用户的姓名
     * @return
     */
    @GetMapping("/findRecordByName")
    public Response findRecordByName(@RequestParam("name") String name) {
        return Response.buildSuccess(employeeService.findRecordByName(name));
    }

    /**
     * 返回当前登录用户是否成功打卡
     * @param name
     * @return
     */
    @GetMapping("/checkRecordByName")
    public Response checkRecordByName(@RequestParam("name") String name) {
        return Response.buildSuccess(employeeService.checkRecordByName(name));
    }
}

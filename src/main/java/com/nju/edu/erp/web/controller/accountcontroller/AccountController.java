package com.nju.edu.erp.web.controller.accountcontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.account.AccountVO;
import com.nju.edu.erp.service.accountservice.AccountService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 创建账户
     * @param accountVO
     * @return
     */
    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF})
    public Response createAccount(@RequestBody AccountVO accountVO) {
        return Response.buildSuccess(accountService.createAccount(accountVO));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF})
    public Response deleteAccount(@RequestParam("id") Integer id) {
        accountService.deleteAccountById(id);
        return Response.buildSuccess();
    }

    /**
     * 更新
     * @param accountVO
     * @return
     */
    @PostMapping("/update")
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF})
    public Response updateAccount(@RequestBody AccountVO accountVO) {
        accountService.updateAccount(accountVO);
        return Response.buildSuccess();
    }

    /**
     * 通过关键词查找
     * @param keyword
     * @return
     */
    @GetMapping("/searchList")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.FINANCIAL_STAFF})
    public Response searchAccountList(@RequestParam("keyword") String keyword) {
        return Response.buildSuccess(accountService.findAccountByKeyword(keyword));
    }

    /**
     * 通过id查找
     * @param id
     * @return
     */
    @GetMapping("/find")
    @Authorized(roles = {Role.ADMIN, Role.FINANCIAL_STAFF})
    public Response searchAccountById(@RequestParam("id") Integer id) {
        return Response.buildSuccess(accountService.searchAccountById(id));
    }

}

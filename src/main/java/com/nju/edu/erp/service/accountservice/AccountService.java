package com.nju.edu.erp.service.accountservice;

import com.nju.edu.erp.model.vo.account.AccountVO;

import java.util.List;

public interface AccountService {
    /**
     * 创建账户
     */
    AccountVO createAccount(AccountVO accountVO);

    /**
     * 删除账户
     */
    void deleteAccountById(Integer id);

    /**
     * 修改账户
     */
    void updateAccount(AccountVO accountVO);

    /**
     * 查看账户（id）
     */
    AccountVO searchAccountById(Integer id);

    /**
     * 查询账户(关键字)
     */
    List<AccountVO> findAccountByKeyword(String keyword);
}

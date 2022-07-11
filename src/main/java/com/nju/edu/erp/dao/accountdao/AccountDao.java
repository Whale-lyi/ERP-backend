package com.nju.edu.erp.dao.accountdao;

import com.nju.edu.erp.model.po.account.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountDao {
    /**
     * 创建账户
     */
    Integer createAccount(AccountPO accountPO);

    /**
     * 删除账户
     */
    void deleteAccountById(@Param("id") Integer id);

    /**
     * 修改账户
     */
    void updateAccount(AccountPO accountPO);

    /**
     * 查看账户（id）
     */
    AccountPO searchAccountById(@Param("id") Integer id);

    /**
     * 查询账户(关键字)
     */
    List<AccountPO> findAccountByKeyword(@Param("keyword") String keyword);
}

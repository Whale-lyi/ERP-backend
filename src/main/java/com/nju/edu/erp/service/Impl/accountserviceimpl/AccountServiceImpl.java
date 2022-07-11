package com.nju.edu.erp.service.Impl.accountserviceimpl;

import com.nju.edu.erp.dao.accountdao.AccountDao;
import com.nju.edu.erp.model.po.account.AccountPO;
import com.nju.edu.erp.model.vo.account.AccountVO;
import com.nju.edu.erp.service.accountservice.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public AccountVO createAccount(AccountVO accountVO) {
        assert accountVO.getMoney().compareTo(BigDecimal.ZERO) >=0;
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountVO, accountPO);
        accountDao.createAccount(accountPO);
        accountVO.setId(accountPO.getId());
        return accountVO;
    }

    @Override
    public void deleteAccountById(Integer id) {
        accountDao.deleteAccountById(id);
    }

    @Override
    public void updateAccount(AccountVO accountVO) {
        if (accountVO.getMoney() != null) {
            assert accountVO.getMoney().compareTo(BigDecimal.ZERO) >=0;
        }
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountVO, accountPO);
        accountDao.updateAccount(accountPO);
    }

    @Override
    public AccountVO searchAccountById(Integer id) {
        AccountVO accountVO = new AccountVO();
        AccountPO accountPO = accountDao.searchAccountById(id);
        BeanUtils.copyProperties(accountPO, accountVO);
        return accountVO;
    }

    @Override
    public List<AccountVO> findAccountByKeyword(String keyword) {
        List<AccountVO> list = new ArrayList<>();
        List<AccountPO> poList = accountDao.findAccountByKeyword(keyword);
        for (AccountPO po : poList) {
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(po, accountVO);
            list.add(accountVO);
        }
        return list;
    }
}

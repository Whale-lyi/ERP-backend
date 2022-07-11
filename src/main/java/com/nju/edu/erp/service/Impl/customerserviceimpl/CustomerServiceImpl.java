package com.nju.edu.erp.service.Impl.customerserviceimpl;

import com.nju.edu.erp.dao.customerdao.CustomerDao;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.vo.customer.CustomerVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.customerservice.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 根据id更新客户信息
     *
     * @param customerPO 客户信息
     */
    @Override
    public void updateCustomer(CustomerPO customerPO) {
        customerDao.updateOne(customerPO);
    }

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    @Override
    public List<CustomerPO> getCustomersByType(CustomerType type) {

        return customerDao.findAllByType(type);
    }

    @Override
    public CustomerPO findCustomerById(Integer supplier) {
        return customerDao.findOneById(supplier);
    }

    @Override
    public CustomerVO createCustomer(UserVO userVO, CustomerVO customerVO) {
        customerVO.setReceivable(BigDecimal.ZERO);
        customerVO.setPayable(BigDecimal.ZERO);
        if (userVO.getName() != null) {
            customerVO.setOperator(userVO.getName());
        }
        CustomerPO customerPO = new CustomerPO();
        BeanUtils.copyProperties(customerVO, customerPO);
        int res = customerDao.createCustomer(customerPO);
        if (res == 0) {
            throw new RuntimeException("插入失败");
        }
        customerVO.setId(customerPO.getId());
        return customerVO;
    }

    @Override
    public void deleteCustomer(String id) {
        int res = customerDao.deleteCustomer(id);
        if (res == 0) throw new RuntimeException("删除失败");
    }
}

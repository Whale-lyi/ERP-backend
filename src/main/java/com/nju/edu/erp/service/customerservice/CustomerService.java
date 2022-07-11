package com.nju.edu.erp.service.customerservice;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import com.nju.edu.erp.model.vo.customer.CustomerVO;
import com.nju.edu.erp.model.vo.user.UserVO;

import java.util.List;

public interface CustomerService {
    /**
     * 根据id更新客户信息
     * @param customerPO 客户信息
     */
    void updateCustomer(CustomerPO customerPO);

    /**
     * 根据type查找对应类型的客户
     * @param type 客户类型
     * @return 客户列表
     */
    List<CustomerPO> getCustomersByType(CustomerType type);


    CustomerPO findCustomerById(Integer supplier);

    CustomerVO createCustomer(UserVO userVO, CustomerVO customerVO);

    void deleteCustomer(String id);
}

package com.nju.edu.erp.dao.customerdao;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.customer.CustomerPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    int updateOne(CustomerPO customerPO);

    CustomerPO findOneById(Integer supplier);

    List<CustomerPO> findAllByType(CustomerType customerType);

    int createCustomer(CustomerPO customerPO);

    int deleteCustomer(String id);
}

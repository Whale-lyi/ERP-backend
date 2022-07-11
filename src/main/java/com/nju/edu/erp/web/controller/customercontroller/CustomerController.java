package com.nju.edu.erp.web.controller.customercontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.customer.CustomerVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.customerservice.CustomerService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/findByType")
    public Response findByType(@RequestParam CustomerType type) {
        return Response.buildSuccess(customerService.getCustomersByType(type));
    }

    @PostMapping("/create")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.SALE_STAFF, Role.SALE_MANAGER})
    public Response createCustomer(UserVO userVO, @RequestBody CustomerVO customerVO) {
        return Response.buildSuccess(customerService.createCustomer(userVO, customerVO));
    }

    @GetMapping("/delete")
    @Authorized(roles = {Role.ADMIN, Role.GM, Role.SALE_STAFF, Role.SALE_MANAGER})
    public Response deleteCustomer(@RequestParam("id") String id) {
        customerService.deleteCustomer(id);
        return Response.buildSuccess();
    }

    @GetMapping("/queryAll")
    public Response findAllCustomer() {
        return Response.buildSuccess();
    }
}

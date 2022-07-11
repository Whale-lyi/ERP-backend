package com.nju.edu.erp.web.controller.viewsheetcontroller;

import com.nju.edu.erp.service.viewsheetservice.BusinessConditionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/business-condition")
public class BusinessConditionController {

    private final BusinessConditionService businessConditionService;

    @Autowired
    public BusinessConditionController(BusinessConditionService businessConditionService) {
        this.businessConditionService = businessConditionService;
    }

    @GetMapping("/search")
    public Response search() {
        return Response.buildSuccess(businessConditionService.search());
    }
}

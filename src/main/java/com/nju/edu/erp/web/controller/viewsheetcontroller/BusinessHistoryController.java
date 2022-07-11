package com.nju.edu.erp.web.controller.viewsheetcontroller;

import com.nju.edu.erp.model.vo.businesshistory.Condition;
import com.nju.edu.erp.service.viewsheetservice.HistoryService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/history")
public class BusinessHistoryController {

    private final HistoryService historyService;

    @Autowired
    public BusinessHistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * 通过条件筛选表单
     * @param condition
     * @return
     */
    @PostMapping(value = "/find-sheet")
    public Response findSheetByCondition(@RequestBody Condition condition) {
        return Response.buildSuccess(historyService.findSheetByCondition(condition));
    }

    @GetMapping(value = "/hongchong")
    public Response hongchong(@RequestParam("type") Integer type, @RequestParam("id") String id) {
        historyService.hongchong(type, id);
        return Response.buildSuccess();
    }
}

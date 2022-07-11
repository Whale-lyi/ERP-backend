package com.nju.edu.erp.web.controller.promotioncontroller;

import com.nju.edu.erp.auth.Authorized;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.pricepack.PricePackVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.promotionservice.PromotionService;
import com.nju.edu.erp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/promotion")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    /**
     * 针对不同级别的用户制定促销策略（折扣，代金券，赠品）
     * @param promotionVO 促销策略（折扣，代金券，赠品）
     * @return
     */
    @PostMapping("/set-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response setPromotion(@RequestBody PromotionVO promotionVO) {
        promotionService.setPromotion(promotionVO);
        return Response.buildSuccess();
    }

    /**
     * 根据等级获取客户的促销策略
     * @param level 客户等级
     * @return
     */
    @GetMapping("/get-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getPromotion(@RequestParam("level") Integer level) {
        return Response.buildSuccess(promotionService.getPromotion(level));
    }

    /**
     * 获取全部等级的促销策略
     * @return
     */
    @GetMapping("/getAllPromotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getPromotion() {
        return Response.buildSuccess(promotionService.getAllPromotion());
    }

    /**
     * 总经理设定特价包
     * @param pricePackVO 特价包
     * @return
     */
    @PostMapping("/set-price-pack")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response setPricePack(@RequestBody PricePackVO pricePackVO) {
        promotionService.setPricePack(pricePackVO);
        return Response.buildSuccess();
    }

    /**
     * 获取全部特价包
     * @return List<PricePackVO>
     */
    @GetMapping("/get-price-pack")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getPricePack() {
        return Response.buildSuccess(promotionService.getPricePack());
    }

    /**
     * 通过id获取特价包
     * @return PricePackVO
     */
    @GetMapping("/getPricePackById")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getPricePackById(@RequestParam("id") Integer id) {
        return Response.buildSuccess(promotionService.getPricePackById(id));
    }

    /**
     * 修改特价包
     * @return
     */
    @PostMapping("/update-price-pack")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response updatePricePack(@RequestBody PricePackVO pricePackVO) {
        promotionService.updatePricePack(pricePackVO);
        return Response.buildSuccess();
    }

    /**
     * 删除特价包
     */
    @GetMapping("/delete-price-pack")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response deletePricePackById(@RequestParam("id") Integer id) {
        promotionService.deletePricePack(id);
        return Response.buildSuccess();
    }

    /**
     * 总经理设定总价促销策略
     * @param amountPromotionVO 总价促销策略
     * @return
     */
    @PostMapping("/set-amount-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response setAmountPromotion(@RequestBody AmountPromotionVO amountPromotionVO) {
        promotionService.setAmountPromotion(amountPromotionVO);
        return Response.buildSuccess();
    }

    /**
     * 获取全部总价促销策略
     * @return List<AmountPromotionVO>
     */
    @GetMapping("/get-amount-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response getAmountPromotion() {
        return Response.buildSuccess(promotionService.getAmountPromotion());
    }

    /**
     * 修改总价促销策略
     * @return
     */
    @PostMapping("/update-amount-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response updateAmountPromotion(@RequestBody AmountPromotionVO amountPromotionVO) {
        promotionService.updateAmountPromotion(amountPromotionVO);
        return Response.buildSuccess();
    }

    /**
     * 删除总价促销策略
     */
    @GetMapping("/delete-amount-promotion")
    @Authorized(roles = {Role.ADMIN, Role.GM})
    public Response deleteAmountPromotion(@RequestParam("id") Integer id) {
        promotionService.deleteAmountPromotion(id);
        return Response.buildSuccess();
    }
}

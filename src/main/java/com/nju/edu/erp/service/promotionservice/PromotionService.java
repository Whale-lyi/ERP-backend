package com.nju.edu.erp.service.promotionservice;

import com.nju.edu.erp.model.vo.pricepack.PricePackVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {

    /**
     *针对不同级别的用户制定促销策略
     * @param promotionVO
     */
    void setPromotion(PromotionVO promotionVO);

    /**
     *根据等级获取客户的促销策略
     * @param level
     * @return
     */
    PromotionVO getPromotion(Integer level);

    /**
     *获取全部等级的促销策略
     * @return
     */
    List<PromotionVO> getAllPromotion();

    /**
     *总经理设定特价包
     * @param pricePackVO
     */
    void setPricePack(PricePackVO pricePackVO);

    /**
     *获取全部特价包
     * @return
     */
    List<PricePackVO> getPricePack();

    /**
     *通过id获取特价包
     * @param id
     * @return
     */
    PricePackVO getPricePackById(Integer id);

    /**
     *修改特价包
     * @param pricePackVO
     */
    void updatePricePack(PricePackVO pricePackVO);

    /**
     *删除特价包
     * @param id
     */
    void deletePricePack(Integer id);

    /**
     *总经理设定总价促销策略
     * @param amountPromotionVO
     */
    void setAmountPromotion(AmountPromotionVO amountPromotionVO);

    /**
     *获取全部总价促销策略
     * @return
     */
    List<AmountPromotionVO> getAmountPromotion();

    /**
     *获取最优总价促销策略
     * @param price
     * @return
     */
    AmountPromotionVO getAmountPromotionByPrice(BigDecimal price);

    /**
     *修改总价促销策略
     * @param amountPromotionVO
     */
    void updateAmountPromotion(AmountPromotionVO amountPromotionVO);

    /**
     *删除总价促销策略
     * @param id
     */
    void deleteAmountPromotion(Integer id);
}

package com.nju.edu.erp.dao.promotiondao;

import com.nju.edu.erp.model.po.pricepack.PricePackContentPO;
import com.nju.edu.erp.model.po.pricepack.PricePackPO;
import com.nju.edu.erp.model.po.promotion.AmountPromotionContentPO;
import com.nju.edu.erp.model.po.promotion.AmountPromotionPO;
import com.nju.edu.erp.model.po.promotion.GiftPO;
import com.nju.edu.erp.model.po.promotion.PromotionPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface PromotionDao {
    /**
     * 根据客户等级获取促销力度
     */
    PromotionPO getPromotionByLevel(Integer level);

    /**
     * 更改某一等级促销策略
     */
    Integer updatePromotion(PromotionPO promotionPO);

    /**
     * 根据等级获取赠品
     */
    List<GiftPO> getGiftByLevel(Integer level);

    /**
     * 插入赠品
     */
    Integer insertGift(List<GiftPO> giftPOList);

    /**
     * 清空某一等级的赠品
     */
    Integer deleteGiftsByLevel(Integer level);

    /**
     * 保存特价包
     */
    Integer savePricePack(PricePackPO pricePackPO);

    /**
     * 保存特价包具体信息
     */
    Integer saveBatchPricePackContent(List<PricePackContentPO> pricePackContent);

    /**
     * 获取全部特价包
     */
    List<PricePackPO> getAllPricePack();

    /**
     * 根据id获取特价包
     */
    PricePackPO getPricePackById(Integer id);

    /**
     * 通过特价包id获取具体内容
     */
    List<PricePackContentPO> getContentByPricePackId(Integer id);

    /**
     * 更改某一特价包
     */
    Integer updatePricePack(PricePackPO pricePackPO);

    /**
     * 删除某一特价包
     */
    Integer deletePricePackById(Integer id);

    /**
     * 删除某一特价包具体内容
     */
    Integer deleteContentByPricePackId(Integer id);

    /**
     * 保存总价促销策略
     */
    Integer saveAmountPromotion(AmountPromotionPO amountPromotionPO);

    /**
     * 保存总价促销策略具体赠品
     */
    Integer saveBatchAmountPromotionContent(List<AmountPromotionContentPO> amountPromotionContent);

    /**
     * 获取全部总价促销策略
     */
    List<AmountPromotionPO> getAllAmountPromotion();

    /**
     * 根据总价获取最接近的总价促销策略
     */
    AmountPromotionPO getAmountPromotionByPrice(BigDecimal price);

    /**
     * 通过总价促销策略id获取具体赠品
     */
    List<AmountPromotionContentPO> getContentByAmountPromotionId(Integer id);

    /**
     * 更改某一总价促销策略
     */
    Integer updateAmountPromotion(AmountPromotionPO pricePackPO);

    /**
     * 删除某一总价促销策略
     */
    Integer deleteAmountPromotionById(Integer id);

    /**
     * 删除某一总价促销策略具体赠品
     */
    Integer deleteContentByAmountPromotionId(Integer id);
}

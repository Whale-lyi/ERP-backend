package com.nju.edu.erp.service.Impl.promotionserviceimpl;

import com.nju.edu.erp.dao.productdao.ProductDao;
import com.nju.edu.erp.dao.promotiondao.PromotionDao;
import com.nju.edu.erp.model.po.product.ProductPO;
import com.nju.edu.erp.model.po.promotion.GiftPO;
import com.nju.edu.erp.model.po.promotion.PromotionPO;
import com.nju.edu.erp.model.vo.promotion.GiftVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.promotionservice.PromotionStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LevelOneStrategy implements PromotionStrategy {

    private final PromotionDao promotionDao;
    private final ProductDao productDao;

    @Autowired
    public LevelOneStrategy(PromotionDao promotionDao, ProductDao productDao) {
        this.productDao = productDao;
        this.promotionDao = promotionDao;
    }
    @Override
    public PromotionVO getPromotion() {
        PromotionPO promotionPO = promotionDao.getPromotionByLevel(1);
        List<GiftPO> giftPOList = promotionDao.getGiftByLevel(1);
        PromotionVO promotionVO = new PromotionVO();
        List<GiftVO> giftVOList = new ArrayList<>();
        BeanUtils.copyProperties(promotionPO, promotionVO);
        for (GiftPO giftPO : giftPOList) {
            GiftVO giftVO = new GiftVO();
            BeanUtils.copyProperties(giftPO, giftVO);
            ProductPO productPO = productDao.findById(giftPO.getPid());
            giftVO.setName(productPO.getName());
            giftVOList.add(giftVO);
        }
        promotionVO.setGiftList(giftVOList);
        return promotionVO;
    }
}

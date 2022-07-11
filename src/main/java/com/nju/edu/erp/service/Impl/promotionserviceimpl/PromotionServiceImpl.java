package com.nju.edu.erp.service.Impl.promotionserviceimpl;

import com.nju.edu.erp.dao.productdao.ProductDao;
import com.nju.edu.erp.dao.promotiondao.PromotionDao;
import com.nju.edu.erp.model.po.pricepack.PricePackContentPO;
import com.nju.edu.erp.model.po.pricepack.PricePackPO;
import com.nju.edu.erp.model.po.product.ProductPO;
import com.nju.edu.erp.model.po.promotion.AmountPromotionContentPO;
import com.nju.edu.erp.model.po.promotion.AmountPromotionPO;
import com.nju.edu.erp.model.po.promotion.GiftPO;
import com.nju.edu.erp.model.po.promotion.PromotionPO;
import com.nju.edu.erp.model.vo.pricepack.PricePackContentVO;
import com.nju.edu.erp.model.vo.pricepack.PricePackVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionContentVO;
import com.nju.edu.erp.model.vo.promotion.AmountPromotionVO;
import com.nju.edu.erp.model.vo.promotion.GiftVO;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.service.promotionservice.PromotionService;
import com.nju.edu.erp.service.promotionservice.PromotionStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionDao promotionDao;
    private final ProductDao productDao;
    private final Map<String, PromotionStrategy> map;

    @Autowired
    public PromotionServiceImpl(PromotionDao promotionDao, ProductDao productDao, Map<String, PromotionStrategy> map) {
        this.promotionDao = promotionDao;
        this.productDao = productDao;
        this.map = map;
    }

    @Override
    public void setPromotion(PromotionVO promotionVO) {
        PromotionPO promotionPO = new PromotionPO();
        BeanUtils.copyProperties(promotionVO, promotionPO);
        promotionDao.updatePromotion(promotionPO);
        promotionDao.deleteGiftsByLevel(promotionPO.getLevel());
        List<GiftPO> giftPOList = new ArrayList<>();
        if (promotionVO.getGiftList().size() > 0) {
            for (GiftVO giftVO : promotionVO.getGiftList()) {
                GiftPO giftPO = new GiftPO();
                BeanUtils.copyProperties(giftVO, giftPO);
                giftPO.setLevel(promotionVO.getLevel());
                giftPOList.add(giftPO);
            }
            promotionDao.insertGift(giftPOList);
        }
    }

    @Override
    public PromotionVO getPromotion(Integer level) {
        PromotionStrategy promotionStrategy = null;
        switch (level) {
            case 1:
                promotionStrategy = map.get("levelOneStrategy");
                break;
            case 2:
                promotionStrategy = map.get("levelTwoStrategy");
                break;
            case 3:
                promotionStrategy = map.get("levelThreeStrategy");
                break;
            case 4:
                promotionStrategy = map.get("levelFourStrategy");
                break;
            case 5:
                promotionStrategy = map.get("levelFiveStrategy");
                break;
        }
        assert promotionStrategy != null;
        return promotionStrategy.getPromotion();
    }

    @Override
    public List<PromotionVO> getAllPromotion() {
        List<PromotionVO> res = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            PromotionVO promotionVO = getPromotion(i);
            res.add(promotionVO);
        }
        return res;
    }

    @Override
    public void setPricePack(PricePackVO pricePackVO) {
        PricePackPO pricePackPO = new PricePackPO();
        BeanUtils.copyProperties(pricePackVO, pricePackPO);
        promotionDao.savePricePack(pricePackPO);
        List<PricePackContentPO> pricePackContentPOList = new ArrayList<>();
        for (PricePackContentVO pricePackContentVO : pricePackVO.getPricePackContent()) {
            PricePackContentPO pricePackContentPO = new PricePackContentPO();
            BeanUtils.copyProperties(pricePackContentVO, pricePackContentPO);
            pricePackContentPO.setPricePackId(pricePackPO.getId());
            pricePackContentPOList.add(pricePackContentPO);
        }
        promotionDao.saveBatchPricePackContent(pricePackContentPOList);
    }

    @Override
    public List<PricePackVO> getPricePack() {
        List<PricePackPO> pricePackPOS = promotionDao.getAllPricePack();
        List<PricePackVO> pricePackVOS = new ArrayList<>();
        for (PricePackPO pricePackPO : pricePackPOS) {
            PricePackVO pricePackVO = new PricePackVO();
            BeanUtils.copyProperties(pricePackPO, pricePackVO);
            List<PricePackContentPO> pricePackContentPOList = promotionDao.getContentByPricePackId(pricePackPO.getId());
            List<PricePackContentVO> pricePackContentVOList = new ArrayList<>();
            for (PricePackContentPO pricePackContentPO : pricePackContentPOList) {
                PricePackContentVO pricePackContentVO = new PricePackContentVO();
                BeanUtils.copyProperties(pricePackContentPO, pricePackContentVO);
                ProductPO productPO = productDao.findById(pricePackContentPO.getPid());
                pricePackContentVO.setName(productPO.getName());
                pricePackContentVOList.add(pricePackContentVO);
            }
            pricePackVO.setPricePackContent(pricePackContentVOList);
            pricePackVOS.add(pricePackVO);
        }
        return pricePackVOS;
    }

    @Override
    public PricePackVO getPricePackById(Integer id) {
        if (id == null) return null;
        PricePackPO pricePackPO = promotionDao.getPricePackById(id);
        PricePackVO pricePackVO = new PricePackVO();
        BeanUtils.copyProperties(pricePackPO, pricePackVO);
        List<PricePackContentPO> pricePackContentPOList = promotionDao.getContentByPricePackId(id);
        List<PricePackContentVO> pricePackContentVOList = new ArrayList<>();
        for (PricePackContentPO pricePackContentPO : pricePackContentPOList) {
            PricePackContentVO pricePackContentVO = new PricePackContentVO();
            BeanUtils.copyProperties(pricePackContentPO, pricePackContentVO);
            ProductPO productPO = productDao.findById(pricePackContentPO.getPid());
            pricePackContentVO.setName(productPO.getName());
            pricePackContentVOList.add(pricePackContentVO);
        }
        pricePackVO.setPricePackContent(pricePackContentVOList);
        return pricePackVO;
    }

    @Override
    public void updatePricePack(PricePackVO pricePackVO) {
        PricePackPO pricePackPO = new PricePackPO();
        BeanUtils.copyProperties(pricePackVO, pricePackPO);
        promotionDao.updatePricePack(pricePackPO);
        promotionDao.deleteContentByPricePackId(pricePackPO.getId());
        if (pricePackVO.getPricePackContent().size() > 0) {
            List<PricePackContentPO> pricePackContentPOList = new ArrayList<>();
            for (PricePackContentVO pricePackContentVO : pricePackVO.getPricePackContent()) {
                PricePackContentPO pricePackContentPO = new PricePackContentPO();
                BeanUtils.copyProperties(pricePackContentVO, pricePackContentPO);
                pricePackContentPO.setPricePackId(pricePackPO.getId());
                pricePackContentPOList.add(pricePackContentPO);
            }
            promotionDao.saveBatchPricePackContent(pricePackContentPOList);
        }
    }

    @Override
    public void deletePricePack(Integer id) {
        promotionDao.deletePricePackById(id);
        promotionDao.deleteContentByPricePackId(id);
    }

    @Override
    public void setAmountPromotion(AmountPromotionVO amountPromotionVO) {
        AmountPromotionPO amountPromotionPO = new AmountPromotionPO();
        BeanUtils.copyProperties(amountPromotionVO, amountPromotionPO);
        promotionDao.saveAmountPromotion(amountPromotionPO);
        if (amountPromotionVO.getAmountPromotionContent().size() > 0) {
            List<AmountPromotionContentPO> amountPromotionContentPOS = new ArrayList<>();
            for (AmountPromotionContentVO amountPromotionContent : amountPromotionVO.getAmountPromotionContent()) {
                AmountPromotionContentPO amountPromotionContentPO = new AmountPromotionContentPO();
                BeanUtils.copyProperties(amountPromotionContent, amountPromotionContentPO);
                amountPromotionContentPO.setAmountPromotionId(amountPromotionPO.getId());
                amountPromotionContentPOS.add(amountPromotionContentPO);
            }
            promotionDao.saveBatchAmountPromotionContent(amountPromotionContentPOS);
        }
    }

    @Override
    public List<AmountPromotionVO> getAmountPromotion() {
        List<AmountPromotionPO> amountPromotionPOS = promotionDao.getAllAmountPromotion();
        List<AmountPromotionVO> amountPromotionVOS = new ArrayList<>();
        for (AmountPromotionPO amountPromotionPO : amountPromotionPOS) {
            AmountPromotionVO amountPromotionVO = new AmountPromotionVO();
            BeanUtils.copyProperties(amountPromotionPO, amountPromotionVO);
            List<AmountPromotionContentPO> amountPromotionContentPOS = promotionDao.getContentByAmountPromotionId(amountPromotionPO.getId());
            List<AmountPromotionContentVO> amountPromotionContentVOS = new ArrayList<>();
            for (AmountPromotionContentPO amountPromotionContentPO : amountPromotionContentPOS) {
                AmountPromotionContentVO amountPromotionContentVO = new AmountPromotionContentVO();
                BeanUtils.copyProperties(amountPromotionContentPO, amountPromotionContentVO);
                ProductPO productPO = productDao.findById(amountPromotionContentPO.getPid());
                amountPromotionContentVO.setName(productPO.getName());
                amountPromotionContentVOS.add(amountPromotionContentVO);
            }
            amountPromotionVO.setAmountPromotionContent(amountPromotionContentVOS);
            amountPromotionVOS.add(amountPromotionVO);
        }
        return amountPromotionVOS;
    }

    @Override
    public AmountPromotionVO getAmountPromotionByPrice(BigDecimal price) {
        AmountPromotionPO amountPromotionPO = promotionDao.getAmountPromotionByPrice(price);
        if (amountPromotionPO == null) return null;
        AmountPromotionVO amountPromotionVO = new AmountPromotionVO();
        BeanUtils.copyProperties(amountPromotionPO, amountPromotionVO);
        List<AmountPromotionContentPO> amountPromotionContentPOS = promotionDao.getContentByAmountPromotionId(amountPromotionPO.getId());
        List<AmountPromotionContentVO> amountPromotionContentVOS = new ArrayList<>();
        for (AmountPromotionContentPO amountPromotionContentPO : amountPromotionContentPOS) {
            AmountPromotionContentVO amountPromotionContentVO = new AmountPromotionContentVO();
            BeanUtils.copyProperties(amountPromotionContentPO, amountPromotionContentVO);
            ProductPO productPO = productDao.findById(amountPromotionContentPO.getPid());
            amountPromotionContentVO.setName(productPO.getName());
            amountPromotionContentVOS.add(amountPromotionContentVO);
        }
        amountPromotionVO.setAmountPromotionContent(amountPromotionContentVOS);
        return amountPromotionVO;
    }

    @Override
    public void updateAmountPromotion(AmountPromotionVO amountPromotionVO) {
        AmountPromotionPO amountPromotionPO = new AmountPromotionPO();
        BeanUtils.copyProperties(amountPromotionVO, amountPromotionPO);
        promotionDao.updateAmountPromotion(amountPromotionPO);
        promotionDao.deleteContentByAmountPromotionId(amountPromotionPO.getId());
        if (amountPromotionVO.getAmountPromotionContent().size() > 0) {
            List<AmountPromotionContentPO> amountPromotionContentPOList = new ArrayList<>();
            for (AmountPromotionContentVO amountPromotionContentVO : amountPromotionVO.getAmountPromotionContent()) {
                AmountPromotionContentPO amountPromotionContentPO = new AmountPromotionContentPO();
                BeanUtils.copyProperties(amountPromotionContentVO, amountPromotionContentPO);
                amountPromotionContentPO.setAmountPromotionId(amountPromotionPO.getId());
                amountPromotionContentPOList.add(amountPromotionContentPO);
            }
            promotionDao.saveBatchAmountPromotionContent(amountPromotionContentPOList);
        }
    }

    @Override
    public void deleteAmountPromotion(Integer id) {
        promotionDao.deleteAmountPromotionById(id);
        promotionDao.deleteContentByAmountPromotionId(id);
    }
}

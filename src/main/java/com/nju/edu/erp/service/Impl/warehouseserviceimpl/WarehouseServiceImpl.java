package com.nju.edu.erp.service.Impl.warehouseserviceimpl;

import com.nju.edu.erp.dao.productdao.ProductDao;
import com.nju.edu.erp.dao.warehousedao.WarehouseDao;
import com.nju.edu.erp.dao.warehousedao.WarehouseGiftSheetDao;
import com.nju.edu.erp.dao.warehousedao.WarehouseInputSheetDao;
import com.nju.edu.erp.dao.warehousedao.WarehouseOutputSheetDao;
import com.nju.edu.erp.enums.sheetState.WarehouseGiftSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseInputSheetState;
import com.nju.edu.erp.enums.sheetState.WarehouseOutputSheetState;
import com.nju.edu.erp.model.po.product.ProductPO;
import com.nju.edu.erp.model.po.warehouse.*;
import com.nju.edu.erp.model.vo.product.ProductInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.model.vo.warehouse.*;
import com.nju.edu.erp.service.productservice.ProductService;
import com.nju.edu.erp.service.warehouseservice.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductDao productDao; // 其实不建议直接访问其他服务的dao层
    private final WarehouseDao warehouseDao;
    private final WarehouseInputSheetDao warehouseInputSheetDao;
    private final WarehouseOutputSheetDao warehouseOutputSheetDao;
    private final ProductService productService;
    private final WarehouseGiftSheetDao warehouseGiftSheetDao;


    @Autowired
    public WarehouseServiceImpl(ProductDao productDao,
                                WarehouseDao warehouseDao,
                                WarehouseInputSheetDao warehouseInputSheetDao,
                                WarehouseOutputSheetDao warehouseOutputSheetDao,
                                ProductService productService,
                                WarehouseGiftSheetDao warehouseGiftSheetDao) {
        this.productDao = productDao;
        this.warehouseDao = warehouseDao;
        this.warehouseInputSheetDao = warehouseInputSheetDao;
        this.warehouseOutputSheetDao = warehouseOutputSheetDao;
        this.productService = productService;
        this.warehouseGiftSheetDao = warehouseGiftSheetDao;
    }

    /**
     * 创建入库单
     * @param warehouseInputFormVO 入库单
     */
    @Override
    @Transactional
    public void productWarehousing(WarehouseInputFormVO warehouseInputFormVO) {
        /**
         * 商品入库
         * 1. 查看上一次入库单
         * 2. 根据上一次入库单来创建新入库单(单号/批次号/...)
         * 3. 更新"商品表", 插入"入库单表", 插入"入库单物品列表"表, 插入"库存表" -> 部分步骤放在了审批后..
         */
        WarehouseInputSheetPO warehouseInputSheetPO =  warehouseInputSheetDao.getLatest();
        if(warehouseInputSheetPO == null) warehouseInputSheetPO = WarehouseInputSheetPO.builder().batchId(-1).build();
        WarehouseInputSheetPO toSave = new WarehouseInputSheetPO();
        toSave.setId(generateWarehouseInputId(warehouseInputSheetPO.getId(), warehouseInputSheetPO.getBatchId()));
        toSave.setBatchId(generateBatchId(warehouseInputSheetPO.getBatchId()));
//        toSave.setOperator(warehouseInputFormVO.getOperator());
        toSave.setCreateTime(new Date());
        toSave.setPurchaseSheetId(warehouseInputFormVO.getPurchaseSheetId());
        toSave.setState(WarehouseInputSheetState.DRAFT);

        List<WarehouseInputSheetContentPO> warehouseInputListPOSheetContent = new ArrayList<>();
//        List<WarehousePO> warehousePOList = new ArrayList<>();
        warehouseInputFormVO.getList().forEach(item -> {
            ProductPO productPO = productDao.findById(item.getPid());
//            productPO.setQuantity(productPO.getQuantity()+item.getQuantity());
//            productPO.setRecentPp(item.getPurchasePrice());
//            productDao.updateById(productPO);

            BigDecimal purchasePrice = item.getPurchasePrice();
            if(purchasePrice == null) {
                purchasePrice = productPO.getPurchasePrice();
            }
            WarehouseInputSheetContentPO warehouseInputSheetContentPO = WarehouseInputSheetContentPO.builder()
                    .wiId(toSave.getId())
                    .pid(item.getPid())
                    .quantity(item.getQuantity())
                    .purchasePrice(purchasePrice)
                    .productionDate(item.getProductionDate())
                    .remark(item.getRemark()).build();
            warehouseInputListPOSheetContent.add(warehouseInputSheetContentPO);
//            WarehousePO warehousePO = WarehousePO.builder()
//                    .pid(item.getPid())
//                    .quantity(item.getQuantity())
//                    .purchasePrice(purchasePrice)
//                    .batchId(toSave.getBatchId())
//                    .productionDate(item.getProductionDate()).build();
//            warehousePOList.add(warehousePO);
        } );

        warehouseInputSheetDao.save(toSave);
        warehouseInputSheetDao.saveBatch(warehouseInputListPOSheetContent);
//        warehouseDao.saveBatch(warehousePOList);
    }

    @Override
    @Transactional
    public void productOutOfWarehouse(WarehouseOutputFormVO warehouseOutputFormVO) {
        // TODO 需要进行修改
        /**
         * 商品出库
         * 1. 查到上一次出库单的ID
         * 2. 根据上一次出库单来创建新出库单
         * 3. 更新"ware..output" "ware..list_output" "warehouse" 和 "product"表
         * 逻辑跟创建入库单相似,
         *      区别有：     1. 出库单的单号需要换种方式计算
         *                 2. 批次是从前端传进来的
         *                 3. 对于warehouse表采取批量更新而不是批量新增操作
         */
        WarehouseOutputSheetPO warehouseOutputSheetPO = warehouseOutputSheetDao.getLatest();
        WarehouseOutputSheetPO toSave = new WarehouseOutputSheetPO();
        toSave.setId(generateWarehouseOutputId(warehouseOutputSheetPO == null ? null : warehouseOutputSheetPO.getId()));
        // toSave.setOperator(warehouseOutputFormVO.getOperator());
        toSave.setCreateTime(new Date());
        toSave.setSaleSheetId(warehouseOutputFormVO.getSaleSheetId());
        toSave.setState(WarehouseOutputSheetState.DRAFT);

        List<WarehouseOutputSheetContentPO> warehouseOutputListPOSheetContent = new ArrayList<>();
        warehouseOutputFormVO.getList().forEach(item -> {
            ProductPO productPO = productDao.findById(item.getPid());
//            productPO.setQuantity(productPO.getQuantity()-item.getQuantity());
//            productDao.updateById(productPO);
            BigDecimal salePrice = item.getSalePrice();
            if(salePrice == null) {
                salePrice = productPO.getRetailPrice();
            }

            WarehouseOutputSheetContentPO warehouseOutputSheetContentPO = WarehouseOutputSheetContentPO.builder()
                    .woId(toSave.getId())
                    .pid(item.getPid())
                    .quantity(item.getQuantity())
                    .salePrice(salePrice)
                    .batchId(item.getBatchId())
                    .remark(item.getRemark()).build();
            warehouseOutputListPOSheetContent.add(warehouseOutputSheetContentPO);
//            WarehousePO warehousePO = WarehousePO.builder()
//                    .pid(item.getPid())
//                    .batchId(item.getBatchId())
//                    .quantity(item.getQuantity())
//                    .build();
//            warehouseDao.deductQuantity(warehousePO);
        } );

        warehouseOutputSheetDao.save(toSave);
        warehouseOutputSheetDao.saveBatch(warehouseOutputListPOSheetContent);
    }

    /**
     * 获取出库单新单号
     * @param id 上一次的出库单单号
     * @return 新的出库单单号
     */
    private String generateWarehouseOutputId(String id) { // "CKD-20220216-00000"
        return IdGenerator.generateSheetId(id, "CKD");
    }

    @Override
    @Transactional
    public List<WarehouseOneProductInfoVO> getWareProductInfo(GetWareProductInfoParamsVO params) {
        /**
         * 这是商品出库的前驱步骤 ——
         * 先查对应商品的批次, 把不同批次的商品按照入库的时间顺序取出
         * [比如有三批相同商品,分别进了100个,现在出库需要150个,那现在取第一批的100个和第二批的50个],
         * 然后返回给前端。
         * 前端可以继续选择不同的商品出库, 最终各种批次的各种商品组织成list作为出库单的参数
         */
        int quantity = params.getQuantity();
        List<WarehousePO> warehousePOS = warehouseDao.findAllNotZeroByPidSortedByBatchId(params.getPid());
        List<WarehouseOneProductInfoVO> res = new ArrayList<>();
        for (int i = 0; i < warehousePOS.size() && quantity > 0; i++) {
            WarehousePO warehousePO = warehousePOS.get(i);
            WarehouseOneProductInfoVO resItem = WarehouseOneProductInfoVO.builder()
                    .productId(warehousePO.getPid())
                    .batchId(warehousePO.getBatchId())
                    .purchasePrice(warehousePO.getPurchasePrice())
                    .totalQuantity(warehousePO.getQuantity())
                    .remark(params.getRemark())
                    .build();
            if (warehousePO.getQuantity() <= quantity) {
                resItem.setSelectedQuantity(warehousePO.getQuantity());
                resItem.setSumPrice(warehousePO.getPurchasePrice().multiply(BigDecimal.valueOf(warehousePO.getQuantity())));
                quantity -= warehousePO.getQuantity();
            } else {
                resItem.setSelectedQuantity(quantity);
                resItem.setSumPrice(warehousePO.getPurchasePrice().multiply(BigDecimal.valueOf(quantity)));
                quantity = 0;
            }
            res.add(resItem);
        }
        return res;
    }

    /**
     * 审批入库单(仓库管理员进行确认/总经理进行审批)
     *
     * @param warehouseInputSheetId 入库单id
     * @param state                 入库单修改后的状态(state == "待审批"/"审批失败"/"审批完成")
     */
    @Override
    @Transactional
    public void approvalInputSheet(UserVO user, String warehouseInputSheetId, WarehouseInputSheetState state) {
        // TODO
        // 也许要加一个修改草稿的接口 此处只是审批通过并修改操作员
        WarehouseInputSheetPO warehouseInputSheetPO = warehouseInputSheetDao.getSheet(warehouseInputSheetId);
        warehouseInputSheetPO.setOperator(user.getName());
        warehouseInputSheetPO.setState(state);
        warehouseInputSheetDao.updateById(warehouseInputSheetPO);
        // 获取对应的商品 更新仓库相关数据
        List<WarehouseInputSheetContentPO> productsList = warehouseInputSheetDao.getAllContentById(warehouseInputSheetId);
        List<WarehousePO> warehousePOList = new ArrayList<>();
        for (WarehouseInputSheetContentPO product : productsList) {
            ProductPO productPO = productDao.findById(product.getPid());
            // 更新最新数量
            productPO.setQuantity(productPO.getQuantity() + product.getQuantity());
            productDao.updateById(productPO);
            // 更新库存信息
            WarehousePO warehousePO = WarehousePO.builder()
                    .pid(product.getPid())
                    .quantity(product.getQuantity())
                    .purchasePrice(product.getPurchasePrice())
                    .batchId(warehouseInputSheetPO.getBatchId())
                    .productionDate(product.getProductionDate()).build();
            warehousePOList.add(warehousePO);
        }
        warehouseDao.saveBatch(warehousePOList);
    }

    /**
     * 通过状态获取入库单(state == null 时获取全部入库单)
     *
     * @param state 入库单状态
     * @return 入库单
     */
    @Override
    public List<WarehouseInputSheetPO> getWareHouseInputSheetByState(WarehouseInputSheetState state) {
        if (state == null) {
            return warehouseInputSheetDao.getAllSheets();
        }
        else {
            return warehouseInputSheetDao.getDraftSheets(state);
        }
    }

    @Override
    public List<WarehouseOutputSheetPO> getWareHouseOutSheetByState(WarehouseOutputSheetState state) {
        if (state == null) {
            return warehouseOutputSheetDao.getAllSheets();
        }
        else {
            return warehouseOutputSheetDao.getDraftSheets(state);
        }
    }

    @Override
    public WarehouseOutputSheetPO getWarehouseOutSheetBySaleSheetId(String saleSheetId) {
        return warehouseOutputSheetDao.getWarehouseOutSheetBySaleSheetId(saleSheetId);
    }

    /**
     * 确认出库单
     * @param user
     * @param sheetId 入库单id
     * @param state 入库单修改后的状态(state == "审批失败"/"审批完成")
     */
    @Override
    @Transactional
    public void approvalOutputSheet(UserVO user, String sheetId, WarehouseOutputSheetState state) {
        WarehouseOutputSheetPO warehouseOutputSheetPO = warehouseOutputSheetDao.getSheet(sheetId);
        warehouseOutputSheetPO.setOperator(user.getName());
        warehouseOutputSheetPO.setState(state);
        warehouseOutputSheetDao.updateById(warehouseOutputSheetPO);
        // 获取对应的商品 更新仓库相关数据
        List<WarehouseOutputSheetContentPO> productsList = warehouseOutputSheetDao.getAllContentById(sheetId);
        // 删除原有的不含批次的content
        warehouseOutputSheetDao.deleteContent(sheetId);
        // 分配后的出库单
        List<WarehouseOutputSheetContentPO> ans = new ArrayList<>();

        for (WarehouseOutputSheetContentPO product : productsList) {
            ProductPO productPO = productDao.findById(product.getPid());
            // 更新最新数量
            productPO.setQuantity(productPO.getQuantity() - product.getQuantity());
            productDao.updateById(productPO);
            // 更新库存信息
            // ?如何出货
            // 查询获取同一商品的不同批次信息 按进价排序
            int remainAmount = product.getQuantity();
            List<WarehousePO> availableWarehouses = warehouseDao.findByPidOrderByPurchasePricePos(product.getPid());
            for (WarehousePO availableWarehouse : availableWarehouses) {
                WarehouseOutputSheetContentPO warehouseOutputSheetContentPO = new WarehouseOutputSheetContentPO();
                BeanUtils.copyProperties(product, warehouseOutputSheetContentPO);
                if (availableWarehouse.getQuantity() >= remainAmount) {
                    availableWarehouse.setQuantity(remainAmount);
                    warehouseDao.deductQuantity(availableWarehouse);
                    warehouseOutputSheetContentPO.setBatchId(availableWarehouse.getBatchId());
                    ans.add(warehouseOutputSheetContentPO);
                    break;
                }
                else {
                    remainAmount = remainAmount - availableWarehouse.getQuantity();
                    warehouseDao.deductQuantity(availableWarehouse);
                    warehouseOutputSheetContentPO.setBatchId(availableWarehouse.getBatchId());
                    ans.add(warehouseOutputSheetContentPO);
                }
            }
        }
        warehouseOutputSheetDao.saveBatch(ans);
    }

    @Override
    public List<WarehouseInputSheetContentPO> getWHISheetContentById(String sheetId) {
        return warehouseInputSheetDao.getAllContentById(sheetId);
    }

    @Override
    public List<WarehouseOutputSheetContentPO> getWHOSheetContentById(String sheetId) {
        return warehouseOutputSheetDao.getAllContentById(sheetId);
    }


    /**
     * 获取新批次
     * @param batchId 批次
     * @return 新批次
     */
    private Integer generateBatchId(Integer batchId) {
        return batchId + 1;
    }

    /**
     * 获取新入库单单号
     * @param id 入库单单号
     * @return 新入库单单号
     */
    private String generateWarehouseInputId(String id, Integer batchId) { // "RKD-20220216-00000"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormat.format(new Date());
        if(batchId == -1) {
            return "RKD-" + today + "-" + String.format("%05d", 0);
        }
        String lastDate = id.split("-")[1];
        if(lastDate.equals(today)) {
            return "RKD-" + today + "-" + String.format("%05d", batchId + 1);
        } else {
            return "RKD-" + today + "-" + String.format("%05d", 0);
        }
    }

    /**
     * 库存查看：设定一个时间段，查看此时间段内的出/入库数量/金额/商品信息/分类信息
     * @param beginDateStr 开始时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @param endDateStr 结束时间字符串  格式为："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    @Override
    public List<WarehouseIODetailPO> getWarehouseIODetailByTime(String beginDateStr, String endDateStr) {
        // TODO
        /**
         * 1.注意日期的格式转换和转换异常
         * 2.考虑开始时间大于结束时间的情况、查询结果为空的情况
         * 3.Dao层和service层接口已实现
         *
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                List<WarehouseIODetailPO> input_list = warehouseInputSheetDao.getWarehouseIODetailByTime(beginTime, endTime);
                List<WarehouseIODetailPO> output_list = warehouseOutputSheetDao.getWarehouseIODetailByTime(beginTime, endTime);
                List<WarehouseIODetailPO> res = new ArrayList<>();
                res.addAll(input_list);
                res.addAll(output_list);
                return res;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 库存查看：一个时间段内的入库数量合计
     * @param beginDateStr 开始时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @param endDateStr 结束时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public int getWarehouseInputProductQuantityByTime(String beginDateStr,String endDateStr){
        // TODO
        /**
         * 1.注意日期的格式转换和转换异常
         * 2.考虑开始时间大于结束时间的情况、查询结果为空的情况
         * 3.Dao层和service层接口已实现，方法对应的Mapper为WarehouseInputSheetMapper
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return 0;
            }else{
                Integer res = warehouseInputSheetDao.getWarehouseInputProductQuantityByTime(beginTime, endTime);
                if (res == null) {
                    return 0;
                } else {
                    return res;
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 库存查看：一个时间段内的出库数量合计
     * @param beginDateStr 开始时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @param endDateStr 结束时间字符串 格式为："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public int getWarehouseOutProductQuantityByTime(String beginDateStr,String endDateStr){
        // TODO
        /**
         * 1.注意日期的格式转换和转换异常
         * 2.考虑开始时间大于结束时间的情况、查询结果为空的情况
         * 3.Dao层和service层接口已提供，需要先补充WarehouseInputSheetMapper中的sql语句
         */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return 0;
            }else{
                Integer res = warehouseOutputSheetDao.getWarehouseOutputProductQuantityByTime(beginTime, endTime);
                if (res == null) {
                    return 0;
                } else {
                    return res;
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 库存盘点
     * 盘点的是当天的库存快照，包括当天的各种商品的
     * 名称，型号，库存数量，库存均价，批次，批号，出厂日期，并且显示行号。
     * 要求可以导出Excel
     */
    @Override
    public List<WarehouseCountingVO> warehouseCounting() {
        List<WarehousePO> all = warehouseDao.findAll();
        List<WarehouseCountingVO> res = new ArrayList<>();
        for(WarehousePO warehousePO : all) {
            WarehouseCountingVO vo = new WarehouseCountingVO();
            BeanUtils.copyProperties(warehousePO, vo);
            String pid = warehousePO.getPid();
            ProductInfoVO product = productService.getOneProductByPid(pid);
            vo.setProduct(product);
            res.add(vo);
        }
        return res;
    }

    /**
     * 库存盘点导出为excel
     * 包括当天的各种商品的名称，型号，库存数量，库存均价，批次，批号，出厂日期，并且显示行号
     */
    @Override
    public void exportExcel(HttpServletResponse response) {
        List<WarehouseCountingVO> resultList = warehouseCounting();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("库存盘点");
        int rowNo = 0;

        HSSFRow titleRow = sheet.createRow(rowNo++);

        //编写每列标题
        for (int i = 0; i < 6; i++) {
            HSSFCell cell = titleRow.createCell(i);
            switch (i) {
                case 0:
                    cell.setCellValue("行号");
                    break;
                case 1:
                    cell.setCellValue("商品Id");
                    break;
                case 2:
                    cell.setCellValue("数量");
                    break;
                case 3:
                    cell.setCellValue("批次");
                    break;
                case 4:
                    cell.setCellValue("价格");
                    break;
                case 5:
                    cell.setCellValue("日期");
                    break;
                default:
                    break;
            }
        }

        for (WarehouseCountingVO countingVO : resultList){
            HSSFRow row = sheet.createRow(rowNo++);
            for (int i = 0; i < 6; i++) {
                HSSFCell cell = row.createCell(i);
                switch (i) {
                    case 0:
                        cell.setCellValue(i + 1);
                        break;
                    case 1:
                        cell.setCellValue(countingVO.getProduct().getId());
                        break;
                    case 2:
                        cell.setCellValue(countingVO.getQuantity());
                        break;
                    case 3:
                        cell.setCellValue(countingVO.getBatchId());
                        break;
                    case 4:
                        cell.setCellValue(countingVO.getPurchasePrice().intValue());
                        break;
                    case 5:
                        cell.setCellValue(countingVO.getProductionDate());
                        break;
                    default:
                        break;
                }
            }
        }

        try{
            String fileName = new String("WarehouseCounting.xls".getBytes(), StandardCharsets.UTF_8);
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void makeGiftSheet(WarehouseGiftSheetVO warehouseGiftSheetVO) {
        WarehouseGiftSheetPO warehouseGiftSheetPO = new WarehouseGiftSheetPO();
        BeanUtils.copyProperties(warehouseGiftSheetVO, warehouseGiftSheetPO);
        //填写属性
        warehouseGiftSheetPO.setCreateTime(new Date());
        WarehouseGiftSheetPO latest = warehouseGiftSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "KCZSD");
        warehouseGiftSheetPO.setId(id);
        warehouseGiftSheetPO.setState(WarehouseGiftSheetState.PENDING);
        //遍历content
        List<WarehouseGiftSheetContentPO> wContentPOList = new ArrayList<>();
        for (WarehouseGiftSheetContentVO content : warehouseGiftSheetVO.getWarehouseGiftSheetContent()) {
            WarehouseGiftSheetContentPO wContentPO = new WarehouseGiftSheetContentPO();
            BeanUtils.copyProperties(content, wContentPO);
            wContentPO.setWarehouseGiftSheetId(id);
            wContentPOList.add(wContentPO);
        }
        warehouseGiftSheetDao.saveBatchSheetContent(wContentPOList);
        warehouseGiftSheetDao.saveSheet(warehouseGiftSheetPO);
    }

    @Override
    public void approvalGiftSheet(String warehouseGiftSheetId, WarehouseGiftSheetState state) {
        if (state.equals(WarehouseGiftSheetState.FAILURE)) {
            WarehouseGiftSheetPO warehouseGiftSheetPO = warehouseGiftSheetDao.findSheetById(warehouseGiftSheetId);
            if (warehouseGiftSheetPO.getState() == WarehouseGiftSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = warehouseGiftSheetDao.updateSheetState(warehouseGiftSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            if (!state.equals(WarehouseGiftSheetState.SUCCESS)) {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = warehouseGiftSheetDao.updateSheetState(warehouseGiftSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            //更改库存
            List<WarehouseGiftSheetContentPO> contentPOList = warehouseGiftSheetDao.findContentBySheetId(warehouseGiftSheetId);
            for (WarehouseGiftSheetContentPO contentPO : contentPOList) {
                // 更新最新数量
                ProductPO productPO = productDao.findById(contentPO.getPid());
                productPO.setQuantity(productPO.getQuantity() - contentPO.getQuantity());
                productDao.updateById(productPO);
                // 更新库存信息
                int remainAmount = contentPO.getQuantity();
                List<WarehousePO> availableWarehouses = warehouseDao.findByPidOrderByPurchasePricePos(productPO.getId());
                for (WarehousePO availableWarehouse : availableWarehouses) {
                    if (availableWarehouse.getQuantity() >= remainAmount) {
                        availableWarehouse.setQuantity(remainAmount);
                        warehouseDao.deductQuantity(availableWarehouse);
                        break;
                    }
                    else {
                        remainAmount = remainAmount - availableWarehouse.getQuantity();
                        warehouseDao.deductQuantity(availableWarehouse);
                    }
                }
            }
        }
    }

    @Override
    public List<WarehouseGiftSheetVO> getGiftSheetByState(WarehouseGiftSheetState state) {
        List<WarehouseGiftSheetVO> res = new ArrayList<>();
        List<WarehouseGiftSheetPO> all;
        if (state == null) {
            all = warehouseGiftSheetDao.findAllSheet();
        } else {
            all = warehouseGiftSheetDao.findAllByState(state);
        }
        for (WarehouseGiftSheetPO po : all) {
            WarehouseGiftSheetVO vo = new WarehouseGiftSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<WarehouseGiftSheetContentPO> alll = warehouseGiftSheetDao.findContentBySheetId(po.getId());
            List<WarehouseGiftSheetContentVO> vos = new ArrayList<>();
            for (WarehouseGiftSheetContentPO p : alll) {
                WarehouseGiftSheetContentVO v = new WarehouseGiftSheetContentVO();
                BeanUtils.copyProperties(p, v);
                ProductInfoVO product = productService.getOneProductByPid(p.getPid());
                v.setName(product.getName());
                vos.add(v);
            }
            vo.setWarehouseGiftSheetContent(vos);
            res.add(vo);
        }
        return res;
    }
}

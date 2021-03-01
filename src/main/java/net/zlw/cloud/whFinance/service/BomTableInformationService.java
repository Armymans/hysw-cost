package net.zlw.cloud.whFinance.service;


import net.zlw.cloud.excel.dao.BomTable1Dao;
import net.zlw.cloud.excel.dao.BomTableInfomationDao;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;
import net.zlw.cloud.whFinance.domain.BomTableInfomationAll;
import net.zlw.cloud.whFinance.domain.vo.BomTableVo;
import net.zlw.cloud.whFinance.domain.vo.BomTableVo2;
import net.zlw.cloud.whFinance.domain.vo.BomTablesVo;
import net.zlw.cloud.whFinance.mapper.BomTableImfomationAllDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BomTableInformationService {

    @Autowired
    private BomTable1Dao bomTableMapper;

    @Autowired
    private BomTableImfomationAllDao bomTableImfomationAllDao;

    @Resource
    private BomTableInfomationDao bomTableImfomationMapper;

    public void getBomTable(BomTablesVo bomTablesVo, String account){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        List<BomTableVo> bomTableInfomations = bomTablesVo.getBomTableInfomation();
        for (BomTableVo bomTableVo : bomTableInfomations) {
            if (bomTableVo != null){

                if (StringUtils.isEmpty(bomTableVo.getStatus())){
                    bomTableVo.setStatus("0");
                }

                BomTableInfomation bomTableInfomation1 = new BomTableInfomation();

                Example e = new Example(BomTableInfomation.class);
                e.createCriteria().andEqualTo("businessCode", bomTableVo.getBusiness_code());
                BomTableInfomation bomTableInfomation = bomTableImfomationMapper.selectOneByExample(e);
                if (bomTableInfomation != null){
                    bomTableInfomation1.setId(bomTableInfomation.getId());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomation1.setDateOf(bomTableVo.getDate_of());
                    bomTableInfomation1.setSalesOrganization(bomTableVo.getSales_organization());
                    bomTableInfomation1.setInventoryOrganization(bomTableVo.getInventory_organization());
                    bomTableInfomation1.setCeaNum(bomTableVo.getCea_num());
                    bomTableInfomation1.setAcquisitionTypes(bomTableVo.getAcquisition_types());
                    bomTableInfomation1.setContractor(bomTableVo.getContractor());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomation1.setProjectTypes(bomTableVo.getProject_types());
                    bomTableInfomation1.setItemCoding(bomTableVo.getItem_coding());
                    bomTableInfomation1.setProjectName(bomTableVo.getProject_name());
                    bomTableInfomation1.setAcquisitionDepartment(bomTableVo.getAcquisition_department());
                    bomTableInfomation1.setBusinessProcess(bomTableVo.getBusiness_process());
                    bomTableInfomation1.setBusinessCode(bomTableVo.getBusiness_code());
                    bomTableInfomation1.setDelFlag(bomTableVo.getStatus());
                    bomTableInfomation1.setCreateTime(date);
                    bomTableInfomation1.setUpdateTime(date);
                    bomTableImfomationMapper.updateByExampleSelective(bomTableInfomation1, e);
                } else {
                    String id = UUID.randomUUID().toString().replace("-", "");
                    //物料基本信息表
                    bomTableInfomation1.setId(id);
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomation1.setDateOf(bomTableVo.getDate_of());
                    bomTableInfomation1.setSalesOrganization(bomTableVo.getSales_organization());
                    bomTableInfomation1.setInventoryOrganization(bomTableVo.getInventory_organization());
                    bomTableInfomation1.setCeaNum(bomTableVo.getCea_num());
                    bomTableInfomation1.setAcquisitionTypes(bomTableVo.getAcquisition_types());
                    bomTableInfomation1.setContractor(bomTableVo.getContractor());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomation1.setProjectTypes(bomTableVo.getProject_types());
                    bomTableInfomation1.setItemCoding(bomTableVo.getItem_coding());
                    bomTableInfomation1.setProjectName(bomTableVo.getProject_name());
                    bomTableInfomation1.setAcquisitionDepartment(bomTableVo.getAcquisition_department());
                    bomTableInfomation1.setBusinessProcess(bomTableVo.getBusiness_process());
                    bomTableInfomation1.setBusinessCode(bomTableVo.getBusiness_code());
                    bomTableInfomation1.setDelFlag(bomTableVo.getStatus());
                    bomTableInfomation1.setCreateTime(date);
                    bomTableInfomation1.setUpdateTime(date);
                    bomTableImfomationMapper.insertSelective(bomTableInfomation1);

                    //全部物料基本信息表
                    BomTableInfomationAll bomTableInfomationAll = new BomTableInfomationAll();
                    bomTableInfomationAll.setId(id);
                    bomTableInfomationAll.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomationAll.setDateOf(bomTableVo.getDate_of());
                    bomTableInfomationAll.setSalesOrganization(bomTableVo.getSales_organization());
                    bomTableInfomationAll.setInventoryOrganization(bomTableVo.getInventory_organization());
                    bomTableInfomationAll.setCeaNum(bomTableVo.getCea_num());
                    bomTableInfomationAll.setAcquisitionTypes(bomTableVo.getAcquisition_types());
                    bomTableInfomationAll.setContractor(bomTableVo.getContractor());
                    bomTableInfomationAll.setProjectCategoriesCoding(bomTableVo.getProject_categories_coding());
                    bomTableInfomationAll.setProjectTypes(bomTableVo.getProject_types());
                    bomTableInfomationAll.setItemCoding(bomTableVo.getItem_coding());
                    bomTableInfomationAll.setProjectName(bomTableVo.getProject_name());
                    bomTableInfomationAll.setAcquisitionDepartment(bomTableVo.getAcquisition_department());
                    bomTableInfomationAll.setBusinessProcess(bomTableVo.getBusiness_process());
                    bomTableInfomationAll.setBusinessCode(bomTableVo.getBusiness_code());
                    bomTableInfomationAll.setDelFlag(bomTableVo.getStatus());
                    bomTableInfomationAll.setCreateTime(date);
                    bomTableInfomationAll.setUpdateTime(date);
                    bomTableImfomationAllDao.insertSelective(bomTableInfomationAll);
                }

                //物料信息
                List<BomTableVo2> bomTables = bomTableVo.getBomTable();
                BomTable bomTable = new BomTable();
                for (BomTableVo2 thisBomTable : bomTables) {
                    if (thisBomTable.getId() != null){
                        Example example = new Example(BomTable.class);
                        example.createCriteria().andEqualTo("bomTableInfomationId", thisBomTable.getBomTableInfomationId());
                        List<BomTable> bomTables1 = bomTableMapper.selectByExample(example);
                        // 如果数据库有该订单，则判断是否存在该物料
                        if (bomTables1.size() > 0){
                            BomTable bomTable1 = bomTableMapper.selectByPrimaryKey(thisBomTable.getId());
                            if (bomTable1 != null){  // 存在进行修改
                                bomTable.setId(thisBomTable.getId());
                                bomTable.setMaterialCode(thisBomTable.getMaterial_code());
                                bomTable.setItemName(thisBomTable.getItem_name());
                                bomTable.setSpecificationsModels(thisBomTable.getSpecifications_models());
                                bomTable.setUnit(thisBomTable.getUnit());
                                bomTable.setUnivalence(thisBomTable.getUnivalence());
                                bomTable.setQuantity(thisBomTable.getQuantity());
                                bomTable.setCombinedPrice(thisBomTable.getCombined_price());
                                bomTable.setRemark(thisBomTable.getRemark());
                                bomTable.setDelFlag("0");
                                bomTable.setUpdateTime(date);
                                bomTableMapper.updateByPrimaryKeySelective(bomTable);
                            } else { // 不存在进行插入
                                if (StringUtils.isEmpty(thisBomTable.getId())){
                                    bomTable.setId(UUID.randomUUID().toString().replace("-", ""));
                                }
                                bomTable.setId(thisBomTable.getId());
                                bomTable.setMaterialCode(thisBomTable.getMaterial_code());
                                bomTable.setItemName(thisBomTable.getItem_name());
                                bomTable.setSpecificationsModels(thisBomTable.getSpecifications_models());
                                bomTable.setUnit(thisBomTable.getUnit());
                                bomTable.setUnivalence(thisBomTable.getUnivalence());
                                bomTable.setQuantity(thisBomTable.getQuantity());
                                bomTable.setCombinedPrice(thisBomTable.getCombined_price());
                                bomTable.setBomTableInfomationId(bomTableInfomation1.getBusinessCode());
                                bomTable.setRemark(thisBomTable.getRemark());
                                bomTable.setDelFlag("0");
                                bomTable.setCreateTime(date);
                                bomTable.setUpdateTime(date);
                                bomTableMapper.insertSelective(bomTable);
                            }
                        } else {    // 没有直接插入物料清单
                            BomTable bomTable1 = bomTableMapper.selectByPrimaryKey(thisBomTable.getId());
                            if (bomTable1 != null || StringUtils.isEmpty(thisBomTable.getId())){
                                bomTable.setId(UUID.randomUUID().toString().replace("-", ""));
                            }
                            bomTable.setId(thisBomTable.getId());
                            bomTable.setMaterialCode(thisBomTable.getMaterial_code());
                            bomTable.setItemName(thisBomTable.getItem_name());
                            bomTable.setSpecificationsModels(thisBomTable.getSpecifications_models());
                            bomTable.setUnit(thisBomTable.getUnit());
                            bomTable.setUnivalence(thisBomTable.getUnivalence());
                            bomTable.setQuantity(thisBomTable.getQuantity());
                            bomTable.setCombinedPrice(thisBomTable.getCombined_price());
                            bomTable.setBomTableInfomationId(bomTableInfomation1.getBusinessCode());
                            bomTable.setRemark(thisBomTable.getRemark());
                            bomTable.setDelFlag("0");
                            bomTable.setCreateTime(date);
                            bomTable.setUpdateTime(date);
                            bomTableMapper.insertSelective(bomTable);
                        }
                    }
                }
            }
        }
    }
}

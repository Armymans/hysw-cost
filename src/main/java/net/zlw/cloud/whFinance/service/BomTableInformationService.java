package net.zlw.cloud.whFinance.service;


import net.zlw.cloud.excel.dao.BomTable1Dao;
import net.zlw.cloud.excel.dao.BomTableInfomation1Dao;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;
import net.zlw.cloud.whFinance.dao.BomTableImfomationAllDao;

import net.zlw.cloud.whFinance.domain.BomTableInfomationAll;
import net.zlw.cloud.whFinance.domain.vo.BomTableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BomTableInformationService {

    @Autowired
    private BomTable1Dao bomTableMapper;

    @Autowired
    private BomTableImfomationAllDao bomTableImfomationAllDao;

    @Resource
    private BomTableInfomation1Dao bomTableImfomationMapper;

    public void getBomTable(BomTableVo bomTableVo,String account){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (bomTableVo != null){
            BomTableInfomation bomTableInfomation1 = new BomTableInfomation();
            if (bomTableVo.getId() != null){
                //物料基本信息表
                bomTableInfomation1.setId(bomTableVo.getId());
                bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProjectId());
                bomTableInfomation1.setDateOf(bomTableVo.getDateOf());
                bomTableInfomation1.setSalesOrganization(bomTableVo.getSalesOrganization());
                bomTableInfomation1.setInventoryOrganization(bomTableVo.getInventoryOrganization());
                bomTableInfomation1.setCeaNum(bomTableVo.getCeaNum());
                bomTableInfomation1.setAcquisitionTypes(bomTableVo.getAcquisitionTypes());
                bomTableInfomation1.setContractor(bomTableVo.getContractor());
                bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProjectCategoriesCoding());
                bomTableInfomation1.setProjectTypes(bomTableVo.getProjectTypes());
                bomTableInfomation1.setItemCoding(bomTableVo.getItemCoding());
                bomTableInfomation1.setProjectName(bomTableVo.getProjectName());
                bomTableInfomation1.setAcquisitionDepartment(bomTableVo.getAcquisitionDepartment());
                bomTableInfomation1.setDelFlag("0");
                bomTableInfomation1.setCreateTime(date);
                bomTableInfomation1.setUpdateTime(date);
                bomTableImfomationMapper.insertSelective(bomTableInfomation1);

                //全部物料基本信息表
                BomTableInfomationAll bomTableInfomationAll = new BomTableInfomationAll();
                bomTableInfomationAll.setId(bomTableVo.getId());
                bomTableInfomationAll.setProjectCategoriesCoding(bomTableVo.getProjectId());
                bomTableInfomationAll.setDateOf(bomTableVo.getDateOf());
                bomTableInfomationAll.setSalesOrganization(bomTableVo.getSalesOrganization());
                bomTableInfomationAll.setInventoryOrganization(bomTableVo.getInventoryOrganization());
                bomTableInfomationAll.setCeaNum(bomTableVo.getCeaNum());
                bomTableInfomationAll.setAcquisitionTypes(bomTableVo.getAcquisitionTypes());
                bomTableInfomationAll.setContractor(bomTableVo.getContractor());
                bomTableInfomationAll.setProjectCategoriesCoding(bomTableVo.getProjectCategoriesCoding());
                bomTableInfomationAll.setProjectTypes(bomTableVo.getProjectTypes());
                bomTableInfomationAll.setItemCoding(bomTableVo.getItemCoding());
                bomTableInfomationAll.setProjectName(bomTableVo.getProjectName());
                bomTableInfomationAll.setAcquisitionDepartment(bomTableVo.getAcquisitionDepartment());
                bomTableInfomationAll.setDelFlag("0");
                bomTableInfomationAll.setCreateTime(date);
                bomTableInfomationAll.setUpdateTime(date);
                bomTableImfomationAllDao.insertSelective(bomTableInfomationAll);

            }
            //物料基本信息表所有的外键
            List<BomTableInfomation> id = bomTableImfomationMapper.findId();
            //判断外键是否重复重复则更新
            for (BomTableInfomation thisId : id) {
                if (bomTableVo.getProjectId().equals(thisId.getProjectCategoriesCoding())){
                    bomTableInfomation1.setId(bomTableVo.getId());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProjectId());
                    bomTableInfomation1.setDateOf(bomTableVo.getDateOf());
                    bomTableInfomation1.setSalesOrganization(bomTableVo.getSalesOrganization());
                    bomTableInfomation1.setInventoryOrganization(bomTableVo.getInventoryOrganization());
                    bomTableInfomation1.setCeaNum(bomTableVo.getCeaNum());
                    bomTableInfomation1.setAcquisitionTypes(bomTableVo.getAcquisitionTypes());
                    bomTableInfomation1.setContractor(bomTableVo.getContractor());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableVo.getProjectCategoriesCoding());
                    bomTableInfomation1.setProjectTypes(bomTableVo.getProjectTypes());
                    bomTableInfomation1.setItemCoding(bomTableVo.getItemCoding());
                    bomTableInfomation1.setProjectName(bomTableVo.getProjectName());
                    bomTableInfomation1.setAcquisitionDepartment(bomTableVo.getAcquisitionDepartment());
                    bomTableInfomation1.setDelFlag("0");
                    bomTableInfomation1.setCreateTime(date);
                    bomTableInfomation1.setUpdateTime(date);
                    bomTableImfomationMapper.updateByPrimaryKeySelective(bomTableInfomation1);
                }
            }

            //物料信息
            List<BomTable> bomTables = bomTableVo.getBomTables();
            BomTable bomTable = new BomTable();
            for (BomTable thisBomTable : bomTables) {
                if (thisBomTable.getId() != null){
                    bomTable.setId(thisBomTable.getId());
                    bomTable.setMaterialCode(thisBomTable.getMaterialCode());
                    bomTable.setItemName(thisBomTable.getItemName());
                    bomTable.setSpecificationsModels(thisBomTable.getSpecificationsModels());
                    bomTable.setUnit(thisBomTable.getUnit());
                    bomTable.setUnivalence(thisBomTable.getUnivalence());
                    bomTable.setQuantity(thisBomTable.getQuantity());
                    bomTable.setCombinedPrice(thisBomTable.getCombinedPrice());
                    bomTable.setRemark(thisBomTable.getRemark());
                    bomTable.setDelFlag("0");
                    bomTable.setCreateTime(date);
                    bomTable.setUpdateTime(date);
                }
                bomTableMapper.insertSelective(bomTable);
            }
        }
    }
}

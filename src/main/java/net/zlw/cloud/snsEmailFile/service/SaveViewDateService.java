package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.excel.dao.BomTable1Dao;
import net.zlw.cloud.excel.dao.BomTableInfomationDao;
import net.zlw.cloud.excel.model.BomTable;
import net.zlw.cloud.excel.model.BomTableInfomation;
import net.zlw.cloud.whFinance.domain.BomTableInfomationAll;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.mapper.BomTableImfomationAllDao;
import net.zlw.cloud.whFinance.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class SaveViewDateService {

    @Resource
    private MaterialMapper materialMapper;

    @Autowired
    private BomTable1Dao bomTableMapper;

    @Autowired
    private BomTableImfomationAllDao bomTableImfomationAllDao;

    @Resource
    private BomTableInfomationDao bomTableInfomationMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void saveMaterialInfo(List<Materie> materieList){
        //物料信息
        Materie materie = new Materie();
        for (Materie thisMaterie : materieList) {
            if (thisMaterie.getMaterialCode() != null){
                Materie materie1 = materialMapper.findByCode(thisMaterie.getMaterialCode());
                if(materie1 != null){
                    materie1.setDelFlag(thisMaterie.getDelFlag());
                    materie1.setUpdateTime(simpleDateFormat.format(new Date()));
                    materie1.setItemName(thisMaterie.getItemName());
                    materie1.setRemark(thisMaterie.getRemark());
                    materie1.setMaterialCode(thisMaterie.getMaterialCode());
                    materie1.setSpecificationsModels(thisMaterie.getSpecificationsModels());
                    materie1.setUnit(thisMaterie.getUnit());
                    materialMapper.updateByPrimaryKeySelective(materie1);
                }else {
                    materie.setId(thisMaterie.getId());
                    materie.setArea("1");
                    materie.setCreateTime(simpleDateFormat.format(new Date()));
                    materie.setUpdateTime(simpleDateFormat.format(new Date()));
                    materie.setItemName(thisMaterie.getItemName());
                    materie.setRemark(thisMaterie.getRemark());
                    materie.setMaterialCode(thisMaterie.getMaterialCode());
                    materie.setSpecificationsModels(thisMaterie.getSpecificationsModels());
                    materie.setUnit(thisMaterie.getUnit());
                    materie.setDelFlag(thisMaterie.getDelFlag());
                    materialMapper.insertSelective(materie);
                }
            }
        }
    }

    public void saveBomInfo(List<BomTableInfomation> bomTableInfomationList){
        for (BomTableInfomation bomTableInfomation : bomTableInfomationList) {
            if (bomTableInfomation != null){
                Example example = new Example(BomTableInfomation.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo(bomTableInfomation.getBusinessCode());
                List<BomTableInfomation> bomTableInfomationList1 = bomTableInfomationMapper.selectByExample(criteria);
                BomTableInfomation oldBomTableInfomation = bomTableInfomationList1.get(0);
                BomTableInfomation bomTableInfomation1 = new BomTableInfomation();
                if (oldBomTableInfomation != null){
                    //判断是否重复重复则更新
                    bomTableInfomation1.setId(oldBomTableInfomation.getId());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableInfomation.getProjectCategoriesCoding());
                    bomTableInfomation1.setDateOf(bomTableInfomation.getDateOf());
                    bomTableInfomation1.setSalesOrganization(bomTableInfomation.getSalesOrganization());
                    bomTableInfomation1.setInventoryOrganization(bomTableInfomation.getInventoryOrganization());
                    bomTableInfomation1.setCeaNum(bomTableInfomation.getCeaNum());
                    bomTableInfomation1.setAcquisitionTypes(bomTableInfomation.getAcquisitionTypes());
                    bomTableInfomation1.setContractor(bomTableInfomation.getContractor());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableInfomation.getProjectCategoriesCoding());
                    bomTableInfomation1.setProjectTypes(bomTableInfomation.getProjectTypes());
                    bomTableInfomation1.setItemCoding(bomTableInfomation.getItemCoding());
                    bomTableInfomation1.setProjectName(bomTableInfomation.getProjectName());
                    bomTableInfomation1.setAcquisitionDepartment(bomTableInfomation.getAcquisitionDepartment());
                    bomTableInfomation1.setDelFlag("0");
                    bomTableInfomation1.setUpdateTime(simpleDateFormat.format(new Date()));
                    bomTableInfomationMapper.updateByPrimaryKeySelective(bomTableInfomation1);
                } else {
                    //物料基本信息表
                    String id = UUID.randomUUID().toString().replace("-", "");
                    bomTableInfomation1.setId(id);
                    bomTableInfomation1.setBusinessCode(bomTableInfomation.getBusinessCode());
                    bomTableInfomation1.setBusinessProcess(bomTableInfomation.getBusinessProcess());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableInfomation.getProjectCategoriesCoding());
                    bomTableInfomation1.setDateOf(bomTableInfomation.getDateOf());
                    bomTableInfomation1.setSalesOrganization(bomTableInfomation.getSalesOrganization());
                    bomTableInfomation1.setInventoryOrganization(bomTableInfomation.getInventoryOrganization());
                    bomTableInfomation1.setCeaNum(bomTableInfomation.getCeaNum());
                    bomTableInfomation1.setAcquisitionTypes(bomTableInfomation.getAcquisitionTypes());
                    bomTableInfomation1.setContractor(bomTableInfomation.getContractor());
                    bomTableInfomation1.setProjectCategoriesCoding(bomTableInfomation.getProjectCategoriesCoding());
                    bomTableInfomation1.setProjectTypes(bomTableInfomation.getProjectTypes());
                    bomTableInfomation1.setItemCoding(bomTableInfomation.getItemCoding());
                    bomTableInfomation1.setProjectName(bomTableInfomation.getProjectName());
                    bomTableInfomation1.setAcquisitionDepartment(bomTableInfomation.getAcquisitionDepartment());
                    bomTableInfomation1.setRemark(bomTableInfomation.getRemark());
                    bomTableInfomation1.setDelFlag("0");
                    bomTableInfomation1.setCreateTime(simpleDateFormat.format(new Date()));
                    bomTableInfomation1.setUpdateTime(simpleDateFormat.format(new Date()));
                    bomTableInfomation1.setFounderCompany(bomTableInfomation.getFounderCompany());
                    bomTableInfomationMapper.insertSelective(bomTableInfomation1);
                }

                //物料信息
                List<BomTable> bomTables = bomTableInfomation.getBomTableList();
                BomTable bomTable = new BomTable();
                for (BomTable thisBomTable : bomTables) {
                    BomTable oldBomTable = bomTableMapper.selectByPrimaryKey(thisBomTable);
//                    if (oldBomTable != null){
//                        thisBomTable.setId(UUID.randomUUID().toString().replace("-", ""));
//                    }
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
                        bomTable.setBomTableInfomationId(thisBomTable.getBomTableInfomationId());
                        bomTable.setFounderCompanyId(thisBomTable.getFounderCompanyId());
                        bomTable.setDelFlag("0");
                        bomTable.setCreateTime(simpleDateFormat.format(new Date()));
                        bomTable.setUpdateTime(simpleDateFormat.format(new Date()));
                    }
                    bomTableMapper.insertSelective(bomTable);
                }
            }
        }
    }

}

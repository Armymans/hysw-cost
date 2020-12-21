package net.zlw.cloud.budgetTask.service;

import net.zlw.cloud.budgetTask.domain.CostMeansList;
import net.zlw.cloud.budgetTask.domain.TotalMeansList;
import net.zlw.cloud.budgetTask.domain.vo.BudgetVo;
import net.zlw.cloud.budgetTask.domain.vo.BudgetVoF;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BudgetTaskService {


    @Autowired
    private AuditInfoDao auditInfoDao;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private BudgetingDao budgetingDao;

    @Autowired
    private CostPreparationDao costPreparationDao;

    @Autowired
    private VeryEstablishmentDao veryEstablishmentDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;


    public void getBudgetEngineering(BudgetVoF budgetVoF) {
        BudgetVo budgetVo = budgetVoF.getBudgetVo();

        //设置时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (budgetVo != null) {
//            //添加基本项目
//            BaseProject baseProject = new BaseProject();
//            if (budgetVo.getApplication_num() != null){
//                baseProject.setId(budgetVo.getBase_broject_id());
//                baseProject.setApplicationNum(budgetVo.getApplication_num());
//                baseProject.setDistrict(budgetVo.getDistinct());
//                baseProject.setCreateTime(format);
//                baseProject.setUpdateTime(format);
//                baseProject.setFounderId(account);
//                baseProject.setDelFlag("0");
//                baseProjectDao.insertSelective(baseProject);
//            }

            //添加预算信息
            Budgeting budgeting = new Budgeting();
            if (budgetVo.getAdded_tax_amount() != null) {
                //强转
                String amountCost = budgetVo.getAmount_cost();
                BigDecimal bigDecimal = new BigDecimal(amountCost);
                budgeting.setId(budgetVo.getId());
                budgeting.setAmountCost(bigDecimal);
                budgeting.setBudgetingPeople(budgetVo.getBudgeting_people());
                String addedTaxAmount = budgetVo.getAdded_tax_amount();
                BigDecimal decimal = new BigDecimal(addedTaxAmount);
                budgeting.setAddedTaxAmount(decimal);
                budgeting.setBudgetingTime(budgetVo.getBudgeting_time());
                budgeting.setBaseProjectId(budgetVo.getApplication_num());
                budgeting.setDelFlag("0");
                budgeting.setCreateTime(format);
                budgeting.setUpdateTime(format);
                budgetingDao.insertSelective(budgeting);
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetVo.getApplication_num());
                if(baseProject != null){
                    baseProject.setBudgetStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }
            }

            //成本表
            CostPreparation costPreparation = new CostPreparation();
            if (budgetVo.getTotal_cost_amount() != null) {
                costPreparation.setId(UUID.randomUUID().toString());
                costPreparation.setBaseProjectId(budgetVo.getBase_broject_id());
                costPreparation.setCostTogether(budgetVo.getCost_by());
                costPreparation.setCostPreparationTime(budgetVo.getCost_preparation_time());
                String totalCostAmount = budgetVo.getTotal_cost_amount();
                if(totalCostAmount!=null){
                    BigDecimal bigDecimal = new BigDecimal(totalCostAmount);
                    costPreparation.setCostTotalAmount(bigDecimal);
                }
                String totalVatAmount = budgetVo.getTotal_cost_amount();
                if(totalVatAmount!=null){
                    BigDecimal bigDecimal1 = new BigDecimal(totalVatAmount);
                    costPreparation.setVatAmount(bigDecimal1);
                }
                String sourcingCost = budgetVo.getOutsourcing_cost_amount();
                if(sourcingCost!=null){
                    BigDecimal bigDecimal2 = new BigDecimal(sourcingCost);
                    costPreparation.setOutsourcingCostAmount(bigDecimal2);
                }
                String sourcingCost1 = budgetVo.getSourcing_cost();
                if(sourcingCost1!=null) {
                    BigDecimal bigDecimal3 = new BigDecimal(sourcingCost1);
                    costPreparation.setTotalPackageMaterial(bigDecimal3);
                }
                String otherExpensesOne = budgetVo.getOther_expenses_one();
                if(otherExpensesOne!=null) {
                    BigDecimal bigDecimal4 = new BigDecimal(otherExpensesOne);
                    costPreparation.setOtherCost1(bigDecimal4);
                }
                String otherExpensesTwo = budgetVo.getOther_expenses_two();
                if(otherExpensesTwo!=null) {
                    BigDecimal bigDecimal5 = new BigDecimal(otherExpensesTwo);
                    costPreparation.setOtherCost2(bigDecimal5);
                }
                String otherExpensesThree = budgetVo.getOther_expenses_three();
                if(otherExpensesThree!=null) {
                    BigDecimal bigDecimal6 = new BigDecimal(otherExpensesThree);
                    costPreparation.setOtherCost3(bigDecimal6);
                }
                costPreparation.setDelFlag("0");
                costPreparationDao.insertSelective(costPreparation);
            }

            //预算编制附件资料
            List<CostMeansList> costMeansList = budgetVo.getCostMeansList();
            if(costMeansList != null && costMeansList.size() > 0){
                for (CostMeansList thisMean : costMeansList) {
                    //文件表
                    FileInfo fileInfo = new FileInfo();
                    if (thisMean.getCost_file_name() != null) {
                        fileInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                        fileInfo.setPlatCode(budgetVo.getId());
                        fileInfo.setName(thisMean.getCost_means_name());
                        fileInfo.setFileName(thisMean.getCost_file_name());
                        fileInfo.setFilePath(budgetVo.getApplication_num());
                        fileInfo.setCreateTime(format);
                        fileInfo.setUpdateTime(format);
                        fileInfo.setStatus("0");
                        fileInfo.setType("ysbzfjzl");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
            }


            //成本编制附件资料
            List<TotalMeansList> totalMeansList = budgetVo.getTotalMeansList();
            if(totalMeansList != null && totalMeansList.size() > 0){
                for (TotalMeansList thisTotal : totalMeansList) {
                    //文件表
                    FileInfo fileInfo = new FileInfo();
                    if (thisTotal.getTotal_file_name() != null) {
                        fileInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                        fileInfo.setPlatCode(budgetVo.getId());
                        fileInfo.setName(thisTotal.getTotal_means_name());
                        fileInfo.setFileName(thisTotal.getTotal_file_name());
                        fileInfo.setFilePath(budgetVo.getApplication_num());
                        fileInfo.setCreateTime(format);
                        fileInfo.setUpdateTime(format);
                        fileInfo.setStatus("0");
                        fileInfo.setType("cbbzfjzl");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
            }

        }
    }

}

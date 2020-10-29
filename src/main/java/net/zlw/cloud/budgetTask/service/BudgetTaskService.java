package net.zlw.cloud.budgetTask.service;

import net.zlw.cloud.budgetTask.domain.CostMeansList;
import net.zlw.cloud.budgetTask.domain.LabelMeansList;
import net.zlw.cloud.budgetTask.domain.TotalMeansList;
import net.zlw.cloud.budgetTask.domain.vo.BudgetVo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
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


    public void getBudgetEngineering(String account, BudgetVo budgetVo) {

        //设置时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if (budgetVo != null){
            //添加基本项目
            BaseProject baseProject = new BaseProject();
            if (budgetVo.getApplicationNum() != null){
                baseProject.setId(UUID.randomUUID().toString().replace("-",""));
                baseProject.setApplicationNum(budgetVo.getApplicationNum());
                baseProject.setDistrict(budgetVo.getDistinct());
                baseProject.setCreateTime(format);
                baseProject.setUpdateTime(format);
                baseProject.setFounderId(account);
                baseProject.setDelFlag("0");
            }
            baseProjectDao.insertSelective(baseProject);

            //添加预算信息
            Budgeting budgeting = new Budgeting();
            if (budgetVo.getId() !=null){
                //强转
                String amountCost = budgetVo.getAmountCost();
                BigDecimal bigDecimal = new BigDecimal(amountCost);
                budgeting.setId(budgetVo.getId());
                budgeting.setAmountCost(bigDecimal);
                budgeting.setBudgetingPeople(budgetVo.getBudgetingPeople());
                String addedTaxAmount = budgetVo.getAddedTaxAmount();
                BigDecimal decimal = new BigDecimal(addedTaxAmount);
                budgeting.setAddedTaxAmount(decimal);
                budgeting.setBudgetingTime(budgetVo.getBudgetingTime());
                budgeting.setBaseProjectId(budgetVo.getBaseProjectId());
                budgeting.setDelFlag("0");
                budgeting.setCreateTime(format);
                budgeting.setUpdateTime(format);
            }
            budgetingDao.insertSelective(budgeting);
            //成本审核信息
            AuditInfo auditInfo = new AuditInfo();
            if (budgetVo.getCostExamineResult() != null){
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                auditInfo.setBaseProjectId(budgetVo.getBaseProjectId());
                auditInfo.setAuditOpinion(budgetVo.getPriceExamineOpinion());
                auditInfo.setAuditorId(budgetVo.getCostExaminer());
                auditInfo.setAuditTime(budgetVo.getCostExamineTime());
                auditInfo.setCreateTime(format);
                auditInfo.setUpdateTime(format);
                auditInfo.setStatus("0");
            }
            auditInfoDao.insertSelective(auditInfo);

            //成本表
            CostPreparation costPreparation = new CostPreparation();
            if (budgetVo.getTotalCostAmount() != null){
                costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
                costPreparation.setBaseProjectId(budgetVo.getBaseProjectId());
                costPreparation.setCostTogether(budgetVo.getCostBy());
                costPreparation.setCostPreparationTime(budgetVo.getCostPreparationTime());
                String totalCostAmount = budgetVo.getTotalCostAmount();
                BigDecimal bigDecimal = new BigDecimal(totalCostAmount);
                costPreparation.setCostTotalAmount(bigDecimal);
                String totalVatAmount = budgetVo.getTotalVatAmount();
                BigDecimal bigDecimal1 = new BigDecimal(totalVatAmount);
                costPreparation.setVatAmount(bigDecimal1);
                String sourcingCost = budgetVo.getOutsourcingCostAmount();
                BigDecimal bigDecimal2 = new BigDecimal(sourcingCost);
                costPreparation.setOutsourcingCostAmount(bigDecimal2);
                String sourcingCost1 = budgetVo.getSourcingCost();
                BigDecimal bigDecimal3 = new BigDecimal(sourcingCost1);
                costPreparation.setTotalPackageMaterial(bigDecimal3);
                String otherExpensesOne = budgetVo.getOtherExpensesOne();
                BigDecimal bigDecimal4 = new BigDecimal(otherExpensesOne);
                String otherExpensesTwo = budgetVo.getOtherExpensesTwo();
                BigDecimal bigDecimal5 = new BigDecimal(otherExpensesTwo);
                String otherExpensesThree = budgetVo.getOtherExpensesThree();
                BigDecimal bigDecimal6 = new BigDecimal(otherExpensesThree);
                costPreparation.setOtherCost1(bigDecimal4);
                costPreparation.setOtherCost2(bigDecimal5);
                costPreparation.setOtherCost3(bigDecimal6);
                costPreparation.setDelFlag("0");
            }
            costPreparationDao.insertSelective(costPreparation);

            //控价编制
            VeryEstablishment veryEstablishment = new VeryEstablishment();
            if (budgetVo.getPriceControlCompilers() != null){
                veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
                veryEstablishment.setPricingTogether(budgetVo.getPriceControlCompilers());
                veryEstablishment.setEstablishmentTime(budgetVo.getPriceControlTime());
                veryEstablishment.setRemarkes(budgetVo.getPriceControlRemark());
                veryEstablishment.setDelFlag("0");
                veryEstablishment.setCreateTime(format);
                veryEstablishment.setUpdateTime(format);
            }
            veryEstablishmentDao.insertSelective(veryEstablishment);
            //控价审核
            if (budgetVo.getPriceExamineResult() != null){
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                auditInfo.setStatus("0");
                auditInfo.setAuditResult(budgetVo.getPriceExamineResult());
                auditInfo.setAuditOpinion(budgetVo.getPriceExamineOpinion());
                auditInfo.setAuditorId(budgetVo.getPriceExaminer());
                auditInfo.setAuditTime(budgetVo.getPriceExamineTime());
                auditInfo.setCreateTime(format);
                auditInfo.setUpdateTime(format);
            }
            auditInfoDao.insertSelective(auditInfo);

            //文件表
            FileInfo fileInfo = new FileInfo();
            //预算编制附件资料
            List<CostMeansList> costMeansList = budgetVo.getCostMeansList();
            for (CostMeansList thisMean : costMeansList) {
                if (thisMean.getCostFileName() != null){
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setName(thisMean.getCostMeansName());
                    fileInfo.setFileName(thisMean.getCostFileName());
                    fileInfo.setFilePath(thisMean.getCostFileDrawing());
                    fileInfo.setCreateTime(format);
                    fileInfo.setUpdateTime(format);
                    fileInfo.setStatus("0");
                    fileInfo.setType("ysbzfjzl");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }

            //成本编制附件资料
            List<TotalMeansList> totalMeansList = budgetVo.getTotalMeansList();
            for (TotalMeansList thisTotal : totalMeansList) {
                if (thisTotal.getTotalFileName() != null){
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setName(thisTotal.getTotalMeansName());
                    fileInfo.setFileName(thisTotal.getTotalFileName());
                    fileInfo.setFilePath(thisTotal.getTotalFileDrawing());
                    fileInfo.setCreateTime(format);
                    fileInfo.setUpdateTime(format);
                    fileInfo.setStatus("0");
                    fileInfo.setType("cbbzfjzl");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }

            //控价编制附件资料
            List<LabelMeansList> labelMeansList = budgetVo.getLabelMeansList();
            for (LabelMeansList thisLable : labelMeansList) {
                if (thisLable.getLabelFileName() != null){
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setName(thisLable.getLabelMeansName());
                    fileInfo.setFileName(thisLable.getLabelFileName());
                    fileInfo.setFilePath(thisLable.getLableFileDrawing());
                    fileInfo.setCreateTime(format);
                    fileInfo.setUpdateTime(format);
                    fileInfo.setStatus("0");
                    fileInfo.setType("kjbzfjzl");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }

        }
    }
}

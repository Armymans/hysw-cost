package net.zlw.cloud.budgeting.service.impl;

import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BudgetingServiceImpl implements BudgetingService {
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private BaseProjectDao projectDao;
    @Resource
    private SurveyInformationDao surveyInformationDao;
    @Resource
    private CostPreparationDao costPreparationDao;
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;
    @Resource
    private FileInfoMapper fileInfoMapper;
    @Autowired
    private FileInfoService fileInfoService;

    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;

    @Resource
    private VisaChangeMapper visaChangeMapper;


    @Override
    public void addBudgeting(BudgetingVo budgetingVo, UserInfo loginUser) {
        //获取基本信息
        Example example = new Example(BaseProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",budgetingVo.getBaseId());
        BaseProject baseProject = projectDao.selectOneByExample(example);

        //预算编制
        Budgeting budgeting = new Budgeting();
        budgeting.setId(UUID.randomUUID().toString().replace("-",""));
        budgeting.setAmountCost(budgetingVo.getAmountCost());
        budgeting.setBudgetingPeople(budgetingVo.getBudgetingPeople());
        budgeting.setAddedTaxAmount(budgetingVo.getAddedTaxAmount());
        budgeting.setOutsourcing(budgetingVo.getOutsourcing());
        budgeting.setNameOfCostUnit(budgetingVo.getNameOfCostUnit());
        budgeting.setContact(budgetingVo.getContact());
        budgeting.setContactPhone(budgetingVo.getContactPhone());
        budgeting.setAmountOutsourcing(budgetingVo.getAmountOutsourcing());
        budgeting.setReceiptTime(budgetingVo.getReceiptTime());
        budgeting.setBudgetingTime(budgetingVo.getBudgetingTime());
        budgeting.setRemarkes(budgetingVo.getBremarkes());
        budgeting.setBaseProjectId(baseProject.getId());
        budgeting.setDelFlag("0");
        budgeting.setWhetherAccount("1");
        budgeting.setFounderId("user309");
        //提交
        if (budgetingVo.getAuditNumber()!=null && !budgetingVo.getAuditNumber().equals("")){
            //修改预算状态为待审核
            baseProject.setBudgetStatus("1");
            baseProject.setProjectFlow(baseProject.getProjectFlow()+",2");
            projectDao.updateByPrimaryKeySelective(baseProject);
            budgetingDao.insertSelective(budgeting);
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            auditInfo.setBaseProjectId(budgeting.getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setStatus("0");
            auditInfo.setAuditorId(budgetingVo.getAuditorId());
            auditInfoDao.insertSelective(auditInfo);
            //保存
        }else{
            //修改预算状态为处理中
            baseProject.setBudgetStatus("2");
            baseProject.setProjectFlow(baseProject.getProjectFlow()+",2");
            projectDao.updateByPrimaryKeySelective(baseProject);
            budgetingDao.insertSelective(budgeting);
        }


        //勘探信息
        SurveyInformation surveyInformation = new SurveyInformation();
        surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
        surveyInformation.setSurveyDate(budgetingVo.getSurveyDate());
        surveyInformation.setInvestigationPersonnel(budgetingVo.getInvestigationPersonnel());
        surveyInformation.setSurveyBriefly(budgetingVo.getSurveyBriefly());
        surveyInformation.setPriceInformationName(budgetingVo.getPriceInformationName());
        surveyInformation.setPriceInformationNper(budgetingVo.getPriceInformationNper());
        surveyInformation.setBudgetingId(budgeting.getId());
        surveyInformation.setDelFlag("0");
        surveyInformation.setBaseProjectId(baseProject.getId());
        surveyInformation.setFounderId("user309");
        surveyInformationDao.insertSelective(surveyInformation);

        //成本编制
        CostPreparation costPreparation = new CostPreparation();
        costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
        costPreparation.setCostTotalAmount(budgetingVo.getCostTotalAmount());
        costPreparation.setVatAmount(budgetingVo.getCVatAmount());
        costPreparation.setTotalPackageMaterial(budgetingVo.getTotalPackageMaterial());
        costPreparation.setOutsourcingCostAmount(budgetingVo.getOutsourcingCostAmount());
        costPreparation.setOtherCost1(budgetingVo.getOtherCost1());
        costPreparation.setOtherCost2(budgetingVo.getOtherCost2());
        costPreparation.setOtherCost3(budgetingVo.getOtherCost3());
        costPreparation.setCostTogether(budgetingVo.getCostTogether());
        costPreparation.setReceivingTime(budgetingVo.getReceivingTime());
        costPreparation.setCostPreparationTime(budgetingVo.getCostPreparationTime());
        costPreparation.setRemarkes(budgetingVo.getCRemarkes());
        costPreparation.setBudgetingId(budgeting.getId());
        costPreparation.setDelFlag("0");
        costPreparation.setBaseProjectId(baseProject.getId());
        costPreparation.setFounderId("user309");
        costPreparationDao.insertSelective(costPreparation);

        //控价编制
        VeryEstablishment veryEstablishment = new VeryEstablishment();
        veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
        veryEstablishment.setBiddingPriceControl(budgetingVo.getBiddingPriceControl());
        veryEstablishment.setVatAmount(budgetingVo.getVVatAmount());
        veryEstablishment.setPricingTogether(budgetingVo.getPricingTogether());
        veryEstablishment.setReceivingTime(budgetingVo.getVReceivingTime());
        veryEstablishment.setEstablishmentTime(budgetingVo.getEstablishmentTime());
        veryEstablishment.setRemarkes(budgetingVo.getVRemarkes());
        veryEstablishment.setBudgetingId(budgeting.getId());
        veryEstablishment.setDelFlag("0");
        veryEstablishment.setBaseProjectId(baseProject.getId());
        veryEstablishment.setFounderId("user309");
        veryEstablishmentDao.insertSelective(veryEstablishment);

        //修改文件外键
        Example example1 = new Example(FileInfo.class);
        Example.Criteria c = example1.createCriteria();
        c.andLike("type","ysxmxj%");
        c.andEqualTo("status","0");
        c.andEqualTo("userId","user309");
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
        for (FileInfo fileInfo : fileInfos) {
            //修改文件外键
            fileInfoService.updateFileName2(fileInfo.getId(),budgeting.getId());
        }
    }

    @Override
    public BudgetingVo selectBudgetingById(String id, UserInfo loginUser) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(id);

        Example example = new Example(SurveyInformation.class);
        example.createCriteria().andEqualTo("budgetingId",id);
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);

        BaseProject baseProject = projectDao.selectByPrimaryKey(budgeting.getBaseProjectId());

        Example example1 = new Example(CostPreparation.class);
        example1.createCriteria().andEqualTo("budgetingId",id);
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);

        Example example2 = new Example(VeryEstablishment.class);
        example2.createCriteria().andEqualTo("budgetingId",id);
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example2);

        Example example3 = new Example(AuditInfo.class);
        Example.Criteria c = example3.createCriteria();
        c.andEqualTo("baseProjectId",id);
        c.andEqualTo("auditResult","0");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example3);
        if (auditInfo == null){
            Example example4 = new Example(AuditInfo.class);
            Example.Criteria c2 = example4.createCriteria();
            c2.andEqualTo("baseProjectId",id);
            c2.andEqualTo("auditResult","2");
             auditInfo = auditInfoDao.selectOneByExample(example4);
        }




        BudgetingVo budgetingVo = new BudgetingVo();
        budgetingVo.setAuditInfo(auditInfo);

        budgetingVo.setId(budgeting.getId());
        budgetingVo.setProjectNum(baseProject.getProjectNum());
        budgetingVo.setAmountCost(budgeting.getAmountCost());
        budgetingVo.setBudgetingPeople(budgeting.getBudgetingPeople());
        budgetingVo.setAddedTaxAmount(budgeting.getAddedTaxAmount());
        budgetingVo.setBudgetingTime(budgeting.getBudgetingTime());
        budgetingVo.setOutsourcing(budgeting.getOutsourcing());
        budgetingVo.setNameOfCostUnit(budgeting.getNameOfCostUnit());
        budgetingVo.setContact(budgeting.getContact());
        budgetingVo.setContactPhone(budgeting.getContactPhone());
        budgetingVo.setAmountOutsourcing(budgeting.getAmountOutsourcing());
        budgetingVo.setReceiptTime(budgeting.getReceiptTime());
        budgetingVo.setBremarkes(budgeting.getRemarkes());
        budgetingVo.setSurveyDate(surveyInformation.getSurveyDate());
        budgetingVo.setInvestigationPersonnel(surveyInformation.getInvestigationPersonnel());
        budgetingVo.setSurveyBriefly(surveyInformation.getSurveyBriefly());
        budgetingVo.setPriceInformationName(surveyInformation.getPriceInformationName());
        budgetingVo.setPriceInformationNper(surveyInformation.getPriceInformationNper());
        budgetingVo.setCostTotalAmount(costPreparation.getCostTotalAmount());
        budgetingVo.setCVatAmount(costPreparation.getVatAmount());
        budgetingVo.setTotalPackageMaterial(costPreparation.getTotalPackageMaterial());
        budgetingVo.setOutsourcingCostAmount(costPreparation.getOutsourcingCostAmount());
        budgetingVo.setOtherCost1(costPreparation.getOtherCost1());
        budgetingVo.setOtherCost2(costPreparation.getOtherCost2());
        budgetingVo.setOtherCost3(costPreparation.getOtherCost3());
        budgetingVo.setCostTogether(costPreparation.getCostTogether());
        budgetingVo.setReceivingTime(costPreparation.getReceivingTime());
        budgetingVo.setCostPreparationTime(costPreparation.getCostPreparationTime());
        budgetingVo.setCRemarkes(costPreparation.getRemarkes());
        budgetingVo.setBiddingPriceControl(veryEstablishment.getBiddingPriceControl());
        budgetingVo.setVVatAmount(veryEstablishment.getVatAmount());
        budgetingVo.setPricingTogether(veryEstablishment.getPricingTogether());
        budgetingVo.setVReceivingTime(veryEstablishment.getReceivingTime());
        budgetingVo.setEstablishmentTime(veryEstablishment.getEstablishmentTime());
        budgetingVo.setVRemarkes(veryEstablishment.getRemarkes());

        if (budgetingVo.getAuditInfo()!=null){

            if ("user309".equals(budgetingVo.getAuditInfo().getAuditorId())){
                budgetingVo.setCheckHidden("0");
            } else {
                budgetingVo.setCheckHidden("1");
            }
        }

        return budgetingVo;
    }

    @Override
    public void updateBudgeting(BudgetingVo budgetingVo) {
        //获取基本信息
        System.err.println(budgetingVo.getBaseId());
        Example example = new Example(BaseProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",budgetingVo.getBaseId());
        BaseProject baseProject = projectDao.selectOneByExample(example);
        //预算编制
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(budgetingVo.getId());
        budgeting.setAmountCost(budgetingVo.getAmountCost());
        budgeting.setBudgetingPeople(budgetingVo.getBudgetingPeople());
        budgeting.setAddedTaxAmount(budgetingVo.getAddedTaxAmount());
        budgeting.setOutsourcing(budgetingVo.getOutsourcing());
        budgeting.setNameOfCostUnit(budgetingVo.getNameOfCostUnit());
        budgeting.setContact(budgetingVo.getContact());
        budgeting.setContactPhone(budgetingVo.getContactPhone());
        budgeting.setAmountOutsourcing(budgetingVo.getAmountOutsourcing());
        budgeting.setReceiptTime(budgetingVo.getReceiptTime());
        budgeting.setBudgetingTime(budgetingVo.getBudgetingTime());
        budgeting.setRemarkes(budgetingVo.getBremarkes());
        budgeting.setBaseProjectId(baseProject.getId());
        if (budgetingVo.getAuditNumber()!=null && !budgetingVo.getAuditNumber().equals("")){
            if (budgetingVo.getAuditNumber().equals("1")){
                baseProject.setBudgetStatus("1");
                projectDao.updateByPrimaryKeySelective(baseProject);
                budgetingDao.updateByPrimaryKeySelective(budgeting);
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                auditInfo.setBaseProjectId(budgeting.getId());
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("0");
                auditInfo.setAuditorId(budgetingVo.getAuditorId());
                auditInfo.setStatus("0");
                auditInfoDao.insertSelective(auditInfo);
                //若未通过则再次提交
            }else if (budgetingVo.getAuditNumber().equals("2")){
                Example example1 = new Example(AuditInfo.class);
                example1.createCriteria().andEqualTo("baseProjectId",budgeting.getId());
                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                for (AuditInfo info : auditInfos) {
                    if (info.getAuditResult().equals("2")){
                        info.setAuditResult("0");
                        baseProject.setBudgetStatus("1");
                        projectDao.updateByPrimaryKeySelective(baseProject);
                        auditInfoDao.updateByPrimaryKeySelective(info);
                    }
                }
            }
        }else{
            baseProject.setBudgetStatus("2");
            projectDao.updateByPrimaryKeySelective(baseProject);
            budgetingDao.updateByPrimaryKeySelective(budgeting);
        }


        //勘探信息
        Example example1 = new Example(SurveyInformation.class);
        example1.createCriteria().andEqualTo("budgetingId",budgetingVo.getId());
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example1);
        System.err.println(surveyInformation);
        surveyInformation.setSurveyDate(budgetingVo.getSurveyDate());
        surveyInformation.setInvestigationPersonnel(budgetingVo.getInvestigationPersonnel());
        surveyInformation.setSurveyBriefly(budgetingVo.getSurveyBriefly());
        surveyInformation.setPriceInformationName(budgetingVo.getPriceInformationName());
        surveyInformation.setPriceInformationNper(budgetingVo.getPriceInformationNper());
        surveyInformation.setBudgetingId(budgeting.getId());
        System.err.println(surveyInformation);
        System.err.println(budgetingVo);
        surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);

        //成本编制
        Example example2 = new Example(CostPreparation.class);
        example2.createCriteria().andEqualTo("budgetingId",budgetingVo.getId());
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example2);
        costPreparation.setCostTotalAmount(budgetingVo.getCostTotalAmount());
        costPreparation.setVatAmount(budgetingVo.getCVatAmount());
        costPreparation.setTotalPackageMaterial(budgetingVo.getTotalPackageMaterial());
        costPreparation.setOutsourcingCostAmount(budgetingVo.getOutsourcingCostAmount());
        costPreparation.setOtherCost1(budgetingVo.getOtherCost1());
        costPreparation.setOtherCost2(budgetingVo.getOtherCost2());
        costPreparation.setOtherCost3(budgetingVo.getOtherCost3());
        costPreparation.setCostTogether(budgetingVo.getCostTogether());
        costPreparation.setReceivingTime(budgetingVo.getReceivingTime());
        costPreparation.setCostPreparationTime(budgetingVo.getCostPreparationTime());
        costPreparation.setRemarkes(budgetingVo.getCRemarkes());
        costPreparation.setBudgetingId(budgeting.getId());
        costPreparationDao.updateByPrimaryKeySelective(costPreparation);

        //控价编制
        Example example3 = new Example(VeryEstablishment.class);
        example3.createCriteria().andEqualTo("budgetingId",budgetingVo.getId());
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example3);
        veryEstablishment.setBiddingPriceControl(budgetingVo.getBiddingPriceControl());
        veryEstablishment.setVatAmount(budgetingVo.getVVatAmount());
        veryEstablishment.setPricingTogether(budgetingVo.getPricingTogether());
        veryEstablishment.setReceivingTime(budgetingVo.getVReceivingTime());
        veryEstablishment.setEstablishmentTime(budgetingVo.getEstablishmentTime());
        veryEstablishment.setRemarkes(budgetingVo.getVRemarkes());
        veryEstablishment.setBudgetingId(budgeting.getId());
        veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            Example example = new Example(AuditInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("baseProjectId",s);
            //所有审核信息
            List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
            for (AuditInfo auditInfo : auditInfos) {
                //通过
                if (batchReviewVo.getAuditResult().equals("1")){
                    //一审通过
                    if (auditInfo.getAuditResult().equals("0") && auditInfo.getAuditType().equals("0")){
                        auditInfo.setAuditResult("1");
                        auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(sim.format(new Date()));
                        auditInfo.setUpdateTime(sim.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        //进入二审
                        AuditInfo twoBatch = new AuditInfo();
                        twoBatch.setId(UUID.randomUUID().toString().replace("-",""));
                        twoBatch.setBaseProjectId(s);
                        twoBatch.setAuditResult("0");
                        twoBatch.setAuditType("1");
                        //上级领导
                        Example example1 = new Example(MemberManage.class);
                        Example.Criteria c = example1.createCriteria();
                        c.andEqualTo("depId","2");
                        c.andEqualTo("depAdmin","1");
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        twoBatch.setAuditorId(memberManage.getId());
                        twoBatch.setStatus("0");
                        twoBatch.setCreateTime(sim.format(new Date()));
                        //二审添加
                        auditInfoDao.insertSelective(twoBatch);
                        break;
                        //二审通过
                    }else if(auditInfo.getAuditResult().equals("0") && auditInfo.getAuditType().equals("1")){
                        auditInfo.setAuditResult("1");
                        auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(sim.format(new Date()));
                        auditInfo.setUpdateTime(sim.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        //判断是否需要三审
                        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
                        if (budgeting.getAmountCost().compareTo(new BigDecimal("8000000.00"))>=0){
                            AuditInfo auditInfo1 = new AuditInfo();
                            auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                            auditInfo1.setBaseProjectId(s);
                            auditInfo1.setAuditResult("0");
                            auditInfo1.setAuditType("4");
                            Example example1 = new Example(MemberManage.class);
                            Example.Criteria c = example1.createCriteria();
                            c.andEqualTo("memberRoleId","2");
                            MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                            auditInfo1.setAuditorId(memberManage.getId());
                            auditInfo1.setStatus("0");
                            auditInfo1.setCreateTime(sim.format(new Date()));
                            auditInfoDao.insertSelective(auditInfo1);
                        }else{
                            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                            //设置为已完成
                            baseProject.setBudgetStatus("4");
                            baseProjectDao.updateByPrimaryKeySelective(baseProject);
                        }
                        //三审通过
                    }else if(auditInfo.getAuditResult().equals("0") && auditInfo.getAuditType().equals("4")){
                        auditInfo.setAuditResult("1");
                        auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(sim.format(new Date()));
                        auditInfo.setUpdateTime(sim.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                        //设置为已完成
                        baseProject.setBudgetStatus("4");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    }
                //未通过
                }else if (batchReviewVo.getAuditResult().equals("2")){
                    if (auditInfo.getAuditResult().equals("0")){
                        auditInfo.setAuditResult("2");
                        auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(simpleDateFormat.format(new Date()));
                        auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        //设置为未通过
                        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                        //设置为已完成
                        baseProject.setBudgetStatus("3");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    }
                }

            }
        }
    }

    @Override
    public void intoAccount(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
            budgeting.setWhetherAccount("0");
            budgetingDao.updateByPrimaryKeySelective(budgeting);
        }
    }

    @Override
    public List<BudgetingListVo> findAllBudgeting(PageBVo pageBVo) {

        return  budgetingDao.findAllBudgeting(pageBVo);

    }
    @Override
    public List<BudgetingListVo> findBudgetingAll(PageBVo pageBVo, String sid) {
        List<BudgetingListVo> budgetingAll = budgetingDao.findBudgetingAll(pageBVo);
        ArrayList<BudgetingListVo> budgetingListVos = new ArrayList<>();
        budgetingListVos.addAll(budgetingAll);
        System.err.println(budgetingAll.size());
        System.err.println(budgetingListVos.size());
        // 判断状态 结算
        if (sid!=null && sid.equals("5")){
            for (BudgetingListVo budgetingListVo : budgetingAll) {
                System.err.println(budgetingAll);
                System.err.println(budgetingAll);
                System.err.println(budgetingAll);
                System.err.println(budgetingAll);

                Example example = new Example(LastSettlementReview.class);
                Example example1 = new Example(SettlementAuditInformation.class);
                Example.Criteria criteria = example.createCriteria();
                Example.Criteria criteria1 = example1.createCriteria();
                criteria.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                criteria.andEqualTo("delFlag","0");
                criteria1.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                criteria1.andEqualTo("delFlag","0");
                List<LastSettlementReview> lastSettlementReviews = lastSettlementReviewDao.selectByExample(example);
                List<SettlementAuditInformation> settlementAuditInformations = settlementAuditInformationDao.selectByExample(example1);
                if (lastSettlementReviews.size()>0 || settlementAuditInformations.size()>0){
                    budgetingListVos.remove(budgetingListVo);
                }
            }
        }else if("3".equals(sid)){// 跟踪审计
            for (BudgetingListVo budgetingListVo : budgetingAll) {
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetingListVo.getBaseId());

                Example example = new Example(TrackAuditInfo.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("status","0");

                List<TrackAuditInfo> trackAuditInfos = trackAuditInfoDao.selectByExample(example);
                for (TrackAuditInfo trackAuditInfo : trackAuditInfos) {
                    if(trackAuditInfo.getBaseProjectId().equals(baseProject.getId())){
                        budgetingListVos.remove(budgetingListVo);
                    }
                }
            }
        }else if("2".equals(sid)){
            for (BudgetingListVo budgetingListVo : budgetingAll) {
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetingListVo.getBaseId());

                Example example = new Example(Budgeting.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("delFlag","0");

                List<Budgeting> budgetings = budgetingDao.selectByExample(example);
                for (Budgeting budgeting : budgetings) {
                    if(baseProject.getId().equals(budgeting.getBaseProjectId())){
                        budgetingListVos.remove(budgetingListVo);
                    }
                }
            }
            //签证变更
        }else if(sid!=null && sid.equals("4")){
            for (BudgetingListVo budgetingListVo : budgetingAll) {
                Example example = new Example(VisaChange.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                c.andEqualTo("state","0");
                List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
                if (visaChanges!=null && visaChanges.size()!=0){
                    budgetingListVos.remove(budgetingListVo);
                }
            }
        }
        return budgetingListVos;
    }

    // 备份
    public List<BudgetingListVo> findBudgetingAll1(PageBVo pageBVo, String sid) {
        List<BudgetingListVo> budgetingAll = budgetingDao.findBudgetingAll(pageBVo);
        ArrayList<BudgetingListVo> budgetingListVos = new ArrayList<>();
        budgetingListVos.addAll(budgetingAll);
        System.err.println(budgetingAll.size());
        System.err.println(budgetingListVos.size());
        // 判断状态
        if (sid!=null && sid.equals("5")){
            for (BudgetingListVo budgetingListVo : budgetingAll) {
                Example example = new Example(LastSettlementReview.class);
                Example example1 = new Example(SettlementAuditInformation.class);
                Example.Criteria criteria = example.createCriteria();
                Example.Criteria criteria1 = example1.createCriteria();
                criteria.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                criteria.andEqualTo("delFlag","0");
                criteria1.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                criteria1.andEqualTo("delFlag","0");
                List<LastSettlementReview> lastSettlementReviews = lastSettlementReviewDao.selectByExample(example);
                List<SettlementAuditInformation> settlementAuditInformations = settlementAuditInformationDao.selectByExample(example1);
                if (lastSettlementReviews!=null && lastSettlementReviews.size()!=0 || settlementAuditInformations!=null && settlementAuditInformations.size()!=0){
                    budgetingListVos.remove(budgetingListVo);
                }
            }
        }
        return budgetingListVos;
    }

    @Override
    public void addAttribution(String id, String designCategory, String district) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        baseProject.setDesignCategory(designCategory);
        baseProject.setDistrict(district);
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
    }

    @Override
    public List<DesignInfo> findDesignAll(PageBVo pageBVo) {
       return baseProjectDao.findDesignAll(pageBVo);
    }

    @Override
    public void deleteBudgeting(String id) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(id);
        budgeting.setDelFlag("1");
        budgetingDao.updateByPrimaryKeySelective(budgeting);

        Example example = new Example(SurveyInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("budgetingId",id);
        c.andEqualTo("delFlag","0");
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);
        surveyInformation.setDelFlag("1");
        surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);

        Example example1 = new Example(CostPreparation.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("budgetingId",id);
        c1.andEqualTo("delFlag","0");
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);
        costPreparation.setDelFlag("1");
        costPreparationDao.updateByPrimaryKeySelective(costPreparation);

        Example example2 = new Example(VeryEstablishment.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("budgetingId",id);
        c2.andEqualTo("delFlag","0");
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example2);
        veryEstablishment.setDelFlag("1");
        veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);

        Example example3 = new Example(AuditInfo.class);
        Example.Criteria criteria = example3.createCriteria();
        criteria.andEqualTo("baseProjectId",id);
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example3);
        for (AuditInfo auditInfo : auditInfos) {
            auditInfoDao.deleteByPrimaryKey(auditInfo);
        }
    }

    @Override
    public void deleteBudgetingFile(String id) {
        Example example = new Example(FileInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("platCode",id);
        c.andEqualTo("status","0");
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
        for (FileInfo fileInfo : fileInfos) {
            fileInfo.setStatus("1");
            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }
    }

    @Override
    public UnionQueryVo unionQuery(String id, UserInfo loginUser) {
        UnionQueryVo unionQueryVo = new UnionQueryVo();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        unionQueryVo.setBaseProject(baseProject);
        Example example = new Example(BaseProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("virtualCode",baseProject.getVirtualCode());
        List<BaseProject> baseProjects = baseProjectDao.selectByExample(example);
        String idi = "";
        for (BaseProject project : baseProjects) {
            List<String> codeAll = unionQueryVo.getCodeAll();
            codeAll.add(project.getProjectNum());
            if (project.getMergeFlag().equals("0")){
                idi = project.getId();
            }
        }
        Example example1 = new Example(Budgeting.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseProjectId",idi);
        Budgeting budgeting1 = budgetingDao.selectOneByExample(example1);
        BudgetingVo budgeting = selectBudgetingById(budgeting1.getId(), loginUser);
        unionQueryVo.setBudgeting(budgeting);

        return unionQueryVo;
    }

    @Override
    public void singleAudit(SingleAuditVo singleAuditVo) {
        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",singleAuditVo.getId());
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
        if (auditInfos!=null){
            for (AuditInfo auditInfo : auditInfos) {
                if (auditInfo.getAuditType().equals("0")){
                    if (auditInfo.getAuditResult().equals("0")){
                        auditInfo.setAuditResult(singleAuditVo.getAuditResult());
                        auditInfo.setAuditOpinion(singleAuditVo.getAuditOpnion());
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(sim.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        if (singleAuditVo.getAuditResult().equals("1")){
                            AuditInfo auditInfo1 = new AuditInfo();
                            auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                            auditInfo1.setBaseProjectId(singleAuditVo.getId());
                            auditInfo1.setAuditResult("0");
                            auditInfo1.setAuditType("1");
                            Example example1 = new Example(MemberManage.class);
                            Example.Criteria criteria1 = example1.createCriteria();
                            criteria1.andEqualTo("depId","2");
                            criteria1.andEqualTo("depAdmin","1");
                            auditInfo1.setAuditorId(memberManageDao.selectOneByExample(example1).getId());
                            auditInfoDao.insertSelective(auditInfo1);
                        }
                    }
                } else if(auditInfo.getAuditType().equals("1")){
                    if (auditInfo.getAuditResult().equals("0")){
                        auditInfo.setAuditResult(singleAuditVo.getAuditResult());
                        auditInfo.setAuditOpinion(singleAuditVo.getAuditOpnion());
                        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo.setAuditTime(sim.format(new Date()));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    }

                }
            }
        }
    }




    /**
     * 清标--新建--项目名称--项目信息下拉列表
     * @param founderId
     * @return
     */
    public List<net.zlw.cloud.clearProject.model.Budgeting> findAllByFounderId(String founderId){
        List<net.zlw.cloud.clearProject.model.Budgeting> budgetingList = budgetingDao.findBudgetingByFounderId(founderId);
        return budgetingList;
    }

    public List<net.zlw.cloud.clearProject.model.Budgeting> findBudgetingByBudgetStatus(String founderId){
        List<net.zlw.cloud.clearProject.model.Budgeting> budgetingByBudgetStatus = budgetingDao.findBudgetingByBudgetStatus(founderId);
        return budgetingByBudgetStatus;
    }
}

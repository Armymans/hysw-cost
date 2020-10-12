package net.zlw.cloud.settleAccounts.service.impl;


import net.tec.cloud.common.util.RestUtil;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SettleAccountsServiceimpl implements SettleAccountsService {
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private MemberManageDao memberManageDao;

    @Override
    public List<AccountsVo> findAllAccounts(PageVo pageVo) {
        List<AccountsVo> list  = baseProjectDao.findAllAccounts(pageVo);
        return list;
    }

    @Override
    public void deleteAcmcounts(String id) {

        Example example = new Example(InvestigationOfTheAmount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",id);
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);
        investigationOfTheAmount.setDelFlag("1");
        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);

        Example example1 = new Example(LastSettlementReview.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseProjectId",id);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example1);
        if (lastSettlementReview!=null){
            lastSettlementReview.setDelFlag("1");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }
        Example example2 = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("baseProjectId",id);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example2);
        if (settlementAuditInformation!=null){
            settlementAuditInformation.setDelFlag("1");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void updateAccount(String s) {
        BaseProject baseProject = new BaseProject();
        baseProject.setId(s);
        baseProject.setSaWhetherAccount("0");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        //上家到账
        Example example = new Example(LastSettlementReview.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",s);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview!=null){
            lastSettlementReview.setWhetherAccount("0");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }

        //下家到账
        Example example1 = new Example(SettlementAuditInformation.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId",s);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation!=null){
            settlementAuditInformation.setWhetherAccount("0");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void addAccount(BaseAccountsVo baseAccountsVo) {
        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("projectNum",baseAccountsVo.getProjectNum());
        BaseProject project = baseProjectDao.selectOneByExample(example);
        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
        LastSettlementReview lastSettlementReview = new LastSettlementReview();
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        if (baseAccountsVo.getProjectPeople()!=null && !baseAccountsVo.getProjectPeople().equals("")){
            lastSettlementReview.setId(UUID.randomUUID().toString());
            lastSettlementReview.setReviewNumber(baseAccountsVo.getReviewNumber());
            lastSettlementReview.setPreparePeople(baseAccountsVo.getPreparePeople());
            lastSettlementReview.setProjectTime(baseAccountsVo.getProjectTime());
            lastSettlementReview.setProjectPeople(baseAccountsVo.getProjectPeople());
            lastSettlementReview.setContractAmount(baseAccountsVo.getContractAmount());
            lastSettlementReview.setContractRemarkes(baseAccountsVo.getContractRemarkes());
            lastSettlementReview.setOutsourcing(baseAccountsVo.getOutsourcing());
            lastSettlementReview.setNameOfTheCost(baseAccountsVo.getNameOfTheCost());
            lastSettlementReview.setContact(baseAccountsVo.getContact());
            lastSettlementReview.setContactPhone(baseAccountsVo.getContactPhone());
            lastSettlementReview.setAmountOutsourcing(baseAccountsVo.getAmountOutsourcing());
            lastSettlementReview.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
            lastSettlementReview.setBaseProjectId(project.getId());
            lastSettlementReview.setRemark(baseAccountsVo.getLastSettleinfo().getRemark());

            lastSettlementReviewDao.insert(lastSettlementReview);
        }



        if (baseAccountsVo.getDownContact()!=null && !baseAccountsVo.getDownContact().equals("")){
            settlementAuditInformation.setId(UUID.randomUUID().toString() );
            settlementAuditInformation.setAuthorizedNumber(baseAccountsVo.getAuthorizedNumber());
            settlementAuditInformation.setSubtractTheNumber(baseAccountsVo.getSubtractTheNumber());
            settlementAuditInformation.setNuclearNumber(baseAccountsVo.getNuclearNNumber());
            settlementAuditInformation.setRemarkes(baseAccountsVo.getDownRemarkes());
            settlementAuditInformation.setContractAmount(baseAccountsVo.getDownContractAmount());
            settlementAuditInformation.setContractRemarkes(baseAccountsVo.getDownContractRemarkes());
            settlementAuditInformation.setPreparePeople(baseAccountsVo.getDownPreparePeople());
            settlementAuditInformation.setOutsourcing(baseAccountsVo.getDownOutsourcing());
            settlementAuditInformation.setNameOfTheCost(baseAccountsVo.getDownNameOfTheCost());
            settlementAuditInformation.setContact(baseAccountsVo.getDownContact());
            settlementAuditInformation.setContactPhone(baseAccountsVo.getDownContactPhone());
            settlementAuditInformation.setAmountOutsourcing(baseAccountsVo.getDownAmountOutsourcing());
            settlementAuditInformation.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
            settlementAuditInformation.setBaseProjectId(project.getId());
            settlementAuditInformationDao.insert(settlementAuditInformation);
        }


        if (baseAccountsVo.getSettleAccountsStatus()!=null && !baseAccountsVo.getSettleAccountsStatus().equals("")){
            project.setSettleAccountsStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(project);
            AuditInfo auditInfo = new AuditInfo();
            if (baseAccountsVo.getDownContact()!=null && !baseAccountsVo.getDownContact().equals("")){
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                auditInfo.setBaseProjectId(settlementAuditInformation.getId());
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("0");
                auditInfoDao.insert(auditInfo);
            }
        }else{
            project.setSettleAccountsStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(project);
        }
        investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-",""));
        investigationOfTheAmount.setSurveyDate(baseAccountsVo.getSurveyDate());
        investigationOfTheAmount.setInvestigationPersonnel(baseAccountsVo.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(baseAccountsVo.getSurveyBriefly());
        investigationOfTheAmount.setUnbalancedQuotationAdjustment(baseAccountsVo.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(baseAccountsVo.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(baseAccountsVo.getOutboundAmount());
        investigationOfTheAmount.setRemarkes(baseAccountsVo.getRemarkes());
        investigationOfTheAmount.setMaterialDifferenceAmount(baseAccountsVo.getMaterialDifferenceAmount());
        investigationOfTheAmount.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
        investigationOfTheAmount.setBaseProjectId(project.getId());
        investigationOfTheAmountDao.insert(investigationOfTheAmount);
    }

    @Override
    public BaseAccountsVo findAccountById(String id) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);

        Example example = new Example(InvestigationOfTheAmount.class);
        example.createCriteria().andEqualTo("baseProjectId",id);
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);

        Example example1 = new Example(LastSettlementReview.class);
        example1.createCriteria().andEqualTo("baseProjectId",id);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example1);

        Example example2 = new Example(SettlementAuditInformation.class);
        example2.createCriteria().andEqualTo("baseProjectId",id);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example2);

        BaseAccountsVo baseAccountsVo = new BaseAccountsVo();
        baseAccountsVo.setId(baseProject.getId());
        baseAccountsVo.setProjectNum(baseProject.getProjectNum());
        baseAccountsVo.setProjectName(baseProject.getProjectName());
        baseAccountsVo.setApplicationNum(baseProject.getApplicationNum());
        baseAccountsVo.setCeaNum(baseProject.getCeaNum());
        baseAccountsVo.setDistrict(baseProject.getDistrict());
        baseAccountsVo.setDesignCategory(baseProject.getDesignCategory());
        baseAccountsVo.setConstructionUnit(baseProject.getConstructionUnit());
        baseAccountsVo.setContacts(baseProject.getContacts());
        baseAccountsVo.setContactNumber(baseProject.getContactNumber());
        baseAccountsVo.setCustomerName(baseProject.getCustomerName());
        baseAccountsVo.setSubject(baseProject.getSubject());
        baseAccountsVo.setCustomerPhone(baseProject.getCustomerPhone());
        baseAccountsVo.setConstructionOrganization(baseProject.getConstructionOrganization());
        baseAccountsVo.setProjectNature(baseProject.getProjectNature());
        baseAccountsVo.setProjectCategory(baseProject.getProjectCategory());
        baseAccountsVo.setWaterAddress(baseProject.getWaterAddress());
        baseAccountsVo.setWaterSupplyType(baseProject.getWaterSupplyType());
        baseAccountsVo.setThisDeclaration(baseProject.getThisDeclaration());
        baseAccountsVo.setAgent(baseProject.getAgent());
        baseAccountsVo.setAgentPhone(baseProject.getAgentPhone());
        baseAccountsVo.setApplicationDate(baseProject.getApplicationDate());
        baseAccountsVo.setBusinessLocation(baseProject.getBusinessLocation());
        baseAccountsVo.setBusinessTypes(baseProject.getBusinessTypes());
        baseAccountsVo.setAB(baseProject.getAB());
        baseAccountsVo.setWaterUse(baseProject.getWaterUse());
        baseAccountsVo.setFireTableSize(baseProject.getFireTableSize());
        baseAccountsVo.setClassificationCaliber(baseProject.getClassificationCaliber());
        baseAccountsVo.setWaterMeterDiameter(baseProject.getWaterMeterDiameter());
        baseAccountsVo.setSite(baseProject.getSite());
        baseAccountsVo.setSystemNumber(baseProject.getSystemNumber());
        baseAccountsVo.setProposer(baseProject.getProposer());
        baseAccountsVo.setApplicationNumber(baseProject.getApplicationNumber());
        baseAccountsVo.setSettleAccountsStatus(baseProject.getSettleAccountsStatus());

        baseAccountsVo.setSurveyDate(investigationOfTheAmount.getSurveyDate());
        baseAccountsVo.setInvestigationPersonnel(investigationOfTheAmount.getInvestigationPersonnel());
        baseAccountsVo.setSurveyBriefly(investigationOfTheAmount.getSurveyBriefly());
        baseAccountsVo.setUnbalancedQuotationAdjustment(investigationOfTheAmount.getUnbalancedQuotationAdjustment());
        baseAccountsVo.setPunishAmount(investigationOfTheAmount.getPunishAmount());
        baseAccountsVo.setOutboundAmount(investigationOfTheAmount.getOutboundAmount());
        baseAccountsVo.setRemarkes(investigationOfTheAmount.getRemarkes());
        baseAccountsVo.setMaterialDifferenceAmount(investigationOfTheAmount.getMaterialDifferenceAmount());
        baseAccountsVo.setMaintenanceProjectInformation(investigationOfTheAmount.getMaintenanceProjectInformation());


        baseAccountsVo.setReviewNumber(lastSettlementReview.getReviewNumber());
        baseAccountsVo.setPreparePeople(lastSettlementReview.getPreparePeople());
        baseAccountsVo.setProjectTime(lastSettlementReview.getProjectTime());
        baseAccountsVo.setProjectPeople(lastSettlementReview.getProjectPeople());
        baseAccountsVo.setContractAmount(lastSettlementReview.getContractAmount());
        baseAccountsVo.setContractRemarkes(lastSettlementReview.getContractRemarkes());
        baseAccountsVo.setOutsourcing(lastSettlementReview.getOutsourcing());
        baseAccountsVo.setNameOfTheCost(lastSettlementReview.getNameOfTheCost());
        baseAccountsVo.setContact(lastSettlementReview.getContact());
        baseAccountsVo.setContactPhone(lastSettlementReview.getContactPhone());
        baseAccountsVo.setAmountOutsourcing(lastSettlementReview.getAmountOutsourcing());
        baseAccountsVo.setMaintenanceProjectInformation(lastSettlementReview.getMaintenanceProjectInformation());

        baseAccountsVo.setDownAmountOutsourcing(settlementAuditInformation.getAmountOutsourcing());
        baseAccountsVo.setSubtractTheNumber(settlementAuditInformation.getSubtractTheNumber());
        baseAccountsVo.setNuclearNNumber(settlementAuditInformation.getNuclearNumber());
        baseAccountsVo.setRemarkes(settlementAuditInformation.getRemarkes());
        baseAccountsVo.setDownContractAmount(settlementAuditInformation.getContractAmount());
        baseAccountsVo.setDownContractRemarkes(settlementAuditInformation.getContractRemarkes());
        baseAccountsVo.setDownPreparePeople(settlementAuditInformation.getPreparePeople());
        baseAccountsVo.setDownOutsourcing(settlementAuditInformation.getOutsourcing());
        baseAccountsVo.setDownNameOfTheCost(settlementAuditInformation.getNameOfTheCost());
        baseAccountsVo.setDownContact(settlementAuditInformation.getContact());
        baseAccountsVo.setDownContactPhone(settlementAuditInformation.getContactPhone());
        baseAccountsVo.setDownAmountOutsourcing(settlementAuditInformation.getAmountOutsourcing());
        baseAccountsVo.setMaintenanceProjectInformation(settlementAuditInformation.getMaintenanceProjectInformation());

        return baseAccountsVo;
    }

    @Override
    public void updateAccountById(BaseAccountsVo baseAccountsVo) {
        BaseProject project = new BaseProject();
        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
        LastSettlementReview lastSettlementReview = new LastSettlementReview();
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        if (baseAccountsVo.getSettleAccountsStatus()!=null && !baseAccountsVo.getSettleAccountsStatus().equals("")){
            if (baseAccountsVo.getSettleAccountsStatus().equals("1")){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setBaseProjectId(baseAccountsVo.getId());
                auditInfo.setAuditType("0");
                auditInfo.setAuditorId(baseAccountsVo.getAuditorId());
                auditInfoDao.insert(auditInfo);
            }else if(baseAccountsVo.getSettleAccountsStatus().equals("2")){
                Example example1 = new Example(AuditInfo.class);
                example1.createCriteria().andEqualTo("baseProjectId",baseAccountsVo.getId());
                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                for (AuditInfo info : auditInfos) {
                    if (info.getAuditResult().equals("0")){
                        info.setAuditResult("1");
                        auditInfoDao.updateByPrimaryKeySelective(info);
                    }
                }
            }

        }else{
            project.setSettleAccountsStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(project);
        }

        investigationOfTheAmount.setSurveyDate(baseAccountsVo.getSurveyDate());
        investigationOfTheAmount.setInvestigationPersonnel(baseAccountsVo.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(baseAccountsVo.getSurveyBriefly());
        investigationOfTheAmount.setUnbalancedQuotationAdjustment(baseAccountsVo.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(baseAccountsVo.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(baseAccountsVo.getOutboundAmount());
        investigationOfTheAmount.setRemarkes(baseAccountsVo.getRemarkes());
        investigationOfTheAmount.setMaterialDifferenceAmount(baseAccountsVo.getMaterialDifferenceAmount());
        investigationOfTheAmount.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
        investigationOfTheAmount.setBaseProjectId(project.getId());
        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);



        lastSettlementReview.setReviewNumber(baseAccountsVo.getReviewNumber());
        lastSettlementReview.setPreparePeople(baseAccountsVo.getPreparePeople());
        lastSettlementReview.setProjectTime(baseAccountsVo.getProjectTime());
        lastSettlementReview.setProjectPeople(baseAccountsVo.getProjectPeople());
        lastSettlementReview.setContractAmount(baseAccountsVo.getContractAmount());
        lastSettlementReview.setContractRemarkes(baseAccountsVo.getContractRemarkes());
        lastSettlementReview.setOutsourcing(baseAccountsVo.getOutsourcing());
        lastSettlementReview.setNameOfTheCost(baseAccountsVo.getNameOfTheCost());
        lastSettlementReview.setContact(baseAccountsVo.getContact());
        lastSettlementReview.setContactPhone(baseAccountsVo.getContactPhone());
        lastSettlementReview.setAmountOutsourcing(baseAccountsVo.getAmountOutsourcing());
        lastSettlementReview.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
        lastSettlementReview.setBaseProjectId(project.getId());
        lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);




        settlementAuditInformation.setAuthorizedNumber(baseAccountsVo.getAuthorizedNumber());
        settlementAuditInformation.setSubtractTheNumber(baseAccountsVo.getSubtractTheNumber());
        settlementAuditInformation.setNuclearNumber(baseAccountsVo.getNuclearNNumber());
        settlementAuditInformation.setRemarkes(baseAccountsVo.getDownRemarkes());
        settlementAuditInformation.setContractAmount(baseAccountsVo.getDownContractAmount());
        settlementAuditInformation.setContractRemarkes(baseAccountsVo.getDownContractRemarkes());
        settlementAuditInformation.setPreparePeople(baseAccountsVo.getDownPreparePeople());
        settlementAuditInformation.setOutsourcing(baseAccountsVo.getDownOutsourcing());
        settlementAuditInformation.setNameOfTheCost(baseAccountsVo.getDownNameOfTheCost());
        settlementAuditInformation.setContact(baseAccountsVo.getDownContact());
        settlementAuditInformation.setContactPhone(baseAccountsVo.getDownContactPhone());
        settlementAuditInformation.setAmountOutsourcing(baseAccountsVo.getDownAmountOutsourcing());
        settlementAuditInformation.setMaintenanceProjectInformation(baseAccountsVo.getMaintenanceProjectInformation());
        settlementAuditInformation.setBaseProjectId(project.getId());
        settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
    }


    @Override
    public void batchReview(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            String audit = "";
            //查找上家
            Example example2 = new Example(LastSettlementReview.class);
            Example.Criteria criteria = example2.createCriteria();
            criteria.andEqualTo("baseProjectId",s);
            LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example2);
            Example example3 = new Example(SettlementAuditInformation.class);
            Example.Criteria criteria1 = example3.createCriteria();
            criteria1.andEqualTo("baseProjectId",s);
            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example3);
            if (settlementAuditInformation!=null){
                audit = settlementAuditInformation.getId();
            }else if(lastSettlementReview!=null){
                audit = lastSettlementReview.getId();
            }

            Example example = new Example(AuditInfo.class);
            example.createCriteria().andEqualTo("baseProjectId",audit).andEqualTo("auditResult","0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
            if (batchReviewVo.getAuditResult().equals("1")){
                if (auditInfo.getAuditType().equals("0")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(audit);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");

                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfoDao.insertSelective(auditInfo1);
                }else if(auditInfo.getAuditType().equals("1")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                }
            }else if(batchReviewVo.getAuditResult().equals("2")){
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            }
        }
    }
}

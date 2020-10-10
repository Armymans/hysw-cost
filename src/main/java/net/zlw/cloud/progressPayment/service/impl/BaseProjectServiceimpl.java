package net.zlw.cloud.progressPayment.service.impl;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.mapper.*;
import net.zlw.cloud.progressPayment.model.*;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;
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
public class BaseProjectServiceimpl implements BaseProjectService {
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private ApplicationInformationDao applicationInformationDao;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private ProgressPaymentTotalPaymentDao progressPaymentTotalPaymentDao;
    @Resource
    private MemberManageDao memberManageDao;
    /**
     * 添加进度款信息
     *
     * @param baseProject 用户填写信息表
     * @param loginUser
     */
    @Override
    public void addProgress(BaseProjectVo baseProject, UserInfo loginUser) {

        //项目基本信息
        BaseProject project = findById(baseProject.getProjectNum());
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = new ProgressPaymentInformation();
        //审核表
        AuditInfo auditInfo = new AuditInfo();
        //成员管理


        paymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
        paymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
        paymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
        paymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
        paymentInformation.setContractAmount(baseProject.getContractAmount());
        paymentInformation.setProjectType(baseProject.getProjectType());
        paymentInformation.setReceivingTime(baseProject.getReceivingTime());
        paymentInformation.setCompileTime(baseProject.getCompileTime());
        paymentInformation.setOutsourcing(baseProject.getOutsourcing());
        paymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
        paymentInformation.setContact(baseProject.getContact());
        paymentInformation.setContactPhone(baseProject.getContactPhone());
        paymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
        paymentInformation.setSituation(baseProject.getSituation());
        paymentInformation.setRemarkes(baseProject.getRemarkes());
        paymentInformation.setBaseProjectId(project.getId());
        paymentInformation.setId(UUID.randomUUID().toString().replace("-",""));
        paymentInformation.setFounderId(loginUser.getId());
        progressPaymentInformationDao.insert(paymentInformation);

        if (baseProject != null && !baseProject.equals("")) {
            if (baseProject.getAuditNumber() != null && !baseProject.getAuditNumber().equals("")) {
                project.setProgressPaymentStatus("1");
                project.setProjectFlow(project.getProjectFlow()+",4");
                baseProjectDao.updateByPrimaryKeySelective(project);
                auditInfo.setBaseProjectId(paymentInformation.getId());
                auditInfo.setAuditResult("0");
                auditInfo.setAuditorId(baseProject.getAuditorId());
                auditInfo.setAuditType("0");
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                auditInfoDao.insert(auditInfo);
            } else {
                project.setProgressPaymentStatus("2");
                project.setProjectFlow(project.getProjectFlow()+",4");
                baseProjectDao.updateByPrimaryKeySelective(project);
            }


            information.setRemarkes(baseProject.getRemarkes());
            information.setBaseProjectId(project.getId());
            information.setId(UUID.randomUUID().toString());
            information.setProgressPaymentId(paymentInformation.getId());
            applicationInformationDao.insert(information);


            payment.setTotalPaymentAmount(baseProject.getTotalPaymentAmount());
            payment.setCumulativeNumberPayment(baseProject.getCumulativeNumberPayment());
            payment.setAccumulativePaymentProportion(baseProject.getAccumulativePaymentProportion());
            payment.setBaseProjectId(project.getId());
            payment.setProgressPaymentId(paymentInformation.getId());
            payment.setId(UUID.randomUUID().toString());
            progressPaymentTotalPaymentDao.insert(payment);



        }
    }

    /**
     * 根据id查询进度款信息
     */
    @Override
    public BaseProjectVo seachProgressById(String id) {
        /*
        * //项目基本信息
        BaseProject project = new BaseProject();
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = new ProgressPaymentInformation();
        //审核表
        AuditInfo auditInfo = new AuditInfo();
        * */
        BaseProjectVo baseProjectVo = new BaseProjectVo();
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);

        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(paymentInformation.getBaseProjectId());

        Example example = new Example(ApplicationInformation.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("progressPaymentId", id);
        ApplicationInformation applicationInformation = applicationInformationDao.selectOneByExample(example);

        Example example1 = new Example(ProgressPaymentTotalPayment.class);
        example1.createCriteria().andEqualTo("progressPaymentId", id);
        ProgressPaymentTotalPayment totalPayment = progressPaymentTotalPaymentDao.selectOneByExample(example1);


        baseProjectVo.setId(baseProject.getId());
        baseProjectVo.setProjectNum(baseProject.getProjectNum());
        baseProjectVo.setProjectName(baseProject.getProjectName());
        baseProjectVo.setApplicationNum(baseProject.getApplicationNum());
        baseProjectVo.setCeaNum(baseProject.getCeaNum());
        baseProjectVo.setDistrict(baseProject.getDistrict());
        baseProjectVo.setDesignCategory(baseProject.getDesignCategory());
        baseProjectVo.setConstructionUnit(baseProject.getConstructionUnit());
        baseProjectVo.setContacts(baseProject.getContacts());
        baseProjectVo.setContactNumber(baseProject.getContactNumber());
        baseProjectVo.setCustomerName(baseProject.getCustomerName());
        baseProjectVo.setSubject(baseProject.getSubject());
        baseProjectVo.setCustomerPhone(baseProject.getCustomerPhone());
        baseProjectVo.setConstructionOrganization(baseProject.getConstructionOrganization());
        baseProjectVo.setProjectNature(baseProject.getProjectNature());
        baseProjectVo.setProjectCategory(baseProject.getProjectCategory());
        baseProjectVo.setWaterAddress(baseProject.getWaterAddress());
        baseProjectVo.setWaterSupplyType(baseProject.getWaterSupplyType());
        baseProjectVo.setThisDeclaration(baseProject.getThisDeclaration());
        baseProjectVo.setAgent(baseProject.getAgent());
        baseProjectVo.setAgentPhone(baseProject.getAgentPhone());
        baseProjectVo.setApplicationDate(baseProject.getApplicationDate());
        baseProjectVo.setBusinessLocation(baseProject.getBusinessLocation());
        baseProjectVo.setBusinessTypes(baseProject.getBusinessTypes());
        baseProjectVo.setAB(baseProject.getAB());
        baseProjectVo.setWaterUse(baseProject.getWaterUse());
        baseProjectVo.setFireTableSize(baseProject.getFireTableSize());
        baseProjectVo.setClassificationCaliber(baseProject.getClassificationCaliber());
        baseProjectVo.setWaterMeterDiameter(baseProject.getWaterMeterDiameter());
        baseProjectVo.setSite(baseProject.getSite());
        baseProjectVo.setSystemNumber(baseProject.getSystemNumber());
        baseProjectVo.setProposer(baseProject.getProposer());
        baseProjectVo.setApplicationNumber(baseProject.getApplicationNumber());


        baseProjectVo.setRemarkes(applicationInformation.getRemarkes());


        baseProjectVo.setTotalPaymentAmount(totalPayment.getTotalPaymentAmount());
        baseProjectVo.setCumulativeNumberPayment(totalPayment.getCumulativeNumberPayment());
        baseProjectVo.setAccumulativePaymentProportion(totalPayment.getAccumulativePaymentProportion());


        baseProjectVo.setCurrentPaymentInformation(paymentInformation.getCurrentPaymentInformation());
        baseProjectVo.setCumulativePaymentTimes(paymentInformation.getCumulativePaymentTimes());
        baseProjectVo.setCurrentPaymentRatio(paymentInformation.getCurrentPaymentRatio());
        baseProjectVo.setCurrentPeriodAccording(paymentInformation.getCurrentPeriodAccording());
        baseProjectVo.setContractAmount(paymentInformation.getContractAmount());
        baseProjectVo.setProjectType(paymentInformation.getProjectType());
        baseProjectVo.setReceivingTime(paymentInformation.getReceivingTime());
        baseProjectVo.setCompileTime(paymentInformation.getCompileTime());
        baseProjectVo.setOutsourcing(paymentInformation.getOutsourcing());
        baseProjectVo.setNameOfCostUnit(paymentInformation.getNameOfCostUnit());
        baseProjectVo.setContact(paymentInformation.getContact());
        baseProjectVo.setContactPhone(paymentInformation.getContactPhone());
        baseProjectVo.setAmountOutsourcing(paymentInformation.getAmountOutsourcing());
        baseProjectVo.setSituation(paymentInformation.getSituation());
        baseProjectVo.setRemarkes(paymentInformation.getRemarkes());
        return baseProjectVo;
    }

    //修改进度款
    @Override
    public void updateProgress(BaseProjectVo baseProject) {
        //项目基本信息
        Example example2 = new Example(BaseProject.class);
        example2.createCriteria().andEqualTo("projectNum",baseProject.getProjectNum());
        BaseProject project = baseProjectDao.selectOneByExample(example2);
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = new ProgressPaymentInformation();
        //审核表
        AuditInfo auditInfo = new AuditInfo();
        if (baseProject != null && !baseProject.equals("")) {
            if (baseProject.getAuditNumber() != null && !baseProject.getAuditNumber().equals("")) {
                if (baseProject.getAuditNumber().equals("1")){
                    project.setAuditNumber("1");
                    baseProjectDao.updateByPrimaryKey(project);
                    auditInfo.setBaseProjectId(baseProject.getId());
                    auditInfo.setAuditResult("0");
                    auditInfo.setAuditorId(baseProject.getAuditorId());
                    auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo.setAuditType("1");
                    auditInfoDao.insert(auditInfo);
                }else if(baseProject.getAuditNumber().equals("2")){
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",baseProject.getId());
                    List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
                    for (AuditInfo info : auditInfos) {
                        if (info.getAuditResult().equals("0")){
                            info.setAuditResult("1");
                            auditInfoDao.updateByPrimaryKeySelective(info);
                            break;
                        }
                    }
                }
            } else {
                    project.setProgressPaymentStatus("2");
                    baseProjectDao.updateByPrimaryKeySelective(project);
            }


            information.setRemarkes(baseProject.getRemarkes());
            Example example = new Example(ApplicationInformation.class);
            example.createCriteria().andEqualTo("progressPaymentId",paymentInformation.getId());
            applicationInformationDao.updateByExampleSelective(information,example);


            payment.setTotalPaymentAmount(baseProject.getTotalPaymentAmount());
            payment.setCumulativeNumberPayment(baseProject.getCumulativeNumberPayment());
            payment.setAccumulativePaymentProportion(baseProject.getAccumulativePaymentProportion());
            Example example1 = new Example(ProgressPaymentTotalPayment.class);
            example1.createCriteria().andEqualTo("progressPaymentId",paymentInformation.getId());
            progressPaymentTotalPaymentDao.updateByExampleSelective(payment,example1);

            paymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
            paymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
            paymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
            paymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
            paymentInformation.setContractAmount(baseProject.getContractAmount());
            paymentInformation.setProjectType(baseProject.getProjectType());
            paymentInformation.setReceivingTime(baseProject.getReceivingTime());
            paymentInformation.setCompileTime(baseProject.getCompileTime());
            paymentInformation.setOutsourcing(baseProject.getOutsourcing());
            paymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
            paymentInformation.setContact(baseProject.getContact());
            paymentInformation.setContactPhone(baseProject.getContactPhone());
            paymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
            paymentInformation.setSituation(baseProject.getSituation());
            paymentInformation.setRemarkes(baseProject.getRemarkes());
            Example example3 = new Example(ProgressPaymentInformation.class);
            example3.createCriteria().andEqualTo("progressPaymentId",paymentInformation.getId());
            progressPaymentInformationDao.updateByExampleSelective(paymentInformation,example3);
        }
    }



    @Override
    public void batchReview(BatchReviewVo batchReviewVo) {
        batchReviewVo.getBatchAll();
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            Example example = new Example(ProgressPaymentInformation.class);
            example.createCriteria().andEqualTo("baseProjectId",s).andEqualTo("auditResult","0");
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
                    auditInfo1.setBaseProjectId(s);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("member_role_id","3");
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
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            }
        }
    }



    @Override
    public List<BaseProject> findBaseProject(String name) {
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        if (name.equals(" ") || name==null){
            List<BaseProject> baseProjects = baseProjectDao.selectAll();
            return baseProjects;
        }
        c.andLike("projectNum","%"+name+"%");
        List<BaseProject> baseProjects = baseProjectDao.selectByExample(example);
        return baseProjects;
    }

    @Override
    public List<BaseProject> findAllBaseProject() {
        return baseProjectDao.selectAll();
    }

    @Override
    public BaseProject findById(String projectNum) {
        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("projectNum",projectNum);
        return baseProjectDao.selectOneByExample(example);
    }

    @Override
    public NumberVo NumberItems() {
        NumberVo numberVo = new NumberVo();
        int i = baseProjectDao.selectCountByExample(null);
        numberVo.setTotalNumberOfProjects(i);


        int ii = 0;
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("desginStatus","1");
        int i1 = baseProjectDao.selectCountByExample(example);
        ii+=i1;

        Example example2 = new Example(BaseProject.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("budgetStatus","1");
        int i2 = baseProjectDao.selectCountByExample(example2);
        ii+=i2;

        Example example3 = new Example(BaseProject.class);
        Example.Criteria c3 = example3.createCriteria();
        c3.andEqualTo("trackStatus","1");
        int i3 = baseProjectDao.selectCountByExample(example3);
        ii+=i3;


        Example example5 = new Example(BaseProject.class);
        Example.Criteria c5 = example5.createCriteria();
        c5.andEqualTo("visaStatus","1");
        int i5 = baseProjectDao.selectCountByExample(example5);
        ii+=i5;

        Example example6 = new Example(BaseProject.class);
        Example.Criteria c6 = example6.createCriteria();
        c6.andEqualTo("progressPaymentStatus","1");
        int i6 = baseProjectDao.selectCountByExample(example6);
        ii+=i6;

        Example example7 = new Example(BaseProject.class);
        Example.Criteria c7 = example7.createCriteria();
        c7.andEqualTo("settleAccountsStatus","1");
        int i7 = baseProjectDao.selectCountByExample(example7);
        ii+=i7;
        numberVo.setTotalNumberOfApproval(ii);




        int iii = 0;
        Example aexample = new Example(BaseProject.class);
        Example.Criteria ac = aexample.createCriteria();
        ac.andEqualTo("desginStatus","2");
        int ai1 = baseProjectDao.selectCountByExample(aexample);
        iii+=ai1;

        Example aexample2 = new Example(BaseProject.class);
        Example.Criteria ac2 = aexample2.createCriteria();
        ac2.andEqualTo("budgetStatus","2");
        int ai2 = baseProjectDao.selectCountByExample(aexample2);
        iii+=ai2;

        Example aexample3 = new Example(BaseProject.class);
        Example.Criteria ac3 = aexample3.createCriteria();
        ac3.andEqualTo("trackStatus","3");
        int ai3 = baseProjectDao.selectCountByExample(aexample3);
        iii+=ai3;



        Example aexample5 = new Example(BaseProject.class);
        Example.Criteria ac5 = aexample5.createCriteria();
        ac5.andEqualTo("visaStatus","5");
        int ai5 = baseProjectDao.selectCountByExample(aexample5);
        iii+=ai5;

        Example aexample6 = new Example(BaseProject.class);
        Example.Criteria ac6 = aexample6.createCriteria();
        ac6.andEqualTo("progressPaymentStatus","2");
        int ai6 = baseProjectDao.selectCountByExample(aexample6);
        iii+=ai6;

        Example aexample7 = new Example(BaseProject.class);
        Example.Criteria ac7 = aexample7.createCriteria();
        ac7.andEqualTo("settleAccountsStatus","2");
        int ai7 = baseProjectDao.selectCountByExample(aexample7);
        iii+=ai7;
        numberVo.setProjectTotal(iii);



        int iiii = 0;
        Example cexample = new Example(BaseProject.class);
        Example.Criteria cc = cexample.createCriteria();
        cc.andEqualTo("desginStatus","2");
        int ci1 = baseProjectDao.selectCountByExample(cexample);
        iiii+=ci1;

        Example cexample2 = new Example(BaseProject.class);
        Example.Criteria cc2 = cexample2.createCriteria();
        cc2.andEqualTo("budgetStatus","2");
        int ci2 = baseProjectDao.selectCountByExample(cexample2);
        iiii+=ci2;

        Example cexample3 = new Example(BaseProject.class);
        Example.Criteria cc3 = cexample3.createCriteria();
        cc3.andEqualTo("trackStatus","3");
        int ci3 = baseProjectDao.selectCountByExample(cexample3);
        iiii+=ci3;



        Example cexample5 = new Example(BaseProject.class);
        Example.Criteria cc5 = cexample5.createCriteria();
        cc5.andEqualTo("visaStatus","5");
        int ci5 = baseProjectDao.selectCountByExample(cexample5);
        iiii+=ci5;

        Example cexample6 = new Example(BaseProject.class);
        Example.Criteria cc6 = cexample6.createCriteria();
        cc6.andEqualTo("progressPaymentStatus","2");
        int ci6 = baseProjectDao.selectCountByExample(aexample6);
        iiii+=ci6;

        Example cexample7 = new Example(BaseProject.class);
        Example.Criteria cc7 = aexample7.createCriteria();
        cc7.andEqualTo("settleAccountsStatus","2");
        int ci7 = baseProjectDao.selectCountByExample(cexample7);
        iiii+=ci7;
        numberVo.setTotalNumberOfCompleted(iiii);


        return numberVo;
    }

    @Override
    public List<ProgressListVo> searchAllProgress(PageVo pageVo) {
        List<ProgressListVo> progressListVos = progressPaymentInformationDao.searchAllProgress(pageVo);
        return progressPaymentInformationDao.searchAllProgress(pageVo);
    }

    @Override
    public void deleteProgress(String id) {
        ProgressPaymentInformation progressPaymentInformation = new ProgressPaymentInformation();
        progressPaymentInformation.setDelFlag("1");
        progressPaymentInformation.setId(id);
        progressPaymentInformationDao.updateByPrimaryKeySelective(progressPaymentInformation);

        Example example = new Example(ProgressPaymentTotalPayment.class);
        example.createCriteria().andEqualTo("progressPaymentId",id);
        ProgressPaymentTotalPayment progressPaymentTotalPayment = new ProgressPaymentTotalPayment();
        progressPaymentTotalPayment.setDelFlag("1");
        progressPaymentTotalPaymentDao.updateByExampleSelective(progressPaymentTotalPayment,example);

        Example example1 = new Example(ApplicationInformation.class);
        example1.createCriteria().andEqualTo("progressPaymentId",id);
        ApplicationInformation applicationInformation = new ApplicationInformation();
        applicationInformation.setDelFlag("1");
        applicationInformationDao.updateByExampleSelective(applicationInformation,example1);

        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId",id);
        auditInfoDao.deleteByExample(example2);
    }


}

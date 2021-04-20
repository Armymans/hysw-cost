package net.zlw.cloud.budgeting.service.impl;

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
import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.designProject.service.ProjectSumService;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.index.mapper.MessageNotificationDao;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private OutSourceMapper outSourceMapper;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private BaseProjectDao projectDao;
    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;
    @Resource
    private SurveyInformationDao surveyInformationDao;
    @Resource
    private CostPreparationDao costPreparationDao;
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;
    @Resource
    private MemberService memberService;
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
    private ProjectSumService projectSumService;

    @Resource
    private OperationLogDao operationLogDao;

    @Resource
    private EmployeeAchievementsInfoMapper employeeAchievementsInfoMapper;
    @Autowired
    private DesignInfoMapper designInfoMapper;
    @Autowired
    private InComeMapper inComeMapper;

    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;

    @Resource
    private VisaChangeMapper visaChangeMapper;
    @Resource
    private RemindSetMapper remindSetMapper;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageNotificationDao messageNotificationDao;
    @Resource
    private MkyUserMapper mkyUserMapper;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private BudgetCoverService budgetCoverService;
    @Resource
    private AnhuiMoneyinfoMapper anhuiMoneyinfoMapper;
    @Resource
    private WujiangMoneyInfoMapper wujiangMoneyInfoMapper;
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;





    @Value("${audit.wujiang.sheji.designHead}")
    private String wjsjh;
    @Value("${audit.wujiang.sheji.designManager}")
    private String wjsjm;
    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh;
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm;

    @Value("${audit.wuhu.sheji.designHead}")
    private String whsjh;
    @Value("${audit.wuhu.sheji.designManager}")
    private String whsjm;
    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh;
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm;


    @Override
    public void addBudgeting(BudgetingVo budgetingVo, UserInfo loginUser, HttpServletRequest request) {

        if (budgetingVo.getRevenue()!=null && !"".equals(budgetingVo.getRevenue())){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetingVo.getBaseId());
            Example example1 = new Example(DesignInfo.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("baseProjectId",baseProject.getId());
            criteria.andEqualTo("status","0");
            DesignInfo designInfo = designInfoMapper.selectOneByExample(example1);
            if ("4".equals(baseProject.getDistrict())){

                Example example = new Example(WujiangMoneyInfo.class);
                Example.Criteria cc = example.createCriteria();
                cc.andEqualTo("baseProjectId",designInfo.getId());
                cc.andEqualTo("status","0");
                WujiangMoneyInfo wujiangMoneyInfo1 = wujiangMoneyInfoMapper.selectOneByExample(example);
                if (wujiangMoneyInfo1 == null){
                    WujiangMoneyInfo wujiangMoneyInfo = new WujiangMoneyInfo();
                    wujiangMoneyInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    wujiangMoneyInfo.setRevenue(new BigDecimal(budgetingVo.getRevenue()));
                    wujiangMoneyInfo.setStatus("0");
                    wujiangMoneyInfo.setBaseProjectId(designInfo.getId());
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    wujiangMoneyInfo.setCreateTime(s.format(new Date()));
                    wujiangMoneyInfoMapper.insertSelective(wujiangMoneyInfo);
                }

            }else{
                Example example = new Example(AnhuiMoneyinfo.class);
                Example.Criteria cc = example.createCriteria();
                cc.andEqualTo("baseProjectId",designInfo.getId());
                cc.andEqualTo("status","0");
                AnhuiMoneyinfo anhuiMoneyinfo1 = anhuiMoneyinfoMapper.selectOneByExample(example);
                if (anhuiMoneyinfo1==null){
                    AnhuiMoneyinfo anhuiMoneyinfo = new AnhuiMoneyinfo();
                    anhuiMoneyinfo.setId(UUID.randomUUID().toString().replace("-",""));
                    anhuiMoneyinfo.setBaseProjectId(designInfo.getId());
                    anhuiMoneyinfo.setRevenue(new BigDecimal(budgetingVo.getRevenue()));
                    anhuiMoneyinfo.setStatus("0");
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    anhuiMoneyinfo.setCreateTime(s.format(new Date()));
                    anhuiMoneyinfoMapper.insertSelective(anhuiMoneyinfo);
                }

            }
        }

        //获取基本信息
        Example example = new Example(BaseProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",budgetingVo.getBaseId());
        BaseProject baseProject = projectDao.selectOneByExample(example);
        Example memExample = new Example(MemberManage.class);
        memExample.createCriteria().andEqualTo("memberName",budgetingVo.getBudgetingPeople().trim());
        MemberManage peopleName = memberManageDao.selectOneByExample(memExample);
        //预算编制
        Budgeting budgeting = new Budgeting();
        budgeting.setId(UUID.randomUUID().toString().replace("-",""));
        budgeting.setAmountCost(budgetingVo.getAmountCost());
        if ("".equals(budgeting.getBudgetingPeople())){
            budgeting.setBudgetingPeople(loginUser.getId());
        }else{
            budgeting.setBudgetingPeople(peopleName.getId());
        }
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
        budgeting.setFounderId(loginUser.getId());
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
            auditInfo.setAuditorId(budgetingVo.getAuditorId() );
            auditInfoDao.insertSelective(auditInfo);

            //消息通知
            String username = loginUser.getUsername();
            MessageVo messageVo = new MessageVo();
            String projectName = baseProject.getProjectName();
                String id1 = budgetingVo.getAuditorId();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(id1);
                //审核人名字
                String name = memberManage.getMemberName();
                messageVo.setId("A06");
                messageVo.setUserId(id1);
                messageVo.setType("1"); // 1 通知
                messageVo.setTitle("您有一个设计项目待审批！");
                messageVo.setDetails(name+"您好！【"+username+"】已将【"+projectName+"】的设计项目提交给您，请审批！");
                //调用消息Service
                messageService.sendOrClose(messageVo);

            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(userId); //当前登陆人
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("3"); //预算项目
            operationLog.setContent(memberManage1.getMemberName()+"新增提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(budgeting.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            //保存
        }else{
            //修改预算状态为处理中
            baseProject.setBudgetStatus("2");
            baseProject.setProjectFlow(baseProject.getProjectFlow()+",2");
            projectDao.updateByPrimaryKeySelective(baseProject);
            budgetingDao.insertSelective(budgeting);
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(userId); //当前登陆人
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("3"); //预算项目
            operationLog.setContent(memberManage1.getMemberName()+"新增保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(budgeting.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
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
        surveyInformation.setFounderId(loginUser.getId());
        surveyInformationDao.insertSelective(surveyInformation);

        //成本编制
        String costName = memberManageDao.findNameById(budgetingVo.getCostTogether());
        CostPreparation costPreparation = new CostPreparation();
        costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
        costPreparation.setCostTotalAmount(budgetingVo.getCostTotalAmount());
        costPreparation.setVatAmount(budgetingVo.getCVatAmount());
        costPreparation.setTotalPackageMaterial(budgetingVo.getTotalPackageMaterial());
        costPreparation.setOutsourcingCostAmount(budgetingVo.getOutsourcingCostAmount());
        costPreparation.setOtherCost1(budgetingVo.getOtherCost1());
        costPreparation.setOtherCost2(budgetingVo.getOtherCost2());
        costPreparation.setOtherCost3(budgetingVo.getOtherCost3());
        if (!"".equals(budgetingVo.getCostTogether())){
            costPreparation.setCostTogether(costName);
        }else{
            costPreparation.setCostTogether(loginUser.getId());
        }
        costPreparation.setReceivingTime(budgetingVo.getReceivingTime());
        costPreparation.setCostPreparationTime(budgetingVo.getCostPreparationTime());
        costPreparation.setRemarkes(budgetingVo.getCRemarkes());
        costPreparation.setBudgetingId(budgeting.getId());
        costPreparation.setDelFlag("0");
        costPreparation.setBaseProjectId(baseProject.getId());
        costPreparation.setFounderId(loginUser.getId());
        costPreparationDao.insertSelective(costPreparation);

        //控价编制
        VeryEstablishment veryEstablishment = new VeryEstablishment();
        String memBerId = memberManageDao.findNameById(budgetingVo.getPricingTogether());
        veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
            veryEstablishment.setBiddingPriceControl(budgetingVo.getBiddingPriceControl());
        veryEstablishment.setVatAmount(budgetingVo.getVVatAmount());
        if (!"".equals(budgetingVo.getPricingTogether())){
            veryEstablishment.setPricingTogether(memBerId);
        }else{
            veryEstablishment.setPricingTogether(loginUser.getId());
        }
        veryEstablishment.setReceivingTime(budgetingVo.getVReceivingTime());
        veryEstablishment.setEstablishmentTime(budgetingVo.getEstablishmentTime());
        veryEstablishment.setRemarkes(budgetingVo.getVRemarkes());
        veryEstablishment.setBudgetingId(budgeting.getId());
        veryEstablishment.setDelFlag("0");
        veryEstablishment.setBaseProjectId(baseProject.getId());
        veryEstablishment.setFounderId(loginUser.getId());
        veryEstablishmentDao.insertSelective(veryEstablishment);

        //修改文件外键
        Example example1 = new Example(FileInfo.class);
        Example.Criteria c = example1.createCriteria();
        c.andLike("type","ysxmxj%");
        c.andEqualTo("status","0");
        c.andEqualTo("platCode",loginUser.getId());
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
        ConstructionUnitManagement unitManagement = constructionUnitManagementMapper.selectByPrimaryKey(baseProject.getConstructionOrganization());
        if (unitManagement != null){
            baseProject.setConstructionOrganization(unitManagement.getConstructionUnitName());
        }
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


        String budgetingPeople = memberManageDao.findIdByName(budgeting.getBudgetingPeople());
        BudgetingVo budgetingVo = new BudgetingVo();
        budgetingVo.setAuditInfo(auditInfo);

        budgetingVo.setId(budgeting.getId());
        budgetingVo.setProjectNum(baseProject.getProjectNum());
        budgetingVo.setAmountCost(budgeting.getAmountCost());
        budgetingVo.setBudgetingPeople(budgetingPeople);
        budgetingVo.setAddedTaxAmount(budgeting.getAddedTaxAmount());
        budgetingVo.setBudgetingTime(budgeting.getBudgetingTime());
        budgetingVo.setOutsourcing(budgeting.getOutsourcing());
        budgetingVo.setNameOfCostUnit(budgeting.getNameOfCostUnit());
        budgetingVo.setContact(budgeting.getContact());
        budgetingVo.setContactPhone(budgeting.getContactPhone());
        budgetingVo.setAmountOutsourcing(budgeting.getAmountOutsourcing());
        budgetingVo.setReceiptTime(budgeting.getReceiptTime());
        budgetingVo.setBremarkes(budgeting.getRemarkes());
        //sql查询勘察信息期数
        SurveyInformation surveyInformation1 = surveyInformationDao.selectByOne(id);
        if (surveyInformation1 != null){
            budgetingVo.setInvestigationPersonnel(surveyInformation1.getInvestigationPersonnel());
            budgetingVo.setSurveyBriefly(surveyInformation1.getSurveyBriefly());
            budgetingVo.setPriceInformationName(surveyInformation1.getPriceInformationName());
            budgetingVo.setPriceInformationNper(surveyInformation1.getPriceInformationNper());
            if (surveyInformation1.getSurveyDate() != null && !"".equals(surveyInformation1)){
                budgetingVo.setSurveyDate(surveyInformation1.getSurveyDate());
            }
        }
        //控价编制人
        String costPeople = memberManageDao.findIdByName(costPreparation.getCostTogether());
        //控价编制人
        String pricePeople = memberManageDao.findIdByName(veryEstablishment.getPricingTogether());
        budgetingVo.setCostTotalAmount(costPreparation.getCostTotalAmount());
        budgetingVo.setCVatAmount(costPreparation.getVatAmount());
        budgetingVo.setTotalPackageMaterial(costPreparation.getTotalPackageMaterial());
        budgetingVo.setOutsourcingCostAmount(costPreparation.getOutsourcingCostAmount());
        budgetingVo.setOtherCost1(costPreparation.getOtherCost1());
        budgetingVo.setOtherCost2(costPreparation.getOtherCost2());
        budgetingVo.setOtherCost3(costPreparation.getOtherCost3());
        budgetingVo.setCostTogether(costPeople);
        budgetingVo.setReceivingTime(costPreparation.getReceivingTime());
        budgetingVo.setCostPreparationTime(costPreparation.getCostPreparationTime());
        budgetingVo.setCRemarkes(costPreparation.getRemarkes());
        budgetingVo.setBiddingPriceControl(veryEstablishment.getBiddingPriceControl());
        budgetingVo.setVVatAmount(veryEstablishment.getVatAmount());
        budgetingVo.setPricingTogether(pricePeople);
        budgetingVo.setVReceivingTime(veryEstablishment.getReceivingTime());
        budgetingVo.setEstablishmentTime(veryEstablishment.getEstablishmentTime());
        budgetingVo.setVRemarkes(veryEstablishment.getRemarkes());

        if (budgetingVo.getAuditInfo()!=null){

            if (loginUser.getId().equals(budgetingVo.getAuditInfo().getAuditorId())){
                budgetingVo.setCheckHidden("0");
            } else {
                budgetingVo.setCheckHidden("1");
            }
        }

        MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingVo.getCostTogether());
        if (mkyUser!=null){
            budgetingVo.setCostPeople(mkyUser.getUserName());
        }
        MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingVo.getPricingTogether());
        if (mkyUser1!=null){
            budgetingVo.setPricingPeople(mkyUser1.getUserName());
        }
        MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingVo.getBudgetingPeople());
        if (mkyUser2!=null){
            budgetingVo.setButPeople(mkyUser2.getUserName());
        }

        return budgetingVo;
    }

    @Override
    public void updateBudgeting(BudgetingVo budgetingVo,UserInfo loginUser,HttpServletRequest request) {

        if (budgetingVo.getRevenue()!=null && !"".equals(budgetingVo.getRevenue()) && !"0".equals(budgetingVo.getRevenue())){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetingVo.getBaseId());
            Example example1 = new Example(DesignInfo.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("baseProjectId",baseProject.getId());
            criteria.andEqualTo("status","0");
            DesignInfo designInfo = designInfoMapper.selectOneByExample(example1);
            if ("4".equals(baseProject.getDistrict())){

                Example example = new Example(WujiangMoneyInfo.class);
                Example.Criteria cc = example.createCriteria();
                cc.andEqualTo("baseProjectId",designInfo.getId());
                cc.andEqualTo("status","0");
                WujiangMoneyInfo wujiangMoneyInfo1 = wujiangMoneyInfoMapper.selectOneByExample(example);
                if (wujiangMoneyInfo1 == null){
                    WujiangMoneyInfo wujiangMoneyInfo = new WujiangMoneyInfo();
                    wujiangMoneyInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    wujiangMoneyInfo.setRevenue(new BigDecimal(budgetingVo.getRevenue()));
                    wujiangMoneyInfo.setStatus("0");
                    wujiangMoneyInfo.setBaseProjectId(designInfo.getId());
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    wujiangMoneyInfo.setCreateTime(s.format(new Date()));
                    wujiangMoneyInfoMapper.insertSelective(wujiangMoneyInfo);
                }

            }else{
                Example example = new Example(AnhuiMoneyinfo.class);
                Example.Criteria cc = example.createCriteria();
                cc.andEqualTo("baseProjectId",designInfo.getId());
                cc.andEqualTo("status","0");
                AnhuiMoneyinfo anhuiMoneyinfo1 = anhuiMoneyinfoMapper.selectOneByExample(example);
                if (anhuiMoneyinfo1==null){
                    AnhuiMoneyinfo anhuiMoneyinfo = new AnhuiMoneyinfo();
                    anhuiMoneyinfo.setId(UUID.randomUUID().toString().replace("-",""));
                    anhuiMoneyinfo.setBaseProjectId(designInfo.getId());
                    anhuiMoneyinfo.setRevenue(new BigDecimal(budgetingVo.getRevenue()));
                    anhuiMoneyinfo.setStatus("0");
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    anhuiMoneyinfo.setCreateTime(s.format(new Date()));
                    anhuiMoneyinfoMapper.insertSelective(anhuiMoneyinfo);
                }
            }
        }

        //获取基本信息
        System.err.println(budgetingVo.getBaseId());
        Example example = new Example(BaseProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",budgetingVo.getBaseId());
        BaseProject baseProject = projectDao.selectOneByExample(example);
        //预算编制
        String nameById = memberManageDao.findNameById(budgetingVo.getBudgetingPeople());
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(budgetingVo.getId());
        budgeting.setAmountCost(budgetingVo.getAmountCost());
        budgeting.setBudgetingPeople(nameById);
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
                //判断是否为退回提交
                String budgetStatus = baseProject.getBudgetStatus();
                if ("6".equals(budgetStatus)){
                    //若是则删除所有审核信息
                    Example example1 = new Example(AuditInfo.class);
                    Example.Criteria c = example1.createCriteria();
                    c.andEqualTo("baseProjectId",budgeting.getId());
                    c.andEqualTo("status","0");
                    List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                    for (AuditInfo auditInfo : auditInfos) {
                        auditInfoDao.deleteByPrimaryKey(auditInfo);
                    }

                   budgeting.setWhetherAccount("1");

                }

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
                        budgetingVo.setAuditorId(info.getAuditorId());
                        info.setAuditResult("0");
                        info.setAuditOpinion("");
                        info.setAuditTime("");
                        baseProject.setBudgetStatus("1");
                        projectDao.updateByPrimaryKeySelective(baseProject);
                        auditInfoDao.updateByPrimaryKeySelective(info);
                    }
                }
            }
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("3"); //预算项目
            operationLog.setContent(memberManage.getMemberName()+"编辑提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(budgeting.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }else{
            projectDao.updateByPrimaryKeySelective(baseProject);
            budgetingDao.updateByPrimaryKeySelective(budgeting);
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("3"); //预算项目
            operationLog.setContent(memberManage.getMemberName()+"编辑保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(budgeting.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
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
        if (StringUtils.isEmpty(budgetingVo.getCostTogether())){
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(loginUser.getId());
            budgetingVo.setCostTogether(memberManage.getMemberName());
        }
        String costId = memberManageDao.findNameById(budgetingVo.getCostTogether());
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
        costPreparation.setCostTogether(costId);
        costPreparation.setReceivingTime(budgetingVo.getReceivingTime());
        costPreparation.setCostPreparationTime(budgetingVo.getCostPreparationTime());
        costPreparation.setRemarkes(budgetingVo.getCRemarkes());
        costPreparation.setBudgetingId(budgeting.getId());
        costPreparationDao.updateByPrimaryKeySelective(costPreparation);

        //控价编制
        Example example3 = new Example(VeryEstablishment.class);
        if (StringUtils.isEmpty(budgetingVo.getPricingTogether())){
            budgetingVo.setPricingTogether(budgetingVo.getCostTogether());
        }
        String priceToger = memberManageDao.findNameById(budgetingVo.getPricingTogether());
        example3.createCriteria().andEqualTo("budgetingId",budgetingVo.getId());
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example3);
        veryEstablishment.setBiddingPriceControl(budgetingVo.getBiddingPriceControl());
        veryEstablishment.setVatAmount(budgetingVo.getVVatAmount());
        veryEstablishment.setPricingTogether(priceToger);
        veryEstablishment.setReceivingTime(budgetingVo.getVReceivingTime());
        veryEstablishment.setEstablishmentTime(budgetingVo.getEstablishmentTime());
        veryEstablishment.setRemarkes(budgetingVo.getVRemarkes());
        veryEstablishment.setBudgetingId(budgeting.getId());

        veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);

        //消息通知
        if (budgetingVo.getAuditNumber()!=null && !budgetingVo.getAuditNumber().equals("")){
            String username = loginUser.getUsername();
            String projectName = baseProject.getProjectName();
            String id1 = budgetingVo.getAuditorId();
            //审核人名字
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(id1);
            MessageVo messageVo = new MessageVo();
            String name = memberManage.getMemberName();
            messageVo.setId("A07");
            messageVo.setUserId(id1);
            messageVo.setType("1"); // 提醒
            messageVo.setTitle("您有一个预算项目待审批！");
            messageVo.setDetails(name+"您好！【"+username+"】已将【"+projectName+"】的设计项目提交给您，请审批！");
            //调用消息Service
            messageService.sendOrClose(messageVo);
        }



    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser,HttpServletRequest request) {
        //登录人id
        String id = loginUser.getId();
        //登录人名字
        String username = loginUser.getUsername();
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
//                        Example example1 = new Example(MemberManage.class);
//                        Example.Criteria c = example1.createCriteria();
//                        c.andEqualTo("depId","2");
//                        c.andEqualTo("depAdmin","1");
//                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
                        String founderId = budgeting.getFounderId();
                        Example example1 = new Example(MemberManage.class);
                        Example.Criteria cc = example1.createCriteria();
                        cc.andEqualTo("id",founderId);
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        //1芜湖
                        if (memberManage.getWorkType().equals("1")){
                            twoBatch.setAuditorId(whzjh);
                            //吴江
                        }else if (memberManage.getWorkType().equals("2")){
                            twoBatch.setAuditorId(wjzjh);
                        }

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

                        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);

                        //将状态改为待确认
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                        baseProject.setBudgetStatus("5");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);



//                            AuditInfo auditInfo1 = new AuditInfo();
//                            auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
//                            auditInfo1.setBaseProjectId(s);
//                            auditInfo1.setAuditResult("0");
//                            auditInfo1.setAuditType("4");
////                            Example example1 = new Example(MemberManage.class);
////                            Example.Criteria c = example1.createCriteria();
////                            c.andEqualTo("memberRoleId","2");
////                            MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                        String founderId = budgeting.getFounderId();
//
//                        Example example1 = new Example(MemberManage.class);
//                        Example.Criteria cc = example1.createCriteria();
//                        cc.andEqualTo("id",founderId);
//                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                        //1芜湖
//                        if (memberManage.getWorkType().equals("1")){
//                            auditInfo1.setAuditorId(whzjm);
//                            //吴江
//                        }else if (memberManage.getWorkType().equals("2")){
//                            auditInfo1.setAuditorId(wjzjm);
//                        }
////                            auditInfo1.setAuditorId(memberManage.getId());
//                            auditInfo1.setStatus("0");
//                            auditInfo1.setCreateTime(sim.format(new Date()));
//                            auditInfoDao.insertSelective(auditInfo1);

                        //三审通过
                    }else if(auditInfo.getAuditResult().equals("0") && auditInfo.getAuditType().equals("4")){
                        Budgeting budgeting1 = budgetingDao.selectByPrimaryKey(s);
                        BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(budgeting1.getBaseProjectId());
                        if (baseProject1.getDistrict() == null || baseProject1.getDistrict().equals("")){
                            budgeting1.setAttributionShow("2");
                        }
                        if (baseProject1.getDesignCategory() == null || baseProject1.getDesignCategory().equals("")){
                            budgeting1.setAttributionShow("2");
                        }
                        Example example2 = new Example(CostPreparation.class);
                        Example.Criteria cc = example2.createCriteria();
                        cc.andEqualTo("budgetingId",budgeting1.getId());
                        cc.andEqualTo("delFlag","0");
                        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example2);

                        Example example3 = new Example(VeryEstablishment.class);
                        Example.Criteria cc1 = example3.createCriteria();
                        cc1.andEqualTo("budgetingId",budgeting1.getId());
                        cc1.andEqualTo("delFlag","0");
                        VeryEstablishment veryEstablishment1 = veryEstablishmentDao.selectOneByExample(example3);

                        String budgetingPeople = budgeting1.getBudgetingPeople();
                        String costTogether = costPreparation.getCostTogether();
                        String pricingTogether = veryEstablishment1.getPricingTogether();

                        if (budgetingPeople == null || budgetingPeople.equals("")){
                            budgeting1.setAttributionShow("2");
                        }

                        if (costTogether == null || costTogether.equals("")){
                            budgeting1.setAttributionShow("2");
                        }

                        if (pricingTogether == null || pricingTogether.equals("")){
                            budgeting1.setAttributionShow("2");
                        }

                        if ( !"2".equals(budgeting1.getAttributionShow())){
                            budgeting1.setAttributionShow("1");
                        }
                        budgetingDao.updateByPrimaryKeySelective(budgeting1);



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
                        // 加入委外信息
                        OutSource outSource = new OutSource();
                        outSource.setId(UUID.randomUUID().toString().replaceAll("-",""));
                        if ("1".equals(budgeting.getOutsourcing())){
                            outSource.setOutMoney(budgeting.getAmountOutsourcing().toString());
                        }else {
                            outSource.setOutMoney("0");
                        }
                        outSource.setDistrict(baseProject.getDistrict());
                        outSource.setDept("2"); //1.设计 2.造价
                        outSource.setDelFlag("0"); //0.正常 1.删除
                        outSource.setOutType("2"); // 预算委外金额
                        outSource.setBaseProjectId(baseProject.getId()); //基本信息表外键
                        outSource.setProjectNum(budgeting.getId()); //设计信息外键
                        outSource.setCreateTime(sim.format(new Date()));
                        outSource.setUpdateTime(sim.format(new Date()));
                        outSource.setFounderId(budgeting.getFounderId()); //项目创建人
                        outSource.setFounderCompanyId(budgeting.getFounderCompanyId()); //公司
                        outSourceMapper.insertSelective(outSource);
                        Example example1 = new Example(VeryEstablishment.class);
                        example1.createCriteria().andEqualTo("budgetingId",budgeting.getId());
                        //招标控制价
                        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example1);
                            //总金额
                            BigDecimal total6 = new BigDecimal(0);
                            if(!"4".equals(baseProject.getDistrict())){
                                //预算编制造价咨询金额
                                Double money = projectSumService.anhuiBudgetingMoney(budgeting.getAmountCost());
                                money = (double)Math.round(money*100)/100;
                                Double money1 = projectSumService.anhuiBudgetingMoney(veryEstablishment.getBiddingPriceControl());//                        total3 = total3.add(new BigDecimal(money1));
                                money1 = (double)Math.round(money1*100)/100;
                                //预算编制咨询费计算基数
                                Double aDouble = projectSumService.BudgetingBase(money, money1);
                                aDouble = (double)Math.round(aDouble*100)/100;
                                //计算基数和
                                //预算编制技提
                                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                                aDouble1 = (double)Math.round(aDouble1*100)/100;
                                //计提和
                                total6 = total6.add(new BigDecimal(aDouble1));
                                BigDecimal actualAmount = total6.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);

                                // 预算收入 存入收入表
                                InCome inCome = new InCome();
                                inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                                inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                                inCome.setIncomeType("2"); // 预算编制咨询费
                                inCome.setDistrict(baseProject.getDistrict());
                                inCome.setDept("2"); // 1 设计 2 造价
                                inCome.setDelFlag("0");
                                inCome.setBaseProjectId(baseProject.getId());
                                inCome.setProjectNum(budgeting.getId());
                                inCome.setCreateTime(sim.format(new Date()));
                                inCome.setUpdateTime(sim.format(new Date()));
                                inCome.setFounderId(budgeting.getFounderId());
//                                inCome.setFounderCompanyId(budgeting.getFounderCompanyId());
                                inComeMapper.insertSelective(inCome);
                                if (veryEstablishment != null){
                                    // 控价信息 存入收入表
                                    InCome inCome1 = new InCome();
                                    inCome1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                                    inCome1.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                                    inCome1.setIncomeType("3"); // 控价编制咨询费
                                    inCome1.setDistrict(baseProject.getDistrict());
                                    inCome1.setDept("2"); // 1 设计 2 造价
                                    inCome1.setDelFlag("0");
                                    inCome1.setBaseProjectId(baseProject.getId());
                                    inCome1.setProjectNum(veryEstablishment.getId());
                                    inCome1.setCreateTime(sim.format(new Date()));
                                    inCome1.setUpdateTime(sim.format(new Date()));
                                    inCome1.setFounderId(veryEstablishment.getFounderId());
//                                    inCome1.setFounderCompanyId(veryEstablishment.getFounderCompanyId());
                                    inComeMapper.insertSelective(inCome1);
                                }
                                //吴江
                            }else{
                                //预算编制造价咨询金额
                                Double money = projectSumService.wujiangBudgetingMoney(budgeting.getAmountCost());
                                money = (double)Math.round(money*100)/100;
                                //预算编制标底咨询金额
                                Double money1 = projectSumService.wujiangBudgetingMoney(veryEstablishment.getBiddingPriceControl());
                                money1 = (double)Math.round(money1*100)/100;
                                //预算编制咨询费计算基数
                                Double aDouble = projectSumService.BudgetingBase(money, money1);
                                aDouble = (double)Math.round(aDouble*100)/100;
                                //预算编制技提
                                Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                                aDouble1 = (double)Math.round(aDouble1*100)/100;
                                //计提和
                                total6 = total6.add(new BigDecimal(aDouble1));
                                BigDecimal actualAmount = total6.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);

                                // 预算收入 存入收入表
                                InCome inCome = new InCome();
                                inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                                inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                                inCome.setIncomeType("2"); // 预算编制咨询费
                                inCome.setDistrict(baseProject.getDistrict());
                                inCome.setDept("2"); // 1 设计 2 造价
                                inCome.setDelFlag("0");
                                inCome.setBaseProjectId(baseProject.getId());
                                inCome.setProjectNum(budgeting.getId());
                                inCome.setCreateTime(sim.format(new Date()));
                                inCome.setUpdateTime(sim.format(new Date()));
                                inCome.setFounderId(budgeting.getFounderId());
//                                inCome.setFounderCompanyId(budgeting.getFounderCompanyId());
                                inComeMapper.insertSelective(inCome);
                                if (veryEstablishment != null){
                                    // 控价信息 存入收入表
                                    InCome inCome1 = new InCome();
                                    inCome1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                                    inCome1.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                                    inCome1.setIncomeType("3"); // 控价编制咨询费4
                                    inCome1.setDistrict(baseProject.getDistrict());
                                    inCome1.setDept("2"); // 1 设计 2 造价
                                    inCome1.setDelFlag("0");
                                    inCome1.setBaseProjectId(baseProject.getId());
                                    inCome1.setProjectNum(veryEstablishment.getId());
                                    inCome1.setCreateTime(sim.format(new Date()));
                                    inCome1.setUpdateTime(sim.format(new Date()));
                                    inCome1.setFounderId(veryEstablishment.getFounderId());
//                                    inCome1.setFounderCompanyId(veryEstablishment.getFounderCompanyId());
                                    inComeMapper.insertSelective(inCome1);
                                }
                            }
                        //获取成员姓名
                        MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                        //如果通过发送消息
                        MessageVo messageVo = new MessageVo();
                        messageVo.setId("A08");
                        messageVo.setUserId(id);
                        messageVo.setType("1"); // 通知
                        messageVo.setTitle("您有一个预算项目审批已通过！");
                        messageVo.setDetails(username + "您好！您提交的【" + baseProject.getProjectName() + "】的预算项目【" + memberManage1.getMemberName() + "】已审批通过！");
                        //调用消息Service
                        messageService.sendOrClose(messageVo);
                    }
                    // 操作日志
                    String userId = loginUser.getId();
                    Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    operationLog.setName(userId);
                    operationLog.setType("3"); //预算项目
                    operationLog.setContent(memberManage.getMemberName()+"审核通过了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
                    operationLog.setDoObject(budgeting.getId()); // 项目标识
                    operationLog.setStatus("0");
                    operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    String ip = memberService.getIp(request);
                    operationLog.setIp(ip);
                    operationLogDao.insertSelective(operationLog);
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

                        //如果未通过发送消息
                        MessageVo messageVo1 = new MessageVo();
                        //审核人名字
                        messageVo1.setId("A08");
                        messageVo1.setUserId(id);
                        messageVo1.setType("1"); // 通知
                        messageVo1.setTitle("您有一个预算项目审批未通过！");
                        messageVo1.setDetails(username + "您好！您提交的【" + baseProject.getProjectName() + "】的预算项目未通过，请查看详情！");
                        //调用消息Service
                        messageService.sendOrClose(messageVo1);

                        // 操作日志
                        String userId = loginUser.getId();
                        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                        OperationLog operationLog = new OperationLog();
                        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                        operationLog.setName(userId);
                        operationLog.setType("3"); //预算项目
                        operationLog.setContent(baseProject.getProjectName()+"项目【"+baseProject.getId()+"】"+memberManage.getMemberName()+"审核未通过");
                        operationLog.setDoObject(budgeting.getId()); // 项目标识
                        operationLog.setStatus("0");
                        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        String ip = memberService.getIp(request);
                        operationLog.setIp(ip);
                        operationLogDao.insertSelective(operationLog);
                    }
                }
            }
        }
    }

    @Override
    public void intoAccount(String s1, String ids) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String[] split = s1.split(",");
        for (String s : split) {

            Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
            Example example1 = new Example(CostPreparation.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("budgetingId",budgeting.getId());
            criteria.andEqualTo("delFlag","0");
            CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);

            Example example2 = new Example(CostPreparation.class);
            Example.Criteria criteria1 = example2.createCriteria();
            criteria1.andEqualTo("budgetingId",budgeting.getId());
            criteria1.andEqualTo("delFlag","0");
            VeryEstablishment veryEstablishment1 = veryEstablishmentDao.selectOneByExample(example2);

            String designCategory = baseProject1.getDesignCategory();
            String district = baseProject1.getDistrict();
            String budgetingPeople = budgeting.getBudgetingPeople();
            String costPreparationTime = costPreparation.getCostPreparationTime();
            String pricingTogether = veryEstablishment1.getPricingTogether();
            if (designCategory == null || "".equals(designCategory)){
                throw new RuntimeException("您所选项目中存在未归属项目,请填写完归属在重新尝试");
            }
            if (district == null || "".equals(district)){
                throw new RuntimeException("您所选项目中存在未归属项目,请填写完归属在重新尝试");
            }
            if (budgetingPeople == null || "".equals(budgetingPeople)){
                throw new RuntimeException("您所选项目中存在未归属项目,请填写完归属在重新尝试");
            }
            if (costPreparationTime == null || "".equals(costPreparationTime)){
                throw new RuntimeException("您所选项目中存在未归属项目,请填写完归属在重新尝试");
            }
            if (pricingTogether == null || "".equals(pricingTogether)){
                throw new RuntimeException("您所选项目中存在未归属项目,请填写完归属在重新尝试");
            }


//            String founderId = budgeting.getFounderId();
            boolean f = false;
            ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("2");
            String personnel = thoseResponsible.getPersonnel();
            if (personnel!=null){
                String[] split1 = personnel.split(",");
                for (String s2 : split1) {
                    if (s2.equals(ids)){
                        f = true;
                    }
                }
            }
            //领导和领导指定人
            if (ids.equals(whzjh) || ids.equals(whzjm) || ids.equals(wjzjh) || f){

                //TODO start
                //根据预算外键查询基本信息
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                //绩效表
                EmployeeAchievementsInfo achievementsInfo = new EmployeeAchievementsInfo();
                //总金额
                BigDecimal total6 = new BigDecimal(0);
                Example example = new Example(VeryEstablishment.class);
                example.createCriteria().andEqualTo("budgetingId",budgeting.getId());
                //招标控制价
                VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example);
                // 如果是安徽

                if(!"4".equals(baseProject.getDistrict())){
                    //预算编制造价咨询金额
                    Double money = projectSumService.anhuiBudgetingMoney(budgeting.getAmountCost());
                    money = (double)Math.round(money*100)/100;
                    Double money1 = projectSumService.anhuiBudgetingMoney(veryEstablishment.getBiddingPriceControl());//                        total3 = total3.add(new BigDecimal(money1));
                    money1 = (double)Math.round(money1*100)/100;
                    //预算编制咨询费计算基数
                    Double aDouble = projectSumService.BudgetingBase(money, money1);
                    aDouble = (double)Math.round(aDouble*100)/100;
                    //计算基数和
                    //预算编制技提
                    Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                    aDouble1 = (double)Math.round(aDouble1*100)/100;
                    //计提和
                    total6 = total6.add(new BigDecimal(aDouble1));

                    Example example3 = new Example(EmployeeAchievementsInfo.class);
                    Example.Criteria c = example3.createCriteria();
                    c.andEqualTo("projectNum",budgeting.getId());
                    c.andEqualTo("delFlag","0");
                    EmployeeAchievementsInfo employeeAchievementsInfo = employeeAchievementsInfoMapper.selectOneByExample(example3);
                    if (employeeAchievementsInfo!=null){
                        BigDecimal actualAmount = total6.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        employeeAchievementsInfo.setActualAmount(actualAmount.add(employeeAchievementsInfo.getAccruedAmount()));
                        employeeAchievementsInfo.setAccruedAmount(total6.add(employeeAchievementsInfo.getAccruedAmount()));
                        BigDecimal balance = total6.subtract(actualAmount);
                        employeeAchievementsInfo.setBalance(balance.add(employeeAchievementsInfo.getBalance()));
                        employeeAchievementsInfoMapper.updateByPrimaryKeySelective(employeeAchievementsInfo);
                    }else{
                        //实际计提金额 取两位小数四舍五入
                        achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                        BigDecimal actualAmount = total6.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                        achievementsInfo.setActualAmount(actualAmount);
                        // 应收
                        achievementsInfo.setAccruedAmount(total6);
                        //余额
                        BigDecimal balance = total6.subtract(actualAmount);
                        achievementsInfo.setBalance(balance);
                        // 员工绩效
                        achievementsInfo.setMemberId(budgeting.getBudgetingPeople()); // 设计人
                        achievementsInfo.setCreateTime(data);
                        achievementsInfo.setUpdateTime(data);
                        achievementsInfo.setFounderId(budgeting.getFounderId());
                        achievementsInfo.setFounderCompanyId(budgeting.getFounderCompanyId());
                        achievementsInfo.setDelFlag("0");
                        achievementsInfo.setDistrict(baseProject.getDistrict());
                        achievementsInfo.setDept("2"); //造价
                        achievementsInfo.setAchievementsType("2"); //预算编制
                        achievementsInfo.setBaseProjectId(baseProject.getId());
                        achievementsInfo.setProjectNum(budgeting.getId());
                        achievementsInfo.setOverFlag("0");
                        employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
                    }



                    //吴江
                }else{
                    //预算编制造价咨询金额
                    Double money = projectSumService.wujiangBudgetingMoney(budgeting.getAmountCost());
                    money = (double)Math.round(money*100)/100;
                    //预算编制标底咨询金额
                    Double money1 = projectSumService.wujiangBudgetingMoney(veryEstablishment.getBiddingPriceControl());
                    money1 = (double)Math.round(money1*100)/100;
                    //预算编制咨询费计算基数
                    Double aDouble = projectSumService.BudgetingBase(money, money1);
                    aDouble = (double)Math.round(aDouble*100)/100;
                    //预算编制技提
                    Double aDouble1 = projectSumService.technicalImprovement(aDouble);
                    aDouble1 = (double)Math.round(aDouble1*100)/100;
                    //计提和
                    total6 = total6.add(new BigDecimal(aDouble1));
                    BigDecimal actualAmount = total6.multiply(new BigDecimal(0.8));
                    achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    //实际计提金额
                    achievementsInfo.setActualAmount(actualAmount);
                    BigDecimal accruedAmount = total6.subtract(actualAmount);
                    //余额
                    BigDecimal balance = total6.subtract(accruedAmount);
                    achievementsInfo.setBalance(balance);
                    // 员工绩效
                    achievementsInfo.setMemberId(budgeting.getBudgetingPeople()); // 设计人
                    achievementsInfo.setCreateTime(data);
                    achievementsInfo.setUpdateTime(data);
                    achievementsInfo.setFounderId(budgeting.getFounderId());
                    achievementsInfo.setFounderCompanyId(budgeting.getFounderCompanyId());
                    achievementsInfo.setDelFlag("0");
                    achievementsInfo.setDistrict(baseProject.getDistrict());
                    achievementsInfo.setDept("2"); //造价
                    achievementsInfo.setAchievementsType("2"); //预算编制
                    achievementsInfo.setBaseProjectId(baseProject.getId());
                    achievementsInfo.setProjectNum(budgeting.getId());
                    achievementsInfo.setOverFlag("0");
                    employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
                }
                //TODO end
            }else{
                throw new RuntimeException("您没有权限进行此操作,请联系领导或领导指定人进行操作");
            }
        }
        for (String s : split) {
            Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);

            budgeting.setWhetherAccount("0");
            budgetingDao.updateByPrimaryKeySelective(budgeting);
        }
    }

    @Override
    public List<BudgetingListVo> findAllBudgeting(PageBVo pageBVo, String id) {
//        List<BudgetingListVo> list = budgetingDao.findAllBudgeting(pageBVo);

        List<BudgetingListVo> returnList = new ArrayList<>();

        //待审核
        if (pageBVo.getBudgetingStatus().equals("1")){
            //领导看所有
            List<BudgetingListVo> list1 = new ArrayList<>();
            if ( id.equals(whzjh) || id.equals(whzjm) ||id.equals(wjzjh)){
                list1 = budgetingDao.findAllBudgetingCheckLeader(pageBVo);
                //普通员工
            }else {
                 list1 = budgetingDao.findAllBudgetingCheckStaff(pageBVo,id);
            }
            for (BudgetingListVo budgetingListVo : list1) {
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
                        .andEqualTo("auditResult","0");
                AuditInfo auditInfo = auditInfoDao.selectByExample(example).get(0);
                Example example1 = new Example(MemberManage.class);
                example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                MemberManage memberManage = memberManageDao.selectByExample(example1).get(0);
                if (memberManage !=null){
                    budgetingListVo.setCurrentHandler(memberManage.getMemberName());
                }
            }
            for (BudgetingListVo budgetingListVo : list1) {
                String baseId = budgetingListVo.getBaseId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
                if (baseProject.getDistrict() == null || baseProject.getDistrict().equals("")){
                    if (budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }
            }
            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }
            }
            return list1;
        }
        //处理中
        if (pageBVo.getBudgetingStatus().equals("2")){

            String loginUserId = id;

            // 编制人及领导，领导只能查看不能进行编辑
            if (id.equals(whzjm) || id.equals(whzjh) || id.equals(wjzjh)){
                loginUserId = "";
            }

            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingProcessing(pageBVo,loginUserId);
            for (int i = 0; i < list1.size(); i++) {
                BudgetingListVo budgetingListVo = list1.get(i);

                String baseId = budgetingListVo.getBaseId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);

                String s = budgetingListVo.getBudgetingPeople();
                if (id.equals(s)){
                    budgetingListVo.setEditFlag("0");
                }

                if (baseProject.getDistrict() == null || baseProject.getDistrict().equals("")){
                    if (budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }
            }

            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }
            }
            return list1;
        }
        //未通过
        if (pageBVo.getBudgetingStatus().equals("3")){

            String loginUserId = id;

            // 编制人及领导，领导只能查看不能进行编辑
            if (id.equals(whzjm) || id.equals(whzjh) || id.equals(wjzjh)){
                loginUserId = "";
            }

//            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingProcessing(pageBVo,id);
            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingUnsanctioned(pageBVo,loginUserId);
            for (BudgetingListVo budgetingListVo : list1) {

                String s = budgetingListVo.getBudgetingPeople();
                if (id.equals(s)){
                    budgetingListVo.setEditFlag("0");
                }

                String baseId = budgetingListVo.getBaseId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
                if (baseProject.getDistrict() == null || baseProject.getDistrict().equals("")){
                    if (budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }

            }
            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }
            }
            return list1;
        }
        //已完成
        if (pageBVo.getBudgetingStatus().equals("4")){
            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingCompleted(pageBVo,id);
            for (BudgetingListVo budgetingListVo : list1) {
                String baseId = budgetingListVo.getBaseId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
                if (id.equals(whzjm) || id.equals(whzjh) || id.equals(wjzjh) || id.equals(budgetingListVo.getBudgetingPeople())){
                    budgetingListVo.setEditFlag("0");
                }
                if (baseProject.getDistrict() == null || baseProject.getDistrict().equals("")){
                    if (budgetingListVo.getFounderId() != null && budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }
                if (baseProject.getDesignCategory() == null || baseProject.getDesignCategory().equals("")){
                    if (budgetingListVo.getFounderId() != null && budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }

                String budgetingPeople = budgetingListVo.getBudgetingPeople();
                String costTogether = budgetingListVo.getCostTogether();
                String pricingTogether = budgetingListVo.getPricingTogether();

                if (budgetingPeople == null || budgetingPeople.equals("")){
                    if (StringUtils.isNotEmpty(budgetingListVo.getFounderId()) && budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }

                if (costTogether == null || costTogether.equals("")){
                    if (StringUtils.isNotEmpty(budgetingListVo.getFounderId()) && budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }

                if (pricingTogether == null || pricingTogether.equals("")){
                    if (StringUtils.isNotEmpty(budgetingListVo.getFounderId()) && budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }
                // 判断CEA编号

                if (StringUtils.isNotEmpty(budgetingListVo.getFounderId()) && id.equals(budgetingListVo.getFounderId())){
                    budgetingListVo.setIsShow("1");
                }
            }
            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }


                if (id.equals(whzjm)){
                    budgetingListVo.setYinShow("1");
                }
            }

            return list1;
        }
        //全部
        if (pageBVo.getBudgetingStatus().equals("") || pageBVo.getBudgetingStatus().equals("0")){
            pageBVo.setBudgetingStatus("");
            String userId = id;
            if (wjzjm.equals(id) || whzjm.equals(id) || whzjh.equals(id)){
                userId = "";
            }
            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingProcessing(pageBVo,userId);
            for (BudgetingListVo budgetingListVo : list1) {
                String baseId = budgetingListVo.getBaseId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
                if (baseProject.getDistrict() == null || baseProject.getDistrict().equals("")){
                    if (budgetingListVo.getFounderId().equals(id)){
                        budgetingListVo.setShowWhether("1");
                    }else{
                        budgetingListVo.setShowWhether("2");
                    }
                }
            }
            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }
            }
            return list1;
        }

        //待确认
        if (pageBVo.getBudgetingStatus().equals("5")){

            List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingConfirmed(pageBVo);

            for (BudgetingListVo budgetingListVo : list1) {
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
                        .andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                Example example1 = new Example(MemberManage.class);
                example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                if (memberManage !=null){
                    budgetingListVo.setCurrentHandler(memberManage.getMemberName());
                }

                if (auditInfo.getAuditorId().equals(id)){
                    budgetingListVo.setShowUnsanctioned("1");
                }
            }

            for (BudgetingListVo budgetingListVo : list1) {
                MkyUser mkyUser2 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (mkyUser2!=null){
                    budgetingListVo.setBudgetingPeople(mkyUser2.getUserName());
                }
                MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (mkyUser!=null){
                    budgetingListVo.setCostTogether(mkyUser.getUserName());
                }
                MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (mkyUser1!=null){
                    budgetingListVo.setPricingTogether(mkyUser1.getUserName());
                }
            }
            return list1;
        }

//        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(returnList);
        return returnList;
//
//        for (BudgetingListVo budgetingListVo : list) {
//            //待审核
////            if (pageBVo.getBudgetingStatus().equals("1")){
////
////                //根据条件查找当前处理人
////                Example example = new Example(AuditInfo.class);
////                example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
////                        .andEqualTo("auditResult","0");
////                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
////                Example example1 = new Example(MemberManage.class);
////                example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
////                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
////                if (memberManage !=null){
////                    budgetingListVo.setCurrentHandler(memberManage.getMemberName());
////                }
////
////                List<BudgetingListVo> list1 = budgetingDao.findAllBudgetingCheck(pageBVo);
////
////                if ( auditInfo.getAuditResult().equals("0")){
////
////                    if(auditInfo.getAuditorId().equals(id) || id.equals(whzjh) || id.equals(whzjm) ||id.equals(wjzjh) || budgetingListVo.getFounderId().equals(id)){
////
////                        budgetingListVos.add(budgetingListVo);
////                    }
////                }
////            }
//            //处理中
////            if (budgetingListVo.getBudgetStatus()!=null && budgetingListVo.getBudgetStatus().equals("处理中")){
////
////                if (budgetingListVo.getFounderId().equals(id)){
////                    budgetingListVos1.add(budgetingListVo);
////                }
////            }
//            //未通过
//            if (budgetingListVo.getAuditResult()!=null && budgetingListVo.getAuditResult().equals("2")){
//                if (budgetingListVo.getFounderId().equals(id)){
//                    budgetingListVos2.add(budgetingListVo);
//                }
//            }
//            //已完成
//            if (budgetingListVo.getBudgetStatus() != null && budgetingListVo.getBudgetStatus().equals("已完成")){
//                budgetingListVos3.add(budgetingListVo);
//            }
//        }
//        //待审核
//        if (pageBVo.getBudgetingStatus().equals("1")){
//            List<BudgetingListVo> budgetingListVoPageInfo = budgetingListVos;
//            ArrayList<BudgetingListVo> budgetingListVos4 = new ArrayList<>();
//            for (BudgetingListVo budgetingListVo : budgetingListVoPageInfo) {
//                if (!budgetingListVos4.contains(budgetingListVo)){
//                    budgetingListVos4.add(budgetingListVo);
//                }
//            }
//            returnList = budgetingListVos4;
//            PageInfo<BudgetingListVo> budgetingListVoPageInfo1 = new PageInfo<>(returnList);
//            return budgetingListVoPageInfo1;
//        }
////        //处理中
//        if (pageBVo.getBudgetingStatus().equals("2")){
//            ArrayList<BudgetingListVo> budgetingListVoPageInfo = budgetingListVos1;
//            returnList = budgetingListVoPageInfo;
//            PageInfo<BudgetingListVo> budgetingListVoPageInfo1 = new PageInfo<>(returnList);
//            return budgetingListVoPageInfo1;
//        }
////        //未通过
//        if (pageBVo.getBudgetingStatus().equals("3")){
//            ArrayList<BudgetingListVo> budgetingListVoPageInfo = budgetingListVos2;
//            returnList = budgetingListVoPageInfo;
//            PageInfo<BudgetingListVo> budgetingListVoPageInfo1 = new PageInfo<>(returnList);
//            return budgetingListVoPageInfo1;
//        }
////        //已完成
//        if (pageBVo.getBudgetingStatus().equals("4")){
//            ArrayList<BudgetingListVo> budgetingListVos4 = new ArrayList<>();
//            for (BudgetingListVo budgetingListVo : budgetingListVos3) {
//                if (!budgetingListVos4.contains(budgetingListVo)){
//                    budgetingListVos4.add(budgetingListVo);
//                }
//            }
//
//            returnList = budgetingListVos4;
//            PageInfo<BudgetingListVo> budgetingListVoPageInfo1 = new PageInfo<>(returnList);
//            return budgetingListVoPageInfo1;
//        }
//        ArrayList<BudgetingListVo> budgetingListVos4 = new ArrayList<>();
//        System.err.println(list);
//        for (BudgetingListVo budgetingListVo : list) {
//
//            if (budgetingListVo.getFounderId().equals(id)){
//                if (!budgetingListVos4.contains(budgetingListVo)){
//                    budgetingListVos4.add(budgetingListVo);
//                }
//            }
//        }
//        returnList = budgetingListVos4;
//        PageInfo<BudgetingListVo> budgetingListVoPageInfo1 = new PageInfo<>(returnList);
//        return budgetingListVoPageInfo1;

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

            for (BudgetingListVo budgetingListVo : budgetingListVos) {
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (memberManage != null){
                    budgetingListVo.setBudgetingPeople(memberManage.getMemberName());
                }
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (memberManage1 != null){
                    budgetingListVo.setCostTogether(memberManage1.getMemberName());
                }
                MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (memberManage2 != null){
                    budgetingListVo.setPricingTogether(memberManage2.getMemberName());
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
            //预算
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
            for (BudgetingListVo budgetingListVo : budgetingListVos) {
                String budgetingPeople = budgetingListVo.getBudgetingPeople();
                String costTogether = budgetingListVo.getCostTogether();
                String pricingTogether = budgetingListVo.getPricingTogether();
                if (budgetingPeople!=null && !"".equals(budgetingPeople)){
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(budgetingPeople);
                    if (mkyUser!=null){
                        budgetingListVo.setBudgetingPeople(mkyUser.getUserName());
                    }
                }

                if (costTogether!=null && !"".equals(costTogether)){
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(costTogether);
                    if (mkyUser!=null){
                        budgetingListVo.setCostTogether(mkyUser.getUserName());
                    }
                }

                if (pricingTogether!=null && !"".equals(pricingTogether)){
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(pricingTogether);
                    if (mkyUser!=null){
                        budgetingListVo.setPricingTogether(mkyUser.getUserName());
                    }
                }
            }
            //清标
        }else if(sid!=null && sid.equals("6")){
            List<BudgetingListVo> clearProjectAll = budgetingDao.findClearProjectAll(pageBVo);
            return clearProjectAll;

            //进度款
        }else if (sid!=null && sid.equals("7")){
            List<BudgetingListVo> budgetingAll2 = budgetingDao.findBudgetingAll(pageBVo);
            ArrayList<BudgetingListVo> budgetingListVos1 = new ArrayList<>();
            for (BudgetingListVo budgetingListVo : budgetingAll2) {
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(budgetingListVo.getBudgetingPeople());
                if (memberManage != null){
                    budgetingListVo.setBudgetingPeople(memberManage.getMemberName());
                }
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(budgetingListVo.getCostTogether());
                if (memberManage1 != null){
                    budgetingListVo.setCostTogether(memberManage1.getMemberName());
                }
                MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(budgetingListVo.getPricingTogether());
                if (memberManage2 != null){
                    budgetingListVo.setPricingTogether(memberManage2.getMemberName());
                }

                budgetingListVos1.add(budgetingListVo);
            }
            for (BudgetingListVo budgetingListVo : budgetingAll2) {
                Example example = new Example(ProgressPaymentInformation.class);
                Example.Criteria cc = example.createCriteria();
                cc.andEqualTo("baseProjectId",budgetingListVo.getBaseId());
                cc.andEqualTo("delFlag","0");
                List<ProgressPaymentInformation> list = progressPaymentInformationDao.selectByExample(example);
                if (list!=null && list.size()!=0){
                    budgetingListVos1.remove(budgetingListVo);
                }
            }
            return budgetingListVos1;
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
    public void addAttribution(String id, String designCategory, String district,String prePeople) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        baseProject.setDesignCategory(designCategory);
        baseProject.setDistrict(district);
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        Example example = new Example(Budgeting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",baseProject.getId());
        criteria.andEqualTo("delFlag","0");
        Budgeting budgeting = budgetingDao.selectOneByExample(example);
        budgeting.setBudgetingPeople(prePeople);
        budgeting.setAttributionShow("1");
        budgetingDao.updateByPrimaryKeySelective(budgeting);

        Example example1 = new Example(CostPreparation.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("budgetingId",budgeting.getId());
        c2.andEqualTo("delFlag","0");
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);
        costPreparation.setCostTogether(prePeople);
        costPreparationDao.updateByPrimaryKeySelective(costPreparation);

        Example example2 = new Example(VeryEstablishment.class);
        Example.Criteria c3 = example2.createCriteria();
        c3.andEqualTo("budgetingId",budgeting.getId());
        c3.andEqualTo("delFlag","0");
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example2);
        veryEstablishment.setPricingTogether(prePeople);
        veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);
    }

    @Override
    public List<DesignInfo> findDesignAll(PageBVo pageBVo) {
       return baseProjectDao.findDesignAll(pageBVo);
    }

    @Override
    public void deleteBudgeting(String id,UserInfo loginUser,HttpServletRequest request) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(id);
        budgeting.setDelFlag("1");
        budgetingDao.updateByPrimaryKeySelective(budgeting);
        Example example = new Example(SurveyInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("budgetingId",id);
        c.andEqualTo("delFlag","0");
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);
        if ( null != surveyInformation) {
            surveyInformation.setDelFlag("1");
            surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);
        }

        Example example1 = new Example(CostPreparation.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("budgetingId",id);
        c1.andEqualTo("delFlag","0");
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);
        if (null != costPreparation) {
            costPreparation.setDelFlag("1");
            costPreparationDao.updateByPrimaryKeySelective(costPreparation);
        }

        Example example2 = new Example(VeryEstablishment.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("budgetingId",id);
        c2.andEqualTo("delFlag","0");
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example2);
        if (null != veryEstablishment) {
            veryEstablishment.setDelFlag("1");
            veryEstablishmentDao.updateByPrimaryKeySelective(veryEstablishment);
        }

        Example example3 = new Example(AuditInfo.class);
        Example.Criteria criteria = example3.createCriteria();
        criteria.andEqualTo("baseProjectId",id);
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example3);
        for (AuditInfo auditInfo : auditInfos) {
            auditInfoDao.deleteByPrimaryKey(auditInfo);
        }
        // 操作日志
        String userId = loginUser.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("3"); //预算项目
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
        if (baseProject != null){
            operationLog.setContent(memberManage.getMemberName()+"删除了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
        }
        operationLog.setDoObject(budgeting.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
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
    public void updateCEA(String baseId, String ceaNum) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        baseProject.setCeaNum(ceaNum);
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
    }

    //预算新增所选项目回显附件
    @Override
    public List<FileInfo> selectById(String id) {
        Example example = new Example(DesignInfo.class);
        example.createCriteria().andEqualTo("baseProjectId",id);
        DesignInfo designInfo = designInfoMapper.selectOneByExample(example);
        if (designInfo == null){
            List<FileInfo> fileInfos = new ArrayList<>();
            return fileInfos;
        }
        Example example1 = new Example(FileInfo.class);
        example1.createCriteria().andEqualTo("platCode",designInfo.getId())
                                 .andEqualTo("type","sjxmxjsjxx"); //设计图纸
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
        //根据用户id查出用户名称
        if (fileInfos != null){
            for (FileInfo thisFile : fileInfos) {
                Example example2 = new Example(MemberManage.class);
                example2.createCriteria().andEqualTo("id",thisFile.getUserId());
                MemberManage memberManage = memberManageDao.selectOneByExample(example2);
                thisFile.setUserName(memberManage.getMemberName());
            }
        }
        return fileInfos;
    }

    @Override
    public List<MkyUser> findPreparePeople(String id) {
      return   auditInfoDao.findPreparePeople(id);
    }

    @Override
    public Budgeting budgetingPeople(String id) {
        String preparePeople = memberManageDao.findIdByName(id);
        Budgeting budgeting = new Budgeting();
        budgeting.setBudgetingPeople(preparePeople);
        return budgeting;
    }

    @Override
    public void editOutSourceMoney(String id, String amountOutsourcing) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(id);
        if (budgeting != null){
            budgeting.setAmountOutsourcing(new BigDecimal(amountOutsourcing));
            budgetingDao.updateByPrimaryKeySelective(budgeting);
        }
    }

    @Override
    public void budgetingSuccess(String s, String ids) {
        String[] split = s.split(",");

            for (String s1 : split) {
                Budgeting budgeting = budgetingDao.selectByPrimaryKey(s1);
                Example example = new Example(AuditInfo.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",budgeting.getId());
                c.andEqualTo("status","0");
                c.andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo!=null){
                    if (! auditInfo.getAuditorId().equals(ids)){
                        throw new RuntimeException("此操作只能由所选项目部门领导来完成");
                    } else {
                        AuditInfo auditInfo1 = new AuditInfo();
                        auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                        auditInfo1.setBaseProjectId(budgeting.getId());
                        auditInfo1.setAuditResult("0");
                        auditInfo1.setAuditType("4");
                        String founderId = budgeting.getFounderId();
                        Example example1 = new Example(MemberManage.class);
                        Example.Criteria cc = example1.createCriteria();
                        cc.andEqualTo("id",founderId);
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        //1芜湖
                        if (memberManage.getWorkType().equals("1")){
                            auditInfo1.setAuditorId(whzjm);
                            //吴江
                        }else if (memberManage.getWorkType().equals("2")){
                            auditInfo1.setAuditorId(wjzjm);
                        }
                        auditInfo1.setFounderId(ids);
                        auditInfo1.setStatus("0");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                        auditInfo1.setUpdateTime(simpleDateFormat.format(new Date()));
                        auditInfoDao.insertSelective(auditInfo1);

                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                        baseProject.setBudgetStatus("1");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);

                    }
                }
            }

    }

    @Override
    public void budgetingSendBack(String s, String id, String id1) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
        baseProject.setBudgetStatus("6");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",budgeting.getId());
        c.andEqualTo("status","0");
        c.andEqualTo("auditType","1");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        auditInfo.setAuditResult("2");
        auditInfo.setAuditOpinion(id1);
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        auditInfo.setAuditTime(ss.format(new Date()));
        auditInfo.setUpdateTime(ss.format(new Date()));
        auditInfoDao.updateByPrimaryKeySelective(auditInfo);

    }

    @Override
    public void budgetingSendBackB(String s, String id, String id1) {
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
        baseProject.setBudgetStatus("6");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",budgeting.getId());
        c.andEqualTo("status","0");
        c.andEqualTo("auditType","4");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        auditInfo.setAuditResult("2");
        auditInfo.setAuditOpinion(id1);
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        auditInfo.setAuditTime(ss.format(new Date()));
        auditInfo.setUpdateTime(ss.format(new Date()));
        auditInfoDao.updateByPrimaryKeySelective(auditInfo);




        //创建人

        String projectName = baseProject.getProjectName();

        MessageVo messageVo = new MessageVo();
        messageVo.setId("A24");
        messageVo.setUserId(budgeting.getFounderId());
        messageVo.setType("1"); //风险
        messageVo.setTitle("您有一个预算项目已被总经理退回！");
        // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
        messageVo.setSnsContent("您好！"+projectName+"项目预算任务已被总经理退回，请重新编制！");
        messageVo.setContent("您好！"+projectName+"项目预算任务已被总经理退回，请重新编制！");
        messageVo.setDetails("您好！"+projectName+"项目预算任务已被总经理退回，请重新编制！");
        messageService.sendOrClose(messageVo);

        // 互审人
        Example example1 = new Example(AuditInfo.class);
        Example.Criteria cc = example1.createCriteria();
        cc.andEqualTo("baseProjectId",budgeting.getId());
        cc.andEqualTo("status","0");
        cc.andEqualTo("auditType","0");
        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example1);

        MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditInfo1.getAuditorId());
        MessageVo messageVo1 = new MessageVo();
        messageVo1.setId("A24");
        messageVo1.setType("1"); // 风险
        messageVo1.setUserId(auditInfo1.getAuditorId());
        messageVo1.setPhone(memberManage1.getPhone());
        messageVo1.setReceiver(memberManage1.getEmail());
        messageVo1.setTitle("您有一个预算项目已被总经理退回！");
        // 「接收人姓名」您好！【提交人】提交给您的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
        messageVo1.setSnsContent("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
        messageVo1.setContent("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
        messageVo1.setDetails("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
//                messageVo1.setSnsContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setDetails(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
        messageService.sendOrClose(messageVo1);

        // 部门领导
        Example example2 = new Example(AuditInfo.class);
        Example.Criteria cc1 = example2.createCriteria();
        cc1.andEqualTo("baseProjectId",budgeting.getId());
        cc1.andEqualTo("status","0");
        cc1.andEqualTo("auditType","1");
        AuditInfo auditInfo2 = auditInfoDao.selectOneByExample(example2);
        MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(auditInfo2.getAuditorId());

        MessageVo messageVo2 = new MessageVo();
        messageVo2.setId("A24");
        messageVo2.setType("1"); // 风险
        messageVo2.setUserId(auditInfo2.getAuditorId());
        messageVo2.setPhone(memberManage2.getPhone());
        messageVo2.setReceiver(memberManage2.getEmail());
        messageVo2.setTitle("您有一个预算项目已被总经理退回！");
        // 「接收人姓名」您好！【提交人】提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
        messageVo2.setSnsContent("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
        messageVo2.setContent("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
        messageVo2.setDetails("您好！"+projectName+"项目预算任务已被总经理退回，请注意关注！");
//                messageVo2.setSnsContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setDetails(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
        messageService.sendOrClose(messageVo2);



    }

    @Override
    public String findDesinerMoney(String id) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        Example example1 = new Example(DesignInfo.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("baseProjectId",baseProject.getId());
        criteria.andEqualTo("status","0");
        DesignInfo designInfo = designInfoMapper.selectOneByExample(example1);
        if (designInfo == null){
            return null;
        }
        if ("4".equals(baseProject.getDistrict())){
            Example example = new Example(WujiangMoneyInfo.class);
            Example.Criteria cc = example.createCriteria();
            cc.andEqualTo("baseProjectId",designInfo.getId());
            cc.andEqualTo("status","0");
            WujiangMoneyInfo wujiangMoneyInfo1 = wujiangMoneyInfoMapper.selectOneByExample(example);
            if (wujiangMoneyInfo1!=null){
                return wujiangMoneyInfo1.getRevenue().toString();
            }
        }else{
            Example example = new Example(AnhuiMoneyinfo.class);
            Example.Criteria cc = example.createCriteria();
            cc.andEqualTo("baseProjectId",designInfo.getId());
            cc.andEqualTo("status","0");
            AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(example);
            if (anhuiMoneyinfo!=null){
                return anhuiMoneyinfo.getRevenue().toString();
            }
        }
        return null;
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

    public Budgeting findOneBudgeting(String id) {
        Budgeting budgeting = budgetingDao.findBudgeting(id);
        return budgeting;

    }

    //

}

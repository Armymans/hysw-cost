package net.zlw.cloud.librarian.service;

import net.zlw.cloud.VisaChange.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.PackageCameMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.PackageCame;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.followAuditing.mapper.TrackApplicationInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.progressPayment.mapper.ApplicationInformationDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.ApplicationInformation;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;

import net.zlw.cloud.settleAccounts.mapper.SettlementInfoMapper;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.MessageService;
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
public class ThoseResponsibleService  {
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private ProjectExplorationMapper projectExplorationMapper;
    @Resource
    private PackageCameMapper packageCameMapper;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private SurveyInformationDao surveyInformationDao;
    @Resource
    private CostPreparationDao costPreparationDao;
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;
    @Resource
    private VisaChangeMapper visaChangeMapper;
    @Resource
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private TrackApplicationInfoDao trackApplicationInfoDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;
    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;
    @Resource
    private SettlementInfoMapper settlementInfoMapper;
    @Resource
    private MkyUserMapper mkyUserMapper;
    @Resource
    private MaintenanceProjectInformationMapper maintenanceProjectInformationMapper;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private ApplicationInformationDao applicationInformationDao;
    @Resource
    private MessageService messageService;







    public List<ThoseResponsible> findthoseResponsiblAll() {
        List<ThoseResponsible> thoseResponsibles = thoseResponsibleDao.selectAll();
        for (ThoseResponsible thoseResponsible : thoseResponsibles) {
            String personnel = thoseResponsible.getPersonnel();
            String a = "";
            if (personnel!=null){
                String[] split = personnel.split(",");
                for (String s : split) {
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(s);
                    a += memberManage.getMemberName()+",";
                }
                a = a.substring(0,a.length()-1);
                thoseResponsible.setPersonnel(a);
            }
        }
        return thoseResponsibles;
    }

    public void addPerson( String remeberId,String type) {
        if ("1".equals(type)){
            ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("1");
            String personnel = thoseResponsible.getPersonnel();
            if (personnel == null){
                String[] split = remeberId.split(",");
                String rId = "";
                for (String s : split) {
                    rId += s+",";
                }
                rId = rId.substring(0,rId.length()-1);
                thoseResponsible.setPersonnel(rId);
                thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
            }else{
                String[] split = remeberId.split(",");
                for (String s : split) {
                    personnel += ","+s+",";
                    personnel = personnel.substring(0,personnel.length()-1);
                }
                thoseResponsible.setPersonnel(personnel);
                thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
            }
        }else if("2".equals(type)){
            ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("2");
            String personnel = thoseResponsible.getPersonnel();
            if (personnel == null){
                String[] split = remeberId.split(",");
                String rId = "";
                for (String s : split) {
                    rId += s+",";
                }
                rId = rId.substring(0,rId.length()-1);
                thoseResponsible.setPersonnel(rId);
                thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
            }else{
                String[] split = remeberId.split(",");
                for (String s : split) {
                    personnel += ","+s+",";
                    personnel = personnel.substring(0,personnel.length()-1);
                }
                thoseResponsible.setPersonnel(personnel);
                thoseResponsibleDao.updateByPrimaryKeySelective(thoseResponsible);
            }
        }

    }

    public List<MemberManage> findAllTaskManager() {
      return   memberManageDao.findAllTaskManager();
    }

    private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void missionPerson(String missionType, String missionPerson, String id,String baseId) {
        //设计
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if ("1".equals(missionType)){

            baseProject.setDesginStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            //设计
            Example example = new Example(DesignInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseProject.getId());
            c.andEqualTo("status","0");
            DesignInfo designInfo1 = designInfoMapper.selectOneByExample(example);


            if (designInfo1 == null){
                DesignInfo designInfo = new DesignInfo();
                designInfo.setId(UUID.randomUUID().toString().replace("-",""));
                designInfo.setBaseProjectId(baseProject.getId());
                designInfo.setFounderId(missionPerson);
                designInfo.setStatus("0");
                designInfo.setDesigner(missionPerson);
                designInfo.setOutsource("1");
                designInfo.setCreateTime(s.format(new Date()));
                designInfoMapper.insertSelective(designInfo);

                ProjectExploration projectExploration = new ProjectExploration();
                projectExploration.setId(UUID.randomUUID().toString().replace("-",""));
                projectExploration.setFounderId(missionPerson);
                projectExploration.setStatus("0");
                projectExploration.setCreateTime(s.format(new Date()));
                projectExploration.setBaseProjectId(designInfo.getId());
                projectExplorationMapper.insertSelective(projectExploration);

                PackageCame packageCame = new PackageCame();
                packageCame.setId(UUID.randomUUID().toString().replace("-",""));
                packageCame.setBassProjectId(designInfo.getId());
                packageCame.setFounderId(missionPerson);
                packageCame.setStatus("0");
                packageCame.setCreateTime(s.format(new Date()));
                packageCameMapper.insertSelective(packageCame);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个设计项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的设计，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的设计，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的设计，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else{
                throw new RuntimeException("当前工程设计任务已存在");
            }

            //预算
        }else if("2".equals(missionType)){

            baseProject.setBudgetStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            //预算
            Example example1 = new Example(Budgeting.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId",baseProject.getId());
            c1.andEqualTo("delFlag","0");
            Budgeting budgeting = budgetingDao.selectOneByExample(example1);

            if (budgeting == null){


                Budgeting budgeting1 = new Budgeting();
                budgeting1.setId(UUID.randomUUID().toString().replace("-",""));
                budgeting1.setCreateTime(s.format(new Date()));
                budgeting1.setBaseProjectId(baseProject.getId());
                budgeting1.setFounderId(missionPerson);
                budgeting1.setBudgetingPeople(missionPerson);
                budgeting1.setDelFlag("0");
                budgeting1.setOutsourcing("2");
                budgetingDao.insertSelective(budgeting1);

                SurveyInformation surveyInformation = new SurveyInformation();
                surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
                surveyInformation.setBudgetingId(budgeting1.getId());
                surveyInformation.setBaseProjectId(baseProject.getId());
                surveyInformation.setCreateTime(s.format(new Date()));
                surveyInformation.setFounderId(missionPerson);
                surveyInformation.setDelFlag("0");
                surveyInformationDao.insertSelective(surveyInformation);

                CostPreparation costPreparation = new CostPreparation();
                costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
                costPreparation.setBaseProjectId(baseProject.getId());
                costPreparation.setBudgetingId(budgeting1.getId());
                costPreparation.setCostTogether(missionPerson);
                costPreparation.setCreateTime(s.format(new Date()));
                costPreparation.setFounderId(missionPerson);
                costPreparation.setDelFlag("0");
                costPreparationDao.insertSelective(costPreparation);

                VeryEstablishment veryEstablishment = new VeryEstablishment();
                veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
                veryEstablishment.setBaseProjectId(baseProject.getId());
                veryEstablishment.setPricingTogether(missionPerson);
                veryEstablishment.setBudgetingId(budgeting1.getId());
                veryEstablishment.setFounderId(missionPerson);
                veryEstablishment.setCreateTime(s.format(new Date()));
                veryEstablishment.setDelFlag("0");
                veryEstablishmentDao.insertSelective(veryEstablishment);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个预算项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的预算，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的预算，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的预算，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else{
                throw new RuntimeException("当前工程预算任务已存在");
            }
            //进度款
        } else if("3".equals(missionType)){
            baseProject.setProgressPaymentStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);

            Example example = new Example(ApplicationInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseProject.getId());
            c.andEqualTo("delFlag","0");
            ApplicationInformation applicationInformation = applicationInformationDao.selectOneByExample(example);
            if (applicationInformation == null){
                ApplicationInformation applicationInformation1 = new ApplicationInformation();
                applicationInformation1.setId(UUID.randomUUID().toString().replace("-",""));
                applicationInformation1.setBaseProjectId(baseProject.getId());
                applicationInformation1.setCreateTime(s.format(new Date()));
                applicationInformation1.setFounderId(missionPerson);
                applicationInformation1.setDelFlag("0");
                applicationInformationDao.insertSelective(applicationInformation1);

                ProgressPaymentInformation progressPaymentInformation = new ProgressPaymentInformation();
                progressPaymentInformation.setId(UUID.randomUUID().toString().replace("-",""));
                progressPaymentInformation.setOutsourcing("2");
                progressPaymentInformation.setBaseProjectId(baseProject.getId());
                progressPaymentInformation.setCreateTime(s.format(new Date()));
                progressPaymentInformation.setFounderId(missionPerson);
                progressPaymentInformation.setDelFlag("0");
                progressPaymentInformation.setChangeNum(1);
                progressPaymentInformationDao.insertSelective(progressPaymentInformation);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个进度款项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的进度款，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的进度款，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的进度款，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else{
                throw new RuntimeException("当前工程进度款任务已存在");
            }


            //签证变更
        }else if("4".equals(missionType)){

            baseProject.setVisaStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            Example example = new Example(VisaChange.class);
            Example.Criteria cc = example.createCriteria();
            cc.andEqualTo("baseProjectId",baseProject.getId());
            cc.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
            if (visaChanges == null || visaChanges.size() == 0){
                VisaApplyChangeInformation visaApplyChangeInformation = new VisaApplyChangeInformation();
                visaApplyChangeInformation.setId(UUID.randomUUID().toString().replace("-",""));
                visaApplyChangeInformation.setUpAndDownMark("0");
                visaApplyChangeInformation.setCreateTime(s.format(new Date()));
                visaApplyChangeInformation.setFouderId(missionPerson);
                visaApplyChangeInformation.setState("0");
                visaApplyChangeInformation.setBaseProjectId(baseProject.getId());
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformation);

                VisaApplyChangeInformation visaApplyChangeInformation1 = new VisaApplyChangeInformation();
                visaApplyChangeInformation1.setId(UUID.randomUUID().toString().replace("-",""));
                visaApplyChangeInformation1.setUpAndDownMark("1");
                visaApplyChangeInformation1.setCreateTime(s.format(new Date()));
                visaApplyChangeInformation1.setFouderId(missionPerson);
                visaApplyChangeInformation1.setState("0");
                visaApplyChangeInformation1.setBaseProjectId(baseProject.getId());
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformation1);

                VisaChange visaChange = new VisaChange();
                visaChange.setId(UUID.randomUUID().toString().replace("-",""));
                visaChange.setCreateTime(s.format(new Date()));
                visaChange.setState("0");
                visaChange.setOutsourcing("2");
                visaChange.setCreatorId(missionPerson);
                visaChange.setChangeNum(1);
                visaChange.setBaseProjectId(baseProject.getId());
                visaChange.setUpAndDownMark("0");
                visaChangeMapper.insertSelective(visaChange);

                VisaChange visaChange1 = new VisaChange();
                visaChange1.setId(UUID.randomUUID().toString().replace("-",""));
                visaChange1.setCreateTime(s.format(new Date()));
                visaChange1.setState("0");
                visaChange1.setOutsourcing("2");
                visaChange1.setCreatorId(missionPerson);
                visaChange1.setChangeNum(1);
                visaChange1.setBaseProjectId(baseProject.getId());
                visaChange1.setUpAndDownMark("1");
                visaChangeMapper.insertSelective(visaChange1);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个签证变更项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的签证变更，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的签证变更，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的签证变更，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else{
                throw new RuntimeException("当前工程签证变更任务已存在");
            }
            //跟踪审计
        }else if("5".equals(missionType)){
            baseProject.setTrackStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            Example example = new Example(TrackAuditInfo.class);
            Example.Criteria cc = example.createCriteria();
            cc.andEqualTo("baseProjectId",baseProject.getId());
            cc.andEqualTo("status","0");
            TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectOneByExample(example);
            if (trackAuditInfo == null){
                TrackAuditInfo trackAuditInfo1 = new TrackAuditInfo();
                trackAuditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                trackAuditInfo1.setOutsource("1");
                trackAuditInfo1.setFounderId(missionPerson);
                trackAuditInfo1.setStatus("0");
                trackAuditInfo1.setCreateTime(s.format(new Date()));
                trackAuditInfo1.setBaseProjectId(baseProject.getId());
                trackAuditInfoDao.insertSelective(trackAuditInfo1);

                TrackApplicationInfo trackApplicationInfo = new TrackApplicationInfo();
                trackApplicationInfo.setId(UUID.randomUUID().toString().replace("-",""));
                trackApplicationInfo.setCreateTime(s.format(new Date()));
                trackApplicationInfo.setFouderId(missionPerson);
                trackApplicationInfo.setState("0");
                trackApplicationInfo.setTrackAudit(trackAuditInfo1.getId());
                trackApplicationInfoDao.insertSelective(trackApplicationInfo);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个跟踪审计项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的跟踪审计，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的跟踪审计，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的跟踪审计，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else {
                throw new RuntimeException("当前工程跟踪审计任务已存在");
            }
        }else if("6".equals(missionType)){
            baseProject.setSettleAccountsStatus("2");
            baseProjectDao.updateByPrimaryKey(baseProject);

            Example example = new Example(LastSettlementReview.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("baseProjectId",baseProject.getId());
            criteria.andEqualTo("delFlag","0");
            LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);

            Example example1 = new Example(SettlementAuditInformation.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("baseProjectId",baseProject.getId());
            criteria1.andEqualTo("delFlag","0");
            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);

            if (lastSettlementReview == null && settlementAuditInformation == null){
                InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
                investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-",""));
                investigationOfTheAmount.setBaseProjectId(baseProject.getId());
                investigationOfTheAmount.setCreateTime(s.format(new Date()));
                investigationOfTheAmount.setFounderId(missionPerson);
                investigationOfTheAmount.setDelFlag("0");
                investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);

                LastSettlementReview lastSettlementReview1 = new LastSettlementReview();
                lastSettlementReview1.setId(UUID.randomUUID().toString().replace("-",""));
                lastSettlementReview1.setOutsourcing("2");
                lastSettlementReview1.setCreateTime(s.format(new Date()));
                lastSettlementReview1.setFounderId(missionPerson);
                lastSettlementReview1.setDelFlag("0");
                lastSettlementReview1.setPreparePeople(missionPerson);
                lastSettlementReview1.setBaseProjectId(baseProject.getId());
                lastSettlementReviewDao.insertSelective(lastSettlementReview1);

                SettlementAuditInformation settlementAuditInformation1 = new SettlementAuditInformation();
                settlementAuditInformation1.setId(UUID.randomUUID().toString().replace("-",""));
                settlementAuditInformation1.setOutsourcing("2");
                settlementAuditInformation1.setCreateTime(s.format(new Date()));
                settlementAuditInformation1.setFounderId(missionPerson);
                settlementAuditInformation1.setDelFlag("0");
                settlementAuditInformation1.setPreparePeople(missionPerson);
                settlementAuditInformation1.setBaseProjectId(baseProject.getId());
                settlementAuditInformation1.setContract("2");
                settlementAuditInformationDao.insertSelective(settlementAuditInformation1);

                SettlementInfo settlementInfo = new SettlementInfo();
                settlementInfo.setId(UUID.randomUUID().toString().replace("-",""));
                settlementInfo.setBaseProjectId(baseProject.getId());
                settlementInfo.setCreateTime(s.format(new Date()));
                settlementInfo.setFouderId(missionPerson);
                settlementInfo.setState("0");
                settlementInfo.setUpAndDown("1");
                settlementInfoMapper.insertSelective(settlementInfo);

                SettlementInfo settlementInfo1 = new SettlementInfo();
                settlementInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                settlementInfo1.setBaseProjectId(baseProject.getId());
                settlementInfo1.setCreateTime(s.format(new Date()));
                settlementInfo1.setFouderId(missionPerson);
                settlementInfo1.setState("0");
                settlementInfo1.setUpAndDown("2");
                settlementInfoMapper.insertSelective(settlementInfo1);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个结算项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的结算，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的结算，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的结算，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else {
                throw new RuntimeException("当前工程结算任务已存在");
            }
        }else if("7".equals(missionType)){
            String projectName = baseProject.getProjectName();
            Example example = new Example(MaintenanceProjectInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("maintenanceItemName",projectName);
            c.andEqualTo("delFlag","0");
            MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectOneByExample(example);
            if (maintenanceProjectInformation == null){
                MaintenanceProjectInformation maintenanceProjectInformation1 = new MaintenanceProjectInformation();
                maintenanceProjectInformation1.setId(UUID.randomUUID().toString().replace("-",""));
                maintenanceProjectInformation1.setMaintenanceItemName(baseProject.getProjectName());
                maintenanceProjectInformation1.setPreparePeople(missionPerson);
                maintenanceProjectInformation1.setProjectAddress(baseProject.getDistrict());
                maintenanceProjectInformation1.setConstructionUnitId(baseProject.getConstructionUnit());
                maintenanceProjectInformation1.setCustomerName(baseProject.getCustomerName());
                maintenanceProjectInformation1.setCreateTime(s.format(new Date()));
                maintenanceProjectInformation1.setPreparePeople(missionPerson);
                maintenanceProjectInformation1.setDelFlag("0");
                maintenanceProjectInformation1.setFounderId(missionPerson);
                maintenanceProjectInformation1.setType("2");
                maintenanceProjectInformationMapper.insertSelective(maintenanceProjectInformation1);

                SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
                settlementAuditInformation.setId(UUID.randomUUID().toString().replace("-",""));
                settlementAuditInformation.setOutsourcing("2");
                settlementAuditInformation.setMaintenanceProjectInformation(maintenanceProjectInformation1.getId());
                settlementAuditInformation.setCreateTime(s.format(new Date()));
                settlementAuditInformation.setPreparePeople(missionPerson);
                settlementAuditInformation.setFounderId(missionPerson);
                settlementAuditInformation.setDelFlag("0");
                settlementAuditInformationDao.insertSelective(settlementAuditInformation);

                InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
                investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-",""));
                investigationOfTheAmount.setMaintenanceProjectInformation(maintenanceProjectInformation1.getId());
                investigationOfTheAmount.setCreateTime(s.format(new Date()));
                investigationOfTheAmount.setFounderId(missionPerson);
                investigationOfTheAmount.setDelFlag("0");
                investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);

                MessageVo messageVo = new MessageVo();
                messageVo.setId("A24");
                messageVo.setUserId(missionPerson);
                messageVo.setType("1"); //风险
                messageVo.setTitle("您收到一个检维修项目");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent("您好！您收一个"+baseProject.getProjectName()+"项目的检维修，请及时处理！");
                messageVo.setContent("您好！您收一个"+baseProject.getProjectName()+"项目的检维修，请及时处理！");
                messageVo.setDetails("您好！您收一个"+baseProject.getProjectName()+"项目的检维修，请及时处理！");
                messageService.sendOrClose(messageVo);

            }else {
                throw new RuntimeException("当前工程检维修任务已存在");
            }

        }
    }

    public List<MkyUser> findPersonAll(String deptId) {
       return mkyUserMapper.findPersonAll(deptId);
    }
}

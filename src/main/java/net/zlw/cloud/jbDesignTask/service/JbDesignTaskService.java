package net.zlw.cloud.jbDesignTask.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.ProjectExploration;
import net.zlw.cloud.jbDesignTask.dao.DiameterInfoDao;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;
import net.zlw.cloud.jbDesignTask.domain.vo.*;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class JbDesignTaskService {

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private DesignInfoMapper designInfoMapper;

    @Autowired
    private ProjectExplorationMapper projectExplorationMapper;

    @Autowired
    private DiameterInfoDao diameterInfoDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private BudgetingDao budgetingDao;

    @Autowired
    private MemberManageDao memberManageDao;
    @Autowired
    private CostPreparationDao costPreparationDao;

    @Autowired
    private VeryEstablishmentDao veryEstablishmentDao;
    @Autowired
    private OperationLogDao operationLogDao;


    /***
     * 设计报装
     * @param jbDesignVoF
     */
        public void getDesignEngineering(JbDesignVoF jbDesignVoF, HttpServletRequest request) throws Exception {

        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        JbDesignVo designVo = jbDesignVoF.getDesignVo();
        BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(designVo.getId());
        if (baseProject1 != null) {
            BaseProject project = new BaseProject();
            project.setBudgetStatus("4");
            if (StringUtils.isNotEmpty(project.getProjectFlow())){
                project.setProjectFlow(project.getProjectFlow() + ",1");
            } else {
                project.setProjectFlow("1");
            }
            project.setProjectId(designVo.getProject_id());
            project.setProjectName(designVo.getProject_name());
            project.setFounderId(jbDesignVoF.getAccount());
            project.setUpdateTime(date);
            project.setDistrict("4"); //吴江
            baseProjectDao.updateByPrimaryKeySelective(project);
            //设计表信息
            DesignInfo designInfo = new DesignInfo();
            if (designVo.getTake_time() != null) {
                designInfo.setId(designVo.getId());
                designInfo.setBaseProjectId(project.getProjectId());
                designInfo.setDesigner(designVo.getDesigner());
                designInfo.setTakeTime(designVo.getTake_time());
                designInfo.setFounderId(designVo.getFounder_id());
                designInfo.setCreateTime(designVo.getCreate_time());
                designInfo.setRemark(designVo.getRemarks());
                designInfo.setOutsource("1");
                designInfo.setStatus("0");
                designInfoMapper.insertSelective(designInfo);
            }

            //勘探表信息
            ProjectExploration projectExploration = new ProjectExploration();
            if (designVo.getExploration_ideal() != null) {
                projectExploration.setId(designVo.getId());
                projectExploration.setBaseProjectId(designInfo.getId());
                projectExploration.setExplorationIdeal(designVo.getExploration_ideal());
                projectExploration.setExplorationTime(designVo.getExploration_time());
                projectExploration.setCreateTime(date);
                projectExploration.setUpdateTime(date);
                projectExploration.setStatus("0");
                projectExplorationMapper.insertSelective(projectExploration);
            }

            //水表信息
            DiameterInfo diameterInfo = new DiameterInfo();
            List<DiameterInfos> diameterInfos = designVo.getDiameterInfo();
            for (DiameterInfos thisInfo : diameterInfos) {
                if (thisInfo.getId() != null) {
                    diameterInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    diameterInfo.setProjectId(thisInfo.getProject_id());
                    diameterInfo.setType(thisInfo.getType());
                    diameterInfo.setDiameterMeter(thisInfo.getDiameter_meter());
                    diameterInfo.setCreateTime(date);
                    diameterInfo.setUpdateTime(date);
                    diameterInfo.setStatus("0");
                    diameterInfoDao.insertSelective(diameterInfo);
                }
            }

            //上传信息集合
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = designVo.getFileInfo();
            for (FileInfos thisFileInfos : fileInfos) {
                if (thisFileInfos.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    fileInfo.setPlatCode(thisFileInfos.getProject_id());
                    fileInfo.setFileName(thisFileInfos.getOpinions_file_name());
                    fileInfo.setCreateTime(thisFileInfos.getOpinions_up_time());
                    fileInfo.setUserId(thisFileInfos.getOpinions_up_by());
                    fileInfo.setFilePath(thisFileInfos.getOpinions_link());
                    fileInfo.setType(thisFileInfos.getType());
                    fileInfo.setStatus("0");
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(jbDesignVoF.getAccount());
            String memberName = "";
            if (memberManage != null) {
                memberName = memberManage.getMemberName();
            }


            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-", ""));
            operationLog.setType("17"); // 错误类型
            operationLog.setDoTime(date);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLog.setStatus("0");
            operationLog.setName(jbDesignVoF.getAccount());
            operationLog.setDoObject(designVo.getId());
            operationLog.setContent("对接过来一个江北工程系统项目编号重复了！【" + designVo.getId() + "】");
            operationLogDao.insertSelective(operationLog);


        } else {
            if (designVo != null) {
                //项目基本表数据
                BaseProject baseProject = new BaseProject();
                baseProject.setId(designVo.getProject_id());
                baseProject.setSite(designVo.getAddress());
                baseProject.setProjectNum(designVo.getProject_id());
                baseProject.setProjectId(designVo.getProject_id());
                baseProject.setProjectName(designVo.getProject_name());
                baseProject.setCustomerName(designVo.getCustomer_name());
                baseProject.setCustomerAddress(designVo.getCustomer_address());
                baseProject.setDesignCategory(designVo.getDesign_category());
                baseProject.setProjectNature(designVo.getProject_nature());
                baseProject.setContactNumber(designVo.getContact_number());
                baseProject.setCustomerPhone(designVo.getCustomer_phone());
                baseProject.setAgent(designVo.getAgent());
                baseProject.setCeaNum(designVo.getCea_num());
                baseProject.setDesginStatus("4");
                baseProject.setProjectFlow("1");
                baseProject.setDistrict("3");
                baseProject.setDelFlag("0");
                baseProject.setCreateTime(date);
                baseProject.setUpdateTime(date);
                baseProject.setAmountPaid(designVo.getAmount_paid());
                baseProjectDao.insertSelective(baseProject);

            //设计表信息
            DesignInfo designInfo = new DesignInfo();
            if (designVo.getTake_time() != null) {
                designInfo.setId(designVo.getId());
                designInfo.setBaseProjectId(baseProject.getProjectId());
                designInfo.setDesigner(designVo.getDesigner());
                designInfo.setTakeTime(designVo.getTake_time());
                designInfo.setFounderId(designVo.getFounder_id());
                designInfo.setCreateTime(designVo.getCreate_time());
                designInfo.setRemark(designVo.getRemarks());
                designInfo.setOutsource("1");
                designInfo.setStatus("0");
                designInfoMapper.insertSelective(designInfo);
            }

            //勘探表信息
            ProjectExploration projectExploration = new ProjectExploration();
            if (designVo.getExploration_ideal() != null) {
                projectExploration.setId(designVo.getId());
                projectExploration.setBaseProjectId(designInfo.getId());
                projectExploration.setExplorationIdeal(designVo.getExploration_ideal());
                projectExploration.setExplorationTime(designVo.getExploration_time());
                projectExploration.setCreateTime(date);
                projectExploration.setUpdateTime(date);
                projectExploration.setStatus("0");
                projectExplorationMapper.insertSelective(projectExploration);
            }

            //水表信息
            DiameterInfo diameterInfo = new DiameterInfo();
            List<DiameterInfos> diameterInfos = designVo.getDiameterInfo();
            for (DiameterInfos thisInfo : diameterInfos) {
                if (thisInfo.getId() != null) {
                    diameterInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    diameterInfo.setProjectId(thisInfo.getProject_id());
                    diameterInfo.setType(thisInfo.getType());
                    diameterInfo.setDiameterMeter(thisInfo.getDiameter_meter());
                    diameterInfo.setCreateTime(date);
                    diameterInfo.setUpdateTime(date);
                    diameterInfo.setStatus("0");
                    diameterInfoDao.insertSelective(diameterInfo);
                }
            }

            //上传信息集合
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = designVo.getFileInfo();
            for (FileInfos thisFileInfos : fileInfos) {
                if (thisFileInfos.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                    fileInfo.setPlatCode(thisFileInfos.getProject_id());
                    fileInfo.setFileName(thisFileInfos.getOpinions_file_name());
                    fileInfo.setCreateTime(thisFileInfos.getOpinions_up_time());
                    fileInfo.setUserId(thisFileInfos.getOpinions_up_by());
                    fileInfo.setFilePath(thisFileInfos.getOpinions_link());
                    fileInfo.setType(thisFileInfos.getType());
                    fileInfo.setStatus("0");
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(jbDesignVoF.getAccount());
            String memberName = "";
            if (memberManage != null) {
                memberName = memberManage.getMemberName();
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-", ""));
            operationLog.setName(jbDesignVoF.getAccount());
            operationLog.setContent(memberName + "对接了一个" + designVo.getProject_name() + "项目" + "【" + designVo.getProject_id() + "】");
            operationLog.setDoObject(designVo.getProject_id());
            operationLog.setStatus("0");
            operationLog.setDoTime(date);
            operationLog.setType("11"); // 设计报装
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);

        }else{
                throw new RuntimeException("参数有误");
        }
    }
    }

    /***
     * 江北造价
     * @param
     * @param
     */
    public void getBudgetEngineering(JbBudgetVoF jbBudgetVoF,HttpServletRequest request) throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        JbBudgetVo budgetVo = jbBudgetVoF.getBudgetVo();
        BaseProject project = baseProjectDao.selectByPrimaryKey(budgetVo.getProject_id());

        if (project != null) {
            BaseProject baseProject = new BaseProject();
            baseProject.setId(budgetVo.getId());
            baseProject.setProjectId(budgetVo.getProject_id());
            baseProject.setProjectNum(budgetVo.getProject_id());
            baseProject.setProjectName(budgetVo.getProject_name());
            baseProject.setBudgetStatus("4");
            if (StringUtils.isNotEmpty(baseProject.getProjectFlow())){
                baseProject.setProjectFlow(baseProject.getProjectFlow() + ",2");
            } else {
                baseProject.setProjectFlow("2");
            }
            baseProject.setDistrict("3"); //江北
            baseProject.setDelFlag("0");
            baseProject.setUpdateTime(date);
            baseProjectDao.updateByPrimaryKeySelective(baseProject);

            //预算信息
            Budgeting budgeting = new Budgeting();
            budgeting.setId(budgetVo.getId());
            budgeting.setBaseProjectId(baseProject.getId());
            budgeting.setBudgetingPeople(budgetVo.getBudgeting_people());
            budgeting.setReceiptTime(budgetVo.getReceipt_time());
            budgeting.setFounderId(budgetVo.getFounder_id());
            budgeting.setRemarkes(budgetVo.getRemark());
            budgeting.setCreateTime(budgetVo.getProject_name());
            String amountCost = budgetVo.getAmount_cost();
            BigDecimal cost = new BigDecimal(amountCost);
            budgeting.setAmountCost(cost);
            budgeting.setSureResult(budgetVo.getSure_result());
            budgeting.setSureMan(budgetVo.getSure_man());
            budgeting.setDelFlag("0");
            budgeting.setCreateTime(date);
            budgetingDao.insertSelective(budgeting);
            //成本编制
            CostPreparation costPreparation = new CostPreparation();
            costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
            costPreparation.setBudgetingId(budgeting.getId());
            costPreparation.setBaseProjectId(baseProject.getId());
            costPreparation.setCostPreparationTime(date);
            costPreparation.setCostTogether(jbBudgetVoF.getAccount());
            costPreparation.setDelFlag("0");
            costPreparation.setCreateTime(date);
            costPreparation.setCostTotalAmount(new BigDecimal(321));
            costPreparation.setVatAmount(new BigDecimal(213));
            costPreparation.setRemarkes("13421");
            costPreparationDao.insertSelective(costPreparation);

            // 控价编制
            VeryEstablishment veryEstablishment = new VeryEstablishment();
            veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
            veryEstablishment.setBiddingPriceControl(new BigDecimal(213));
            veryEstablishment.setVatAmount(new BigDecimal(3214));
            veryEstablishment.setPricingTogether(jbBudgetVoF.getAccount());
            veryEstablishment.setBaseProjectId(baseProject.getId());
            veryEstablishment.setBudgetingId(budgeting.getId());
            veryEstablishment.setCreateTime(date);
            veryEstablishment.setDelFlag("0");
            veryEstablishmentDao.insertSelective(veryEstablishment);

            //上传附件信息
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = budgetVo.getFileInfo();
            for (FileInfos thisInfo : fileInfos) {
                if (thisInfo.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setPlatCode(thisInfo.getProject_id());
                    fileInfo.setFileName(thisInfo.getOpinions_file_name());
                    fileInfo.setCreateTime(thisInfo.getOpinions_up_time());
                    fileInfo.setUserId(thisInfo.getOpinions_up_by());
                    fileInfo.setFilePath(thisInfo.getOpinions_link());
                    fileInfo.setStatus("0");
                    fileInfo.setType("jbbzscfjxx");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(jbBudgetVoF.getAccount());
            String memberName = "";
            if(memberManage != null){
                memberName = memberManage.getMemberName();
            }

            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-", ""));
            operationLog.setType("17"); // 错误类型
            operationLog.setDoTime(date);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLog.setStatus("0");
            operationLog.setName(jbBudgetVoF.getAccount());
            operationLog.setDoObject(budgetVo.getId());
            operationLog.setContent("对接过来一个江北工程系统项目编号重复了！【" + budgetVo.getId() + "】");
            operationLogDao.insertSelective(operationLog);
            }else {

            if (budgetVo != null) {

                BaseProject baseProject = new BaseProject();
                baseProject.setId(budgetVo.getId());
                baseProject.setProjectId(budgetVo.getProject_id());
                baseProject.setProjectNum(budgetVo.getProject_id());
                baseProject.setProjectName(budgetVo.getProject_name());
                baseProject.setBudgetStatus("4");
                baseProject.setProjectFlow("2");
                baseProject.setDistrict("3"); //江北
                baseProject.setDelFlag("0");
                baseProject.setCreateTime(date);
                baseProjectDao.insertSelective(baseProject);

                //预算信息
                Budgeting budgeting = new Budgeting();
                budgeting.setId(budgetVo.getId());
                budgeting.setBaseProjectId(baseProject.getId());
                budgeting.setBudgetingPeople(budgetVo.getBudgeting_people());
                budgeting.setReceiptTime(budgetVo.getReceipt_time());
                budgeting.setFounderId(budgetVo.getFounder_id());
                budgeting.setRemarkes(budgetVo.getRemark());
                budgeting.setCreateTime(budgetVo.getProject_name());
                String amountCost = budgetVo.getAmount_cost();
                BigDecimal cost = new BigDecimal(amountCost);
                budgeting.setAmountCost(cost);
                budgeting.setSureResult(budgetVo.getSure_result());
                budgeting.setSureMan(budgetVo.getSure_man());
                budgeting.setDelFlag("0");
                budgeting.setCreateTime(date);
                budgetingDao.insertSelective(budgeting);
            //成本编制
            CostPreparation costPreparation = new CostPreparation();
            costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
            costPreparation.setBudgetingId(budgeting.getId());
            costPreparation.setBaseProjectId(baseProject.getId());
            costPreparation.setCostPreparationTime(date);
            costPreparation.setCostTogether(jbBudgetVoF.getAccount());
            costPreparation.setDelFlag("0");
            costPreparation.setCostTotalAmount(new BigDecimal(321));
            costPreparation.setVatAmount(new BigDecimal(213));
            costPreparation.setRemarkes("13421");
            costPreparation.setCreateTime(date);
            costPreparationDao.insertSelective(costPreparation);

            // 控价编制
            VeryEstablishment veryEstablishment = new VeryEstablishment();
            veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
            veryEstablishment.setBiddingPriceControl(new BigDecimal(213));
            veryEstablishment.setVatAmount(new BigDecimal(3214));
            veryEstablishment.setPricingTogether(jbBudgetVoF.getAccount());
            veryEstablishment.setBaseProjectId(baseProject.getId());
            veryEstablishment.setBudgetingId(budgeting.getId());
            veryEstablishment.setCreateTime(date);
            veryEstablishment.setDelFlag("0");
            veryEstablishmentDao.insertSelective(veryEstablishment);

            //上传附件信息
            FileInfo fileInfo = new FileInfo();
            List<FileInfos> fileInfos = budgetVo.getFileInfo();
            for (FileInfos thisInfo : fileInfos) {
                if (thisInfo.getId() != null) {
                    fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    fileInfo.setPlatCode(thisInfo.getProject_id());
                    fileInfo.setFileName(thisInfo.getOpinions_file_name());
                    fileInfo.setCreateTime(thisInfo.getOpinions_up_time());
                    fileInfo.setUserId(thisInfo.getOpinions_up_by());
                    fileInfo.setFilePath(thisInfo.getOpinions_link());
                    fileInfo.setStatus("0");
                    fileInfo.setType("jbbzscfjxx");
                }
                fileInfoMapper.insertSelective(fileInfo);
            }
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(jbBudgetVoF.getAccount());
            String memberName = "";
            if(memberManage != null){
                memberName = memberManage.getMemberName();
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-",""));
            operationLog.setName(jbBudgetVoF.getAccount());
            operationLog.setContent(memberName+"对接了一个"+budgetVo.getProject_name()+"项目"+"【"+budgetVo.getProject_id()+"】");
            operationLog.setDoObject(budgetVo.getProject_id());
            operationLog.setStatus("0");
            operationLog.setType("12"); //预算报装
            operationLog.setDoTime(date);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }
            else{
                throw new RuntimeException("参数有误");
            }
        }
    }

    public void updateBudgetAmount(AmountVo amountVo,HttpServletRequest request){
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String budgetAmount = amountVo.getBudget_amount();
        if (amountVo.getProject_id() != null && !"".equals(amountVo.getProject_id())){
            Example example = new Example(Budgeting.class);
            example.createCriteria().andEqualTo("baseProjectId",amountVo.getProject_id())
                                    .andEqualTo("delFlag","0");
            Budgeting budgeting = budgetingDao.selectOneByExample(example);
            BaseProject baseProject = null;
            if (budgeting != null){
                baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
                budgeting.setAmountCost(new BigDecimal(budgetAmount));
                budgeting.setUpdateTime(data);
                budgetingDao.updateByPrimaryKeySelective(budgeting);
            }
            if(baseProject != null){
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(amountVo.getAccount());
                String memberName = "";
                if(memberManage != null){
                    memberName = memberManage.getMemberName();
                }
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                operationLog.setName(amountVo.getAccount());
                operationLog.setContent(memberName+"修改了"+baseProject.getProjectName()+"项目的造价金额"+"【"+baseProject.getId()+"】");
                operationLog.setDoObject(baseProject.getId());
                operationLog.setStatus("0");
                operationLog.setType("13"); //修改金额
                operationLog.setDoTime(data);
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);
            }
        }
    }

    // 更新cea接口
    public void updateCea(CEAVo ceaVo,HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (ceaVo != null){
            Example example = new Example(BaseProject.class);
            example.createCriteria().andEqualTo("projectId",ceaVo.getProject_id())
                                    .andEqualTo("delFlag","0");
            BaseProject baseProject = baseProjectDao.selectOneByExample(example);
            if (baseProject != null){
                baseProject.setCeaNum(ceaVo.getCea());
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                MemberManage memberManage = memberManageDao.selectByPrimaryKey(ceaVo.getAccount());
                String memberName = "";
                if(memberManage != null){
                    memberName = memberManage.getMemberName();
                }
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                operationLog.setName(ceaVo.getAccount());
                operationLog.setContent(memberName+"修改了"+baseProject.getProjectName()+"项目的CEA编号"+"【"+baseProject.getId()+"】");
                operationLog.setDoObject(baseProject.getId());
                operationLog.setStatus("0");
                operationLog.setType("14"); //CEA编号
                operationLog.setDoTime(data);
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);
            }
        }
    }
}

package net.zlw.cloud.jbDesignTask.service;

import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.PackageCameMapper;
import net.zlw.cloud.designProject.mapper.ProjectExplorationMapper;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.excel.dao.MaterialAnalysisDao;
import net.zlw.cloud.excel.model.MaterialAnalysis;
import net.zlw.cloud.followAuditing.mapper.TrackApplicationInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.jbDesignTask.domain.proVo.*;
import net.zlw.cloud.jbDesignTask.domain.vo.JbBudgetVoF;
import net.zlw.cloud.jbDesignTask.domain.vo.WjBudgetVoF;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementInfoMapper;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.rmi.server.ExportException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WjEngineeringService {
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private OperationLogDao operationLogDao;
    @Resource
    private MaterialAnalysisDao materialAnalysisDao;
    @Resource
    private MemberService memberService;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private TrackApplicationInfoDao trackApplicationInfoDao;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;
    @Resource
    private TrackMonthlyDao trackMonthlyDao;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private CostPreparationDao costPreparationDao;
    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;
    @Resource
    private FileInfoMapper fileInfoMapper;
    @Resource
    private ProjectExplorationMapper projectExplorationMapper;
    @Resource
    private PackageCameMapper packageCameMapper;
    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private SettlementInfoMapper settlementInfoMapper;


    public void getWjProjectEngineering(WjDesignVoA wjDesignVoA, HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        DesignVo designVo = wjDesignVoA.getDesignVo();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(designVo.getId());
        if (baseProject!= null){
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-",""));
            operationLog.setType("17"); // 错误类型
            operationLog.setDoTime(data);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLog.setStatus("0");
            operationLog.setName(wjDesignVoA.getAccount());
            operationLog.setDoObject(designVo.getId());
            operationLog.setContent("对接过来一个吴江工程系统项目编号重复了！【"+designVo.getId()+"】");
            operationLogDao.insertSelective(operationLog);
        }else{
            if (designVo != null){
                //基本信息表
                BaseProject project = new BaseProject();
                project.setId(designVo.getId());
                project.setProjectNum(designVo.getBase_project_id());
                project.setProjectName(designVo.getProject_name());
                project.setDelFlag("0");
                project.setDistrict("4"); //吴江
                project.setCreateTime(data);
                project.setDesginStatus("4");
                baseProjectDao.insertSelective(project);
                // 设计表
                DesignInfo designInfo = new DesignInfo();
                designInfo.setId(designVo.getId());
                designInfo.setBlueprintCountersignTime(designVo.getScene_time());
                designInfo.setYearDesignUnit(designVo.getAnnual_design_uti());
                designInfo.setDesignUnit(designVo.getDesign_util());
                designInfo.setBaseProjectId(project.getId());
                designInfo.setStatus("0");
                designInfo.setCreateTime(data);
                designInfoMapper.insertSelective(designInfo);
                //项目勘察
                ProjectExploration exploration = new ProjectExploration();
                exploration.setId(UUID.randomUUID().toString().replace("-",""));
                exploration.setScout(designVo.getSurveyor());
                exploration.setExplorationTime(designVo.getSurvey_time());
                exploration.setSite(designVo.getAddress());
                exploration.setRemark(designVo.getRemark());
                exploration.setStatus("0");
                exploration.setCreateTime(data);
                exploration.setBaseProjectId(designInfo.getId());
                projectExplorationMapper.insertSelective(exploration);
                //方案会审
                PackageCame packageCame = new PackageCame();
                packageCame.setId(UUID.randomUUID().toString().replace("-",""));
                packageCame.setParticipant(designVo.getParticipants());
                packageCame.setRemark(designVo.getRemark());
                packageCame.setBassProjectId(designInfo.getId());
                packageCame.setStatus("0");
                packageCame.setCreateTime(data);
                packageCameMapper.insertSelective(packageCame);
                //现场照片附件
                List<ScenePhotosList> scenePhotosList = designVo.getScenePhotosList();
                if (scenePhotosList.size()>0){
                    for (ScenePhotosList photosList : scenePhotosList) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(photosList.getScene_photos_file_name());
                        fileInfo.setFileType(photosList.getScene_photos_type());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(photosList.getScene_photos_up_time());//上传时间
                        fileInfo.setUserId(photosList.getScene_photos_up_by());
                        fileInfo.setFilePath(photosList.getScene_photos_link());
                        fileInfo.setPlatCode(project.getId());
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //方案会审-参会意见
                List<OpinionsList> opinionsList = designVo.getOpinionsList();
                if (opinionsList.size()>0){
                    for (OpinionsList list : opinionsList) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(list.getOpinions_file_name());
                        fileInfo.setFileType(list.getOpinions_type());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(list.getOpinions_up_time());//上传时间
                        fileInfo.setUserId(list.getOpinions_up_by());
                        fileInfo.setFilePath(list.getOpinions_link());
                        fileInfo.setPlatCode(project.getId());
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }

                }
                //设计图纸/附件
                List<DesignDrawingsList> designDrawingsList = designVo.getDesignDrawingsList();
                if (designDrawingsList.size()>0){
                    for (DesignDrawingsList drawingsList : designDrawingsList) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(drawingsList.getDesign_drawings_file_name());
                        fileInfo.setFileType(drawingsList.getDesign_drawings_type());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(drawingsList.getDesign_drawings_up_time());//上传时间
                        fileInfo.setUserId(drawingsList.getDesign_drawings_up_by());
                        fileInfo.setFilePath(drawingsList.getDesign_drawings_link());
                        fileInfo.setPlatCode(project.getId());
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                operationLog.setType("18"); // 吴江工程报装
                operationLog.setDoTime(data);
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLog.setStatus("0");
                operationLog.setName(wjDesignVoA.getAccount());
                operationLog.setDoObject(designVo.getId());
                operationLog.setContent("对接过来一个吴江工程系统【"+designVo.getId()+"】");
                operationLogDao.insertSelective(operationLog);
            }else {
                throw new RuntimeException("参数有误");
            }
        }

    }

    public void getWjBudgetTask(WjBudgetVoA wjBudgetVoA, HttpServletRequest request) {
        BudgetVo budgetVo = wjBudgetVoA.getBudgetVo();
        if (budgetVo != null){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgetVo.getBase_project_id());
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (baseProject != null){
                baseProject.setBudgetStatus("4");
                baseProject.setProjectName(budgetVo.getProject_name());
                baseProject.setDistrict("4"); //吴江
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
                Budgeting budgeting = new Budgeting();
                budgeting.setId(budgetVo.getId());
                budgeting.setBaseProjectId(baseProject.getId());
                budgeting.setDelFlag("0");
                budgeting.setCreateTime(data);
                budgeting.setNameOfCostUnit(budgetVo.getName_of_cost_unit());
                budgeting.setRemarkes(budgetVo.getRemark());
                budgeting.setBudgetingTime(budgetVo.getBudgeting_time());
                budgetingDao.insertSelective(budgeting);
                //成本编制
                CostPreparation costPreparation = new CostPreparation();
                costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
                costPreparation.setBaseProjectId(baseProject.getId());
                costPreparation.setBudgetingId(budgeting.getId());
                costPreparation.setDelFlag("0");
                costPreparation.setCostTotalAmount(new BigDecimal(0));
                costPreparation.setVatAmount(new BigDecimal(0));
                costPreparation.setTotalPackageMaterial(new BigDecimal(0));
                costPreparationDao.insertSelective(costPreparation);
                //控价编制
                VeryEstablishment veryEstablishment = new VeryEstablishment();
                veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
                veryEstablishment.setBudgetingId(budgeting.getId());
                veryEstablishment.setBaseProjectId(baseProject.getId());
                veryEstablishment.setDelFlag("0");
                veryEstablishment.setPricingTogether(wjBudgetVoA.getAccount());
                veryEstablishmentDao.insertSelective(veryEstablishment);
                //甲供材附件
                List<MaterialsAList> materialsAList = budgetVo.getMaterialsAList();
                if (materialsAList.size()>0){
                    for (MaterialsAList aList : materialsAList) {
                        MaterialAnalysis materialAnalysis = new MaterialAnalysis();
                        materialAnalysis.setId(UUID.randomUUID().toString().replace("-",""));
                        materialAnalysis.setMaterialsACode(aList.getMaterials_a_code());
                        materialAnalysis.setMaterialName(aList.getMaterials_a_name());
                        materialAnalysis.setSpecifications(aList.getMaterials_a_type());
                        materialAnalysis.setUnit(aList.getMaterials_a_unit());
                        materialAnalysis.setOutboundOrderQuantity(aList.getMaterials_a_num());
                        materialAnalysis.setContractPrice(new BigDecimal(aList.getMaterials_a_price()));
                        materialAnalysis.setMoreAmount(new BigDecimal(aList.getMaterials_a_money()));
                        materialAnalysis.setMaterialsANvCode(aList.getMaterials_a_nv_code());
                        materialAnalysis.setMaterialsANvName(aList.getMaterials_a_nv_name());
                        materialAnalysis.setMaterialsANcTtpe(aList.getMaterials_a_nc_ttpe());
                        materialAnalysis.setMaterialsALink(aList.getMaterials_a_link());
                        materialAnalysis.setSettlementId(budgeting.getId());
                        materialAnalysis.setDelFlag("0");
                        materialAnalysis.setCreateTime(data);
                        materialAnalysisDao.insertSelective(materialAnalysis);
                    }
                }
                // 预算附件
                List<BudgetFileList> budgetFileList = budgetVo.getBudgetFileList();
                if (budgetFileList.size()>0){
                    for (BudgetFileList fileList : budgetFileList) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(fileList.getBudget_file_name());
                        fileInfo.setUpdateTime(fileList.getBudget_file_up_time());
                        fileInfo.setUserId(fileList.getBudget_file_up_by());
                        fileInfo.setFilePath(fileList.getBudget_file_link());
                        fileInfo.setStatus("0");
                        fileInfo.setFilePath(budgeting.getId());
                        fileInfo.setCreateTime(data);
                        fileInfo.setType("yjsfjxx"); //预结算附件信息
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                    operationLog.setType("19"); // 吴江预算任务
                    operationLog.setDoTime(data);
                    String ip = memberService.getIp(request);
                    operationLog.setIp(ip);
                    operationLog.setStatus("0");
                    operationLog.setName(wjBudgetVoA.getAccount());
                    operationLog.setDoObject(budgeting.getId());
                    operationLog.setContent("对接过来一个吴江预算任务【"+baseProject.getId()+"】");
                    operationLogDao.insertSelective(operationLog);
                }
            }
        }else {
            throw new RuntimeException("参数有误");
        }
    }

    public void getSettlementEngineering(WjSettlementVoA wjSettlementVoA, HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        SettlementVo settlementVo = wjSettlementVoA.getSettlementVo();
        if (settlementVo != null){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(settlementVo.getBase_project_id());
            if (baseProject != null){
                baseProject.setDistrict("4");
                baseProject.setProjectName(settlementVo.getProject_name());
                baseProject.setSettleAccountsStatus("5");
                baseProject.setCeaNum(settlementVo.getCea_num());
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
                //下家
                SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
                settlementAuditInformation.setId(settlementVo.getId());
                settlementAuditInformation.setAuditFee(settlementVo.getAudit_fee());
                settlementAuditInformation.setAuditFeeMaterials(settlementVo.getAudit_fee_materials());
                settlementAuditInformation.setAudiConstruction(settlementVo.getAudi_construction());
                settlementAuditInformation.setBidSection(settlementVo.getBid_section());
                settlementAuditInformation.setRemarkes(settlementVo.getRemarks());
                settlementAuditInformation.setAuthorizedNumber(new BigDecimal(settlementVo.getReview_number()));
                settlementAuditInformation.setDelFlag("0");
                settlementAuditInformation.setCreateTime(data);
                settlementAuditInformation.setBaseProjectId(baseProject.getId());
                settlementAuditInformationDao.insertSelective(settlementAuditInformation);

                SettlementInfo upSettlementInfo = new SettlementInfo();
                upSettlementInfo.setId(UUID.randomUUID().toString().replace("-",""));
                upSettlementInfo.setUpAndDown("1");
                upSettlementInfo.setBaseProjectId(baseProject.getId());
                upSettlementInfo.setState("0");
                upSettlementInfo.setCreateTime(data);
                settlementInfoMapper.insertSelective(upSettlementInfo);

                SettlementInfo downSettlementInfo = new SettlementInfo();
                downSettlementInfo.setId(UUID.randomUUID().toString().replace("-",""));
                downSettlementInfo.setUpAndDown("2");
                downSettlementInfo.setBaseProjectId(baseProject.getId());
                downSettlementInfo.setState("0");
                downSettlementInfo.setCreateTime(data);
                settlementInfoMapper.insertSelective(downSettlementInfo);
                //上家
                net.zlw.cloud.settleAccounts.model.LastSettlementReview lastSettlementReview = new net.zlw.cloud.settleAccounts.model.LastSettlementReview();
                lastSettlementReview.setId(settlementVo.getId());
                lastSettlementReview.setBaseProjectId(baseProject.getId());
                lastSettlementReview.setRemark(settlementVo.getRemark());
                lastSettlementReview.setDelFlag("0");
                lastSettlementReview.setCreateTime(data);
                lastSettlementReview.setAccountId(settlementAuditInformation.getId());
                lastSettlementReviewDao.insertSelective(lastSettlementReview);
                // 勘察金额
                InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
                investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-",""));
                investigationOfTheAmount.setBaseProjectId(baseProject.getId());
                investigationOfTheAmount.setCreateTime(data);
                investigationOfTheAmount.setSurveyDate("");
                investigationOfTheAmount.setDelFlag("0");
                investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);
                //项目组资料见详
                List<ProjectInformation> projectInformation = settlementVo.getProjectInformation();
                if (projectInformation.size() >0){
                    for (ProjectInformation information : projectInformation) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(information.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(information.getFile_up_time());
                        fileInfo.setUserId(information.getFile_up_by());
                        fileInfo.setFilePath(information.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("xmzzljx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //上家合同审计附件见详
                List<LastContractAudit> lastContractAudit = settlementVo.getLastContractAudit();
                if (lastContractAudit.size() >0){
                    for (LastContractAudit contractAudit : lastContractAudit) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(contractAudit.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(contractAudit.getFile_up_time());
                        fileInfo.setUserId(contractAudit.getFile_up_by());
                        fileInfo.setFilePath(contractAudit.getFile_link());
                        fileInfo.setPlatCode(lastSettlementReview.getId());
                        fileInfo.setType("sjhtsjfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //其他资料附件见详
                List<OtherMeans> otherMeans = settlementVo.getOtherMeans();
                if (otherMeans.size() >0){
                    for (OtherMeans otherMean : otherMeans) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(otherMean.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(otherMean.getFile_up_time());
                        fileInfo.setUserId(otherMean.getFile_up_by());
                        fileInfo.setFilePath(otherMean.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("qtzlfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //审核资料附件见详
                List<ShangExamineMeans> shangExamineMeans = settlementVo.getShangExamineMeans();
                if (shangExamineMeans.size() >0){
                    for (ShangExamineMeans shangExamineMean : shangExamineMeans) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(shangExamineMean.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(shangExamineMean.getFile_up_time());
                        fileInfo.setUserId(shangExamineMean.getFile_up_by());
                        fileInfo.setFilePath(shangExamineMean.getFile_link());
                        fileInfo.setPlatCode(lastSettlementReview.getId());
                        fileInfo.setType("shzlfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //标段审计资料附件见详
                List<BidSectionAudit> bidSectionAudit = settlementVo.getBidSectionAudit();
                if (bidSectionAudit.size()>0){
                    for (BidSectionAudit sectionAudit : bidSectionAudit) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(sectionAudit.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(sectionAudit.getFile_up_time());
                        fileInfo.setUserId(sectionAudit.getFile_up_by());
                        fileInfo.setFilePath(sectionAudit.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("bdsjzlfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //甲供材一览信息见详
                List<MaterialInformation> materialInformation = settlementVo.getMaterialInformation();
                if (materialInformation.size()>0){
                    for (MaterialInformation information : materialInformation) {
                        MaterialAnalysis materialAnalysis = new MaterialAnalysis();
                        materialAnalysis.setId(UUID.randomUUID().toString().replace("-",""));
                        materialAnalysis.setMaterialName(information.getMaterial_name());
                        materialAnalysis.setSpecifications(information.getSpecification_model());
                        materialAnalysis.setUnit(information.getCompany_of_util());
                        materialAnalysis.setTurnForQuantity(new BigDecimal(information.getAmount_provided_a()));
                        materialAnalysis.setActualConsumption(information.getActual_consumption());
                        materialAnalysis.setContractPrice(new BigDecimal(information.getPrice_by_a()));
                        materialAnalysis.setTotalMaterialsByA(information.getTotal_materials_by_a());
                        materialAnalysis.setSuperCollarMaterials(information.getSuper_collar_materials());
                        materialAnalysis.setDelFlag("0");
                        materialAnalysis.setSettlementId(settlementAuditInformation.getId());
                        materialAnalysisDao.insertSelective(materialAnalysis);
                    }
                }
                //审计报告附件见详
                List<AuditReport> auditReport = settlementVo.getAuditReport();
                if (auditReport.size()>0){
                    for (AuditReport thisAudit : auditReport) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(thisAudit.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(thisAudit.getFile_up_time());
                        fileInfo.setUserId(thisAudit.getFile_up_by());
                        fileInfo.setFilePath(thisAudit.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("sjbgfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //验收报告附件见详
                List<AcceptanceReport> acceptanceReport = settlementVo.getAcceptanceReport();
                if (acceptanceReport.size()>0){
                    for (AcceptanceReport report : acceptanceReport) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(report.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(report.getFile_up_time());
                        fileInfo.setUserId(report.getFile_up_by());
                        fileInfo.setFilePath(report.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("ysbgfjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
                //审核资料附件下家见详
                List<XiaExamineMeans> xiaExamineMeans = settlementVo.getXiaExamineMeans();
                if (xiaExamineMeans.size()>0){
                    for (XiaExamineMeans xiaExamineMean : xiaExamineMeans) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(UUID.randomUUID().toString().replace("-",""));
                        fileInfo.setFileName(xiaExamineMean.getFile_name());
                        fileInfo.setCreateTime(data);
                        fileInfo.setUpdateTime(xiaExamineMean.getFile_up_time());
                        fileInfo.setUserId(xiaExamineMean.getFile_up_by());
                        fileInfo.setFilePath(xiaExamineMean.getFile_link());
                        fileInfo.setPlatCode(settlementAuditInformation.getId());
                        fileInfo.setType("shzlfjxjjx");
                        fileInfo.setStatus("0");
                        fileInfoMapper.insertSelective(fileInfo);

                        OperationLog operationLog = new OperationLog();
                        operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                        operationLog.setType("20"); // 吴江结算任务
                        operationLog.setDoTime(data);
                        String ip = memberService.getIp(request);
                        operationLog.setIp(ip);
                        operationLog.setStatus("0");
                        operationLog.setName(wjSettlementVoA.getAccount());
                        operationLog.setDoObject(settlementVo.getId());
                        operationLog.setContent("对接过来一个吴江结算任务【"+baseProject.getId()+"】");
                        operationLogDao.insertSelective(operationLog);
                    }
                }
            }
        }else {
            throw new RuntimeException("参数有误");
        }
    }

    public void getTrackEngineering(WjTrackVoA wjTrackVoA, HttpServletRequest request) {
        TrackVo trackVo = wjTrackVoA.getTrackVo();
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (trackVo != null){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackVo.getBase_project_id());
            baseProject.setCeaNum(trackVo.getCea_num());
            baseProject.setProjectName(trackVo.getProject_name());
            baseProject.setTrackStatus("5");
            baseProject.setSupervisorUnit(trackVo.getSupervisor_unit());
            baseProject.setApplicationNum(trackVo.getApplicationNum());
            baseProject.setConstructionUnit(trackVo.getConstruction_unit());
            baseProject.setConstructionOrganization(trackVo.getConstruction_organization());
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            //跟踪审计
            TrackAuditInfo trackAuditInfo = new TrackAuditInfo();
            trackAuditInfo.setId(trackVo.getId());
            trackAuditInfo.setAuditUnitNameId(trackVo.getAudit_unit());
            trackAuditInfo.setDesignOrganizationId(trackVo.getDesign_unit());
            trackAuditInfo.setCreateTime(data);
            trackAuditInfo.setFounderId(wjTrackVoA.getAccount());
            trackAuditInfo.setStatus(trackVo.getStatus());
            trackAuditInfo.setBaseProjectId(baseProject.getId());
            trackAuditInfo.setContractAmount(new BigDecimal(trackVo.getContract_amount()));
            trackAuditInfo.setStatus("0");
            trackAuditInfoDao.insertSelective(trackAuditInfo);
            //申请信息
            TrackApplicationInfo trackApplicationInfo = new TrackApplicationInfo();
            trackApplicationInfo.setId(UUID.randomUUID().toString().replace("-",""));
            trackApplicationInfo.setRemark("");
            trackApplicationInfo.setCreateTime(data);
            trackApplicationInfo.setState("0");
            trackApplicationInfo.setApplicantName("");
            trackApplicationInfo.setTrackAudit(trackAuditInfo.getId());
            trackApplicationInfoDao.insertSelective(trackApplicationInfo);
            //月报
            List<MonthlyAuditReport> monthlyAuditReport = trackVo.getMonthlyAuditReport();
            if (monthlyAuditReport.size()>0){
                for (MonthlyAuditReport auditReport : monthlyAuditReport) {
                    TrackMonthly trackMonthly = new TrackMonthly();
                    trackMonthly.setId(UUID.randomUUID().toString().replace("-",""));
                    trackMonthly.setTime(auditReport.getBelong_time());
                    trackMonthly.setTitle(auditReport.getTitle());
                    trackMonthly.setPerformAmount(new BigDecimal(auditReport.getExecution_money()));
                    trackMonthly.setFillTime(auditReport.getCompleted_time());
                    trackMonthly.setTrackId(trackAuditInfo.getId());
                    trackMonthly.setStatus("0");
                    trackMonthly.setCreateTime(data);
                    trackMonthly.setWritter(auditReport.getCompleted_by());
                    trackMonthlyDao.insertSelective(trackMonthly);
                }
            }

            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-",""));
            operationLog.setType("21"); // 吴江跟踪审计任务
            operationLog.setDoTime(data);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLog.setStatus("0");
            operationLog.setName(wjTrackVoA.getAccount());
            operationLog.setDoObject(trackVo.getId());
            operationLog.setContent("对接过来一个吴江跟踪审计任务【"+baseProject.getId()+"】");
            operationLogDao.insertSelective(operationLog);
        }else {
            throw new RuntimeException("参数有误");
        }
    }
}

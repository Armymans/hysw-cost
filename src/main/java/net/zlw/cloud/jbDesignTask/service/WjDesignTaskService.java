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
import net.zlw.cloud.jbDesignTask.domain.BudgetFileInfo;
import net.zlw.cloud.jbDesignTask.domain.WjFileInfo;
import net.zlw.cloud.jbDesignTask.domain.vo.WjBudgetVo;
import net.zlw.cloud.jbDesignTask.domain.vo.WjBudgetVoF;
import net.zlw.cloud.jbDesignTask.domain.vo.WjDesignVo;
import net.zlw.cloud.jbDesignTask.domain.vo.WjDesignVoF;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WjDesignTaskService {

    @Resource
    private BaseProjectDao baseProjectDao;

    @Resource
    private OperationLogDao operationLogDao;

    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private MemberService memberService;

    @Resource
    private BudgetingDao budgetingDao;

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


    // 吴江设计报装
    public void getWjDesignTask(WjDesignVoF wjDesignVoF, HttpServletRequest request){
        WjDesignVo wjDesignVo = wjDesignVoF.getDesignVo();
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if (wjDesignVo != null){
            BaseProject project = new BaseProject();
            project.setId(wjDesignVo.getApplication_num());
            project.setApplicationNum(wjDesignVo.getApplication_num());
            project.setCustomerName(wjDesignVo.getCustomer_name());
            project.setFireTableSize(wjDesignVo.getFire_table_size());
            project.setClassificationCaliber(wjDesignVo.getClassification_caliber());
            project.setWaterMeterDiameter(wjDesignVo.getWater_meter_diameter());
            project.setDistrict("4");
            project.setStatus(wjDesignVo.getStatus());
            project.setCustomerAddress(wjDesignVo.getCustomer_address());
            project.setSubject(wjDesignVo.getCustomer_category());
            project.setSite(wjDesignVo.getSite());
            project.setCustomerEmail(wjDesignVo.getPostcode());
            project.setCustomerPhone(wjDesignVo.getPhone());
            project.setAgent(wjDesignVo.getAgent());
            project.setContactNumber(wjDesignVo.getPhone_num());
            project.setAcceptanceUnit(wjDesignVo.getAcceptance_unit());
            project.setLegalRepresentative(wjDesignVo.getLegal_representative());
            project.setCustomerResults(wjDesignVo.getCustomer_results());
            project.setTotalAmountQuotation(wjDesignVo.getTotal_amount_quotation());
            project.setLivingSurfaceDiameter(wjDesignVo.getLiving_surface_diameter());
            project.setLandCertificate(wjDesignVo.getLand_certificate());
            project.setChargeAmount(wjDesignVo.getCharge_amount());
            project.setFax(wjDesignVo.getFax());
            project.setProjectNature(wjDesignVo.getNature_water_use());
            project.setDelFlag("0");
            project.setCreateTime(data);
//            Random random = new Random(1);
//            int i = random.nextInt(100);
            project.setProjectName("吴江设计报装对接" + wjDesignVo.getApplication_num());
            project.setDesginStatus("4");
            baseProjectDao.insertSelective(project);

            DesignInfo designInfo = new DesignInfo();
            designInfo.setId(UUID.randomUUID().toString().replace("-",""));
            designInfo.setCreateTime(data);
            designInfo.setDesigner(wjDesignVoF.getAccount());
            designInfo.setStatus("0");
            designInfo.setBaseProjectId(project.getApplicationNum());
            designInfo.setFounderId(wjDesignVoF.getAccount());
            designInfoMapper.insertSelective(designInfo);

            ProjectExploration exploration = new ProjectExploration();
            exploration.setId(UUID.randomUUID().toString().replace("-",""));
            exploration.setSurveyResults(wjDesignVo.getSurvey_results());
            exploration.setExplorationIdeal(wjDesignVo.getSurvey_option());
            exploration.setScout(wjDesignVo.getSurveyor());
            exploration.setExplorationTime(wjDesignVo.getSurvey_time());
            exploration.setRemark(wjDesignVo.getRemark());
            exploration.setBaseProjectId(designInfo.getId());
            exploration.setCreateTime(data);
            exploration.setStatus("0");
            projectExplorationMapper.insertSelective(exploration);

            List<WjFileInfo> designFileList = wjDesignVo.getDesignFileList();
            if (designFileList != null && designFileList.size()>0){
                for (WjFileInfo thisFile : designFileList) {
                    FileInfo fileInfo1 = fileInfoMapper.selectByPrimaryKey(thisFile.getId());
                    if(null == fileInfo1){
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(thisFile.getId());
                        fileInfo.setFileName(thisFile.getDesign_file_name());
                        fileInfo.setCreateTime(thisFile.getDesign_up_time());
                        fileInfo.setUserId(thisFile.getDesign_up_by());
                        fileInfo.setFilePath(thisFile.getDesign_link());
                        fileInfo.setType("wjsjbzwjsc");
                        fileInfo.setStatus("0");
                        fileInfo.setPlatCode(project.getApplicationNum());
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-",""));
            operationLog.setType("15"); // 吴江设计报装
            operationLog.setDoTime(data);
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLog.setStatus("0");
            operationLog.setName(wjDesignVoF.getAccount());
            operationLog.setDoObject(wjDesignVo.getApplication_num());
            operationLog.setContent("对接过来一个吴江设计报装接口【"+wjDesignVo.getApplication_num()+"】");
            operationLogDao.insertSelective(operationLog);
        }else{
            throw new RuntimeException("参数有误");
        }

    }

    public void getWjBudgetEngineering(WjBudgetVoF wjBudgetVoF, HttpServletRequest request) {

        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        WjBudgetVo wjBudgetVo = wjBudgetVoF.getBudgetVo();
        BaseProject baseProject = new BaseProject();
        if (wjBudgetVo != null){
            String base_project_id = wjBudgetVo.getApplication_num();
            baseProject = baseProjectDao.selectByPrimaryKey(base_project_id);
            if (baseProject != null){
                baseProject.setCustomerName(wjBudgetVo.getCustomer_name());
                baseProject.setStatus(wjBudgetVo.getStatus());
                baseProject.setFireTableSize(wjBudgetVo.getFire_table_size());
                baseProject.setClassificationCaliber(wjBudgetVo.getClassification_caliber());
                baseProject.setCustomerAddress(wjBudgetVo.getCustomer_address());
                baseProject.setAcceptanceUnit(wjBudgetVo.getAcceptance_unit());
                baseProject.setLegalRepresentative(wjBudgetVo.getLegal_representative());
                baseProject.setCustomerResults(wjBudgetVo.getCustomer_results());
                baseProject.setTotalAmountQuotation(wjBudgetVo.getTotal_amount_quotation());
                baseProject.setLivingSurfaceDiameter(wjBudgetVo.getLiving_surface_diameter());
                baseProject.setSubject(wjBudgetVo.getCustomer_category());
                baseProject.setLandCertificate(wjBudgetVo.getLand_certificate());
                baseProject.setChargeAmount(wjBudgetVo.getCharge_amount());
                baseProject.setSite(wjBudgetVo.getSite());
                baseProject.setCustomerEmail(wjBudgetVo.getPostcode());
                baseProject.setAgent(wjBudgetVo.getAgent());
                baseProject.setCustomerPhone(wjBudgetVo.getPhone());
                baseProject.setContactNumber(wjBudgetVo.getPhon_num());
                baseProject.setFax(wjBudgetVo.getFax());
                baseProject.setBudgetStatus("4");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }else {
                baseProject = new BaseProject();
                baseProject.setId(UUID.randomUUID().toString().replace("-",""));
                baseProject.setCustomerName(wjBudgetVo.getCustomer_name());
                baseProject.setStatus(wjBudgetVo.getStatus());
                baseProject.setFireTableSize(wjBudgetVo.getFire_table_size());
                baseProject.setClassificationCaliber(wjBudgetVo.getClassification_caliber());
                baseProject.setCustomerAddress(wjBudgetVo.getCustomer_address());
                baseProject.setAcceptanceUnit(wjBudgetVo.getAcceptance_unit());
                baseProject.setLegalRepresentative(wjBudgetVo.getLegal_representative());
                baseProject.setCustomerResults(wjBudgetVo.getCustomer_results());
                baseProject.setTotalAmountQuotation(wjBudgetVo.getTotal_amount_quotation());
                baseProject.setLivingSurfaceDiameter(wjBudgetVo.getLiving_surface_diameter());
                baseProject.setSubject(wjBudgetVo.getCustomer_category());
                baseProject.setLandCertificate(wjBudgetVo.getLand_certificate());
                baseProject.setChargeAmount(wjBudgetVo.getCharge_amount());
                baseProject.setSite(wjBudgetVo.getSite());
                baseProject.setCustomerEmail(wjBudgetVo.getPostcode());
                baseProject.setAgent(wjBudgetVo.getAgent());
                baseProject.setCustomerPhone(wjBudgetVo.getPhone());
                baseProject.setContactNumber(wjBudgetVo.getPhon_num());
                baseProject.setFax(wjBudgetVo.getFax());
                baseProject.setDelFlag("0");
                baseProject.setBudgetStatus("4");
                baseProjectDao.insertSelective(baseProject);
            }
            List<Budgeting> budgeting1 = budgetingDao.findBudgetingGetBaseId(wjBudgetVo.getBase_project_id());
            Budgeting budgeting = new Budgeting();
            if(budgeting1.size() == 0){
                budgeting.setId(UUID.randomUUID().toString().replace("-",""));
                budgeting.setBaseProjectId(wjBudgetVo.getBase_project_id());
                budgeting.setAmountCost(new BigDecimal(0));
                budgeting.setBudgetingPeople(wjBudgetVoF.getAccount());
                budgeting.setAddedTaxAmount(new BigDecimal(0));
                budgeting.setFounderId(wjBudgetVoF.getAccount());
                budgeting.setDelFlag("0");
                budgeting.setCreateTime(data);
                budgeting.setRemarkes(wjBudgetVo.getRemark());
                budgetingDao.insertSelective(budgeting);
            }
            ProjectExploration exploration = new ProjectExploration();
            exploration.setId(UUID.randomUUID().toString().replace("-",""));
            exploration.setExplorationIdeal(wjBudgetVo.getSurvey_results());
            exploration.setScout(wjBudgetVo.getOperator());
            exploration.setExplorationTime(wjBudgetVo.getOperation_time());
            exploration.setBaseProjectId(wjBudgetVo.getBase_project_id());
            exploration.setRemark(wjBudgetVo.getRemarks());
            exploration.setStatus("0");
            exploration.setCreateTime(data);
            projectExplorationMapper.insertSelective(exploration);

            CostPreparation costPreparation = new CostPreparation();
            costPreparation.setId(UUID.randomUUID().toString().replace("-",""));
            costPreparation.setBaseProjectId(wjBudgetVo.getBase_project_id());
            if(budgeting1.size() > 0){
                costPreparation.setBudgetingId(budgeting1.get(0).getId());
            }else{
                costPreparation.setBudgetingId(budgeting.getId());
            }
            costPreparation.setDelFlag("0");
            costPreparation.setCostTotalAmount(new BigDecimal(0));
            costPreparation.setVatAmount(new BigDecimal(0));
            costPreparation.setTotalPackageMaterial(new BigDecimal(0));
            costPreparationDao.insertSelective(costPreparation);

            VeryEstablishment veryEstablishment = new VeryEstablishment();
            veryEstablishment.setId(UUID.randomUUID().toString().replace("-",""));
            if(budgeting1.size() > 0){
                veryEstablishment.setBudgetingId(budgeting1.get(0).getId());
            }else{
                veryEstablishment.setBudgetingId(budgeting.getId());
            }
            veryEstablishment.setBaseProjectId(base_project_id);
            veryEstablishment.setDelFlag("0");
            veryEstablishment.setPricingTogether(wjBudgetVoF.getAccount());
            veryEstablishmentDao.insertSelective(veryEstablishment);

            List<BudgetFileInfo> fileList = wjBudgetVo.getFileList();
            if (fileList != null && fileList.size()>0){
                for (BudgetFileInfo thisFile : fileList) {
                    FileInfo fileInfo1 = fileInfoMapper.selectByPrimaryKey(thisFile.getId());
                    if(null == fileInfo1){
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setId(thisFile.getId());
                        fileInfo.setFileName(thisFile.getFile_name());
                        fileInfo.setCreateTime(thisFile.getUp_time());
                        fileInfo.setUserId(thisFile.getUper());
                        fileInfo.setFilePath(thisFile.getLink());
                        fileInfo.setPlatCode(wjBudgetVo.getBase_project_id());
                        fileInfo.setStatus("0");
                        fileInfo.setType("wjysbzwjsc");
                        fileInfoMapper.insertSelective(fileInfo);
                    }
                }
            }
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replace("-",""));
            operationLog.setName(wjBudgetVoF.getAccount());
            operationLog.setDoObject(wjBudgetVo.getApplication_num());
            operationLog.setContent("对接了吴江预算报装【"+baseProject.getId()+"】");
            operationLog.setStatus("0");
            operationLog.setType("16"); //预算报装
            operationLogDao.insertSelective(operationLog);

        }else{
            throw new RuntimeException("参数有误");
        }

    }
}

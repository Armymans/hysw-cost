package net.zlw.cloud.designTask.service;

import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designTask.dao.DeclarationInformationDao;
import net.zlw.cloud.designTask.dao.MangerDemandDao;
import net.zlw.cloud.designTask.dao.OperationSubmitTypeDao;
import net.zlw.cloud.designTask.dao.SubmitOperationDeptDao;
import net.zlw.cloud.designTask.model.*;
import net.zlw.cloud.designTask.model.vo.DesignVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.sun.deploy.cache.Cache.exists;

/**
 * @Classname DesinTaskService
 * @Description TODO
 * @Date 2020/10/29 10:13
 * @Created sjf
 */
@Service
@Transactional
public class DesinTaskService {

    @Autowired
    private DesignInfoMapper designInfoMapper;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private DeclarationInformationDao declarationInformationDao;

    @Autowired
    private MangerDemandDao mangerDemandDao;

    @Autowired
    private OperationSubmitTypeDao operationSubmitTypeDao;

    @Autowired
    private SubmitOperationDeptDao submitOperationDeptDao;


    public void getDesignEngineering(String account, DesignVo designVo) {
        //设置创建时间以及修改时间
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        //判断vo
        if (designVo != null) {
            //创建项目对象
            BaseProject baseProject = new BaseProject();
            //添加数据
            baseProject.setId(UUID.randomUUID().toString().replace("-", ""));
            baseProject.setApplicationNum(designVo.getApplicationNum());
            baseProject.setDistrict(designVo.getDistinct());
            baseProject.setProjectName(designVo.getProjectName());
            baseProject.setConstructionUnit(designVo.getConstructionUnit());
            baseProject.setCustomerName(designVo.getCustomerName());
            baseProject.setContactNumber(designVo.getContactNumberOperator());
            baseProject.setSubject(designVo.getSubject());
            baseProject.setProjectNature(designVo.getProjectNature());
            baseProject.setProjectCategory(designVo.getProjectCategory());
            baseProject.setWaterAddress(designVo.getWaterAddress());
            baseProject.setWaterSupplyType(designVo.getWaterSupplyType());
            baseProject.setAgent(designVo.getAgent());
            baseProject.setApplicationDate(designVo.getApplicationDate());
            baseProject.setBusinessLocation(designVo.getBusinessLocation());
            baseProject.setBusinessTypes(designVo.getBusinessTypes());
            baseProject.setWaterUse(designVo.getWaterUses());
            baseProject.setApplicationNumber(designVo.getApplicationNumber());
            baseProject.setShouldBe(designVo.getShouldBe());
            baseProject.setStatus(designVo.getStatus());
            baseProject.setThisDeclaration(designVo.getThisTask());
            baseProject.setVirtualCode(designVo.getVirtualCode());
            baseProject.setDelFlag("0");
            //写到数据库
            baseProjectDao.insertSelective(baseProject);

            DesignInfo designInfo = new DesignInfo();

            //判断信息表如果没有重复外键的话就添加数据
            if (StringUtils.isNotEmpty(designVo.getApplicationNum())) {
                designInfo.setId(designVo.getId());
                designInfo.setPhone(designVo.getPhone());
                designInfo.setProjectRemark(designVo.getProjectRemark());
                designInfo.setWaterSupply(designVo.getWaterSupply());
                designInfo.setSpecialUser(designVo.getSpecialUser());
                designInfo.setUserOfDay(designVo.getUserOfDay());
                designInfo.setTimeOfUser(designVo.getTimeOfUser());
                designInfo.setNumberOfBuilding(designVo.getNumberOfBuilding());
                designInfo.setNumBuilding(designVo.getNumBuilding());
                designInfo.setBuildingLayers(designVo.getBuildingLayers());
                designInfo.setNotesDrawingTime(designVo.getNotesDrawingTime());
                designInfo.setBlueprintCountersignTime(designVo.getBlueprintCountersignTime());
                designInfo.setBlueprintStartTime(designVo.getBlueprintStartTime());
                designInfo.setStatus("0");
            }
            //写到数据库
            designInfoMapper.insertSelective(designInfo);


            //管理表对象
            MangerDemand mangerDemand = new MangerDemand();
            //管理表集合
            List<MangerDemand> mangerDemand1 = designVo.getMangerDemand();
            //遍历
            for (MangerDemand thisManger : mangerDemand1) {
                //判断管理表是否为空
                if (thisManger != null) {
                    mangerDemand.setId(thisManger.getId());
                    mangerDemand.setBaseProjectId(designVo.getBaseProjectId());
                    mangerDemand.setCaliber(thisManger.getCaliber());
                    mangerDemand.setCount(thisManger.getCount());
                    mangerDemand.setType(thisManger.getType());
                }
            }
            mangerDemandDao.insertSelective(mangerDemand);
            //获得提交对象
            SubmitOperationDept submitOperationDept = new SubmitOperationDept();
            //获得提交集合
            List<SubmitOperationDept> submitOperationDept1 = designVo.getSubmitOperationDept();
            for (SubmitOperationDept thisSubmitOperation : submitOperationDept1) {
                if (thisSubmitOperation != null) {
                    submitOperationDept.setId(thisSubmitOperation.getId());
                    submitOperationDept.setBaseProjectId(designVo.getBaseProjectId());
                    submitOperationDept.setExamineDept(thisSubmitOperation.getExamineDept());
                    submitOperationDept.setExamineOpinion(thisSubmitOperation.getExamineOpinion());
                    submitOperationDept.setExamineTime(thisSubmitOperation.getExamineTime());
                    submitOperationDept.setReviewer(thisSubmitOperation.getReviewer());
                    submitOperationDept.setSubmitDept(thisSubmitOperation.getSubmitDept());
                    submitOperationDept.setSubmitter(thisSubmitOperation.getSubmitter());
                    submitOperationDept.setSubmitTime(thisSubmitOperation.getSubmitTime());
                }
                submitOperationDeptDao.insertSelective(submitOperationDept);

            }

            //拟申报水表对象
            DeclarationInformation declarationInformation = new DeclarationInformation();
            List<DeclarationInformation> declarationInformation1 = designVo.getDeclarationInformation();
            for (DeclarationInformation thisDeclar : declarationInformation1) {
                if (thisDeclar != null) {
                    declarationInformation.setId(thisDeclar.getId());
                    declarationInformation.setBaseProjectId(designVo.getBaseProjectId());
                    declarationInformation.setDeclarationCount(thisDeclar.getDeclarationCount());
                    declarationInformation.setDeclarationType(thisDeclar.getDeclarationType());
                    declarationInformation.setDeclaredDiameter(thisDeclar.getDeclaredDiameter());
                    declarationInformation.setSettingArea(thisDeclar.getSettingArea());
                    declarationInformation.setUserOfLife(thisDeclar.getUserOfLife());
                    declarationInformation.setWaterUse(thisDeclar.getWaterUse());
                }
                declarationInformationDao.insertSelective(declarationInformation);
            }

            //创建操作历史对象
            OperationSubmitType operationSubmitType = new OperationSubmitType();
            //获得vo集合并遍历
            List<OperationSubmitType> operationSubmitType1 = designVo.getOperationSubmitType();
            for (OperationSubmitType thisSubmitType : operationSubmitType1) {
                //判断是否为空
                if (thisSubmitType != null) {
                    operationSubmitType.setId(thisSubmitType.getId());
                    operationSubmitType.setBaseProjectId(thisSubmitType.getBaseProjectId());
                    operationSubmitType.setOperationDept(thisSubmitType.getOperationDept());
                    operationSubmitType.setOperationTime(thisSubmitType.getOperationTime());
                    operationSubmitType.setOperationTpe(thisSubmitType.getOperationTpe());
                    operationSubmitType.setOperator(account);
                    operationSubmitType.setResreon(thisSubmitType.getResreon());
                }
                //写入数据库
                operationSubmitTypeDao.insertSelective(operationSubmitType);
            }

            //获得文件对象
            FileInfo fileInfo = new FileInfo();
            //获得图纸、批文资料
            List<CustomerProvidedFile> customerProvidedFile = designVo.getCustomerProvidedFile();

            for (CustomerProvidedFile thisProvidedFile : customerProvidedFile) {
                fileInfo.setId(thisProvidedFile.getId());
                fileInfo.setPlatCode(thisProvidedFile.getBaseProjectId());
                fileInfo.setFileName(thisProvidedFile.getCustomerProvidedFileName());
                fileInfo.setName(thisProvidedFile.getCustomerProvidedName());
                fileInfo.setCreateTime(thisProvidedFile.getCustomerProvidedTime());
                fileInfo.setUserId(thisProvidedFile.getCustomerProvidedBy());
                fileInfo.setFilePath(thisProvidedFile.getCustomerProvidedDrawing());
                fileInfo.setType("tzpwzl");
            }
            //插入文件表
            fileInfoMapper.insertSelective(fileInfo);


            //获得工程设计图纸集合
            List<ProjectDesign> projectDesign = designVo.getProjectDesign();

            for (ProjectDesign thisProject : projectDesign) {
                //如果文件信息不重复就写入
                    fileInfo.setId(thisProject.getId());
                    fileInfo.setPlatCode(thisProject.getBaseProjectId());
                    fileInfo.setFileName(thisProject.getProjectFileName());
                    fileInfo.setCreateTime(thisProject.getProjectUpTime());
                    fileInfo.setRemark(thisProject.getProjectUploadedBy());
                    fileInfo.setCreateTime(thisProject.getProjectUploadedBy());
                    fileInfo.setStatus("0");
                    fileInfo.setType("gcsjtz");
                }

                //插入文件表
                fileInfoMapper.insertSelective(fileInfo);


            //获得水表数量清单集合
            List<WatherList> waterList = designVo.getWaterList();

            for (WatherList thisWater : waterList) {
                //如果文件信息不重复就写入
                    fileInfo.setId(thisWater.getId());
                    fileInfo.setPlatCode(thisWater.getBaseProjectId());
                    fileInfo.setFileName(thisWater.getWaterListFileName());
                    fileInfo.setCreateTime(thisWater.getWaterListTime());
                    fileInfo.setUserId(thisWater.getWaterListBy());
                    fileInfo.setFilePath(thisWater.getWaterListDrawing());
                    fileInfo.setType("sbslqd");
                }

                //插入文件表
                fileInfoMapper.insertSelective(fileInfo);


        }

    }


}

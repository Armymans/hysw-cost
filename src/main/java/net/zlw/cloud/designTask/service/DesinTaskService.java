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
//        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        //查询设计集合
        List<DesignInfo> designInfos = designInfoMapper.selectAll();
        System.out.println(designInfos);
        //判断vo
        if (StringUtils.isNotEmpty(designVo.getId())) {
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
            //写到数据库
            baseProjectDao.insertSelective(baseProject);

            //遍历设计信息表
            for (DesignInfo thisDesI : designInfos) {
                //判断信息表如果没有重复外键的话就添加数据
                if (thisDesI.getBaseProjectId().contains(designVo.getBaseProjectId())) {
                    thisDesI.setId(designVo.getId());
                    thisDesI.setPhone(designVo.getPhone());
//                    thisDesI.setProjectRemark(designVo.getProjectRemark());
                    thisDesI.setWaterSupply(designVo.getWaterSupply());
                    thisDesI.setSpecialUser(designVo.getSpecialUser());
                    thisDesI.setUserOfDay(designVo.getUserOfDay());
                    thisDesI.setTimeOfUser(designVo.getTimeOfUser());
                    thisDesI.setNumberOfBuilding(designVo.getNumberOfBuilding());
                    thisDesI.setNumBuilding(designVo.getNumBuilding());
                    thisDesI.setBuildingLayers(designVo.getBuildingLayers());
                    thisDesI.setNotesDrawingTime(designVo.getNotesDrawingTime());
                    thisDesI.setBlueprintCountersignTime(designVo.getBlueprintCountersignTime());
                    thisDesI.setBlueprintStartTime(designVo.getBlueprintStartTime());
                }
                //写到数据库
                designInfoMapper.insertSelective(thisDesI);
            }

            //管理表对象
            MangerDemand mangerDemand = new MangerDemand();
            //管理表集合
            List<MangerDemand> mangerDemand1 = designVo.getMangerDemand();
            //遍历
            for (MangerDemand thisManger : mangerDemand1) {
                //判断管理表是否为空
                if (StringUtils.isNotEmpty(thisManger.getBaseProjectId())) {
                    mangerDemand.setId(UUID.randomUUID().toString().replace("-", ""));
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
                if (StringUtils.isNotEmpty(thisSubmitOperation.getBaseProjectId())){
                    submitOperationDept.setId(UUID.randomUUID().toString().replace("-",""));
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
                if (StringUtils.isNotEmpty(thisDeclar.getBaseProjectId())) {
                    declarationInformation.setId(UUID.randomUUID().toString().replace("-", ""));
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
                if (StringUtils.isNotEmpty(thisSubmitType.getBaseProjectId())) {
                    operationSubmitType.setId(UUID.randomUUID().toString().replace("-", ""));
                    operationSubmitType.setBaseProjectId(thisSubmitType.getBaseProjectId());
                    operationSubmitType.setOperationDept(thisSubmitType.getOperationDept());
                    operationSubmitType.setOperationTime(thisSubmitType.getOperationTime());
                    operationSubmitType.setOperationTpe(thisSubmitType.getOperationTpe());
                    operationSubmitType.setOperator(thisSubmitType.getOperator());
                    operationSubmitType.setResreon(thisSubmitType.getResreon());
                }
                //写入数据库
                operationSubmitTypeDao.insertSelective(operationSubmitType);
            }

            //查询文件集合
            List<FileInfo> fileInfos = fileInfoMapper.selectAll();
            //获得图纸、批文资料
            List<CustomerProvidedFile> customerProvidedFile = designVo.getCustomerProvidedFile();
            for (FileInfo thisFile : fileInfos) {
                for (CustomerProvidedFile thisProvidedFile : customerProvidedFile) {
                    //如果文件信息不重复就写入
                    if (!thisFile.getPlatCode().contains(thisProvidedFile.getBaseProjectId())) {
                        thisFile.setId(UUID.randomUUID().toString().replace("-",""));
                        thisFile.setPlatCode(thisProvidedFile.getBaseProjectId());
                        thisFile.setFileName(thisProvidedFile.getCustomerProvidedFileName());
                        thisFile.setName(thisProvidedFile.getCustomerProvidedName());
                        thisFile.setCreateTime(thisProvidedFile.getCustomerProvidedTime());
                        thisFile.setUserId(thisProvidedFile.getCustomerProvidedBy());
                        thisFile.setFilePath(thisProvidedFile.getCustomerProvidedDrawing());
                        thisFile.setType("tzpwzl");

                    }
                }
                //插入文件表
                fileInfoMapper.insertSelective(thisFile);
            }
            //获得工程设计图纸集合
            List<ProjectDesign> projectDesign = designVo.getProjectDesign();
            for (FileInfo thisFile : fileInfos) {
                for (ProjectDesign thisProject : projectDesign) {
                    //如果文件信息不重复就写入
                    if (!thisFile.getPlatCode().contains(thisProject.getBaseProjectId())) {
                        thisFile.setId(UUID.randomUUID().toString().replace("-",""));
                        thisFile.setPlatCode(thisProject.getBaseProjectId());
                        thisFile.setFileName(thisProject.getProjectFileName());
                        thisFile.setCreateTime(thisProject.getProjectUpTime());
                        thisFile.setRemark(thisProject.getProjectUploadedBy());
                        thisFile.setCreateTime(thisProject.getProjectUploadedBy());
                        thisFile.setType("gcsjtz");
                    }
                }
                //插入文件表
                fileInfoMapper.insertSelective(thisFile);
            }
            //获得水表数量清单集合
            List<WatherList> waterList = designVo.getWaterList();
            for (FileInfo thisFile : fileInfos) {
                for (WatherList thisWater : waterList) {
                    //如果文件信息不重复就写入
                    if (!thisFile.getPlatCode().contains(thisWater.getBaseProjectId())) {
                        thisFile.setId(UUID.randomUUID().toString().replace("-",""));
                        thisFile.setPlatCode(thisWater.getBaseProjectId());
                        thisFile.setFileName(thisWater.getWaterListFileName());
                        thisFile.setCreateTime(thisWater.getWaterListTime());
                        thisFile.setUserId(thisWater.getWaterListBy());
                        thisFile.setFilePath(thisWater.getWaterListDrawing());
                        thisFile.setType("sbslqd");
                    }
                }
                //插入文件表
                fileInfoMapper.insertSelective(thisFile);
            }



        }

    }


}

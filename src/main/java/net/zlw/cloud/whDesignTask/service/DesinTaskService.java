package net.zlw.cloud.whDesignTask.service;

import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.whDesignTask.dao.DeclarationInformationDao;
import net.zlw.cloud.whDesignTask.dao.MangerDemandDao;
import net.zlw.cloud.whDesignTask.dao.OperationSubmitTypeDao;
import net.zlw.cloud.whDesignTask.dao.SubmitOperationDeptDao;
import net.zlw.cloud.whDesignTask.model.*;
import net.zlw.cloud.whDesignTask.model.vo.DesignVo;
import net.zlw.cloud.whDesignTask.model.vo.DesignVoF;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Classname DesinTaskService
 * @Description TODO
 * @Date 2020/10/29 10:13
 * @Created sjf
 */
@Service
@Transactional
public class DesinTaskService {

    //设置创建时间以及修改时间
    String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

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
    private MemberManageDao memberManageDao;

    @Autowired
    private OperationSubmitTypeDao operationSubmitTypeDao;

    @Autowired
    private SubmitOperationDeptDao submitOperationDeptDao;


    public void getDesignEngineering(DesignVoF designVoF) {
        DesignVo designVo = designVoF.getDesignVo();
        //判断vo
        if (designVo != null) {
            // 根据账号查询id
            String userId = memberManageDao.selectById(designVoF.getAccount());
            //创建项目对象
            BaseProject baseProject = new BaseProject();
            //添加数据
            baseProject.setId(designVo.getBase_project_id());
            baseProject.setApplicationNum(designVo.getApplication_num());
            //TODO 如果没有获取到地区就暂给 1
            if (designVo.getDistinct() != null && "".equals(designVo.getDistinct())){
                baseProject.setDistrict(designVo.getDistinct());
            }else {
                baseProject.setDistrict("1");
            }
            baseProject.setProjectName(designVo.getProject_name());
            baseProject.setConstructionUnit(designVo.getConstruction_unit());
            baseProject.setCustomerName(designVo.getCustomer_name());
            baseProject.setContactNumber(designVo.getContact_number_operator());
            baseProject.setSubject(designVo.getSubject());
            baseProject.setProjectNature(designVo.getProject_nature());
            baseProject.setProjectCategory(designVo.getProject_category());
            baseProject.setWaterAddress(designVo.getWater_address());
            baseProject.setWaterSupplyType(designVo.getWater_supply_type());
            baseProject.setAgent(designVo.getAgent());
            baseProject.setApplicationDate(designVo.getApplication_date());
            baseProject.setBusinessLocation(designVo.getBusiness_location());
            baseProject.setBusinessTypes(designVo.getBusiness_types());
            baseProject.setWaterUse(designVo.getWater_address());
            baseProject.setApplicationNumber(designVo.getApplication_number());
            baseProject.setShouldBe(designVo.getShould_be());
            baseProject.setStatus(designVo.getStatus());
            baseProject.setThisDeclaration(designVo.getThis_task());
            baseProject.setVirtualCode(designVo.getVirtual_code());
            baseProject.setStatus("0");
            baseProject.setDelFlag("0");
            baseProject.setDesginStatus("4");
            //写到数据库
            baseProjectDao.insertSelective(baseProject);

            DesignInfo designInfo = new DesignInfo();
            //判断信息表如果没有重复外键的话就添加数据
            if (StringUtils.isNotEmpty(designVo.getApplication_num())) {
                designInfo.setId(designVo.getApplication_num());
                designInfo.setBaseProjectId(designVo.getBase_project_id());
                designInfo.setPhone(designVo.getPhone());
                designInfo.setProjectRemark(designVo.getProject_remark());
                designInfo.setWaterSupply(designVo.getWater_supply());
                designInfo.setSpecialUser(designVo.getSpecial_user());
                designInfo.setUserOfDay(designVo.getUserOf_day());
                designInfo.setTimeOfUser(designVo.getTimeOf_user());
                designInfo.setNumberOfBuilding(designVo.getNumber_of_building());
                designInfo.setNumBuilding(designVo.getNum_building());
                designInfo.setBuildingLayers(designVo.getBuilding_layers());
                designInfo.setFounderId(designVo.getAgent());
                designInfo.setNotesDrawingTime(designVo.getNotes_drawing_time());
                designInfo.setBlueprintCountersignTime(designVo.getBlueprint_countersign_time());
                designInfo.setBlueprintStartTime(designVo.getBlueprint_start_time());
                designInfo.setStatus("0");
                //写到数据库
                designInfoMapper.insertSelective(designInfo);
            }

            //管理表集合
            List<MangerDemand> mangerDemand1 = designVo.getMangerDemand();
            //遍历
            if (mangerDemand1 != null && mangerDemand1.size() > 0) {
                for (MangerDemand thisManger : mangerDemand1) {
                    //判断管理表是否为空
                    if (thisManger != null) {
                        //管理表对象
                        MangerDemand mangerDemand = new MangerDemand();
                        mangerDemand.setId(thisManger.getId());
                        mangerDemand.setBase_project_id(designVo.getBase_project_id());
                        mangerDemand.setCaliber(thisManger.getCaliber());
                        mangerDemand.setCount(thisManger.getCount());
                        mangerDemand.setType(thisManger.getType());
                        mangerDemandDao.insertSelective(mangerDemand);
                    }
                }
            }


            //获得提交集合
            List<SubmitOperationDept> submitOperationDept1 = designVo.getSubmitOperationDept();
            if ( submitOperationDept1 != null &&  submitOperationDept1.size() > 0) {
                for (SubmitOperationDept thisSubmitOperation : submitOperationDept1) {
                    if (thisSubmitOperation != null) {
                        //获得提交对象
                        SubmitOperationDept submitOperationDept = new SubmitOperationDept();
                        submitOperationDept.setId(thisSubmitOperation.getId());
                        submitOperationDept.setBase_project_id(designVo.getBase_project_id());
                        submitOperationDept.setExamine_dep(thisSubmitOperation.getExamine_dep());
                        submitOperationDept.setExamine_opinion(thisSubmitOperation.getExamine_opinion());
                        submitOperationDept.setExamine_time(thisSubmitOperation.getExamine_time());
                        submitOperationDept.setReviewer(thisSubmitOperation.getReviewer());
                        submitOperationDept.setSubmit_dept(thisSubmitOperation.getSubmit_dept());
                        submitOperationDept.setSubmitter(thisSubmitOperation.getSubmitter());
                        submitOperationDept.setSubmit_time(thisSubmitOperation.getSubmit_time());
                        submitOperationDeptDao.insertSelective(submitOperationDept);
                    }

                }
            }


            List<DeclarationInformation> declarationInformation1 = designVo.getDeclaration_information();
            if (declarationInformation1 != null &&  declarationInformation1.size() > 0) {
                for (DeclarationInformation thisDeclar : declarationInformation1) {
                    if (thisDeclar != null) {
                        //拟申报水表对象
                        DeclarationInformation declarationInformation = new DeclarationInformation();
                        declarationInformation.setId(designVo.getApplication_num());
                        declarationInformation.setBase_project_id(designVo.getBase_project_id());
                        declarationInformation.setDeclaration_count(thisDeclar.getDeclaration_count());
                        declarationInformation.setDeclaration_type(thisDeclar.getDeclaration_type());
                        declarationInformation.setDeclared_diameter(thisDeclar.getDeclared_diameter());
                        declarationInformation.setSetting_area(thisDeclar.getSetting_area());
                        declarationInformation.setUser_of_life(thisDeclar.getUser_of_life());
                        declarationInformation.setWater_use(thisDeclar.getWater_use());
                        declarationInformationDao.insertSelective(declarationInformation);
                    }
                }
            }


            //获得vo集合并遍历
            List<OperationSubmitType> operationSubmitType1 = designVo.getOperationSubmitType();
            if (operationSubmitType1 != null && operationSubmitType1.size() > 0) {
                for (OperationSubmitType thisSubmitType : operationSubmitType1) {
                    //判断是否为空
                    if (thisSubmitType != null) {
                        //创建操作历史对象
                        OperationSubmitType operationSubmitType = new OperationSubmitType();
                        operationSubmitType.setId(designVo.getApplication_num());
                        operationSubmitType.setBase_project_id(thisSubmitType.getBase_project_id());
                        operationSubmitType.setOperation_dept(thisSubmitType.getOperation_dept());
                        operationSubmitType.setOperation_time(thisSubmitType.getOperation_time());
                        operationSubmitType.setOperation_tpe(thisSubmitType.getOperation_tpe());
                        operationSubmitType.setOperator(thisSubmitType.getOperator());
                        operationSubmitType.setResreon(thisSubmitType.getResreon());
                        //写入数据库
                        operationSubmitTypeDao.insertSelective(operationSubmitType);
                    }
                }
            }


            //获得图纸、批文资料
            List<CustomerProvidedFile> customerProvidedFile = designVo.getCustomerProvidedFile();
            if (customerProvidedFile != null && customerProvidedFile.size() > 0) {
                for (CustomerProvidedFile thisProvidedFile : customerProvidedFile) {
                    //获得文件对象
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setId(thisProvidedFile.getId());
                    fileInfo.setFileName(thisProvidedFile.getCustomer_provided_file_name());
                    fileInfo.setName(thisProvidedFile.getCustomer_provided_name());
                    fileInfo.setCreateTime(thisProvidedFile.getCustomer_provided_time());
                    fileInfo.setUserId(thisProvidedFile.getCustomer_provided_by());
                    fileInfo.setFilePath(thisProvidedFile.getCustomer_provided_drawing());
                    fileInfo.setPlatCode(designInfo.getId());
                    fileInfo.setType("tzpwzl");
                    fileInfo.setFileSource("3");
                    fileInfo.setStatus("0");
                    //插入文件表
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }


            //获得工程设计图纸集合
            List<ProjectDesign> projectDesign = designVo.getProjectDesign();
            if(projectDesign != null &&  projectDesign.size() > 0){
                for (ProjectDesign thisProject : projectDesign) {
                    //获得文件对象
                    FileInfo fileInfo = new FileInfo();
                    //如果文件信息不重复就写入
                    fileInfo.setId(thisProject.getId());
                    fileInfo.setPlatCode(thisProject.getBase_project_id());
                    fileInfo.setFileName(thisProject.getProject_file_name());
                    fileInfo.setCreateTime(thisProject.getProject_up_time());
                    fileInfo.setRemark(thisProject.getProject_uploaded_by());
                    fileInfo.setPlatCode(designInfo.getId());
                    fileInfo.setStatus("0");
                    fileInfo.setFileSource("3");
                    fileInfo.setType("gcsjtz");
                    //插入文件表
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }


            //获得水表数量清单集合
            List<WatherList> waterList = designVo.getWaterList();
            if( waterList != null &&  waterList.size() > 0){
                for (WatherList thisWater : waterList) {
                    //获得文件对象
                    FileInfo fileInfo = new FileInfo();
                    //如果文件信息不重复就写入
                    fileInfo.setId(thisWater.getId());
                    fileInfo.setPlatCode(thisWater.getBase_project_id());
                    fileInfo.setFileName(thisWater.getWater_list_file_name());
                    fileInfo.setCreateTime(thisWater.getWater_list_time());
                    fileInfo.setUserId(thisWater.getWater_list_by());
                    fileInfo.setFilePath(thisWater.getWater_list_drawing());
                    fileInfo.setPlatCode(designInfo.getId());
                    fileInfo.setType("sbslqd");
                    fileInfo.setFileSource("3");
                    //插入文件表
                    fileInfoMapper.insertSelective(fileInfo);
                }
            }

        }

    }


}

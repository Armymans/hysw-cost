package net.zlw.cloud.whDesignTask.service;

import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.jbDesignTask.dao.DiameterInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.whDesignTask.dao.*;
import net.zlw.cloud.whDesignTask.model.DockLog;
import net.zlw.cloud.whDesignTask.model.WatherList;
import net.zlw.cloud.whDesignTask.model.vo.DesignVo;
import net.zlw.cloud.whDesignTask.model.vo.DesignVoF;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    //设置创建时间以及修改时间
    String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Autowired
    private DockLogDao dockLogDao;

    @Autowired
    private DesignInfoMapper designInfoMapper;

    private DiameterInfoDao diameterInfoDao;

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

    @Autowired
    private MemberService memberService;


    public void getDesignEngineering(DesignVoF designVoF , HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

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
            baseProject.setProjectNum(designVo.getBase_project_id());
            baseProject.setProjectName(designVo.getProject_name());
            baseProject.setConstructionUnit(designVo.getConstruction_unit());
            baseProject.setCustomerName(designVo.getCustomer_name());
            baseProject.setContactNumber(designVo.getContact_number_operator());
            baseProject.setSubject(designVo.getSubject());
            baseProject.setProjectNature((Integer.parseInt(designVo.getProject_nature()) + 1) + "");
            baseProject.setProjectCategory((Integer.parseInt(designVo.getProject_category()) + 1) + "");
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
            baseProject.setProjectFlow("1");
            baseProject.setMergeFlag("1");
            baseProject.setCreateTime(data);
            baseProject.setUpdateTime(data);
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
                designInfo.setOutsource("1");
                designInfo.setStatus("0");
                designInfo.setCreateTime(data);
                designInfo.setUpdateTime(data);
                //写到数据库
                designInfoMapper.insertSelective(designInfo);
            }

            //获得水表数量清单集合
            List<WatherList> waterList = designVo.getWaterList();
            if( waterList != null &&  waterList.size() > 0){
                for (WatherList thisWater : waterList) {
                    //获得文件对象
                    FileInfo fileInfo = new FileInfo();
                    //如果文件信息不重复就写入
                    fileInfo.setId(thisWater.getId());
                    fileInfo.setPlatCode(designInfo.getId());
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
           /*  `id` varchar(60) NOT NULL COMMENT '唯一标识',
             `name` varchar(60) DEFAULT NULL COMMENT '操作人',
             `type` varchar(1) DEFAULT NULL COMMENT '类型1.设计2预算3结算4跟踪审计5状态更新',
             `content` longtext COMMENT '参数',
             `do_time` varchar(60) DEFAULT NULL COMMENT '操作时间',
             `do_object` varchar(200) DEFAULT NULL COMMENT '项目标识',
             `status` varchar(1) DEFAULT NULL COMMENT '状态',
             `ip` varchar(255) DEFAULT NULL COMMENT 'ip',*/
            DockLog dockLog = new DockLog();
            dockLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            dockLog.setName(designVoF.getAccount()); // 操作人
            dockLog.setType("1"); //设计
            dockLog.setContent(designVoF.toString());
            dockLog.setDoTime(data);
            dockLog.setDoObject(designVoF.getDesignVo().getApplication_num());
            dockLog.setStatus("0");
            String ip = memberService.getIp(request);
            dockLog.setIp(ip);
            dockLogDao.insertSelective(dockLog);
        }

    }


}

package net.zlw.cloud.maintenanceProjectInformation.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.client.http.HttpRequest;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageResult;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author dell
 * @Date 2020/9/27 10:52
 * @Version 1.0
 */
@Service
public class MaintenanceProjectInformationService{

    @Resource
    private MaintenanceProjectInformationMapper maintenanceProjectInformationMapper;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

    @Resource
    private AuditInfoDao auditInfoDao;

    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private SurveyInformationDao surveyInformationDao;


    /**
     * 分页查询所有
     * @param userInfo
     * @return
     */
    public List<MaintenanceProjectInformation> findAllMaintenanceProjectInformation(PageRequest pageRequest,UserInfo userInfo){
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        Example example = new Example(MaintenanceProjectInformation.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag","0");

        // TODO
        //     查询条件  所属地区
        if(pageRequest.getAddress() != null && (!"".equals(pageRequest.getAddress()))){
            criteria.andLike("projectAddress","%"+pageRequest.getAddress()+"%");
        }

        //     查询条件   检维修类型
        if(pageRequest.getMaintenanceType() != null && (!"".equals(pageRequest.getMaintenanceType()))){
            criteria.andEqualTo("maintenanceItemType",pageRequest.getMaintenanceType());
        }
        // 查询条件  状态
        if(pageRequest.getPageStaus() != null && (!"".equals(pageRequest.getPageStaus()))){
            criteria.andEqualTo("",pageRequest.getPageStaus());
        }

        // 查询条件 开始时间
        if(pageRequest.getStartTime() != null && (!"".equals(pageRequest.getStartTime()))){
            String startTime = pageRequest.getStartTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.andGreaterThanOrEqualTo("createTime",date);

        }


        // 查询条件 结束时间
        if(pageRequest.getEndTime() != null && (!"".equals(pageRequest.getEndTime()))){
            String endTime = pageRequest.getEndTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.andLessThanOrEqualTo("updateTime",date);

        }

        // 查询条件 内容
        if(pageRequest.getKeyWord() != null && (!"".equals(pageRequest.getKeyWord()))){
           criteria.andLike("maintenanceItemName","%"+pageRequest.getKeyWord()+"%");
        }

        List<MaintenanceProjectInformation> maintenanceProjectInformations = maintenanceProjectInformationMapper.selectByExample(example);
//        List<MaintenanceProjectInformation> maintenanceProjectInformations = maintenanceProjectInformationMapper.selectAllByDelFlag();

        for (MaintenanceProjectInformation maintenanceProjectInformation : maintenanceProjectInformations) {

            //滤空判断
            if(maintenanceProjectInformation.getConstructionUnitId()!= null && (!"".equals(maintenanceProjectInformation.getConstructionUnitId()))){
                ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectById(maintenanceProjectInformation.getConstructionUnitId());
                maintenanceProjectInformation.setConstructionUnitManagement(constructionUnitManagement);
                //获取施工单位名字
                maintenanceProjectInformation.setConstructionUnitName(constructionUnitManagement.getConstructionUnitName());
            }


            if(maintenanceProjectInformation.getPreparePeople() != null && (!"".equals(maintenanceProjectInformation.getPreparePeople()))){
                MemberManage memberManage = memberManageDao.selectByIdAndStatus(maintenanceProjectInformation.getPreparePeople());

                maintenanceProjectInformation.setMemberName(memberManage.getMemberName());
            }

//            maintenanceProjectInformation.setFounderId(userInfo.getId());
//            maintenanceProjectInformation.setFounderCompanyId(userInfo.getCompanyId());
        }

        PageInfo<MaintenanceProjectInformation> projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformations);

        System.out.println("list:"+projectInformationPageInfo.getList().toString());

        return projectInformationPageInfo.getList();

    }


    /**
     * 检维修--删除
     * @param id
     */
    public void deleteMaintenanceProjectInformation(String id){
        maintenanceProjectInformationMapper.deleteMaintenanceProjectInformation(id);
    }


    /**
     * 检维修--新增--提交
     * @param maintenanceProjectInformation
     * @param userInfo
     */
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo){
        String id = UUID.randomUUID().toString().replaceAll("-","");
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(id);
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        information.setCustomerName(maintenanceProjectInformation.getCustomerName());
        //        information.setFounderId(userInfo.getId());

//        information.setFounderCompanyId(userInfo.getCompanyId());
        //勘探信息
//        SurveyInformation surveyInformation = new SurveyInformation();
//        surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
//        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyInformation().getSurveyDate());
//        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getSurveyInformation().getInvestigationPersonnel());
//        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyInformation().getSurveyBriefly());
//
//        surveyInformationDao.insertSelective(surveyInformation);

        // 审核信息

        AuditInfo auditInfo = new AuditInfo();

        String s = UUID.randomUUID().toString().replaceAll("-", "");
        auditInfo.setId(s);
        auditInfo.setBaseProjectId(information.getMaintenanceItemId());

        auditInfo.setAuditResult("0");

        auditInfo.setAuditType("0");

        auditInfo.setStatus("0");

        auditInfo.setAuditorId(information.getPreparePeople());

        Date date = new Date();
        String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
        auditInfo.setAuditTime(format);
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());

        auditInfo.setCreateTime(createDate);

        auditInfoDao.insertSelective(auditInfo);


//        MemberManage memberManage = memberManageDao.selectByIdAndStatus(auditInfo.getId());

        maintenanceProjectInformationMapper.insertSelective(information);

    }


    /**
     * 批量审核
     * @param batchReviewVo
     */
    public void batchReview(BatchReviewVo batchReviewVo){
        //获取批量审核的id
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            Example example = new Example(AuditInfo.class);
            // auditResult = 0 , 未审批
            example.createCriteria().andEqualTo("baseProjectId",s).andEqualTo("auditResult","0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

            MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectById(s);
            // 未审核
            maintenanceProjectInformation.setType("1");

            //如果审核通过
            if(batchReviewVo.getAuditResult().equals("1")){
                // 0 一审
                if(auditInfo.getAuditType().equals("0")){
                    // 审核通过
                    auditInfo.setAuditResult("1");
                    // 待确认
                    maintenanceProjectInformation.setType("4");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
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
                maintenanceProjectInformation.setType("3");
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
            }
        }

    }

    public void updateMaintenanceProjectInformation(MaintenanceProjectInformation maintenanceProjectInformation){

    }

    /**
     * 根据id,查找回显数据
     * @param id
     * @return
     */
    public MaintenanceProjectInformation selectMaintenanceProjectInformationById(String id){
        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectById(id);
        return maintenanceProjectInformation;
    }

    /**
     * 新增--保存
     */
    public void saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation, HttpSession session){
        String id = UUID.randomUUID().toString().replaceAll("-","");
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(id);
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        //        information.setFounderId(userInfo.getId());

//        information.setFounderCompanyId(userInfo.getCompanyId());
        //勘探信息
        SurveyInformation surveyInformation = new SurveyInformation();
        surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyInformation().getSurveyDate());
        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getSurveyInformation().getInvestigationPersonnel());
        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyInformation().getSurveyBriefly());

        surveyInformationDao.insertSelective(surveyInformation);

    }



}

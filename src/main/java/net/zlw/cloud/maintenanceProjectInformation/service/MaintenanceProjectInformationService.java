package net.zlw.cloud.maintenanceProjectInformation.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.client.http.HttpRequest;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.model.OneCensus;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.*;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

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

    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;


    /**
     * 分页查询所有
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformation> findAllMaintenanceProjectInformation(PageRequest pageRequest,UserInfo userInfo){
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        Example example = new Example(MaintenanceProjectInformation.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag","0");
        // 查询条件  状态
        if(pageRequest.getPageStaus() != null && (!"".equals(pageRequest.getPageStaus()))){
            criteria.andEqualTo("type",pageRequest.getPageStaus());
        }

        // TODO 登陆人
//        criteria.andEqualTo("founderId",userInfo.getId());
        //     查询条件   检维修类型
        if(pageRequest.getMaintenanceType() != null && (!"".equals(pageRequest.getMaintenanceType()))){
            criteria.andEqualTo("maintenanceItemType",pageRequest.getMaintenanceType());
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
//                maintenanceProjectInformation.setConstructionUnitManagement(constructionUnitManagement);
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

        return projectInformationPageInfo;

    }

    /**
     * 统计分析 列表
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformation> list(PageRequest pageRequest,UserInfo userInfo){
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), 5);

        Example example = new Example(MaintenanceProjectInformation.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag","0");
        // 查询条件  状态
        if(pageRequest.getPageStaus() != null && (!"".equals(pageRequest.getPageStaus()))){
            criteria.andEqualTo("type",pageRequest.getPageStaus());
        }

        // TODO 登陆人
//        criteria.andEqualTo("founderId",userInfo.getId());
        //  地区
        if(pageRequest.getProjectAddress() != null && (!"".equals(pageRequest.getProjectAddress()))){
            criteria.andEqualTo("projectAddress",pageRequest.getProjectAddress());
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
//                maintenanceProjectInformation.setConstructionUnitManagement(constructionUnitManagement);
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

        return projectInformationPageInfo;

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
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo,String id){
        String uuId = UUID.randomUUID().toString().replaceAll("-","");
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(uuId);
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        // 编制人
//        information.setPreparePeople(maintenanceProjectInformation.getSettlementAuditInformation().getPreparePeople());
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

        if(maintenanceProjectInformation.getSurveyInformation() != null){
            SurveyInformation surveyInformation = maintenanceProjectInformation.getSurveyInformation();
            String sid = UUID.randomUUID().toString().replace("-","");
            surveyInformation.setId(sid);
            //创建时间
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String surveyInformationCreateTime = simpleDateFormat1.format(new Date());

            surveyInformation.setCreateTime(surveyInformationCreateTime);
            // todo 项目基本信息的id
            surveyInformation.setBaseProjectId(information.getId());

            surveyInformationDao.insertSelective(surveyInformation);
        }



        //结算审核信息
//        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

//        if(maintenanceProjectInformation.getSettlementAuditInformation() != null){
//            SettlementAuditInformation settlementAuditInformation = maintenanceProjectInformation.getSettlementAuditInformation();
//            String sid = UUID.randomUUID().toString().replace("-","");
//            settlementAuditInformation.setId(sid);
//            // 创建时间
//            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String settlementAuditInformationCreateTime = simpleDateFormat2.format(new Date());
//
//            settlementAuditInformation.setCreateTime(settlementAuditInformationCreateTime);
//
//            // todo 检维修项目信息id
//            settlementAuditInformation.setMaintenanceProjectInformation(information.getId());
//            settlementAuditInformationDao.insertSelective(settlementAuditInformation);
//        }

        // 审核信息

        AuditInfo auditInfo = new AuditInfo();

        String s = UUID.randomUUID().toString().replaceAll("-", "");
        auditInfo.setId(s);
        auditInfo.setBaseProjectId(information.getId());

        auditInfo.setAuditResult("0");

        auditInfo.setAuditType("0");

        auditInfo.setStatus("0");

        // 审核人id
        auditInfo.setAuditorId(id);

//        Date date = new Date();
//        String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
//        auditInfo.setAuditTime(format);//现在没有审核时间
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
        if(split.length > 0){
            for (String s : split) {
                if(StringUtil.isNotEmpty(s)){
                    Example example = new Example(AuditInfo.class);
                    // auditResult = 0 , 未审批
                    example.createCriteria().andEqualTo("baseProjectId",s).andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

                    MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectById(s);
                    // 未审核
                    maintenanceProjectInformation.setType("1");

                    // 判断更改状态
                    if(batchReviewVo.getAuditResult().equals("1")){
                        // 0 一审
                        if(auditInfo.getAuditType().equals("0")){
                            // 审核通过
                            auditInfo.setAuditResult("1");
                            //一级审批的意见，时间
                            auditInfo.setAuditTime(sdf.format(date));
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            //修改审批状态
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                            // 待确认
                            maintenanceProjectInformation.setType("4");
                            Date date = new Date();
                            String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                            auditInfo.setAuditTime(format);
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());


                            maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
                            //                            一审通过在审核表插入一条数据
                            AuditInfo auditInfo1 = new AuditInfo();
                            auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                            auditInfo1.setBaseProjectId(s);
                            auditInfo1.setAuditResult("0");
                            auditInfo1.setAuditType("1");
                            Example example1 = new Example(MemberManage.class);
                            example1.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
                            MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                            auditInfo1.setAuditorId(memberManage.getId());
                            auditInfoDao.insertSelective(auditInfo1);
                        }else if(auditInfo.getAuditType().equals("1")){//二审
                            auditInfo.setAuditResult("1");
                            maintenanceProjectInformation.setType("5");
                            Date date = new Date();
                            String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                            auditInfo.setAuditTime(format);
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        }
                    }else if(batchReviewVo.getAuditResult().equals("2")){
                        auditInfo.setAuditResult("2");
                        maintenanceProjectInformation.setType("3");
                        auditInfo.setAuditTime(sdf.format(date));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
                    }
                }
            }
        }


    }

    /**
     * 编辑
     * @param maintenanceProjectInformationVo
     * @param userInfo
     * @param id 审核人id
     */
    public void updateMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo,UserInfo userInfo,String id){


        //修改时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());

        // 根据检维修项目id  查询到的 检维修对象
        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectBymaintenanceItemId(maintenanceProjectInformationVo.getMaintenanceItemId());
        // 检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(maintenanceProjectInformation.getId());
        // 修改时间
        information.setUpdateTime(updateTime);
        information.setDelFlag("0");
        information.setMaintenanceItemId(maintenanceProjectInformationVo.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformationVo.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformationVo.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformationVo.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformationVo.getSubmitTime());
//        编制人
        information.setPreparePeople(maintenanceProjectInformationVo.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformationVo.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformationVo.getConstructionUnitId());
        information.setReviewAmount(maintenanceProjectInformationVo.getReviewAmount());
        information.setRemarkes(maintenanceProjectInformationVo.getRemarkes());
        information.setCustomerName(maintenanceProjectInformationVo.getCustomerName());
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

        if(maintenanceProjectInformationVo.getSurveyInformation() != null){
            Example example = new Example(SurveyInformation.class);
            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("baseProjectId",id);

            SurveyInformation selectSurveyInformation = surveyInformationDao.selectOneByExample(example);

            SurveyInformation surveyInformation = maintenanceProjectInformationVo.getSurveyInformation();

            //修改时间
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String surveyInformationUpdateTime = simpleDateFormat1.format(new Date());

            surveyInformation.setUpdateTime(surveyInformationUpdateTime);
            surveyInformation.setId(selectSurveyInformation.getId());

            surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);
        }



        //结算审核信息
//        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        if(maintenanceProjectInformationVo.getSettlementAuditInformation() != null){
            //查询对应的结算审核信息对象
            Example example1 = new Example(SettlementAuditInformation.class);
            Example.Criteria example1Criteria = example1.createCriteria();

            example1Criteria.andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());

            SettlementAuditInformation selectSettlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);

            SettlementAuditInformation settlementAuditInformation = maintenanceProjectInformationVo.getSettlementAuditInformation();

            // 修改时间
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String settlementAuditInformationUpdateTime = simpleDateFormat2.format(new Date());

            settlementAuditInformation.setUpdateTime(settlementAuditInformationUpdateTime);
            settlementAuditInformation.setId(selectSettlementAuditInformation.getId());

            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }

        // 审核信息

        AuditInfo auditInfo = new AuditInfo();

        String s = UUID.randomUUID().toString().replaceAll("-", "");
        auditInfo.setId(s);
        auditInfo.setBaseProjectId(information.getMaintenanceItemId());

        auditInfo.setAuditResult("0");

        auditInfo.setAuditType("0");

        auditInfo.setStatus("0");

        // 审核人id
        auditInfo.setAuditorId(id);

        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());

        auditInfo.setCreateTime(createDate);

        auditInfoDao.insertSelective(auditInfo);


//        MemberManage memberManage = memberManageDao.selectByIdAndStatus(auditInfo.getId());

        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);


    }

    /**
     * 根据id,查找回显数据
     * @param id
     * @return
     */
    public MaintenanceProjectInformationVo selectMaintenanceProjectInformationById(String id){
//        根据id 找到检维修对象
        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectById(id);

//        返回到页面显示的vo对象
        MaintenanceProjectInformationVo maintenanceProjectInformationVo = new MaintenanceProjectInformationVo();

        maintenanceProjectInformationVo.setId(maintenanceProjectInformation.getId());
        maintenanceProjectInformationVo.setCreateTime(maintenanceProjectInformation.getCreateTime());
        maintenanceProjectInformationVo.setDelFlag(maintenanceProjectInformation.getDelFlag());
        maintenanceProjectInformationVo.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        maintenanceProjectInformationVo.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        maintenanceProjectInformationVo.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        maintenanceProjectInformationVo.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        maintenanceProjectInformationVo.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        maintenanceProjectInformationVo.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        maintenanceProjectInformationVo.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        maintenanceProjectInformationVo.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        maintenanceProjectInformationVo.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        maintenanceProjectInformationVo.setRemarkes(maintenanceProjectInformation.getRemarkes());
        //        information.setFounderId(userInfo.getId());

//        information.setFounderCompanyId(userInfo.getCompanyId());
        // 查询对应的勘探信息对象
        Example example = new Example(SurveyInformation.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("baseProjectId",id);

        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);

        if(surveyInformation != null){
            maintenanceProjectInformationVo.setSurveyInformation(surveyInformation);
        }

        //查询对应的结算审核信息对象
        Example example1 = new Example(SettlementAuditInformation.class);
        Example.Criteria example1Criteria = example1.createCriteria();

        example1Criteria.andEqualTo("maintenanceProjectInformation",id);

        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);

        if(settlementAuditInformation != null){
            maintenanceProjectInformationVo.setSettlementAuditInformation(settlementAuditInformation);
        }

        //查询对应的审核信息对象

        Example auditExample = new Example(AuditInfo.class);

        Example.Criteria auditExampleCriteria = auditExample.createCriteria();

        auditExampleCriteria.andEqualTo("baseProjectId",maintenanceProjectInformation.getId());

        AuditInfo auditInfo = auditInfoDao.selectOneByExample(auditExample);
        if(auditInfo != null){
            maintenanceProjectInformationVo.setAuditInfo(auditInfo);
        }
        return maintenanceProjectInformationVo;
    }

    /**
     * 新增--保存
     */
    public void saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo){
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
//        SurveyInformation surveyInformation = new SurveyInformation();
//        surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
//        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyInformation().getSurveyDate());
//        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getSurveyInformation().getInvestigationPersonnel());
//        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyInformation().getSurveyBriefly());
        if(maintenanceProjectInformation.getSurveyInformation() != null){
            SurveyInformation surveyInformation = maintenanceProjectInformation.getSurveyInformation();
            String sid = UUID.randomUUID().toString().replace("-","");
            surveyInformation.setId(sid);
            //创建时间
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String surveyInformationCreateTime = simpleDateFormat1.format(new Date());

            surveyInformation.setCreateTime(surveyInformationCreateTime);
            // todo 项目基本信息的id
            surveyInformation.setBaseProjectId(information.getId());

            surveyInformationDao.insertSelective(surveyInformation);
        }



        //结算审核信息
//        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        if(maintenanceProjectInformation.getSettlementAuditInformation() != null){
            SettlementAuditInformation settlementAuditInformation = maintenanceProjectInformation.getSettlementAuditInformation();
            String sid = UUID.randomUUID().toString().replace("-","");
            settlementAuditInformation.setId(sid);
            // 创建时间
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String settlementAuditInformationCreateTime = simpleDateFormat2.format(new Date());

            settlementAuditInformation.setCreateTime(settlementAuditInformationCreateTime);

            // todo 检维修项目信息id
            settlementAuditInformation.setMaintenanceProjectInformation(information.getId());
            settlementAuditInformationDao.insertSelective(settlementAuditInformation);
        }


        maintenanceProjectInformationMapper.insertSelective(information);

    }

    /**
     * @Author Armyman
     * @Description //检维修任务统计数量
     * @Date 11:26 2020/10/8
     **/
    public StatisticalNumberVo statisticalNumber(String projectAddress) {
        return maintenanceProjectInformationMapper.statisticalNumber(projectAddress);
    }

    public List<StatisticalFigureVo> statisticalFigure(String projectAddress, String startDate, String endDate, UserInfo loginUser) {
        return maintenanceProjectInformationMapper.statisticalFigure(projectAddress,startDate,endDate);
    }

    /**
     * @Author Armyman
     * @Description //本月任务
     * @Date 20:18 2020/10/8
     **/
    public Integer month() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        String month = String.valueOf(now.get(Calendar.MONTH) + 1);

         String day = year+"-"+month;
        return maintenanceProjectInformationMapper.monthCount(day);
    }

    /**
     * @Author Armyman
     * @Description // 本年任务
     * @Date 20:18 2020/10/8
     **/
    public Integer year() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        return maintenanceProjectInformationMapper.yearCount(year);
    }

    /**
     * @Author Armyman
     * @Description //上月任务
     * @Date 20:18 2020/10/8
     **/
    public Integer month2() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        String month = String.valueOf(now.get(Calendar.MONTH));

         String day = year+"-"+month;
        return maintenanceProjectInformationMapper.monthCount(day);
    }

    /**
     * @Author Armyman
     * @Description // 上年任务
     * @Date 20:18 2020/10/8
     **/
    public Integer year2() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR) - 1);
        return maintenanceProjectInformationMapper.yearCount(year);
    }

}

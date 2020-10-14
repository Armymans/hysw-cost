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
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
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

    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;


    /**
     * 分页查询所有
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformationReturnVo> findAllMaintenanceProjectInformation(PageRequest pageRequest,UserInfo userInfo){
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos = maintenanceProjectInformationMapper.selectAllByDelFlag(pageRequest);
        PageInfo<MaintenanceProjectInformationReturnVo> projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos);

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
        if(pageRequest.getType() != null && (!"".equals(pageRequest.getType()))){
            criteria.andEqualTo("type",pageRequest.getType());
        }

        // TODO 登陆人
//        criteria.andEqualTo("founderId",userInfo.getId());
        //  地区
        if(pageRequest.getDistrict() != null && (!"".equals(pageRequest.getDistrict()))){
            criteria.andEqualTo("projectAddress",pageRequest.getDistrict());
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
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo,String id) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");


        //创建时间1
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());


        if (maintenanceProjectInformation.getAuditorId() != null) {
            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            //todo 暂时添加LoginUserId
            auditInfo.setFounderId(maintenanceProjectInformation.getLoginUserId());
            auditInfo.setCompanyId(maintenanceProjectInformation.getLoginUserId());
            auditInfo.setAuditorId(maintenanceProjectInformation.getAuditorId());
            // 数据状态 0:正常 1:删除
            auditInfo.setStatus("0");
            // 0一审1二审2变更一审3变更二审
            auditInfo.setAuditType("0");
            // 0未审批 1通过 2未通过
            auditInfo.setAuditResult("0");
            auditInfo.setAuditOpinion(maintenanceProjectInformation.getAuditOpinion());
            auditInfo.setCreateTime(createTime);

            auditInfoDao.insertSelective(auditInfo);

            //检维修对象
            MaintenanceProjectInformation information = new MaintenanceProjectInformation();
            if (maintenanceProjectInformation.getMaintenanceItemId() != null) {
                information.setId(uuid);
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
                information.setFounderId(userInfo.getId());
                information.setFounderCompanyId(userInfo.getCompanyId());
            }
            //勘探信息
            SurveyInformation surveyInformation = new SurveyInformation();
            if (maintenanceProjectInformation.getInvestigationPersonnel() != null) {
                String sid = UUID.randomUUID().toString().replace("-", "");
                surveyInformation.setId(sid);
                surveyInformation.setCreateTime(simpleDateFormat.format(new Date()));
                surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());

                //勘察人员
                surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
                surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());
                // todo 项目基本信息的id
                surveyInformation.setFounderId(userInfo.getId());
                surveyInformation.setFounderCompanyId(userInfo.getCompanyId());
                surveyInformationDao.insertSelective(surveyInformation);
            }


            //结算审核信息
            SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
            if (maintenanceProjectInformation.getAuthorizedNumber() != null) {
                String saiId = UUID.randomUUID().toString().replace("-", "");
                settlementAuditInformation.setId(saiId);
                settlementAuditInformation.setCreateTime(simpleDateFormat.format(new Date()));

                // todo 检维修项目信息id
                settlementAuditInformation.setMaintenanceProjectInformation(information.getId());

                settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
                settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
                settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());

                settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
                settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
                settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
                settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
                settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
                settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
                settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
                settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
                settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
                settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

                settlementAuditInformation.setFounderId(maintenanceProjectInformation.getLoginUserId());
                settlementAuditInformation.setFounderCompanyId(maintenanceProjectInformation.getLoginUserId());
                settlementAuditInformationDao.insertSelective(settlementAuditInformation);

            }
            // 勘探金额

            InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
            if (maintenanceProjectInformation.getOutboundAmount().toString() != null || maintenanceProjectInformation.getMaterialDifferenceAmount().toString() != null || maintenanceProjectInformation.getUnbalancedQuotationAdjustment() != null) {
                String ioaId = UUID.randomUUID().toString().replace("-", "");
                investigationOfTheAmount.setId(ioaId);
                investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
                investigationOfTheAmount.setDelFlag("0");

                investigationOfTheAmount.setFounderId(maintenanceProjectInformation.getLoginUserId());
                investigationOfTheAmount.setFounderCompanyId(maintenanceProjectInformation.getLoginUserId());
                investigationOfTheAmount.setMaintenanceProjectInformation(maintenanceProjectInformation.getId());

                investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
                investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
                investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
                investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());
                investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
                investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);
                maintenanceProjectInformationMapper.insertSelective(information);
            }

            //保存
        } else {
            //检维修对象
            MaintenanceProjectInformation information = new MaintenanceProjectInformation();
            if (maintenanceProjectInformation.getMaintenanceItemId() != null) {
                information.setId(uuid);
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
                information.setFounderId(maintenanceProjectInformation.getLoginUserId());
                information.setFounderCompanyId(maintenanceProjectInformation.getLoginUserId());
                maintenanceProjectInformationMapper.insertSelective(information);

            }
            //勘探信息
            SurveyInformation surveyInformation = new SurveyInformation();
            if (maintenanceProjectInformation.getInvestigationPersonnel() != null) {
                String sid = UUID.randomUUID().toString().replace("-", "");
                surveyInformation.setId(sid);
                surveyInformation.setCreateTime(simpleDateFormat.format(new Date()));
                surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());

                //勘察人员
                surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
                surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());
                // todo 项目基本信息的id
                surveyInformation.setFounderId(maintenanceProjectInformation.getLoginUserId());
                surveyInformation.setFounderCompanyId(maintenanceProjectInformation.getLoginUserId());
                surveyInformationDao.insertSelective(surveyInformation);
            }


            //结算审核信息
            SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
            if (maintenanceProjectInformation.getAuthorizedNumber() != null) {
                String saiId = UUID.randomUUID().toString().replace("-", "");
                settlementAuditInformation.setId(saiId);
                settlementAuditInformation.setCreateTime(simpleDateFormat.format(new Date()));

                // todo 检维修项目信息id
                settlementAuditInformation.setMaintenanceProjectInformation(information.getId());
                settlementAuditInformation.setDelFlag("0");
                settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
                settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
                settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());
                settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
                settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
                settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
                settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
                settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
                settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
                settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
                settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
                settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
                settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

                settlementAuditInformation.setFounderId(maintenanceProjectInformation.getLoginUserId());
                settlementAuditInformation.setFounderCompanyId(maintenanceProjectInformation.getLoginUserId());
                settlementAuditInformation.setBaseProjectId(maintenanceProjectInformation.getBaseProjectId());
                settlementAuditInformationDao.insertSelective(settlementAuditInformation);

            }
            // 勘探金额
            InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
//            强转BigDecimal进行判断
            BigDecimal OutboundAmount = maintenanceProjectInformation.getOutboundAmount();
            String amount = OutboundAmount+"";

            BigDecimal materialDifferenceAmount = maintenanceProjectInformation.getMaterialDifferenceAmount();
            String amount1 = materialDifferenceAmount+"";

            BigDecimal unbalancedQuotationAdjustment = maintenanceProjectInformation.getUnbalancedQuotationAdjustment();
            String amount2 = unbalancedQuotationAdjustment+"";

            if (amount != null || amount1 != null || amount2 != null) {
                String uid = UUID.randomUUID().toString().replace("-"," ");
               investigationOfTheAmount.setId(uid);
               investigationOfTheAmount.setSurveyDate(simpleDateFormat.format(new Date()));
               investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
               investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
               investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
               investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getRemarkes());
               investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());
               investigationOfTheAmount.setDelFlag("0");
               investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getRemarkes());

            }

        }
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

//    /**
//     * 编辑
//     * @param maintenanceProjectInformationVo
//     * @param userInfo
//     * @param id 审核人id
//     */
//    public void updateMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo,UserInfo userInfo,String id){
//
//
//        //修改时间
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String updateTime = simpleDateFormat.format(new Date());
//
//        // 根据检维修项目id  查询到的 检维修对象
//        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectBymaintenanceItemId(maintenanceProjectInformationVo.getMaintenanceItemId());
//        // 检维修对象
//        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
//        information.setId(maintenanceProjectInformation.getId());
//        // 修改时间
//        information.setUpdateTime(updateTime);
//        information.setDelFlag("0");
//        information.setMaintenanceItemId(maintenanceProjectInformationVo.getMaintenanceItemId());
//        information.setMaintenanceItemName(maintenanceProjectInformationVo.getMaintenanceItemName());
//        information.setMaintenanceItemType(maintenanceProjectInformationVo.getMaintenanceItemType());
//        information.setSubmittedDepartment(maintenanceProjectInformationVo.getSubmittedDepartment());
//        information.setSubmitTime(maintenanceProjectInformationVo.getSubmitTime());
////        编制人
//        information.setPreparePeople(maintenanceProjectInformationVo.getPreparePeople());
//        information.setProjectAddress(maintenanceProjectInformationVo.getProjectAddress());
//        information.setConstructionUnitId(maintenanceProjectInformationVo.getConstructionUnitId());
//        information.setReviewAmount(maintenanceProjectInformationVo.getReviewAmount());
//        information.setRemarkes(maintenanceProjectInformationVo.getRemarkes());
//        information.setCustomerName(maintenanceProjectInformationVo.getCustomerName());
//        //        information.setFounderId(userInfo.getId());
//
////        information.setFounderCompanyId(userInfo.getCompanyId());
//        //勘探信息
////        SurveyInformation surveyInformation = new SurveyInformation();
////        surveyInformation.setId(UUID.randomUUID().toString().replace("-",""));
////        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyInformation().getSurveyDate());
////        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getSurveyInformation().getInvestigationPersonnel());
////        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyInformation().getSurveyBriefly());
////
////        surveyInformationDao.insertSelective(surveyInformation);
//
//        if(maintenanceProjectInformationVo.getSurveyInformation() != null){
//            Example example = new Example(SurveyInformation.class);
//            Example.Criteria criteria = example.createCriteria();
//
//            criteria.andEqualTo("baseProjectId",id);
//
//            SurveyInformation selectSurveyInformation = surveyInformationDao.selectOneByExample(example);
//
//            SurveyInformation surveyInformation = maintenanceProjectInformationVo.getSurveyInformation();
//
//            //修改时间
//            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String surveyInformationUpdateTime = simpleDateFormat1.format(new Date());
//
//            surveyInformation.setUpdateTime(surveyInformationUpdateTime);
//            surveyInformation.setId(selectSurveyInformation.getId());
//
//            surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);
//        }
//
//
//
//        //结算审核信息
////        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
//
//        if(maintenanceProjectInformationVo.getSettlementAuditInformation() != null){
//            //查询对应的结算审核信息对象
//            Example example1 = new Example(SettlementAuditInformation.class);
//            Example.Criteria example1Criteria = example1.createCriteria();
//
//            example1Criteria.andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());
//
//            SettlementAuditInformation selectSettlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
//
//            SettlementAuditInformation settlementAuditInformation = maintenanceProjectInformationVo.getSettlementAuditInformation();
//
//            // 修改时间
//            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String settlementAuditInformationUpdateTime = simpleDateFormat2.format(new Date());
//
//            settlementAuditInformation.setUpdateTime(settlementAuditInformationUpdateTime);
//            settlementAuditInformation.setId(selectSettlementAuditInformation.getId());
//
//            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
//        }
//
//        // 审核信息
//
//        AuditInfo auditInfo = new AuditInfo();
//
//        String s = UUID.randomUUID().toString().replaceAll("-", "");
//        auditInfo.setId(s);
//        auditInfo.setBaseProjectId(information.getMaintenanceItemId());
//
//        auditInfo.setAuditResult("0");
//
//        auditInfo.setAuditType("0");
//
//        auditInfo.setStatus("0");
//
//        // 审核人id
//        auditInfo.setAuditorId(id);
//
//        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
//
//        auditInfo.setCreateTime(createDate);
//
//        auditInfoDao.insertSelective(auditInfo);
//
//
////        MemberManage memberManage = memberManageDao.selectByIdAndStatus(auditInfo.getId());
//
//        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);
//
//
//    }

    /**
     * 根据id,查找回显数据
     * @param id
     * @return
     */
    public MaintenanceVo selectMaintenanceProjectInformationById(String id){

        MaintenanceVo maintenanceVo = new MaintenanceVo();

        MaintenanceProjectInformation information = maintenanceProjectInformationMapper.selectByPrimaryKey(id);

//        private SurveyInformation surveyInformation;
//        private SettlementAuditInformation settlementAuditInformation;
//        private InvestigationOfTheAmount investigationOfTheAmount;
        Example example = new Example(SurveyInformation.class);
        example.createCriteria().andEqualTo("baseProjectId",information.getMaintenanceItemId());
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);

        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation",information.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation",information.getMaintenanceItemId());
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example2);

        return maintenanceVo;
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
        information.setFounderId(userInfo.getId());

        information.setFounderCompanyId(userInfo.getCompanyId());

        //勘探信息
        SurveyInformation surveyInformation = new SurveyInformation();
        String sid = UUID.randomUUID().toString().replace("-","");
        surveyInformation.setId(sid);
        surveyInformation.setCreateTime(simpleDateFormat.format(new Date()));
        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());
        // todo 项目基本信息的id
        surveyInformation.setBaseProjectId(information.getId());
        surveyInformation.setFounderId(userInfo.getId());
        surveyInformation.setFounderCompanyId(userInfo.getCompanyId());
        surveyInformationDao.insertSelective(surveyInformation);




        //结算审核信息
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        String saiId = UUID.randomUUID().toString().replace("-","");
        settlementAuditInformation.setId(saiId);
        settlementAuditInformation.setCreateTime(simpleDateFormat.format(new Date()));

        // todo 检维修项目信息id
        settlementAuditInformation.setMaintenanceProjectInformation(information.getId());

        settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
        settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
        settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());

        settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
        settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
        settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

        settlementAuditInformation.setFounderId(userInfo.getId());
        settlementAuditInformation.setFounderCompanyId(userInfo.getCompanyId());
        settlementAuditInformationDao.insertSelective(settlementAuditInformation);


        // 勘探金额

        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();

        String ioaId = UUID.randomUUID().toString().replace("-","");
        investigationOfTheAmount.setId(ioaId);
        investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
        investigationOfTheAmount.setDelFlag("0");

        investigationOfTheAmount.setFounderId(userInfo.getId());
        investigationOfTheAmount.setFounderCompanyId(userInfo.getCompanyId());
        investigationOfTheAmount.setMaintenanceProjectInformation(maintenanceProjectInformation.getId());
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(maintenanceProjectInformation.getId());

//        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.get);
        investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());

        investigationOfTheAmountDao.insert(investigationOfTheAmount);


        maintenanceProjectInformationMapper.insertSelective(information);

    }

    /**
     * 编辑--保存
     */
    public void updateSaveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo){

        //修改时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());

        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(maintenanceProjectInformation.getId());
        information.setUpdateTime(updateTime);
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
        information.setFounderId(userInfo.getId());

        information.setFounderCompanyId(userInfo.getCompanyId());

        //勘探信息

        Example example = new Example(SurveyInformation.class);
        example.createCriteria().andEqualTo("baseProjectId",maintenanceProjectInformation.getId());
        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);

        surveyInformation.setUpdateTime(simpleDateFormat.format(new Date()));
        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());

        surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);


        //结算审核信息

        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);

        settlementAuditInformation.setUpdateTime(simpleDateFormat.format(new Date()));


        settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
        settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
        settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());

        settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
        settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
        settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

        settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);


        // 勘探金额

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());

        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example2);


        investigationOfTheAmount.setUpdateTime(simpleDateFormat.format(new Date()));


        investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());

        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);


        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);

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

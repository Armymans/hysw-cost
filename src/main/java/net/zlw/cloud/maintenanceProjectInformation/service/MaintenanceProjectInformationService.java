package net.zlw.cloud.maintenanceProjectInformation.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.client.http.HttpRequest;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.model.OneCensus;
import net.zlw.cloud.index.model.vo.pageVo;
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

        List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos1 = maintenanceProjectInformationMapper.selectAllByDelFlag1(pageRequest);

        PageInfo<MaintenanceProjectInformationReturnVo> projectInformationPageInfo = new PageInfo<>();
        //如果是待审核，或者未通过，根据uid查找
        if("1".equals(pageRequest.getType()) || "3".equals(pageRequest.getType())){
            projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos);
        }else{
            projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos1);
        }

        System.out.println("list:"+projectInformationPageInfo.getList().toString());

        return projectInformationPageInfo;

    }

    /**
     * 统计分析 列表
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformation> list(PageRequest pageRequest,UserInfo userInfo){
        //todo        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

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
            criteria.andLessThanOrEqualTo("createTime",date);

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
                if(memberManage != null){
                    maintenanceProjectInformation.setMemberName(memberManage.getMemberName());
                }
            }

//            maintenanceProjectInformation.setFounderId(userInfo.getId());
//            maintenanceProjectInformation.setFounderCompanyId(userInfo.getCompanyId());
        }

        PageInfo<MaintenanceProjectInformation> projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformations);

        System.err.println("list:"+projectInformationPageInfo.getList().toString());

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
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation,UserInfo userInfo){

        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.err.println(uuid);
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();

        information.setId(uuid);
//        information.setId(maintenanceProjectInformation.getId());
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        if(StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())){
            information.setType("1");
        }else{
            information.setType("2");
        }
        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setCustomerName(maintenanceProjectInformation.getCustomerName());
        information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        information.setFounderId(userInfo.getId());

        information.setFounderCompanyId(userInfo.getCompanyId());

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
        settlementAuditInformation.setBaseProjectId(uuid);
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

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
        investigationOfTheAmount.setId(ioaId);
        investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
        investigationOfTheAmount.setDelFlag("0");

        investigationOfTheAmount.setFounderId(userInfo.getId());
        investigationOfTheAmount.setFounderCompanyId(userInfo.getCompanyId());
        investigationOfTheAmount.setMaintenanceProjectInformation(information.getId());
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(information.getId());

//        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.get);
        investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());

        investigationOfTheAmountDao.insert(investigationOfTheAmount);


        // 审核信息

        if(StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())) {
            AuditInfo auditInfo = new AuditInfo();
            String s = UUID.randomUUID().toString().replaceAll("-", "");
            auditInfo.setId(s);
            auditInfo.setBaseProjectId(information.getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setStatus("0");
            // 审核人id
            auditInfo.setAuditorId(maintenanceProjectInformation.getAuditorId());
            String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
            auditInfo.setCreateTime(createDate);
            auditInfoDao.insertSelective(auditInfo);
        }
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
//                            auditInfo.setAuditTime(format);
//                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());


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


        //结算审核信息
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        settlementAuditInformation.setUpdateTime(simpleDateFormat.format(new Date()));

        // todo 检维修项目信息id
        settlementAuditInformation.setMaintenanceProjectInformation(information.getId());

        settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformationVo.getAuthorizedNumber());
        settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformationVo.getSubtractTheNumber());
        settlementAuditInformation.setNuclearNumber(maintenanceProjectInformationVo.getNuclearNumber());

        settlementAuditInformation.setContractAmount(maintenanceProjectInformationVo.getContractAmount());
        settlementAuditInformation.setBaseProjectId(information.getId());
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformationVo.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformationVo.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformationVo.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformationVo.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformationVo.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformationVo.getContactPhone());
        settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformationVo.getAmountOutsourcing());
        settlementAuditInformation.setCompileTime(maintenanceProjectInformationVo.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformationVo.getRemark());

        Example example = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("maintenanceProjectInformation",information.getId());

        SettlementAuditInformation selectOneByExample = settlementAuditInformationDao.selectOneByExample(example);

        settlementAuditInformation.setId(selectOneByExample.getId());


        settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);


        // 勘探金额

        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();


        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformationVo.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformationVo.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformationVo.getSurveyBriefly());

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformationVo.getAmountRemarks());
        investigationOfTheAmount.setUpdateTime(simpleDateFormat.format(new Date()));

        investigationOfTheAmount.setMaintenanceProjectInformation(information.getId());
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(information.getId());

//        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.get);
        investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformationVo.getUnbalancedQuotationAdjustment());
        investigationOfTheAmount.setPunishAmount(maintenanceProjectInformationVo.getPunishAmount());
        investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformationVo.getOutboundAmount());
        investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformationVo.getMaterialDifferenceAmount());

        Example example1 = new Example(InvestigationOfTheAmount.class);
        Example.Criteria example1Criteria = example1.createCriteria();
        example1Criteria.andEqualTo("maintenanceProjectInformation",information.getId());

        InvestigationOfTheAmount ofTheAmount = investigationOfTheAmountDao.selectOneByExample(example1);

        investigationOfTheAmount.setId(ofTheAmount.getId());
        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);

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
    public MaintenanceVo selectMaintenanceProjectInformationById(String id){

        MaintenanceVo maintenanceVo = new MaintenanceVo();

        MaintenanceProjectInformation information = maintenanceProjectInformationMapper.selectByPrimaryKey(id);
        if(information != null){
            maintenanceVo.setMaintenanceProjectInformation(information);
        }else{
            maintenanceVo.setMaintenanceProjectInformation(new MaintenanceProjectInformation());
        }


        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation",information.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if(settlementAuditInformation != null){
            maintenanceVo.setSettlementAuditInformation(settlementAuditInformation);
        }else{
            maintenanceVo.setSettlementAuditInformation(new SettlementAuditInformation());
        }

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation",information.getId());
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example2);
        if(investigationOfTheAmount != null){
            maintenanceVo.setInvestigationOfTheAmount(investigationOfTheAmount);
        }else {
            maintenanceVo.setInvestigationOfTheAmount(new InvestigationOfTheAmount());
        }
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
        if(StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())){
            information.setType("1");
        }else{
            information.setType("2");
        }
        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setCustomerName(maintenanceProjectInformation.getCustomerName());
        information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        information.setFounderId(userInfo.getId());

        information.setFounderCompanyId(userInfo.getCompanyId());

        //勘探信息
//        SurveyInformation surveyInformation = new SurveyInformation();
//        String sid = UUID.randomUUID().toString().replace("-","");
//        surveyInformation.setId(sid);
//        surveyInformation.setCreateTime(simpleDateFormat.format(new Date()));
//        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
//        //勘察人员
//        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
//        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());
//        // todo 项目基本信息的id
//        surveyInformation.setBaseProjectId(id);
//        surveyInformation.setFounderId(userInfo.getId());
//        surveyInformation.setFounderCompanyId(userInfo.getCompanyId());
//        surveyInformationDao.insertSelective(surveyInformation);




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
        settlementAuditInformation.setBaseProjectId(id);
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

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());

        investigationOfTheAmount.setId(ioaId);
        investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
        investigationOfTheAmount.setDelFlag("0");

        investigationOfTheAmount.setFounderId(userInfo.getId());
        investigationOfTheAmount.setFounderCompanyId(userInfo.getCompanyId());
        investigationOfTheAmount.setMaintenanceProjectInformation(id);
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(id);

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
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
//
//        information.setFounderCompanyId(userInfo.getCompanyId());

        //勘探信息

//        Example example = new Example(SurveyInformation.class);
//        example.createCriteria().andEqualTo("baseProjectId",maintenanceProjectInformation.getId());
//        SurveyInformation surveyInformation = surveyInformationDao.selectOneByExample(example);
//
//        surveyInformation.setUpdateTime(simpleDateFormat.format(new Date()));
//        surveyInformation.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
//        //勘察人员
//        surveyInformation.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
//        surveyInformation.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());
//
//        surveyInformationDao.updateByPrimaryKeySelective(surveyInformation);


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

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());


        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
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

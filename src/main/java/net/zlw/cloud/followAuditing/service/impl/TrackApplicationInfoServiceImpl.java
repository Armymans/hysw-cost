package net.zlw.cloud.followAuditing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.mapper.EmployeeAchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.OutSourceMapper;
import net.zlw.cloud.designProject.model.EmployeeAchievementsInfo;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.OutSource;
import net.zlw.cloud.designProject.service.ProjectSumService;
import net.zlw.cloud.followAuditing.mapper.DesignUnitManagementDao;
import net.zlw.cloud.followAuditing.mapper.TrackApplicationInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.DesignUnitManagement;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TrackApplicationInfoServiceImpl implements TrackApplicationInfoService {
    @Resource
    private OutSourceMapper outSourceMapper;
    @Resource
    private TrackApplicationInfoDao trackApplicationInfoDao;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private TrackMonthlyDao trackMonthlyDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private DesignUnitManagementDao designUnitManagementDao;
    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;
    @Resource
    private OperationLogDao operationLogDao;
    @Resource
    private MemberService memberService;


    @Resource
    private RemindSetMapper remindSetMapper;

    @Resource
    private MessageService messageService;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Resource
    private ProjectSumService projectSumService;
    @Resource
    private EmployeeAchievementsInfoMapper employeeAchievementsInfoMapper;

    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh;  //吴江造价领导
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm; //吴江造价经理

    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh; //芜湖造价领导
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm; //芜湖造价经理

    @Override
    public PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo) {
        // 设置分页助手
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        ArrayList<ReturnTrackVo> returnTrackVos1 = new ArrayList<>();
        PageInfo<ReturnTrackVo> pageInfo = new PageInfo<>();

        //待审核领导看所有，部门主管也看所有，员工自己创建的（无互审）
        if ("1".equals(pageVo.getTrackStatus())) {
            //如果当前用户等于芜湖吴江部门主管.部门经理则 展示所有待审核信息
            if (wjzjh.equals(pageVo.getUid()) || wjzjm.equals(pageVo.getUid()) || whzjh.equals(pageVo.getUid())
                    || whzjm.equals(pageVo.getUid())) {
                //如果为部门领导查看所有
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList1(pageVo);
                //需要赋值当前负责人
                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    // 编制人
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(returnTrackVo.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }
                    // 造价单位名称
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getAuditUnitNameId());
                    if (costUnitManagement != null){
                        returnTrackVo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
                    }else {
                        returnTrackVo.setAuditUnitNameId("/");
                    }
                    // 施工单位
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getConstructionOrganization());
                    if (constructionUnitManagement != null){
                        returnTrackVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }else {
                        returnTrackVo.setConstructionOrganization("/");
                    }

                    List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByTrickId2(returnTrackVo.getId());
                    for (TrackMonthly trackMonthly : trackMonthlies) {
                        //审核信息为未审核状态得
                        Example example = new Example(AuditInfo.class);
                        example.createCriteria().andEqualTo("baseProjectId", trackMonthly.getId())
                                .andEqualTo("auditResult", "0");
                        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                        if (auditInfo != null) {
                            if (auditInfo.getAuditorId() != null) {
                                //获得当前审核人
                                Example example1 = new Example(MemberManage.class);
                                example1.createCriteria().andEqualTo("id", auditInfo.getAuditorId());
                                MemberManage memberManage1 = memberManageDao.selectOneByExample(example1);
                                if (memberManage1 != null) {
                                    returnTrackVo.setCurrentHandler(memberManage1.getMemberName());
                                } else {
                                    returnTrackVo.setCurrentHandler("暂未审核");
                                }
                            }
                        }
                    }
                }
                pageInfo = new PageInfo<>(returnTrackVos);
            } else {
                //普通员工则根据创建人查看
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);
                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    // 编制人
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(returnTrackVo.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }
                    // 造价单位名称
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getAuditUnitNameId());
                    if (costUnitManagement != null){
                        returnTrackVo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
                    }else {
                        returnTrackVo.setAuditUnitNameId("/");
                    }
                    // 施工单位
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getConstructionOrganization());
                    if (constructionUnitManagement != null){
                        returnTrackVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }else {
                        returnTrackVo.setConstructionOrganization("/");
                    }
                    List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByTrickId2(returnTrackVo.getId());
                    for (TrackMonthly trackMonthly : trackMonthlies) {
                        //审核信息为未审核状态得
                        Example example = new Example(AuditInfo.class);
                        example.createCriteria().andEqualTo("baseProjectId", trackMonthly.getId())
                                .andEqualTo("auditResult", "0");
                        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                        if (auditInfo != null) {
                            if (auditInfo.getAuditorId() != null) {
                                //获得当前审核人
                                Example example1 = new Example(MemberManage.class);
                                example1.createCriteria().andEqualTo("id", auditInfo.getAuditorId());
                                MemberManage memberManage1 = memberManageDao.selectOneByExample(example1);
                                if (memberManage1 != null) {
                                    returnTrackVo.setCurrentHandler(memberManage1.getMemberName());
                                } else {
                                    returnTrackVo.setCurrentHandler("暂未审核");
                                }
                            }
                        }
                    }
                }
                pageInfo = new PageInfo<>(returnTrackVos);
            }
        } else {
            //待确认
            if("6".equals(pageVo.getTrackStatus())){
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList2(pageVo);
                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(returnTrackVo.getId());
                    Example example1 = new Example(TrackMonthly.class);
                    example1.createCriteria().andEqualTo("trackId",trackAuditInfo.getId())
                                             .andEqualTo("status","0");
                    TrackMonthly trackMonthly1 = trackMonthlyDao.selectOneByExample(example1);
                    if (trackAuditInfo != null){
                        Example example = new Example(AuditInfo.class);
                        example.createCriteria().andEqualTo("baseProjectId",trackMonthly1.getId())
                                                .andEqualTo("status","0")
                                                .andEqualTo("auditType","0");
                        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                        MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                        // 当前处理人
                        if (memberManage != null){
                            returnTrackVo.setCurrentHandler(memberManage.getMemberName());
                        }
                        // 判断退回显隐
                        if (auditInfo.getAuditorId().equals(pageVo.getUid())){
                            returnTrackVo.setIsShow("1");
                        }
                    }
                    // 填写人
                    Example example = new Example(TrackMonthly.class);
                    example.createCriteria().andEqualTo("trackId",returnTrackVo.getId())
                                            .andEqualTo("status","0");
                    TrackMonthly trackMonthly = trackMonthlyDao.selectOneByExample(example);
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(trackMonthly.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }

                }
                pageInfo = new PageInfo<>(returnTrackVos);
            }else if ("3".equals(pageVo.getTrackStatus())){ // 未通过
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);
                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    // 施工单位
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getConstructionOrganization());
                    if (constructionUnitManagement != null){
                        returnTrackVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }else {
                        returnTrackVo.setConstructionOrganization("/");
                    }
                    // 编制人
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(returnTrackVo.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }
                    // 造价单位名称
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getAuditUnitNameId());
                    if (costUnitManagement != null){
                        returnTrackVo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
                    }else {
                        returnTrackVo.setAuditUnitNameId("/");
                    }
                }
                pageInfo = new PageInfo<>(returnTrackVos);
            }else if ("5".equals(pageVo.getTrackStatus())) {
                //进行中，已完成不分层级，都能看到
                //但是进行中的和已完成按钮除查看只有领导和创建人可操作
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList1(pageVo);

                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    if (pageVo.getUid().equals(returnTrackVo.getFounderId())){
                        returnTrackVo.setIsShow("1");
                    }
                    TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(returnTrackVo.getId());
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
                    String district = baseProject.getDistrict();
                    String designCategory = baseProject.getDesignCategory();
                    if (district == null || "".equals(district)){
                        returnTrackVo.setAttributionShow("0");
                    }
                    if (designCategory == null || "".equals(designCategory)){
                        returnTrackVo.setAttributionShow("0");
                    }

                    // 施工单位
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getConstructionOrganization());
                    if (constructionUnitManagement != null){
                        returnTrackVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }else {
                        returnTrackVo.setConstructionOrganization("/");
                    }
                    // 编制人
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(returnTrackVo.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }
                    // 造价单位名称
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getAuditUnitNameId());
                    if (costUnitManagement != null){
                        returnTrackVo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
                    }else {
                        returnTrackVo.setAuditUnitNameId("/");
                    }
                    if (returnTrackVo.getFounderId().equals(pageVo.getUid())) {
                        returnTrackVo.setShowEdit("1");
                    } else {
                        returnTrackVo.setShowEdit("0");
                    }
                }
                pageInfo = new PageInfo<>(returnTrackVos);
            } else {
                //全部，未提交和谁创建谁看到
                List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);
                for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                    // 施工单位
                    ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getConstructionOrganization());
                    if (constructionUnitManagement != null){
                        returnTrackVo.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
                    }else {
                        returnTrackVo.setConstructionOrganization("/");
                    }
                    // 编制人
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(returnTrackVo.getWritter());
                    if (memberManage != null){
                        returnTrackVo.setWritter(memberManage.getMemberName());
                    }
                    // 造价单位名称
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(returnTrackVo.getAuditUnitNameId());
                    if (costUnitManagement != null){
                        returnTrackVo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
                    }else {
                        returnTrackVo.setAuditUnitNameId("/");
                    }
                }
                pageInfo = new PageInfo<>(returnTrackVos);
            }
        }
        return pageInfo;
//        if ("1".equals(pageVo.getTrackStatus()) || "4".equals(pageVo.getTrackStatus())) {
//            List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);
//            for (ReturnTrackVo returnTrackVo : returnTrackVos) {
//                //当前处理人
//                Example example = new Example(AuditInfo.class);
//                example.createCriteria().andEqualTo("baseProjectId", returnTrackVo.getId())
//                        .andEqualTo("auditResult", "0");
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//                if (auditInfo != null) {
//                    if (auditInfo.getAuditorId() != null) {
//                        Example example1 = new Example(MemberManage.class);
//                        example1.createCriteria().andEqualTo("id", auditInfo.getAuditorId());
//                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                        if (memberManage != null) {
//                            returnTrackVo.setCurrentHandler(memberManage.getMemberName());
//                        }
//                    }
//                }
//
//
//                if (!returnTrackVos1.contains(returnTrackVo)) {
//                    returnTrackVos1.add(returnTrackVo);
//                }
//            }
//            pageInfo = new PageInfo<>(returnTrackVos1);
//
//        } else {
//
//            List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList1(pageVo);
//            for (ReturnTrackVo returnTrackVo : returnTrackVos) {
//                if (!returnTrackVos1.contains(returnTrackVo)) {
//                    returnTrackVos1.add(returnTrackVo);
//                }
//            }
//            pageInfo = new PageInfo<>(returnTrackVos1);
//
//        }
//
//        return pageInfo;
    }

    // 删除审计月报
    public void deleteByIdTrackMonthly(String id) {
        TrackMonthly trackMonthly = trackMonthlyDao.selectByPrimaryKey(id);
        trackMonthly.setStatus("1");
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void deleteById(String id,UserInfo loginUser , HttpServletRequest request) {
        TrackAuditInfo trackAuditInfo = new TrackAuditInfo();
        trackAuditInfo.setId(id);
        trackAuditInfo.setStatus("1");
        trackAuditInfoDao.updateByPrimaryKeySelective(trackAuditInfo);

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit", id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);
        trackApplicationInfo.setState("1");
        trackApplicationInfoDao.updateByPrimaryKeySelective(trackApplicationInfo);

        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId", id);
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        if (trackMonthlies.size() > 0) {
            for (TrackMonthly trackMonthly : trackMonthlies) {
                trackMonthly.setStatus("1");
                trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
            }
        }
        // 操作日志
        String userId = loginUser.getId();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("8"); //跟踪审计项目
        operationLog.setContent(memberManage.getMemberName()+"删除了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
        operationLog.setDoObject(trackAuditInfo.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo, UserInfo userInfo,HttpServletRequest request) {
        //获取当前用户id
        //todo userInfo.getId(); userInfo.getCompanyId();
        String userId = userInfo.getId();
        String username = userInfo.getUsername();
        //获取当前公司id
        String companyId = userInfo.getCompanyId();
        //时间
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前登陆人信息
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        //根据主键查询
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(batchReviewVo.getBatchAll());
        AuditInfo auditInfo = new AuditInfo();
        TrackMonthly trackMonthlyOld = trackMonthlyDao.selectOne1(trackAuditInfo.getId());
        //查询当前审核信息
        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId", trackMonthlyOld.getId()); //审核信息外键
        criteria.andEqualTo("auditorId", userId); //审核人
        criteria.andEqualTo("auditResult", "0"); //审核状态
        auditInfo = auditInfoDao.selectOneByExample(example);


        //基本信息
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
        //查询当前该项目归哪个地区负责(根据项目创建人的地区判断)
        MemberManage creater = memberManageDao.selectByPrimaryKey(trackAuditInfo.getFounderId());
        //说明当前用户可以进行审核
        if (memberManage != null) {
            //如果当前审核人是一级领导
            if (whzjh.equals(memberManage.getId()) || wjzjh.equals(memberManage.getId())) {
                //如果为通过
                if ("1".equals(batchReviewVo.getAuditResult())) {

//                        String s = trackMonthlyDao.selectByTrickId(trackAuditInfo.getId());


                    //将审核状态改为 待确认
                    baseProject.setTrackStatus("6");
                } else {
                    //将审核状态改为 未通过
                    auditInfo.setAuditType("1");
                    baseProject.setTrackStatus("4");
                }
                //修改之前的审核信息
                auditInfo.setAuditResult(batchReviewVo.getAuditResult());
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }
            //如果当前审核人是二级领导审核
            if (whzjm.equals(memberManage.getId()) || wjzjm.equals(memberManage.getId())) {
                String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                //如果为通过
                if ("1".equals(batchReviewVo.getAuditResult())) {
                    //将审核状态改为 进行中(二审)
                    baseProject.setTrackStatus("3");
//                    auditInfo.setAuditType("1");

                    // 加入委外金额
                    OutSource outSource = new OutSource();
                    outSource.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    if ("0".equals(trackAuditInfo.getOutsource())){
                        outSource.setOutMoney(trackAuditInfo.getOutsourceMoney().toString());
                    }else {
                        outSource.setOutMoney("0");
                    }
                    outSource.setDistrict(baseProject.getDistrict());
                    outSource.setDept("2"); //1.设计 2.造价
                    outSource.setDelFlag("0"); //0.正常 1.删除
                    outSource.setOutType("5"); // 跟踪审计委外金额
                    outSource.setBaseProjectId(baseProject.getId()); //基本信息表外键
                    outSource.setProjectNum(trackAuditInfo.getId()); //跟踪审计信息外键
                    outSource.setCreateTime(data);
                    outSource.setUpdateTime(data);
                    outSource.setFounderId(trackAuditInfo.getFounderId()); //项目创建人
                    outSource.setFounderCompanyId(trackAuditInfo.getCompanyId()); //公司
                    outSourceMapper.insertSelective(outSource);
                    //项目名称
                    String projectName = baseProject.getProjectName();
                    //成员名称
                    String name = memberManage.getMemberName();
                    //发送消息
                    MessageVo messageVo = new MessageVo();
                    messageVo.setId("A20");
                    messageVo.setType("1"); // 通知
                    messageVo.setUserId(userId);
                    messageVo.setTitle("您有一个跟踪审计项目已通过！");
                    messageVo.setDetails(username + "您好！您提交的【" + projectName + "】的跟踪审计项目【" + name + "】已审批通过");
                    messageService.sendOrClose(messageVo);
                } else {
                    //将审核状态改为 未通过
                    baseProject.setTrackStatus("4");
                    auditInfo.setAuditType("1");
                }
                //修改之前的审核信息
                auditInfo.setAuditResult(batchReviewVo.getAuditResult());
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }
        }
        if ("1".equals(batchReviewVo.getAuditResult())) {
            // 操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(memberManage.getMemberName()+"审核通过了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(trackAuditInfo.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }else {
            // 操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(baseProject.getProjectName()+"项目【"+baseProject.getId()+"】"+memberManage.getMemberName()+"审核未通过");
            operationLog.setDoObject(trackAuditInfo.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }
//        String[] split = batchReviewVo.getBatchAll().split(",");
//        if (split != null) {
//            for (String s : split) {
//                Example example = new Example(AuditInfo.class);
//                example.createCriteria().andEqualTo("baseProjectId", s);
//                //跟踪审计审核
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//
//                TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(s);
//                String baseProjectId = trackAuditInfo.getBaseProjectId();
////                Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
////                String baseProjectId = budgeting.getBaseProjectId();
//                System.err.println(baseProjectId);
//
//
//                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
//                if (!auditInfo.getAuditResult().equals("1")) {
//                    auditInfo.setAuditResult(batchReviewVo.getAuditResult());
//                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    auditInfo.setAuditTime(sim.format(new Date()));
//                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                    if (batchReviewVo.getAuditResult().equals("1")) {
//                        baseProject.setTrackStatus("3");
//                    } else if (batchReviewVo.getAuditResult().equals("2")) {
//                        baseProject.setTrackStatus("4");
//                    }
//                }
//                baseProjectDao.updateByPrimaryKeySelective(baseProject);
//                Example example1 = new Example(TrackMonthly.class);
//                example1.createCriteria().andEqualTo("trackId", s);
//                List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
//                if (trackMonthlies != null) {
//                    for (TrackMonthly trackMonthly : trackMonthlies) {
//                        Example example2 = new Example(AuditInfo.class);
//                        example2.createCriteria().andEqualTo("baseProjectId", trackMonthly.getId());
////                        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example2);
//
//                        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
//                        for (AuditInfo info : auditInfos) {
//                            if (!info.getAuditResult().equals("1")) {
//                                info.setAuditResult(batchReviewVo.getAuditResult());
//                                info.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                                info.setAuditTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//                                auditInfoDao.updateByPrimaryKeySelective(info);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void addTrack(TrackVo trackVo, UserInfo userInfo, String baseId, HttpServletRequest request) throws Exception {
        //todo userInfo.getCompanyId() userInfo.getId()
        String userInfoId = userInfo.getId();

        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("id", baseId);
        BaseProject baseProject = baseProjectDao.selectOneByExample(example);
        baseProject.setProjectFlow(baseProject.getProjectFlow() + ",3");
        //0保存1提交
        if ("0".equals(trackVo.getStatus())) {
            // 设置未提交
            baseProject.setTrackStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            TrackAuditInfo auditInfo = trackVo.getAuditInfo();

            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            auditInfo.setBaseProjectId(baseProject.getId());
            //判断如果跟踪审计信息decimal类型如果为null就设置成0
            if (auditInfo.getCeaTotalMoney() == null) {
                auditInfo.setCeaTotalMoney(new BigDecimal("0"));
            }
            if (auditInfo.getContractAmount() == null) {
                auditInfo.setContractAmount(new BigDecimal("0"));
            }
            if (auditInfo.getOutsourceMoney() == null) {
                auditInfo.setOutsourceMoney(new BigDecimal("0"));
            }
            if (auditInfo.getTrackAuditBase() == null) {
                auditInfo.setTrackAuditBase(new BigDecimal("0"));
            }
            trackVo.getAuditInfo().setFounderId(userInfoId);
            trackVo.getAuditInfo().setStatus("0");
            trackVo.getAuditInfo().setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());

            //月报
            Example example1 = new Example(TrackMonthly.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("trackId", userInfoId);
            criteria.andEqualTo("status", "0");
            TrackMonthly trackMonthly1 = trackMonthlyDao.selectOneByExample(example1);
            if (trackMonthly1 == null) {
                throw new Exception("请上传月报");
            }
            trackMonthly1.setAuditCount("1");
            trackMonthly1.setTrackId(auditInfo.getId());
            trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly1);

            // 上传文件，新建-申请信息，列表文件
            List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(trackVo.getKey(), trackVo.getType());

            for (FileInfo fileInfo : byFreignAndType) {
                fileInfo.setPlatCode(trackVo.getAuditInfo().getId());

                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
            // 操作日志
            String userId = userInfo.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(memberManage.getMemberName()+"新增保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(auditInfo.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        } else if ("1".equals(trackVo.getStatus())) {

            baseProject.setTrackStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            //存入跟踪审计
            if (trackVo.getAuditInfo().getCeaTotalMoney() == null) {
                trackVo.getAuditInfo().setCeaTotalMoney(new BigDecimal("0"));
            }
            if (trackVo.getAuditInfo().getContractAmount() == null) {
                trackVo.getAuditInfo().setContractAmount(new BigDecimal("0"));
            }
            if (trackVo.getAuditInfo().getOutsourceMoney() == null) {
                trackVo.getAuditInfo().setOutsourceMoney(new BigDecimal("0"));
            }
            if (trackVo.getAuditInfo().getTrackAuditBase() == null) {
                trackVo.getAuditInfo().setTrackAuditBase(new BigDecimal("0"));
            }
            trackVo.getAuditInfo().setBaseProjectId(baseProject.getId());
            trackVo.getAuditInfo().setId(UUID.randomUUID().toString().replace("-", ""));
            trackVo.getAuditInfo().setFounderId(userInfoId);
            trackVo.getAuditInfo().setStatus("0");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackVo.getAuditInfo().setCreateTime(simpleDateFormat.format(new Date()));
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());

            //月报
            Example example1 = new Example(TrackMonthly.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("trackId", userInfoId);
            criteria.andEqualTo("status", "0");
            TrackMonthly trackMonthly1 = trackMonthlyDao.selectOneByExample(example1);
            if (trackMonthly1 == null) {
                throw new Exception("请上传月报");
            }
            trackMonthly1.setAuditCount("1");
            trackMonthly1.setTrackId(trackVo.getAuditInfo().getId());
            trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly1);
            String id = userInfo.getId();

            //存入审核表
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userInfoId);
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            auditInfo.setBaseProjectId(trackMonthly1.getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(id);
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(simpleDateFormat.format(new Date()));
            //判断当前项目创建人属于哪个地区 根据地区提交给相应领导
            //如果为芜湖
            if ("1".equals(memberManage.getWorkType())) {
                auditInfo.setAuditorId(whzjh);
            } else {
                auditInfo.setAuditorId(wjzjh);
            }
            auditInfoDao.insertSelective(auditInfo);

            // 操作日志
            String userId = userInfo.getId();
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(memberManage.getMemberName()+"新增提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(auditInfo.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);


            //消息站内通知
            String username = userInfo.getUsername();
            String projectName = baseProject.getProjectName();
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
            //审核人名字
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A18");
            messageVo.setUserId(auditInfo.getAuditorId());
            messageVo.setTitle("您有一个跟踪审计项目待审核！");
            messageVo.setType("1"); // 通知
            messageVo.setDetails(memberManage1.getMemberName() + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批!");
            //调用消息Service
            messageService.sendOrClose(messageVo);

//            Exadep_adminmple example3 = new Example(MemberManage.class);
//            example3.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
//            MemberManage memberManage = memberManageDao.selectOneByExample(example3);

            // 上传文件，新建-申请信息，列表文件
            List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(trackVo.getKey(), trackVo.getType());

            for (FileInfo fileInfo : byFreignAndType) {
                fileInfo.setPlatCode(trackVo.getAuditInfo().getId());

                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
        }


        trackVo.getTrackApplicationInfo().setId(UUID.randomUUID().toString().replace("-", ""));
        trackVo.getTrackApplicationInfo().setTrackAudit(trackVo.getAuditInfo().getId());
        trackVo.getTrackApplicationInfo().setState("0");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trackVo.getTrackApplicationInfo().setCreateTime(simpleDateFormat.format(new Date()));

        trackApplicationInfoDao.insertSelective(trackVo.getTrackApplicationInfo());

//        List<TrackMonthly> monthlyList = trackVo.getMonthlyList();
//        for (TrackMonthly trackMonthly : monthlyList) {
//            if (trackVo.getStatus().equals("0")) {
//                trackMonthly.setId(UUID.randomUUID().toString().replace("-", ""));
//                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
//                trackMonthlyDao.insert(trackMonthly);
//            } else if (trackVo.getStatus().equals("1")) {
//                trackMonthly.setId(UUID.randomUUID().toString().replace("-", ""));
//                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
//                trackMonthlyDao.insert(trackMonthly);
//
//                //存入审核表
//                AuditInfo auditInfo = new AuditInfo();
//                auditInfo.setId(UUID.randomUUID().toString().replace("", "-"));
//                auditInfo.setBaseProjectId(trackMonthly.getId());
//                auditInfo.setAuditResult("0");
//                auditInfo.setAuditType("0");
//                Example example1 = new Example(MemberManage.class);
//                example1.createCriteria().andEqualTo("member_role_id", "3");
//                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                auditInfo.setAuditorId(memberManage.getId());
//                auditInfoDao.insertSelective(auditInfo);
//            }
//        }

    }

    @Override
    public TrackVo selectTrackById(String id, UserInfo userInfo) {
        String userId = userInfo.getId();
//        String userId = "user324";
        TrackVo trackVo = new TrackVo();
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        // 设计单位名称
        DesignUnitManagement designName = designUnitManagementDao.selectByPrimaryKey(trackAuditInfo.getDesignOrganizationId());
        if (designName != null){
            trackAuditInfo.setDesignOrganizationId(designName.getDesignUnitName());
        }
        // 造价单位名称
        CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(trackAuditInfo.getAuditUnitNameId());
        if (costUnitManagement != null){
            trackAuditInfo.setAuditUnitNameId(costUnitManagement.getCostUnitName());
        }
        BaseProject baseProject = baseProjectDao.findTrackBaseProjectId(trackAuditInfo.getBaseProjectId());

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit", id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);

        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");
        //根据创建时间排序
//        example1.orderBy("createTime").desc();
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        AuditInfo auditInfo = null;
        trackVo.setAuditWord("第" + trackMonthlies.size() + "次月报");
        TrackMonthly trackMonthlyOld = trackMonthlyDao.selectOne1(id);
        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", trackMonthlyOld.getId())
                .andEqualTo("status", "0")
                .andEqualTo("auditorId", userId)
                .andEqualTo("auditResult", "0");
        auditInfo = auditInfoDao.selectOneByExample(example2);

        if (auditInfo == null) {
            trackVo.setAuditResult("0"); //不审核
        } else {
            trackVo.setAuditResult("1");
        }
        trackVo.setTrackStatus(baseProject.getTrackStatus());
        trackVo.setBaseProject(baseProject);
        trackVo.setAuditInfo(trackAuditInfo);
        trackVo.setTrackApplicationInfo(trackApplicationInfo);
        trackVo.setMonthlyList(trackMonthlies);
        return trackVo;
    }

    @Override
    public TrackVo editTrackById(String id, UserInfo userInfo) {
        String userId = userInfo.getId();
//        String userId = "user324";
        TrackVo trackVo = new TrackVo();
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        BigDecimal outsourceMoney = trackAuditInfo.getOutsourceMoney();
        String money = outsourceMoney.toString();
        if ("0.00".equals(money)){
            trackAuditInfo.setOutsourceMoney(null);
        }else {
            trackAuditInfo.setOutsourceMoney(trackAuditInfo.getOutsourceMoney());
        }
        BaseProject baseProject = baseProjectDao.findTrackBaseProjectId(trackAuditInfo.getBaseProjectId());

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit", id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);

        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");
        //根据创建时间排序
//        example1.orderBy("createTime").desc();
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        AuditInfo auditInfo = null;
        trackVo.setAuditWord("第" + trackMonthlies.size() + "次月报");
        TrackMonthly trackMonthlyOld = trackMonthlyDao.selectOne1(id);
        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", trackMonthlyOld.getId())
                .andEqualTo("status", "0")
                .andEqualTo("auditorId", userId)
                .andEqualTo("auditResult", "0");
        auditInfo = auditInfoDao.selectOneByExample(example2);

        if (auditInfo == null) {
            trackVo.setAuditResult("0"); //不审核
        } else {
            trackVo.setAuditResult("1");
        }
        trackVo.setTrackStatus(baseProject.getTrackStatus());
        trackVo.setBaseProject(baseProject);
        trackVo.setAuditInfo(trackAuditInfo);
        trackVo.setTrackApplicationInfo(trackApplicationInfo);
        trackVo.setMonthlyList(trackMonthlies);
        return trackVo;
    }
    // TODO 回显页面，月报
    public List<TrackMonthly> findAllByTrackId(String id) {
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");
        //根据创建时间排序
        example1.orderBy("createTime").desc();
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        for (int i = 0; i < trackMonthlies.size(); i++) {
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(trackMonthlies.get(i).getWritter());
            if (memberManage != null){
                trackMonthlies.get(i).setWritter(memberManage.getMemberName());
            }
            trackMonthlies.get(i).setAuditCount("第"+(i+1)+"次月报");
        }
        return trackMonthlies;
    }

    public List<TrackMonthly> findAllByTrackId3(String id, UserInfo userInfo) {
        ArrayList<TrackMonthly> trackMonthlies1 = new ArrayList<>();
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");
        //根据创建时间排序
        example1.orderBy("createTime").desc();
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        trackMonthlies1.addAll(trackMonthlies);

        Example example2 = new Example(TrackMonthly.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("trackId", userInfo.getId());
        criteria2.andEqualTo("status", "0");
        //根据创建时间排序
        example1.orderBy("createTime").desc();
        List<TrackMonthly> trackMonthlies2 = trackMonthlyDao.selectByExample(example2);
        trackMonthlies1.addAll(trackMonthlies2);
        for (int i = 0; i < trackMonthlies2.size(); i++) {
            trackMonthlies.get(i).setAuditCount("第"+trackMonthlies.get(i).getAuditCount()+"次月报");
        }
        return trackMonthlies1;
    }

    //新增删除月报文件
    @Override
    public void deleteMonthly(String id) {
        //删除月报
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", id)  //登录人id
                .andEqualTo("status", "0");
        List<TrackMonthly> trackMonthly = trackMonthlyDao.selectByExample(example1);
        if (trackMonthly.size() > 0){
            for (TrackMonthly monthly : trackMonthly) {
                monthly.setStatus("1");
                trackMonthlyDao.updateByPrimaryKeySelective(monthly);
            }
        }
        //删除文件
        Example example = new Example(FileInfo.class);
        example.createCriteria().andEqualTo("userId",id)
                                .andEqualTo("status","0")
                                .andEqualTo("type","gzsjxjsqxx");
        List<FileInfo> fileInfo = fileInfoMapper.selectByExample(example);
        if (fileInfo.size() > 0){
            for (FileInfo info : fileInfo) {
                info.setStatus("1");
                fileInfoMapper.updateByPrimaryKeySelective(info);
            }
        }
    }


    @Override
    public void addAttribution(String baseId, String district, String designCategory, String prePeople) {
        TrackAuditInfo trackAuditInfo1 = trackAuditInfoDao.selectByPrimaryKey(baseId);
        String baseProjectId = trackAuditInfo1.getBaseProjectId();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
        baseProject.setDistrict(district);
        baseProject.setDesignCategory(designCategory);
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());


        Example example1 = new Example(Budgeting.class);
        example1.createCriteria().andEqualTo("baseProjectId",baseProject.getId()).andEqualTo("delFlag","0");
        Example example4 = new Example(TrackAuditInfo.class);
        example4.createCriteria().andEqualTo("baseProjectId",baseProject.getId()).andEqualTo("status","0");
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectOneByExample(example4);
        if (trackAuditInfo != null){
            EmployeeAchievementsInfo achievementsInfo = new EmployeeAchievementsInfo();
            BigDecimal trackAuditBase = trackAuditInfo.getTrackAuditBase();
            double track = trackAuditBase.doubleValue();
            //计价基数
            //计提和
            BigDecimal total5= new BigDecimal(0);
            if(!"4".equals(baseProject.getDistrict())){
                Double aDouble = projectSumService.trackImprovement(track);
                aDouble = (double)Math.round(aDouble*100)/100;
                total5 = total5.add(new BigDecimal(aDouble));
                //实际计提
                BigDecimal actualAmount = total5.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);;
                //余额
                BigDecimal balance = total5.subtract(actualAmount);
                // 员工绩效
                Example example = new Example(TrackMonthly.class);
                example.createCriteria().andEqualTo("trackId",trackAuditInfo.getId());
                List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example);
                for (TrackMonthly thisMony : trackMonthlies) {
                    achievementsInfo.setMemberId(thisMony.getWritter());
                }
                achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                achievementsInfo.setCreateTime(format);
                achievementsInfo.setUpdateTime(format);
                achievementsInfo.setFounderId(trackAuditInfo.getFounderId());
                achievementsInfo.setFounderCompanyId(trackAuditInfo.getCompanyId());
                achievementsInfo.setDelFlag("0");
                achievementsInfo.setDistrict(baseProject.getDistrict());
                achievementsInfo.setDept("2"); //造价
                achievementsInfo.setAccruedAmount(total5);
                achievementsInfo.setActualAmount(actualAmount);
                achievementsInfo.setBalance(balance);
                achievementsInfo.setAchievementsType("5"); //跟踪审计
                achievementsInfo.setBaseProjectId(baseProject.getId());
                achievementsInfo.setProjectNum(trackAuditInfo.getId());
                achievementsInfo.setOverFlag("0");
                employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
            }else{
//                            Double money = projectSumService.wujiangTrackChargeBaseRate(amountCost);
                Double aDouble = projectSumService.trackImprovement(track);
                aDouble = (double)Math.round(aDouble*100)/100;
                total5 = total5.add(new BigDecimal(aDouble));
                Example example = new Example(TrackMonthly.class);
                example.createCriteria().andEqualTo("trackId",trackAuditInfo.getId());
                List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example);

                //实际计提 2位 四舍五入
                BigDecimal actualAmount = total5.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);;
                //余额
                BigDecimal balance = total5.subtract(actualAmount);
                // 员工绩效
                for (TrackMonthly thisMony : trackMonthlies) {
                    achievementsInfo.setMemberId(thisMony.getWritter());
                }
                achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                achievementsInfo.setCreateTime(format);
                achievementsInfo.setUpdateTime(format);
                achievementsInfo.setFounderId(trackAuditInfo.getFounderId());
                achievementsInfo.setFounderCompanyId(trackAuditInfo.getCompanyId());
                achievementsInfo.setDelFlag("0");
                achievementsInfo.setDistrict(baseProject.getDistrict());
                achievementsInfo.setDept("2"); //造价
                achievementsInfo.setAccruedAmount(total5);
                achievementsInfo.setActualAmount(actualAmount);
                achievementsInfo.setBalance(balance);
                achievementsInfo.setAchievementsType("5"); //跟踪审计
                achievementsInfo.setBaseProjectId(baseProject.getId());
                achievementsInfo.setProjectNum(trackAuditInfo.getId());
                achievementsInfo.setOverFlag("0");
                employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
            }
        }

    }

    @Override
    public TrackMonthly findWritter(String id) {
        String idByName = memberManageDao.findIdByName(id);
        TrackMonthly trackMonthly = new TrackMonthly();
        trackMonthly.setWritter(idByName);
        return trackMonthly;
    }

    @Override
    public void editOutMoney(String id, String outMoney) {
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        if (trackAuditInfo != null){
            trackAuditInfo.setOutsourceMoney(new BigDecimal(outMoney));
            trackAuditInfoDao.updateByPrimaryKeySelective(trackAuditInfo);
        }
    }

    @Override
    public void accomplish(String ids, UserInfo userInfo) {
//        userInfo = new UserInfo("200101005",null,null,false);
        String id = userInfo.getId();
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String[] split = ids.split(",");
        for (String tid : split) {
            Example example = new Example(TrackMonthly.class);
            example.createCriteria().andEqualTo("trackId",tid)
                                    .andEqualTo("status","0");
            TrackMonthly trackMonthly = trackMonthlyDao.selectOneByExample(example);
            Example example1 = new Example(AuditInfo.class);
            example1.createCriteria().andEqualTo("baseProjectId",trackMonthly.getId())
                                     .andEqualTo("auditType","0")
                                     .andEqualTo("status","0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
            MemberManage creater = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
            TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(tid);
            if (auditInfo != null){
                if (! auditInfo.getAuditorId().equals(id)){
                    throw new RuntimeException("此操作只能由所选项目部门领导来完成");
                }else {

                    AuditInfo newAuditInfo = new AuditInfo();
                    newAuditInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    newAuditInfo.setBaseProjectId(trackMonthly.getId());
                    newAuditInfo.setAuditType("4");
                    //审核结果 结果待审核
                    newAuditInfo.setAuditResult("0");
                    //根据项目创建人地区判断
                    if ("1".equals(creater.getWorkType())) {
                        newAuditInfo.setAuditorId(whzjm);
                    } else {
                        newAuditInfo.setAuditorId(wjzjm);
                    }
                    newAuditInfo.setCreateTime(data);
                    newAuditInfo.setFounderId(id);
                    //状态正常
                    newAuditInfo.setStatus("0");
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
                    baseProject.setTrackStatus("1");

                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    //将新的领导信息添加到审核表中
                    auditInfoDao.insert(newAuditInfo);
                }
            }

        }
    }

    @Override
    public void sendBack(String id, String opinion) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
        Example example = new Example(TrackMonthly.class);
        example.createCriteria().andEqualTo("trackId",trackAuditInfo.getId())
                                .andEqualTo("status","0");
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example);
        if (trackMonthlies.size()>0){
            for (TrackMonthly trackMonthly : trackMonthlies) {
                Example example1 = new Example(AuditInfo.class);
                example1.createCriteria().andEqualTo("baseProjectId",trackMonthly.getId())
                                         .andEqualTo("auditType","0")
                                         .andEqualTo("status","0");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(opinion);
                auditInfo.setAuditTime("");
                auditInfo.setUpdateTime(data);
                baseProject.setTrackStatus("7");
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
            }
        }
    }

    // TODO 回显页面，新增页面月报显示
    public List<TrackMonthly> findAllByTrackId2(String id) {
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();

        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");

        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        return trackMonthlies;
    }

    // todo 查看页面，审核信息
    public List<AuditInfoVo> findAllAuditInfosByTrackId(String id) {
//        Example example2 = new Example(AuditInfo.class);
//        example2.createCriteria().andEqualTo("baseProjectId",id);
//        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId", id);
        example1.orderBy("createTime").desc(); // 时间排序
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        List<AuditInfoVo> allAuditInfosByTrackId = new ArrayList<>();
        for (TrackMonthly trackMonthly : trackMonthlies) {
            List<AuditInfoVo> allAuditInfosByTrackId2 = trackAuditInfoDao.findAllAuditInfosByTrackId(trackMonthly.getId());
            for (AuditInfoVo auditInfoVo : allAuditInfosByTrackId2) {
                auditInfoVo.setAuditWord("第" + trackMonthly.getAuditCount() + "次月报");
                if ("未审批".equals(auditInfoVo.getAuditResult())){
                    auditInfoVo.setAuditOpinion("-");
                    auditInfoVo.setAuditTime("-");
                }

            }
            allAuditInfosByTrackId.addAll(allAuditInfosByTrackId2);
        }

        return allAuditInfosByTrackId;
    }

    public void addTrackMonthly(TrackMonthly trackMonthly) {
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();
        criteria.andEqualTo("trackId", trackMonthly.getCode());
        criteria.andEqualTo("status", "0");
        List<TrackMonthly> trackMonthly1 = trackMonthlyDao.selectByExample(example1);
        trackMonthly.setAuditCount((trackMonthly1.size()+1)+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trackMonthly.setId(UUID.randomUUID().toString().replace("-", ""));
        trackMonthly.setCreateTime(simpleDateFormat.format(new Date()));
        trackMonthly.setTrackId(trackMonthly.getCode());
        trackMonthly.setFounderId(trackMonthly.getCode());
        trackMonthly.setTrackId(trackMonthly.getTrackId());
        trackMonthly.setStatus("0");
        String nameById = memberManageDao.findNameById(trackMonthly.getWritter());
        trackMonthly.setWritter(nameById);
        trackMonthlyDao.insertSelective(trackMonthly);
    }

    @Override
    public void updateMonthly(TrackMonthly trackMonthly) {
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void updateTrack(TrackVo trackVo, UserInfo userInfo,HttpServletRequest request) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String userInfoId = userInfo.getId();
//        String userInfoId = "user320";


        //如果点击得是保存
        if (trackVo.getStatus().equals("0")) {
            trackVo.getAuditInfo().setId(trackVo.getAuditInfo().getId());
            trackVo.getAuditInfo().setUpdateTime(simpleDateFormat.format(new Date()));
            // 如果前台传一个null或者空字符串，那么bigDecimal类型就修改不了
            if (trackVo.getAuditInfo().getOutsourceMoney() == null && "".equals(trackVo.getAuditInfo().getOutsourceMoney())){
                trackVo.getAuditInfo().setOutsourceMoney(new BigDecimal(0));
            }
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());
            // 操作日志
            String userId = userInfo.getId();
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackVo.getAuditInfo().getBaseProjectId());
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(memberManage.getMemberName()+"修改保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(trackVo.getAuditInfo().getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        } else if (trackVo.getStatus().equals("1")) {
            //如果是提交 将数据覆盖
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());

            String baseProjectId = trackVo.getAuditInfo().getBaseProjectId();
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
            baseProject.setTrackStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);

            Example example = new Example(AuditInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", trackVo.getAuditInfo().getId());
            c.andEqualTo("auditResult", "2"); //未通过
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

            //月报
            Example exa = new Example(TrackMonthly.class);
            Example.Criteria newMonthly = exa.createCriteria();
            newMonthly.andEqualTo("trackId", trackVo.getAuditInfo().getId());
            newMonthly.andEqualTo("status", "0");
            List<TrackMonthly> trackMonthlyNew = trackMonthlyDao.selectByExample(exa);
            boolean booleans = true;
            for (TrackMonthly trackMonthly : trackMonthlyNew) {
                Example newAudid = new Example(AuditInfo.class);
                Example.Criteria newAudidc = newAudid.createCriteria();
                newAudidc.andEqualTo("baseProjectId", trackMonthly.getId());
                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(newAudid);
                if (auditInfos.size() > 0) {
                    booleans = false;
                }
            }
            // 如果是退回未通过
            if ("7".equals(trackVo.getTrackStatus())){
                // 把所有的审核信息删除
                if (trackMonthlyNew.size()>0){
                    for (TrackMonthly thisMonthly : trackMonthlyNew) {
                        Example example1 = new Example(AuditInfo.class);
                        example1.createCriteria().andEqualTo("baseProjectId",thisMonthly.getId());
                        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                        if (auditInfos.size()>0){
                            auditInfoDao.deleteByPrimaryKey(auditInfos);
                        }
                    }
                }
                AuditInfo auditInfo1 = new AuditInfo();
                auditInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                auditInfo1.setAuditResult("0");
                auditInfo1.setStatus("0");
                auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userInfoId);
                //判断当前项目创建人属于哪个地区 根据地区提交给相应领导
                //如果为芜湖
                if ("1".equals(memberManage.getWorkType())) {
                    auditInfo1.setAuditorId(whzjh);
                } else {
                    auditInfo1.setAuditorId(wjzjh);
                }
                auditInfoDao.insertSelective(auditInfo1);
            } else if (auditInfo == null) { //未提交 进行中
                if (booleans) { //未提交
                    //存入审核表
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(userInfoId);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(trackMonthlyNew.get(0).getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("0");
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                    //判断当前项目创建人属于哪个地区 根据地区提交给相应领导
                    //如果为芜湖
                    if ("1".equals(memberManage.getWorkType())) {
                        auditInfo1.setAuditorId(whzjh);
                    } else {
                        auditInfo1.setAuditorId(wjzjh);
                    }
                    auditInfoDao.insertSelective(auditInfo1);

                } else { //进行中
                    TrackMonthly trackMonthlyOld = trackMonthlyDao.selectOne1(trackVo.getAuditInfo().getId());
                    trackMonthlyOld.setAuditCount(trackMonthlyNew.size() + "");
                    trackMonthlyDao.updateByPrimaryKeySelective(trackMonthlyOld);

                    //存入审核表
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(userInfoId);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(trackMonthlyOld.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("0");
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                    //判断当前项目创建人属于哪个地区 根据地区提交给相应领导
                    //如果为芜湖
                    if ("1".equals(memberManage.getWorkType())) {
                        auditInfo1.setAuditorId(whzjh);
                    } else {
                        auditInfo1.setAuditorId(wjzjh);
                    }
                    auditInfoDao.insertSelective(auditInfo1);

                    //消息站内通知
                    String username = userInfo.getUsername();
                    String projectName = baseProject.getProjectName();
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditInfo1.getAuditorId());
                    //审核人名字
                    MessageVo messageVo = new MessageVo();
                    messageVo.setId("A19");
                    messageVo.setType("1"); // 通知
                    messageVo.setUserId(auditInfo1.getAuditorId());
                    messageVo.setTitle("您有一个跟踪审计项目待审核！");
                    messageVo.setDetails(memberManage1.getMemberName() + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批!");
                    //调用消息Service
                    messageService.sendOrClose(messageVo);
                }
            } else {
                //未通过
                //todo 更新月报信息
                TrackMonthly trackMonthly = trackMonthlyDao.selectByPrimaryKey(auditInfo.getBaseProjectId());
                trackMonthly.setStatus("1");
                trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);

                TrackMonthly trackMonthlyOld = trackMonthlyDao.selectOne1(trackVo.getAuditInfo().getId());
                trackMonthlyOld.setTrackId(trackVo.getAuditInfo().getId());
                trackMonthlyOld.setAuditCount(trackMonthlyNew.size() + 1 + "");
                trackMonthlyDao.updateByPrimaryKeySelective(trackMonthlyOld);

                auditInfo.setAuditResult("0");
                auditInfo.setAuditOpinion("");
                auditInfo.setAuditTime("");
                //修改基本状态
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            }
//            AuditInfo auditInfo2 = new AuditInfo();
//            auditInfo2.setId(UUID.randomUUID().toString().replace("-", ""));
//            auditInfo2.setBaseProjectId(trackVo.getAuditInfo().getId());
//            auditInfo2.setAuditResult("0");
//            auditInfo2.setAuditType("0");
//            Example example2 = new Example(MemberManage.class);
//            Example.Criteria criteria = example2.createCriteria();
//            criteria.andEqualTo("depId", "2");
//            criteria.andEqualTo("depAdmin", "1");
//            MemberManage memberManage1 = memberManageDao.selectOneByExample(example2);
//            auditInfo2.setAuditorId(memberManage1.getId());
//            auditInfo2.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            auditInfoDao.insertSelective(auditInfo2);


//            Example example = new Example(AuditInfo.class);
//            Example.Criteria c = example.createCriteria();
//            for (TrackMonthly trackMonthly : trackVo.getMonthlyList()) {
//                c.andEqualTo("baseProjectId", trackMonthly.getId());
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//                if (auditInfo == null) {
//                    AuditInfo auditInfo1 = new AuditInfo();
//                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
//                    auditInfo1.setBaseProjectId(trackMonthly.getId());
//                    auditInfo1.setAuditResult("0");
//                    auditInfo1.setAuditType("0");
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("member_role_id", "3");
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                    auditInfo1.setAuditorId(memberManage.getId());
//                    auditInfo1.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                    auditInfoDao.insertSelective(auditInfo1);
//                }
//
//            }
            // 操作日志
            String userId = userInfo.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("8"); //跟踪审计项目
            operationLog.setContent(memberManage.getMemberName()+"修改提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(trackVo.getAuditInfo().getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }

        trackApplicationInfoDao.updateByPrimaryKeySelective(trackVo.getTrackApplicationInfo());
    }
}

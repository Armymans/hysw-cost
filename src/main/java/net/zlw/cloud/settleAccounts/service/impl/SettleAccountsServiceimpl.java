package net.zlw.cloud.settleAccounts.service.impl;


import com.alibaba.fastjson.JSONObject;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.mapper.EmployeeAchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.InComeMapper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.OutSourceMapper;
import net.zlw.cloud.designProject.model.EmployeeAchievementsInfo;
import net.zlw.cloud.designProject.model.InCome;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.OutSource;
import net.zlw.cloud.designProject.service.ProjectSumService;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.settleAccounts.mapper.*;
import net.zlw.cloud.settleAccounts.model.*;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.DetailsVo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import net.zlw.cloud.warningDetails.service.WarningDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SettleAccountsServiceimpl implements SettleAccountsService {
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private EmployeeAchievementsInfoMapper employeeAchievementsInfoMapper;
    @Resource
    private ProjectSumService projectSumService;
    @Resource
    private OtherInfoMapper otherInfoMapper;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private MemberManageDao memberManageDao;
    @Resource
    private SettlementInfoMapper settlementInfoMapper;
    @Resource
    private SettlementInfoMapper settlementInfoMapper2;
    @Resource
    private FileInfoMapper fileInfoMapper;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private OperationLogDao operationLogDao;

    @Autowired
    private TrackMonthlyDao trackMonthlyDao;
    @Autowired
    private OutSourceMapper outSourceMapper;
    @Autowired
    private InComeMapper inComeMapper;
    @Autowired
    private BudgetingDao budgetingDao;

    @Autowired
    private RemindSetMapper remindSetMapper;
    @Autowired
    private MessageService messageService;
    @Resource
    private MkyUserMapper mkyUserMapper;
    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;
    @Resource
    private WarningDetailsService warningDetailsService;
    @Resource
    private BudgetCoverService budgetCoverService;



    @Value("${audit.wujiang.sheji.designHead}")
    private String wjsjh;
    @Value("${audit.wujiang.sheji.designManager}")
    private String wjsjm;
    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh;
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm;

    @Value("${audit.wuhu.sheji.designHead}")
    private String whsjh;
    @Value("${audit.wuhu.sheji.designManager}")
    private String whsjm;
    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh;
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm;


    @Override
    public List<AccountsVo> findAllAccounts(PageVo pageVo, UserInfo loginUser) {
//        loginUser = new UserInfo("200101005",null,null,true);
//        loginUser = new UserInfo("user309", null, null, true);
        pageVo.setUserId(loginUser.getId());
//        List<AccountsVo> list = baseProjectDao.findAllAccounts(pageVo);
//        ArrayList<AccountsVo> returnList = new ArrayList<>();
        //待审核
        if (pageVo.getSettleAccountsStatus().equals("1")) {
            //待审核 领导
            if (whzjh.equals(loginUser.getId()) || whzjm.equals(loginUser.getId()) || wjzjh.equals(loginUser.getId())) {
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsCheckLeader(pageVo);
                for (AccountsVo accountsVo : list1) {
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId", accountsVo.getAccountId())
                            .andEqualTo("auditResult", "0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo != null) {
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id", auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if (memberManage != null) {
                            accountsVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                    if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType()) ){
                        accountsVo.setSettleAccountsStatus("结算审核");
                    }else {
                        accountsVo.setSettleAccountsStatus("结算确认审核");
                    }
                }
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                     if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                }

                return list1;
            }
            //待审核 普通员工
            else {
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsCheckStaff(pageVo);
                for (AccountsVo accountsVo : list1) {
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId", accountsVo.getAccountId())
                            .andEqualTo("auditResult", "0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo != null) {
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id", auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if (memberManage != null) {
                            accountsVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                    if (auditInfo!=null){
                        if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType()) ){
                            accountsVo.setSettleAccountsStatus("结算审核");
                        }else {
                            accountsVo.setSettleAccountsStatus("结算确认审核");
                        }
                    }
                }
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                     if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                }

                return list1;
            }
        }
            //处理中
            if (pageVo.getSettleAccountsStatus().equals("2")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsProcessing(pageVo);
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                     if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                     if (accountsVo.getAuthorizedNumber()!=null && accountsVo.getLReviewNumber()!=null){
                         accountsVo.setSettleAccountsStatus("-");
                     }else if(accountsVo.getAuthorizedNumber()!=null){
                         accountsVo.setSettleAccountsStatus("下家处理中");
                     }else if(accountsVo.getLReviewNumber()!=null){
                         accountsVo.setSettleAccountsStatus("上家处理中");
                     }

                }

                return list1;
            }
            //未通过
            if (pageVo.getSettleAccountsStatus().equals("3")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsProcessing(pageVo);
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                    if (mkyUser!=null){
                         if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                    }

                    Example example = new Example(AuditInfo.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("auditResult","2");
                    c.andEqualTo("baseProjectId",accountsVo.getAccountId());
                    c.andEqualTo("status","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType()) ){
                        accountsVo.setSettleAccountsStatus("结算未通过");
                    }else {
                        accountsVo.setSettleAccountsStatus("结算确认未通过");
                    }
                }

                return list1;
            }
            //待确认
            if (pageVo.getSettleAccountsStatus().equals("4")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsSuccess2(pageVo);
                for (AccountsVo accountsVo : list1) {
                    if (loginUser.getId().equals(accountsVo.getFounderId())){
                        accountsVo.setShowConfirmed("1");
                    }else{
                        accountsVo.setShowConfirmed("2");
                    }
                }
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                    if (mkyUser!=null){
                         if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                    }
                }

                return list1;
            }
            //已完成
            if (pageVo.getSettleAccountsStatus().equals("5")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsSuccess(pageVo);
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                    if (mkyUser!=null){
                         if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                        }
                    }
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(accountsVo.getId());
                    String designCategory = baseProject.getDesignCategory();
                    String district = baseProject.getDistrict();
                    if (designCategory == null || "".equals(designCategory)){
                        accountsVo.setAttributionShow("0");
                    }
                    if (district == null || "".equals(district)){
                        accountsVo.setAttributionShow("0");
                    }
                    if (preparePeople == null || "".equals(preparePeople)){
                        accountsVo.setAttributionShow("0");
                    }


                }

                return list1;
            }
            //全部
            if (pageVo.getSettleAccountsStatus().equals("") || pageVo.getSettleAccountsStatus().equals("0")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsProcessing(pageVo);
                for (AccountsVo accountsVo : list1) {
                    if (loginUser.getId().equals(accountsVo.getFounderId())){
                        accountsVo.setShowConfirmed("1");
                    }else{
                        accountsVo.setShowConfirmed("2");
                    }



                }
                for (AccountsVo accountsVo : list1) {
                    String preparePeople = accountsVo.getPreparePeople();
                    MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
                     if (mkyUser!=null){
                        accountsVo.setPreparePeople(mkyUser.getUserName());
                    }
                }

                return list1;
            }


//        for (AccountsVo accountsVo : list) {
//            //待审核
//            if (pageVo.getSettleAccountsStatus().equals("1")){
//                Example example = new Example(AuditInfo.class);
//                example.createCriteria().andEqualTo("baseProjectId",accountsVo.getAccountId())
//                        .andEqualTo("auditResult","0");
//                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
//                if (auditInfo!=null){
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                    if (memberManage !=null){
//                        accountsVo.setCurrentHandler(memberManage.getMemberName());
//                    }
//                }
//
//                if (loginUser.getId().equals(accountsVo.getFounderId()) || whzjh.equals(loginUser.getId()) || whzjm.equals(loginUser.getId()) || wjzjh.equals(loginUser.getId()) || loginUser.getId().equals(auditInfo.getAuditorId())){
//                    returnList.add(accountsVo);
//                }
//            }
//            //处理中
//            if (pageVo.getSettleAccountsStatus().equals("2")){
//                if (loginUser.getId().equals(accountsVo.getFounderId())){
//                    returnList.add(accountsVo);
//                }
//            }
//            //未通过
//            if (pageVo.getSettleAccountsStatus().equals("3")){
//                if (loginUser.getId().equals(accountsVo.getFounderId())){
//                    returnList.add(accountsVo);
//                }
//            }
//            //待确认
//            if (pageVo.getSettleAccountsStatus().equals("4")){
//                returnList.add(accountsVo);
//            }
//            //已完成
//            if (pageVo.getSettleAccountsStatus().equals("5")){
//                returnList.add(accountsVo);
//            }
//            //全部
//            if (pageVo.getSettleAccountsStatus().equals("") || pageVo.getSettleAccountsStatus().equals("0")){
//                if (loginUser.getId().equals(accountsVo.getFounderId())){
//                    returnList.add(accountsVo);
//                }
//            }
//        }
//        ArrayList<AccountsVo> accountsVos = new ArrayList<>();
//        for (AccountsVo accountsVo : returnList) {
//            if (! accountsVos.contains(accountsVo)){
//                accountsVos.add(accountsVo);
//            }
//        }
        return new ArrayList<AccountsVo>();
    }

    @Override
    public void deleteAcmcounts(String id,UserInfo loginUser,HttpServletRequest request) {

        Example example = new Example(InvestigationOfTheAmount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId", id);
        criteria.andEqualTo("delFlag", "0");
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);
        investigationOfTheAmount.setDelFlag("1");
        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);

        Example example1 = new Example(LastSettlementReview.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseProjectId", id);
        criteria1.andEqualTo("delFlag", "0");
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example1);
        if (lastSettlementReview != null) {
            lastSettlementReview.setDelFlag("1");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
            // 操作日志
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(lastSettlementReview.getBaseProjectId());
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(memberManage.getMemberName()+"删除了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(lastSettlementReview.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);

        }
        Example example2 = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("baseProjectId", id);
        criteria2.andEqualTo("delFlag", "0");
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example2);
        if (settlementAuditInformation != null) {
            settlementAuditInformation.setDelFlag("1");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
            // 操作日志
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(settlementAuditInformation.getBaseProjectId());
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(memberManage.getMemberName()+"删除了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(settlementAuditInformation.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }

    }

    @Override
    public void updateAccount(String s, UserInfo loginUser, String checkWhether) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        BaseProject baseProject = new BaseProject();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
        String accountWhether = baseProject.getAccountWhether();
        String[] split = null;
        String[] split1 = null;
        if (accountWhether!=null){
           split = accountWhether.split(",");
        }
        if (checkWhether!=null){
            split1 = checkWhether.split(",");
        }
        if (split!=null){
            if (split1!=null){
                for (String s1 : split1) {
                    for (String s2 : split) {
                        if (s1.equals(s2)){
                            if (s1.equals("1")){
                                throw new RuntimeException("上家已到账,请勿重复操作");
                            }else if(s1.equals("2")){
                                throw new RuntimeException("下家已到账请勿重复操作");
                            }
                        }
                    }
                    String accountWhether1 = baseProject.getAccountWhether();
                    if (accountWhether1!=null){
                        baseProject.setAccountWhether(accountWhether+","+s1);
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    }else{
                        baseProject.setAccountWhether(accountWhether);
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    }
                }
            }
        }else{
            String a = "";
            for (String s1 : split1) {
                if (a.equals("")){
                    a=s1;
                }else{
                    a+=","+s1;
                }
            }
            baseProject.setAccountWhether(a);
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }
//        baseProject.setId(s);
        baseProject.setSaWhetherAccount("0");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
        //上家到账
        Example example = new Example(LastSettlementReview.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId", s);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview != null) {
            // 绩效计算
            EmployeeAchievementsInfo achievementsInfo = new EmployeeAchievementsInfo();
            //计提和
            BigDecimal total3 = new BigDecimal(0);
            // 如果是安徽
            if(!"4".equals(baseProject.getDistrict())){
                Double money = projectSumService.anhuiLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                //咨询费
                money = (double)Math.round(money*100)/100;
                //计提
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                total3 = total3.add(new BigDecimal(aDouble1));
                //实际计提 保留两位小数四舍五入
                BigDecimal actualAmount = total3.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                //余额
                BigDecimal balance = total3.subtract(actualAmount);
                achievementsInfo.setAccruedAmount(total3);
                achievementsInfo.setActualAmount(actualAmount);
                achievementsInfo.setBalance(balance);
                achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                achievementsInfo.setMemberId(lastSettlementReview.getPreparePeople()); //编制人
                achievementsInfo.setCreateTime(data);
                achievementsInfo.setUpdateTime(data);
                achievementsInfo.setFounderId(lastSettlementReview.getFounderId());
                achievementsInfo.setFounderCompanyId(lastSettlementReview.getFounderCompanyId());
                achievementsInfo.setDelFlag("0");
                achievementsInfo.setDistrict(baseProject.getDistrict());
                achievementsInfo.setDept("2"); // 造价
                achievementsInfo.setAchievementsType("3"); //上家送审绩效
                achievementsInfo.setBaseProjectId(baseProject.getId());
                achievementsInfo.setProjectNum(lastSettlementReview.getId());
                achievementsInfo.setOverFlag("0"); //是否发放完结 0否1是
                employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
                //如果是吴江
            }else{
                //咨询费
                Double money = projectSumService.wujiangLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                money = (double)Math.round(money*100)/100;
                //计提
                Double aDouble1 = projectSumService.technicalImprovement(money);
                aDouble1 = (double)Math.round(aDouble1*100)/100;
                total3 = total3.add(new BigDecimal(aDouble1));

                //实际计提 保留两位小数四舍五入
                BigDecimal actualAmount = total3.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                //余额
                BigDecimal balance = total3.subtract(actualAmount);
                achievementsInfo.setAccruedAmount(total3);
                achievementsInfo.setActualAmount(actualAmount);
                achievementsInfo.setBalance(balance);
                achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                achievementsInfo.setMemberId(lastSettlementReview.getPreparePeople()); //编制人
                achievementsInfo.setCreateTime(data);
                achievementsInfo.setUpdateTime(data);
                achievementsInfo.setFounderId(lastSettlementReview.getFounderId());
                achievementsInfo.setFounderCompanyId(lastSettlementReview.getFounderCompanyId());
                achievementsInfo.setDelFlag("0");
                achievementsInfo.setDistrict(baseProject.getDistrict());
                achievementsInfo.setDept("2"); // 造价
                achievementsInfo.setAchievementsType("3"); //上家送审绩效
                achievementsInfo.setBaseProjectId(baseProject.getId());
                achievementsInfo.setProjectNum(lastSettlementReview.getId());
                achievementsInfo.setOverFlag("0"); //是否发放完结 0否1是
                employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
            }
            lastSettlementReview.setWhetherAccount("0");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }
        //下家到账
        Example example1 = new Example(SettlementAuditInformation.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId", s);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation != null) {
            //计算计提
            //计提和
            BigDecimal total7 = new BigDecimal(0);
            Example example2 = new Example(SettlementInfo.class);
            //查找结算送审信息
            example2.createCriteria().andEqualTo("baseProjectId",baseProject.getId())
                                     .andEqualTo("upAndDown","2");
            SettlementInfo settlementInfo = settlementInfoMapper.selectOneByExample(example2);
            EmployeeAchievementsInfo achievementsInfo = new EmployeeAchievementsInfo();
            // 送审金额
            String sumbitMoney = settlementInfo.getSumbitMoney();
            if(!"4".equals(baseProject.getDistrict())){
                    //计算基本费
                    Double money = projectSumService.anhuiSettlementAuditInformationChargeBase(new BigDecimal(sumbitMoney));
                    money = (double)Math.round(money*100)/100;
                    //计算核检费
                    Double money1 = projectSumService.anhuiSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                    money1 = (double)Math.round(money1*100)/100;
                    //计算咨询费计算基数
                    Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                    //计提
                    Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                    aDouble = (double)Math.round(aDouble*100)/100;
                    total7 = total7.add(new BigDecimal(aDouble));
                    //实际计提 保留两位小数四舍五入
                    BigDecimal actualAmount = total7.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    //余额
                    BigDecimal balance = total7.subtract(actualAmount);
                    achievementsInfo.setAccruedAmount(total7);
                    achievementsInfo.setActualAmount(actualAmount);
                    achievementsInfo.setBalance(balance);
                    achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    achievementsInfo.setMemberId(settlementAuditInformation.getPreparePeople()); //编制人
                    achievementsInfo.setCreateTime(data);
                    achievementsInfo.setUpdateTime(data);
                    achievementsInfo.setFounderId(settlementAuditInformation.getFounderId());
                    achievementsInfo.setFounderCompanyId(settlementAuditInformation.getFounderCompanyId());
                    achievementsInfo.setDelFlag("0");
                    achievementsInfo.setDistrict(baseProject.getDistrict());
                    achievementsInfo.setDept("2"); // 造价
                    achievementsInfo.setAchievementsType("4"); //下家送审绩效
                    achievementsInfo.setBaseProjectId(baseProject.getId());
                    achievementsInfo.setProjectNum(settlementAuditInformation.getId());
                    achievementsInfo.setOverFlag("0"); //是否发放完结 0否1是
                    employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
                }else{
                    //计算基本费
                    Double money = projectSumService.wujiangSettlementAuditInformationChargeBase(new BigDecimal(sumbitMoney));
                    money = (double)Math.round(money*100)/100;
                    //计算核检费
                    Double money1 = projectSumService.wujiangSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                    money1 = (double)Math.round(money1*100)/100;
                    //计算咨询费计算基数
                    Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                    //计提
                    Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                    aDouble = (double)Math.round(aDouble*100)/100;
                    total7 = total7.add(new BigDecimal(aDouble));
                    //实际计提 保留两位小数四舍五入
                    BigDecimal actualAmount = total7.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    //余额
                    BigDecimal balance = total7.subtract(actualAmount);

                    achievementsInfo.setAccruedAmount(total7);
                    achievementsInfo.setActualAmount(actualAmount);
                    achievementsInfo.setBalance(balance);
                    achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    achievementsInfo.setMemberId(lastSettlementReview.getPreparePeople()); //编制人
                    achievementsInfo.setCreateTime(data);
                    achievementsInfo.setUpdateTime(data);
                    achievementsInfo.setFounderId(lastSettlementReview.getFounderId());
                    achievementsInfo.setFounderCompanyId(lastSettlementReview.getFounderCompanyId());
                    achievementsInfo.setDelFlag("0");
                    achievementsInfo.setDistrict(baseProject.getDistrict());
                    achievementsInfo.setDept("2"); // 造价
                    achievementsInfo.setAchievementsType("4"); //下家送审绩效
                    achievementsInfo.setBaseProjectId(baseProject.getId());
                    achievementsInfo.setProjectNum(lastSettlementReview.getId());
                    achievementsInfo.setOverFlag("0"); //是否发放完结 0否1是
                    employeeAchievementsInfoMapper.insertSelective(achievementsInfo);
                }
            settlementAuditInformation.setWhetherAccount("0");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void addAccount(BaseAccountsVo baseAccountsVo, UserInfo loginUser, HttpServletRequest request) {
//        loginUser = new UserInfo("user320",null,null,true);


        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //判断bigdecimal
        if ("".equals(baseAccountsVo.getSettlementInfo().getSumbitMoney())){
            baseAccountsVo.getSettlementInfo().setSumbitMoney("0");
        }


        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //其他信息表
        if (!"".equals(baseAccountsVo.getComs()) && baseAccountsVo.getComs() != null){
            Json coms = baseAccountsVo.getComs();
            String json = coms.value();
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            int num = 1;
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(baseProject.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(data);
                    otherInfo1.setStatus("0");

                    otherInfo1.setFoundId(loginUser.getId());
                    otherInfo1.setFounderCompany(loginUser.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    otherInfoMapper.insertSelective(otherInfo1);
                    num++;
                }
            }
        }
        //添加上家送审
        System.out.println(baseAccountsVo);
        if (baseAccountsVo.getLastSettlementInfo().getSumbitName() != null && ! baseAccountsVo.getLastSettlementInfo().getSumbitName().equals("")){
            baseAccountsVo.getLastSettlementInfo().setId(UUID.randomUUID().toString().replace("-",""));
            baseAccountsVo.getLastSettlementInfo().setBaseProjectId(baseProject.getId());
            baseAccountsVo.getLastSettlementInfo().setCreateTime(data);
            baseAccountsVo.getLastSettlementInfo().setState("0");
            baseAccountsVo.getLastSettlementInfo().setFouderId(loginUser.getId());
            baseAccountsVo.getLastSettlementInfo().setUpAndDown("1");
            settlementInfoMapper.insertSelective(baseAccountsVo.getLastSettlementInfo());
        }
        //添加勘察金额
        if (baseAccountsVo.getInvestigationOfTheAmount() != null){
            baseAccountsVo.getInvestigationOfTheAmount().setId(UUID.randomUUID().toString().replace("-",""));
            baseAccountsVo.getInvestigationOfTheAmount().setBaseProjectId(baseProject.getId());
            baseAccountsVo.getInvestigationOfTheAmount().setCreateTime(data);
            baseAccountsVo.getInvestigationOfTheAmount().setDelFlag("0");
            baseAccountsVo.getInvestigationOfTheAmount().setFounderId(loginUser.getId());
            investigationOfTheAmountDao.insertSelective(baseAccountsVo.getInvestigationOfTheAmount());
        }

        //添加下家送审
        if (baseAccountsVo.getSettlementInfo().getSumbitMoney() != null && ! "".equals(baseAccountsVo.getSettlementInfo().getSumbitMoney())){
            baseAccountsVo.getSettlementInfo().setId(UUID.randomUUID().toString().replace("-",""));
            baseAccountsVo.getSettlementInfo().setBaseProjectId(baseProject.getId());
            baseAccountsVo.getSettlementInfo().setCreateTime(data);
            baseAccountsVo.getSettlementInfo().setState("0");
            baseAccountsVo.getSettlementInfo().setFouderId(loginUser.getId());
            baseAccountsVo.getSettlementInfo().setUpAndDown("2");
            settlementInfoMapper2.insertSelective(baseAccountsVo.getSettlementInfo());
        }

        //添加上家结算送审
        if (baseAccountsVo.getLastSettlementReview().getReviewNumber()!=null){

            baseAccountsVo.getLastSettlementReview().setId(UUID.randomUUID().toString().replace("-",""));
            baseAccountsVo.getLastSettlementReview().setCreateTime(data);
            baseAccountsVo.getLastSettlementReview().setDelFlag("0");
            baseAccountsVo.getLastSettlementReview().setBaseProjectId(baseProject.getId());
            baseAccountsVo.getLastSettlementReview().setFounderId(loginUser.getId());
        }

        if (baseAccountsVo.getSettlementAuditInformation().getAuthorizedNumber()!=null){
            //添加下家结算送审
            baseAccountsVo.getSettlementAuditInformation().setId(UUID.randomUUID().toString().replace("-", ""));
            baseAccountsVo.getSettlementAuditInformation().setCreateTime(data);
            baseAccountsVo.getSettlementAuditInformation().setDelFlag("0");
            baseAccountsVo.getSettlementAuditInformation().setBaseProjectId(baseProject.getId());
            baseAccountsVo.getSettlementAuditInformation().setFounderId(loginUser.getId());

        }

        if (baseAccountsVo.getLastSettlementReview().getId() != null) {
            baseAccountsVo.getSettlementAuditInformation().setAccountId(baseAccountsVo.getLastSettlementReview().getId());
            baseAccountsVo.getSettlementAuditInformation().setWhetherAccount("1");
        }
        if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
            baseAccountsVo.getLastSettlementReview().setAccountId(baseAccountsVo.getSettlementAuditInformation().getId());
            baseAccountsVo.getLastSettlementReview().setWhetherAccount("1");
        }
        if (baseProject.getAB().equals("1") ){
            if ("2".equals(baseAccountsVo.getSettlementAuditInformation().getContract())){
                if(baseAccountsVo.getLastSettlementReview().getId() != null) {
                    if (baseAccountsVo.getLastSettlementReview().getPreparePeople().equals("")){
                        baseAccountsVo.getLastSettlementReview().setPreparePeople(loginUser.getId());
                    }
                    lastSettlementReviewDao.insertSelective(baseAccountsVo.getLastSettlementReview());
                }
                if(baseAccountsVo.getSettlementAuditInformation().getId() != null){
                    settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());
                }
            }else if("1".equals(baseAccountsVo.getSettlementAuditInformation().getContract())){
                settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());
            }

        }else if(baseProject.getAB().equals("2")){
            settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());
        }

        if (baseAccountsVo.getAuditId() != null) {
            //添加 一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            if (baseProject.getAB().equals("2") || baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
            } else {
                auditInfo.setBaseProjectId(baseAccountsVo.getLastSettlementReview().getId());
            }
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(baseAccountsVo.getAuditId());
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(data);
            auditInfoDao.insertSelective(auditInfo);
            baseProject.setSettleAccountsStatus("1");
            baseProject.setProjectFlow(baseProject.getProjectFlow()+",6");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(memberManage1.getMemberName()+"新增提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            if (baseAccountsVo.getSettlementAuditInformation() != null){
                operationLog.setDoObject(baseAccountsVo.getSettlementAuditInformation().getId()); // 项目标识
            } else if (baseAccountsVo.getLastSettlementReview() != null){
                operationLog.setDoObject(baseAccountsVo.getLastSettlementReview().getId());
            }else {
                operationLog.setDoObject(baseAccountsVo.getSettlementAuditInformation().getId()+","+baseAccountsVo.getLastSettlementReview().getId()); // 项目标识
            }
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        } else {
            baseProject.setSettleAccountsStatus("2");
            baseProject.setProjectFlow(baseProject.getProjectFlow()+",6");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(memberManage2.getMemberName()+"新增保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            if (baseAccountsVo.getSettlementAuditInformation() != null){
                operationLog.setDoObject(baseAccountsVo.getSettlementAuditInformation().getId()); // 项目标识
            } else if (baseAccountsVo.getLastSettlementReview() != null){
                operationLog.setDoObject(baseAccountsVo.getLastSettlementReview().getId());
            }else {
                operationLog.setDoObject(baseAccountsVo.getSettlementAuditInformation().getId()+","+baseAccountsVo.getLastSettlementReview().getId()); // 项目标识
            }
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }

        Example example = new Example(FileInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andLike("type", "jsxmxj%");
        c.andEqualTo("status", "0");
         c.andEqualTo("platCode", loginUser.getId());


        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
        for (FileInfo fileInfo : fileInfos) {
            //修改文件外键
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                fileInfoService.updateFileName2(fileInfo.getId(), baseProject.getId());
            } else {
                fileInfoService.updateFileName2(fileInfo.getId(), baseProject.getId());
            }
        }
        //消息通知
        if (baseAccountsVo.getAuditId() != null) {
            //根据上家结算送审外键找到关联预算的信息
            Example example1 = new Example(Budgeting.class);
            example1.createCriteria().andEqualTo("baseProjectId", baseProject.getId());
            Budgeting budgeting = budgetingDao.selectOneByExample(example1);
            //项目名称
            String projectName = baseProject.getProjectName();
            //审核id
            String auditId = baseAccountsVo.getAuditId();
            //预算造价金额
            BigDecimal amountCost = budgeting.getAmountCost();
            //上家送审数
            BigDecimal reviewNumber = baseAccountsVo.getLastSettlementReview().getReviewNumber();
            //下家审定数
            BigDecimal authorizedNumber = baseAccountsVo.getSettlementAuditInformation().getAuthorizedNumber();
            //提交人
            String username = loginUser.getUsername();
            //如果送审数或者审定数超过造价金额的话
            if (reviewNumber.compareTo(amountCost) == 1 || authorizedNumber.compareTo(amountCost) == 1) {


                    DetailsVo detailsVo = new DetailsVo();
                    detailsVo.setType("结算超预算");
                    detailsVo.setDetails("您好，"+baseProject.getProjectName()+"项目，结算金额超出预算，请关注！");
                    detailsVo.setAuditId(baseAccountsVo.getAuditId());
                    detailsVo.setRiskNotification(baseProject.getProjectName()+"项目结算超预算");
                    detailsVo.setBaseId(baseProject.getId());
                    warningDetailsService.addDetails(detailsVo,loginUser);

                //提交人
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A01");
                messageVo.setUserId(loginUser.getId());
                messageVo.setType("3"); //风险
                messageVo.setTitle("您有一个结算项目的结算金额超过造价金额");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo.setContent(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo.setDetails(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageService.sendOrClose(messageVo);
                // 审核人
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditId);
                String name2 = memberManage1.getMemberName();
                MessageVo messageVo1 = new MessageVo();
                messageVo1.setId("A01");
                messageVo1.setType("3"); // 风险
                messageVo1.setUserId(whsjm);
                messageVo1.setPhone(memberManage1.getPhone());
                messageVo1.setReceiver(memberManage1.getEmail());
                messageVo1.setTitle("您有一个待审核的结算项目结算金额超过造价金额！");
                // 「接收人姓名」您好！【提交人】提交给您的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
                messageVo1.setSnsContent(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo1.setContent(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo1.setDetails(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setSnsContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setDetails(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageService.sendOrClose(messageVo1);

                // whzjh 罗均
                MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
                String name3 = memberManage2.getMemberName();
                MessageVo messageVo2 = new MessageVo();
                messageVo2.setId("A01");
                messageVo2.setType("3"); // 风险
                messageVo2.setUserId(whzjh);
                messageVo2.setPhone(memberManage2.getPhone());
                messageVo2.setReceiver(memberManage2.getEmail());
                messageVo2.setTitle("您有一个结算项目的结算金额超过造价金额！");
                // 「接收人姓名」您好！【提交人】提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
                messageVo2.setSnsContent(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo2.setContent(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo2.setDetails(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setSnsContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setDetails(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageService.sendOrClose(messageVo2);

                // whzjm 殷莉萍
                MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
                String name4 = memberManage3.getMemberName();
                MessageVo messageVo3 = new MessageVo();
                messageVo3.setId("A01");
                messageVo3.setType("3"); // 风险
                messageVo3.setUserId(whzjm);
                messageVo3.setPhone(memberManage3.getPhone());
                messageVo3.setReceiver(memberManage3.getEmail());
                messageVo3.setTitle("您有一个结算项目的结算金额超过造价金额！");
                messageVo3.setSnsContent(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo3.setContent(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo3.setDetails(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setSnsContent(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setContent(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setDetails(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageService.sendOrClose(messageVo3);

            }else {
                // 站内信
                MessageVo messageVo4 = new MessageVo();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditId);
                String name = memberManage.getMemberName();
                messageVo4.setId("A15");
                messageVo4.setUserId(auditId);
                messageVo4.setType("1"); //通知
                messageVo4.setTitle("您有一个结算项目待审批！");
                // 「接收人姓名」您好！【提交人】已将【所选项目名称】的结算项目提交给您，请审批！
                messageVo4.setDetails(name + "您好！【"+username+"】已将【" + projectName + "】的结算项目提交给您，请审批！");
//                messageVo4.setDetails(name + "您好！【sjf】已将【" + projectName + "】的结算项目提交给您，请审批！");
                //调用消息Service
//                messageService.sendOrClose(messageVo4);
            }

        }
    }

    @Override
    public BaseAccountsVo findAccountById(String id, UserInfo loginUser) {
//        loginUser = new UserInfo("200101005",null,null,true);
        BaseAccountsVo baseAccountsVo = new BaseAccountsVo();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);

//        1芜湖2马鞍山3江北4吴江
        String district = baseProject.getDistrict();
        if (district.equals("1")){
            baseProject.setDistrict("芜湖");
        }else if(district.equals("2")){
            baseProject.setDistrict("马鞍山");
        }else if(district.equals("3")){
            baseProject.setDistrict("江北");
        }else if(district.equals("4")){
            baseProject.setDistrict("吴江");
        }
//        设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业
        String designCategory = baseProject.getDesignCategory();
        if (designCategory.equals("1")){
            baseProject.setDesignCategory("市政管道");
        }else if(designCategory.equals("2")){
            baseProject.setDesignCategory("管网改造");
        }else if(designCategory.equals("3")){
            baseProject.setDesignCategory("新建小区");
        }else if(designCategory.equals("4")){
            baseProject.setDesignCategory("二次供水项目");
        }else if(designCategory.equals("5")){
            baseProject.setDesignCategory("工商户");
        }else if(designCategory.equals("6")){
            baseProject.setDesignCategory("居民装接水");
        }else if(designCategory.equals("7")){
            baseProject.setDesignCategory("行政事业");
        }
//        1住宅区配套 2商业区配套 3工商区配套
        String projectCategory = baseProject.getProjectCategory();
        if (projectCategory.equals("1")){
            baseProject.setProjectCategory("住宅区配套");
        }else if(projectCategory.equals("2")){
            baseProject.setProjectCategory("商业区配套");
        }else if(projectCategory.equals("3")){
            baseProject.setProjectCategory("工商区配套");
        }
//         1居民住户2开发商 3政府事业 4工商户 5芜湖华衍
        String subject = baseProject.getSubject();
        if (subject.equals("1")){
            baseProject.setSubject("居民住户");
        }else if(subject.equals("2")){
            baseProject.setSubject("开发商");
        }else if(subject.equals("3")){
            baseProject.setSubject("政府事业");
        }else if(subject.equals("4")){
            baseProject.setSubject("工商户");
        }else if(subject.equals("5")){
            baseProject.setSubject("芜湖华衍");
        }
//        1新建 2改造
        String projectNature = baseProject.getProjectNature();
        if (projectNature.equals("1")){
            baseProject.setProjectNature("新建");
        }else if(projectNature.equals("2")){
            baseProject.setProjectNature("改造");
        }
//      1直供水 2二次供水
        String waterSupplyType = baseProject.getWaterSupplyType();
        if (waterSupplyType.equals("1")){
            baseProject.setWaterSupplyType("直供水");
        }else if(waterSupplyType.equals("2")){
            baseProject.setWaterSupplyType("二次供水");
        }
//        AB 1A2B
        String ab = baseProject.getAB();
        if (ab.equals("1")){
            baseProject.setAB("A");
        }else if(ab.equals("2")){
            baseProject.setAB("B");
        }
        String constructionUnit = baseProject.getConstructionOrganization();
        if (constructionUnit!=null && !"".equals(constructionUnit)){
            ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectByPrimaryKey(constructionUnit);
            if (constructionUnitManagement!=null){
                baseProject.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
            }
        }

        baseAccountsVo.setBaseProject(baseProject);


        Example example = new Example(InvestigationOfTheAmount.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId", id);
        c.andEqualTo("delFlag", "0");
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);
        baseAccountsVo.setInvestigationOfTheAmount(investigationOfTheAmount);

        Example example1 = new Example(SettlementInfo.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("baseProjectId", baseProject.getId());
        List<SettlementInfo> settlementInfos = settlementInfoMapper.selectByExample(example1);
        if (settlementInfos.size()>0){
            for (SettlementInfo settlementInfo : settlementInfos) {
                if (settlementInfo.getSumbitMoney() != null) {
                    baseAccountsVo.setSettlementInfo(settlementInfo);

                } else {
                    baseAccountsVo.setLastSettlementInfo(settlementInfo);

                }
            }
            if (baseAccountsVo.getLastSettlementInfo()==null){
                SettlementInfo settlementInfo1 = new SettlementInfo();
                baseAccountsVo.setLastSettlementInfo(settlementInfo1);
            }
            if (baseAccountsVo.getSettlementInfo()==null){
                SettlementInfo settlementInfo1 = new SettlementInfo();
                baseAccountsVo.setSettlementInfo(settlementInfo1);
            }
        }else {
            SettlementInfo settlementInfo = new SettlementInfo();
            baseAccountsVo.setSettlementInfo(settlementInfo);
        }


        Example example2 = new Example(LastSettlementReview.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("baseProjectId", baseProject.getId());
        c2.andEqualTo("delFlag", "0");
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example2);
        if (lastSettlementReview!=null){
            String idByName = memberManageDao.findIdByName(lastSettlementReview.getPreparePeople());
            if (idByName != null){
                lastSettlementReview.setPreparePeople(idByName);
            }
        }
        baseAccountsVo.setLastSettlementReview(lastSettlementReview);
        Example example3 = new Example(SettlementAuditInformation.class);
        Example.Criteria c3 = example3.createCriteria();
        c3.andEqualTo("baseProjectId", baseProject.getId());
        c3.andEqualTo("delFlag", "0");
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example3);
        baseAccountsVo.setSettlementAuditInformation(settlementAuditInformation);
        if (settlementAuditInformation!=null && settlementAuditInformation.getId() != null) {
            baseAccountsVo.setId(settlementAuditInformation.getId());
        } else {
            baseAccountsVo.setId(lastSettlementReview.getId());
        }
        Example example4 = new Example(AuditInfo.class);
        Example.Criteria c4 = example4.createCriteria();
        c4.andEqualTo("baseProjectId", baseAccountsVo.getId());
        c4.andEqualTo("status", "0");
        c4.andEqualTo("auditResult", "0");
        c4.andEqualTo("auditorId", loginUser.getId());
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example4);
        if (auditInfo != null) {
            baseAccountsVo.setCheckAudit(auditInfo.getAuditType());
            if (auditInfo.getAuditType().equals("0") || auditInfo.getAuditType().equals("1") || auditInfo.getAuditType().equals("4")) {
                baseAccountsVo.setShowHidden("1");
            } else if (auditInfo.getAuditType().equals("2") || auditInfo.getAuditType().equals("3") || auditInfo.getAuditType().equals("5")) {
                baseAccountsVo.setShowHidden("2");
            }
        }

        // 回显消息
        String json = "[";
        Example examples = new Example(OtherInfo.class);
        examples.createCriteria().andEqualTo("foreignKey",baseProject.getId())
                .andEqualTo("status","0");
        examples.orderBy("changeNum").asc();
        List<OtherInfo> otherInfos = otherInfoMapper.selectByExample(examples);
        for (int i = 0; i < otherInfos.size(); i++) {
            json += "{" +
                    "\"serialNumber\" : \""+otherInfos.get(i).getSerialNumber()+"\"," +
                    "\"num\": \""+otherInfos.get(i).getNum()+"\","+
                    "},";
        }
        json+="]";
        baseAccountsVo.setJson(json);
//        System.err.println(auditInfo.getAuditType());
        //消息站内通知
//        MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
//        String projectName = baseProject.getProjectName();
//        String id1 = loginUser.getId();
//        String username = loginUser.getUsername();
//        if ("1".equals(auditInfo.getAuditResult())) {
//            MessageVo messageVo = new MessageVo();
//            messageVo.setId("A17");
//            messageVo.setUserId(id1);
//            messageVo.setTitle("您有一个结算项目审批已通过！");
//            messageVo.setDetails(username + "您好！您提交的【" + projectName + "】的结算项目【" + memberManage.getMemberName() + "】已审批通过！");
//            //调用消息Service
//            messageService.sendOrClose(messageVo);
//        } else {
//            MessageVo messageVo = new MessageVo();
//            messageVo.setId("A17");
//            messageVo.setUserId(id1);
//            messageVo.setTitle("您有一个结算项目审批未通过！");
//            messageVo.setDetails(username + "您好！您提交的【" + projectName + "】的结算项目【" + memberManage.getMemberName() + "】未通过，请查看详情！");
//            //调用消息Service
//            messageService.sendOrClose(messageVo);
//        }

        if (baseAccountsVo.getLastSettlementReview()==null){
            baseAccountsVo.setLastSettlementReview(new LastSettlementReview());
        }
        if (baseAccountsVo.getSettlementAuditInformation()==null){
            baseAccountsVo.setSettlementAuditInformation(new SettlementAuditInformation());
        }
        MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(baseAccountsVo.getLastSettlementReview().getPreparePeople());
        if (mkyUser!=null){
            baseAccountsVo.setPreName(mkyUser.getUserName());
        }
        return baseAccountsVo;
    }


    @Override
    public void updateAccountById(BaseAccountsVo baseAccountsVo,UserInfo loginUser,HttpServletRequest request) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //上家审核修改
        if (baseProject.getAB().equals("1") && "2".equals(baseAccountsVo.getSettlementAuditInformation().getContract())){
            LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectByPrimaryKey(baseAccountsVo.getLastSettlementReview().getId());
            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectByPrimaryKey(baseAccountsVo.getSettlementAuditInformation().getId());
            if (lastSettlementReview==null){
                baseAccountsVo.getLastSettlementReview().setId(UUID.randomUUID().toString().replace("-",""));
                baseAccountsVo.getLastSettlementReview().setCreateTime(data);
                baseAccountsVo.getLastSettlementReview().setDelFlag("0");
                baseAccountsVo.getLastSettlementReview().setBaseProjectId(baseProject.getId());
                baseAccountsVo.getLastSettlementReview().setFounderId(loginUser.getId());
                baseAccountsVo.getLastSettlementReview().setAccountId(baseAccountsVo.getSettlementAuditInformation().getId());
                baseAccountsVo.getLastSettlementReview().setWhetherAccount("1");
                lastSettlementReviewDao.insertSelective(baseAccountsVo.getLastSettlementReview());
            }
            if (settlementAuditInformation==null){
                baseAccountsVo.getSettlementAuditInformation().setId(UUID.randomUUID().toString().replace("-", ""));
                baseAccountsVo.getSettlementAuditInformation().setCreateTime(data);
                baseAccountsVo.getSettlementAuditInformation().setDelFlag("0");
                baseAccountsVo.getSettlementAuditInformation().setBaseProjectId(baseProject.getId());
                baseAccountsVo.getSettlementAuditInformation().setFounderId(loginUser.getId());
                baseAccountsVo.getSettlementAuditInformation().setAccountId(baseAccountsVo.getLastSettlementReview().getId());
                baseAccountsVo.getSettlementAuditInformation().setWhetherAccount("1");
                settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());
            }
        }

        settlementInfoMapper.updateByPrimaryKeySelective( baseAccountsVo.getLastSettlementInfo());
        //判断decimal是否为空
        //下家审核修改
        settlementInfoMapper2.updateByPrimaryKeySelective( baseAccountsVo.getSettlementInfo());
        //勘察金额修改
        investigationOfTheAmountDao.updateByPrimaryKeySelective(baseAccountsVo.getInvestigationOfTheAmount());

        if (baseProject.getAB().equals("1") && "2".equals(baseAccountsVo.getSettlementAuditInformation().getContract())){
            //上家送审修改
            lastSettlementReviewDao.updateByPrimaryKeySelective(baseAccountsVo.getLastSettlementReview());
        }
        //下家送审修改
        settlementAuditInformationDao.updateByPrimaryKeySelective(baseAccountsVo.getSettlementAuditInformation());

        Example example2 = new Example(OtherInfo.class);
        Example.Criteria c = example2.createCriteria();
        c.andEqualTo("foreignKey",baseProject.getId());
        c.andEqualTo("status","0");
        List<OtherInfo> otherInfos1 = otherInfoMapper.selectByExample(example2);
        if (otherInfos1!=null && otherInfos1.size()!=0){
            for (OtherInfo otherInfo : otherInfos1) {
                otherInfoMapper.deleteByPrimaryKey(otherInfo);
            }
        }
        // json转换
        if (!"".equals(baseAccountsVo.getComs()) && baseAccountsVo.getComs() != null){
            Json coms = baseAccountsVo.getComs();
            String json = coms.value();
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            int num = 1;
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(baseProject.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(data);
                    otherInfo1.setStatus("0");

                    otherInfo1.setFoundId(loginUser.getId());
                    otherInfo1.setFounderCompany(loginUser.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    otherInfoMapper.insertSelective(otherInfo1);
                    num++;
                }
            }
        }

        //保存
        if (baseAccountsVo.getAuditNumber() == null || baseAccountsVo.getAuditNumber().equals("0")) {
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(memberManage.getMemberName()+"修改保存了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(baseAccountsVo.getLastSettlementReview().getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            return;
            //一审
        } else if (baseAccountsVo.getAuditNumber().equals("1")) {
            //添加一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
            } else {
                auditInfo.setBaseProjectId(baseAccountsVo.getLastSettlementReview().getId());
            }
            auditInfo.setAuditResult("0");
            if (baseProject.getSettleAccountsStatus().equals("2")){
                auditInfo.setAuditType("0");
            }else if(baseProject.getSettleAccountsStatus().equals("4")){
                auditInfo.setAuditType("2");
            }
            auditInfo.setAuditorId(baseAccountsVo.getAuditId());
            auditInfo.setStatus("0");
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            auditInfo.setCreateTime(sim.format(new Date()));
            auditInfoDao.insertSelective(auditInfo);
            baseProject.setSettleAccountsStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            //未通过审
        } else if (baseAccountsVo.getAuditNumber().equals("2")) {
            Example example = new Example(AuditInfo.class);
            Example.Criteria criteria = example.createCriteria();
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null && ! baseAccountsVo.getSettlementAuditInformation().getId().equals("")) {
                criteria.andEqualTo("baseProjectId", baseAccountsVo.getSettlementAuditInformation().getId());
            } else if (baseAccountsVo.getLastSettlementReview().getId() != null && ! baseAccountsVo.getLastSettlementReview().getId().equals("")) {
                criteria.andEqualTo("baseProjectId", baseAccountsVo.getLastSettlementReview().getId());
            }
            List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
            for (AuditInfo auditInfo : auditInfos) {
                if (auditInfo.getAuditResult().equals("2")) {
                    auditInfo.setAuditResult("0");
                    auditInfo.setAuditOpinion("");
                    auditInfo.setAuditTime("");
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    baseProject.setSettleAccountsStatus("1");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }
            }
        }
        //如果提交审核 则发送消息
        if (baseAccountsVo.getAuditNumber().equals("1")) {
            // 操作日志
            String userId = loginUser.getId();
            MemberManage member = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("6"); //结算项目
            operationLog.setContent(member.getMemberName()+"修改提交了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(baseAccountsVo.getLastSettlementReview().getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            //根据上家结算送审外键找到关联预算的信息
            Example example1 = new Example(Budgeting.class);
            example1.createCriteria().andEqualTo("baseProjectId", baseProject.getId());
            Budgeting budgeting = budgetingDao.selectOneByExample(example1);
            //项目名称
            String projectName = baseProject.getProjectName();
            //审核id
            String auditId = baseAccountsVo.getAuditId();
            //预算造价金额
            BigDecimal amountCost = budgeting.getAmountCost();
            //上家送审数
            BigDecimal reviewNumber = baseAccountsVo.getLastSettlementReview().getReviewNumber();
            //下家审定数
            BigDecimal authorizedNumber = baseAccountsVo.getSettlementAuditInformation().getAuthorizedNumber();
            //提交人
            String username = loginUser.getUsername();
            //如果送审数或者审定数超过造价金额的话
            if (reviewNumber!=null && reviewNumber.compareTo(amountCost) == 1 || authorizedNumber!=null && authorizedNumber.compareTo(amountCost) == 1) {

                //提交人
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A01");
                messageVo.setUserId(loginUser.getId());
                messageVo.setType("3"); //风险
                messageVo.setTitle("您有一个结算项目的结算金额超过造价金额");
                // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
                messageVo.setSnsContent(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo.setContent(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo.setDetails(username + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageService.sendOrClose(messageVo);
                // 审核人
                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditId);
                String name2 = memberManage1.getMemberName();
                MessageVo messageVo1 = new MessageVo();
                messageVo1.setId("A01");
                messageVo1.setType("3"); // 风险
                messageVo1.setUserId(whsjm);
                messageVo1.setPhone(memberManage1.getPhone());
                messageVo1.setReceiver(memberManage1.getEmail());
                messageVo1.setTitle("您有一个待审核的结算项目结算金额超过造价金额！");
                // 「接收人姓名」您好！【提交人】提交给您的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
                messageVo1.setSnsContent(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo1.setContent(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo1.setDetails(name2 + "您好！【"+username+"】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setSnsContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setDetails(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageService.sendOrClose(messageVo1);

                // whzjh 罗均
                MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
                String name3 = memberManage2.getMemberName();
                MessageVo messageVo2 = new MessageVo();
                messageVo2.setId("A01");
                messageVo2.setType("3"); // 风险
                messageVo2.setUserId(whzjh);
                messageVo2.setPhone(memberManage2.getPhone());
                messageVo2.setReceiver(memberManage2.getEmail());
                messageVo2.setTitle("您有一个结算项目的结算金额超过造价金额！");
                // 「接收人姓名」您好！【提交人】提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
                messageVo2.setSnsContent(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo2.setContent(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo2.setDetails(name3 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setSnsContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setDetails(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageService.sendOrClose(messageVo2);

                // whzjm 殷莉萍
                MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
                String name4 = memberManage3.getMemberName();
                MessageVo messageVo3 = new MessageVo();
                messageVo3.setId("A01");
                messageVo3.setType("3"); // 风险
                messageVo3.setUserId(whzjm);
                messageVo3.setPhone(memberManage3.getPhone());
                messageVo3.setReceiver(memberManage3.getEmail());
                messageVo3.setTitle("您有一个结算项目的结算金额超过造价金额！");
                messageVo3.setSnsContent(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo3.setContent(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageVo3.setDetails(name4 + "您好！【"+username+"】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setSnsContent(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setContent(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo3.setDetails(name4 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
                messageService.sendOrClose(messageVo3);

            }else {
                // 站内信
                MessageVo messageVo4 = new MessageVo();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditId);
                String name = memberManage.getMemberName();
                messageVo4.setId("A15");
                messageVo4.setUserId(auditId);
                messageVo4.setType("1"); //通知
                messageVo4.setTitle("您有一个结算项目待审批！");
                // 「接收人姓名」您好！【提交人】已将【所选项目名称】的结算项目提交给您，请审批！
                messageVo4.setDetails(name + "您好！【"+username+"】已将【" + projectName + "】的结算项目提交给您，请审批！");
//                messageVo4.setDetails(name + "您好！【sjf】已将【" + projectName + "】的结算项目提交给您，请审批！");
                //调用消息Service
                messageService.sendOrClose(messageVo4);
            }

        }

    }


    @Override
    public void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser,HttpServletRequest request) {
        String id = loginUser.getId();
        String username = loginUser.getUsername();
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            String audit = "";
            //查找上家
            Example example2 = new Example(LastSettlementReview.class);
            Example.Criteria criteria = example2.createCriteria();
            criteria.andEqualTo("baseProjectId",s);
            criteria.andEqualTo("delFlag","0");
            LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example2);
            Example example3 = new Example(SettlementAuditInformation.class);
            Example.Criteria criteria1 = example3.createCriteria();
            criteria1.andEqualTo("baseProjectId",s);
            criteria1.andEqualTo("delFlag","0");
            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example3);
            if (settlementAuditInformation!=null){
                audit = settlementAuditInformation.getId();
            }else if(lastSettlementReview!=null){
                audit = lastSettlementReview.getId();
            }

            Example example = new Example(AuditInfo.class);
            example.createCriteria().andEqualTo("baseProjectId",audit).andEqualTo("auditResult","0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
            if (batchReviewVo.getAuditResult().equals("1")){
                if (auditInfo.getAuditType().equals("0")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(audit);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
                    auditInfo1.setStatus("0");
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");
//
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                   String fouderId = "";
                    if (settlementAuditInformation!=null){
                        fouderId = settlementAuditInformation.getFounderId();
                    }else if(lastSettlementReview!=null){
                        fouderId = lastSettlementReview.getFounderId();
                    }

                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id",fouderId);
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }
//                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfoDao.insertSelective(auditInfo1);

                }else if(auditInfo.getAuditType().equals("1")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(audit);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("4");
                    auditInfo1.setStatus("0");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");
//
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
                    String fouderId = "";
                    if (settlementAuditInformation!=null){
                        fouderId = settlementAuditInformation.getFounderId();
                    }else if(lastSettlementReview!=null){
                        fouderId = lastSettlementReview.getFounderId();
                    }
                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id",fouderId);
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }
//                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfoDao.insertSelective(auditInfo1);

//                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
//                    baseProject.setSettleAccountsStatus("5");
//                    baseProject.setProgressPaymentStatus("6");
//                    baseProject.setVisaStatus("6");
//                    baseProject.setTrackStatus("5");
//                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    //三审
                }else if(auditInfo.getAuditType().equals("4")){
                    String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                    baseProject.setSettleAccountsStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    // 三审通过插入委外金额
                    OutSource outSource = new OutSource();
                    BigDecimal addAmount = new BigDecimal(0);
                    if ("1".equals(lastSettlementReview.getOutsourcing())){
                        addAmount = addAmount.add(lastSettlementReview.getAmountOutsourcing());
                    }else if ("1".equals(settlementAuditInformation.getOutsourcing())){
                        addAmount = addAmount.add(settlementAuditInformation.getAmountOutsourcing());
                    }else {
                        addAmount = new BigDecimal(0);
                    }
                    outSource.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    outSource.setOutMoney(addAmount.toString());
                    outSource.setBaseProjectId(baseProject.getId());
                    outSource.setDistrict(baseProject.getDistrict());
                    outSource.setDept("2"); // 1 设计 2 造价
                    outSource.setDelFlag("0");
                    outSource.setOutType("6"); //结算委外金额
                    outSource.setCreateTime(data);
                    outSource.setUpdateTime(data);
//                    outSource.setFounderCompanyId();
//                    outSource.setFounderId();
                    outSourceMapper.insertSelective(outSource);
                    //上家结算收入信息
                    if (lastSettlementReview != null) {
                        // 绩效计算
                        //计提和
                        BigDecimal total3 = new BigDecimal(0);
                        // 如果是安徽
                        if (!"4".equals(baseProject.getDistrict())) {
                            Double money = projectSumService.anhuiLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                            //咨询费
                            money = (double) Math.round(money * 100) / 100;
                            //计提
                            Double aDouble1 = projectSumService.technicalImprovement(money);
                            aDouble1 = (double) Math.round(aDouble1 * 100) / 100;
                            total3 = total3.add(new BigDecimal(aDouble1));
                            //实际计提 保留两位小数四舍五入
                            BigDecimal actualAmount = total3.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //余额
                            // 上家结算收入信息
                            InCome inCome = new InCome();
                            inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                            inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                            inCome.setIncomeType("4"); // 上家结算编制咨询费
                            inCome.setDistrict(baseProject.getDistrict());
                            inCome.setDept("2"); // 1 设计 2 造价
                            inCome.setDelFlag("0");
                            inCome.setBaseProjectId(baseProject.getId());
                            inCome.setProjectNum(lastSettlementReview.getId());
                            inCome.setCreateTime(data);
                            inCome.setUpdateTime(data);
                            inCome.setFounderId(lastSettlementReview.getFounderId());
//                            inCome.setFounderCompanyId(lastSettlementReview.getFounderCompanyId());
                            inComeMapper.insertSelective(inCome);
                            //如果是吴江
                        } else {
                            //咨询费
                            Double money = projectSumService.wujiangLastSettlementReviewChargeMoney(lastSettlementReview.getReviewNumber());
                            money = (double) Math.round(money * 100) / 100;
                            //计提
                            Double aDouble1 = projectSumService.technicalImprovement(money);
                            aDouble1 = (double) Math.round(aDouble1 * 100) / 100;
                            total3 = total3.add(new BigDecimal(aDouble1));
                            //实际计提 保留两位小数四舍五入
                            BigDecimal actualAmount = total3.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
                           // 下家结算收入信息
                            // 上家结算收入信息
                            InCome inCome = new InCome();
                            inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                            inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                            inCome.setIncomeType("4"); // 上家结算编制咨询费
                            inCome.setDistrict(baseProject.getDistrict());
                            inCome.setDept("2"); // 1 设计 2 造价
                            inCome.setDelFlag("0");
                            inCome.setBaseProjectId(baseProject.getId());
                            inCome.setProjectNum(lastSettlementReview.getId());
                            inCome.setCreateTime(data);
                            inCome.setUpdateTime(data);
                            inCome.setFounderId(lastSettlementReview.getFounderId());
//                            inCome.setFounderCompanyId(lastSettlementReview.getFounderCompanyId());
                            inComeMapper.insertSelective(inCome);

                        }
                    }
                    //下家结算送审 收入信息
                    if (settlementAuditInformation != null) {
                        //计算计提
                        //计提和
                        BigDecimal total7 = new BigDecimal(0);
                        Example example5 = new Example(SettlementInfo.class);
                        //查找结算送审信息
                        example5.createCriteria().andEqualTo("baseProjectId", baseProject.getId())
                                .andEqualTo("upAndDown", "2");
                        SettlementInfo settlementInfo = settlementInfoMapper.selectOneByExample(example5);
                        // 送审金额
                        String sumbitMoney = settlementInfo.getSumbitMoney();
                        if (!"4".equals(baseProject.getDistrict())) {
                            //计算基本费
                            Double money = projectSumService.anhuiSettlementAuditInformationChargeBase(new BigDecimal(sumbitMoney));
                            money = (double) Math.round(money * 100) / 100;
                            //计算核检费
                            Double money1 = projectSumService.anhuiSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                            money1 = (double) Math.round(money1 * 100) / 100;
                            //计算咨询费计算基数
                            Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                            //计提
                            Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                            aDouble = (double) Math.round(aDouble * 100) / 100;
                            total7 = total7.add(new BigDecimal(aDouble));
                            //实际计提 保留两位小数四舍五入
                            BigDecimal actualAmount = total7.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            // 下家结算收入信息
                            InCome inCome = new InCome();
                            inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                            inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                            inCome.setIncomeType("5"); // 下家结算编制咨询费
                            inCome.setDistrict(baseProject.getDistrict());
                            inCome.setDept("2"); // 1 设计 2 造价
                            inCome.setDelFlag("0");
                            inCome.setBaseProjectId(baseProject.getId());
                            inCome.setProjectNum(settlementAuditInformation.getId());
                            inCome.setCreateTime(data);
                            inCome.setUpdateTime(data);
                            inCome.setFounderId(settlementAuditInformation.getFounderId());
//                            inCome.setFounderCompanyId(settlementAuditInformation.getFounderCompanyId());
                            inComeMapper.insertSelective(inCome);
                        } else {
                            //计算基本费
                            Double money = projectSumService.wujiangSettlementAuditInformationChargeBase(new BigDecimal(sumbitMoney));
                            money = (double) Math.round(money * 100) / 100;
                            //计算核检费
                            Double money1 = projectSumService.wujiangSubtractTheNumberMoney(settlementAuditInformation.getSubtractTheNumber());
                            money1 = (double) Math.round(money1 * 100) / 100;
                            //计算咨询费计算基数
                            Double money2 = projectSumService.anhuiSettlementAuditInformationChargeMoney(money, money1);
                            //计提
                            Double aDouble = projectSumService.settlementAuditImprovement(money, money1, money2);
                            aDouble = (double) Math.round(aDouble * 100) / 100;
                            total7 = total7.add(new BigDecimal(aDouble));
                            //实际计提 保留两位小数四舍五入
                            BigDecimal actualAmount = total7.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            // 下家结算收入信息
                            InCome inCome = new InCome();
                            inCome.setId(UUID.randomUUID().toString().replaceAll("-",""));
                            inCome.setInMoney(actualAmount+""); // 收入金额 (实际计提金额)
                            inCome.setIncomeType("5"); // 下家结算编制咨询费
                            inCome.setDistrict(baseProject.getDistrict());
                            inCome.setDept("2"); // 1 设计 2 造价
                            inCome.setDelFlag("0");
                            inCome.setBaseProjectId(baseProject.getId());
                            inCome.setProjectNum(settlementAuditInformation.getId());
                            inCome.setCreateTime(data);
                            inCome.setUpdateTime(data);
                            inCome.setFounderId(settlementAuditInformation.getFounderId());
//                            inCome.setFounderCompanyId(settlementAuditInformation.getFounderCompanyId());
                            inComeMapper.insertSelective(inCome);
                        }
                    }
                    //预算
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
                            Example monthly = new Example(TrackMonthly.class);
                            monthly.createCriteria().andEqualTo("trackId",trackAuditInfo.getId());
                            List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(monthly);
                            for (TrackMonthly thisMony : trackMonthlies) {
                                achievementsInfo.setMemberId(thisMony.getWritter());
                            }
                            //实际计提
                            BigDecimal actualAmount = total5.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);;
                            //余额
                            BigDecimal balance = total5.subtract(actualAmount);
                            // 员工绩效
//                            achievementsInfo.setMemberId(trackAuditInfo.getp()); 跟踪审计没有编制人
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
                            //实际计提 2位 四舍五入
                            BigDecimal actualAmount = total5.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP);;
                            //余额
                            BigDecimal balance = total5.subtract(actualAmount);
                            // 员工绩效
                            Example monthly = new Example(TrackMonthly.class);
                            monthly.createCriteria().andEqualTo("trackId",trackAuditInfo.getId());
                            List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(monthly);
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
                    //待确认一审
                }else if(auditInfo.getAuditType().equals("2")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(audit);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("3");
                    auditInfo1.setStatus("0");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");
//
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);

//                    String founderId = settlementAuditInformation1.getFounderId();
                    String founderId = "";
                    if (settlementAuditInformation!=null){
                        founderId = settlementAuditInformation.getFounderId();
                    }else if(lastSettlementReview!=null){
                        founderId = lastSettlementReview.getFounderId();
                    }
                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id",founderId);
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }
//                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfoDao.insertSelective(auditInfo1);
                    //二审通过进三审
                } else if(auditInfo.getAuditType().equals("3")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(audit);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("5");
                    auditInfo1.setStatus("0");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
//                    Example example1 = new Example(MemberManage.class);
//                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");
//
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
//                    String founderId = settlementAuditInformation1.getFounderId();
                    String founderId = "";
                    if (settlementAuditInformation!=null){
                        founderId = settlementAuditInformation.getFounderId();
                    }else if(lastSettlementReview!=null){
                        founderId = lastSettlementReview.getFounderId();
                    }
                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id",founderId);
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }
//                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfoDao.insertSelective(auditInfo1);
                    //三审通过已完成
                }else if(auditInfo.getAuditType().equals("5")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
//                    baseProject.setSettleAccountsStatus("5");
//                    baseProject.setProgressPaymentStatus("6");
//                    baseProject.setVisaStatus("6");
//                    baseProject.setTrackStatus("5");
//                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    //消息
                    String projectName = baseProject.getProjectName();
                    String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
                    MessageVo messageVo = new MessageVo();
                    messageVo.setId("A17");
                    messageVo.setUserId(id);
                    messageVo.setTitle("您有一个结算项目审批已通过！");
                    messageVo.setDetails(username+"您好！您提交的【"+projectName+"】的进度款支付项目【"+name+"】已审批通过！");
                    //调用消息Service
                    messageService.sendOrClose(messageVo);
                    //若缺失上下家则进入处理中
                    BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(s);
                    if (lastSettlementReview == null || settlementAuditInformation == null){
                        if ( baseProject1.getAB().equals("1") ){
                            if (settlementAuditInformation!=null){
                                if ("2".equals(settlementAuditInformation.getContract())){
                                    BaseProject baseProject3 = baseProjectDao.selectByPrimaryKey(s);
                                    baseProject3.setSettleAccountsStatus("2");
                                    baseProjectDao.updateByPrimaryKeySelective(baseProject3);

                                    if (lastSettlementReview!=null){
                                        Example example1 = new Example(AuditInfo.class);
                                        Example.Criteria c = example1.createCriteria();
                                        c.andEqualTo("baseProjectId",lastSettlementReview.getId());
                                        c.andEqualTo("status","0");
                                        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                                        for (AuditInfo info : auditInfos) {
                                            auditInfoDao.deleteByPrimaryKey(info);
                                        }
                                    }
                                    if (settlementAuditInformation!=null){
                                        Example example1 = new Example(AuditInfo.class);
                                        Example.Criteria c = example1.createCriteria();
                                        c.andEqualTo("baseProjectId",settlementAuditInformation.getId());
                                        c.andEqualTo("status","0");
                                        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                                        for (AuditInfo info : auditInfos) {
                                            auditInfoDao.deleteByPrimaryKey(info);
                                        }
                                    }

                                }
                            }
                        }
                        //否则则进入已完成
                    }else{
                        BaseProject baseProject2 = baseProjectDao.selectByPrimaryKey(s);
                        baseProject2.setSettleAccountsStatus("5");
                        baseProject2.setProgressPaymentStatus("6");
                        baseProject2.setVisaStatus("6");
                        baseProject2.setTrackStatus("5");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject2);
                    }
                }
                // 操作日志
                String userId = loginUser.getId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                operationLog.setName(userId);
                operationLog.setType("6"); //结算算项目
                operationLog.setContent(memberManage.getMemberName()+"审核通过了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
                if (lastSettlementReview != null){
                    operationLog.setDoObject(lastSettlementReview.getId()); // 项目标识
                }else if (settlementAuditInformation != null){
                    operationLog.setDoObject(settlementAuditInformation.getId());
                }else {
                    operationLog.setDoObject(lastSettlementReview.getId()+","+settlementAuditInformation.getId());
                }
                operationLog.setStatus("0");
                operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);

            }else if(batchReviewVo.getAuditResult().equals("2")){
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                baseProject.setSettleAccountsStatus("3");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                // 操作日志
                String userId = loginUser.getId();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                operationLog.setName(userId);
                operationLog.setType("6"); //结算项目
                operationLog.setContent(baseProject.getProjectName()+"项目【"+baseProject.getId()+"】"+memberManage.getMemberName()+"审核未通过");
                if (lastSettlementReview != null){
                    operationLog.setDoObject(lastSettlementReview.getId()); // 项目标识
                }else if (settlementAuditInformation != null){
                    operationLog.setDoObject(settlementAuditInformation.getId());
                }else {
                    operationLog.setDoObject(lastSettlementReview.getId()+","+settlementAuditInformation.getId());
                }
                operationLog.setStatus("0");
                operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);

                //未通过发送消息
                String projectName = baseProject.getProjectName();
                String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A17");
                messageVo.setUserId(id);
                messageVo.setTitle("您有一个结算项目审批未通过！");
                messageVo.setDetails(username+"您好！您提交的【"+projectName+"】的结算项目【"+name+"】未通过,请及时查看详情！");
                //调用消息Service
                messageService.sendOrClose(messageVo);
            }
        }
    }

    @Override
    public List<OtherInfo> selectInfoList(String baseId) {
        Example example = new Example(OtherInfo.class);
        example.createCriteria().andEqualTo("foreignKey",baseId)
                                .andEqualTo("status","0");
        example.orderBy("changeNum").asc();
        List<OtherInfo> otherInfos = otherInfoMapper.selectByExample(example);
        return otherInfos;
//        List<OtherInfo> otherInfos = otherInfoMapper.selectOtherList(baseId);
//        return otherInfos;
    }

    @Override
    public void addAttribution(String baseId, String district, String designCategory, String prePeople) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        baseProject.setDistrict(district);
        baseProject.setDesignCategory(designCategory);
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        Example example = new Example(LastSettlementReview.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",baseId);
        criteria.andEqualTo("delFlag","0");
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview!=null){
            lastSettlementReview.setPreparePeople(prePeople);
        }
        lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
    }

    @Override
    public LastSettlementReview selectPeople(UserInfo loginUser) {
        String memName = memberManageDao.findIdByName(loginUser.getId());
        LastSettlementReview settlementReview = new LastSettlementReview();
        settlementReview.setPreparePeople(memName);
        return settlementReview;
    }

    @Override
    public void addsettleImport(String id, FileInputStream fileInputStream, FileInputStream stream, FileInputStream inputStream, FileInputStream inputStream2, FileInputStream inputStream3) {
        budgetCoverService.summaryTableImport(id,inputStream,fileInputStream);
        budgetCoverService.verificationSheetImport(id,inputStream2);
        budgetCoverService.materialAnalysisImport(id,inputStream3,stream);
    }

    @Override
    public void editOutsourceMoney(String id, String upOutMoney, String downOutMoney) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        Example example = new Example(LastSettlementReview.class);
        example.createCriteria().andEqualTo("baseProjectId",baseProject.getId());
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview != null){
            lastSettlementReview.setAmountOutsourcing(new BigDecimal(upOutMoney));
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }
        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("baseProjectId",baseProject.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation != null){
            settlementAuditInformation.setAmountOutsourcing(new BigDecimal(upOutMoney));
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void addUniProjectImport(String id, FileInputStream inputStream, FileInputStream inputStream2) {
            budgetCoverService.LastSummaryCoverImport(id,inputStream);
            budgetCoverService.UnitProjectSummaryImport(id,inputStream2);
    }
}

package net.zlw.cloud.settleAccounts.service.impl;


import com.alibaba.fastjson.JSONObject;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
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
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    private BudgetingDao budgetingDao;

    @Autowired
    private RemindSetMapper remindSetMapper;
    @Autowired
    private MessageService messageService;

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
//        loginUser = new UserInfo("user320", null, null, true);
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
                }
                return list1;
            }
        }
            //处理中
            if (pageVo.getSettleAccountsStatus().equals("2")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsProcessing(pageVo);
                return list1;
            }
            //未通过
            if (pageVo.getSettleAccountsStatus().equals("3")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsProcessing(pageVo);
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

                return list1;
            }
            //已完成
            if (pageVo.getSettleAccountsStatus().equals("5")){
                List<AccountsVo> list1 = baseProjectDao.findAllAccountsSuccess(pageVo);
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
    public void deleteAcmcounts(String id) {

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
        }
        Example example2 = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("baseProjectId", id);
        criteria2.andEqualTo("delFlag", "0");
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example2);
        if (settlementAuditInformation != null) {
            settlementAuditInformation.setDelFlag("1");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void updateAccount(String s, UserInfo loginUser) {
        BaseProject baseProject = new BaseProject();
        baseProject.setId(s);
        baseProject.setSaWhetherAccount("0");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        //上家到账
        Example example = new Example(LastSettlementReview.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId", s);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview != null) {
            lastSettlementReview.setWhetherAccount("0");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }

        //下家到账
        Example example1 = new Example(SettlementAuditInformation.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId", s);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation != null) {
            settlementAuditInformation.setWhetherAccount("0");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
        //消息通知
        //项目名称
        String projectName = baseProject.getProjectName();
        //根据上家结算送审外键找到关联预算的信息
        Example example2 = new Example(Budgeting.class);
        example1.createCriteria().andEqualTo("baseProjectId", s);
        Budgeting budgeting = budgetingDao.selectOneByExample(example2);
        Example example3 = new Example(AuditInfo.class);
        example1.createCriteria().andEqualTo("baseProjectId", s);
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example3);
        String auditorId = auditInfo.getAuditorId();
        //预算造价金额
        BigDecimal amountCost = budgeting.getAmountCost();
        //上家送审数
        BigDecimal reviewNumber = lastSettlementReview.getReviewNumber();
        //下家审定数
        BigDecimal authorizedNumber = settlementAuditInformation.getAuthorizedNumber();
        //如果送审数或者审定数超过造价金额的话
        if (reviewNumber.compareTo(amountCost) == 1 || authorizedNumber.compareTo(amountCost) == 1) {
            //whsjh 朱让宁
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(whsjh);
            String name1 = memberManage.getMemberName();
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A01");
            messageVo.setUserId(whsjh);

            messageVo.setTitle("您有一个结算项目的结算金额超过造价金额！");
            messageVo.setSnsContent(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo.setContent(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo.setDetails(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
            messageService.sendOrClose(messageVo);
            // whsjm 刘永涛
            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(whsjm);
            String name2 = memberManage1.getMemberName();
            MessageVo messageVo1 = new MessageVo();
            messageVo1.setId("A01");
            messageVo1.setUserId(whsjm);
            messageVo1.setTitle("您有一个结算项目的结算金额超过造价金额！");
            messageVo1.setSnsContent(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo1.setContent(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo1.setDetails(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
            messageService.sendOrClose(messageVo1);

            // whzjh 罗均
            MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
            String name3 = memberManage2.getMemberName();
            MessageVo messageVo2 = new MessageVo();
            messageVo2.setId("A01");
            messageVo2.setUserId(whzjh);
            messageVo2.setTitle("您有一个结算项目的结算金额超过造价金额！");
            messageVo2.setSnsContent(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo2.setContent(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo2.setDetails(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
            messageService.sendOrClose(messageVo2);

            // whzjm 殷莉萍
            MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
            String name4 = memberManage3.getMemberName();
            MessageVo messageVo3 = new MessageVo();
            messageVo3.setId("A01");
            messageVo3.setUserId(whzjm);
            messageVo3.setTitle("您有一个结算项目的结算金额超过造价金额！");
            messageVo3.setSnsContent(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo3.setContent(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
            messageVo3.setDetails(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
            messageService.sendOrClose(messageVo2);

        } else {
            // 站内信
            MessageVo messageVo4 = new MessageVo();
            messageVo4.setId("A15");
            // TODO 登录人id
            messageVo4.setUserId(auditorId);
            messageVo4.setTitle("您有一个结算项目待审批！");
            messageVo4.setDetails(loginUser.getUsername() + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
            //调用消息Service
            messageService.sendOrClose(messageVo4);
        }
    }

    @Override
    public void addAccount(BaseAccountsVo baseAccountsVo, UserInfo loginUser) {
//        loginUser = new UserInfo("user320",null,null,true);
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //判断bigdecimal
        if ("".equals(baseAccountsVo.getSettlementInfo().getSumbitMoney())){
            baseAccountsVo.getSettlementInfo().setSumbitMoney("0");
        }

        if ("".equals(baseAccountsVo.getLastSettlementReview().getReviewNumber())){
            baseAccountsVo.getLastSettlementReview().setReviewNumber(null);
        }
        if ("".equals(baseAccountsVo.getSettlementAuditInformation().getAuthorizedNumber())){
            baseAccountsVo.getSettlementAuditInformation().setAuthorizedNumber(null);
        }
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //其他信息表
        Json coms = baseAccountsVo.getComs();
        String json = coms.value();
        List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
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
                otherInfoMapper.insertSelective(otherInfo1);
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
        if (baseProject.getAB().equals("1")){
            if(baseAccountsVo.getLastSettlementReview().getId() != null) {
                lastSettlementReviewDao.insertSelective(baseAccountsVo.getLastSettlementReview());
            }
            if(baseAccountsVo.getSettlementAuditInformation().getId() != null){
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
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        } else {
            baseProject.setSettleAccountsStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
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
        //项目名称
        String projectName = baseProject.getProjectName();
        //根据上家结算送审外键找到关联预算的信息
        Example example1 = new Example(Budgeting.class);
        example1.createCriteria().andEqualTo("baseProjectId", baseProject.getId());
        Budgeting budgeting = budgetingDao.selectOneByExample(example1);
        //审核id
        String auditId = baseAccountsVo.getAuditId();
        //预算造价金额
        BigDecimal amountCost = budgeting.getAmountCost();
        //上家送审数
        BigDecimal reviewNumber = baseAccountsVo.getLastSettlementReview().getReviewNumber();
        //下家审定数
        BigDecimal authorizedNumber = baseAccountsVo.getSettlementAuditInformation().getAuthorizedNumber();


        //如果送审数或者审定数超过造价金额的话
//        if (reviewNumber.compareTo(amountCost) == 1 || authorizedNumber.compareTo(amountCost) == 1) {
//            //whsjh 朱让宁
//            MemberManage memberManage = memberManageDao.selectByPrimaryKey(whsjh);
//            String name1 = memberManage.getMemberName();
//            MessageVo messageVo = new MessageVo();
//            messageVo.setId("A01");
//            messageVo.setUserId(whsjh);
//
//            messageVo.setTitle("您有一个结算项目的结算金额超过造价金额！");
//            messageVo.setSnsContent(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo.setContent(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo.setDetails(name1 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
//            messageService.sendOrClose(messageVo);
//            // whsjm 刘永涛
//            MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(whsjm);
//            String name2 = memberManage1.getMemberName();
//            MessageVo messageVo1 = new MessageVo();
//            messageVo1.setId("A01");
//            messageVo1.setUserId(whsjm);
//            messageVo1.setTitle("您有一个结算项目的结算金额超过造价金额！");
//            messageVo1.setSnsContent(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo1.setContent(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo1.setDetails(name2 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
//            messageService.sendOrClose(messageVo1);
//
//            // whzjh 罗均
//            MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
//            String name3 = memberManage2.getMemberName();
//            MessageVo messageVo2 = new MessageVo();
//            messageVo2.setId("A01");
//            messageVo2.setUserId(whzjh);
//            messageVo2.setTitle("您有一个结算项目的结算金额超过造价金额！");
//            messageVo2.setSnsContent(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo2.setContent(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo2.setDetails(name3 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
//            messageService.sendOrClose(messageVo2);
//
//            // whzjm 殷莉萍
//            MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
//            String name4 = memberManage3.getMemberName();
//            MessageVo messageVo3 = new MessageVo();
//            messageVo3.setId("A01");
//            messageVo3.setUserId(whzjm);
//            messageVo3.setTitle("您有一个结算项目的结算金额超过造价金额！");
//            messageVo3.setSnsContent(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo3.setContent(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//            messageVo3.setDetails(name4 + "您好！您提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时查看详情！");
//            messageService.sendOrClose(messageVo2);
//
//        } else {
//            // 站内信
//            MessageVo messageVo4 = new MessageVo();
//            messageVo4.setId("A15");
//            messageVo4.setUserId(auditId);
//            messageVo4.setTitle("您有一个结算项目待审批！");
//            messageVo4.setDetails(loginUser.getUsername() + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
//            //调用消息Service
////            messageService.sendOrClose(messageVo4);
//        }
    }

    @Override
    public BaseAccountsVo findAccountById(String id, UserInfo loginUser) {
//        loginUser = new UserInfo("200610002",null,null,true);
        BaseAccountsVo baseAccountsVo = new BaseAccountsVo();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
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
                    SettlementInfo settlementInfo1 = new SettlementInfo();
                    baseAccountsVo.setLastSettlementInfo(settlementInfo1);
                } else {
                    baseAccountsVo.setLastSettlementInfo(settlementInfo);
                    SettlementInfo settlementInfo1 = new SettlementInfo();
                    baseAccountsVo.setSettlementInfo(settlementInfo1);
                }
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
        baseAccountsVo.setLastSettlementReview(lastSettlementReview);

        Example example3 = new Example(SettlementAuditInformation.class);
        Example.Criteria c3 = example3.createCriteria();
        c3.andEqualTo("baseProjectId", baseProject.getId());
        c3.andEqualTo("delFlag", "0");
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example3);
        baseAccountsVo.setSettlementAuditInformation(settlementAuditInformation);
        if (settlementAuditInformation.getId() != null) {
            baseAccountsVo.setId(settlementAuditInformation.getId());
        } else {
            baseAccountsVo.setId(lastSettlementReview.getId());
        }
        Example example4 = new Example(AuditInfo.class);
        Example.Criteria c4 = example4.createCriteria();
        c4.andEqualTo("baseProjectId", baseAccountsVo.getId());
        c4.andEqualTo("status", "0");
        c4.andEqualTo("auditResult", "0");
//        c4.andEqualTo("auditorId", loginUser.getId());
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
        return baseAccountsVo;
    }

    @Override
    public void updateAccountById(BaseAccountsVo baseAccountsVo,UserInfo loginUser) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //上家审核修改
        settlementInfoMapper.updateByPrimaryKeySelective( baseAccountsVo.getLastSettlementInfo());
        //判断decimal是否为空
        //下家审核修改
        settlementInfoMapper2.updateByPrimaryKeySelective( baseAccountsVo.getSettlementInfo());
        //勘察金额修改
        investigationOfTheAmountDao.updateByPrimaryKeySelective(baseAccountsVo.getInvestigationOfTheAmount());
        //上家送审修改
        lastSettlementReviewDao.updateByPrimaryKeySelective(baseAccountsVo.getLastSettlementReview());
        //下家送审修改
        settlementAuditInformationDao.updateByPrimaryKeySelective(baseAccountsVo.getSettlementAuditInformation());
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // json转换
        Json coms = baseAccountsVo.getComs();
        String json = coms.value();
        List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
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
                otherInfoMapper.updateByPrimaryKeySelective(otherInfo1);
            }
        }
        //保存
        if (baseAccountsVo.getAuditNumber() == null || baseAccountsVo.getAuditNumber().equals("0")) {
            return;
            //一审
        } else if (baseAccountsVo.getAuditNumber().equals("1")) {
            //添加一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                auditInfo.setBaseProjectId(baseAccountsVo.getLastSettlementReview().getId());
            } else {
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
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
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                criteria.andEqualTo("baseProjectId", baseAccountsVo.getSettlementAuditInformation().getId());
            } else if (baseAccountsVo.getLastSettlementReview().getId() != null) {
                criteria.andEqualTo("baseProjectId", baseAccountsVo.getLastSettlementReview().getId());
            }
            List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
            for (AuditInfo auditInfo : auditInfos) {
                if (auditInfo.getAuditResult().equals("2")) {
                    auditInfo.setAuditResult("0");
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    baseProject.setSettleAccountsStatus("1");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }
            }
        }

    }


    @Override
    public void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser) {
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
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                    baseProject.setSettleAccountsStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
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
                    baseProject.setSettleAccountsStatus("5");
                    baseProject.setProgressPaymentStatus("6");
                    baseProject.setVisaStatus("6");
                    baseProject.setTrackStatus("5");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
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
                    if ((lastSettlementReview == null || settlementAuditInformation == null) && ! baseProject1.getAB().equals("2")){
                        BaseProject baseProject3 = baseProjectDao.selectByPrimaryKey(s);
                        baseProject3.setSettleAccountsStatus("2");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject3);
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


            }else if(batchReviewVo.getAuditResult().equals("2")){
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                baseProject.setSettleAccountsStatus("3");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                //未通过发送消息
                String projectName = baseProject.getProjectName();
                String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A17");
                messageVo.setUserId(id);
                messageVo.setTitle("您有一个进度款支付项目审批未通过！");
                messageVo.setDetails(username+"您好！您提交的【"+projectName+"】的进度款支付项目【"+name+"】未通过,请及时查看详情！");
                //调用消息Service
                messageService.sendOrClose(messageVo);
            }
        }
    }

    @Override
    public List<OtherInfo> selectInfoList(String baseId) {
        Example example = new Example(OtherInfo.class);
        example.createCriteria().andEqualTo("foreignKey",baseId);
        List<OtherInfo> otherInfos = otherInfoMapper.selectByExample(baseId);
        return otherInfos;
    }
}

package net.zlw.cloud.settleAccounts.service.impl;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementInfoMapper;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SettleAccountsServiceimpl implements SettleAccountsService {
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
        loginUser = new UserInfo("user309",null,null,true);
        List<AccountsVo> list = baseProjectDao.findAllAccounts(pageVo);
        ArrayList<AccountsVo> returnList = new ArrayList<>();
        for (AccountsVo accountsVo : list) {
            //待审核
            if (pageVo.getSettleAccountsStatus().equals("1")){
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",accountsVo.getAccountId())
                        .andEqualTo("auditResult","0");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo!=null){
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage !=null){
                        accountsVo.setCurrentHandler(memberManage.getMemberName());
                    }
                }

                if (loginUser.getId().equals(accountsVo.getFounderId()) || whzjh.equals(accountsVo.getFounderId()) || whzjm.equals(accountsVo.getFounderId()) || wjzjh.equals(accountsVo.getFounderId()) || loginUser.getId().equals(accountsVo.getAuditorId())){
                    returnList.add(accountsVo);
                }
            }
            //处理中
            if (pageVo.getSettleAccountsStatus().equals("2")){
                if (loginUser.getId().equals(accountsVo.getFounderId())){
                    returnList.add(accountsVo);
                }
            }
            //未通过
            if (pageVo.getSettleAccountsStatus().equals("3")){
                if (loginUser.getId().equals(accountsVo.getFounderId())){
                    returnList.add(accountsVo);
                }
            }
            //待确认
            if (pageVo.getSettleAccountsStatus().equals("4")){
                returnList.add(accountsVo);
            }
            //已完成
            if (pageVo.getSettleAccountsStatus().equals("5")){
                returnList.add(accountsVo);
            }
            //全部
            if (pageVo.getSettleAccountsStatus().equals("") || pageVo.getSettleAccountsStatus().equals("0")){
                if (loginUser.getId().equals(accountsVo.getFounderId())){
                    returnList.add(accountsVo);
                }
            }
        }
        ArrayList<AccountsVo> accountsVos = new ArrayList<>();
        for (AccountsVo accountsVo : returnList) {
            if (! accountsVos.contains(accountsVo)){
                accountsVos.add(accountsVo);
            }
        }
        return accountsVos;
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
    public void updateAccount(String s) {
        BaseProject baseProject = new BaseProject();
        baseProject.setId(s);
        baseProject.setSaWhetherAccount("0");
        baseProjectDao.updateByPrimaryKeySelective(baseProject);

        //上家到账
        Example example = new Example(LastSettlementReview.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",s);
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        if (lastSettlementReview!=null){
            lastSettlementReview.setWhetherAccount("0");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }

        //下家到账
        Example example1 = new Example(SettlementAuditInformation.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId",s);
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation!=null){
            settlementAuditInformation.setWhetherAccount("0");
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        }
    }

    @Override
    public void addAccount(BaseAccountsVo baseAccountsVo, UserInfo loginUser) {
        System.err.println(baseAccountsVo.getInvestigationOfTheAmount());
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //添加上家送审
        System.out.println(baseAccountsVo);
        baseAccountsVo.getLastSettlementInfo().setId(UUID.randomUUID().toString().replace("-",""));
        baseAccountsVo.getLastSettlementInfo().setBaseProjectId(baseProject.getId());
        SimpleDateFormat simd = new SimpleDateFormat("yyyy-MM-dd");
        baseAccountsVo.getLastSettlementInfo().setCreateTime(simd.format(new Date()));
        baseAccountsVo.getLastSettlementInfo().setState("0");
        baseAccountsVo.getLastSettlementInfo().setFouderId(loginUser.getId());
        settlementInfoMapper.insertSelective(baseAccountsVo.getLastSettlementInfo());
        //添加勘察金额
        baseAccountsVo.getInvestigationOfTheAmount().setId(UUID.randomUUID().toString().replace("-",""));
        baseAccountsVo.getInvestigationOfTheAmount().setBaseProjectId(baseProject.getId());
        baseAccountsVo.getInvestigationOfTheAmount().setCreateTime(simd.format(new Date()));
        baseAccountsVo.getInvestigationOfTheAmount().setDelFlag("0");
        baseAccountsVo.getInvestigationOfTheAmount().setFounderId(loginUser.getId());
        investigationOfTheAmountDao.insertSelective(baseAccountsVo.getInvestigationOfTheAmount());
        //添加下家送审
        baseAccountsVo.getSettlementInfo().setId(UUID.randomUUID().toString().replace("-",""));
        baseAccountsVo.getSettlementInfo().setBaseProjectId(baseProject.getId());
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        baseAccountsVo.getSettlementInfo().setCreateTime(sim.format(new Date()));
        baseAccountsVo.getSettlementInfo().setState("0");
        baseAccountsVo.getSettlementInfo().setFouderId(loginUser.getId());
        settlementInfoMapper2.insertSelective(baseAccountsVo.getSettlementInfo());
        //添加上家结算送审
        baseAccountsVo.getLastSettlementReview().setId(UUID.randomUUID().toString().replace("-",""));
        baseAccountsVo.getLastSettlementReview().setCreateTime(sim.format(new Date()));
        baseAccountsVo.getLastSettlementReview().setDelFlag("0");
        baseAccountsVo.getLastSettlementReview().setBaseProjectId(baseProject.getId());
        baseAccountsVo.getLastSettlementReview().setFounderId(loginUser.getId());
        //添加下家结算送审
        baseAccountsVo.getSettlementAuditInformation().setId(UUID.randomUUID().toString().replace("-", ""));
        baseAccountsVo.getSettlementAuditInformation().setCreateTime(sim.format(new Date()));
        baseAccountsVo.getSettlementAuditInformation().setDelFlag("0");
        baseAccountsVo.getSettlementAuditInformation().setBaseProjectId(baseProject.getId());
        baseAccountsVo.getSettlementAuditInformation().setFounderId(loginUser.getId());

        if (baseAccountsVo.getLastSettlementReview().getId() != null) {
            baseAccountsVo.getSettlementAuditInformation().setAccountId(baseAccountsVo.getLastSettlementReview().getId());
            baseAccountsVo.getSettlementAuditInformation().setWhetherAccount("1");
        }
        if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
            baseAccountsVo.getLastSettlementReview().setAccountId(baseAccountsVo.getSettlementAuditInformation().getId());
            baseAccountsVo.getLastSettlementReview().setWhetherAccount("1");
        }
        lastSettlementReviewDao.insertSelective(baseAccountsVo.getLastSettlementReview());
        settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());

        if (baseAccountsVo.getAuditId() != null) {
            //添加一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
            } else {
                auditInfo.setBaseProjectId(baseAccountsVo.getLastSettlementReview().getId());
            }
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(baseAccountsVo.getAuditId());
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(sim.format(new Date()));
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
        c.andEqualTo("userId", loginUser.getId());
        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);
        for (FileInfo fileInfo : fileInfos) {
            //修改文件外键
            if (baseAccountsVo.getSettlementAuditInformation().getId() != null) {
                fileInfoService.updateFileName2(fileInfo.getId(), baseAccountsVo.getSettlementAuditInformation().getId());
            } else {
                fileInfoService.updateFileName2(fileInfo.getId(), baseAccountsVo.getLastSettlementReview().getId());
            }
        }

    }

    @Override
    public BaseAccountsVo findAccountById(String id, UserInfo loginUser) {
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
        for (SettlementInfo settlementInfo : settlementInfos) {
            if (settlementInfo.getSumbitMoney() != null) {
                baseAccountsVo.setSettlementInfo(settlementInfo);
            } else {
                baseAccountsVo.setLastSettlementInfo(settlementInfo);
            }
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
        c4.andEqualTo("auditorId", loginUser.getId());
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example4);
        if (auditInfo!=null){
            baseAccountsVo.setCheckAudit(auditInfo.getAuditType());
        }
//        System.err.println(auditInfo.getAuditType());

        return baseAccountsVo;
    }

    @Override
    public void updateAccountById(BaseAccountsVo baseAccountsVo) {
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseAccountsVo.getBaseProject().getId());
        //上家审核修改
        settlementInfoMapper.updateByPrimaryKeySelective( baseAccountsVo.getLastSettlementInfo());
        //下家审核修改
        settlementInfoMapper2.updateByPrimaryKeySelective( baseAccountsVo.getSettlementInfo());
        //勘察金额修改
        investigationOfTheAmountDao.updateByPrimaryKeySelective(baseAccountsVo.getInvestigationOfTheAmount());
        //上家送审修改
        lastSettlementReviewDao.updateByPrimaryKeySelective(baseAccountsVo.getLastSettlementReview());
        //下家送审修改
        settlementAuditInformationDao.updateByPrimaryKeySelective(baseAccountsVo.getSettlementAuditInformation());

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
    public void batchReview(BatchReviewVo batchReviewVo) {
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
                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
                    String founderId = settlementAuditInformation1.getFounderId();
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
                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
                    String founderId = settlementAuditInformation1.getFounderId();
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
                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
                    String founderId = settlementAuditInformation1.getFounderId();
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
                    SettlementAuditInformation settlementAuditInformation1 = settlementAuditInformationDao.selectByPrimaryKey(s);
                    String founderId = settlementAuditInformation1.getFounderId();
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
            }
        }
    }
}

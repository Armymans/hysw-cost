package net.zlw.cloud.settleAccounts.service.impl;


import net.tec.cloud.common.bean.UserInfo;
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
import net.zlw.cloud.warningDetails.model.MemberManage;
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

    @Override
    public List<AccountsVo> findAllAccounts(PageVo pageVo, UserInfo loginUser) {
        List<AccountsVo> list  = baseProjectDao.findAllAccounts(pageVo);
        ArrayList<AccountsVo> accountsVos = new ArrayList<>();
        for (AccountsVo accountsVo : list) {
            if (accountsVo.getSumbitMoney()!=null){
                if (!accountsVos.contains(accountsVo)){
                    accountsVos.add(accountsVo);
                }
            }
        }
        for (int i = 0; i < accountsVos.size(); i++) {
            if (pageVo.getSettleAccountsStatus().equals("1")){
                if (accountsVos.get(i).getAuditorId()!=null){
                    if (!accountsVos.get(i).getAuditorId().equals(loginUser.getId())){
                        accountsVos.remove(i);
                        i--;
                    }
                }else{
                    accountsVos.remove(i);
                    i--;
                }
            }
            if (pageVo.getSettleAccountsStatus().equals("3")){
                if (accountsVos.get(i).getFounderId()==null && !accountsVos.get(i).getFounderId().equals(loginUser.getId())){
                    accountsVos.remove(i);
                    i--;
                }
            }
        }

        return accountsVos;
    }

    @Override
    public void deleteAcmcounts(String id) {

        Example example = new Example(InvestigationOfTheAmount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",id);
        criteria.andEqualTo("delFlag","0");
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);
        investigationOfTheAmount.setDelFlag("1");
        investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);

        Example example1 = new Example(LastSettlementReview.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseProjectId",id);
        criteria1.andEqualTo("delFlag","0");
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example1);
        if (lastSettlementReview!=null){
            lastSettlementReview.setDelFlag("1");
            lastSettlementReviewDao.updateByPrimaryKeySelective(lastSettlementReview);
        }
        Example example2 = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("baseProjectId",id);
        criteria2.andEqualTo("delFlag","0");
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example2);
        if (settlementAuditInformation!=null){
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
        baseAccountsVo.getSettlementAuditInformation().setId(UUID.randomUUID().toString().replace("-",""));
        baseAccountsVo.getSettlementAuditInformation().setCreateTime(sim.format(new Date()));
        baseAccountsVo.getSettlementAuditInformation().setDelFlag("0");
        baseAccountsVo.getSettlementAuditInformation().setBaseProjectId(baseProject.getId());
        baseAccountsVo.getSettlementAuditInformation().setFounderId(loginUser.getId());

        if (baseAccountsVo.getLastSettlementReview().getId()!=null){
            baseAccountsVo.getSettlementAuditInformation().setAccountId(baseAccountsVo.getLastSettlementReview().getId());
            baseAccountsVo.getSettlementAuditInformation().setWhetherAccount("1");
        }
        if (baseAccountsVo.getSettlementAuditInformation().getId()!=null){
            baseAccountsVo.getLastSettlementReview().setAccountId(baseAccountsVo.getSettlementAuditInformation().getId());
            baseAccountsVo.getLastSettlementReview().setWhetherAccount("1");
        }
        lastSettlementReviewDao.insertSelective(baseAccountsVo.getLastSettlementReview());
        settlementAuditInformationDao.insertSelective(baseAccountsVo.getSettlementAuditInformation());

        if (baseAccountsVo.getAuditId()!=null){
            //添加一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            if (baseAccountsVo.getSettlementAuditInformation().getId()!=null){
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
            }else{
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
        }else{
            baseProject.setSettleAccountsStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }

    }

    @Override
    public BaseAccountsVo findAccountById(String id) {
        BaseAccountsVo baseAccountsVo = new BaseAccountsVo();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(id);
        baseAccountsVo.setBaseProject(baseProject);

        Example example = new Example(InvestigationOfTheAmount.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example);
        baseAccountsVo.setInvestigationOfTheAmount(investigationOfTheAmount);

        Example example1 = new Example(SettlementInfo.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("baseProjectId",baseProject.getId());
        List<SettlementInfo> settlementInfos = settlementInfoMapper.selectByExample(example1);
        for (SettlementInfo settlementInfo : settlementInfos) {
            if (settlementInfo.getSumbitMoney()!=null){
                baseAccountsVo.setSettlementInfo(settlementInfo);
            }else{
                baseAccountsVo.setLastSettlementInfo(settlementInfo);
            }
        }

        Example example2 = new Example(LastSettlementReview.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("baseProjectId",baseProject.getId());
        LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example2);
        baseAccountsVo.setLastSettlementReview(lastSettlementReview);

        Example example3 = new Example(SettlementAuditInformation.class);
        Example.Criteria c3 = example3.createCriteria();
        c3.andEqualTo("baseProjectId",baseProject.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example3);
        baseAccountsVo.setSettlementAuditInformation(settlementAuditInformation);


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
        if (baseAccountsVo.getAuditNumber() == null || baseAccountsVo.getAuditNumber().equals("0")){
            return;
         //一审
        }else if (baseAccountsVo.getAuditNumber().equals("1")){
            //添加一审
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            if (baseAccountsVo.getSettlementAuditInformation().getId()!=null){
                auditInfo.setBaseProjectId(baseAccountsVo.getLastSettlementReview().getId());
            }else{
                auditInfo.setBaseProjectId(baseAccountsVo.getSettlementAuditInformation().getId());
            }
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(baseAccountsVo.getAuditId());
            auditInfo.setStatus("0");
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            auditInfo.setCreateTime(sim.format(new Date()));
            auditInfoDao.insertSelective(auditInfo);
            baseProject.setSettleAccountsStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
         //未通过审
        } else if(baseAccountsVo.getAuditNumber().equals("2")){
            Example example = new Example(AuditInfo.class);
            Example.Criteria criteria = example.createCriteria();
            if (baseAccountsVo.getSettlementAuditInformation().getId()!=null){
                criteria.andEqualTo("baseProjectId",baseAccountsVo.getSettlementAuditInformation().getId());
            }else if(baseAccountsVo.getLastSettlementReview().getId()!=null){
                criteria.andEqualTo("baseProjectId",baseAccountsVo.getLastSettlementReview().getId());
            }
            List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
            for (AuditInfo auditInfo : auditInfos) {
                if (auditInfo.getAuditResult().equals("2")){
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
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("depId","2") .andEqualTo("depAdmin","1");

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
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                    baseProject.setSettleAccountsStatus("5");
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

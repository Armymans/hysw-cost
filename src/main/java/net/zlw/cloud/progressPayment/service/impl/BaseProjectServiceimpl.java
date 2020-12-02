package net.zlw.cloud.progressPayment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.mapper.BudgetingMapper;
import net.zlw.cloud.designProject.model.Budgeting;
import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.progressPayment.mapper.*;
import net.zlw.cloud.progressPayment.model.*;
import net.zlw.cloud.progressPayment.model.vo.*;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.statisticalAnalysis.model.vo.NumberVo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BaseProjectServiceimpl implements BaseProjectService {
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private ApplicationInformationDao applicationInformationDao;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private ProgressPaymentTotalPaymentDao progressPaymentTotalPaymentDao;
    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

    @Resource
    private BudgetingMapper budgetingMapper;

    @Resource
    private RemindSetMapper remindSetMapper;
    @Resource
    private MessageService messageService;

    @Autowired
    private FileInfoMapper fileInfoMapper;

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

    /**
     * 添加进度款信息
     *
     * @param baseProject 用户填写信息表
     * @param loginUser
     */
    @Override
    public void addProgress(BaseProjectVo baseProject, UserInfo loginUser) {

        if (baseProject.getAmountOutsourcing().equals("")){
            baseProject.setAmountOutsourcing(null);
        }

//        loginUser = new UserInfo("user309",null,null,true);

//        Budgeting budgeting = budgetingMapper.selectByPrimaryKey(baseProject.getId());

        //项目基本信息
//        BaseProject project = findById(budgeting.getBaseProjectId());
        BaseProject project = baseProjectDao.selectByPrimaryKey(baseProject.getBaseId());
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = new ProgressPaymentInformation();
        //审核表
        AuditInfo auditInfo = new AuditInfo();
        //成员管理

        paymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
        paymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
        paymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
        paymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
        paymentInformation.setContractAmount(baseProject.getContractAmount());
        paymentInformation.setProjectType(baseProject.getProjectType());
        paymentInformation.setReceivingTime(baseProject.getReceivingTime());
        paymentInformation.setCompileTime(baseProject.getCompileTime());
        paymentInformation.setOutsourcing(baseProject.getOutsourcing());
        paymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
        paymentInformation.setContact(baseProject.getContact());
        paymentInformation.setContactPhone(baseProject.getContactPhone());
        paymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
        paymentInformation.setSituation(baseProject.getSituation());
        paymentInformation.setRemarkes(baseProject.getRemarkes());
        paymentInformation.setBaseProjectId(project.getId());
        paymentInformation.setId(UUID.randomUUID().toString().replace("-", ""));
        paymentInformation.setFounderId(loginUser.getId());
        paymentInformation.setDelFlag("0");
        paymentInformation.setChangeNum(1);

        String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
        paymentInformation.setCreateTime(format);


        if (baseProject != null && !baseProject.equals("")) {
            if (baseProject.getAuditNumber() != null && !baseProject.getAuditNumber().equals("")) {
//                paymentInformation.setChangeNum(1);

                project.setProgressPaymentStatus("1");
                project.setProjectFlow(project.getProjectFlow() + ",4");
                baseProjectDao.updateByPrimaryKeySelective(project);
                auditInfo.setBaseProjectId(paymentInformation.getId());
                auditInfo.setAuditResult("0");
                auditInfo.setAuditorId(baseProject.getAuditorId());
                auditInfo.setCreateTime(format);
                auditInfo.setAuditType("0");
                auditInfo.setStatus("0");
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                auditInfoDao.insertSelective(auditInfo);
                // 站内信
                MessageVo messageVo4 = new MessageVo();
                messageVo4.setId("A09");
                messageVo4.setType("1"); //通知
                messageVo4.setUserId(auditInfo.getAuditorId());
                messageVo4.setTitle("您有一个进度款项目待审批！");
                messageVo4.setDetails(loginUser.getUsername()+"您好！您提交的【" + baseProject.getProjectName() + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
                //调用消息Service
                messageService.sendOrClose(messageVo4);
            } else {
                project.setProgressPaymentStatus("2");
                project.setProjectFlow(project.getProjectFlow() + ",4");
                baseProjectDao.updateByPrimaryKeySelective(project);
            }
            progressPaymentInformationDao.insertSelective(paymentInformation);

            information.setRemarkes(baseProject.getRemarkes());
            information.setBaseProjectId(project.getId());
            information.setId(UUID.randomUUID().toString());
            information.setProgressPaymentId(paymentInformation.getId());
            information.setDelFlag("0");
            information.setCreateTime(format);
            information.setFounderId(loginUser.getId());
            applicationInformationDao.insertSelective(information);


            payment.setId(UUID.randomUUID().toString());
            payment.setTotalPaymentAmount(baseProject.getCurrentPaymentInformation());
            payment.setCumulativeNumberPayment(new BigDecimal(baseProject.getCumulativePaymentTimes()));
            payment.setAccumulativePaymentProportion(baseProject.getCurrentPaymentRatio());
            payment.setProgressPaymentId(paymentInformation.getId());
            payment.setBaseProjectId(project.getId());
            payment.setCreateTime(format);
            payment.setFounderId(loginUser.getId());
            payment.setDelFlag("0");

            progressPaymentTotalPaymentDao.insertSelective(payment);










            // 上传文件，新建-申请信息，列表文件
            List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(baseProject.getKey(), baseProject.getType());

            for (FileInfo fileInfo : byFreignAndType) {
                fileInfo.setPlatCode(paymentInformation.getId());

                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }

            // 本期进度款支付信息列表文件
            List<FileInfo> freignAndType = fileInfoMapper.findByFreignAndType(baseProject.getKey(), baseProject.getType1());
            for (FileInfo fileInfo : freignAndType) {
                fileInfo.setPlatCode(paymentInformation.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }

            String auditorId = baseProject.getAuditorId();//审核id
            String projectName = baseProject.getProjectName();//项目名称
            BigDecimal totalMoney = new BigDecimal(0);
            //根据id找到累积进度款支付的信息
//            List<ProgressPaymentInformation> amount = progressPaymentInformationDao.findAmount(project.getId());
//            //根据id找到基本信息表的信息
//            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(project.getId());



            //站内信
                BigDecimal contractAmount = paymentInformation.getContractAmount();//合同金额
//                if (totalMoney != null && "".equals(totalMoney) || contractAmount != null && "".equals(contractAmount)) {
                    BigDecimal qi = new BigDecimal("0.7");
                    BigDecimal multiply = contractAmount.multiply(qi);
                    //如果累计支付金额大于合同金额的70%就发邮件和短信
                    if (totalMoney.compareTo(multiply) == 1) {
                        //whsjh 朱让宁
                        MemberManage memberManage = memberManageDao.selectByPrimaryKey(whsjh);
                        String name1 = memberManage.getMemberName();
                        MessageVo messageVo = new MessageVo();
                        messageVo.setId("A02");
                        messageVo.setUserId(whsjh);
                        messageVo.setType("3"); //風險
                        messageVo.setReceiver(auditorId);
                        messageVo.setTitle("您有一个进度款支付项目已超额！");
                        messageVo.setSnsContent(name1+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo.setContent(name1+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo.setDetails(name1+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
                        messageService.sendOrClose(messageVo);
                        // whsjm 刘永涛
                        MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(whsjm);
                        String name2 = memberManage1.getMemberName();
                        MessageVo messageVo1 = new MessageVo();
                        messageVo1.setId("A02");
                        messageVo1.setUserId(whsjm);
                        messageVo1.setType("3"); //風險
                        messageVo1.setReceiver(auditorId);
                        messageVo1.setType("3"); // 风险
                        messageVo1.setTitle("您有一个进度款支付项目已超额");
                        messageVo1.setSnsContent(name2+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo1.setContent(name2+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo1.setDetails(name2+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
                        messageService.sendOrClose(messageVo1);

                        // whzjh 罗均
                        MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
                        String name3 = memberManage2.getMemberName();
                        MessageVo messageVo2 = new MessageVo();
                        messageVo2.setId("A02");
                        messageVo2.setUserId(whzjh);
                        messageVo2.setType("3"); //風險
                        messageVo2.setReceiver(auditorId);
                        messageVo2.setPhone(auditorId);
                        messageVo2.setType("3"); // 风险
                        messageVo2.setTitle("您有一个进度款支付项目已超额");
                        messageVo2.setSnsContent(name3+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo2.setContent(name3+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo2.setDetails(name3+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
                        messageService.sendOrClose(messageVo2);

                        // whzjm 殷莉萍
                        MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
                        String name4 = memberManage3.getMemberName();
                        MessageVo messageVo3 = new MessageVo();
                        messageVo3.setId("A02");
                        messageVo3.setType("3"); //風險
                        messageVo3.setUserId(whzjm);
                        messageVo3.setReceiver(auditorId);
                        messageVo3.setPhone(auditorId);
                        messageVo3.setTitle("您有一个进度款支付项目已超额");
                        messageVo3.setSnsContent(name4+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo3.setContent(name4+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
                        messageVo3.setDetails(name4+"您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
                        messageService.sendOrClose(messageVo2);
                    }
                }


            }


//    }

    /**
     * 根据id查询进度款信息
     */
    @Override
    public BaseProjectVo seachProgressById(String id, UserInfo userInfo, String visaNum) {

//        userInfo = new UserInfo("user309",null,null,true);

        /*
        * //项目基本信息
        BaseProject project = new BaseProject();
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = new ProgressPaymentInformation();
        //审核表
        AuditInfo auditInfo = new AuditInfo();
        * */
        BaseProjectVo baseProjectVo = new BaseProjectVo();
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(id);

        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(paymentInformation.getBaseProjectId());

        // 查询审核信息的集合
        Example auditExample = new Example(AuditInfo.class);
        Example.Criteria criteria = auditExample.createCriteria();
        criteria.andEqualTo("baseProjectId", paymentInformation.getId());
        criteria.andEqualTo("status", "0");
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(auditExample);

        for (AuditInfo auditInfo : auditInfos) {
            if ("0".equals(auditInfo.getAuditResult())) {
                baseProjectVo.setAuditType(auditInfo.getAuditType());
            }
        }


        Example example = new Example(ApplicationInformation.class);
        example.createCriteria().andEqualTo("baseProjectId", baseProject.getId()).andEqualTo("delFlag","0");
        ApplicationInformation applicationInformation = applicationInformationDao.selectOneByExample(example);

        Example example1 = new Example(ProgressPaymentTotalPayment.class);
        example1.createCriteria().andEqualTo("progressPaymentId", id).andEqualTo("delFlag","0");
        ProgressPaymentTotalPayment totalPayment = progressPaymentTotalPaymentDao.selectOneByExample(example1);


        baseProjectVo.setId(id);
        baseProjectVo.setProgressPaymentStatus(baseProject.getProgressPaymentStatus());
        baseProjectVo.setManagementTable(baseProject.getManagementTable()); //后加的 管理表字段
        baseProjectVo.setProjectNum(baseProject.getProjectNum());
        baseProjectVo.setProjectName(baseProject.getProjectName());
        baseProjectVo.setApplicationNum(baseProject.getApplicationNum());
        baseProjectVo.setCeaNum(baseProject.getCeaNum());
        baseProjectVo.setDistrict(baseProject.getDistrict());
        baseProjectVo.setDesignCategory(baseProject.getDesignCategory());
        baseProjectVo.setConstructionUnit(baseProject.getConstructionUnit());
        baseProjectVo.setContacts(baseProject.getContacts());
        baseProjectVo.setContactNumber(baseProject.getContactNumber());
        baseProjectVo.setCustomerName(baseProject.getCustomerName());
        baseProjectVo.setSubject(baseProject.getSubject());
        baseProjectVo.setCustomerPhone(baseProject.getCustomerPhone());
        baseProjectVo.setConstructionOrganization(baseProject.getConstructionOrganization());
        baseProjectVo.setProjectNature(baseProject.getProjectNature());
        baseProjectVo.setProjectCategory(baseProject.getProjectCategory());
        baseProjectVo.setWaterAddress(baseProject.getWaterAddress());
        baseProjectVo.setWaterSupplyType(baseProject.getWaterSupplyType());
        baseProjectVo.setThisDeclaration(baseProject.getThisDeclaration());
        baseProjectVo.setAgent(baseProject.getAgent());
        baseProjectVo.setAgentPhone(baseProject.getAgentPhone());
        baseProjectVo.setApplicationDate(baseProject.getApplicationDate());
        baseProjectVo.setBusinessLocation(baseProject.getBusinessLocation());
        baseProjectVo.setBusinessTypes(baseProject.getBusinessTypes());
        baseProjectVo.setAB(baseProject.getAB());
        baseProjectVo.setWaterUse(baseProject.getWaterUse());
        baseProjectVo.setFireTableSize(baseProject.getFireTableSize());
        baseProjectVo.setClassificationCaliber(baseProject.getClassificationCaliber());
        baseProjectVo.setWaterMeterDiameter(baseProject.getWaterMeterDiameter());
        baseProjectVo.setSite(baseProject.getSite());
        baseProjectVo.setSystemNumber(baseProject.getSystemNumber());
        baseProjectVo.setProposer(baseProject.getProposer());
        baseProjectVo.setApplicationNumber(baseProject.getApplicationNumber());


        baseProjectVo.setRemarkes(applicationInformation.getRemarkes());

        if (visaNum.equals("1")){
            baseProjectVo.setCurrentPaymentInformation(paymentInformation.getCurrentPaymentInformation());
            baseProjectVo.setCumulativePaymentTimes(paymentInformation.getCumulativePaymentTimes());
            baseProjectVo.setCurrentPaymentRatio(paymentInformation.getCurrentPaymentRatio());
            baseProjectVo.setCurrentPeriodAccording(paymentInformation.getCurrentPeriodAccording());
            baseProjectVo.setContractAmount(paymentInformation.getContractAmount());
            baseProjectVo.setProjectType(paymentInformation.getProjectType());
            baseProjectVo.setReceivingTime(paymentInformation.getReceivingTime());
            baseProjectVo.setCompileTime(paymentInformation.getCompileTime());
            baseProjectVo.setOutsourcing(paymentInformation.getOutsourcing());
            baseProjectVo.setNameOfCostUnit(paymentInformation.getNameOfCostUnit());
            baseProjectVo.setContact(paymentInformation.getContact());
            baseProjectVo.setContactPhone(paymentInformation.getContactPhone());
            baseProjectVo.setAmountOutsourcing(paymentInformation.getAmountOutsourcing());
            baseProjectVo.setSituation(paymentInformation.getSituation());
            baseProjectVo.setRemarkes1(paymentInformation.getRemarkes());
        }

//        Example example2 = new Example(AuditInfo.class);
//        Example.Criteria criteria1 = example2.createCriteria();
//        criteria1.andEqualTo("baseProjectId", id);
//
//        List<AuditInfo> auditInfos1 = auditInfoDao.selectByExample(example2);

//        for (AuditInfo auditInfo : auditInfos1) {
//            if("0".equals(auditInfo.getAuditResult()) && ("0".equals(auditInfo.getAuditType()))){
//                baseProjectVo.setAuditNumber("1");
//            }else if("0".equals(auditInfo.getAuditResult()) && ("1".equals(auditInfo.getAuditType()))){
//                baseProjectVo.setAuditNumber("2");
//            }
//        }


        //查找审核数据
        Example auditInfoExample = new Example(AuditInfo.class);
        Example.Criteria criteria2 = auditInfoExample.createCriteria();
        criteria2.andEqualTo("baseProjectId", paymentInformation.getId());
//        criteria1.andEqualTo("auditType",'0');
        // 未审批
        criteria2.andEqualTo("auditResult", '0');
        criteria2.andEqualTo("auditorId", userInfo.getId());

        AuditInfo auditInfo = auditInfoDao.selectOneByExample(auditInfoExample);

        if (auditInfo!=null){
            if (auditInfo.getAuditType().equals("0") || auditInfo.getAuditType().equals("1") ||auditInfo.getAuditType().equals("4")){
                baseProjectVo.setAuditNumber("1");
            }else if(auditInfo.getAuditType().equals("2") || auditInfo.getAuditType().equals("3") ||auditInfo.getAuditType().equals("5")){
                baseProjectVo.setAuditNumber("2");
            }
        }
//        if (auditInfo != null) {
//            baseProjectVo.setAuditInfo(auditInfo);
//            // 0 代表一审，未审批
//            if ("0".equals(auditInfo.getAuditType())) {
//                baseProjectVo.setAuditNumber("0");
//            } else if ("1".equals(auditInfo.getAuditType())) {
//                baseProjectVo.setAuditNumber("1");
//            }
//        }
        return baseProjectVo;
    }

    //修改进度款
    @Override
    public void updateProgress(BaseProjectVo baseProject, UserInfo loginUser) {
//        loginUser = new UserInfo("user309",null,null,true);

        if (baseProject.getAmountOutsourcing().equals("")){
            baseProject.setAmountOutsourcing(null);
        }

        //项目基本信息
        Example example2 = new Example(BaseProject.class);
        example2.createCriteria().andEqualTo("projectNum", baseProject.getProjectNum());
        BaseProject project = baseProjectDao.selectOneByExample(example2);
        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(baseProject.getId());
        //审核表
        AuditInfo auditInfo = new AuditInfo();

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String format = sim.format(new Date());
        if (baseProject != null && !baseProject.equals("")) {
            if (baseProject.getAuditNumber() != null && !baseProject.getAuditNumber().equals("")) {
                //处理中
                if(project.getProgressPaymentStatus().equals("2")){



                    auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo.setBaseProjectId(baseProject.getId());
                    auditInfo.setAuditResult("0");
                    auditInfo.setAuditType("0");
                    auditInfo.setAuditorId(baseProject.getAuditorId());
                    auditInfo.setFounderId(loginUser.getId());
                    auditInfo.setStatus("0");
                    auditInfo.setCreateTime(format);
                    auditInfoDao.insertSelective(auditInfo);


                    progressPaymentInformationDao.updateByPrimaryKeySelective(paymentInformation);


                    Example example = new Example(ProgressPaymentTotalPayment.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",project.getId());
                    c.andEqualTo("delFlag","0");
                    ProgressPaymentTotalPayment progressPaymentTotalPayment = progressPaymentTotalPaymentDao.selectOneByExample(example);
                    progressPaymentTotalPayment.setTotalPaymentAmount(progressPaymentTotalPayment.getTotalPaymentAmount().add(baseProject.getCurrentPaymentInformation()));
                    progressPaymentTotalPayment.setCumulativeNumberPayment(progressPaymentTotalPayment.getCumulativeNumberPayment().add(new BigDecimal(baseProject.getCumulativePaymentTimes())));
                    progressPaymentTotalPayment.setAccumulativePaymentProportion(Double.parseDouble(progressPaymentTotalPayment.getAccumulativePaymentProportion())+Double.parseDouble(baseProject.getCurrentPaymentRatio())+"");
                    progressPaymentTotalPaymentDao.updateByPrimaryKeySelective(progressPaymentTotalPayment);

                    project.setProgressPaymentStatus("1");;
                    baseProjectDao.updateByPrimaryKeySelective(project);
                    //待确认
                }else if(project.getProgressPaymentStatus().equals("4")){
                    auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo.setBaseProjectId(baseProject.getId());
                    auditInfo.setAuditResult("0");
                    auditInfo.setAuditType("2");
                    auditInfo.setAuditorId(baseProject.getAuditorId());
                    auditInfo.setFounderId(loginUser.getId());
                    auditInfo.setStatus("0");
                    auditInfo.setCreateTime(format);
                    auditInfoDao.insertSelective(auditInfo);

                    project.setProgressPaymentStatus("1");;
                    baseProjectDao.updateByPrimaryKeySelective(project);
                    //未通过
                }else if(project.getProgressPaymentStatus().equals("3")){
                    Example example = new Example(AuditInfo.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",baseProject.getId());
                    c.andEqualTo("status","0");
                    c.andEqualTo("auditResult","2");
                    AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditOpinion("");
                    auditInfo1.setAuditTime("");
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo1);

                    project.setProgressPaymentStatus("1");;
                    baseProjectDao.updateByPrimaryKeySelective(project);
                    //进行中
                }else if(project.getProgressPaymentStatus().equals("5")){

                    ProgressPaymentInformation progressPaymentInformation1 = progressPaymentInformationDao.selectByPrimaryKey(baseProject.getId());
                    progressPaymentInformation1.setDelFlag("2");
                    progressPaymentInformationDao.updateByPrimaryKeySelective(progressPaymentInformation1);


                    ProgressPaymentInformation progressPaymentInformation = new ProgressPaymentInformation();
                    progressPaymentInformation.setId(UUID.randomUUID().toString().replace("-",""));
                    progressPaymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
                    progressPaymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
                    progressPaymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
                    progressPaymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
                    progressPaymentInformation.setContractAmount(baseProject.getContractAmount());
                    progressPaymentInformation.setProjectType(baseProject.getProjectType());
                    progressPaymentInformation.setReceivingTime(baseProject.getReceivingTime());
                    progressPaymentInformation.setCompileTime(baseProject.getCompileTime());
                    progressPaymentInformation.setOutsourcing(baseProject.getOutsourcing());
                    progressPaymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
                    progressPaymentInformation.setContact(baseProject.getContact());
                    progressPaymentInformation.setContactPhone(baseProject.getContactPhone());
                    progressPaymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
                    progressPaymentInformation.setSituation(baseProject.getSituation());
                    progressPaymentInformation.setRemarkes(baseProject.getRemarkes());
                    progressPaymentInformation.setBaseProjectId(project.getId());
                    progressPaymentInformation.setCreateTime(format);
                    progressPaymentInformation.setFounderId(loginUser.getId());
                    progressPaymentInformation.setDelFlag("0");
                    progressPaymentInformation.setChangeNum(paymentInformation.getChangeNum()+1);
                    progressPaymentInformationDao.insertSelective(progressPaymentInformation);


                    Example example = new Example(ProgressPaymentTotalPayment.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",project.getId());
                    c.andEqualTo("delFlag","0");
                    ProgressPaymentTotalPayment progressPaymentTotalPayment = progressPaymentTotalPaymentDao.selectOneByExample(example);
                    if (progressPaymentTotalPayment.getTotalPaymentAmount()==null){
                        progressPaymentTotalPayment.setTotalPaymentAmount(progressPaymentInformation.getCurrentPaymentInformation());
                    }else{
                        progressPaymentTotalPayment.setTotalPaymentAmount(progressPaymentTotalPayment.getTotalPaymentAmount().add(progressPaymentInformation.getCurrentPaymentInformation()));

                    }
                    if (progressPaymentTotalPayment.getCumulativeNumberPayment()==null){
                        progressPaymentTotalPayment.setCumulativeNumberPayment(new BigDecimal(progressPaymentInformation.getCumulativePaymentTimes()));
                    }else {
                        progressPaymentTotalPayment.setCumulativeNumberPayment(progressPaymentTotalPayment.getCumulativeNumberPayment().add(new BigDecimal(progressPaymentInformation.getCumulativePaymentTimes())));
                    }
                    if (progressPaymentTotalPayment.getAccumulativePaymentProportion()==null){
                        progressPaymentTotalPayment.setAccumulativePaymentProportion(Double.parseDouble(progressPaymentInformation.getCurrentPaymentRatio())+"");
                    }else{
                        progressPaymentTotalPayment.setAccumulativePaymentProportion(Double.parseDouble(progressPaymentTotalPayment.getAccumulativePaymentProportion())+Double.parseDouble(progressPaymentInformation.getCurrentPaymentRatio())+"");
                    }
                    progressPaymentTotalPaymentDao.updateByPrimaryKeySelective(progressPaymentTotalPayment);

                    auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo.setBaseProjectId(progressPaymentInformation.getId());
                    auditInfo.setAuditResult("0");
                    auditInfo.setAuditType("0");
                    auditInfo.setAuditorId(baseProject.getAuditorId());
                    auditInfo.setFounderId(loginUser.getId());
                    auditInfo.setStatus("0");
                    auditInfo.setCreateTime(format);
                    auditInfoDao.insertSelective(auditInfo);

                    project.setProgressPaymentStatus("1");
                    baseProjectDao.updateByPrimaryKeySelective(project);
                    return;
                }
            }else if(project.getProgressPaymentStatus().equals("5")){

                ProgressPaymentInformation progressPaymentInformation1 = progressPaymentInformationDao.selectByPrimaryKey(baseProject.getId());
                progressPaymentInformation1.setDelFlag("2");
                progressPaymentInformationDao.updateByPrimaryKeySelective(progressPaymentInformation1);




                ProgressPaymentInformation progressPaymentInformation = new ProgressPaymentInformation();
                progressPaymentInformation.setId(UUID.randomUUID().toString().replace("-",""));
                progressPaymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
                progressPaymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
                progressPaymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
                progressPaymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
                progressPaymentInformation.setContractAmount(baseProject.getContractAmount());
                progressPaymentInformation.setProjectType(baseProject.getProjectType());
                progressPaymentInformation.setReceivingTime(baseProject.getReceivingTime());
                progressPaymentInformation.setCompileTime(baseProject.getCompileTime());
                progressPaymentInformation.setOutsourcing(baseProject.getOutsourcing());
                progressPaymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
                progressPaymentInformation.setContact(baseProject.getContact());
                progressPaymentInformation.setContactPhone(baseProject.getContactPhone());
                progressPaymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
                progressPaymentInformation.setSituation(baseProject.getSituation());
                progressPaymentInformation.setRemarkes(baseProject.getRemarkes());
                progressPaymentInformation.setBaseProjectId(project.getId());
                progressPaymentInformation.setCreateTime(format);
                progressPaymentInformation.setFounderId(loginUser.getId());
                progressPaymentInformation.setDelFlag("0");
//                progressPaymentInformation.setChangeNum(paymentInformation.getChangeNum()+1);
                progressPaymentInformationDao.insertSelective(progressPaymentInformation);


//                Example example = new Example(ProgressPaymentTotalPayment.class);
//                Example.Criteria c = example.createCriteria();
//                c.andEqualTo("baseProjectId",project.getId());
//                c.andEqualTo("delFlag","0");
//                ProgressPaymentTotalPayment progressPaymentTotalPayment = progressPaymentTotalPaymentDao.selectOneByExample(example);
//                progressPaymentTotalPayment.setTotalPaymentAmount(progressPaymentTotalPayment.getTotalPaymentAmount().add(progressPaymentInformation.getCurrentPaymentInformation()));
//                progressPaymentTotalPayment.setCumulativeNumberPayment(progressPaymentTotalPayment.getCumulativeNumberPayment().add(new BigDecimal(progressPaymentInformation.getCumulativePaymentTimes())));
//                progressPaymentTotalPayment.setAccumulativePaymentProportion(Double.parseDouble(progressPaymentTotalPayment.getAccumulativePaymentProportion())+Double.parseDouble(progressPaymentInformation.getCurrentPaymentRatio())+"");
//                progressPaymentTotalPaymentDao.updateByPrimaryKeySelective(progressPaymentTotalPayment);

//                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
//                auditInfo.setBaseProjectId(progressPaymentInformation.getId());
//                auditInfo.setAuditResult("0");
//                auditInfo.setAuditType("0");
//                auditInfo.setAuditorId(baseProject.getAuditorId());
//                auditInfo.setFounderId(loginUser.getId());
//                auditInfo.setStatus("0");
//                auditInfo.setCreateTime(format);
//                auditInfoDao.insertSelective(auditInfo);

                project.setProgressPaymentStatus("2");;
                baseProjectDao.updateByPrimaryKeySelective(project);
                return;
            }

            information.setRemarkes(baseProject.getRemarkes());
            Example example = new Example(ApplicationInformation.class);
            Example.Criteria c1 = example.createCriteria();
                    c1.andEqualTo("baseProjectId",project.getId());
                    c1.andEqualTo("delFlag","0");
            ApplicationInformation applicationInformation = applicationInformationDao.selectOneByExample(example);
            information.setId(applicationInformation.getId());
            applicationInformationDao.updateByPrimaryKeySelective(information);
//            applicationInformationDao.updateByExampleSelective(information, example);



            paymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
            paymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
            paymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
            paymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
            paymentInformation.setContractAmount(baseProject.getContractAmount());
            paymentInformation.setProjectType(baseProject.getProjectType());
            paymentInformation.setReceivingTime(baseProject.getReceivingTime());
            paymentInformation.setCompileTime(baseProject.getCompileTime());
            paymentInformation.setOutsourcing(baseProject.getOutsourcing());
            paymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
            paymentInformation.setContact(baseProject.getContact());
            paymentInformation.setContactPhone(baseProject.getContactPhone());
            paymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
            paymentInformation.setSituation(baseProject.getSituation());
            paymentInformation.setRemarkes(baseProject.getRemarkes());
            Example example3 = new Example(ProgressPaymentInformation.class);
            example3.createCriteria().andEqualTo("id", baseProject.getId()).andEqualTo("delFlag","0");
            ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectOneByExample(example3);
            paymentInformation.setId(progressPaymentInformation.getId());
            progressPaymentInformationDao.updateByPrimaryKeySelective(paymentInformation);
//            progressPaymentInformationDao.updateByExampleSelective(paymentInformation, example3);

            // 本期进度款支付信息列表文件
            List<FileInfo> freignAndType = fileInfoMapper.findByFreignAndType(baseProject.getKey(), baseProject.getType1());
            for (FileInfo fileInfo : freignAndType) {
                fileInfo.setPlatCode(paymentInformation.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }


            //消息通知
            String auditorId = baseProject.getAuditorId();//审核id
            String projectName = baseProject.getProjectName();//项目名称

            //累计支付金额
            BigDecimal totalAmount = baseProject.getCurrentPaymentInformation();
            //合同金额
            BigDecimal contractAmount = baseProject.getContractAmount();
            //如果累计支付金额大于合同金额的70%就发邮件和短信
//            if (totalAmount.compareTo(contractAmount) == 1) {
//                //whsjh 朱让宁
//                MemberManage memberManage = memberManageDao.selectByPrimaryKey(whsjh);
//                String name1 = memberManage.getMemberName();
//                MessageVo messageVo = new MessageVo();
//                messageVo.setId("A02");
//                messageVo.setUserId(whsjh);
//                messageVo.setReceiver(auditorId);
//                messageVo.setTitle("您有一个进度款支付项目已超额！");
//                messageVo.setSnsContent(name1 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo.setContent(name1 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo.setDetails(name1 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
////                messageService.sendOrClose(messageVo);
//                // whsjm 刘永涛
//                MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(whsjm);
//                String name2 = memberManage1.getMemberName();
//                MessageVo messageVo1 = new MessageVo();
//                messageVo1.setId("A02");
//                messageVo1.setUserId(whsjm);
//                messageVo1.setReceiver(auditorId);
//                messageVo1.setTitle("您有一个进度款支付项目已超额");
//                messageVo1.setSnsContent(name2 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo1.setContent(name2 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo1.setDetails(name2 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
//                messageService.sendOrClose(messageVo1);
//
//                // whzjh 罗均
//                MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(whzjh);
//                String name3 = memberManage2.getMemberName();
//                MessageVo messageVo2 = new MessageVo();
//                messageVo2.setId("A02");
//                messageVo2.setUserId(whzjh);
//                messageVo2.setReceiver(auditorId);
//                messageVo2.setPhone(auditorId);
//                messageVo2.setTitle("您有一个进度款支付项目已超额");
//                messageVo2.setSnsContent(name3 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo2.setContent(name3 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo2.setDetails(name3 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
//                messageService.sendOrClose(messageVo2);
//
//                // whzjm 殷莉萍
//                MemberManage memberManage3 = memberManageDao.selectByPrimaryKey(whzjm);
//                String name4 = memberManage3.getMemberName();
//                MessageVo messageVo3 = new MessageVo();
//                messageVo3.setId("A02");
//                messageVo3.setUserId(whzjm);
//                messageVo3.setReceiver(auditorId);
//                messageVo3.setPhone(auditorId);
//                messageVo3.setTitle("您有一个进度款支付项目已超额");
//                messageVo3.setSnsContent(name4 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo3.setContent(name4 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时登录造价管理平台查看详情！");
//                messageVo3.setDetails(name4 + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
//                messageService.sendOrClose(messageVo2);
//
//            } else {
//                // 站内信
//                MessageVo messageVo4 = new MessageVo();
//                messageVo4.setId("A10");
//                messageVo4.setUserId(auditorId);
//                messageVo4.setTitle("您有一个进度款项目待审批！");
//                messageVo4.setDetails(loginUser.getUsername() + "您好！您提交的【" + projectName + "】的进度款支付项目进度款支付金额已达到合同金额的70%以上，请及时查看详情！");
//                //调用消息Service
//                messageService.sendOrClose(messageVo4);
//            }
        }
    }



    // 未通過編輯
    public void updateProgressPayment(BaseProjectVo baseProject) {

        //申请信息
        ApplicationInformation information = new ApplicationInformation();
        //进度款累计支付信息
        ProgressPaymentTotalPayment payment = new ProgressPaymentTotalPayment();
        //本期进度款支付信息
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(baseProject.getId());
        //审核表
        AuditInfo auditInfo = new AuditInfo();

        //项目基本信息
        BaseProject project = baseProjectDao.selectByPrimaryKey(paymentInformation.getBaseProjectId());

        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId", paymentInformation.getId());

        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);

        for (AuditInfo info : auditInfos) {
            // 不通過,一審
            if ("2".equals(info.getAuditResult()) && "0".equals(info.getAuditType())) {

                info.setAuditResult("0");
                info.setBaseProjectId(paymentInformation.getId());
                String updateDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());

                info.setUpdateTime(updateDate);

                project.setProgressPaymentStatus("1");

                auditInfoDao.updateByPrimaryKeySelective(info);
                baseProjectDao.updateByPrimaryKeySelective(project);

            } else if ("2".equals(info.getAuditResult()) && "1".equals(info.getAuditType())) {
                auditInfoDao.deleteByPrimaryKey(info);

                Example example1 = new Example(AuditInfo.class);

                Example.Criteria criteria1 = example1.createCriteria();

                criteria1.andEqualTo("baseProjectId", paymentInformation.getId()).andEqualTo("auditType", "0");

                AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example1);

                auditInfo1.setAuditResult("0");

                auditInfoDao.updateByPrimaryKeySelective(auditInfo1);
                project.setProgressPaymentStatus("1");

                baseProjectDao.updateByPrimaryKeySelective(project);
            }
        }
        information.setRemarkes(baseProject.getRemarkes());
        Example example3 = new Example(ApplicationInformation.class);
        example3.createCriteria().andEqualTo("progressPaymentId", paymentInformation.getId());
        applicationInformationDao.updateByExampleSelective(information, example3);


        payment.setTotalPaymentAmount(baseProject.getTotalPaymentAmount());
        payment.setCumulativeNumberPayment(baseProject.getCumulativeNumberPayment());
        payment.setAccumulativePaymentProportion(baseProject.getAccumulativePaymentProportion());
        Example example1 = new Example(ProgressPaymentTotalPayment.class);
        example1.createCriteria().andEqualTo("progressPaymentId", paymentInformation.getId());
        progressPaymentTotalPaymentDao.updateByExampleSelective(payment, example1);

        paymentInformation.setCurrentPaymentInformation(baseProject.getCurrentPaymentInformation());
        paymentInformation.setCumulativePaymentTimes(baseProject.getCumulativePaymentTimes());
        paymentInformation.setCurrentPaymentRatio(baseProject.getCurrentPaymentRatio());
        paymentInformation.setCurrentPeriodAccording(baseProject.getCurrentPeriodAccording());
        paymentInformation.setContractAmount(baseProject.getContractAmount());
        paymentInformation.setProjectType(baseProject.getProjectType());
        paymentInformation.setReceivingTime(baseProject.getReceivingTime());
        paymentInformation.setCompileTime(baseProject.getCompileTime());
        paymentInformation.setOutsourcing(baseProject.getOutsourcing());
        paymentInformation.setNameOfCostUnit(baseProject.getNameOfCostUnit());
        paymentInformation.setContact(baseProject.getContact());
        paymentInformation.setContactPhone(baseProject.getContactPhone());
        paymentInformation.setAmountOutsourcing(baseProject.getAmountOutsourcing());
        paymentInformation.setSituation(baseProject.getSituation());
        paymentInformation.setRemarkes(baseProject.getRemarkes());
        Example example4 = new Example(ProgressPaymentInformation.class);
        example4.createCriteria().andEqualTo("progressPaymentId", paymentInformation.getId());
        progressPaymentInformationDao.updateByExampleSelective(paymentInformation, example4);

    }


    @Override
    public void batchReview(BatchReviewVo batchReviewVo,UserInfo loginUser) {
//        loginUser = new UserInfo("user320",null,null,true);
        //id
        String id = loginUser.getId();
//        String id = "user320";
        //姓名
//        String username = "zaojia3";
        String username = loginUser.getUsername();

        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            Example example = new Example(AuditInfo.class);
            example.createCriteria().andEqualTo("baseProjectId", s).andEqualTo("auditResult", "0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

            ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectByPrimaryKey(s);
            String baseProjectId = progressPaymentInformation.getBaseProjectId();

            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);


            if (batchReviewVo.getAuditResult().equals("1")) {
                if (auditInfo.getAuditType().equals("0")) {
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(s);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
                    auditInfo1.setFounderId(loginUser.getId());
                    auditInfo1.setCreateTime(format);
                    auditInfo1.setStatus("0");




                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id",progressPaymentInformation.getFounderId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    //1芜湖
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                        //吴江
                    }else if (memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }

                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("1")) {
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(s);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("4");
                    auditInfo1.setFounderId(loginUser.getId());
                    auditInfo1.setCreateTime(format);
                    auditInfo1.setStatus("0");



                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                     cc.andEqualTo("id",progressPaymentInformation.getFounderId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    //1芜湖
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                        //吴江
                    }else if (memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }

                    auditInfoDao.insertSelective(auditInfo1);
                }else if(auditInfo.getAuditType().equals("4")){
                    auditInfo.setAuditResult("1");
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    baseProject.setProgressPaymentStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }else if(auditInfo.getAuditType().equals("2")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(s);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("3");
                    auditInfo1.setFounderId(loginUser.getId());
                    auditInfo1.setCreateTime(format);
                    auditInfo1.setStatus("0");




                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                     cc.andEqualTo("id",progressPaymentInformation.getFounderId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    //1芜湖
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                        //吴江
                    }else if (memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }

                    auditInfoDao.insertSelective(auditInfo1);
                }else if(auditInfo.getAuditType().equals("3")){
                    auditInfo.setAuditResult("1");
                    Date date = new Date();
                    String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                    auditInfo.setAuditTime(format);
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(s);
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("5");
                    auditInfo1.setFounderId(loginUser.getId());
                    auditInfo1.setCreateTime(format);
                    auditInfo1.setStatus("0");



                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                     cc.andEqualTo("id",progressPaymentInformation.getFounderId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    //1芜湖
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                        //吴江
                    }else if (memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }

                    auditInfoDao.insertSelective(auditInfo1);
                }else if(auditInfo.getAuditType().equals("5")){
                    auditInfo.setAuditResult("1");
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                    baseProject.setProgressPaymentStatus("5");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }
//                //消息通知
//                //项目名称
//                String projectName = baseProject.getProjectName();
//                //通过审核人id查找成员姓名
//                MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
//                String name = memberManage.getMemberName();
//                //如果通过的话发送消息
//                MessageVo messageVo = new MessageVo();
//                messageVo.setId(id);
//                messageVo.setTitle("您有一个进度款支付项目审批已通过！");
//                messageVo.setDetails(username+"您好！您提交的【"+projectName+"】的进度款支付项目【"+name+"】已审批通过！");
//                messageService.sendOrClose(messageVo);
//
            } else if (batchReviewVo.getAuditResult().equals("2")) {
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                Date date = new Date();
                String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
                auditInfo.setAuditTime(format);
                baseProject.setProgressPaymentStatus("3");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                //成员姓名
//                String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
//                //如果不通过发送消息
//                MessageVo messageVo1 = new MessageVo();
//                messageVo1.setId(id);
//                messageVo1.setTitle("您有一个进度款支付项目审批未通过！");
//                messageVo1.setDetails(username+"您好！您提交的【"+baseProject.getProjectName()+"】的进度款支付项目【"+name+"】未通过,请及时查看详情！");
//                messageService.sendOrClose(messageVo1);
            }
        }

    }


    @Override
    public List<BaseProject> findBaseProject(String name) {
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        if (name.equals(" ") || name == null) {
            List<BaseProject> baseProjects = baseProjectDao.selectAll();
            return baseProjects;
        }
        c.andLike("projectNum", "%" + name + "%");
        List<BaseProject> baseProjects = baseProjectDao.selectByExample(example);
        return baseProjects;
    }

    @Override
    public List<BaseProject> findAllBaseProject(pageVo pageVo) {
        return baseProjectDao.findAllBaseProject(pageVo);
    }

    @Override
    public BaseProject findById(String id) {

        Budgeting budgeting = budgetingMapper.selectByPrimaryKey(id);
        BaseProject baseProject = new BaseProject();
        if (budgeting != null) {
            String baseProjectId = budgeting.getBaseProjectId();
            baseProject = baseProjectDao.findTrackBaseProjectId(baseProjectId);
        } else {
            baseProject = baseProjectDao.findTrackBaseProjectId(id);
        }
        if (baseProject != null) {
            ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectById(baseProject.getConstructionUnit());
            if (constructionUnitManagement != null) {
                baseProject.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
            }
        }

        return baseProject;
    }

    @Override
    public BaseProject findBaseProjectById(String id) {
        Budgeting budgeting = budgetingMapper.selectByPrimaryKey(id);
        BaseProject baseProject = new BaseProject();
        if (budgeting != null) {
            String baseProjectId = budgeting.getBaseProjectId();
//            baseProject = baseProjectDao.findBaseProjectId(baseProjectId);
            baseProject = baseProjectDao.findTrackBaseProjectId(baseProjectId);
        } else {
//            baseProject = baseProjectDao.findBaseProjectId(id);
            baseProject = baseProjectDao.findTrackBaseProjectId(id);
        }
        return baseProject;
    }

    @Override
    public List<AuditChekedVo> auditMaintenanceChek(String id) {
        return auditInfoDao.auditMaintenanceChek(id);
    }

    @Override
    public List<AuditChekedVo> auditAgainMaintenanceChek(String id) {
        return auditInfoDao.auditAgainMaintenanceChek(id);
    }

    @Override
    public List<ProgressPaymentInformation> findTotalList(String baseId) {
        Example example = new Example(ProgressPaymentInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("delFlag","2");
        c.andEqualTo("baseProjectId",baseId);
        List<ProgressPaymentInformation> progressPaymentInformations = progressPaymentInformationDao.selectByExample(example);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if (! baseProject.getProgressPaymentStatus().equals("2")){
            Example example1 = new Example(ProgressPaymentInformation.class);
            Example.Criteria c2 = example1.createCriteria();
            c2.andEqualTo("baseProjectId",baseId);
            c2.andEqualTo("delFlag","0");
            ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectOneByExample(example1);
            progressPaymentInformations.add(progressPaymentInformation);
        }
        for (ProgressPaymentInformation progressPaymentInformation : progressPaymentInformations) {
            if (progressPaymentInformation.getProjectType().equals("1")){
                progressPaymentInformation.setProjectType("合同内进度款支付");
            }else if(progressPaymentInformation.getProjectType().equals("2")){
                progressPaymentInformation.setProjectType("合同外进度款支付");
            }
        }

        return progressPaymentInformations;
    }

    @Override
    public TotalVo findTotal(String baseId) {
        TotalVo totalVo = new TotalVo();

        Example example = new Example(ProgressPaymentInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("delFlag","2");
        c.andEqualTo("baseProjectId",baseId);
        List<ProgressPaymentInformation> progressPaymentInformations = progressPaymentInformationDao.selectByExample(example);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if (! baseProject.getProgressPaymentStatus().equals("2")){
            Example example1 = new Example(ProgressPaymentInformation.class);
            Example.Criteria c2 = example1.createCriteria();
            c2.andEqualTo("baseProjectId",baseId);
            c2.andEqualTo("delFlag","0");

            ProgressPaymentInformation progressPaymentInformation = progressPaymentInformationDao.selectOneByExample(example1);
            if (progressPaymentInformation!=null){
                progressPaymentInformations.add(progressPaymentInformation);
            }
        }

        BigDecimal bigDecimal = new BigDecimal(0);
        BigDecimal bigDecimal1 = new BigDecimal(0);
        int pro = 0;
        for (ProgressPaymentInformation progressPaymentInformation : progressPaymentInformations) {
           if (progressPaymentInformation.getCurrentPaymentInformation()!=null){
               bigDecimal = bigDecimal.add(progressPaymentInformation.getCurrentPaymentInformation());
           }
           if (progressPaymentInformation.getCumulativePaymentTimes()!=null){
               bigDecimal1 = bigDecimal1.add(new BigDecimal(progressPaymentInformation.getCumulativePaymentTimes()));
           }
           if (progressPaymentInformation.getCurrentPaymentRatio()!=null){
               if (pro<=progressPaymentInformation.getChangeNum()){
                   pro = progressPaymentInformation.getChangeNum();
               }
           }
        }
        totalVo.setTotalPaymentAmount(bigDecimal);
        totalVo.setCumulativeNumberPayment(bigDecimal1);
        totalVo.setAccumulativePaymentProportion(pro+"");

        return totalVo;
    }

    @Override
    public List<AuditChekedVo> auditChekAccount() {

        return null;
    }

    @Override
    public List<AuditChekedVo> findcheckAll(String num, String id) {
        List<AuditChekedVo> auditChekedVos = auditChek(id);
        if ("1".equals(num)){
            for (int i = 0; i < auditChekedVos.size(); i++) {
                if (auditChekedVos.get(i).getAuditType().equals("0") || auditChekedVos.get(i).getAuditType().equals("1") || auditChekedVos.get(i).getAuditType().equals("4")){

                }else{
                    auditChekedVos.remove(i);
                    i--;
                }
            }
        }
        if ("2".equals(num)){
            for (int i = 0; i < auditChekedVos.size(); i++) {
                if (auditChekedVos.get(i).getAuditType().equals("0") || auditChekedVos.get(i).getAuditType().equals("1") || auditChekedVos.get(i).getAuditType().equals("4")){
                    auditChekedVos.remove(i);
                    i--;
                }else{

                }
            }
        }
        return auditChekedVos;
    }

    @Override
    public NumberVo NumberItems() {
        NumberVo numberVo = new NumberVo();
        int i = baseProjectDao.selectCountByExample(null);
        numberVo.setTotalNumberOfProjects(i);

        int ii = 0;
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("desginStatus", "1");
        int i1 = baseProjectDao.selectCountByExample(example);
        ii += i1;

        Example example2 = new Example(BaseProject.class);
        Example.Criteria c2 = example2.createCriteria();
        c2.andEqualTo("budgetStatus", "1");
        int i2 = baseProjectDao.selectCountByExample(example2);
        ii += i2;

        Example example3 = new Example(BaseProject.class);
        Example.Criteria c3 = example3.createCriteria();
        c3.andEqualTo("trackStatus", "1");
        int i3 = baseProjectDao.selectCountByExample(example3);
        ii += i3;


        Example example5 = new Example(BaseProject.class);
        Example.Criteria c5 = example5.createCriteria();
        c5.andEqualTo("visaStatus", "1");
        int i5 = baseProjectDao.selectCountByExample(example5);
        ii += i5;

        Example example6 = new Example(BaseProject.class);
        Example.Criteria c6 = example6.createCriteria();
        c6.andEqualTo("progressPaymentStatus", "1");
        int i6 = baseProjectDao.selectCountByExample(example6);
        ii += i6;

        Example example7 = new Example(BaseProject.class);
        Example.Criteria c7 = example7.createCriteria();
        c7.andEqualTo("settleAccountsStatus", "1");
        int i7 = baseProjectDao.selectCountByExample(example7);
        ii += i7;
        numberVo.setTotalNumberOfApproval(ii);


        int iii = 0;
        Example aexample = new Example(BaseProject.class);
        Example.Criteria ac = aexample.createCriteria();
        ac.andEqualTo("desginStatus", "2");
        int ai1 = baseProjectDao.selectCountByExample(aexample);
        iii += ai1;

        Example aexample2 = new Example(BaseProject.class);
        Example.Criteria ac2 = aexample2.createCriteria();
        ac2.andEqualTo("budgetStatus", "2");
        int ai2 = baseProjectDao.selectCountByExample(aexample2);
        iii += ai2;

        Example aexample3 = new Example(BaseProject.class);
        Example.Criteria ac3 = aexample3.createCriteria();
        ac3.andEqualTo("trackStatus", "3");
        int ai3 = baseProjectDao.selectCountByExample(aexample3);
        iii += ai3;


        Example aexample5 = new Example(BaseProject.class);
        Example.Criteria ac5 = aexample5.createCriteria();
        ac5.andEqualTo("visaStatus", "5");
        int ai5 = baseProjectDao.selectCountByExample(aexample5);
        iii += ai5;

        Example aexample6 = new Example(BaseProject.class);
        Example.Criteria ac6 = aexample6.createCriteria();
        ac6.andEqualTo("progressPaymentStatus", "2");
        int ai6 = baseProjectDao.selectCountByExample(aexample6);
        iii += ai6;

        Example aexample7 = new Example(BaseProject.class);
        Example.Criteria ac7 = aexample7.createCriteria();
        ac7.andEqualTo("settleAccountsStatus", "2");
        int ai7 = baseProjectDao.selectCountByExample(aexample7);
        iii += ai7;
        numberVo.setProjectTotal(iii);


        int iiii = 0;
        Example cexample = new Example(BaseProject.class);
        Example.Criteria cc = cexample.createCriteria();
        cc.andEqualTo("desginStatus", "2");
        int ci1 = baseProjectDao.selectCountByExample(cexample);
        iiii += ci1;

        Example cexample2 = new Example(BaseProject.class);
        Example.Criteria cc2 = cexample2.createCriteria();
        cc2.andEqualTo("budgetStatus", "2");
        int ci2 = baseProjectDao.selectCountByExample(cexample2);
        iiii += ci2;

        Example cexample3 = new Example(BaseProject.class);
        Example.Criteria cc3 = cexample3.createCriteria();
        cc3.andEqualTo("trackStatus", "3");
        int ci3 = baseProjectDao.selectCountByExample(cexample3);
        iiii += ci3;


        Example cexample5 = new Example(BaseProject.class);
        Example.Criteria cc5 = cexample5.createCriteria();
        cc5.andEqualTo("visaStatus", "5");
        int ci5 = baseProjectDao.selectCountByExample(cexample5);
        iiii += ci5;

        Example cexample6 = new Example(BaseProject.class);
        Example.Criteria cc6 = cexample6.createCriteria();
        cc6.andEqualTo("progressPaymentStatus", "2");
        int ci6 = baseProjectDao.selectCountByExample(aexample6);
        iiii += ci6;

        Example cexample7 = new Example(BaseProject.class);
        Example.Criteria cc7 = aexample7.createCriteria();
        cc7.andEqualTo("settleAccountsStatus", "2");
        int ci7 = baseProjectDao.selectCountByExample(cexample7);
        iiii += ci7;
        numberVo.setTotalNumberOfCompleted(iiii);


        return numberVo;
    }

    @Override
    public PageInfo<ProgressListVo> searchAllProgress(PageVo pageVo) {
//       searchAllProgress
        //待审核领导
        if (pageVo.getProgressStatus().equals("1")){
            if (pageVo.getUid().equals(whzjh) || pageVo.getUid().equals(whzjm) || pageVo.getUid().equals(wjzjh)){
                List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressLeader(pageVo);
                for (ProgressListVo progressListVo : list) {
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",progressListVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage !=null){
                        progressListVo.setCurrentHandler(memberManage.getMemberName());
                    }
                    TotalVo total = findTotal(progressListVo.getBaseId());
                    progressListVo.setTotalPaymentAmount(total.getTotalPaymentAmount());
                    progressListVo.setCumulativeNumberPayment(total.getCumulativeNumberPayment());
                    progressListVo.setAccumulativePaymentProportion(total.getAccumulativePaymentProportion());
                }
                return new PageInfo<ProgressListVo>(list);
                //普通员工
            }else {
                List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressStaff(pageVo);
                for (ProgressListVo progressListVo : list) {
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",progressListVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    if (memberManage !=null){
                        progressListVo.setCurrentHandler(memberManage.getMemberName());
                    }
                    TotalVo total = findTotal(progressListVo.getBaseId());
                    progressListVo.setTotalPaymentAmount(total.getTotalPaymentAmount());
                    progressListVo.setCumulativeNumberPayment(total.getCumulativeNumberPayment());
                    progressListVo.setAccumulativePaymentProportion(total.getAccumulativePaymentProportion());
                }
                return new PageInfo<ProgressListVo>(list);
            }
        }
        //处理中
        if (pageVo.getProgressStatus().equals("2")){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressProcessed(pageVo);
            return new PageInfo<ProgressListVo>(list);
        }
        //未通过
        if (pageVo.getProgressStatus().equals("3")){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressProcessed(pageVo);
            return new PageInfo<ProgressListVo>(list);
        }
        //待确认
        if (pageVo.getProgressStatus().equals("4")){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressProcessed(pageVo);
            return new PageInfo<ProgressListVo>(list);
        }
        //进行中
        if (pageVo.getProgressStatus().equals("5")){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressSueecss(pageVo);
            for (ProgressListVo progressListVo : list) {
                if(progressListVo.getProgressPaymentStatus().equals("进行中")){
                    if (progressListVo.getFounderId().equals(pageVo.getUid())){
                        progressListVo.setShowUpdate("3");
                    }
                }
            }
            return new PageInfo<ProgressListVo>(list);
        }
        //已完成
        if (pageVo.getProgressStatus().equals("6")){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressSueecss(pageVo);
            return new PageInfo<ProgressListVo>(list);
        }
        //全部
        if (pageVo.getProgressStatus().equals("") || pageVo.getProgressStatus() == null){
            List<ProgressListVo> list = progressPaymentInformationDao.searchAllProgressProcessed(pageVo);
            for (ProgressListVo progressListVo : list) {
                if (progressListVo.getProgressPaymentStatus().equals("处理中")){
                    progressListVo.setShowUpdate("1");
                }else if (progressListVo.getProgressPaymentStatus().equals("待确认")){
                    progressListVo.setShowUpdate("2");
                }else if(progressListVo.getProgressPaymentStatus().equals("进行中")){
                    if (progressListVo.getFounderId().equals(pageVo.getUid())){
                        progressListVo.setShowUpdate("3");
                    }
                }
            }
            return new PageInfo<ProgressListVo>(list);
        }

        return new PageInfo<ProgressListVo>();
    }


    @Override
    public void deleteProgress(String id) {
        ProgressPaymentInformation progressPaymentInformation = new ProgressPaymentInformation();
        progressPaymentInformation.setDelFlag("1");
        progressPaymentInformation.setId(id);
        progressPaymentInformationDao.updateByPrimaryKeySelective(progressPaymentInformation);

        Example example = new Example(ProgressPaymentTotalPayment.class);
        example.createCriteria().andEqualTo("progressPaymentId", id);
        ProgressPaymentTotalPayment progressPaymentTotalPayment = new ProgressPaymentTotalPayment();
        progressPaymentTotalPayment.setDelFlag("1");
        progressPaymentTotalPaymentDao.updateByExampleSelective(progressPaymentTotalPayment, example);

        Example example1 = new Example(ApplicationInformation.class);
        example1.createCriteria().andEqualTo("progressPaymentId", id);
        ApplicationInformation applicationInformation = new ApplicationInformation();
        applicationInformation.setDelFlag("1");
        applicationInformationDao.updateByExampleSelective(applicationInformation, example1);

        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", id);
        auditInfoDao.deleteByExample(example2);
    }

    /***
     * 签证变更所选项目
     * @param visaBaseProjectVo
     * @return
     */
    @Override
    public List<VisaBaseProjectVo> selectByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo) {
        PageHelper.startPage(visaBaseProjectVo.getPageNum(), visaBaseProjectVo.getPageSize());

        return baseProjectDao.findByBaseProject(visaBaseProjectVo);

    }

    @Override
    public void updateProject(BaseProject baseProject) {
        baseProjectDao.updateByPrimaryKeySelective(baseProject);
    }

    @Override
    public List<BaseProject> findByBuildingProject(String id) {
        return baseProjectDao.findByBuildingProject(id);
    }

    @Override
    public BaseProject findByBaseProjectId(VisaBaseProjectVo visaBaseProjectVo) {
        return baseProjectDao.selectByPrimaryKey(visaBaseProjectVo.getId());

    }

    @Override
    public BaseProject findByBuilding(String id) {
        Budgeting byId = budgetingMapper.findById(id);

        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("id", byId.getBaseProjectId());
        BaseProject baseProject = baseProjectDao.selectOneByExample(example);

        if (baseProject != null) {
            ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectById(baseProject.getConstructionUnit());
            if (constructionUnitManagement != null) {
                baseProject.setConstructionOrganization(constructionUnitManagement.getConstructionUnitName());
            }
        }
        return baseProject;
    }

    @Override
    public List<AuditChekedVo> auditChek(String id) {
        return auditInfoDao.auditChek(id);
    }

    @Override
    public List<AuditChekedVo> auditDesginChek(String id) {
        return auditInfoDao.auditDesginChek(id);
    }

    @Override
    public List<AuditChekedVo> auditChangeDesginChek(String id) {
        return auditInfoDao.auditChangeDesginChek(id);
    }
}

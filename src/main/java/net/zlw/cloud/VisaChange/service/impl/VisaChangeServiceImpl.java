package net.zlw.cloud.VisaChange.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.vo.LoginUser;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaChangeInformation;
import net.zlw.cloud.VisaChange.mapper.VisaChangeInformationMapper;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeInfoVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeStatisticVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/***
 * 签证变更逻辑层
 */
@Service
@Transactional
public class VisaChangeServiceImpl implements VisaChangeService {

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");


    @Autowired
    private VisaChangeMapper vcMapper;

    @Autowired
    private AuditInfoDao auditInfoDao;

    @Autowired
    private MemberManageDao memberManageDao;

    @Autowired
    private VisaChangeInformationMapper applyMapper;

    @Autowired
    private BaseProjectDao baseProjectDao;


    /***
     * 分页查询所有
     */
    @Override
    public PageInfo<VisaChangeVo> findAllPage(VisaChangeVo visaChangeVO, UserInfo loginUser) {
        PageHelper.startPage(visaChangeVO.getPageNum(), visaChangeVO.getPageSize());
        //TODO 需要改
        visaChangeVO.setLoginUserId("user305");
        List<VisaChangeVo> all = null;
        //未审核
        if ("1".equals(visaChangeVO.getStatus())) {

            all = vcMapper.findByNoExamine(visaChangeVO);


//           未通过
        } else if ("3".equals(visaChangeVO.getStatus())) {
            all = vcMapper.findByNotPass(visaChangeVO);

//            已完成
        } else {
            all = vcMapper.findAll(visaChangeVO);
        }

        BigDecimal shang = new BigDecimal(0);
        BigDecimal xia = new BigDecimal(0);
        for (VisaChangeVo thisAll : all) {
            String baseProjectId = thisAll.getBaseProjectId();
            List<VisaChange> a = vcMapper.findByBaseProjectId(baseProjectId);
            for (VisaChange visaChange : a) {
                if ("0".equals(visaChange.getUpAndDownMark())) {
//                    累积签证变更金额
                    shang = shang.add(new BigDecimal(visaChange.getAmountVisaChange()));
                    thisAll.setContractAmountXia("-");
                    thisAll.setAmountVisaChangeAddXia("-");
                    thisAll.setProportionContractXia("-");
                    thisAll.setAmountVisaChangeXia("-");
                    thisAll.setContractAmountShang(visaChange.getContractAmount());
                    thisAll.setProportionContractShang(visaChange.getProportionContract());
                    thisAll.setAmountVisaChangeShang(visaChange.getAmountVisaChange() + "");

                } else {
                    xia = xia.add(new BigDecimal(visaChange.getAmountVisaChange()));
                    thisAll.setContractAmountShang("-");
                    thisAll.setAmountVisaChangeAddShang("-");
                    thisAll.setProportionContractShang("-");
                    thisAll.setAmountVisaChangeShang("-");
                    thisAll.setContractAmountXia(visaChange.getContractAmount());
                    thisAll.setProportionContractXia(visaChange.getProportionContract());
                    thisAll.setAmountVisaChangeXia(visaChange.getAmountVisaChange() + "");
                }
            }

            thisAll.setAmountVisaChangeAddShang(shang.toString());
            thisAll.setAmountVisaChangeAddXia(xia.toString());
        }
        PageInfo<VisaChangeVo> info = new PageInfo<>(all);
        return info;

    }

    @Override
    public void delete(String id) {
        vcMapper.deleteById(id);
    }

    //编辑回显
    @Override
    public VisaChangeInfoVo selectByEditId(String id) {

        VisaChangeInfoVo visaChangeInfoVo = new VisaChangeInfoVo();

        VisaChange visaChange = vcMapper.selectByPrimaryKey(id);
        VisaChange byChangNum = vcMapper.selectByChangNum();

        //项目信息
        String baseProjectId = visaChange.getBaseProjectId();
        Example example1 = new Example(BaseProject.class);
        example1.createCriteria().andEqualTo("id", baseProjectId);
        BaseProject baseProject = baseProjectDao.selectOneByExample(example1);

        visaChangeInfoVo.setBaseProject(baseProject);

        //获取上下家签证/变更申请信息
        Example example = new Example(VisaChangeInformation.class);
        example.createCriteria().andEqualTo("baseProjectId", baseProjectId);
        List<VisaChangeInformation> changeInformations = applyMapper.selectByExample(example);

        for (VisaChangeInformation changeInformation : changeInformations) {
            if ("0".equals(changeInformation.getUpAndDownMark())) {
                visaChangeInfoVo.setApplicantNameUp(changeInformation.getApplicantName());
                visaChangeInfoVo.setRemarkUp(changeInformation.getRemark());
                visaChangeInfoVo.setId(changeInformation.getId());
            }
            if ("1".equals(changeInformation.getUpAndDownMark())) {
                visaChangeInfoVo.setSubmitMoneyDown(changeInformation.getSubmitMoney());
                visaChangeInfoVo.setApplicantNameDown(changeInformation.getApplicantName());
                visaChangeInfoVo.setRemarkDown(changeInformation.getRemark());
                visaChangeInfoVo.setId(changeInformation.getId());
            }

        }
        BigDecimal totalUp = new BigDecimal(0);
        BigDecimal totalDown = new BigDecimal(0);

        List<VisaChange> a = vcMapper.findByBaseProjectId(baseProjectId);
        for (VisaChange thisA : a) {
            if ("0".equals(thisA.getUpAndDownMark())) {
                //累积签证变更金额
                totalUp = totalUp.add(new BigDecimal(thisA.getAmountVisaChange()));
            } else {
                totalDown = totalDown.add(new BigDecimal(thisA.getAmountVisaChange()));

            }
        }

        String totalUpRate = "";
        String totalDownRate = "";
        BigDecimal oneHu = new BigDecimal(100);
        totalUpRate = totalUp.divide(totalDown, 2, BigDecimal.ROUND_HALF_UP).multiply(oneHu).toString();
        totalDownRate = totalDown.divide(totalUp, 2, BigDecimal.ROUND_HALF_UP).multiply(oneHu).toString();

        System.out.println(totalDownRate);
        System.out.println(totalUpRate);
        //签证/变更统计信息
        visaChangeInfoVo.setChangeNum(visaChange.getChangeNum());
        //上家累计签证/变更金额
        visaChangeInfoVo.setTotalAmountVisaChangeUp(totalUp.toString());
        //下家累计签证/变更金额
        visaChangeInfoVo.setTotalAmountVisaChangeDown(totalDown.toString());
        //累计占上家合同比例
        visaChangeInfoVo.setTotalProportionContractUp(totalUpRate);
        //累计占下家合同比例
        visaChangeInfoVo.setTotalProportionContractDown(totalDownRate);


        Example visaChangeExample = new Example(VisaChange.class);
        visaChangeExample.createCriteria().andEqualTo("baseProjectId", baseProject);
        List<VisaChange> visaChanges = vcMapper.selectByExample(visaChangeExample);

        List<VisaChangeStatisticVo> changeStatisticVos = new ArrayList<>();

        for (VisaChange change : visaChanges) {
            //上家
            if ("0".equals(visaChange.getUpAndDownMark())) {
                // BeanUtils.copyProperties();
                visaChangeInfoVo.setId(change.getId());
                visaChangeInfoVo.setAmountVisaChange(change.getAmountVisaChange() + "");
                visaChangeInfoVo.setContractAmount(change.getContractAmount());
                visaChangeInfoVo.setCompileTime(change.getCompileTime());
                visaChangeInfoVo.setCompletionTime(change.getCompletionTime());
                visaChangeInfoVo.setProportionContract(change.getProportionContract());
                visaChangeInfoVo.setOutsourcing(change.getOutsourcing());
                visaChangeInfoVo.setNameOfCostUnit(change.getNameOfCostUnit());
                visaChangeInfoVo.setContact(change.getContact());
                visaChangeInfoVo.setContactNumber(change.getContactNumber());
                visaChangeInfoVo.setOutsourcingAmount(change.getOutsourcingAmount() + "");
                visaChangeInfoVo.setVisaChangeReason(change.getVisaChangeReason());

                //封装
                VisaChangeStatisticVo statisticVo = new VisaChangeStatisticVo();
                statisticVo.setId(byChangNum.getChangeNum());
                statisticVo.setAmountVisaChangeUp(change.getAmountVisaChange() + "");
                statisticVo.setAmountVisaChangeDown("-");
                statisticVo.setCompileTime(change.getCompileTime());
                statisticVo.setCreateTime(change.getCreateTime());
                statisticVo.setContact(change.getCreatorId());
                statisticVo.setProportionContractUp(change.getProportionContract());
                statisticVo.setProportionContractDown("-");
                changeStatisticVos.add(statisticVo);


            }
            if ("1".equals(visaChange.getUpAndDownMark())) {

                visaChangeInfoVo.setChangeDownId(change.getId());
                visaChangeInfoVo.setAmountVisaChangeDown(change.getAmountVisaChange() + "");
                visaChangeInfoVo.setContractAmountDown(change.getContractAmount());
                visaChangeInfoVo.setCompileTimeDown(change.getCompileTime());
                visaChangeInfoVo.setCompletionTimeDown(change.getCompletionTime());
                visaChangeInfoVo.setProportionContractDown(change.getProportionContract());
                visaChangeInfoVo.setOutsourcingDown(change.getOutsourcing());
                visaChangeInfoVo.setNameOfCostUnitDown(change.getNameOfCostUnit());
                visaChangeInfoVo.setContactDown(change.getContact());
                visaChangeInfoVo.setContactNumberDown(change.getContactNumber());
                visaChangeInfoVo.setOutsourcingAmountDown(change.getOutsourcingAmount() + "");
                visaChangeInfoVo.setVisaChangeReasonDown(change.getVisaChangeReason());

                //封装
                VisaChangeStatisticVo statisticVo = new VisaChangeStatisticVo();
                statisticVo.setId(byChangNum.getChangeNum());
                statisticVo.setAmountVisaChangeUp("-");
                statisticVo.setAmountVisaChangeDown(change.getAmountVisaChange() + "");
                statisticVo.setCompileTime(change.getCompileTime());
                statisticVo.setCreateTime(change.getCreateTime());
                statisticVo.setContact(change.getCreatorId());
                statisticVo.setProportionContractUp("-");
                statisticVo.setProportionContractDown(change.getProportionContract());

                changeStatisticVos.add(statisticVo);
            }
        }

        //审核信息
        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", baseProjectId);
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
        visaChangeInfoVo.setAuditInfos(auditInfos);


        return visaChangeInfoVo;
    }


    /***
     * 批量审核
     * @param batchReviewVo
     */
    @Override
    public void approvalProcess(BatchReviewVo batchReviewVo, UserInfo userInfo) {
        //todo  获取登陆人
//        userInfo.getId();
        String[] split = batchReviewVo.getBatchAll().split(",");

        if (split.length > 0) {
            for (String s : split) {
                if (StringUtil.isNotEmpty(s)) {

                    Example example1 = new Example(VisaChange.class);
                    example1.createCriteria().andEqualTo("id", s);
                    VisaChange visaChange = vcMapper.selectOneByExample(example1);

                    //获得当前记录的审核人id
                    Example example2 = new Example(AuditInfo.class);
                    example2.createCriteria().andEqualTo("baseProjectId", s)
                            .andEqualTo("auditResult", "0")
                            .andEqualTo("auditorId", "123");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example2);
//                判断如果一级审核通过更改状态
                    if ("1".equals(batchReviewVo.getAuditResult())) {
                        if ("0".equals(auditInfo.getAuditType())) {
                            auditInfo.setAuditResult("1");
//                        一级审批的意见,时间
                            auditInfo.setAuditTime(sdf.format(date));
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                       修改审批状态
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                            一审通过在审核表插入一条数据
                            AuditInfo auditInfo1 = new AuditInfo();
                            auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                            auditInfo1.setBaseProjectId(s);
                            auditInfo1.setAuditResult("0");
                            auditInfo1.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            auditInfo1.setCreateTime(sdf.format(new Date()));
                            auditInfo1.setUpdateTime(sdf.format(new Date()));
                            visaChange.setStatus("4");

                            Example example3 = new Example(MemberManage.class);
                            example3.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
                            MemberManage memberManage = memberManageDao.selectOneByExample(example3);
                            auditInfo1.setAuditorId(memberManage.getId());

                            vcMapper.updateByPrimaryKeySelective(visaChange);
                            auditInfoDao.insertSelective(auditInfo1);
//                      判断二级审核通过
                        } else if ("1".equals(auditInfo.getAuditType())) {
                            visaChange.setStatus("5");
                            auditInfo.setAuditResult("1");

                            Date date = new Date();
                            auditInfo.setAuditTime(sdf.format(date));
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                        修改表信息
                            vcMapper.updateByPrimaryKeySelective(visaChange);
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        }
//                    如果审核未通过,修改主表从表审核状态
                    } else if ("2".equals(batchReviewVo.getAuditResult())) {
                        auditInfo.setAuditResult("2");
                        visaChange.setStatus("3");
                        auditInfo.setAuditTime(sdf.format(date));
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        vcMapper.updateByPrimaryKeySelective(visaChange);
                    }
                }
            }
        }
    }

    /***
     * 添加提交保存
     * @param
     */
    @Override
    public void addVisChangeVo(VisaChangeInfoVo visaChangeInfoVo, UserInfo loginUser) {

        //TODO 需要改
        visaChangeInfoVo.setLoginUserId("123");

        Date date = new Date();
        String createTime = new SimpleDateFormat().format(date);

        //提交
        if (StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())) {

            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            auditInfo.setFounderId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setCompanyId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setAuditorId(visaChangeInfoVo.getAuditId());
            // 数据状态 0:正常 1:删除
            auditInfo.setStatus("0");
            // 0一审1二审2变更一审3变更二审
            auditInfo.setAuditType("0");
            // 0未审批 1通过 2未通过
            auditInfo.setAuditResult("0");
            auditInfo.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
            auditInfo.setCreateTime(createTime);

            auditInfoDao.insertSelective(auditInfo);

            insertVisaInfoAndApplyInfo(visaChangeInfoVo, createTime);
        } else {//保存

            insertVisaInfoAndApplyInfo(visaChangeInfoVo, createTime);

        }
    }


    private void insertVisaInfoAndApplyInfo(VisaChangeInfoVo visaChangeInfoVo, String createTime) {


        //判断上家签证/变更申请信息
        String applicantNameUp = visaChangeInfoVo.getApplicantNameUp();
        if (StringUtil.isNotEmpty(applicantNameUp)) {
            VisaChangeInformation applyChangeInformation = new VisaChangeInformation();
            applyChangeInformation.setId(UUID.randomUUID().toString().replace("-", ""));
            applyChangeInformation.setApplicantName(applicantNameUp);
            applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkUp());
            applyChangeInformation.setUpAndDownMark("0");
            applyChangeInformation.setCreateTime(createTime);
            applyChangeInformation.setUpdateTime(createTime);
            applyChangeInformation.setFouderId(visaChangeInfoVo.getLoginUserId());
            //0:正常 1:删除
            applyChangeInformation.setState("0");
            applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            //applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                applyChangeInformation.setStatus("1");
            }else{
                applyChangeInformation.setStatus("2");
            }
            applyMapper.insertSelective(applyChangeInformation);

        }
        //判断下家签证/变更申请信息
        String applicantNameDown = visaChangeInfoVo.getApplicantNameDown();
        if (StringUtil.isNotEmpty(applicantNameDown)) {
            VisaChangeInformation applyChangeInformation = new VisaChangeInformation();

            applyChangeInformation.setId(UUID.randomUUID().toString().replace("-", ""));
            applyChangeInformation.setApplicantName(applicantNameDown);
            applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkDown());
            applyChangeInformation.setSubmitMoney(visaChangeInfoVo.getSubmitMoneyDown());
            applyChangeInformation.setUpAndDownMark("1");
            applyChangeInformation.setCreateTime(createTime);
            applyChangeInformation.setUpdateTime(createTime);
            applyChangeInformation.setFouderId(visaChangeInfoVo.getLoginUserId());

            //0:正常 1:删除
            applyChangeInformation.setState("0");
            applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                applyChangeInformation.setStatus("1");
            }else{
                applyChangeInformation.setStatus("2");
            }
            applyMapper.insertSelective(applyChangeInformation);
        }

        //本次上家签证/变更信息
        String amountVisaChange = visaChangeInfoVo.getAmountVisaChange();
        if (StringUtil.isNotEmpty(amountVisaChange)) {
            VisaChange visaChange = new VisaChange();
            visaChange.setId(UUID.randomUUID().toString().replace("-", ""));
            visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange.setCreateTime(createTime);
            visaChange.setUpdateTime(createTime);
            visaChange.setContractAmount(visaChangeInfoVo.getContractAmount());
            visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

            //0正常1删除
            visaChange.setState("0");
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange.setCompileTime(createTime);
            visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
            visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
            visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
            visaChange.setContact(visaChangeInfoVo.getContact());
            visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
            if(!"1".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
            visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange.setUpAndDownMark("0");
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                visaChange.setStatus("1");
            }else{
                visaChange.setStatus("2");
            }
            visaChange.setProportionContract(visaChangeInfoVo.getProportionContract());
            visaChange.setChangeNum("1");

            vcMapper.insertSelective(visaChange);
        }

        //本次下家签证/变更信息
        String amountVisaChangeDown = visaChangeInfoVo.getAmountVisaChangeDown();
        if (StringUtil.isNotEmpty(amountVisaChangeDown)) {
            VisaChange visaChange = new VisaChange();
            visaChange.setId(UUID.randomUUID().toString().replace("-", ""));
            visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange.setCreateTime(createTime);
            visaChange.setUpdateTime(createTime);
            visaChange.setContractAmount(visaChangeInfoVo.getContractAmountDown());
            visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

            //0正常1删除
            visaChange.setState("0");
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeDown()));
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange.setCompileTime(createTime);
            visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
            visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
            visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
            visaChange.setContact(visaChangeInfoVo.getContactDown());
            visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
            visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonDown());
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange.setUpAndDownMark("1");
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                visaChange.setStatus("1");
            }else{
                visaChange.setStatus("2");
            }
            visaChange.setProportionContract(visaChangeInfoVo.getProportionContractDown());
            visaChange.setChangeNum("1");

            vcMapper.insertSelective(visaChange);
        }
    }


    //编辑提交/保存
    @Override
    public void submitOrSave(VisaChangeInfoVo visaChangeInfoVo) {

        Date date1 = new Date();
        String updateTime = new SimpleDateFormat().format(date1);

        Example example = new Example(VisaChange.class);
        example.createCriteria().andEqualTo("id", visaChangeInfoVo.getId());
        VisaChange visaChange = vcMapper.selectOneByExample(example);

        Example example1 = new Example(AuditInfo.class);
        example1.createCriteria().andEqualTo("id", visaChange.getBaseProjectId());
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);

        Example example2 = new Example(VisaChangeInformation.class);
        example2.createCriteria().andEqualTo("id", visaChange.getApplyChangeInfoId());
        VisaChangeInformation applyChangeInformation = applyMapper.selectOneByExample(example2);

        //                判断如果一级审核通过更改状态
        if ("1".equals(auditInfo.getAuditResult())) {
            if ("0".equals(auditInfo.getAuditType())) {
                auditInfo.setAuditResult("1");
//                        一级审批的意见,时间
                auditInfo.setAuditTime(sdf.format(date));
                auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
//                       修改审批状态
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                            一审通过在审核表插入一条数据
                AuditInfo auditInfo1 = new AuditInfo();
                auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                auditInfo1.setBaseProjectId(UUID.randomUUID().toString().replace("-", ""));
                auditInfo1.setAuditResult("0");
                auditInfo1.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
                auditInfo1.setCreateTime(sdf.format(new Date()));
                auditInfo1.setUpdateTime(sdf.format(new Date()));
                visaChange.setStatus("4");

                Example example3 = new Example(MemberManage.class);
                example3.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
                MemberManage memberManage = memberManageDao.selectOneByExample(example3);
                auditInfo1.setAuditorId(memberManage.getId());

                vcMapper.updateByPrimaryKeySelective(visaChange);
                auditInfoDao.insertSelective(auditInfo1);
//                      判断二级审核通过
            } else if ("1".equals(auditInfo.getAuditType())) {
                visaChange.setStatus("5");
                auditInfo.setAuditResult("1");

                Date date = new Date();
                auditInfo.setAuditTime(sdf.format(date));
                auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
//                        修改表信息
                vcMapper.updateByPrimaryKeySelective(visaChange);
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            }
//                    如果审核未通过,修改主表从表审核状态
        } else if ("2".equals(auditInfo.getAuditResult())) {
            auditInfo.setAuditResult("2");
            visaChange.setStatus("3");
            auditInfo.setAuditTime(sdf.format(date));
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            vcMapper.updateByPrimaryKeySelective(visaChange);
        }


        //提交
        if (StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())) {


            auditInfo.setId(visaChangeInfoVo.getAuditId());
            auditInfo.setFounderId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setCompanyId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setAuditorId(visaChangeInfoVo.getAuditId());

            auditInfo.setStatus("0");
            auditInfo.setAuditType("0");

            auditInfo.setAuditResult("0");
            auditInfo.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());

            auditInfo.setUpdateTime(updateTime);
            auditInfo.setAuditTime(updateTime);

            auditInfoDao.updateByPrimaryKeySelective(auditInfo);


            String applicantNameUp = visaChangeInfoVo.getApplicantNameUp();
            if (StringUtil.isNotEmpty(applicantNameUp)) {
                applyChangeInformation.setApplicantName(applicantNameUp);
                applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkUp());
                applyChangeInformation.setUpdateTime(updateTime);
                applyChangeInformation.setFouderId(visaChangeInfoVo.getLoginUserId());

                //0:正常 1:删除
                applyChangeInformation.setState("0");
                applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProject().getId());
                //applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
                applyChangeInformation.setStatus("0");
                applyMapper.updateByPrimaryKeySelective(applyChangeInformation);

            }
            //判断下家签证/变更申请信息
            String applicantNameDown = visaChangeInfoVo.getApplicantNameDown();
            if (StringUtil.isNotEmpty(applicantNameDown)) {
                applyChangeInformation.setId(visaChangeInfoVo.getId());
                applyChangeInformation.setApplicantName(applicantNameDown);
                applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkDown());
                applyChangeInformation.setSubmitMoney(visaChangeInfoVo.getSubmitMoneyDown());
                applyChangeInformation.setUpdateTime(updateTime);
                applyMapper.updateByPrimaryKeySelective(applyChangeInformation);
            }

            //本次上家签证/变更信息
            String amountVisaChange = visaChangeInfoVo.getAmountVisaChange();
            if (StringUtil.isNotEmpty(amountVisaChange)) {
                visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
                visaChange.setUpdateTime(updateTime);
                visaChange.setContractAmount(visaChangeInfoVo.getContractAmount());
                visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

                visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
                visaChange.setContact(visaChangeInfoVo.getContact());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
                visaChange.setStatus("0");
                visaChange.setProportionContract(visaChangeInfoVo.getProportionContract());
                String changeNum = visaChange.getChangeNum();
                int num = Integer.parseInt(changeNum);
                num = num + 1;
                visaChange.setChangeNum(num + "");

                vcMapper.updateByPrimaryKeySelective(visaChange);
            }

            //本次下家签证/变更信息
            String amountVisaChangeDown = visaChangeInfoVo.getAmountVisaChangeDown();
            if (StringUtil.isNotEmpty(amountVisaChangeDown)) {
                visaChange.setId(visaChangeInfoVo.getChangeDownId());
                visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
                visaChange.setUpdateTime(updateTime);
                visaChange.setContractAmount(visaChangeInfoVo.getContractAmountDown());
                visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

                visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeDown()));
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
                visaChange.setContact(visaChangeInfoVo.getContactDown());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonDown());
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
                visaChange.setStatus("0");
                visaChange.setProportionContract(visaChangeInfoVo.getProportionContractDown());
                String changeNum = visaChange.getChangeNum();
                int num = Integer.parseInt(changeNum);
                num = num + 1;
                visaChange.setChangeNum(num + "");
                vcMapper.updateByPrimaryKeySelective(visaChange);
            }
            //保存
        } else {

            String applicantNameUp = visaChangeInfoVo.getApplicantNameUp();
            if (StringUtil.isNotEmpty(applicantNameUp)) {
                applyChangeInformation.setApplicantName(applicantNameUp);
                applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkUp());
                applyChangeInformation.setUpdateTime(updateTime);
                applyChangeInformation.setFouderId(visaChangeInfoVo.getLoginUserId());
                applyMapper.updateByPrimaryKeySelective(applyChangeInformation);

            }
            //判断下家签证/变更申请信息
            String applicantNameDown = visaChangeInfoVo.getApplicantNameDown();
            if (StringUtil.isNotEmpty(applicantNameDown)) {
                applyChangeInformation.setApplicantName(applicantNameDown);
                applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkDown());
                applyChangeInformation.setSubmitMoney(visaChangeInfoVo.getSubmitMoneyDown());
                applyChangeInformation.setUpdateTime(updateTime);
                applyMapper.updateByPrimaryKeySelective(applyChangeInformation);
            }

            //本次上家签证/变更信息
            String amountVisaChange = visaChangeInfoVo.getAmountVisaChange();
            if (StringUtil.isNotEmpty(amountVisaChange)) {
                visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
                visaChange.setUpdateTime(updateTime);
                visaChange.setContractAmount(visaChangeInfoVo.getContractAmount());
                visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

                //0正常1删除
                visaChange.setState("0");
                visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
                visaChange.setContact(visaChangeInfoVo.getContact());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setUpAndDownMark("0");
                //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
                visaChange.setStatus("0");
                visaChange.setProportionContract(visaChangeInfoVo.getProportionContract());
                String changeNum = visaChange.getChangeNum();
                int num = Integer.parseInt(changeNum);
                num = num + 1;
                visaChange.setChangeNum(num + "");

                vcMapper.updateByPrimaryKeySelective(visaChange);
            }

            //本次下家签证/变更信息
            String amountVisaChangeDown = visaChangeInfoVo.getAmountVisaChangeDown();
            if (StringUtil.isNotEmpty(amountVisaChangeDown)) {
                visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
                visaChange.setUpdateTime(updateTime);
                visaChange.setContractAmount(visaChangeInfoVo.getContractAmountDown());
                visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());

                visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeDown()));
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
                visaChange.setContact(visaChangeInfoVo.getContactDown());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonDown());
                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setUpAndDownMark("1");

                visaChange.setProportionContract(visaChangeInfoVo.getProportionContractDown());
                String changeNum = visaChange.getChangeNum();
                int num = Integer.parseInt(changeNum);
                num = num + 1;
                visaChange.setChangeNum(num + "");

                vcMapper.updateByPrimaryKeySelective(visaChange);


            }

        }


    }

    // 查看回显
    @Override
    public VisaChangeInfoVo selectById(String id) {

        VisaChangeInfoVo visaChangeInfoVo = getVisaChangeInfoVo(id);


        return visaChangeInfoVo;
    }

    private VisaChangeInfoVo getVisaChangeInfoVo(String id) {
        VisaChangeInfoVo visaChangeInfoVo = new VisaChangeInfoVo();

        VisaChange visaChange = vcMapper.selectByPrimaryKey(id);
//        签证变更次序
        VisaChange byChangNum = vcMapper.selectByChangNum();
        // 回显审核信息
        Example example3 = new Example(AuditInfo.class);
        example3.createCriteria().andEqualTo("id", visaChange.getBaseProjectId());
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example3);
        if (auditInfo != null) {
            auditInfo.setId(visaChangeInfoVo.getAuditId());
            auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
            auditInfo.setAuditResult(visaChangeInfoVo.getAuditResult());
        }

        //项目信息
        String baseProjectId = visaChange.getBaseProjectId();
        Example example1 = new Example(BaseProject.class);
        example1.createCriteria().andEqualTo("id", baseProjectId);
        BaseProject baseProject = baseProjectDao.selectOneByExample(example1);

        visaChangeInfoVo.setBaseProject(baseProject);

        //获取上下家签证/变更申请信息
        Example example = new Example(VisaChangeInformation.class);
        example.createCriteria().andEqualTo("id", baseProjectId);
        List<VisaChangeInformation> changeInformations = applyMapper.selectByExample(example);

        for (VisaChangeInformation changeInformation : changeInformations) {
            if ("0".equals(changeInformation.getUpAndDownMark())) {
                visaChangeInfoVo.setApplicantNameUp(changeInformation.getApplicantName());
                visaChangeInfoVo.setRemarkUp(changeInformation.getRemark());
                visaChangeInfoVo.setId(changeInformation.getId());
            }
            if ("1".equals(changeInformation.getUpAndDownMark())) {
                visaChangeInfoVo.setSubmitMoneyDown(changeInformation.getSubmitMoney());
                visaChangeInfoVo.setApplicantNameDown(changeInformation.getApplicantName());
                visaChangeInfoVo.setRemarkDown(changeInformation.getRemark());
                visaChangeInfoVo.setId(changeInformation.getId());
            }

        }
        BigDecimal totalUp = new BigDecimal(0);
        BigDecimal totalDown = new BigDecimal(0);
        BigDecimal totalUpRate = new BigDecimal(0);
        BigDecimal totalDownRate = new BigDecimal(0);

        List<VisaChange> a = vcMapper.findByBaseProjectId(baseProjectId);
        for (VisaChange thisA : a) {
            if ("0".equals(thisA.getUpAndDownMark())) {
                //累积上家签证变更金额
                totalUp = totalUp.add(new BigDecimal(thisA.getAmountVisaChange()));
                //累积上家签证变更占比
                totalUpRate = totalDownRate.add(new BigDecimal(thisA.getProportionContract()));
            } else {
                //累积下家签证变更金额
                totalDown = totalDown.add(new BigDecimal(thisA.getAmountVisaChange()));
                //累积下家签证变更占比
                totalDownRate = totalDownRate.add(new BigDecimal(thisA.getProportionContract()));

            }
        }

        //签证/变更统计信息
        visaChangeInfoVo.setChangeNum(visaChange.getChangeNum());
        //上家累计签证/变更金额
        visaChangeInfoVo.setTotalAmountVisaChangeUp(totalUp.toString());
        //下家累计签证/变更金额
        visaChangeInfoVo.setTotalAmountVisaChangeDown(totalDown.toString());
        //累计占上家合同比例
        visaChangeInfoVo.setTotalProportionContractUp(totalUpRate.toString());
        //累计占下家合同比例
        visaChangeInfoVo.setTotalProportionContractDown(totalDownRate.toString());
        System.out.println(totalDownRate);
        System.out.println(totalUpRate);
        System.out.println(totalUp);
        System.out.println(totalDown);

        List<VisaChangeStatisticVo> changeStatisticVos = new ArrayList<>();

        //上家
        if ("0".equals(visaChange.getUpAndDownMark())) {
            // BeanUtils.copyProperties();
            visaChangeInfoVo.setId(visaChange.getId());
            visaChangeInfoVo.setAmountVisaChange(visaChange.getAmountVisaChange() + "");
            visaChangeInfoVo.setContractAmount(visaChange.getContractAmount());
            visaChangeInfoVo.setCompileTime(visaChange.getCompileTime());
            visaChangeInfoVo.setCompletionTime(visaChange.getCompletionTime());
            visaChangeInfoVo.setProportionContract(visaChange.getProportionContract());
            visaChangeInfoVo.setOutsourcing(visaChange.getOutsourcing());
            visaChangeInfoVo.setNameOfCostUnit(visaChange.getNameOfCostUnit());
            visaChangeInfoVo.setContact(visaChange.getContact());
            visaChangeInfoVo.setContactNumber(visaChange.getContactNumber());
            visaChangeInfoVo.setOutsourcingAmount(visaChange.getOutsourcingAmount() + "");
            visaChangeInfoVo.setVisaChangeReason(visaChange.getVisaChangeReason());

            //封装
            VisaChangeStatisticVo statisticVo = new VisaChangeStatisticVo();
            statisticVo.setId(byChangNum.getChangeNum());
            statisticVo.setAmountVisaChangeUp(visaChange.getAmountVisaChange() + "");
            statisticVo.setAmountVisaChangeDown("-");
            statisticVo.setCompileTime(visaChange.getCompileTime());
            statisticVo.setCreateTime(visaChange.getCreateTime());

            statisticVo.setContact(visaChange.getCreatorId());
            statisticVo.setProportionContractUp(visaChange.getProportionContract());
            statisticVo.setProportionContractDown("-");
            changeStatisticVos.add(statisticVo);


        }
        if ("1".equals(visaChange.getUpAndDownMark())) {

            visaChangeInfoVo.setChangeDownId(visaChange.getId());
            visaChangeInfoVo.setAmountVisaChangeDown(visaChange.getAmountVisaChange() + "");
            visaChangeInfoVo.setContractAmountDown(visaChange.getContractAmount());
            visaChangeInfoVo.setCompileTimeDown(visaChange.getCompileTime());
            visaChangeInfoVo.setCompletionTimeDown(visaChange.getCompletionTime());
            visaChangeInfoVo.setProportionContractDown(visaChange.getProportionContract());
            visaChangeInfoVo.setOutsourcingDown(visaChange.getOutsourcing());
            visaChangeInfoVo.setNameOfCostUnitDown(visaChange.getNameOfCostUnit());
            visaChangeInfoVo.setContactDown(visaChange.getContact());
            visaChangeInfoVo.setContactNumberDown(visaChange.getContactNumber());
            visaChangeInfoVo.setOutsourcingAmountDown(visaChange.getOutsourcingAmount() + "");
            visaChangeInfoVo.setVisaChangeReasonDown(visaChange.getVisaChangeReason());

            //封装
            VisaChangeStatisticVo statisticVo = new VisaChangeStatisticVo();
            if (statisticVo != null) {


                statisticVo.setId(byChangNum.getChangeNum());
                statisticVo.setAmountVisaChangeUp("-");
                statisticVo.setAmountVisaChangeDown(visaChange.getAmountVisaChange() + "");
                statisticVo.setCompileTime(visaChange.getCompileTime());
                statisticVo.setCreateTime(visaChange.getCreateTime());
                statisticVo.setContact(visaChange.getCreatorId());
                statisticVo.setProportionContractUp("-");
                statisticVo.setProportionContractDown(visaChange.getProportionContract());

                changeStatisticVos.add(statisticVo);
            }
        }

        //审核信息
        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", visaChange.getId());
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);

        visaChangeInfoVo.setAuditInfos(auditInfos);
        return visaChangeInfoVo;
    }


}

package net.zlw.cloud.VisaChange.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/***
 * 签证变更逻辑层
 */
@Service
@Transactional
public class VisaChangeImpl implements VisaChangeService {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");


    @Autowired
    private VisaChangeMapper vcMapper;

    @Autowired
    private AuditInfoDao auditInfoDao;

    @Autowired
    private MemberManageDao memberManageDao;

    @Autowired
    private VisaChangeInformationMapper vacifMapper;

    @Autowired
    private BaseProjectDao baseProjectDao;


    /***
     * 分页查询所有
     */
    @Override
    public PageInfo<VisaChangeVo> findAllPage(VisaChangeVo visaChangeVO, UserInfo loginUser) {
        PageHelper.startPage(visaChangeVO.getPageNum(), visaChangeVO.getPageSize());
        //TODO 需要改
        visaChangeVO.setLoginUserId("123");
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

    @Override
    public VisaChangeInfoVo selectById(String id) {

        VisaChange visaChange = vcMapper.selectByPrimaryKey(id);

        Example example = new Example(VisaChangeInformation.class);
        example.createCriteria().andEqualTo("baseProjectId", id);
        VisaChangeInformation vcif = vacifMapper.selectOneByExample(example);

        Example example1 = new Example(BaseProject.class);
        example1.createCriteria().andEqualTo("id", id);
        BaseProject baseProject = baseProjectDao.selectOneByExample(example1);


        Example example2 = new Example(AuditInfo.class);
        example2.createCriteria().andEqualTo("baseProjectId", id);
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example2);

        VisaChangeInfoVo infoVo = new VisaChangeInfoVo();
        infoVo.setBaseProject(baseProject);
//            如果是上家就把上家申请、信息表填进去
        if ("0".equals(infoVo.getUpAndDownMark()) && "1".equals(infoVo.getUpAndDownMark())) {
//        ------------------------上家
//            申请表
            infoVo.setApplicantNameShang(vcif.getApplicantName());
            infoVo.setRemarkShang(vcif.getRemark());
//            信息表
            infoVo.setAmountVisaChange(visaChange.getAmountVisaChange()+"");
            infoVo.setContractAmount(visaChange.getContractAmount());
            infoVo.setCompileTime(visaChange.getCompileTime());
            infoVo.setCompletionTime(visaChange.getCompletionTime());
            infoVo.setProportionContract(visaChange.getProportionContract());
            infoVo.setOutsourcing(visaChange.getOutsourcing());
            infoVo.setNameOfCostUnit(visaChange.getNameOfCostUnit());
            infoVo.setContact(visaChange.getContact());
            infoVo.setContactNumber(visaChange.getContactNumber());
            infoVo.setOutsourcingAmount(visaChange.getOutsourcingAmount()+"");
            infoVo.setVisaChangeReason(visaChange.getVisaChangeReason());

//   ---------------------下家
            //            申请表
            infoVo.setApplicantNameXia(vcif.getApplicantName());
            infoVo.setRemarkXia(vcif.getRemark());
            infoVo.setSubmitMoneyXia(vcif.getSubmitMoney());
//            信息表
            infoVo.setAmountVisaChangeXia(visaChange.getAmountVisaChange()+"");
            infoVo.setContractAmountXia(visaChange.getContractAmount());
            infoVo.setCompileTimeXia(visaChange.getCompileTime());
            infoVo.setCompletionTimeXia(visaChange.getCompletionTime());
            infoVo.setProportionContractXia(visaChange.getProportionContract());
            infoVo.setOutsourcingXia(visaChange.getOutsourcing());
            infoVo.setNameOfCostUnitXia(visaChange.getNameOfCostUnit());
            infoVo.setContactXia(visaChange.getContact());
            infoVo.setContactNumberXia(visaChange.getContactNumber());
            infoVo.setOutsourcingAmountXia(visaChange.getOutsourcingAmount()+"");
            infoVo.setVisaChangeReasonXia(visaChange.getVisaChangeReason());

//            如果是下家就把xia家申请、信息表填进去
        }else if ("0".equals(infoVo.getUpAndDownMark())){
            infoVo.setApplicantNameShang(vcif.getApplicantName());
            infoVo.setRemarkShang(vcif.getRemark());
//            信息表
            infoVo.setAmountVisaChange(visaChange.getAmountVisaChange()+"");
            infoVo.setContractAmount(visaChange.getContractAmount());
            infoVo.setCompileTime(visaChange.getCompileTime());
            infoVo.setCompletionTime(visaChange.getCompletionTime());
            infoVo.setProportionContract(visaChange.getProportionContract());
            infoVo.setOutsourcing(visaChange.getOutsourcing());
            infoVo.setNameOfCostUnit(visaChange.getNameOfCostUnit());
            infoVo.setContact(visaChange.getContact());
            infoVo.setContactNumber(visaChange.getContactNumber());
            infoVo.setOutsourcingAmount(visaChange.getOutsourcingAmount()+"");
            infoVo.setVisaChangeReason(visaChange.getVisaChangeReason());
        }else if ("1".equals(infoVo.getUpAndDownMark())){
            infoVo.setApplicantNameXia(vcif.getApplicantName());
            infoVo.setRemarkXia(vcif.getRemark());
            infoVo.setSubmitMoneyXia(vcif.getSubmitMoney());
//            信息表
            infoVo.setAmountVisaChangeXia(visaChange.getAmountVisaChange()+"");
            infoVo.setContractAmountXia(visaChange.getContractAmount());
            infoVo.setCompileTimeXia(visaChange.getCompileTime());
            infoVo.setCompletionTimeXia(visaChange.getCompletionTime());
            infoVo.setProportionContractXia(visaChange.getProportionContract());
            infoVo.setOutsourcingXia(visaChange.getOutsourcing());
            infoVo.setNameOfCostUnitXia(visaChange.getNameOfCostUnit());
            infoVo.setContactXia(visaChange.getContact());
            infoVo.setContactNumberXia(visaChange.getContactNumber());
            infoVo.setOutsourcingAmountXia(visaChange.getOutsourcingAmount()+"");
            infoVo.setVisaChangeReasonXia(visaChange.getVisaChangeReason());
        }

        List<VisaChange> byNum = vcMapper.findByInfoOne();
        for (VisaChange thisNum : byNum) {

            List<VisaChange> byInfoTwo = vcMapper.findByInfoTwo();
            if (byInfoTwo.size() >=2 ){

                for (int i = 0; i < byInfoTwo.size(); i++){
                    if ("0".equals(byInfoTwo.get(i).getUpAndDownMark())){
                        VisaChangeStatisticVo visaChangeStatisticVo = new VisaChangeStatisticVo();
                        visaChangeStatisticVo.setAmountVisaChangeShang(byInfoTwo.get(i).getId());
                        visaChangeStatisticVo.setProportionContractShang(byInfoTwo.get(i).getProportionContract());
                        visaChangeStatisticVo.setCreatorId(byInfoTwo.get(i).getCreatorId());
                        visaChangeStatisticVo.setCreateTime(byInfoTwo.get(i).getCreateTime());
                        visaChangeStatisticVo.setCompileTime(byInfoTwo.get(i).getCompileTime());
                    }else if ("1".equals(byInfoTwo.get(i).getUpAndDownMark())){
                        VisaChangeStatisticVo visaChangeStatisticVo = new VisaChangeStatisticVo();
                        visaChangeStatisticVo.setAmountVisaChangeXia(byInfoTwo.get(i).getId());
                        visaChangeStatisticVo.setProportionContractXia(byInfoTwo.get(i).getProportionContract());
                        visaChangeStatisticVo.setCreatorId(byInfoTwo.get(i).getCreatorId());
                        visaChangeStatisticVo.setCreateTime(byInfoTwo.get(i).getCreateTime());
                        visaChangeStatisticVo.setCompileTime(byInfoTwo.get(i).getCompileTime());
                    }
                }
            }
        }


//        签证变更次数

//        上下家签证累计金额


//        累计占上下家合同比例

   return null;
    }


    /***
     * 批量审核
     * @param batchReviewVo
     */
    @Override
    public void approvalProcess(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        if (split.length > 0) {
            for (String s : split) {
                if (StringUtil.isNotEmpty(s)) {
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId", s).andEqualTo("auditResult", "0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    VisaChange visaChange = vcMapper.selectById(s);

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
                            auditInfo1.setAuditType("1");
                            Example example1 = new Example(MemberManage.class);
                            example1.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
                            MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                            auditInfo1.setAuditorId(memberManage.getId());
//
                            auditInfoDao.insertSelective(auditInfo1);
                        }
//                        判断二级审核通过
                        if ("1".equals(auditInfo.getAuditType())) {
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
     * 提交
     * @param
     */
    @Override
    public void addVisChangeVo(VisaChangeInfoVo visaChangeInfoVo, UserInfo loginUser) {

        //TODO 需要改
        visaChangeInfoVo.setLoginUserId("123");
        VisaChange visaChange = new VisaChange();
        VisaChangeInformation vcif = new VisaChangeInformation();

        String id1 = UUID.randomUUID().toString().replace("-", "");
        String id2 = UUID.randomUUID().toString().replace("-", "");
        String id3 = UUID.randomUUID().toString().replace("-", "");
        String id4 = UUID.randomUUID().toString().replace("-", "");


//        判断上家签证变更信息表 并赋值

        if (StringUtil.isNotEmpty(visaChangeInfoVo.getApplicantNameShang()) && StringUtil.isNotEmpty(visaChangeInfoVo.getApplicantNameXia())) {


            VisaChange visaChange1 = new VisaChange();
            VisaChange visaChange2 = new VisaChange();

            visaChange1.setCreatorId(visaChangeInfoVo.getLoginUserId());
            visaChange1.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange1.setStatus("0");
            visaChange1.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange1.setState("0");
            visaChange1.setCreateTime(new SimpleDateFormat().format(new Date()));
            visaChange1.setId(id1);
            visaChange1.setUpAndDownMark("0");
            visaChange1.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange1.setContractAmount(visaChangeInfoVo.getContractAmount());
            visaChange1.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange1.setCompletionTime(visaChangeInfoVo.getCompletionTime());
            visaChange1.setProportionContract(visaChangeInfoVo.getProportionContract());
            visaChange1.setOutsourcing(visaChangeInfoVo.getOutsourcing());
            visaChange1.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
            visaChange1.setContact(visaChangeInfoVo.getContact());
            visaChange1.setContactNumber(visaChangeInfoVo.getContactNumber());
            visaChange1.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            visaChange1.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());


            visaChange2.setId(id2);
            visaChange2.setCreatorId(visaChangeInfoVo.getLoginUserId());
            visaChange2.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange2.setStatus("0");
            visaChange2.setState("0");
            visaChange2.setCreateTime(new SimpleDateFormat().format(new Date()));
            visaChange2.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());

            visaChange2.setUpAndDownMark("1");
            visaChange2.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeXia()));
            visaChange2.setContractAmount(visaChangeInfoVo.getContractAmountXia());
            visaChange2.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeXia()));
            visaChange2.setCompletionTime(visaChangeInfoVo.getCompletionTimeXia());
            visaChange2.setProportionContract(visaChangeInfoVo.getProportionContractXia());
            visaChange2.setOutsourcing(visaChangeInfoVo.getOutsourcingXia());
            visaChange2.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitXia());
            visaChange2.setContact(visaChangeInfoVo.getContactXia());
            visaChange2.setContactNumber(visaChangeInfoVo.getContactNumberXia());
            visaChange2.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmountXia());
            visaChange2.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonXia());

            vcMapper.insertSelective(visaChange1);
            vcMapper.insertSelective(visaChange2);


//            判断上家签证变更申请表 并赋值
        } else if (StringUtil.isNotEmpty(visaChangeInfoVo.getApplicantNameShang())) {
            vcif.setId(UUID.randomUUID().toString().replace("-", ""));

            visaChange.setId(id3);
            visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());
            visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange.setStatus("0");
            visaChange.setState("0");
            visaChange.setUpAndDownMark("1");
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());

            visaChange.setCreateTime(new SimpleDateFormat().format(new Date()));
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange.setContractAmount(visaChangeInfoVo.getContractAmount());
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
            visaChange.setProportionContract(visaChangeInfoVo.getProportionContract());
            visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
            visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
            visaChange.setContact(visaChangeInfoVo.getContact());
            visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
            visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());

            vcMapper.insertSelective(visaChange);


        } else if (StringUtil.isNotEmpty(visaChangeInfoVo.getApplicantNameXia())) {


            visaChange.setId(id4);
            visaChange.setCreatorId(visaChangeInfoVo.getLoginUserId());
            visaChange.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange.setUpAndDownMark("0");
            visaChange.setStatus("0");
            visaChange.setState("0");
            visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());

            visaChange.setCreateTime(new SimpleDateFormat().format(new Date()));
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeXia()));
            visaChange.setContractAmount(visaChangeInfoVo.getContractAmountXia());
            visaChange.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChangeXia()));
            visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTimeXia());
            visaChange.setProportionContract(visaChangeInfoVo.getProportionContractXia());
            visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingXia());
            visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitXia());
            visaChange.setContact(visaChangeInfoVo.getContactXia());
            visaChange.setContactNumber(visaChangeInfoVo.getContactNumberXia());
            visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmountXia());
            visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonXia());
            vcMapper.insertSelective(visaChange);
        }

//        判断上家签证变更信息表 并赋值
        if (StringUtil.isNotEmpty(visaChangeInfoVo.getAmountVisaChange()) && StringUtil.isNotEmpty(visaChangeInfoVo.getAmountVisaChangeXia())) {

            VisaChangeInformation vcif1 = new VisaChangeInformation();
            VisaChangeInformation vcif2 = new VisaChangeInformation();


            vcif1.setId(UUID.randomUUID().toString().replace("-", ""));
            vcif1.setCreateTime(new SimpleDateFormat().format(new Date()));
            vcif1.setBaseProjectId(id1);
            vcif1.setFouderId(visaChangeInfoVo.getLoginUserId());
            vcif1.setFouderCompany(visaChangeInfoVo.getLoginUserId());
            vcif1.setUpAndDownMark("0");
            vcif1.setStatus("1");
            vcif.setState("0");
            vcif1.setApplicantName(visaChangeInfoVo.getApplicantNameShang());
            vcif1.setRemark(visaChangeInfoVo.getRemarkShang());

            vacifMapper.insertSelective(vcif1);

            vcif2.setId(UUID.randomUUID().toString().replace("-", ""));
            vcif2.setCreateTime(new SimpleDateFormat().format(new Date()));
            vcif2.setBaseProjectId(id2);
            vcif2.setFouderId(visaChangeInfoVo.getLoginUserId());
            vcif2.setFouderCompany(visaChangeInfoVo.getLoginUserId());
            vcif2.setUpAndDownMark("0");
            vcif2.setStatus("1");
            vcif.setState("0");
            vcif2.setApplicantName(visaChangeInfoVo.getApplicantNameXia());
            vcif2.setRemark(visaChangeInfoVo.getRemarkXia());


            vacifMapper.insertSelective(vcif2);
//        判断下家签证变更信息表 并赋值
        } else if (StringUtil.isEmpty(visaChangeInfoVo.getApplicantNameShang())) {

            vcif.setCreateTime(new SimpleDateFormat().format(new Date()));
            vcif.setBaseProjectId(id3);
            vcif.setFouderId(visaChangeInfoVo.getLoginUserId());
            vcif.setFouderCompany(visaChangeInfoVo.getLoginUserId());
            vcif.setUpAndDownMark("0");
            vcif.setStatus("1");
            vcif.setState("0");
            vcif.setApplicantName(visaChangeInfoVo.getApplicantNameShang());
            vcif.setRemark(visaChangeInfoVo.getRemarkShang());

            vacifMapper.insertSelective(vcif);
        } else if (StringUtil.isEmpty(visaChangeInfoVo.getApplicantNameXia())) {


            vcif.setId(UUID.randomUUID().toString().replace("-", ""));
            vcif.setCreateTime(new SimpleDateFormat().format(new Date()));
            vcif.setBaseProjectId(id4);
            vcif.setFouderId(visaChangeInfoVo.getLoginUserId());
            vcif.setFouderCompany(visaChangeInfoVo.getLoginUserId());
            vcif.setUpAndDownMark("0");
            vcif.setStatus("1");
            vcif.setState("0");
            vcif.setApplicantName(visaChangeInfoVo.getApplicantNameXia());
            vcif.setRemark(visaChangeInfoVo.getRemarkXia());
            vcif.setSubmitMoney(visaChangeInfoVo.getSubmitMoneyXia());

            vacifMapper.insertSelective(vcif);


        }


//           把数据提交到审核表里
        if (StringUtil.isNotEmpty(visaChangeInfoVo.getAuditId())) {


            AuditInfo auditInfo = new AuditInfo();

            auditInfo.setFounderId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setCompanyId(visaChangeInfoVo.getLoginUserId());
            auditInfo.setAuditorId(visaChangeInfoVo.getAuditId());
            auditInfo.setStatus("0");
            auditInfo.setAuditType("0");

            auditInfo.setAuditResult("0");
            auditInfo.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            auditInfo.setAuditOpinion(visaChangeInfoVo.getAuditOpinion());
            Date date = new Date();
            String createTime = new SimpleDateFormat().format(date);
            auditInfo.setCreateTime(createTime);

            vcMapper.insertSelective(visaChange);
            vacifMapper.insertSelective(vcif);
            auditInfoDao.insertSelective(auditInfo);

        }
    }


}

package net.zlw.cloud.VisaChange.service.impl;


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
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
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

    @Autowired
    private BaseProjectService baseProjectService;

    @Autowired
    private FileInfoMapper fileInfoMapper;


    /***
     * 分页查询所有
     */
    @Override
    public ArrayList<VisaChangeVo> findAllPage(VisaChangeVo visaChangeVO, UserInfo loginUser) {

        //TODO 需要改

        visaChangeVO.setLoginUserId(loginUser.getId());
        List<VisaChangeVo> all = null;
        //未审核
        if ("1".equals(visaChangeVO.getStatus())) {

            all = vcMapper.findByNoExamine(visaChangeVO);
            System.err.println(visaChangeVO.getStatus());

//           未通过
        } else if ("3".equals(visaChangeVO.getStatus())) {
            all = vcMapper.findByNotPass(visaChangeVO);

//            已完成
        } else {
            all = vcMapper.findAll(visaChangeVO);
        }
        List<VisaChangeVo> all1 = vcMapper.findAll(visaChangeVO);
        all.addAll(all1);
        if (visaChangeVO.getStatus().equals("1")){
            for (int i = 0; i < all1.size(); i++) {
                if (all1.get(i).getAuditorId()!=null){
                    if (!all1.get(i).getAuditorId().equals(loginUser.getId())){
                        all1.remove(i);
                        i--;
                    }
                }else {
                    all1.remove(i);
                    i--;
                }
            }
            }
        if (visaChangeVO.getStatus().equals("3")){
            for (int i = 0; i < all1.size(); i++) {
                if (!all1.get(i).getFounderId().equals(loginUser.getId())){
                    all1.remove(i);
                    i--;
                }
            }
        }


        all = all1;


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
        ArrayList<VisaChangeVo> visaChangeVos = new ArrayList<>();
        for (VisaChangeVo visaChangeVo : all) {
            if (! visaChangeVos.contains(visaChangeVo)){
                visaChangeVos.add(visaChangeVo);
            }
        }


        return visaChangeVos;

    }

    @Override
    public void delete(String id) {
        Example example = new Example(VisaChange.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        List<VisaChange> visaChanges = vcMapper.selectByExample(example);
        if (visaChanges!=null && visaChanges.size()!=0){
            for (VisaChange visaChange : visaChanges) {
                visaChange.setState("1");
                vcMapper.updateByPrimaryKeySelective(visaChange);
                VisaChangeInformation visaChangeInformation = applyMapper.selectByPrimaryKey(visaChange.getApplyChangeInfoId());
                if (visaChangeInformation!=null){
                    visaChangeInformation.setState("1");
                    applyMapper.updateByPrimaryKeySelective(visaChangeInformation);
                }
                Example example1 = new Example(AuditInfo.class);
                Example.Criteria cc = example1.createCriteria();
                cc.andEqualTo("baseProjectId",visaChange.getId());
                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example1);
                if (auditInfos!=null && auditInfos.size()!=0){
                    for (AuditInfo auditInfo : auditInfos) {
                        auditInfo.setStatus("1");
                        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    }
                }
            }
        }
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
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
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
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
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

        if (split.length!=0){
            for (String s : split) {

                VisaChange visaChange1 = vcMapper.selectByPrimaryKey(s);
                if (!visaChange1.getUpAndDownMark().equals("1")){
                    Example example = new Example(VisaChange.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",visaChange1.getBaseProjectId());
                    c.andEqualTo("state","0");
                    c.andEqualTo("upAndDownMark","1");
                    VisaChange visaChange = vcMapper.selectOneByExample(example);
                    s = visaChange.getId();
                }


                VisaChange visaChange = vcMapper.selectByPrimaryKey(s);
                Example example = new Example(AuditInfo.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("baseProjectId",visaChange.getId());
                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
                for (AuditInfo auditInfo : auditInfos) {
                    //通过
                    if (batchReviewVo.getAuditResult().equals("1")){
                        //一审
                        if (auditInfo.getAuditResult().equals("0") && auditInfo.getAuditType().equals("0")){
                            auditInfo.setAuditResult("1");
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            auditInfo.setAuditTime(simpleDateFormat.format(new Date()));
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                            //加入二审
                            AuditInfo auditInfo1 = new AuditInfo();
                            auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                            auditInfo1.setBaseProjectId(s);
                            auditInfo1.setAuditResult("0");
                            auditInfo1.setAuditType("1");
                            Example example1 = new Example(MemberManage.class);
                            Example.Criteria c = example1.createCriteria();
                            c.andEqualTo("depId","2");
                            c.andEqualTo("depAdmin","1");
                            MemberManage manage = memberManageDao.selectOneByExample(example1);
                            auditInfo1.setAuditorId(manage.getId());
                            auditInfo1.setStatus("0");
                            auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                            auditInfoDao.insertSelective(auditInfo1);

                        }else if(auditInfo.getAuditResult().equals("1") && auditInfo.getAuditType().equals("0")){
                            auditInfo.setAuditResult("1");
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                            auditInfo.setAuditTime(sim.format(new Date()));
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                            baseProject.setVisaStatus("5");
                            baseProjectDao.updateByPrimaryKeySelective(baseProject);
                        }

                        //不通过
                    }else if(batchReviewVo.getAuditResult().equals("2")){
                            auditInfo.setAuditResult("2");
                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                            auditInfo.setAuditTime(sim.format(new Date()));
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                        baseProject.setVisaStatus("3");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);

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
        visaChangeInfoVo.setLoginUserId(loginUser.getId());

        String createTime = sdf.format(new Date());

        //提交
        if (StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())) {
            insertVisaInfoAndApplyInfo(visaChangeInfoVo, createTime);

        } else {//保存

            insertVisaInfoAndApplyInfo(visaChangeInfoVo, createTime);

        }
    }


    private void insertVisaInfoAndApplyInfo(VisaChangeInfoVo visaChangeInfoVo, String createTime) {

        VisaChangeInformation upvisachange = new VisaChangeInformation();
        VisaChangeInformation downvisachange = new VisaChangeInformation();
        if ("".equals(visaChangeInfoVo.getAmountVisaChange())){
                visaChangeInfoVo.setAmountVisaChange(new BigDecimal("0").toString());
        }
        if ("".equals(visaChangeInfoVo.getAmountVisaChangeDown())){
            visaChangeInfoVo.setAmountVisaChangeDown(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getContractAmount())){
            visaChangeInfoVo.setAmountVisaChange(new BigDecimal("0").toString());
        }
        if ("".equals(visaChangeInfoVo.getContractAmountDown())){
            visaChangeInfoVo.setContractAmountDown(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getOutsourcingAmount())){
            visaChangeInfoVo.setOutsourcingAmount(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getOutsourcingAmountDown())){
            visaChangeInfoVo.setOutsourcingAmountDown(new BigDecimal("0").toString());
        }
        if ("".equals(visaChangeInfoVo.getSubmitMoneyDown())){
            visaChangeInfoVo.setSubmitMoneyDown(new BigDecimal("0").toString());
        }


        //判断上家签证/变更申请信息
        String applicantNameUp = visaChangeInfoVo.getApplicantNameUp();
        if (StringUtil.isNotEmpty(applicantNameUp)) {
            VisaChangeInformation applyChangeInformation = new VisaChangeInformation();
            upvisachange = applyChangeInformation;
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
            //todo 文件上传
            List<FileInfo> byFreignAndType1 = fileInfoMapper.findByFreignAndType(visaChangeInfoVo.getKey(), visaChangeInfoVo.getType1());
            for (FileInfo fileInfo : byFreignAndType1) {
                fileInfo.setPlatCode(applyChangeInformation.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
            applyMapper.insertSelective(applyChangeInformation);
        }
        //判断下家签证/变更申请信息
        String applicantNameDown = visaChangeInfoVo.getApplicantNameDown();
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
            downvisachange = applyChangeInformation;
            downvisachange.setId(UUID.randomUUID().toString().replace("-",""));
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                applyChangeInformation.setStatus("1");
            }else{
                applyChangeInformation.setStatus("2");
            }
            //todo 文件上传
            List<FileInfo> byFreignAndType2 = fileInfoMapper.findByFreignAndType(visaChangeInfoVo.getKey(), visaChangeInfoVo.getType2());
            for (FileInfo fileInfo : byFreignAndType2) {
                fileInfo.setPlatCode(applyChangeInformation.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
            applyMapper.insertSelective(applyChangeInformation);


        //本次上家签证/变更信息
        VisaChange visaChange1 = new VisaChange();
        String amountVisaChange = visaChangeInfoVo.getAmountVisaChange();
        if (StringUtil.isNotEmpty(amountVisaChange)) {
            visaChange1 = new VisaChange();
            visaChange1.setId(UUID.randomUUID().toString().replace("-", ""));
            visaChange1.setCreatorCompanyId(visaChangeInfoVo.getLoginUserId());
            visaChange1.setCreateTime(createTime);
            visaChange1.setUpdateTime(createTime);
            visaChange1.setContractAmount(visaChangeInfoVo.getContractAmount());
            visaChange1.setCreatorId(visaChangeInfoVo.getLoginUserId());

            //0正常1删除
            visaChange1.setState("0");
            visaChange1.setAmountVisaChange(Double.parseDouble(visaChangeInfoVo.getAmountVisaChange()));
            visaChange1.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange1.setCompileTime(createTime);
            visaChange1.setCompletionTime(visaChangeInfoVo.getCompletionTime());
            visaChange1.setOutsourcing(visaChangeInfoVo.getOutsourcing());
            visaChange1.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
            visaChange1.setContact(visaChangeInfoVo.getContact());
            visaChange1.setContactNumber(visaChangeInfoVo.getContactNumber());
            if(!"1".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange1.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
            visaChange1.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());
            visaChange1.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
            visaChange1.setUpAndDownMark("0");
            //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
            if(StringUtils.isNotEmpty(visaChangeInfoVo.getAuditId())){
                visaChange1.setStatus("1");
            }else{
                visaChange1.setStatus("2");
            }
            visaChange1.setProportionContract(visaChangeInfoVo.getProportionContract());
            visaChange1.setChangeNum("1");
            if (upvisachange.getId()!=null){
                visaChange1.setApplyChangeInfoId(upvisachange.getId());
            }
            //todo 文件上传
            List<FileInfo> byFreignAndType3 = fileInfoMapper.findByFreignAndType(visaChangeInfoVo.getKey(), visaChangeInfoVo.getType3());
            for (FileInfo fileInfo : byFreignAndType3) {
                fileInfo.setPlatCode(visaChange1.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
            vcMapper.insertSelective(visaChange1);
        }

        //本次下家签证/变更信息
        VisaChange visaChange = new VisaChange();
        String amountVisaChangeDown = visaChangeInfoVo.getAmountVisaChangeDown();
        if (StringUtil.isNotEmpty(amountVisaChangeDown)) {
             visaChange = new VisaChange();
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
            visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTimeDown());
            visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
            visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
            visaChange.setContact(visaChangeInfoVo.getContactDown());
            visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
            if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
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

            visaChange.setApplyChangeInfoId(downvisachange.getId());
            //todo 文件上传
            List<FileInfo> byFreignAndType4 = fileInfoMapper.findByFreignAndType(visaChangeInfoVo.getKey(), visaChangeInfoVo.getType4());
            for (FileInfo fileInfo : byFreignAndType4) {
                fileInfo.setPlatCode(visaChange.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
            vcMapper.insertSelective(visaChange);
        }


        if (visaChangeInfoVo.getAuditId()!=null && !visaChangeInfoVo.getAuditId().equals("")){
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            if (visaChange!=null){
                auditInfo.setBaseProjectId(visaChange.getId());
            }else if(visaChange1!=null){
                auditInfo.setBaseProjectId(visaChange1.getId());
            }
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(visaChangeInfoVo.getAuditId());
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(sim.format(new Date()));
            auditInfoDao.insertSelective(auditInfo);
            AuditInfo auditInfo1 = new AuditInfo();
            auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
            auditInfo1.setBaseProjectId(downvisachange.getId());
            auditInfo1.setAuditResult("0");
            auditInfo1.setAuditType("0");
            auditInfo1.setAuditorId(visaChangeInfoVo.getAuditId());
            auditInfo1.setStatus("0");
            auditInfo1.setCreateTime(sim.format(new Date()));
            auditInfoDao.insertSelective(auditInfo1);
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeInfoVo.getBaseProjectId());
            baseProject.setVisaStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }else {
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeInfoVo.getBaseProjectId());
            baseProject.setVisaStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }
    }


    //编辑提交/保存
    @Override
    public void submitOrSave(VisaChangeInfoVo visaChangeInfoVo) {


        if ("".equals(visaChangeInfoVo.getAmountVisaChange())){
            visaChangeInfoVo.setAmountVisaChange(new BigDecimal("0").toString());
        }
        if ("".equals(visaChangeInfoVo.getAmountVisaChangeDown())){
            visaChangeInfoVo.setAmountVisaChangeDown(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getContractAmount())){
            visaChangeInfoVo.setAmountVisaChange(new BigDecimal("0").toString());
        }
        if ("".equals(visaChangeInfoVo.getContractAmountDown())){
            visaChangeInfoVo.setContractAmountDown(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getOutsourcingAmount())){
            visaChangeInfoVo.setOutsourcingAmount(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getOutsourcingAmountDown())){
            visaChangeInfoVo.setOutsourcingAmountDown(new BigDecimal("0").toString());
        }

        if ("".equals(visaChangeInfoVo.getSubmitMoneyDown())){
            visaChangeInfoVo.setSubmitMoneyDown(new BigDecimal("0").toString());
        }


        if (visaChangeInfoVo.getAuditNumber()==null){
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeInfoVo.getBaseProjectId());
            baseProject.setVisaStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }else if(visaChangeInfoVo.getAuditNumber().equals("1")){

            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));

            VisaChange visaChange = vcMapper.selectByPrimaryKey(visaChangeInfoVo.getId());
            if (visaChange.getUpAndDownMark().equals("0")){
                Example example = new Example(VisaChange.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",visaChangeInfoVo.getBaseProjectId());
                c.andEqualTo("state","0");
                c.andEqualTo("upAndDownMark","1");
                VisaChange visaChange1 = vcMapper.selectOneByExample(example);
                visaChangeInfoVo.setId(visaChange1.getId());
            }
            auditInfo.setBaseProjectId(visaChangeInfoVo.getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(visaChangeInfoVo.getAuditId());
            auditInfo.setStatus("0");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String format = simpleDateFormat.format(new Date());
            auditInfo.setCreateTime(format);
            auditInfoDao.insertSelective(auditInfo);

            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeInfoVo.getBaseProjectId());
            baseProject.setVisaStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }else if(visaChangeInfoVo.getAuditNumber().equals("2")){
            VisaChange visaChange = vcMapper.selectByPrimaryKey(visaChangeInfoVo.getId());
            if (visaChange.getUpAndDownMark().equals("0")){
                Example example = new Example(VisaChange.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",visaChange.getBaseProjectId());
                c.andEqualTo("state","0");
                c.andEqualTo("upAndDownMark","1");
                VisaChange visaChange1 = vcMapper.selectOneByExample(example);
                visaChangeInfoVo.setId(visaChange1.getId());
            }
            Example example = new Example(AuditInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",visaChangeInfoVo.getId());
            c.andEqualTo("status","0");
            c.andEqualTo("auditResult","2");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
            auditInfo.setAuditResult("0");
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeInfoVo.getBaseProjectId());
            baseProject.setVisaStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }

        String updateTime = sdf.format(new Date());

        Example example = new Example(VisaChange.class);
        example.createCriteria().andEqualTo("id", visaChangeInfoVo.getId());
        VisaChange visaChange = vcMapper.selectOneByExample(example);
        System.err.println(visaChange);

        Example example1 = new Example(AuditInfo.class);
        example1.createCriteria().andEqualTo("id", visaChange.getBaseProjectId());
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);

        Example example2 = new Example(VisaChangeInformation.class);
        example2.createCriteria().andEqualTo("id", visaChange.getApplyChangeInfoId());
        VisaChangeInformation applyChangeInformation = applyMapper.selectOneByExample(example2);

        //                判断如果一级审核通过更改状态
        if (auditInfo!=null && "1".equals(auditInfo.getAuditResult())) {
            if ("0".equals(auditInfo.getAuditType())) {
                auditInfo.setAuditResult("1");
//                        一级审批的意见,时间
                auditInfo.setAuditTime(sdf.format(new Date()));
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
            } else if (auditInfo!=null && "1".equals(auditInfo.getAuditType())) {
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
        } else if (auditInfo!=null && "2".equals(auditInfo.getAuditResult())) {
            auditInfo.setAuditResult("2");
            visaChange.setStatus("3");
            auditInfo.setAuditTime(sdf.format(new Date()));
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

//            auditInfoDao.updateByPrimaryKeySelective(auditInfo);


            String applicantNameUp = visaChangeInfoVo.getApplicantNameUp();
            if (StringUtil.isNotEmpty(applicantNameUp)) {
                applyChangeInformation.setApplicantName(applicantNameUp);
                applyChangeInformation.setRemark(visaChangeInfoVo.getRemarkUp());
                applyChangeInformation.setUpdateTime(updateTime);
                applyChangeInformation.setFouderId(visaChangeInfoVo.getLoginUserId());

                //0:正常 1:删除
                applyChangeInformation.setState("0");
                applyChangeInformation.setBaseProjectId(visaChangeInfoVo.getBaseProject().getId());
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
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
                visaChange.setContact(visaChangeInfoVo.getContact());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
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
//                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
                visaChange.setContact(visaChangeInfoVo.getContactDown());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
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
//                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcing());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnit());
                visaChange.setContact(visaChangeInfoVo.getContact());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumber());
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReason());
//                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
//                visaChange.setUpAndDownMark("0");
                //1待审核 2处理中 3未通过 4待确认 5进行中 6已完成
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
//                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
                visaChange.setCompileTime(updateTime);
                visaChange.setCompletionTime(visaChangeInfoVo.getCompletionTime());
                visaChange.setOutsourcing(visaChangeInfoVo.getOutsourcingDown());
                visaChange.setNameOfCostUnit(visaChangeInfoVo.getNameOfCostUnitDown());
                visaChange.setContact(visaChangeInfoVo.getContactDown());
                visaChange.setContactNumber(visaChangeInfoVo.getContactNumberDown());
                if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                    visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
                }
                visaChange.setVisaChangeReason(visaChangeInfoVo.getVisaChangeReasonDown());
//                visaChange.setBaseProjectId(visaChangeInfoVo.getBaseProjectId());
//                visaChange.setUpAndDownMark("1");

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

    @Override
    public List<VisaChangeStatisticVo> selectListById(String id) {
        VisaChangeInfoVo visaChangeInfoVo = new VisaChangeInfoVo();

        VisaChange visaChange = vcMapper.selectByPrimaryKey(id);
//        签证变更次序
        VisaChange byChangNum = vcMapper.selectByChangNum();

        //项目信息
        String baseProjectId = visaChange.getBaseProjectId();

        //获取上下家签证/变更申请信息
        Example example = new Example(VisaChangeInformation.class);


        List<VisaChange> a = vcMapper.findByBaseProjectId(baseProjectId);

        //签证/变更统计信息
        visaChangeInfoVo.setChangeNum(visaChange.getChangeNum());

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
            if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
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
            visaChangeInfoVo.setChangeStatisticVos(changeStatisticVos);

        }
        if ("1".equals(visaChange.getUpAndDownMark())) {

            visaChangeInfoVo.setChangeDownId(visaChange.getId());
            visaChangeInfoVo.setAmountVisaChange(visaChange.getAmountVisaChange()+"");
            visaChangeInfoVo.setAmountVisaChangeDown(visaChange.getAmountVisaChange() + "");
            visaChangeInfoVo.setContractAmountDown(visaChange.getContractAmount());
            visaChangeInfoVo.setCompileTimeDown(visaChange.getCompileTime());
            visaChangeInfoVo.setCompletionTimeDown(visaChange.getCompletionTime());
            visaChangeInfoVo.setProportionContractDown(visaChange.getProportionContract());
            visaChangeInfoVo.setOutsourcingDown(visaChange.getOutsourcing());
            visaChangeInfoVo.setNameOfCostUnitDown(visaChange.getNameOfCostUnit());
            visaChangeInfoVo.setContactDown(visaChange.getContact());
            visaChangeInfoVo.setContactNumberDown(visaChange.getContactNumber());
            if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
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
                visaChangeInfoVo.setChangeStatisticVos(changeStatisticVos);
            }
        }
        return changeStatisticVos;
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
        BaseProject baseProject = baseProjectService.findById(baseProjectId);
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
                if (thisA.getProportionContract()==null){
                    thisA.setProportionContract("0");
                }
                if (totalDownRate!=null){
                    totalUpRate = totalDownRate.add(new BigDecimal(thisA.getProportionContract()));
                }
            } else {
                //累积下家签证变更金额
                totalDown = totalDown.add(new BigDecimal(thisA.getAmountVisaChange()));
                //累积下家签证变更占比
                if (thisA.getProportionContract()==null){
                    thisA.setProportionContract("0");
                }
                if (totalDownRate!=null){
                    totalDownRate = totalDownRate.add(new BigDecimal(thisA.getProportionContract()));
                }

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
            if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
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
            visaChangeInfoVo.setChangeStatisticVos(changeStatisticVos);

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
            if(!"".equals(visaChangeInfoVo.getOutsourcingAmount())){
                visaChange.setOutsourcingAmount(visaChangeInfoVo.getOutsourcingAmount());
            }
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
                visaChangeInfoVo.setChangeStatisticVos(changeStatisticVos);
            }
        }
        VisaChange visaChange1 = vcMapper.selectByPrimaryKey(id);
        if (!visaChange1.getUpAndDownMark().equals("1")){
            Example example1 = new Example(VisaChange.class);
            Example.Criteria c = example1.createCriteria();
            c.andEqualTo("baseProjectId",visaChange1.getBaseProjectId());
            c.andEqualTo("upAndDownMark","1");
            c.andEqualTo("state","0");
            VisaChange visaChange2 = vcMapper.selectOneByExample(example1);
            visaChange1 = visaChange2;
        }

        Example example1 = new Example(AuditInfo.class);
        Example.Criteria c = example1.createCriteria();
        c.andEqualTo("baseProjectId",visaChange1.getId());
        c.andEqualTo("auditResult","0");
        AuditInfo auditInfos = auditInfoDao.selectOneByExample(example1);
        if (auditInfos!=null){
            if (auditInfos.getAuditType().equals("0")){
                visaChangeInfoVo.setAuditType("0");
            }else if (auditInfos.getAuditType().equals("1")){
                visaChangeInfoVo.setAuditType("1");
            }
        }

        return visaChangeInfoVo;
    }


}

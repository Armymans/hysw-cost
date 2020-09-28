package net.zlw.cloud.VisaChange.service.impl;


import com.github.pagehelper.PageHelper;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.aspectj.weaver.ast.Var;
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


    /***
     * 分页查询所有
     */
    @Override
    public List<VisaChangeVO> findAllPage(VisaChangeVO visaChangeVO, UserInfo loginUser) {
        PageHelper.startPage(visaChangeVO.getPageNum(), visaChangeVO.getPageSize());
        visaChangeVO.setLoginUserId("123");
        List<VisaChangeVO> all = vcMapper.findAll(visaChangeVO);

        BigDecimal shang = new BigDecimal(0);
        BigDecimal xia = new BigDecimal(0);
        for (VisaChangeVO thisAll : all) {
            String baseProjectId = thisAll.getBaseProjectId();
            List<VisaChange> a = vcMapper.findByBaseProjectId(baseProjectId);
            for (VisaChange visaChange : a) {
                if("0".equals(visaChange.getUpAndDownMark())){
                    shang = shang.add(new BigDecimal(visaChange.getContractAmount()));
                }else{
                    xia = xia.add(new BigDecimal(visaChange.getContractAmount()));
                }
            }
            thisAll.setContractAmountShang(shang.toString());
            thisAll.setContractAmountXia(xia.toString());
        }






        return all;
    }

    @Override
    public void delete(String id) {
        vcMapper.deleteById(id);
    }

    @Override
    public VisaChange selectById(String id) {
        VisaChange visaChange = vcMapper.selectByPrimaryKey(id);
        return visaChange;
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

}

package net.zlw.cloud.warningDetails.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.mapper.WarningDetailsMapper;
import net.zlw.cloud.warningDetails.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 风险预警详情数据回显
 * Created by xulei on 2020/9/21.
 */
@Service
@Transactional
public class WarningDetailsService {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private WarningDetailsMapper warningDetailsMapper;


    @Resource
    private MemberManageDao manageDao;

    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private MemberManageDao memberManageDao;



    /**
     * 根据 id查询数据
     *
     * @param id
     * @return
     */
    public WarningDetails findById(String id) {
        return warningDetailsMapper.selectByPrimaryKey(id);
    }


    /*
     *  根据查询到的数据进行修改
     * */
    public void update(WarningDetails warningDetailsOne) {
        warningDetailsMapper.updateByPrimaryKey(warningDetailsOne);
    }

    /**
     * 更新预警信息/保存审核信息
     *
     * @param id
     * @return
     */
    public WarningDetails warningFindById(String id, String instructions, UserInfo userInfo) {
        WarningDetails warningDetails = findById(id);
        warningDetails.setInstructions(instructions);
        warningDetails.setStatus("3");
        //更新预警信息
        warningDetailsMapper.updateByPrimaryKey(warningDetails);
        //查询审批人(部门负责人)
        MemberManage memberManage = manageDao.selectAdmin();
        //存审批
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setId(UUID.randomUUID().toString());
        auditInfo.setBaseProjectId(id);
        auditInfo.setAuditResult("0");
        auditInfo.setAuditType("risk");
        auditInfo.setAuditorId(memberManage.getId());
        auditInfo.setStatus("0");
        String date = sdf.format(new Date());
        auditInfo.setCreateTime(date);
        auditInfo.setFounderId(userInfo.getId());
        auditInfo.setCompanyId(userInfo.getCompanyId());
        auditInfoDao.insert(auditInfo);
        return warningDetails;
    }

    /**
     * 查询详情信息
     *
     * @param id
     * @param loginUser
     * @return
     */
    public WarningDetails detailById(String id, String userId, UserInfo loginUser) {
        //查询预警信息
        WarningDetails warningDetails = warningDetailsMapper.selectByPrimaryKey(id);
        warningDetails.setStatus("2");
        warningDetailsMapper.updateByPrimaryKeySelective(warningDetails);
        //获取审核信息
        AuditInfo auditInfo = auditInfoDao.findByTypeAndAuditorIdAndAuditResult(loginUser.getId(), id);
        //未审核
        if (auditInfo != null) {
            warningDetails.setAuditInfo(auditInfo);
        } else {
            //已审核或者未提交说明
            auditInfo = auditInfoDao.findByTypeAnd(id);
            if(auditInfo != null){
                if ("0".equals(auditInfo.getAuditResult())) {
                    auditInfo.setAuditResult("未审批");
                }
                if ("1".equals(auditInfo.getAuditResult())) {
                    auditInfo.setAuditResult("通过");
                }
                if ("2".equals(auditInfo.getAuditResult())){
                    auditInfo.setAuditResult("未通过");
                }
                MemberManage memberManage = manageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                auditInfo.setAuditorId(memberManage.getMemberName());
            }else{
                auditInfo = new AuditInfo();
            }
        }
        warningDetails.setAuditInfo(auditInfo);
        if (auditInfo.getAuditorId()!=null){
            if (auditInfo.getAuditorId().equals(loginUser.getId())){
                warningDetails.setCheckAudit("0");
            }else{
                warningDetails.setCheckAudit("1");
            }
        }else{
            warningDetails.setCheckAudit("1");
        }

        return warningDetails;
    }


    public void updateWarningDetails(WarningDetailAndAuditInfoVo warningDetailAndAuditInfoVo) {
        WarningDetails warningDetails = warningDetailsMapper.selectByPrimaryKey(warningDetailAndAuditInfoVo.getId());
        AuditInfo auditInfo = auditInfoDao.selectByPrimaryKey(warningDetailAndAuditInfoVo.getAid());
        if (warningDetails != null) {
            if ("1".equals(warningDetailAndAuditInfoVo.getAuditResult())) {
                warningDetails.setStatus("5");
            }else{
                warningDetails.setStatus("4");
            }
            warningDetails.setUpdateTime(sdf.format(new Date()));
            warningDetailsMapper.updateByPrimaryKeySelective(warningDetails);
        }
        if (auditInfo!=null){
            auditInfo.setAuditOpinion(warningDetailAndAuditInfoVo.getAuditOpinion());
            auditInfo.setAuditResult(warningDetailAndAuditInfoVo.getAuditResult());
            auditInfo.setAuditTime(sdf.format(new Date()));
            auditInfo.setUpdateTime(sdf.format(new Date()));
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
        }
    }

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");

    public void addDetails(DetailsVo detailsVo, UserInfo loginUser) {
        //发送人
        String id = loginUser.getId();
        //上级领导
        Example example = new Example(MemberManage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberRoleId","3");
        MemberManage memberManage = memberManageDao.selectOneByExample(example);
        //总经理
        Example example1 = new Example(MemberManage.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("memberRoleId","2");
        MemberManage memberManage1 = memberManageDao.selectOneByExample(example1);
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        list.add(detailsVo.getAuditId());
        list.add(memberManage.getId());
        list.add(memberManage1.getId());
        for (String s : list) {
            WarningDetails warningDetails = new WarningDetails();
            warningDetails.setId(UUID.randomUUID().toString().replace("-",""));
            warningDetails.setSender(id);
            warningDetails.setSendTime(sim.format(new Date()));
            warningDetails.setTitle(detailsVo.getDetails());
            warningDetails.setRiskType(detailsVo.getType());
            warningDetails.setRiskNotification(detailsVo.getDetails());
            warningDetails.setStatus("1");
            warningDetails.setRecipientId(s);
            warningDetails.setCreateTime(sim.format(new Date()));
            warningDetails.setFounderId(id);
            warningDetails.setDelFlag("0");
            warningDetails.setRiskTime(sim.format(new Date()));
            warningDetailsMapper.insertSelective(warningDetails);
        }
    }


    public List<WarningDetailsVo> findDetails(PageVo pageVo, UserInfo loginUser) {
     List<WarningDetailsVo> list =  warningDetailsMapper.findDetails(pageVo,"user320");
        for (WarningDetailsVo warningDetailsVo : list) {
            if (warningDetailsVo.getFounderId().equals("user320")){
                warningDetailsVo.setCheckInstructions("0");
            }else{
                warningDetailsVo.setCheckInstructions("1");
            }
        }
     return list;

    }
}

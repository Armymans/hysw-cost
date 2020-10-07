package net.zlw.cloud.warningDetails.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.mapper.WarningDetailsMapper;
import net.zlw.cloud.warningDetails.model.MemberManage;
import net.zlw.cloud.warningDetails.model.WarningDetailAndAuditInfoVo;
import net.zlw.cloud.warningDetails.model.WarningDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     * @return
     */
    public WarningDetails detailById(String id, String userId) {
        //查询预警信息
        WarningDetails warningDetails = warningDetailsMapper.selectByPrimaryKey(id);
        //获取审核信息
        AuditInfo auditInfo = auditInfoDao.findByTypeAndAuditorIdAndAuditResult(userId, id);
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
}

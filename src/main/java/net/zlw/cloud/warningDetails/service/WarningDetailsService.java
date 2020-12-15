package net.zlw.cloud.warningDetails.service;

import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.warningDetails.mapper.WarningDetailsMapper;
import net.zlw.cloud.warningDetails.model.*;
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
        String baseId = warningDetails.getBaseId();
        Example example = new Example(WarningDetails.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseId",baseId);
        criteria.andEqualTo("delFlag","0");
        List<WarningDetails> warningDetails1 = warningDetailsMapper.selectByExample(example);
        for (WarningDetails details : warningDetails1) {
            details.setInstructions(instructions);
            details.setStatus("3");
            //更新预警信息
            warningDetailsMapper.updateByPrimaryKey(details);
        }
        //查询审批人(部门负责人)
//        MemberManage memberManage = manageDao.selectAdmin();
        Example example1 = new Example(AuditInfo.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("baseProjectId",baseId+"#");
        criteria1.andEqualTo("status","0");
        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example1);
        if (auditInfo1 == null){
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userInfo.getId());
            String auditid = "";
            if (memberManage.getWorkType().equals("1")){
                auditid = whzjh;
            }else if(memberManage.getWorkType().equals("2")){
                auditid = wjzjh;
            }
            //存审批
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString());
            auditInfo.setBaseProjectId(baseId+"#");
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("risk");
            auditInfo.setAuditorId(auditid);
            auditInfo.setStatus("0");
            String date = sdf.format(new Date());
            auditInfo.setCreateTime(date);
            auditInfo.setFounderId(userInfo.getId());
//        auditInfo.setCompanyId("user88");
            auditInfoDao.insert(auditInfo);
        }else if(auditInfo1.getAuditResult().equals("2")){
            auditInfo1.setAuditResult("0");
            auditInfo1.setAuditTime("");
            auditInfo1.setAuditOpinion("");
            auditInfoDao.updateByPrimaryKeySelective(auditInfo1);
        }


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
//        warningDetails.setStatus("2");
//        warningDetailsMapper.updateByPrimaryKeySelective(warningDetails);
        //获取审核信息
        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",warningDetails.getBaseId()+"#");
        c.andEqualTo("status","0");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        //未审核
        if (auditInfo != null) {
            if (auditInfo.getAuditResult().equals("0")){
                auditInfo.setAuditResult("待审核");
            }else if(auditInfo.getAuditResult().equals("1")){
                auditInfo.setAuditResult("已通过");
            }else if(auditInfo.getAuditResult().equals("2")){
                auditInfo.setAuditResult("未通过");
            }

            if (auditInfo.getAuditorId()!=null){
                if (auditInfo.getAuditorId().equals(loginUser.getId())){
                    if (!auditInfo.getAuditResult().equals("已通过") && "3".equals(warningDetails.getStatus())){
                        warningDetails.setCheckAudit("1");
                    }else{
                        warningDetails.setCheckAudit("0");
                    }
                }else{
                    warningDetails.setCheckAudit("1");
                }
            }else{
                warningDetails.setCheckAudit("1");
            }

            MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
            if (memberManage!=null){
                auditInfo.setAuditorId(memberManage.getMemberName());
            }
            warningDetails.setAuditInfo(auditInfo);


        }else{
            warningDetails.setAuditInfo(new AuditInfo());
        }


        String sender = warningDetails.getSender();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(sender);
        if (memberManage!=null){
            warningDetails.setSender(memberManage.getMemberName());
        }
        return warningDetails;
    }


    public void updateWarningDetails(WarningDetailAndAuditInfoVo warningDetailAndAuditInfoVo) {
        WarningDetails warningDetails = warningDetailsMapper.selectByPrimaryKey(warningDetailAndAuditInfoVo.getId());
        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",warningDetails.getBaseId()+"#");
        criteria.andEqualTo("status","0");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        if (warningDetails != null) {
            if ("1".equals(warningDetailAndAuditInfoVo.getAuditResult())) {
                Example example1 = new Example(WarningDetails.class);
                Example.Criteria c = example1.createCriteria();
                c.andEqualTo("baseId",warningDetails.getBaseId());
                c.andEqualTo("delFlag","0");
                List<WarningDetails> warningDetails1 = warningDetailsMapper.selectByExample(example1);
                for (WarningDetails details : warningDetails1) {
                    details.setStatus("5");
                    details.setUpdateTime(sdf.format(new Date()));
                    warningDetailsMapper.updateByPrimaryKeySelective(details);
                }
            }else{
                Example example1 = new Example(WarningDetails.class);
                Example.Criteria c = example1.createCriteria();
                c.andEqualTo("baseId",warningDetails.getBaseId());
                c.andEqualTo("delFlag","0");
                List<WarningDetails> warningDetails1 = warningDetailsMapper.selectByExample(example1);
                for (WarningDetails details : warningDetails1) {
                    details.setStatus("4");
                    details.setUpdateTime(sdf.format(new Date()));
                    warningDetailsMapper.updateByPrimaryKeySelective(details);
                }
//                warningDetails.setStatus("4");
            }
//            warningDetails.setUpdateTime(sdf.format(new Date()));
//            warningDetailsMapper.updateByPrimaryKeySelective(warningDetails);
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
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(id);
        String id1 = "";
        if (memberManage!=null){
            if (memberManage.getWorkType().equals("1")){
                id1 = whzjh;
            }else if(memberManage.getWorkType().equals("2")){
                id1 = wjzjh;
            }
        }
        //总经理
        String id2 = "";
        if (memberManage!=null){
            if (memberManage.getWorkType().equals("1")){
                id2 = whzjm;
            }else if(memberManage.getWorkType().equals("2")){
                id2 = wjzjm;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        list.add(detailsVo.getAuditId());
        list.add(id1);
        list.add(id2);
        for (String s : list) {
            WarningDetails warningDetails = new WarningDetails();
            warningDetails.setId(UUID.randomUUID().toString().replace("-",""));
            warningDetails.setSender(id);
            warningDetails.setSendTime(sim.format(new Date()));
            warningDetails.setTitle(detailsVo.getRiskNotification());
            warningDetails.setRiskType(detailsVo.getType());
            warningDetails.setRiskNotification(detailsVo.getRiskNotification());
            warningDetails.setStatus("1");
            warningDetails.setRecipientId(s);
            warningDetails.setCreateTime(sim.format(new Date()));
            warningDetails.setFounderId(id);
            warningDetails.setDelFlag("0");
            warningDetails.setRiskTime(sim.format(new Date()));
            warningDetails.setBaseId(detailsVo.getBaseId());
            warningDetails.setElaborate(detailsVo.getDetails());
            warningDetailsMapper.insertSelective(warningDetails);
        }
    }


    public List<WarningDetailsVo> findDetails(PageVo pageVo, UserInfo loginUser) {
     List<WarningDetailsVo> list =  warningDetailsMapper.findDetails(pageVo,loginUser.getId());
        for (WarningDetailsVo warningDetailsVo : list) {
            if (warningDetailsVo.getFounderId().equals(loginUser.getId())){


                if (warningDetailsVo.getStatus().equals("已说明") || warningDetailsVo.getStatus().equals("已通过") ){
                    warningDetailsVo.setCheckInstructions("1");
                }else {

                    warningDetailsVo.setCheckInstructions("0");
                }

            }else{
                warningDetailsVo.setCheckInstructions("1");
            }
        }
     return list;

    }
}

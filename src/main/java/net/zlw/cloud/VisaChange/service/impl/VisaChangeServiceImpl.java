package net.zlw.cloud.VisaChange.service.impl;


import net.zlw.cloud.VisaChange.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.PageVo;

import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    private VisaChangeMapper visaChangeMapper;

    @Autowired
    private AuditInfoDao auditInfoDao;

    @Autowired
    private MemberManageDao memberManageDao;

    @Autowired
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private BaseProjectService baseProjectService;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public List<VisaChangeListVo> findAllVisa(PageVo pageVo) {
        List<VisaChangeListVo> list = visaChangeMapper.findAllVisa(pageVo);
        ArrayList<VisaChangeVo> visaChangeVos = new ArrayList<>();
        for (VisaChangeListVo visaChangeListVo : list) {

        }
        return list;
    }

    @Override
    public void addVisa(VisaChangeVo visaChangeVo, String id) {
        VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
        visaApplyChangeInformationUp.setId(UUID.randomUUID().toString().replace("-",""));
        visaApplyChangeInformationUp.setCreateTime(sim.format(new Date()));
        visaApplyChangeInformationUp.setFouderId(id);
        visaApplyChangeInformationUp.setState("0");
        visaApplyChangeInformationUp.setBaseProjectId(visaChangeVo.getBaseId());
        visaApplyChangeInformationUp.setUpAndDownMark("0");
        visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationUp);

        VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
        visaApplyChangeInformationDown.setId(UUID.randomUUID().toString().replace("-",""));
        visaApplyChangeInformationDown.setCreateTime(sim.format(new Date()));
        visaApplyChangeInformationDown.setFouderId(id);
        visaApplyChangeInformationDown.setState("0");
        visaApplyChangeInformationDown.setBaseProjectId(visaChangeVo.getBaseId());
        visaApplyChangeInformationDown.setUpAndDownMark("1");
        visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationDown);

        VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
        visaChangeUp.setId(UUID.randomUUID().toString().replace("-",""));
        visaChangeUp.setCreateTime(sim.format(new Date()));
        visaChangeUp.setCreatorId(id);
        visaChangeUp.setState("0");
        visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
        visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
        visaChangeUp.setUpAndDownMark("0");
        if (visaChangeVo.getAuditNumber()!=null && !visaChangeVo.getAuditNumber().equals("")){
            visaChangeUp.setChangeNum(1);
            if (visaChangeUp.getAmountVisaChange() ==null){
                visaChangeUp.setAmountVisaChange(new BigDecimal(0));
            }
            visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());
        }
        visaChangeMapper.insertSelective(visaChangeUp);

        VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
        visaChangeDown.setId(UUID.randomUUID().toString().replace("-",""));
        visaChangeDown.setCreateTime(sim.format(new Date()));
        visaChangeDown.setCreatorId(id);
        visaChangeDown.setState("0");
        visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
        visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
        visaChangeDown.setUpAndDownMark("1");
        if (visaChangeVo.getAuditNumber()!=null && !visaChangeVo.getAuditNumber().equals("")){
            visaChangeDown.setChangeNum(1);
            if (visaChangeDown.getAmountVisaChange() ==null){
                visaChangeDown.setAmountVisaChange(new BigDecimal(0));
            }
            visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
        }
        visaChangeMapper.insertSelective(visaChangeDown);

        if (visaChangeVo.getAuditNumber()!=null && !visaChangeVo.getAuditNumber().equals("")){
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            if (visaChangeDown.getId()!=null && ! visaChangeDown.equals("")){
                auditInfo.setBaseProjectId(visaChangeDown.getId());
            }else if(visaChangeUp.getId()!=null && ! visaChangeUp.getId().equals("")){
                auditInfo.setBaseProjectId(visaChangeUp.getId());
            }
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setAuditorId(visaChangeVo.getAuditId());
            auditInfo.setFounderId(id);
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(sim.format(new Date()));
            auditInfoDao.insertSelective(auditInfo);
        }



    }



    @Override
    public VisaChangeVo findVisaById(String baseId, String visaNum, String id) {
        VisaChangeVo visaChangeVo = new VisaChangeVo();
        //普通查询
        if (visaNum.equals("0")){
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseId);
            c.andEqualTo("state","0");
            List<VisaApplyChangeInformation> visaApplyChangeInformations = visaApplyChangeInformationMapper.selectByExample(example);
            for (VisaApplyChangeInformation visaApplyChangeInformation : visaApplyChangeInformations) {
                //上家
                if (visaApplyChangeInformation.getUpAndDownMark().equals("0")){
                    visaChangeVo.setVisaApplyChangeInformationUp(visaApplyChangeInformation);
                }else if(visaApplyChangeInformation.getUpAndDownMark().equals("1")){
                    visaChangeVo.setVisaApplyChangeInformationDown(visaApplyChangeInformation);
                }
            }
            Example example1 = new Example(VisaChange.class);
            Example.Criteria c2 = example1.createCriteria();
            c2.andEqualTo("baseProjectId",baseId);
            c2.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example1);
            for (VisaChange visaChange : visaChanges) {
                if (visaChange.getUpAndDownMark().equals("0")){
                    visaChangeVo.setVisaChangeUp(visaChange);
                }else if(visaChange.getUpAndDownMark().equals("1")){
                    visaChangeVo.setVisaChangeDown(visaChange);
                }
            }
            //进行中查询
        }else if (visaNum.equals("1")){
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",baseId);
            c.andEqualTo("state","0");
            List<VisaApplyChangeInformation> visaApplyChangeInformations = visaApplyChangeInformationMapper.selectByExample(example);
            for (VisaApplyChangeInformation visaApplyChangeInformation : visaApplyChangeInformations) {
                //上家
                if (visaApplyChangeInformation.getUpAndDownMark().equals("0")){
                    visaChangeVo.setVisaApplyChangeInformationUp(visaApplyChangeInformation);
                }else if(visaApplyChangeInformation.getUpAndDownMark().equals("1")){
                    visaChangeVo.setVisaApplyChangeInformationDown(visaApplyChangeInformation);
                }
            }
        }
        return visaChangeVo;
    }
    @Override
    public void updateVisa(VisaChangeVo visaChangeVo, String id) {
        if (visaChangeVo.getVisaNum().equals("1")){
            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);
            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);
            Example example = new Example(VisaChange.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",visaChangeVo.getBaseId());
            c.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
            int upNum = 0;
            int downNum = 0;
            BigDecimal bigDecimal = new BigDecimal(0);
            BigDecimal bigDecimal1 = new BigDecimal(0);
            for (VisaChange visaChange : visaChanges) {
                visaChange.setState("2");
                visaChangeMapper.updateByPrimaryKeySelective(visaChange);
            }

            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            visaChangeUp.setId(UUID.randomUUID().toString().replace("-",""));
            visaChangeUp.setCreateTime(sim.format(new Date()));
            visaChangeUp.setCreatorId(id);
            visaChangeUp.setState("0");
            visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
            visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
            visaChangeUp.setUpAndDownMark("0");
            if (visaChangeVo.getAuditNumber()!=null && !visaChangeVo.getAuditNumber().equals("")){
//                visaChangeUp.setChangeNum(vi);
                if (visaChangeUp.getAmountVisaChange() ==null){
                    visaChangeUp.setAmountVisaChange(new BigDecimal(0));
                }
                visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());
            }
            visaChangeMapper.insertSelective(visaChangeUp);

            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            visaChangeDown.setId(UUID.randomUUID().toString().replace("-",""));
            visaChangeDown.setCreateTime(sim.format(new Date()));
            visaChangeDown.setCreatorId(id);
            visaChangeDown.setState("0");
            visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
            visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
            visaChangeDown.setUpAndDownMark("1");
            visaChangeMapper.insertSelective(visaChangeDown);

            if (visaChangeVo.getAuditNumber()!=null && !visaChangeVo.getAuditNumber().equals("")){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                if (visaChangeDown.getId()!=null && ! visaChangeDown.equals("")){
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                }else if(visaChangeUp.getId()!=null && ! visaChangeUp.getId().equals("")){
                    auditInfo.setBaseProjectId(visaChangeUp.getId());
                }
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("0");
                auditInfo.setAuditorId(visaChangeVo.getAuditId());
                auditInfo.setFounderId(id);
                auditInfo.setStatus("0");
                auditInfo.setCreateTime(sim.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }


        }else{
            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);
            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);
            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);
            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);
            if (visaChangeVo.getAuditNumber().equals("0")){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
                if (visaChangeDown.getId()!=null && ! visaChangeDown.equals("")){
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                }else if(visaChangeUp.getId()!=null && ! visaChangeUp.getId().equals("")){
                    auditInfo.setBaseProjectId(visaChangeUp.getId());
                }
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("0");
                auditInfo.setAuditorId(visaChangeVo.getAuditId());
                auditInfo.setFounderId(id);
                auditInfo.setStatus("0");
                auditInfo.setCreateTime(sim.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }
        }
    }
}

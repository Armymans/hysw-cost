package net.zlw.cloud.followAuditing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.mapper.TrackApplicationInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TrackApplicationInfoServiceImpl implements TrackApplicationInfoService {
    @Resource
    private TrackApplicationInfoDao trackApplicationInfoDao;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private TrackMonthlyDao trackMonthlyDao;
    @Resource
    private AuditInfoDao auditInfoDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private MemberManageDao memberManageDao;


    @Override
    public PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo) {
        // 设置分页助手
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);

        PageInfo<ReturnTrackVo> pageInfo = new PageInfo<>(returnTrackVos);

        return pageInfo;

    }

    @Override
    public void deleteById(String id) {
        TrackAuditInfo trackAuditInfo = new TrackAuditInfo();
        trackAuditInfo.setId(id);
        trackAuditInfo.setStatus("1");
        trackAuditInfoDao.updateByPrimaryKeySelective(trackAuditInfo);

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit",id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);
        trackApplicationInfo.setState("1");
        trackApplicationInfoDao.updateByPrimaryKeySelective(trackApplicationInfo);

        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId",id);
        TrackMonthly trackMonthly = trackMonthlyDao.selectOneByExample(example1);
        trackMonthly.setStatus("1");
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        if (split != null){
            for (String s : split) {
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",s);
                //跟踪审计审核
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (!auditInfo.getAuditResult().equals("1")){
                    auditInfo.setAuditResult(batchReviewVo.getAuditResult());
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    auditInfo.setAuditTime(sim.format(new Date()));
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                }
                Example example1 = new Example(TrackMonthly.class);
                example1.createCriteria().andEqualTo("trackId",s);
                List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
                if (trackMonthlies!=null){
                    for (TrackMonthly trackMonthly : trackMonthlies) {
                        Example example2 = new Example(AuditInfo.class);
                        example2.createCriteria().andEqualTo("baseProjectId",trackMonthly.getId());
                        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example2);
                        if (!auditInfo1.getAuditResult().equals("1")){
                            auditInfo1.setAuditResult(batchReviewVo.getAuditResult());
                            auditInfo1.setAuditOpinion(batchReviewVo.getAuditOpinion());
                            auditInfo1.setAuditTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            auditInfoDao.updateByPrimaryKeySelective(auditInfo1);
                        }
                    }
                }

            }
        }
    }

    @Override
    public void addTrack(TrackVo trackVo) {
        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("projectNum",trackVo.getBaseProject().getProjectNum());
        BaseProject baseProject = baseProjectDao.selectOneByExample(example);
        baseProject.setProjectFlow(baseProject.getProjectFlow()+",3");
        //0保存1提交
        if (trackVo.getStatus().equals("0")){
            baseProject.setTrackStatus("3");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            trackVo.getAuditInfo().setBaseProjectId(baseProject.getId());
            trackVo.getAuditInfo().setId(UUID.randomUUID().toString().replace("-",""));
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());
        }else if(trackVo.getStatus().equals("1")){
            baseProject.setTrackStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            //存入跟踪审计
            trackVo.getAuditInfo().setBaseProjectId(baseProject.getId());
            trackVo.getAuditInfo().setId(UUID.randomUUID().toString().replace("-",""));
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());
            //存入审核表
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-",""));
            auditInfo.setBaseProjectId(trackVo.getAuditInfo().getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            Example example1 = new Example(MemberManage.class);
            example1.createCriteria().andEqualTo("member_role_id","3");
            MemberManage memberManage = memberManageDao.selectOneByExample(example1);
            auditInfo.setAuditorId(memberManage.getId());
            auditInfoDao.insertSelective(auditInfo);
        }


        trackVo.getTrackApplicationInfo().setId(UUID.randomUUID().toString().replace("-",""));
        trackVo.getTrackApplicationInfo().setTrackAudit(trackVo.getAuditInfo().getId());
        trackApplicationInfoDao.insertSelective(trackVo.getTrackApplicationInfo());

        List<TrackMonthly> monthlyList = trackVo.getMonthlyList();
        for (TrackMonthly trackMonthly : monthlyList) {
            if (trackVo.getStatus().equals("0")){
                trackMonthly.setId(UUID.randomUUID().toString().replace("-",""));
                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
                trackMonthlyDao.insert(trackMonthly);
            }else if(trackVo.getStatus().equals("1")){
                trackMonthly.setId(UUID.randomUUID().toString().replace("-",""));
                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
                trackMonthlyDao.insert(trackMonthly);

                //存入审核表
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("","-"));
                auditInfo.setBaseProjectId(trackMonthly.getId());
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("0");
                Example example1 = new Example(MemberManage.class);
                example1.createCriteria().andEqualTo("member_role_id","3");
                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                auditInfo.setAuditorId(memberManage.getId());
                auditInfoDao.insertSelective(auditInfo);
            }

        }


    }

    @Override
    public TrackVo selectTrackById(String id) {
        TrackVo trackVo = new TrackVo();
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit",id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);

        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId",id);
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        trackVo.setBaseProject(baseProject);
        trackVo.setAuditInfo(trackAuditInfo);
        trackVo.setTrackApplicationInfo(trackApplicationInfo);
        trackVo.setMonthlyList(trackMonthlies);
        return trackVo;
    }
    // TODO 回显页面，月报
    public List<TrackMonthly> findAllByTrackId(String id){
        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId",id);
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        return trackMonthlies;
    }

    @Override
    public void updateMonthly(TrackMonthly trackMonthly) {
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void updateTrack(TrackVo trackVo) {

        if (trackVo.getStatus().equals("0")){
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());
        }else if(trackVo.getStatus().equals("1")){
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());
            AuditInfo auditInfo2 = new AuditInfo();
            auditInfo2.setId(UUID.randomUUID().toString().replace("-",""));
            auditInfo2.setBaseProjectId(trackVo.getAuditInfo().getId());
            auditInfo2.setAuditResult("0");
            auditInfo2.setAuditType("0");
            Example example2 = new Example(MemberManage.class);
            example2.createCriteria().andEqualTo("member_role_id","3");
            MemberManage memberManage1 = memberManageDao.selectOneByExample(example2);
            auditInfo2.setAuditorId(memberManage1.getId());
            auditInfo2.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            auditInfoDao.insertSelective(auditInfo2);


            Example example = new Example(AuditInfo.class);
            Example.Criteria c = example.createCriteria();
            for (TrackMonthly trackMonthly : trackVo.getMonthlyList()) {
                c.andEqualTo("baseProjectId",trackMonthly.getId());
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo==null){
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-",""));
                    auditInfo1.setBaseProjectId(trackMonthly.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("0");
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("member_role_id","3");
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    auditInfo1.setAuditorId(memberManage.getId());
                    auditInfo1.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                }

            }
        }

        trackApplicationInfoDao.updateByPrimaryKeySelective(trackVo.getTrackApplicationInfo());
    }
}

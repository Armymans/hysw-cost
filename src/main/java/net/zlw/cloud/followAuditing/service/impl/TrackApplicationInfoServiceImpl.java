package net.zlw.cloud.followAuditing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.followAuditing.mapper.TrackApplicationInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.followAuditing.model.vo.AuditInfoVo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import net.zlw.cloud.followAuditing.model.vo.TrackVo;
import net.zlw.cloud.followAuditing.service.TrackApplicationInfoService;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Resource
    private BudgetingDao budgetingDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;


    @Override
    public PageInfo<ReturnTrackVo> selectTrackList(PageVo pageVo) {
        // 设置分页助手
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        ArrayList<ReturnTrackVo> returnTrackVos1 = new ArrayList<>();
        PageInfo<ReturnTrackVo> pageInfo = new PageInfo<>();

        if ("1".equals(pageVo.getTrackStatus())|| "4".equals(pageVo.getTrackStatus())) {
            List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList(pageVo);
            for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                //当前处理人
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId",returnTrackVo.getId())
                                        .andEqualTo("auditResult","0");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo != null){
                    if (auditInfo.getAuditorId() != null){
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if (memberManage != null){
                            returnTrackVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                }


                if (!returnTrackVos1.contains(returnTrackVo)) {
                    returnTrackVos1.add(returnTrackVo);
                }
            }
            pageInfo = new PageInfo<>(returnTrackVos1);

        } else {

            List<ReturnTrackVo> returnTrackVos = trackAuditInfoDao.selectTrackList1(pageVo);
            for (ReturnTrackVo returnTrackVo : returnTrackVos) {
                if (!returnTrackVos1.contains(returnTrackVo)) {
                    returnTrackVos1.add(returnTrackVo);
                }
            }
            pageInfo = new PageInfo<>(returnTrackVos1);

        }

        return pageInfo;
    }

    // 删除审计月报
    public void deleteByIdTrackMonthly(String id){
        TrackMonthly trackMonthly = trackMonthlyDao.selectByPrimaryKey(id);
        trackMonthly.setStatus("1");
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void deleteById(String id) {
        TrackAuditInfo trackAuditInfo = new TrackAuditInfo();
        trackAuditInfo.setId(id);
        trackAuditInfo.setStatus("1");
        trackAuditInfoDao.updateByPrimaryKeySelective(trackAuditInfo);

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit", id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);
        trackApplicationInfo.setState("1");
        trackApplicationInfoDao.updateByPrimaryKeySelective(trackApplicationInfo);

        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId", id);
        TrackMonthly trackMonthly = trackMonthlyDao.selectOneByExample(example1);
        if (trackMonthly != null) {
            trackMonthly.setStatus("1");
            trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
        }
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo) {
        String[] split = batchReviewVo.getBatchAll().split(",");
        if (split != null) {
            for (String s : split) {
                Example example = new Example(AuditInfo.class);
                example.createCriteria().andEqualTo("baseProjectId", s);
                //跟踪审计审核
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

                TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(s);
                String baseProjectId = trackAuditInfo.getBaseProjectId();
//                Budgeting budgeting = budgetingDao.selectByPrimaryKey(s);
//                String baseProjectId = budgeting.getBaseProjectId();
                System.err.println(baseProjectId);

                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
                if (!auditInfo.getAuditResult().equals("1")) {
                    auditInfo.setAuditResult(batchReviewVo.getAuditResult());
                    auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    auditInfo.setAuditTime(sim.format(new Date()));
                    auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                    if (batchReviewVo.getAuditResult().equals("1")) {
                        baseProject.setTrackStatus("3");
                    } else if (batchReviewVo.getAuditResult().equals("2")) {
                        baseProject.setTrackStatus("4");
                    }
                }
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
                Example example1 = new Example(TrackMonthly.class);
                example1.createCriteria().andEqualTo("trackId", s);
                List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
                if (trackMonthlies != null) {
                    for (TrackMonthly trackMonthly : trackMonthlies) {
                        Example example2 = new Example(AuditInfo.class);
                        example2.createCriteria().andEqualTo("baseProjectId", trackMonthly.getId());
//                        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example2);

                        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
                        for (AuditInfo info : auditInfos) {
                            if (!info.getAuditResult().equals("1")) {
                                info.setAuditResult(batchReviewVo.getAuditResult());
                                info.setAuditOpinion(batchReviewVo.getAuditOpinion());
                                info.setAuditTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                auditInfoDao.updateByPrimaryKeySelective(info);
                            }
                        }


                    }
                }

            }
        }
    }

    @Override
    public void addTrack(TrackVo trackVo, UserInfo userInfo, String baseId) {
        Example example = new Example(BaseProject.class);
        example.createCriteria().andEqualTo("id", baseId);
        BaseProject baseProject = baseProjectDao.selectOneByExample(example);
        baseProject.setProjectFlow(baseProject.getProjectFlow() + ",3");
        //0保存1提交
        if (trackVo.getStatus().equals("0")) {
            // 设置未提交
            baseProject.setTrackStatus("2");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            trackVo.getAuditInfo().setBaseProjectId(baseProject.getId());
            trackVo.getAuditInfo().setId(UUID.randomUUID().toString().replace("-", ""));
            if(userInfo != null){
                trackVo.getAuditInfo().setFounderId(userInfo.getId());
            }
            trackVo.getAuditInfo().setFounderId("user312");
            trackVo.getAuditInfo().setStatus("0");
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());

            // 上传文件，新建-申请信息，列表文件
            List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(trackVo.getKey(), trackVo.getType());

            for (FileInfo fileInfo : byFreignAndType) {
                fileInfo.setPlatCode(trackVo.getAuditInfo().getId());

                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
        } else if (trackVo.getStatus().equals("1")) {
            baseProject.setTrackStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
            //存入跟踪审计
            trackVo.getAuditInfo().setBaseProjectId(baseProject.getId());
            trackVo.getAuditInfo().setId(UUID.randomUUID().toString().replace("-", ""));
            trackVo.getAuditInfo().setFounderId(userInfo.getId());
            trackVo.getAuditInfo().setStatus("0");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            trackVo.getAuditInfo().setCreateTime(simpleDateFormat.format(new Date()));
            trackAuditInfoDao.insertSelective(trackVo.getAuditInfo());
            //存入审核表
            AuditInfo auditInfo = new AuditInfo();
            auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            auditInfo.setBaseProjectId(trackVo.getAuditInfo().getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setStatus("0");
            auditInfo.setCreateTime(simpleDateFormat.format(new Date()));
            Example example1 = new Example(MemberManage.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("depId", "2");
            criteria.andEqualTo("depAdmin", "1");
            MemberManage memberManage = memberManageDao.selectOneByExample(example1);

//            Exadep_adminmple example3 = new Example(MemberManage.class);
//            example3.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
//            MemberManage memberManage = memberManageDao.selectOneByExample(example3);

            auditInfo.setAuditorId(memberManage.getId());
            auditInfoDao.insertSelective(auditInfo);

            // 上传文件，新建-申请信息，列表文件
            List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(trackVo.getKey(), trackVo.getType());

            for (FileInfo fileInfo : byFreignAndType) {
                fileInfo.setPlatCode(trackVo.getAuditInfo().getId());

                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
        }


        trackVo.getTrackApplicationInfo().setId(UUID.randomUUID().toString().replace("-", ""));
        trackVo.getTrackApplicationInfo().setTrackAudit(trackVo.getAuditInfo().getId());
        trackApplicationInfoDao.insertSelective(trackVo.getTrackApplicationInfo());



        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();

        if(userInfo != null){
            criteria.andEqualTo("trackId", userInfo.getId());
        }
        // todo 待修改
        criteria.andEqualTo("trackId", "user312");
        criteria.andEqualTo("status", "0");

        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);

        for (TrackMonthly trackMonthly : trackMonthlies) {
            trackMonthly.setTrackId(trackVo.getAuditInfo().getId());

            trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
        }

//        List<TrackMonthly> monthlyList = trackVo.getMonthlyList();
//        for (TrackMonthly trackMonthly : monthlyList) {
//            if (trackVo.getStatus().equals("0")) {
//                trackMonthly.setId(UUID.randomUUID().toString().replace("-", ""));
//                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
//                trackMonthlyDao.insert(trackMonthly);
//            } else if (trackVo.getStatus().equals("1")) {
//                trackMonthly.setId(UUID.randomUUID().toString().replace("-", ""));
//                trackMonthly.setTrackId(trackVo.getAuditInfo().getId());
//                trackMonthlyDao.insert(trackMonthly);
//
//                //存入审核表
//                AuditInfo auditInfo = new AuditInfo();
//                auditInfo.setId(UUID.randomUUID().toString().replace("", "-"));
//                auditInfo.setBaseProjectId(trackMonthly.getId());
//                auditInfo.setAuditResult("0");
//                auditInfo.setAuditType("0");
//                Example example1 = new Example(MemberManage.class);
//                example1.createCriteria().andEqualTo("member_role_id", "3");
//                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                auditInfo.setAuditorId(memberManage.getId());
//                auditInfoDao.insertSelective(auditInfo);
//            }
//        }


    }

    @Override
    public TrackVo selectTrackById(String id) {
        TrackVo trackVo = new TrackVo();
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(id);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());

        Example example = new Example(TrackApplicationInfo.class);
        example.createCriteria().andEqualTo("trackAudit", id);
        TrackApplicationInfo trackApplicationInfo = trackApplicationInfoDao.selectOneByExample(example);

        Example example1 = new Example(TrackMonthly.class);
        example1.createCriteria().andEqualTo("trackId", id);
        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);


        trackVo.setBaseProject(baseProject);
        trackVo.setAuditInfo(trackAuditInfo);
        trackVo.setTrackApplicationInfo(trackApplicationInfo);
        trackVo.setMonthlyList(trackMonthlies);
        return trackVo;
    }

    // TODO 回显页面，月报
    public List<TrackMonthly> findAllByTrackId(String id) {
        Example example1 = new Example(TrackMonthly.class);
        Example.Criteria criteria = example1.createCriteria();

        criteria.andEqualTo("trackId", id);
        criteria.andEqualTo("status", "0");

        List<TrackMonthly> trackMonthlies = trackMonthlyDao.selectByExample(example1);
        return trackMonthlies;
    }

    // todo 查看页面，审核信息
    public List<AuditInfoVo> findAllAuditInfosByTrackId(String id) {
//        Example example2 = new Example(AuditInfo.class);
//        example2.createCriteria().andEqualTo("baseProjectId",id);
//        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);

        List<AuditInfoVo> allAuditInfosByTrackId = trackAuditInfoDao.findAllAuditInfosByTrackId(id);

        for (AuditInfoVo auditInfoVo : allAuditInfosByTrackId) {
            auditInfoVo.setAuditWord("第一次月报");
        }
        return allAuditInfosByTrackId;
    }

    public void addTrackMonthly(TrackMonthly trackMonthly) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        trackMonthly.setId(UUID.randomUUID().toString().replace("-",""));
        trackMonthly.setCreateTime(simpleDateFormat.format(new Date()));
        trackMonthly.setTrackId(trackMonthly.getTrackId());
        trackMonthly.setStatus("0");
        trackMonthlyDao.insertSelective(trackMonthly);
    }

    @Override
    public void updateMonthly(TrackMonthly trackMonthly) {
        trackMonthlyDao.updateByPrimaryKeySelective(trackMonthly);
    }

    @Override
    public void updateTrack(TrackVo trackVo) {

        if (trackVo.getStatus().equals("0")) {
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());
        } else if (trackVo.getStatus().equals("1")) {
            trackAuditInfoDao.updateByPrimaryKeySelective(trackVo.getAuditInfo());

            String baseProjectId = trackVo.getAuditInfo().getBaseProjectId();
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
            baseProject.setTrackStatus("1");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);

            AuditInfo auditInfo2 = new AuditInfo();
            auditInfo2.setId(UUID.randomUUID().toString().replace("-", ""));
            auditInfo2.setBaseProjectId(trackVo.getAuditInfo().getId());
            auditInfo2.setAuditResult("0");
            auditInfo2.setAuditType("0");
            Example example2 = new Example(MemberManage.class);
            Example.Criteria criteria = example2.createCriteria();
            criteria.andEqualTo("depId", "2");
            criteria.andEqualTo("depAdmin", "1");
            MemberManage memberManage1 = memberManageDao.selectOneByExample(example2);
            auditInfo2.setAuditorId(memberManage1.getId());
            auditInfo2.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            auditInfoDao.insertSelective(auditInfo2);



            Example example = new Example(AuditInfo.class);
            Example.Criteria c = example.createCriteria();
            for (TrackMonthly trackMonthly : trackVo.getMonthlyList()) {
                c.andEqualTo("baseProjectId", trackMonthly.getId());
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo == null) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(trackMonthly.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("0");
                    Example example1 = new Example(MemberManage.class);
                    example1.createCriteria().andEqualTo("member_role_id", "3");
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

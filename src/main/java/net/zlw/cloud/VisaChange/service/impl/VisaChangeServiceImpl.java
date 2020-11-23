package net.zlw.cloud.VisaChange.service.impl;


import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaChange.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
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
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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


    @Resource
    private VisaChangeMapper visaChangeMapper;

    @Resource
    private AuditInfoDao auditInfoDao;

    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;

    @Resource
    private BaseProjectDao baseProjectDao;

    @Resource
    private BaseProjectService baseProjectService;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private RemindSetMapper remindSetMapper;

    @Resource
    private MessageService messageService;

    @Resource
    private MkyUserMapper mkyUserMapper;

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public List<VisaChangeListVo> findAllVisa(PageVo pageVo) {
        List<VisaChangeListVo> list = visaChangeMapper.findAllVisa(pageVo);
        ArrayList<VisaChangeListVo> visaChangeReturnVos = new ArrayList<>();

        ArrayList<VisaChangeListVo> upList = new ArrayList<>();
        ArrayList<VisaChangeListVo> downList = new ArrayList<>();

        for (VisaChangeListVo visaChangeListVo : list) {
            if (visaChangeListVo.getUpAndDownMark().equals("0")) {
                upList.add(visaChangeListVo);
            }
            if (visaChangeListVo.getUpAndDownMark().equals("1")) {
                downList.add(visaChangeListVo);
            }
        }
        if (downList != null && downList.size() != 0) {
            for (VisaChangeListVo visaChangeListVo : downList) {
                VisaChange visaChange = visaChangeMapper.selectByPrimaryKey(visaChangeListVo.getId());
                visaChangeListVo.setContractAmountXia(new BigDecimal(visaChange.getContractAmount()));
                visaChangeListVo.setProportionContractXia(visaChange.getProportionContract());
                visaChangeListVo.setCurrentXia(visaChange.getAmountVisaChange() + "");
                visaChangeListVo.setCreateTime(visaChange.getCompletionTime());
                visaChangeListVo.setCompileTime(visaChange.getCompileTime());
                visaChangeListVo.setFounderId(visaChange.getCreatorId());
                visaChangeReturnVos.add(visaChangeListVo);
            }
        }
        if (upList != null && upList.size() != 0) {
            for (VisaChangeListVo visaChangeListVo : upList) {
                VisaChange visaChange = visaChangeMapper.selectByPrimaryKey(visaChangeListVo.getId());
                if (visaChangeReturnVos != null && visaChangeReturnVos.size() != 0) {
                    for (VisaChangeListVo visaChangeReturnVo : visaChangeReturnVos) {
                        if (visaChangeReturnVo.getBaseProjectId().equals(visaChangeListVo.getBaseProjectId())) {
                            visaChangeReturnVo.setContractAmountShang(new BigDecimal(visaChange.getContractAmount()));
                            visaChangeReturnVo.setProportionContractShang(visaChange.getProportionContract());
                            visaChangeReturnVo.setCurrentShang(visaChange.getAmountVisaChange() + "");
                            visaChangeReturnVo.setCreateTime(visaChange.getCompletionTime());
                            visaChangeReturnVo.setCompileTime(visaChange.getCompileTime());
                            visaChangeReturnVo.setFounderId(visaChange.getCreatorId());
                            break;
                        }
                    }
                }
                visaChangeListVo.setContractAmountShang(new BigDecimal(visaChange.getContractAmount()));
                visaChangeListVo.setProportionContractShang(visaChange.getProportionContract());
                visaChangeListVo.setCurrentShang(visaChange.getAmountVisaChange() + "");
                visaChangeListVo.setCreateTime(visaChange.getCompletionTime());
                visaChangeListVo.setCompileTime(visaChange.getCompileTime());
                visaChangeListVo.setFounderId(visaChange.getCreatorId());
//                    visaChangeReturnVos.add(visaChangeListVo);
            }
        }


        for (VisaChangeListVo visaChangeReturnVo : visaChangeReturnVos) {
            for (VisaChangeListVo visaChangeListVo : list) {
                Example example = new Example(VisaChange.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId", visaChangeListVo.getBaseProjectId());
                c.andEqualTo("state", "0");
                List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
                VisaChange visaChange = null;
                if (visaChanges.size() == 1) {
                    visaChange = visaChanges.get(0);
                } else if (visaChanges.size() == 2) {
                    for (VisaChange change : visaChanges) {
                        if (change.getUpAndDownMark().equals("1")) {
                            visaChange = change;
                        }
                    }
                }
                if (visaChange != null) {
                    Example example1 = new Example(AuditInfo.class);
                    Example.Criteria c2 = example1.createCriteria();
                    c2.andEqualTo("baseProjectId", visaChange.getId());
                    c2.andEqualTo("status", "0");
                    c2.andEqualTo("auditType", "4");
                    c2.andEqualTo("auditResult", "1");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);


                    if (auditInfo == null && !visaChangeReturnVo.getStatus().equals("进行中")) {
                        visaChangeReturnVo.setShowUpdate("2");
                    } else if (visaChangeReturnVo.getStatus().equals("进行中")) {
                        visaChangeReturnVo.setShowUpdate("3");
                    } else if (auditInfo == null && visaChangeReturnVo.getStatus().equals("处理中")) {
                        visaChangeReturnVo.setShowUpdate("1");
                    }
                }
            }
        }


        return visaChangeReturnVos;
    }

    @Override
    public void addVisa(VisaChangeVo visaChangeVo, UserInfo loginUser) {
        String id = loginUser.getId();
        String username = loginUser.getUsername();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
        VisaApplyChangeInformation visaApplyChangeInformationUp = null;
        VisaApplyChangeInformation visaApplyChangeInformationDown = null;
        VisaChange visaChangeUp = null;
        VisaChange visaChangeDown = null;
        //1A 包含所有可能性
        if (baseProject.getAB().equals("1")) {
            //如果不存在申请人字段则判断上家申请不存在

            if (visaChangeVo.getVisaApplyChangeInformationUp().getApplicantName() != null && !visaChangeVo.getVisaApplyChangeInformationUp().getApplicantName().equals("")) {
                visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
                visaApplyChangeInformationUp.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationUp.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationUp.setFouderId(id);
                visaApplyChangeInformationUp.setState("0");
                visaApplyChangeInformationUp.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationUp.setUpAndDownMark("0");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationUp);
            }

            //如果不存在送审金额字段则判断下家申请不存在
            if (visaChangeVo.getVisaApplyChangeInformationDown().getSubmitMoney() != null && !visaChangeVo.getVisaApplyChangeInformationDown().getSubmitMoney().equals("")) {
                visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
                visaApplyChangeInformationDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationDown.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationDown.setFouderId(id);
                visaApplyChangeInformationDown.setState("0");
                visaApplyChangeInformationDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationDown.setUpAndDownMark("1");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationDown);

            }

            if (visaChangeVo.getVisaChangeUp().getAmountVisaChange() != null && !visaChangeVo.getVisaChangeUp().getAmountVisaChange().equals("")) {
                visaChangeUp = visaChangeVo.getVisaChangeUp();
                visaChangeUp.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeUp.setCreateTime(sim.format(new Date()));
                visaChangeUp.setCreatorId(id);
                visaChangeUp.setState("0");
                visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
//                visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
                visaChangeUp.setUpAndDownMark("0");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeUp.setChangeNum(1);
                    if (visaChangeUp.getAmountVisaChange() == null) {
                        visaChangeUp.setAmountVisaChange(new BigDecimal(0));
                    }
                    visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());
                }
                visaChangeMapper.insertSelective(visaChangeUp);
            }

            if (visaChangeVo.getVisaChangeDown().getAmountVisaChange() != null && !visaChangeVo.getVisaChangeDown().getAmountVisaChange().equals("")) {
                visaChangeDown = visaChangeVo.getVisaChangeDown();
                visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeDown.setCreateTime(sim.format(new Date()));
                visaChangeDown.setCreatorId(id);
                visaChangeDown.setState("0");
                visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
//                visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
                visaChangeDown.setUpAndDownMark("1");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeDown.setChangeNum(1);
                    if (visaChangeDown.getAmountVisaChange() == null) {
                        visaChangeDown.setAmountVisaChange(new BigDecimal(0));
                    }
                    visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
                }
                visaChangeMapper.insertSelective(visaChangeDown);
            }
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");


            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                baseProject1.setVisaStatus("1");
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
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
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);

            //2B只允许存在下家
        } else if (baseProject.getAB().equals("2")) {
            //如果不存在送审金额字段则判断下家申请不存在
            if (visaChangeVo.getVisaApplyChangeInformationDown().getSubmitMoney() != null && !visaChangeVo.getVisaApplyChangeInformationDown().getSubmitMoney().equals("")) {
                visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
                visaApplyChangeInformationDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationDown.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationDown.setFouderId(id);
                visaApplyChangeInformationDown.setState("0");
                visaApplyChangeInformationDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationDown.setUpAndDownMark("1");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationDown);
            }

            if (visaChangeVo.getVisaChangeDown().getAmountVisaChange() != null && !visaChangeVo.getVisaChangeDown().getAmountVisaChange().equals("")) {
                visaChangeDown = visaChangeVo.getVisaChangeDown();
                visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeDown.setCreateTime(sim.format(new Date()));
                visaChangeDown.setCreatorId(id);
                visaChangeDown.setState("0");
                visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
                visaChangeDown.setUpAndDownMark("1");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeDown.setChangeNum(1);
                    if (visaChangeDown.getAmountVisaChange() == null) {
                        visaChangeDown.setAmountVisaChange(new BigDecimal(0));
                    }
                    visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
                }
                visaChangeMapper.insertSelective(visaChangeDown);
            }

            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                baseProject1.setVisaStatus("1");
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
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
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //消息通知
            MessageVo messageVo = new MessageVo();
            String projectName = baseProject.getProjectName();

            MemberManage memberManage = memberManageDao.selectByPrimaryKey(visaChangeVo.getAuditId());
            //审核人名字
            String name = memberManage.getMemberName();
            messageVo.setId("A12");
            messageVo.setUserId(visaChangeVo.getAuditId());
            messageVo.setTitle("您有一个签证变更项目待审核！");
            messageVo.setDetails(name + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批！");
        }

    }


    @Override
    public VisaChangeVo findVisaById(String baseId, String visaNum, UserInfo loginUser) {
        VisaChangeVo visaChangeVo = new VisaChangeVo();
        //普通查询
        if (visaNum.equals("0")) {
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", baseId);
            c.andEqualTo("state", "0");
            List<VisaApplyChangeInformation> visaApplyChangeInformations = visaApplyChangeInformationMapper.selectByExample(example);
            for (VisaApplyChangeInformation visaApplyChangeInformation : visaApplyChangeInformations) {
                //上家
                if (visaApplyChangeInformation.getUpAndDownMark().equals("0")) {
                    visaChangeVo.setVisaApplyChangeInformationUp(visaApplyChangeInformation);
                } else if (visaApplyChangeInformation.getUpAndDownMark().equals("1")) {
                    visaChangeVo.setVisaApplyChangeInformationDown(visaApplyChangeInformation);
                }
            }
            Example example1 = new Example(VisaChange.class);
            Example.Criteria c2 = example1.createCriteria();
            c2.andEqualTo("baseProjectId", baseId);
            c2.andEqualTo("state", "0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example1);
            for (VisaChange visaChange : visaChanges) {
                if (visaChange.getUpAndDownMark().equals("0")) {
                    visaChangeVo.setVisaChangeUp(visaChange);
                } else if (visaChange.getUpAndDownMark().equals("1")) {
                    visaChangeVo.setVisaChangeDown(visaChange);
                }
            }
            //进行中查询
        } else if (visaNum.equals("1")) {
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", baseId);
            c.andEqualTo("state", "0");
            List<VisaApplyChangeInformation> visaApplyChangeInformations = visaApplyChangeInformationMapper.selectByExample(example);
            for (VisaApplyChangeInformation visaApplyChangeInformation : visaApplyChangeInformations) {
                //上家
                if (visaApplyChangeInformation.getUpAndDownMark().equals("0")) {
                    visaChangeVo.setVisaApplyChangeInformationUp(visaApplyChangeInformation);
                } else if (visaApplyChangeInformation.getUpAndDownMark().equals("1")) {
                    visaChangeVo.setVisaApplyChangeInformationDown(visaApplyChangeInformation);
                }
            }
            visaChangeVo.setVisaChangeUp(new VisaChange());
            visaChangeVo.setVisaChangeDown(new VisaChange());
        }
        return visaChangeVo;
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo, UserInfo loginUser) {
        String id = loginUser.getId();
        String username = loginUser.getUsername();
        String[] split = batchReviewVo.getBatchAll().split(",");
        for (String s : split) {
            Example example = new Example(VisaChange.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", s);
            c.andEqualTo("state", "0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
            VisaChange visaChange = null;
            if (visaChanges.size() == 1) {
                visaChange = visaChanges.get(0);
            } else if (visaChanges.size() == 2) {
                for (VisaChange change : visaChanges) {
                    if (change.getUpAndDownMark().equals("1")) {
                        visaChange = change;
                    }
                }
            } else {
                throw new RuntimeException("为null或者大于2");

            }


            Example example1 = new Example(AuditInfo.class);
            Example.Criteria c2 = example1.createCriteria();
            c2.andEqualTo("baseProjectId", visaChange.getId());
            c2.andEqualTo("auditResult", "0");
            c2.andEqualTo("status", "0");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
            if (batchReviewVo.getAuditResult().equals("1")) {
                auditInfo.setAuditResult("1");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                if (auditInfo.getAuditType().equals("0")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
                    Example example2 = new Example(MkyUser.class);
                    Example.Criteria c3 = example2.createCriteria();
                    c3.andEqualTo("roleId", "role7615");
                    c3.andEqualTo("delFlag", "0");
                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
                    auditInfo1.setAuditorId(mkyUser.getId());
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("1")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("4");
                    Example example2 = new Example(MkyUser.class);
                    Example.Criteria c3 = example2.createCriteria();
                    c3.andEqualTo("roleId", "role7614");
                    c3.andEqualTo("delFlag", "0");
                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
                    auditInfo1.setAuditorId(mkyUser.getId());
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("4")) {
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                    baseProject.setVisaStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                } else if (auditInfo.getAuditType().equals("2")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("3");
                    Example example2 = new Example(MkyUser.class);
                    Example.Criteria c3 = example2.createCriteria();
                    c3.andEqualTo("roleId", "role7615");
                    c3.andEqualTo("delFlag", "0");
                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
                    auditInfo1.setAuditorId(mkyUser.getId());
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("3")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("5");
                    Example example2 = new Example(MkyUser.class);
                    Example.Criteria c3 = example2.createCriteria();
                    c3.andEqualTo("roleId", "role7614");
                    c3.andEqualTo("delFlag", "0");
                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
                    auditInfo1.setAuditorId(mkyUser.getId());
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("5")) {
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                    baseProject.setVisaStatus("5");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                }
                //审核通过发送消息
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                //项目名称
                String projectName = baseProjectDao.selectByPrimaryKey(auditInfo.getBaseProjectId()).getProjectName();
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A14");
                messageVo.setUserId(auditInfo.getAuditorId());
                messageVo.setTitle("您有一个签证变更项目已通过！");
                messageVo.setDetails(memberManage.getMemberName() + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批！");
                messageService.sendOrClose(messageVo);
            } else if (batchReviewVo.getAuditResult().equals("2")) {
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                auditInfo.setAuditTime(sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                //项目名称
                String projectName = baseProjectDao.selectByPrimaryKey(auditInfo.getBaseProjectId()).getProjectName();
                //成员名称
                String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
                //如果不通过发送消息
                MessageVo messageVo1 = new MessageVo();
                messageVo1.setId("A14");
                messageVo1.setUserId(auditInfo.getAuditorId());
                messageVo1.setTitle("您有一个签证变更项目未通过！");
                messageVo1.setDetails(username + "您好！【" + name + "】已将【" + projectName + "】的签证/变更项目未通过，请查看详情！");
                //调用消息Service
                messageService.sendOrClose(messageVo1);
            }
        }
    }

    @Override
    public List<VisaChangeStatisticVo> findAllchangeStatistics(String baseId) {
        Example example = new Example(VisaChange.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId", baseId);
        c.andEqualTo("state", "2");
        //所有集合
        List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
        Example example1 = new Example(VisaChange.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId", baseId);
        c2.andEqualTo("state", "0");
        List<VisaChange> visaChanges1 = visaChangeMapper.selectByExample(example1);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if (!baseProject.getVisaStatus().equals("2")) {
            for (VisaChange visaChange : visaChanges1) {
                visaChanges.add(visaChange);
            }
        }
        //返回出去的集合
        ArrayList<VisaChangeStatisticVo> visaChangeStatisticVos = new ArrayList<>();
        //上家集合
        ArrayList<VisaChange> upVisachanges = new ArrayList<>();
        //下家集合
        ArrayList<VisaChange> downVisachanges = new ArrayList<>();
        for (VisaChange visaChange : visaChanges) {
            if (visaChange.getUpAndDownMark().equals("0")) {
                upVisachanges.add(visaChange);
            } else if (visaChange.getUpAndDownMark().equals("1")) {
                downVisachanges.add(visaChange);
            }
        }
        for (VisaChange downVisachange : downVisachanges) {
            VisaChangeStatisticVo visaChangeStatisticVo = new VisaChangeStatisticVo();
            visaChangeStatisticVo.setBaseId(downVisachange.getBaseProjectId());
            visaChangeStatisticVo.setChangeNum(downVisachange.getChangeNum());
            visaChangeStatisticVo.setVisaChangeDownAmount(downVisachange.getAmountVisaChange());
            visaChangeStatisticVo.setVisaChangeDownProportionContract(downVisachange.getProportionContract());
            MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(downVisachange.getCreatorId());
            visaChangeStatisticVo.setCreatorName(mkyUser.getUserName());
            visaChangeStatisticVo.setCompletionTime(downVisachange.getCompletionTime());
            visaChangeStatisticVo.setCompileTime(downVisachange.getCompileTime());
            visaChangeStatisticVos.add(visaChangeStatisticVo);
        }
        //给for循环命名 跳过命名循环
        outer:
        for (VisaChange upVisachange : upVisachanges) {
            for (VisaChangeStatisticVo visaChangeStatisticVo : visaChangeStatisticVos) {
                if (upVisachange.getChangeNum() == visaChangeStatisticVo.getChangeNum()) {
                    visaChangeStatisticVo.setVisaChangeUpAmount(upVisachange.getAmountVisaChange());
                    visaChangeStatisticVo.setVisaChangeUpProportionContract(upVisachange.getProportionContract());
                    visaChangeStatisticVo.setCompletionTime(upVisachange.getCompletionTime());
                    visaChangeStatisticVo.setCompileTime(upVisachange.getCompileTime());
                    continue outer;
                }

            }
            VisaChangeStatisticVo visaChangeStatisticVo1 = new VisaChangeStatisticVo();
            visaChangeStatisticVo1.setVisaChangeUpAmount(upVisachange.getAmountVisaChange());
            visaChangeStatisticVo1.setVisaChangeUpProportionContract(upVisachange.getProportionContract());
            visaChangeStatisticVo1.setCompletionTime(upVisachange.getCompletionTime());
            visaChangeStatisticVo1.setCompileTime(upVisachange.getCompileTime());
            visaChangeStatisticVos.add(visaChangeStatisticVo1);
        }


        return visaChangeStatisticVos;
    }

    @Override
    public String showHiddenCard(String id, String baseId) {
        Example example = new Example(VisaChange.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", "0");
        c.andEqualTo("baseProjectId", baseId);
        List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
        VisaChange visaChange = null;
        if (visaChanges != null && visaChanges.size() == 2) {
            for (VisaChange change : visaChanges) {
                if (change.getUpAndDownMark().equals("1")) {
                    visaChange = change;
                }
            }
        } else if (visaChanges != null && visaChanges.size() == 1) {
            visaChange = visaChanges.get(0);
        }

        Example example1 = new Example(AuditInfo.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId", visaChange.getId());
        c2.andEqualTo("status", "0");
        c2.andEqualTo("auditResult", "0");
        c2.andEqualTo("auditorId", id);
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
        String returnShow = "0";
        if (auditInfo != null) {
            if (auditInfo.getAuditType().equals("0") || auditInfo.getAuditType().equals("1") || auditInfo.getAuditType().equals("4")) {
                //第一次审则显示第一张卡片
                returnShow = "1";
                //第二次审则显示第二张卡片
            } else if (auditInfo.getAuditType().equals("2") || auditInfo.getAuditType().equals("3") || auditInfo.getAuditType().equals("5")) {
                returnShow = "2";
            }
        }


        return returnShow;
    }

    @Override
    public void deleteVisa(String baseId) {
        Example example = new Example(VisaApplyChangeInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId", baseId);
        List<VisaApplyChangeInformation> visaApplyChangeInformations = visaApplyChangeInformationMapper.selectByExample(example);
        for (VisaApplyChangeInformation visaApplyChangeInformation : visaApplyChangeInformations) {
            visaApplyChangeInformation.setState("1");
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformation);
        }
        Example example1 = new Example(VisaChange.class);
        Example.Criteria c2 = example1.createCriteria();
        c2.andEqualTo("baseProjectId", baseId);
        List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example1);
        for (VisaChange visaChange : visaChanges) {
            visaChange.setState("1");
            visaChangeMapper.updateByPrimaryKey(visaChange);
        }
    }

    @Override
    public void updateVisa(VisaChangeVo visaChangeVo, UserInfo loginUser) {
        String id = loginUser.getId();
        //进行中编辑
        if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("1")) {
            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c.andEqualTo("state", "0");
            c.andEqualTo("upAndDownMark", "0");
            VisaApplyChangeInformation visaApplyChangeInformation = visaApplyChangeInformationMapper.selectOneByExample(example);
            visaApplyChangeInformationUp.setId(visaApplyChangeInformation.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);

            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            Example example1 = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c1.andEqualTo("state", "0");
            c1.andEqualTo("upAndDownMark", "1");
            VisaApplyChangeInformation visaApplyChangeInformation1 = visaApplyChangeInformationMapper.selectOneByExample(example1);
            visaApplyChangeInformationDown.setId(visaApplyChangeInformation1.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);

            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example3);
            int upNum = 0;
            int downNum = 0;
            BigDecimal bigDecimal = new BigDecimal(0);
            BigDecimal bigDecimal1 = new BigDecimal(0);
            for (VisaChange visaChange : visaChanges) {
                if (visaChange.getUpAndDownMark().equals("0")) {
                    upNum = visaChange.getChangeNum();
                    bigDecimal = visaChange.getCumulativeChangeAmount();
                } else if (visaChange.getUpAndDownMark().equals("1")) {
                    downNum = visaChange.getChangeNum();
                    bigDecimal1 = visaChange.getCumulativeChangeAmount();
                }
                visaChange.setState("2");
                visaChangeMapper.updateByPrimaryKeySelective(visaChange);
            }

            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            visaChangeUp.setId(UUID.randomUUID().toString().replace("-", ""));
            visaChangeUp.setCreateTime(sim.format(new Date()));
            visaChangeUp.setCreatorId(id);
            visaChangeUp.setState("0");
            visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
            visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
            visaChangeUp.setUpAndDownMark("0");
            visaChangeUp.setChangeNum(upNum + 1);
            if (visaChangeUp.getAmountVisaChange() == null) {
                visaChangeUp.setAmountVisaChange(new BigDecimal(0));
            }
            visaChangeUp.setCumulativeChangeAmount(bigDecimal.add(visaChangeUp.getAmountVisaChange()));
//                visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());

            visaChangeMapper.insertSelective(visaChangeUp);

            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
            visaChangeDown.setCreateTime(sim.format(new Date()));
            visaChangeDown.setCreatorId(id);
            visaChangeDown.setState("0");
            visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
            visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
            visaChangeDown.setUpAndDownMark("1");
            visaChangeDown.setChangeNum(downNum + 1);
            if (visaChangeDown.getAmountVisaChange() == null) {
                visaChangeDown.setAmountVisaChange(new BigDecimal(0));
            }
            visaChangeDown.setCumulativeChangeAmount(bigDecimal1.add(visaChangeDown.getAmountVisaChange()));
//                visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
            visaChangeMapper.insertSelective(visaChangeDown);
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                baseProject1.setVisaStatus("1");
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                baseProject.setVisaStatus("1");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
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
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //待确认编辑
        } else if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("2")) {
            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c.andEqualTo("state", "0");
            c.andEqualTo("upAndDownMark", "0");
            VisaApplyChangeInformation visaApplyChangeInformation = visaApplyChangeInformationMapper.selectOneByExample(example);
            visaApplyChangeInformationUp.setId(visaApplyChangeInformation.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);

            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            Example example1 = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c1.andEqualTo("state", "0");
            c1.andEqualTo("upAndDownMark", "1");
            VisaApplyChangeInformation visaApplyChangeInformation1 = visaApplyChangeInformationMapper.selectOneByExample(example1);
            visaApplyChangeInformationDown.setId(visaApplyChangeInformation1.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);


            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            Example example2 = new Example(VisaChange.class);
            Example.Criteria c2 = example2.createCriteria();
            c2.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c2.andEqualTo("state", "0");
            c2.andEqualTo("upAndDownMark", "0");
            VisaChange visaChange = visaChangeMapper.selectOneByExample(example2);
            visaChangeUp.setId(visaChange.getId());
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);


            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            c3.andEqualTo("upAndDownMark", "1");
            VisaChange visaChange2 = visaChangeMapper.selectOneByExample(example3);
            visaChangeDown.setId(visaChange2.getId());
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && visaChangeVo.getAuditNumber().equals("0")) {
                baseProject1.setVisaStatus("1");
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                baseProject.setVisaStatus("1");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
                    auditInfo.setBaseProjectId(visaChangeUp.getId());
                }
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("2");
                auditInfo.setAuditorId(visaChangeVo.getAuditId());
                auditInfo.setFounderId(id);
                auditInfo.setStatus("0");
                auditInfo.setCreateTime(sim.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //普通编辑
        } else if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("3")) {
            System.err.println(visaChangeVo);
            System.err.println(visaChangeVo);
            System.err.println(visaChangeVo);
            System.err.println(visaChangeVo);
            System.err.println(visaChangeVo);
            System.err.println(visaChangeVo);


            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            Example example = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c.andEqualTo("state", "0");
            c.andEqualTo("upAndDownMark", "0");
            VisaApplyChangeInformation visaApplyChangeInformation = visaApplyChangeInformationMapper.selectOneByExample(example);
            visaApplyChangeInformationUp.setId(visaApplyChangeInformation.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);

            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            Example example1 = new Example(VisaApplyChangeInformation.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c1.andEqualTo("state", "0");
            c1.andEqualTo("upAndDownMark", "1");
            VisaApplyChangeInformation visaApplyChangeInformation1 = visaApplyChangeInformationMapper.selectOneByExample(example1);
            visaApplyChangeInformationDown.setId(visaApplyChangeInformation1.getId());
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);


            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            Example example2 = new Example(VisaChange.class);
            Example.Criteria c2 = example2.createCriteria();
            c2.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c2.andEqualTo("state", "0");
            c2.andEqualTo("upAndDownMark", "0");
            VisaChange visaChange = visaChangeMapper.selectOneByExample(example2);
            visaChangeUp.setId(visaChange.getId());
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);


            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            c3.andEqualTo("upAndDownMark", "1");
            VisaChange visaChange2 = visaChangeMapper.selectOneByExample(example3);
            visaChangeDown.setId(visaChange2.getId());
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);


            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                baseProject1.setVisaStatus("1");
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                baseProject.setVisaStatus("1");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setId(UUID.randomUUID().toString().replace("-", ""));
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    auditInfo.setBaseProjectId(visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
                    auditInfo.setBaseProjectId(visaChangeUp.getId());
                }
                auditInfo.setAuditResult("0");
                auditInfo.setAuditType("2");
                auditInfo.setAuditorId(visaChangeVo.getAuditId());
                auditInfo.setFounderId(id);
                auditInfo.setStatus("0");
                auditInfo.setCreateTime(sim.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //未通过提交
        } else if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("4")) {
            VisaApplyChangeInformation visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationUp);
            VisaApplyChangeInformation visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
            visaApplyChangeInformationMapper.updateByPrimaryKeySelective(visaApplyChangeInformationDown);
            VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);
            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("1");

            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                Example example = new Example(AuditInfo.class);
                Example.Criteria c = example.createCriteria();
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    c.andEqualTo("baseProjectId", visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
                    c.andEqualTo("baseProjectId", visaChangeUp.getId());
                }
                c.andEqualTo("status", "0");
                c.andEqualTo("auditResult", "2");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                auditInfo.setAuditResult("0");
                auditInfo.setAuditOpinion("");
                auditInfo.setAuditTime("");
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            }

        }

        String username = loginUser.getUsername();
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
        String projectName = baseProject.getProjectName();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(visaChangeVo.getAuditId());
        //审核人名字
        String name = memberManage.getMemberName();
        MessageVo messageVo = new MessageVo();
        messageVo.setId("A13");
        messageVo.setUserId(visaChangeVo.getAuditId());
        messageVo.setTitle("您有一个签证变更项目待审核！");
        messageVo.setDetails(name + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批！");
        //调用消息Service
        messageService.sendOrClose(messageVo);


    }
}

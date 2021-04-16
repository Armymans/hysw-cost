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
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.OutSourceMapper;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.OutSource;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private MemberService memberService;

    @Resource
    private OperationLogDao operationLogDao;

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

    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private OutSourceMapper outSourceMapper;

    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;

    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;

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

    private SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public List<VisaChangeListVo> findAllVisa(PageVo pageVo) {
//        List<VisaChangeListVo> list = visaChangeMapper.findAllVisa(pageVo);

        //待审核
        if (pageVo.getStatus().equals("1")){
            //领导
            if ( pageVo.getUserId().equals(whzjh) || pageVo.getUserId().equals(whzjm) || pageVo.getUserId().equals(wjzjh)){
                List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaCheckLeader(pageVo);

                for (VisaChangeListVo budgetingListVo : list1) {
//                    // 造价单位名称
//                    if (budgetingListVo.getNameOfCostUnit() != null && !"".equals(budgetingListVo.getNameOfCostUnit())){
//                        budgetingListVo.setNameOfCostUnit(budgetingListVo.getNameOfCostUnit());
//                    }else {
//                        budgetingListVo.setNameOfCostUnit("/");
//                    }
//
//                    // 上家合同金额（元）
//                    if (budgetingListVo.getContractAmountShang() != null && !"".equals(budgetingListVo.getContractAmountShang())){
//                        budgetingListVo.setContractAmountShang(budgetingListVo.getContractAmountShang());
//                    }else {
//                        budgetingListVo.setContractAmountShang(null);
//                    }
//                    // 累计上家签证/变更金额（元）
//                    if (budgetingListVo.getAmountVisaChangeAddShang() != null && !"".equals(budgetingListVo.getAmountVisaChangeAddShang())){
//                        budgetingListVo.setAmountVisaChangeAddShang(budgetingListVo.getAmountVisaChangeAddShang());
//                    }else {
//                        budgetingListVo.setAmountVisaChangeAddShang("/");
//                    }
//                    // 占上家合同比例
//                    if (budgetingListVo.getProportionContractShang() != null && !"".equals(budgetingListVo.getProportionContractShang())){
//                        budgetingListVo.setProportionContractShang(budgetingListVo.getProportionContractShang());
//                    }else {
//                        budgetingListVo.setProportionContractShang("/");
//                    }
//                    // 下家合同金额（元）
//                    if (budgetingListVo.getContractAmountXia() != null && !"".equals(budgetingListVo.getContractAmountXia())){
//                        budgetingListVo.setContractAmountXia(budgetingListVo.getContractAmountXia());
//                    }else {
//                        budgetingListVo.setContractAmountXia(null);
//                    }
//                    // 累计下家签证/变更金额（元）
//                    if (budgetingListVo.getAmountVisaChangeAddXia() != null && !"".equals(budgetingListVo.getAmountVisaChangeAddXia())){
//                        budgetingListVo.setAmountVisaChangeAddXia(budgetingListVo.getAmountVisaChangeAddXia());
//                    }else {
//                        budgetingListVo.setAmountVisaChangeAddXia("/");
//                    }
//                    // 占下家合同比例
//                    if (budgetingListVo.getProportionContractXia() != null && !"".equals(budgetingListVo.getProportionContractXia())){
//                        budgetingListVo.setProportionContractXia(budgetingListVo.getProportionContractXia());
//                    }else {
//                        budgetingListVo.setProportionContractXia("/");
//                    }
//                    // 本期上家签证/变更金额（元）
//                    if (budgetingListVo.getCurrentShang() != null && !"".equals(budgetingListVo.getCurrentShang())){
//                        budgetingListVo.setCurrentShang(budgetingListVo.getCurrentShang());
//                    }else {
//                        budgetingListVo.setCurrentShang("/");
//                    }
//                    // 本期下家签证/变更金额（元）
//                    if (budgetingListVo.getCurrentXia() != null && !"".equals(budgetingListVo.getCurrentXia())){
//                        budgetingListVo.setCurrentXia(budgetingListVo.getCurrentXia());
//                    }else {
//                        budgetingListVo.setCurrentXia("/");
//                    }
//                    // 接收时间
//                    if (budgetingListVo.getCreateTime() != null && !"".equals(budgetingListVo.getCreateTime())){
//                        budgetingListVo.setCreateTime(budgetingListVo.getCreateTime());
//                    }else {
//                        budgetingListVo.setCreateTime("/");
//                    }
//                    // 编制时间
//                    if (budgetingListVo.getCompileTime() != null && !"".equals(budgetingListVo.getCompileTime())){
//                        budgetingListVo.setCompileTime(budgetingListVo.getCompileTime());
//                    }else {
//                        budgetingListVo.setCompileTime("/");
//                    }
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if(auditInfo!=null){
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if (memberManage !=null){
                            budgetingListVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                }
                for (VisaChangeListVo visaChangeListVo : list1) {
                    Example example = new Example(AuditInfo.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",visaChangeListVo.getId());
                    c.andEqualTo("auditResult","0");
                    c.andEqualTo("status","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType())){
                        visaChangeListVo.setStatus("签证/变更审核");
                    }else{
                        visaChangeListVo.setStatus("签证/变更确认审核");
                    }
                }
                for (VisaChangeListVo visaChangeListVo : list1) {
                    String proportionContractShang = visaChangeListVo.getProportionContractShang();
                    String proportionContractXia = visaChangeListVo.getProportionContractXia();
                    if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                        BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                    }
                    if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                        BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                    }
                }
                return list1;
                //普通员工
            }else {
                List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaCheckStaff(pageVo);
                for (VisaChangeListVo budgetingListVo : list1) {
//                    // 造价单位名称
//                    if (  budgetingListVo.getNameOfCostUnit() != null && !"".equals(  budgetingListVo.getNameOfCostUnit())){
//                        budgetingListVo.setNameOfCostUnit(  budgetingListVo.getNameOfCostUnit());
//                    }else {
//                        budgetingListVo.setNameOfCostUnit("/");
//                    }
//
//                    // 上家合同金额（元）
//                    if (  budgetingListVo.getContractAmountShang() != null && !"".equals(  budgetingListVo.getContractAmountShang())){
//                        budgetingListVo.setContractAmountShang(  budgetingListVo.getContractAmountShang());
//                    }else {
//                        budgetingListVo.setContractAmountShang(null);
//                    }
//                    // 累计上家签证/变更金额（元）
//                    if (  budgetingListVo.getAmountVisaChangeAddShang() != null && !"".equals(  budgetingListVo.getAmountVisaChangeAddShang())){
//                        budgetingListVo.setAmountVisaChangeAddShang(  budgetingListVo.getAmountVisaChangeAddShang());
//                    }else {
//                        budgetingListVo.setAmountVisaChangeAddShang("/");
//                    }
//                    // 占上家合同比例
//                    if (  budgetingListVo.getProportionContractShang() != null && !"".equals(  budgetingListVo.getProportionContractShang())){
//                        budgetingListVo.setProportionContractShang(  budgetingListVo.getProportionContractShang());
//                    }else {
//                        budgetingListVo.setProportionContractShang("/");
//                    }
//                    // 下家合同金额（元）
//                    if (  budgetingListVo.getContractAmountXia() != null && !"".equals(  budgetingListVo.getContractAmountXia())){
//                        budgetingListVo.setContractAmountXia(  budgetingListVo.getContractAmountXia());
//                    }else {
//                        budgetingListVo.setContractAmountXia(null);
//                    }
//                    // 累计下家签证/变更金额（元）
//                    if (  budgetingListVo.getAmountVisaChangeAddXia() != null && !"".equals(  budgetingListVo.getAmountVisaChangeAddXia())){
//                        budgetingListVo.setAmountVisaChangeAddXia(  budgetingListVo.getAmountVisaChangeAddXia());
//                    }else {
//                        budgetingListVo.setAmountVisaChangeAddXia("/");
//                    }
//                    // 占下家合同比例
//                    if (  budgetingListVo.getProportionContractXia() != null && !"".equals(  budgetingListVo.getProportionContractXia())){
//                        budgetingListVo.setProportionContractXia(  budgetingListVo.getProportionContractXia());
//                    }else {
//                        budgetingListVo.setProportionContractXia("/");
//                    }
//                    // 本期上家签证/变更金额（元）
//                    if (  budgetingListVo.getCurrentShang() != null && !"".equals(  budgetingListVo.getCurrentShang())){
//                        budgetingListVo.setCurrentShang(  budgetingListVo.getCurrentShang());
//                    }else {
//                        budgetingListVo.setCurrentShang("/");
//                    }
//                    // 本期下家签证/变更金额（元）
//                    if (  budgetingListVo.getCurrentXia() != null && !"".equals(  budgetingListVo.getCurrentXia())){
//                        budgetingListVo.setCurrentXia(  budgetingListVo.getCurrentXia());
//                    }else {
//                        budgetingListVo.setCurrentXia("/");
//                    }
//                    // 接收时间
//                    if (  budgetingListVo.getCreateTime() != null && !"".equals(  budgetingListVo.getCreateTime())){
//                        budgetingListVo.setCreateTime(  budgetingListVo.getCreateTime());
//                    }else {
//                        budgetingListVo.setCreateTime("/");
//                    }
//                    // 编制时间
//                    if (  budgetingListVo.getCompileTime() != null && !"".equals(  budgetingListVo.getCompileTime())){
//                        budgetingListVo.setCompileTime(  budgetingListVo.getCompileTime());
//                    }else {
//                        budgetingListVo.setCompileTime("/");
//                    }
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria().andEqualTo("baseProjectId",budgetingListVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo!=null){
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if (memberManage !=null){
                            budgetingListVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }

                }
                for (VisaChangeListVo visaChangeListVo : list1) {
                    Example example = new Example(AuditInfo.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",visaChangeListVo.getId());
                    c.andEqualTo("auditResult","0");
                    c.andEqualTo("status","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType())){
                        visaChangeListVo.setStatus("签证/变更审核");
                    }else{
                        visaChangeListVo.setStatus("签证/变更确认审核");
                    }
                }

                for (VisaChangeListVo visaChangeListVo : list1) {
                    String proportionContractShang = visaChangeListVo.getProportionContractShang();
                    String proportionContractXia = visaChangeListVo.getProportionContractXia();
                    if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                        BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                    }
                    if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                        BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                    }
                }
                return list1;
            }
        }
        //处理中
        if (pageVo.getStatus().equals("2")){
            if ( pageVo.getUserId().equals(whzjh) || pageVo.getUserId().equals(whzjm) || pageVo.getUserId().equals(wjzjh)) {
                List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaProcessing2(pageVo);
//            for (VisaChangeListVo thisList : list1) {
//                // 造价单位名称
//                if (  thisList.getNameOfCostUnit() != null && !"".equals(  thisList.getNameOfCostUnit())){
//                    thisList.setNameOfCostUnit(  thisList.getNameOfCostUnit());
//                }else {
//                    thisList.setNameOfCostUnit("/");
//                }
//
//                // 上家合同金额（元）
//                if (  thisList.getContractAmountShang() != null && !"".equals(  thisList.getContractAmountShang())){
//                    thisList.setContractAmountShang(  thisList.getContractAmountShang());
//                }else {
//                    thisList.setContractAmountShang(null);
//                }
//                // 累计上家签证/变更金额（元）
//                if (  thisList.getAmountVisaChangeAddShang() != null && !"".equals(  thisList.getAmountVisaChangeAddShang())){
//                    thisList.setAmountVisaChangeAddShang(  thisList.getAmountVisaChangeAddShang());
//                }else {
//                    thisList.setAmountVisaChangeAddShang("/");
//                }
//                // 占上家合同比例
//                if (  thisList.getProportionContractShang() != null && !"".equals(  thisList.getProportionContractShang())){
//                    thisList.setProportionContractShang(  thisList.getProportionContractShang());
//                }else {
//                    thisList.setProportionContractShang("/");
//                }
//                // 下家合同金额（元）
//                if (  thisList.getContractAmountXia() != null && !"".equals(  thisList.getContractAmountXia())){
//                    thisList.setContractAmountXia(  thisList.getContractAmountXia());
//                }else {
//                    thisList.setContractAmountXia(null);
//                }
//                // 累计下家签证/变更金额（元）
//                if (  thisList.getAmountVisaChangeAddXia() != null && !"".equals(  thisList.getAmountVisaChangeAddXia())){
//                    thisList.setAmountVisaChangeAddXia(  thisList.getAmountVisaChangeAddXia());
//                }else {
//                    thisList.setAmountVisaChangeAddXia("/");
//                }
//                // 占下家合同比例
//                if (  thisList.getProportionContractXia() != null && !"".equals(  thisList.getProportionContractXia())){
//                    thisList.setProportionContractXia(  thisList.getProportionContractXia());
//                }else {
//                    thisList.setProportionContractXia("/");
//                }
//                // 本期上家签证/变更金额（元）
//                if (  thisList.getCurrentShang() != null && !"".equals(  thisList.getCurrentShang())){
//                    thisList.setCurrentShang(  thisList.getCurrentShang());
//                }else {
//                    thisList.setCurrentShang("/");
//                }
//                // 本期下家签证/变更金额（元）
//                if (  thisList.getCurrentXia() != null && !"".equals(  thisList.getCurrentXia())){
//                    thisList.setCurrentXia(  thisList.getCurrentXia());
//                }else {
//                    thisList.setCurrentXia("/");
//                }
//                // 接收时间
//                if (  thisList.getCreateTime() != null && !"".equals(  thisList.getCreateTime())){
//                    thisList.setCreateTime(  thisList.getCreateTime());
//                }else {
//                    thisList.setCreateTime("/");
//                }
//                // 编制时间
//                if (  thisList.getCompileTime() != null && !"".equals(  thisList.getCompileTime())){
//                    thisList.setCompileTime(  thisList.getCompileTime());
//                }else {
//                    thisList.setCompileTime("/");
//                }
                //           }

                for (VisaChangeListVo visaChangeListVo : list1) {
                    if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang()) && visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())) {
                       // visaChangeListVo.setStatus("-");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeUp().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {
                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }

                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeUp().getNameOfCostUnit());

                    } else if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang())) {
                        visaChangeListVo.setStatus("上家编制中");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeUp().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {
                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }


                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeUp().getNameOfCostUnit());
                        if (visaById.getVisaChangeUp().getOutsourcingAmount() != null && !"".equals(visaById.getVisaChangeUp().getOutsourcingAmount())) {
                            visaChangeListVo.setAmountCost(Integer.parseInt(visaById.getVisaChangeUp().getOutsourcingAmount()));
                        } else {
                            visaChangeListVo.setAmountCost(0);
                        }
                    } else if (visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())) {
                        visaChangeListVo.setStatus("下家编制中");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeDown().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {
                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }

                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeDown().getNameOfCostUnit());

                    }
                }
                for (VisaChangeListVo visaChangeListVo : list1) {
                    String proportionContractShang = visaChangeListVo.getProportionContractShang();
                    String proportionContractXia = visaChangeListVo.getProportionContractXia();
                    if (!"".equals(proportionContractShang) && proportionContractShang != null && !"/".equals(proportionContractShang)) {
                        BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                    }
                    if (!"".equals(proportionContractXia) && proportionContractXia != null && !"/".equals(proportionContractXia)) {
                        BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                    }

                }

                for (VisaChangeListVo visaChangeListVo : list1) {
                    String nameOfCostUnit = visaChangeListVo.getNameOfCostUnit();
                    if (nameOfCostUnit != null && !"".equals(nameOfCostUnit)) {
                        CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfCostUnit);
                        if (costUnitManagement != null) {
                            visaChangeListVo.setNameOfCostUnit(costUnitManagement.getCostUnitName());
                        }
                    }
                }

                ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("1");
                String personnel = thoseResponsible.getPersonnel();
                boolean f = false;
                if (personnel != null) {
                    String[] split = personnel.split(",");
                    for (String s : split) {
                        if (s.equals(pageVo.getUserId()) || pageVo.getUserId().equals(whzjh) || pageVo.getUserId().equals(whzjm) || pageVo.getUserId().equals(wjzjh)) {
                            f = true;
                        }
                    }
                }
                if (f) {
                    for (VisaChangeListVo visaChangeListVo  : list1) {
                        visaChangeListVo.setFshow("1");
                    }
                }
                return list1;
            }else{
                List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaProcessing(pageVo);
                for (VisaChangeListVo visaChangeListVo : list1) {
                    if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang()) && visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())) {
    //                    visaChangeListVo.setStatus("-");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeUp().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {
                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }

                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeUp().getNameOfCostUnit());

                    } else if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang())) {
                        visaChangeListVo.setStatus("上家编制中");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeUp().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {

                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }


                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeUp().getNameOfCostUnit());
                        if (visaById.getVisaChangeUp().getOutsourcingAmount() != null && !"".equals(visaById.getVisaChangeUp().getOutsourcingAmount())) {
                            visaChangeListVo.setAmountCost(Integer.parseInt(visaById.getVisaChangeUp().getOutsourcingAmount()));
                        } else {
                            visaChangeListVo.setAmountCost(0);
                        }
                    } else if (visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())) {
                        visaChangeListVo.setStatus("下家编制中");
                        VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                        String outsourcing = visaById.getVisaChangeDown().getOutsourcing();
                        if (outsourcing != null && !"".equals(outsourcing)) {
                            if (outsourcing.equals("1")) {
                                outsourcing = "是";
                            } else if (outsourcing.equals("2")) {
                                outsourcing = "否";
                            }

                        }
                        visaChangeListVo.setOutsourcing(outsourcing);
                        visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeDown().getNameOfCostUnit());

                    }
                }
                for (VisaChangeListVo visaChangeListVo : list1) {
                    String proportionContractShang = visaChangeListVo.getProportionContractShang();
                    String proportionContractXia = visaChangeListVo.getProportionContractXia();
                    if (!"".equals(proportionContractShang) && proportionContractShang != null && !"/".equals(proportionContractShang)) {
                        BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                    }
                    if (!"".equals(proportionContractXia) && proportionContractXia != null && !"/".equals(proportionContractXia)) {
                        BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                        visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                    }

                }

                for (VisaChangeListVo visaChangeListVo : list1) {
                    String nameOfCostUnit = visaChangeListVo.getNameOfCostUnit();
                    if (nameOfCostUnit != null && !"".equals(nameOfCostUnit)) {
                        CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfCostUnit);
                        if (costUnitManagement != null) {
                            visaChangeListVo.setNameOfCostUnit(costUnitManagement.getCostUnitName());
                        }
                    }
                }
                return list1;
            }
        }
        //未通过
        if (pageVo.getStatus().equals("3")){
            List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaUnsanctioned(pageVo);
            for (VisaChangeListVo thisList : list1) {

                String baseProjectId = thisList.getBaseProjectId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
                if ("7".equals(baseProject.getVisaStatus())){
                    thisList.setUnShow("1");
                }

//                    // 造价单位名称
//                    if (  thisList.getNameOfCostUnit() != null && !"".equals(  thisList.getNameOfCostUnit())){
//                        thisList.setNameOfCostUnit(  thisList.getNameOfCostUnit());
//                    }else {
//                        thisList.setNameOfCostUnit("/");
//                    }
//                    // 上家合同金额（元）
//                    if (  thisList.getContractAmountShang() != null && !"".equals(  thisList.getContractAmountShang())){
//                        thisList.setContractAmountShang(  thisList.getContractAmountShang());
//                    }else {
//                        thisList.setContractAmountShang(null);
//                    }
//                    // 累计上家签证/变更金额（元）
//                    if (  thisList.getAmountVisaChangeAddShang() != null && !"".equals(  thisList.getAmountVisaChangeAddShang())){
//                        thisList.setAmountVisaChangeAddShang(  thisList.getAmountVisaChangeAddShang());
//                    }else {
//                        thisList.setAmountVisaChangeAddShang("/");
//                    }
//                    // 占上家合同比例
//                    if (  thisList.getProportionContractShang() != null && !"".equals(  thisList.getProportionContractShang())){
//                        thisList.setProportionContractShang(  thisList.getProportionContractShang());
//                    }else {
//                        thisList.setProportionContractShang("/");
//                    }
//                    // 下家合同金额（元）
//                    if (  thisList.getContractAmountXia() != null && !"".equals(  thisList.getContractAmountXia())){
//                        thisList.setContractAmountXia(  thisList.getContractAmountXia());
//                    }else {
//                        thisList.setContractAmountXia(null);
//                    }
//                    // 累计下家签证/变更金额（元）
//                    if (  thisList.getAmountVisaChangeAddXia() != null && !"".equals(  thisList.getAmountVisaChangeAddXia())){
//                        thisList.setAmountVisaChangeAddXia(  thisList.getAmountVisaChangeAddXia());
//                    }else {
//                        thisList.setAmountVisaChangeAddXia("/");
//                    }
//                    // 占下家合同比例
//                    if (  thisList.getProportionContractXia() != null && !"".equals(  thisList.getProportionContractXia())){
//                        thisList.setProportionContractXia(  thisList.getProportionContractXia());
//                    }else {
//                        thisList.setProportionContractXia("/");
//                    }
//                    // 本期上家签证/变更金额（元）
//                    if (  thisList.getCurrentShang() != null && !"".equals(  thisList.getCurrentShang())){
//                        thisList.setCurrentShang(  thisList.getCurrentShang());
//                    }else {
//                        thisList.setCurrentShang("/");
//                    }
//                    // 本期下家签证/变更金额（元）
//                    if (  thisList.getCurrentXia() != null && !"".equals(  thisList.getCurrentXia())){
//                        thisList.setCurrentXia(  thisList.getCurrentXia());
//                    }else {
//                        thisList.setCurrentXia("/");
//                    }
//                    // 接收时间
//                    if (  thisList.getCreateTime() != null && !"".equals(  thisList.getCreateTime())){
//                        thisList.setCreateTime(  thisList.getCreateTime());
//                    }else {
//                        thisList.setCreateTime("/");
//                    }
//                    // 编制时间
//                    if (  thisList.getCompileTime() != null && !"".equals(  thisList.getCompileTime())){
//                        thisList.setCompileTime(  thisList.getCompileTime());
//                    }else {
//                        thisList.setCompileTime("/");
//                    }
                }
            for (VisaChangeListVo visaChangeListVo : list1) {
                Example example = new Example(AuditInfo.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",visaChangeListVo.getId());
                c.andEqualTo("auditResult","2");
                c.andEqualTo("status","0");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                if (auditInfo!=null){
                    if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType())){
                        visaChangeListVo.setStatus("签证/变更未通过");
                    }else{
                        visaChangeListVo.setStatus("签证/变更确认未通过");
                    }
                }


                if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang()) && visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())){

                    VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                    String outsourcing = visaById.getVisaChangeDown().getOutsourcing();
                    if (outsourcing!=null && !"".equals(outsourcing)){
                        if (outsourcing.equals("1")){
                            outsourcing = "是";
                        }else if(outsourcing.equals("2")){
                            outsourcing = "否";
                        }

                    }
                    visaChangeListVo.setOutsourcing(outsourcing);
                    visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeDown().getNameOfCostUnit());
                   
                }else if(visaChangeListVo.getCurrentShang() != null && ! "".equals(visaChangeListVo.getCurrentShang())){

                    VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                    String outsourcing = visaById.getVisaChangeUp().getOutsourcing();
                    if (outsourcing!=null && !"".equals(outsourcing)){
                        if (outsourcing.equals("1")){
                            outsourcing = "是";
                        }else if(outsourcing.equals("2")){
                            outsourcing = "否";
                        }

                    }
                    visaChangeListVo.setOutsourcing(outsourcing);
                    visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeUp().getNameOfCostUnit());
                    if (visaById.getVisaChangeUp().getOutsourcingAmount()!=null && !"".equals(visaById.getVisaChangeUp().getOutsourcingAmount())){
                        visaChangeListVo.setAmountCost(Integer.parseInt(visaById.getVisaChangeUp().getOutsourcingAmount()));
                    }else{
                        visaChangeListVo.setAmountCost(0);
                    }
                }else if(visaChangeListVo.getCurrentXia() != null && ! "".equals(visaChangeListVo.getCurrentXia())){

                    VisaChangeVo visaById = findVisaById(visaChangeListVo.getBaseProjectId(), "0", new UserInfo(pageVo.getUserId(), null, null, true));
                    String outsourcing = visaById.getVisaChangeDown().getOutsourcing();
                    if (outsourcing!=null && !"".equals(outsourcing)){
                        if (outsourcing.equals("1")){
                            outsourcing = "是";
                        }else if(outsourcing.equals("2")){
                            outsourcing = "否";
                        }

                    }
                    visaChangeListVo.setOutsourcing(outsourcing);
                    visaChangeListVo.setNameOfCostUnit(visaById.getVisaChangeDown().getNameOfCostUnit());
                   
                }

            }

            for (VisaChangeListVo visaChangeListVo : list1) {
                String proportionContractShang = visaChangeListVo.getProportionContractShang();
                String proportionContractXia = visaChangeListVo.getProportionContractXia();
                if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                    BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                }
                if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                    BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                }
            }

            for (VisaChangeListVo visaChangeListVo : list1) {
                String nameOfCostUnit = visaChangeListVo.getNameOfCostUnit();
                if (nameOfCostUnit!=null && !"".equals(nameOfCostUnit)){
                    CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfCostUnit);
                    if (costUnitManagement!=null){
                        visaChangeListVo.setNameOfCostUnit(costUnitManagement.getCostUnitName());
                    }
                }
            }
            return list1;
        }
        //待确认
        if (pageVo.getStatus().equals("4")){
            List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaProcessing1(pageVo);
            for (VisaChangeListVo thisList : list1) {
                Example example1 = new Example(AuditInfo.class);
                Example.Criteria criteria = example1.createCriteria();
                criteria.andEqualTo("baseProjectId",thisList.getId());
                criteria.andEqualTo("status","0");
                criteria.andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
                if (auditInfo!=null){
                    if (pageVo.getUserId().equals(auditInfo.getAuditorId())){
                        thisList.setBackShow("1");
                    }
                }

//                    // 造价单位名称
//                    if (  thisList.getNameOfCostUnit() != null && !"".equals(  thisList.getNameOfCostUnit())){
//                        thisList.setNameOfCostUnit(  thisList.getNameOfCostUnit());
//                    }else {
//                        thisList.setNameOfCostUnit("/");
//                    }
//                    // 上家合同金额（元）
//                    if (  thisList.getContractAmountShang() != null && !"".equals(  thisList.getContractAmountShang())){
//                        thisList.setContractAmountShang(  thisList.getContractAmountShang());
//                    }else {
//                        thisList.setContractAmountShang(null);
//                    }
//                    // 累计上家签证/变更金额（元）
//                    if (  thisList.getAmountVisaChangeAddShang() != null && !"".equals(  thisList.getAmountVisaChangeAddShang())){
//                        thisList.setAmountVisaChangeAddShang(  thisList.getAmountVisaChangeAddShang());
//                    }else {
//                        thisList.setAmountVisaChangeAddShang("/");
//                    }
//                    // 占上家合同比例
//                    if (  thisList.getProportionContractShang() != null && !"".equals(  thisList.getProportionContractShang())){
//                        thisList.setProportionContractShang(  thisList.getProportionContractShang());
//                    }else {
//                        thisList.setProportionContractShang("/");
//                    }
//                    // 下家合同金额（元）
//                    if (  thisList.getContractAmountXia() != null && !"".equals(  thisList.getContractAmountXia())){
//                        thisList.setContractAmountXia(  thisList.getContractAmountXia());
//                    }else {
//                        thisList.setContractAmountXia(null);
//                    }
//                    // 累计下家签证/变更金额（元）
//                    if (  thisList.getAmountVisaChangeAddXia() != null && !"".equals(  thisList.getAmountVisaChangeAddXia())){
//                        thisList.setAmountVisaChangeAddXia(  thisList.getAmountVisaChangeAddXia());
//                    }else {
//                        thisList.setAmountVisaChangeAddXia("/");
//                    }
//                    // 占下家合同比例
//                    if (  thisList.getProportionContractXia() != null && !"".equals(  thisList.getProportionContractXia())){
//                        thisList.setProportionContractXia(  thisList.getProportionContractXia());
//                    }else {
//                        thisList.setProportionContractXia("/");
//                    }
//                    // 本期上家签证/变更金额（元）
//                    if (  thisList.getCurrentShang() != null && !"".equals(  thisList.getCurrentShang())){
//                        thisList.setCurrentShang(  thisList.getCurrentShang());
//                    }else {
//                        thisList.setCurrentShang("/");
//                    }
//                    // 本期下家签证/变更金额（元）
//                    if (  thisList.getCurrentXia() != null && !"".equals(  thisList.getCurrentXia())){
//                        thisList.setCurrentXia(  thisList.getCurrentXia());
//                    }else {
//                        thisList.setCurrentXia("/");
//                    }
//                    // 接收时间
//                    if (  thisList.getCreateTime() != null && !"".equals(  thisList.getCreateTime())){
//                        thisList.setCreateTime(  thisList.getCreateTime());
//                    }else {
//                        thisList.setCreateTime("/");
//                    }
//                    // 编制时间
//                    if (  thisList.getCompileTime() != null && !"".equals(  thisList.getCompileTime())){
//                        thisList.setCompileTime(  thisList.getCompileTime());
//                    }else {
//                        thisList.setCompileTime("/");
//                    }
                }
            for (VisaChangeListVo visaChangeListVo : list1) {
                String proportionContractShang = visaChangeListVo.getProportionContractShang();
                String proportionContractXia = visaChangeListVo.getProportionContractXia();
                if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                    BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                }
                if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                    BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                }
            }
            return list1;
        }
        //进行中
        if (pageVo.getStatus().equals("5")){
            List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaSuccess(pageVo);
            for (VisaChangeListVo visaChangeListVo : list1) {
//                // 造价单位名称
//                if (  visaChangeListVo.getNameOfCostUnit() != null && !"".equals(  visaChangeListVo.getNameOfCostUnit())){
//                    visaChangeListVo.setNameOfCostUnit(  visaChangeListVo.getNameOfCostUnit());
//                }else {
//                    visaChangeListVo.setNameOfCostUnit("/");
//                }
//                // 上家合同金额（元）
//                if (  visaChangeListVo.getContractAmountShang() != null && !"".equals(  visaChangeListVo.getContractAmountShang())){
//                    visaChangeListVo.setContractAmountShang(  visaChangeListVo.getContractAmountShang());
//                }else {
//                    visaChangeListVo.setContractAmountShang(null);
//                }
//                // 累计上家签证/变更金额（元）
//                if (  visaChangeListVo.getAmountVisaChangeAddShang() != null && !"".equals(  visaChangeListVo.getAmountVisaChangeAddShang())){
//                    visaChangeListVo.setAmountVisaChangeAddShang(  visaChangeListVo.getAmountVisaChangeAddShang());
//                }else {
//                    visaChangeListVo.setAmountVisaChangeAddShang("/");
//                }
//                // 占上家合同比例
//                if (  visaChangeListVo.getProportionContractShang() != null && !"".equals(  visaChangeListVo.getProportionContractShang())){
//                    visaChangeListVo.setProportionContractShang(  visaChangeListVo.getProportionContractShang());
//                }else {
//                    visaChangeListVo.setProportionContractShang("/");
//                }
//                // 下家合同金额（元）
//                if (  visaChangeListVo.getContractAmountXia() != null && !"".equals(  visaChangeListVo.getContractAmountXia())){
//                    visaChangeListVo.setContractAmountXia(  visaChangeListVo.getContractAmountXia());
//                }else {
//                    visaChangeListVo.setContractAmountXia(null);
//                }
//                // 累计下家签证/变更金额（元）
//                if (  visaChangeListVo.getAmountVisaChangeAddXia() != null && !"".equals(  visaChangeListVo.getAmountVisaChangeAddXia())){
//                    visaChangeListVo.setAmountVisaChangeAddXia(  visaChangeListVo.getAmountVisaChangeAddXia());
//                }else {
//                    visaChangeListVo.setAmountVisaChangeAddXia("/");
//                }
//                // 占下家合同比例
//                if (  visaChangeListVo.getProportionContractXia() != null && !"".equals(  visaChangeListVo.getProportionContractXia())){
//                    visaChangeListVo.setProportionContractXia(  visaChangeListVo.getProportionContractXia());
//                }else {
//                    visaChangeListVo.setProportionContractXia("/");
//                }
//                // 本期上家签证/变更金额（元）
//                if (  visaChangeListVo.getCurrentShang() != null && !"".equals(  visaChangeListVo.getCurrentShang())){
//                    visaChangeListVo.setCurrentShang(  visaChangeListVo.getCurrentShang());
//                }else {
//                    visaChangeListVo.setCurrentShang("/");
//                }
//                // 本期下家签证/变更金额（元）
//                if (  visaChangeListVo.getCurrentXia() != null && !"".equals(  visaChangeListVo.getCurrentXia())){
//                    visaChangeListVo.setCurrentXia(  visaChangeListVo.getCurrentXia());
//                }else {
//                    visaChangeListVo.setCurrentXia("/");
//                }
//                // 接收时间
//                if (  visaChangeListVo.getCreateTime() != null && !"".equals(  visaChangeListVo.getCreateTime())){
//                    visaChangeListVo.setCreateTime(  visaChangeListVo.getCreateTime());
//                }else {
//                    visaChangeListVo.setCreateTime("/");
//                }
//                // 编制时间
//                if (  visaChangeListVo.getCompileTime() != null && !"".equals(  visaChangeListVo.getCompileTime())){
//                    visaChangeListVo.setCompileTime(  visaChangeListVo.getCompileTime());
//                }else {
//                    visaChangeListVo.setCompileTime("/");
//                }

                if (visaChangeListVo.getFounderId().equals(pageVo.getUserId())){
                    visaChangeListVo.setShowUnderway("1");
                }else{
                    visaChangeListVo.setShowUnderway("2");
                }
            }

            for (VisaChangeListVo visaChangeListVo : list1) {
                String proportionContractShang = visaChangeListVo.getProportionContractShang();
                String proportionContractXia = visaChangeListVo.getProportionContractXia();
                if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                    BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                }
                if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                    BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                }
            }
            return list1;
        }
        //已完成
        if (pageVo.getStatus().equals("6")){
            List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaSuccess(pageVo);
            for (VisaChangeListVo thisList : list1) {
//                // 造价单位名称
//                if (  thisList.getNameOfCostUnit() != null && !"".equals(  thisList.getNameOfCostUnit())){
//                    thisList.setNameOfCostUnit(  thisList.getNameOfCostUnit());
//                }else {
//                    thisList.setNameOfCostUnit("/");
//                }
//                // 上家合同金额（元）
//                if (  thisList.getContractAmountShang() != null && !"".equals(  thisList.getContractAmountShang())){
//                    thisList.setContractAmountShang(  thisList.getContractAmountShang());
//                }else {
//                    thisList.setContractAmountShang(null);
//                }
//                // 累计上家签证/变更金额（元）
//                if (  thisList.getAmountVisaChangeAddShang() != null && !"".equals(  thisList.getAmountVisaChangeAddShang())){
//                    thisList.setAmountVisaChangeAddShang(  thisList.getAmountVisaChangeAddShang());
//                }else {
//                    thisList.setAmountVisaChangeAddShang("/");
//                }
//                // 占上家合同比例
//                if (  thisList.getProportionContractShang() != null && !"".equals(  thisList.getProportionContractShang())){
//                    thisList.setProportionContractShang(  thisList.getProportionContractShang());
//                }else {
//                    thisList.setProportionContractShang("/");
//                }
//                // 下家合同金额（元）
//                if (  thisList.getContractAmountXia() != null && !"".equals(  thisList.getContractAmountXia())){
//                    thisList.setContractAmountXia(  thisList.getContractAmountXia());
//                }else {
//                    thisList.setContractAmountXia(null);
//                }
//                // 累计下家签证/变更金额（元）
//                if (  thisList.getAmountVisaChangeAddXia() != null && !"".equals(  thisList.getAmountVisaChangeAddXia())){
//                    thisList.setAmountVisaChangeAddXia(  thisList.getAmountVisaChangeAddXia());
//                }else {
//                    thisList.setAmountVisaChangeAddXia("/");
//                }
//                // 占下家合同比例
//                if (  thisList.getProportionContractXia() != null && !"".equals(  thisList.getProportionContractXia())){
//                    thisList.setProportionContractXia(  thisList.getProportionContractXia());
//                }else {
//                    thisList.setProportionContractXia("/");
//                }
//                // 本期上家签证/变更金额（元）
//                if (  thisList.getCurrentShang() != null && !"".equals(  thisList.getCurrentShang())){
//                    thisList.setCurrentShang(  thisList.getCurrentShang());
//                }else {
//                    thisList.setCurrentShang("/");
//                }
//                // 本期下家签证/变更金额（元）
//                if (  thisList.getCurrentXia() != null && !"".equals(  thisList.getCurrentXia())){
//                    thisList.setCurrentXia(  thisList.getCurrentXia());
//                }else {
//                    thisList.setCurrentXia("/");
//                }
//                // 接收时间
//                if (  thisList.getCreateTime() != null && !"".equals(  thisList.getCreateTime())){
//                    thisList.setCreateTime(  thisList.getCreateTime());
//                }else {
//                    thisList.setCreateTime("/");
//                }
//                // 编制时间
//                if (  thisList.getCompileTime() != null && !"".equals(  thisList.getCompileTime())){
//                    thisList.setCompileTime(  thisList.getCompileTime());
//                }else {
//                    thisList.setCompileTime("/");
//                }
            }
            for (VisaChangeListVo visaChangeListVo : list1) {
                String proportionContractShang = visaChangeListVo.getProportionContractShang();
                String proportionContractXia = visaChangeListVo.getProportionContractXia();
                if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                    BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                }
                if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                    BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                }
            }
            return list1;
        }
        //全部
        if (pageVo.getStatus().equals("") || pageVo.getStatus() == null || pageVo.getStatus().equals("0")) {
            pageVo.setStatus("");
            String userId = pageVo.getUserId();
            if (whzjh.equals(pageVo.getUserId()) || whzjm.equals(pageVo.getUserId()) || wjzjm.equals(pageVo.getUserId())){
                pageVo.setUserId("");
            }
            List<VisaChangeListVo> list1 = visaChangeMapper.findAllVisaProcessing(pageVo);
            for (VisaChangeListVo visaChangeListVo : list1) {


//                System.err.println(visaChangeListVo);
//                // 造价单位名称
//                if (visaChangeListVo.getNameOfCostUnit() != null && !"".equals(visaChangeListVo.getNameOfCostUnit())){
//                    visaChangeListVo.setNameOfCostUnit(visaChangeListVo.getNameOfCostUnit());
//                }else {
//                    visaChangeListVo.setNameOfCostUnit("/");
//                }
//                // 上家合同金额（元）
//                if (visaChangeListVo.getContractAmountShang() != null && !"".equals(visaChangeListVo.getContractAmountShang())){
//                    visaChangeListVo.setContractAmountShang(visaChangeListVo.getContractAmountShang());
//                }else {
//                    visaChangeListVo.setContractAmountShang(null);
//                }
//                // 累计上家签证/变更金额（元）
//                if (visaChangeListVo.getAmountVisaChangeAddShang() != null && !"".equals(visaChangeListVo.getAmountVisaChangeAddShang())){
//                    visaChangeListVo.setAmountVisaChangeAddShang(visaChangeListVo.getAmountVisaChangeAddShang());
//                }else {
//                    visaChangeListVo.setAmountVisaChangeAddShang("/");
//                }
//                // 占上家合同比例
//                if (visaChangeListVo.getProportionContractShang() != null && !"".equals(visaChangeListVo.getProportionContractShang())){
//                    visaChangeListVo.setProportionContractShang(visaChangeListVo.getProportionContractShang());
//                }else {
//                    visaChangeListVo.setProportionContractShang("/");
//                }
//                // 下家合同金额（元）
//                if (visaChangeListVo.getContractAmountXia() != null && !"".equals(visaChangeListVo.getContractAmountXia())){
//                    visaChangeListVo.setContractAmountXia(visaChangeListVo.getContractAmountXia());
//                }else {
//                    visaChangeListVo.setContractAmountXia(null);
//                }
//                // 累计下家签证/变更金额（元）
//                if (visaChangeListVo.getAmountVisaChangeAddXia() != null && !"".equals(visaChangeListVo.getAmountVisaChangeAddXia())){
//                    visaChangeListVo.setAmountVisaChangeAddXia(visaChangeListVo.getAmountVisaChangeAddXia());
//                }else {
//                    visaChangeListVo.setAmountVisaChangeAddXia("/");
//                }
//                // 占下家合同比例
//                if (visaChangeListVo.getProportionContractXia() != null && !"".equals(visaChangeListVo.getProportionContractXia())){
//                    visaChangeListVo.setProportionContractXia(visaChangeListVo.getProportionContractXia());
//                }else {
//                    visaChangeListVo.setProportionContractXia("/");
//                }
//                // 本期上家签证/变更金额（元）
//                if (visaChangeListVo.getCurrentShang() != null && !"".equals(visaChangeListVo.getCurrentShang())){
//                    visaChangeListVo.setCurrentShang(visaChangeListVo.getCurrentShang());
//                }else {
//                    visaChangeListVo.setCurrentShang("/");
//                }
//                // 本期下家签证/变更金额（元）
//                if (visaChangeListVo.getCurrentXia() != null && !"".equals(visaChangeListVo.getCurrentXia())){
//                    visaChangeListVo.setCurrentXia(visaChangeListVo.getCurrentXia());
//                }else {
//                    visaChangeListVo.setCurrentXia("/");
//                }
//                // 接收时间
//                if (visaChangeListVo.getCreateTime() != null && !"".equals(visaChangeListVo.getCreateTime())){
//                    visaChangeListVo.setCreateTime(visaChangeListVo.getCreateTime());
//                }else {
//                    visaChangeListVo.setCreateTime("/");
//                }
//                // 编制时间
//                if (visaChangeListVo.getCompileTime() != null && !"".equals(visaChangeListVo.getCompileTime())){
//                    visaChangeListVo.setCompileTime(visaChangeListVo.getCompileTime());
//                }else {
//                    visaChangeListVo.setCompileTime("/");
//                }
                    Example example1 = new Example(AuditInfo.class);
                    Example.Criteria c2 = example1.createCriteria();
                    c2.andEqualTo("baseProjectId", visaChangeListVo.getId());
                    c2.andEqualTo("status", "0");
                    c2.andEqualTo("auditType", "4");
                    c2.andEqualTo("auditResult", "1");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);

                    if (auditInfo == null && visaChangeListVo.getStatus().equals("处理中")){
                        visaChangeListVo.setShowUpdate("1");
                    }else if(auditInfo != null && visaChangeListVo.getStatus().equals("待确认")){
                        visaChangeListVo.setShowUpdate("2");
                    }else if(visaChangeListVo.getStatus().equals("进行中")){
                        visaChangeListVo.setShowUpdate("3");
                        if (visaChangeListVo.getFounderId().equals(userId)){
                            visaChangeListVo.setShowUnderway("1");
                        }else{
                            visaChangeListVo.setShowUnderway("2");
                        }
                    }
            }

            for (VisaChangeListVo visaChangeListVo : list1) {

                String baseProjectId = visaChangeListVo.getBaseProjectId();
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseProjectId);
                if ("7".equals(baseProject.getVisaStatus())){
                    visaChangeListVo.setUnShow("1");
                }

                String proportionContractShang = visaChangeListVo.getProportionContractShang();
                String proportionContractXia = visaChangeListVo.getProportionContractXia();
                if (!"".equals(proportionContractShang) && proportionContractShang!=null && !"/".equals(proportionContractShang)){
                    BigDecimal bigDecimal = new BigDecimal(proportionContractShang).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractShang(bigDecimal.toString());
                }
                if (!"".equals(proportionContractXia) && proportionContractXia!=null && !"/".equals(proportionContractXia)){
                    BigDecimal bigDecimal1 = new BigDecimal(proportionContractXia).setScale(2, BigDecimal.ROUND_HALF_UP);
                    visaChangeListVo.setProportionContractXia(bigDecimal1.toString());
                }
            }
            return list1;
        }





////        for (VisaChangeListVo visaChangeListVo : list) {
////            if (visaChangeListVo.getUpAndDownMark().equals("0")) {
////                upList.add(visaChangeListVo);
////            }
////            if (visaChangeListVo.getUpAndDownMark().equals("1")) {
////                downList.add(visaChangeListVo);
////            }
////        }
////        if (downList != null && downList.size() != 0) {
////            for (VisaChangeListVo visaChangeListVo : downList) {
////                VisaChange visaChange = visaChangeMapper.selectByPrimaryKey(visaChangeListVo.getId());
////                visaChangeListVo.setContractAmountXia(new BigDecimal(visaChange.getContractAmount()));
////                visaChangeListVo.setProportionContractXia(visaChange.getProportionContract());
////                visaChangeListVo.setCurrentXia(visaChange.getAmountVisaChange() + "");
////                visaChangeListVo.setCreateTime(visaChange.getCompletionTime());
////                visaChangeListVo.setCompileTime(visaChange.getCompileTime());
////                visaChangeListVo.setFounderId(visaChange.getCreatorId());
////                visaChangeReturnVos.add(visaChangeListVo);
////            }
////        }
////        if (upList != null && upList.size() != 0) {
////            for (VisaChangeListVo visaChangeListVo : upList) {
////                VisaChange visaChange = visaChangeMapper.selectByPrimaryKey(visaChangeListVo.getId());
////                if (visaChangeReturnVos != null && visaChangeReturnVos.size() != 0) {
////                    for (VisaChangeListVo visaChangeReturnVo : visaChangeReturnVos) {
////                        if (visaChangeReturnVo.getBaseProjectId().equals(visaChangeListVo.getBaseProjectId())) {
////                            visaChangeReturnVo.setContractAmountShang(new BigDecimal(visaChange.getContractAmount()));
////                            visaChangeReturnVo.setProportionContractShang(visaChange.getProportionContract());
////                            visaChangeReturnVo.setCurrentShang(visaChange.getAmountVisaChange() + "");
////                            visaChangeReturnVo.setCreateTime(visaChange.getCompletionTime());
////                            visaChangeReturnVo.setCompileTime(visaChange.getCompileTime());
////                            visaChangeReturnVo.setFounderId(visaChange.getCreatorId());
////                            break;
////                        }
////                    }
////                }
////                visaChangeListVo.setContractAmountShang(new BigDecimal(visaChange.getContractAmount()));
////                visaChangeListVo.setProportionContractShang(visaChange.getProportionContract());
////                visaChangeListVo.setCurrentShang(visaChange.getAmountVisaChange() + "");
////                visaChangeListVo.setCreateTime(visaChange.getCompletionTime());
////                visaChangeListVo.setCompileTime(visaChange.getCompileTime());
////                visaChangeListVo.setFounderId(visaChange.getCreatorId());
//////                    visaChangeReturnVos.add(visaChangeListVo);
////            }
////        }
////
////
////        for (VisaChangeListVo visaChangeReturnVo : visaChangeReturnVos) {
////            for (VisaChangeListVo visaChangeListVo : list) {
////                Example example = new Example(VisaChange.class);
////                Example.Criteria c = example.createCriteria();
////                c.andEqualTo("baseProjectId", visaChangeListVo.getBaseProjectId());
////                c.andEqualTo("state", "0");
////                List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
////                VisaChange visaChange = null;
////                if (visaChanges.size() == 1) {
////                    visaChange = visaChanges.get(0);
////                } else if (visaChanges.size() == 2) {
////                    for (VisaChange change : visaChanges) {
////                        if (change.getUpAndDownMark().equals("1")) {
////                            visaChange = change;
////                        }
////                    }
////                }
//                if (visaChange != null) {
//                    Example example1 = new Example(AuditInfo.class);
//                    Example.Criteria c2 = example1.createCriteria();
//                    c2.andEqualTo("baseProjectId", visaChange.getId());
//                    c2.andEqualTo("status", "0");
//                    c2.andEqualTo("auditType", "4");
//                    c2.andEqualTo("auditResult", "1");
//                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
//
//
//
//                    if (auditInfo != null && !visaChangeReturnVo.getStatus().equals("进行中")) {
//                        visaChangeReturnVo.setShowUpdate("2");
//                    } else if (visaChangeReturnVo.getStatus().equals("进行中")) {
//                        visaChangeReturnVo.setShowUpdate("3");
//                    } else if (auditInfo == null && visaChangeReturnVo.getStatus().equals("处理中")) {
//                        visaChangeReturnVo.setShowUpdate("1");
//                    }
//                }
//            }
//        }


        return new ArrayList<VisaChangeListVo>();
    }

    @Override
    public void addVisa(VisaChangeVo visaChangeVo, UserInfo loginUser, HttpServletRequest request) {
        String id = loginUser.getId();
//        String id = "user309";
        String username = loginUser.getUsername();
//        String username = "造价业务员3";


        if (visaChangeVo.getVisaChangeUp().getProportionContract().equals("NaN")){
            visaChangeVo.getVisaChangeUp().setProportionContract("0");
        }
        if (visaChangeVo.getVisaChangeDown().getProportionContract().equals("NaN")){
            visaChangeVo.getVisaChangeDown().setProportionContract("0");
        }

        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
        VisaApplyChangeInformation visaApplyChangeInformationUp = null;
        VisaApplyChangeInformation visaApplyChangeInformationDown = null;
        VisaChange visaChangeUp = null;
        VisaChange visaChangeDown = null;
        //1A 包含所有可能性
        if (baseProject.getAB().equals("1")) {
            //如果不存在申请人字段则判断上家申请不存在


                visaApplyChangeInformationUp = visaChangeVo.getVisaApplyChangeInformationUp();
                visaApplyChangeInformationUp.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationUp.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationUp.setFouderId(id);
                visaApplyChangeInformationUp.setState("0");
                visaApplyChangeInformationUp.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationUp.setUpAndDownMark("0");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationUp);


            //如果不存在送审金额字段则判断下家申请不存在

                visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
                visaApplyChangeInformationDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationDown.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationDown.setFouderId(id);
                visaApplyChangeInformationDown.setState("0");
                visaApplyChangeInformationDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationDown.setUpAndDownMark("1");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationDown);



                visaChangeUp = visaChangeVo.getVisaChangeUp();
            if ("".equals(visaChangeUp.getAmountVisaChange())){
                visaChangeUp.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeUp.getContractAmount()) ){
                visaChangeUp.setContractAmount(null);
            }
            if ("".equals(visaChangeUp.getOutsourcingAmount())){
                visaChangeUp.setOutsourcingAmount(null);
            }
            if (visaChangeUp.getProportionContract().length()>60){
                visaChangeUp.setProportionContract(null);
            }
                visaChangeUp.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeUp.setCreateTime(sim.format(new Date()));
                visaChangeUp.setCreatorId(id);
                visaChangeUp.setState("0");
                visaChangeUp.setChangeNum(1);
                visaChangeUp.setCumulativeChangeAmount(visaChangeVo.getVisaChangeUp().getAmountVisaChange());
                visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
//                visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
                visaChangeUp.setUpAndDownMark("0");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeUp.setChangeNum(1);
                    visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());
                }
                visaChangeMapper.insertSelective(visaChangeUp);

                visaChangeDown = visaChangeVo.getVisaChangeDown();


            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if (visaChangeDown.getProportionContract().length()>60){
                visaChangeDown.setProportionContract(null);
            }
                visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeDown.setCreateTime(sim.format(new Date()));
                visaChangeDown.setCreatorId(id);
                visaChangeDown.setState("0");
                visaChangeDown.setChangeNum(1);
                visaChangeDown.setCumulativeChangeAmount(visaChangeVo.getVisaChangeDown().getAmountVisaChange());
                visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
//                visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
                visaChangeDown.setUpAndDownMark("1");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeDown.setChangeNum(1);
                    visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
                }
                if (visaChangeDown.getOutsourcingAmount().equals("")){
                    visaChangeDown.setOutsourcingAmount(null);
                }
                visaChangeMapper.insertSelective(visaChangeDown);

            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
            baseProject1.setVisaStatus("2");
            baseProject1.setProjectFlow(baseProject1.getProjectFlow()+",5");

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }

            //修改文件外键
            Example example1 = new Example(FileInfo.class);
            Example.Criteria c = example1.createCriteria();
            c.andLike("type","qzbgxmxj%");
            c.andEqualTo("status","0");
            c.andEqualTo("platCode",id);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
            for (FileInfo fileInfo : fileInfos) {
                //修改文件外键
                fileInfoService.updateFileName2(fileInfo.getId(),visaChangeDown.getId());
            }
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("5"); //签证变更项目
            operationLog.setContent(memberManage.getMemberName()+"新增了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            // 如果上下家都有就存两家，如果只有一家就只存一家
            if (visaChangeUp != null && visaChangeDown != null){
                String upId = visaChangeUp.getId();
                String downId = visaChangeDown.getId();
                operationLog.setDoObject(upId+","+downId); // 项目标识
            }else if (visaChangeUp != null){
                operationLog.setDoObject(visaChangeUp.getId()); // 项目标识
            }else if (visaChangeDown != null){
                operationLog.setDoObject(visaChangeDown.getId()); // 项目标识
            }
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);

            //2B只允许存在下家
        } else if (baseProject.getAB().equals("2")) {
            //如果不存在送审金额字段则判断下家申请不存在
            if (visaChangeVo.getVisaApplyChangeInformationDown().getApplicantName() != null && !visaChangeVo.getVisaApplyChangeInformationDown().getApplicantName().equals("")) {
                visaApplyChangeInformationDown = visaChangeVo.getVisaApplyChangeInformationDown();
                visaApplyChangeInformationDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaApplyChangeInformationDown.setCreateTime(sim.format(new Date()));
                visaApplyChangeInformationDown.setFouderId(id);
                visaApplyChangeInformationDown.setState("0");
                visaApplyChangeInformationDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaApplyChangeInformationDown.setUpAndDownMark("1");
                visaApplyChangeInformationMapper.insertSelective(visaApplyChangeInformationDown);
            }

                visaChangeDown = visaChangeVo.getVisaChangeDown();
                visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeDown.setCreateTime(sim.format(new Date()));
                visaChangeDown.setCreatorId(id);
                visaChangeDown.setState("0");
                visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaChangeDown.setChangeNum(1);
                visaChangeDown.setCumulativeChangeAmount(visaChangeVo.getVisaChangeDown().getAmountVisaChange());
                visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
                visaChangeDown.setUpAndDownMark("1");
                if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                    visaChangeDown.setChangeNum(1);
                    visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
                }
            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if ("".equals(visaChangeDown.getOutsourcingAmount())){
                visaChangeDown.setOutsourcingAmount(null);
            }
            if (visaChangeDown.getProportionContract().length()>60){
                visaChangeDown.setProportionContract(null);
            }

                visaChangeMapper.insertSelective(visaChangeDown);


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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
                //消息通知
                MessageVo messageVo = new MessageVo();
                String projectName = baseProject.getProjectName();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(visaChangeVo.getAuditId());
                //审核人名字
                String name = memberManage.getMemberName();
                messageVo.setId("A12");
                messageVo.setUserId(visaChangeVo.getAuditId());
                messageVo.setType("1"); //通知
                messageVo.setTitle("您有一个签证变更项目待审核！");
                messageVo.setDetails(name + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批！");
                messageService.sendOrClose(messageVo);
            }
            Example example1 = new Example(FileInfo.class);
            Example.Criteria c = example1.createCriteria();
            c.andLike("type","qzbgxmxj%");
            c.andEqualTo("status","0");
            c.andEqualTo("platCode",id);
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
            for (FileInfo fileInfo : fileInfos) {
                //修改文件外键
                    fileInfoService.updateFileName2(fileInfo.getId(),visaChangeDown.getId());

            }
            // 操作日志
            String userId = loginUser.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("5"); //签证变更项目
            operationLog.setContent(memberManage.getMemberName()+"编辑了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(visaChangeDown.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
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
            VisaChange visaChange = new VisaChange();
            visaChange.setOutsourcing("2");
            visaChangeVo.setVisaChangeUp(visaChange);
            VisaChange visaChange1 = new VisaChange();
            visaChange1.setOutsourcing("2");
            visaChangeVo.setVisaChangeDown(visaChange1);

            Example example2 = new Example(VisaChange.class);
            Example.Criteria cc = example2.createCriteria();
            cc.andEqualTo("baseProjectId",baseId);
            cc.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example2);
            VisaChange v = null;
            for (VisaChange change : visaChanges) {
                if (change.getUpAndDownMark().equals("1")){
                    v = change;
                }
            }

            Example example1 = new Example(FileInfo.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("type","qzbgxmxjbcsjqz");
            criteria.andEqualTo("status","0");
            criteria.andEqualTo("platCode",v.getId());
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
            for (FileInfo fileInfo : fileInfos) {
                fileInfo.setStatus("1");
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }

            Example example3 = new Example(FileInfo.class);
            Example.Criteria criteria1 = example3.createCriteria();
            criteria1.andEqualTo("type","qzbgxmxjbcxjqz");
            criteria1.andEqualTo("status","0");
            criteria1.andEqualTo("platCode",v.getId());
            List<FileInfo> fileInfos1 = fileInfoMapper.selectByExample(example3);
            for (FileInfo fileInfo : fileInfos1) {
                fileInfo.setStatus("1");
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }


        }

        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
        if (baseProject.getVisaStatus().equals("2")){
            Integer changeNum = visaChangeVo.getVisaChangeDown().getChangeNum();
            if (changeNum>1){
                visaChangeVo.setShowCart("1");
            }else{
                visaChangeVo.setShowCart("0");
            }
        }else {
            visaChangeVo.setShowCart("1");
        }
        return visaChangeVo;
    }

    @Override
    public void batchReview(BatchReviewVo batchReviewVo, UserInfo loginUser,HttpServletRequest request) {

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                auditInfo.setAuditTime(this.sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                //一审通过
                if (auditInfo.getAuditType().equals("0")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("1");
//                    Example example2 = new Example(MkyUser.class);
//                    Example.Criteria c3 = example2.createCriteria();
//                    c3.andEqualTo("roleId", "role7615");
//                    c3.andEqualTo("delFlag", "0");
//                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
                    Example example2 = new Example(MemberManage.class);
                    Example.Criteria criteria = example2.createCriteria();
                    criteria.andEqualTo("id",visaChange.getCreatorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example2);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(sim.format(new Date()));
                    auditInfo1.setUpdateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                    //二审通过
                } else if (auditInfo.getAuditType().equals("1")) {

                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                    baseProject.setVisaStatus("4");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);

//                    AuditInfo auditInfo1 = new AuditInfo();
//                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
//                    auditInfo1.setBaseProjectId(visaChange.getId());
//                    auditInfo1.setAuditResult("0");
//                    auditInfo1.setAuditType("4");
////                    Example example2 = new Example(MkyUser.class);
////                    Example.Criteria c3 = example2.createCriteria();
////                    c3.andEqualTo("roleId", "role7614");
////                    c3.andEqualTo("delFlag", "0");
////                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
////                    auditInfo1.setAuditorId(mkyUser.getId());
//                    Example example2 = new Example(MemberManage.class);
//                    Example.Criteria criteria = example2.createCriteria();
//                    criteria.andEqualTo("id",visaChange.getCreatorId());
//                    MemberManage memberManage = memberManageDao.selectOneByExample(example2);
//                    if (memberManage.getWorkType().equals("1")){
//                        auditInfo1.setAuditorId(whzjm);
//                    }else if(memberManage.getWorkType().equals("2")){
//                        auditInfo1.setAuditorId(wjzjm);
//                    }
//                    auditInfo1.setFounderId(id);
//                    auditInfo1.setStatus("0");
//                    auditInfo1.setCreateTime(this.sim.format(new Date()));
//                    auditInfo1.setUpdateTime(sim.format(new Date()));
//                    auditInfoDao.insertSelective(auditInfo1);
                    //三审通过
                } else if (auditInfo.getAuditType().equals("4")) {
                    String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                    baseProject.setVisaStatus("5");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    // 三审通过插入委外金额
                    BigDecimal sumAmount = new BigDecimal(0);
                    for (VisaChange thisVisa : visaChanges) {
                        if ("1".equals(thisVisa.getOutsourcing())){
                                sumAmount=sumAmount.add(new BigDecimal(thisVisa.getOutsourcingAmount()));
                        }else {
                            sumAmount = new BigDecimal(0);
                        }
                    }
                    OutSource outSource = new OutSource();
                    outSource.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    outSource.setOutMoney(sumAmount.toString());
                    outSource.setBaseProjectId(baseProject.getId());
                    outSource.setDistrict(baseProject.getDistrict());
                    outSource.setDept("2"); // 1 设计 2 造价
                    outSource.setDelFlag("0");
                    outSource.setOutType("4"); // 签证/变更委外金额
                    outSource.setBaseProjectId(baseProject.getId());
                    outSource.setDistrict(baseProject.getDistrict());
                    outSource.setProjectNum(visaChange.getId());
                    outSource.setCreateTime(data);
                    outSource.setUpdateTime(data);
//                    outSource.setFounderCompanyId();
//                    outSource.setFounderId();
                    outSourceMapper.insertSelective(outSource);
                    MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                    //审核通过发送消息
                    MessageVo messageVo = new MessageVo();
                    messageVo.setId("A14");
                    messageVo.setType("1"); // 通知
                    messageVo.setUserId(auditInfo.getAuditorId());
                    messageVo.setTitle("您有一个签证变更项目已通过！");
                    messageVo.setDetails(memberManage.getMemberName() + "您好！【" + username + "】已将【" + baseProject.getProjectName() + "】的签证/变更项目提交给您，请审批！");
                    messageService.sendOrClose(messageVo);
                } else if (auditInfo.getAuditType().equals("2")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("3");
//                    Example example2 = new Example(MkyUser.class);
//                    Example.Criteria c3 = example2.createCriteria();
//                    c3.andEqualTo("roleId", "role7615");
//                    c3.andEqualTo("delFlag", "0");
//                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
//                    auditInfo1.setAuditorId(mkyUser.getId());
                    Example example2 = new Example(MemberManage.class);
                    Example.Criteria criteria = example2.createCriteria();
                    criteria.andEqualTo("id",visaChange.getCreatorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example2);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjh);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjh);
                    }
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(this.sim.format(new Date()));
                    auditInfo1.setUpdateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                } else if (auditInfo.getAuditType().equals("3")) {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("5");
//                    Example example2 = new Example(MkyUser.class);
//                    Example.Criteria c3 = example2.createCriteria();
//                    c3.andEqualTo("roleId", "role7614");
//                    c3.andEqualTo("delFlag", "0");
//                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
//                    auditInfo1.setAuditorId(mkyUser.getId());
                    Example example2 = new Example(MemberManage.class);
                    Example.Criteria criteria = example2.createCriteria();
                    criteria.andEqualTo("id",visaChange.getCreatorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example2);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(this.sim.format(new Date()));
                    auditInfo1.setUpdateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);

                } else if (auditInfo.getAuditType().equals("5")) {

                    String f = "";

                    for (VisaChange change : visaChanges) {
                        if (change.getAmountVisaChange() == null){
                            f = "b";
                        }
                    }
                    if (f.equals("")){
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                        baseProject.setVisaStatus("5");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);
                    }else if(f.equals("b")){
                        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                        baseProject.setVisaStatus("2");
                        baseProjectDao.updateByPrimaryKeySelective(baseProject);

                        for (VisaChange change : visaChanges) {

                                Example example2 = new Example(AuditInfo.class);
                                Example.Criteria c3 = example2.createCriteria();
                                c3.andEqualTo("baseProjectId",change.getId());
                                c3.andEqualTo("status","0");
                                List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
                                for (AuditInfo info : auditInfos) {
                                    auditInfoDao.deleteByPrimaryKey(info);
                                }

                        }

                    }


                }
                // 操作日志
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                String userId = loginUser.getId();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                operationLog.setName(userId);
                operationLog.setType("5"); //签证变更项目
                operationLog.setContent(memberManage.getMemberName()+"审核通过了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
                operationLog.setDoObject(visaChange.getId()); // 项目标识
                operationLog.setStatus("0");
                operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);
            } else if (batchReviewVo.getAuditResult().equals("2")) {
                auditInfo.setAuditResult("2");
                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
                auditInfo.setAuditTime(this.sim.format(new Date()));
                auditInfo.setUpdateTime(sim.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);


                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
                baseProject.setVisaStatus("3");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);

//                //项目名称
                String projectName = baseProject.getProjectName();
                //成员名称
                String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
                //如果不通过发送消息
                MessageVo messageVo1 = new MessageVo();
                messageVo1.setId("A14");
                messageVo1.setType("1"); //通知
                messageVo1.setUserId(auditInfo.getAuditorId());
                messageVo1.setTitle("您有一个签证变更项目未通过！");
                messageVo1.setDetails(username + "您好！您所提交的【" + projectName + "】的签证/变更项目【" + name + "】审批未通过，请查看详情！");
                //调用消息Service
                messageService.sendOrClose(messageVo1);
                // 操作日志
                String userId = loginUser.getId();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
                OperationLog operationLog = new OperationLog();
                operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
                operationLog.setName(userId);
                operationLog.setType("5"); //签证变更项目
                operationLog.setContent(baseProject.getProjectName()+"项目【"+baseProject.getId()+"】"+memberManage.getMemberName()+"审核未通过");
                operationLog.setDoObject(visaChange.getId()); // 项目标识
                operationLog.setStatus("0");
                operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                String ip = memberService.getIp(request);
                operationLog.setIp(ip);
                operationLogDao.insertSelective(operationLog);
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

            for (VisaChange visaChange : visaChanges1) {
                visaChanges.add(visaChange);
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

        for (VisaChangeStatisticVo visaChangeStatisticVo : visaChangeStatisticVos) {
            String visaChangeUpProportionContract = visaChangeStatisticVo.getVisaChangeUpProportionContract();
            String visaChangeDownProportionContract = visaChangeStatisticVo.getVisaChangeDownProportionContract();
            if (visaChangeUpProportionContract!=null && !"".equals(visaChangeUpProportionContract)){
                BigDecimal bigDecimal = new BigDecimal(visaChangeUpProportionContract);
                BigDecimal bigDecimal2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                visaChangeStatisticVo.setVisaChangeUpProportionContract(bigDecimal2.toString());
            }
            if (visaChangeDownProportionContract!=null && !"".equals(visaChangeDownProportionContract)){
                BigDecimal bigDecimal1 = new BigDecimal(visaChangeDownProportionContract);
                BigDecimal bigDecimal3 = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP);
                visaChangeStatisticVo.setVisaChangeDownProportionContract(bigDecimal3.toString());
            }
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
    public void deleteVisa(String baseId,UserInfo loginUser,HttpServletRequest request) {
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
            // 操作日志
            String userId = loginUser.getId();
            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("5"); //进度款项目
            operationLog.setContent(memberManage.getMemberName()+"删除了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
            operationLog.setDoObject(visaChange.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
        }

    }

    @Override
    public List<MemberManage> costOfPersonnel() {
        Example example = new Example(MemberManage.class);
        example.createCriteria().andEqualTo("deptId","2")
                                .andEqualTo("status","0");
        List<MemberManage> memberManages = memberManageDao.selectByExample(example);
        return memberManages;
    }

    @Override
    public void renewFile(String baseId, String visaNum) {

        if(visaNum.equals("1")){
            Example example2 = new Example(VisaChange.class);
            Example.Criteria cc = example2.createCriteria();
            cc.andEqualTo("baseProjectId",baseId);
            cc.andEqualTo("state","0");
            List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example2);
            VisaChange v = null;
            for (VisaChange change : visaChanges) {
                if (change.getUpAndDownMark().equals("1")){
                    v = change;
                }
            }

            Example example1 = new Example(FileInfo.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("type","qzbgxmxjbcsjqz");
            criteria.andEqualTo("status","1");
            criteria.andEqualTo("platCode",v.getId());
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example1);
            for (FileInfo fileInfo : fileInfos) {
                fileInfo.setStatus("0");
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }

            Example example3 = new Example(FileInfo.class);
            Example.Criteria criteria1 = example3.createCriteria();
            criteria1.andEqualTo("type","qzbgxmxjbcxjqz");
            criteria1.andEqualTo("status","1");
            criteria1.andEqualTo("platCode",v.getId());
            List<FileInfo> fileInfos1 = fileInfoMapper.selectByExample(example3);
            for (FileInfo fileInfo : fileInfos1) {
                fileInfo.setStatus("0");
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
        }

    }

    @Override
    public void editOutSourceMoney(String id, String upMoney,String downMoney) {
        Example example = new Example(VisaChange.class);
        example.createCriteria().andEqualTo("id",id);
        List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
       if (visaChanges.size()>0){
           for (VisaChange visaChange : visaChanges) {
               //如果是上家
               if ("0".equals(visaChange.getUpAndDownMark())){
                   visaChange.setOutsourcingAmount(upMoney);
                   visaChangeMapper.updateByPrimaryKeySelective(visaChange);
                   //如果是上家
               }else if ("1".equals(visaChange.getUpAndDownMark())){
                   visaChange.setOutsourcingAmount(downMoney);
                   visaChangeMapper.updateByPrimaryKeySelective(visaChange);
               }
           }
       }
    }

    @Override
    public void visaSuccess(String ids, String id) {
        String[] split = ids.split(",");
        for (String s : split) {

            Example example = new Example(VisaChange.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",s);
            c.andEqualTo("state","0");
            c.andEqualTo("upAndDownMark","1");
            VisaChange visaChange = visaChangeMapper.selectOneByExample(example);
            if (visaChange!=null){
                Example example1 = new Example(AuditInfo.class);
                Example.Criteria criteria = example1.createCriteria();
                criteria.andEqualTo("baseProjectId",visaChange.getId());
                criteria.andEqualTo("status","0");
                criteria.andEqualTo("auditType","1");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
                if (!id.equals(auditInfo.getAuditorId())){
                    throw new RuntimeException("此操作只能由所选项目部门领导来完成");
                }else{

                    BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                    baseProject.setVisaStatus("1");
                    baseProjectDao.updateByPrimaryKeySelective(baseProject);

                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(visaChange.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("4");
//                    Example example2 = new Example(MkyUser.class);
//                    Example.Criteria c3 = example2.createCriteria();
//                    c3.andEqualTo("roleId", "role7614");
//                    c3.andEqualTo("delFlag", "0");
//                    MkyUser mkyUser = mkyUserMapper.selectOneByExample(example2);
//                    auditInfo1.setAuditorId(mkyUser.getId());
                    Example example2 = new Example(MemberManage.class);
                    Example.Criteria criteria1 = example2.createCriteria();
                    criteria1.andEqualTo("id",visaChange.getCreatorId());
                    MemberManage memberManage = memberManageDao.selectOneByExample(example2);
                    if (memberManage.getWorkType().equals("1")){
                        auditInfo1.setAuditorId(whzjm);
                    }else if(memberManage.getWorkType().equals("2")){
                        auditInfo1.setAuditorId(wjzjm);
                    }
                    auditInfo1.setFounderId(id);
                    auditInfo1.setStatus("0");
                    auditInfo1.setCreateTime(this.sim.format(new Date()));
                    auditInfo1.setUpdateTime(sim.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);
                }
            }
        }
    }

    @Override
    public void visaBack(String baseId, String backOption) {

        Example example = new Example(VisaChange.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",baseId);
        c.andEqualTo("state","0");
        c.andEqualTo("upAndDownMark","1");
        VisaChange visaChange = visaChangeMapper.selectOneByExample(example);

        if (visaChange!=null){
            Example example1 = new Example(AuditInfo.class);
            Example.Criteria criteria = example1.createCriteria();
            criteria.andEqualTo("baseProjectId",visaChange.getId());
            criteria.andEqualTo("status","0");
            criteria.andEqualTo("auditType","1");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example1);
            auditInfo.setAuditResult("2");
            auditInfo.setAuditOpinion(backOption);
            auditInfo.setAuditTime(sim.format(new Date()));
            auditInfo.setUpdateTime(sim.format(new Date()));
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);

            BaseProject baseProject = baseProjectDao.selectByPrimaryKey(baseId);
            baseProject.setVisaStatus("7");
            baseProjectDao.updateByPrimaryKeySelective(baseProject);
        }
    }

    @Override
    public void updateVisa(VisaChangeVo visaChangeVo, UserInfo loginUser,HttpServletRequest request) {
        String id = loginUser.getId();
//        String id = "user309";
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
                    if (visaChange.getChangeNum()!=null){
                        upNum = visaChange.getChangeNum();
                        bigDecimal = visaChange.getCumulativeChangeAmount();
                    }
                } else if (visaChange.getUpAndDownMark().equals("1")) {
                    if (visaChange.getChangeNum()!=null){
                        downNum = visaChange.getChangeNum();
                        bigDecimal1 = visaChange.getCumulativeChangeAmount();
                    }
                }
                visaChange.setState("2");
                visaChangeMapper.updateByPrimaryKeySelective(visaChange);
            }

            BaseProject baseProject2 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                VisaChange visaChangeUp = visaChangeVo.getVisaChangeUp();
            if ("".equals(visaChangeUp.getAmountVisaChange())){
                visaChangeUp.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeUp.getContractAmount())){
                visaChangeUp.setContractAmount(null);
            }
            if ("".equals(visaChangeUp.getOutsourcingAmount())){
                visaChangeUp.setOutsourcingAmount(null);
            }
            if (visaChangeUp.getProportionContract().length()>60){
                visaChangeUp.setProportionContract(null);
            }
                visaChangeUp.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeUp.setCreateTime(sim.format(new Date()));
                visaChangeUp.setCreatorId(id);
                visaChangeUp.setState("0");
                visaChangeUp.setBaseProjectId(visaChangeVo.getBaseId());
                visaChangeUp.setApplyChangeInfoId(visaApplyChangeInformationUp.getId());
                visaChangeUp.setUpAndDownMark("0");
                visaChangeUp.setChangeNum(upNum + 1);
                if (bigDecimal == null){
                    bigDecimal = new BigDecimal(0);
                }
                if (visaChangeUp.getAmountVisaChange() == null){
                    visaChangeUp.setCumulativeChangeAmount(bigDecimal.add(new BigDecimal(0)));
                }else{
                    visaChangeUp.setCumulativeChangeAmount(bigDecimal.add(visaChangeUp.getAmountVisaChange()));
                }


//                visaChangeUp.setCumulativeChangeAmount(visaChangeUp.getAmountVisaChange());

                visaChangeMapper.insertSelective(visaChangeUp);

                VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if ("".equals(visaChangeDown.getOutsourcingAmount())){
                visaChangeDown.setOutsourcingAmount(null);
            }
            if (visaChangeDown.getProportionContract().length()>60){
                visaChangeDown.setProportionContract(null);
            }
            if("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
                visaChangeDown.setId(UUID.randomUUID().toString().replace("-", ""));
                visaChangeDown.setCreateTime(sim.format(new Date()));
                visaChangeDown.setCreatorId(id);
                visaChangeDown.setState("0");
                visaChangeDown.setBaseProjectId(visaChangeVo.getBaseId());
                visaChangeDown.setApplyChangeInfoId(visaApplyChangeInformationDown.getId());
                visaChangeDown.setUpAndDownMark("1");
                visaChangeDown.setChangeNum(downNum + 1);

            if (bigDecimal1 == null){
                bigDecimal1 = new BigDecimal(0);
            }
            if (visaChangeDown.getAmountVisaChange() == null){
                visaChangeDown.setCumulativeChangeAmount(bigDecimal1.add(new BigDecimal(0)));

            }else{
                visaChangeDown.setCumulativeChangeAmount(bigDecimal1.add(visaChangeDown.getAmountVisaChange()));
            }
//                visaChangeDown.setCumulativeChangeAmount(visaChangeDown.getAmountVisaChange());
                visaChangeMapper.insertSelective(visaChangeDown);


            VisaChange change = null;
            for (VisaChange visaChange : visaChanges) {
                if (visaChange.getUpAndDownMark().equals("1")){
                    change = visaChange;
                }
            }

            Example example2 = new Example(FileInfo.class);
            Example.Criteria criteria = example2.createCriteria();
            criteria.andLike("type","qzbgxmxj%");
            criteria.andEqualTo("status","0");
            criteria.andEqualTo("platCode",change.getId());
            List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example2);
            for (FileInfo fileInfo : fileInfos) {
                fileInfo.setPlatCode(visaChangeDown.getId());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }

//            Example example4 = new Example(FileInfo.class);
//            Example.Criteria criteria1 = example4.createCriteria();
//            criteria1.andEqualTo("type","qzbgxmxjbcxjqz");
//            criteria1.andEqualTo("status","0");
//            criteria1.andEqualTo("platCode",change.getId());
//            List<FileInfo> fileInfos1 = fileInfoMapper.selectByExample(example4);
//            for (FileInfo fileInfo : fileInfos1) {
//                fileInfo.setPlatCode(visaChangeDown.getId());
//                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
//            }


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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
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

            if ("".equals(visaChangeUp.getAmountVisaChange())){
                visaChangeUp.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeUp.getContractAmount())){
                visaChangeUp.setContractAmount(null);
            }
            if ("".equals(visaChangeUp.getOutsourcingAmount())){
                visaChangeUp.setOutsourcingAmount(null);
            }
            if (Double.parseDouble(visaChangeUp.getProportionContract())<60){
                visaChangeUp.setProportionContract(null);
            }
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);


            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            c3.andEqualTo("upAndDownMark", "1");
            VisaChange visaChange2 = visaChangeMapper.selectOneByExample(example3);
            visaChangeDown.setId(visaChange2.getId());

            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if ("".equals(visaChangeDown.getOutsourcingAmount())){
                visaChangeDown.setOutsourcingAmount(null);
            }
            if (visaChangeDown.getProportionContract().length()>60){
                visaChangeDown.setProportionContract(null);
            }
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);
            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
//            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && visaChangeVo.getAuditNumber().equals("0")) {

//                baseProject1.setVisaStatus("1");
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());

                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());
                System.out.println(baseProject.getVisaStatus());

                if ("7".equals(baseProject.getVisaStatus())){
                    Example example4 = new Example(AuditInfo.class);
                    Example.Criteria c4 = example4.createCriteria();
                    c4.andEqualTo("baseProjectId",visaChangeDown.getId());
                    c4.andEqualTo("status","0");
                    List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example4);
                    for (AuditInfo auditInfo : auditInfos) {
                        auditInfoDao.deleteByPrimaryKey(auditInfo);
                    }
                }
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //普通编辑
        } else if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("3")) {



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

            if ("".equals(visaChangeUp.getAmountVisaChange())){
                visaChangeUp.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeUp.getContractAmount())){
                visaChangeUp.setContractAmount(null);
            }
            if ("".equals(visaChangeUp.getOutsourcingAmount())){
                visaChangeUp.setOutsourcingAmount(null);
            }
            if (Double.parseDouble(visaChangeUp.getProportionContract())<60){
                visaChangeUp.setProportionContract(null);
            }
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);


            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            c3.andEqualTo("upAndDownMark", "1");
            VisaChange visaChange2 = visaChangeMapper.selectOneByExample(example3);
            visaChangeDown.setId(visaChange2.getId());

            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if ("".equals(visaChangeDown.getOutsourcingAmount())){
                visaChangeDown.setOutsourcingAmount(null);
            }
            if (Double.parseDouble(visaChangeDown.getProportionContract())<60){
                visaChangeDown.setProportionContract(null);
            }

            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);


            BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
//            baseProject1.setVisaStatus("2");
            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {

                BigDecimal cumulativeChangeAmount = visaChangeUp.getCumulativeChangeAmount();
                if (cumulativeChangeAmount==null){
                    BigDecimal amountVisaChange = visaChangeUp.getAmountVisaChange();
                    if (amountVisaChange!=null){
                        visaChangeUp.setCumulativeChangeAmount(amountVisaChange);
                        visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);
                    }
                }
                BigDecimal cumulativeChangeAmount1 = visaChangeDown.getCumulativeChangeAmount();
                if (cumulativeChangeAmount1==null){
                    BigDecimal amountVisaChange = visaChangeDown.getAmountVisaChange();
                    if (amountVisaChange!=null){
                        visaChangeDown.setCumulativeChangeAmount(amountVisaChange);
                        visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);
                    }
                }

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                auditInfoDao.insertSelective(auditInfo);
            }
            baseProjectDao.updateByPrimaryKeySelective(baseProject1);
            //未通过提交
        } else if (visaChangeVo.getVisaNum() != null && visaChangeVo.getVisaNum().equals("4")) {
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

            if ("".equals(visaChangeUp.getAmountVisaChange())){
                visaChangeUp.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeUp.getContractAmount())){
                visaChangeUp.setContractAmount(null);
            }
            if ("".equals(visaChangeUp.getOutsourcingAmount())){
                visaChangeUp.setOutsourcingAmount(null);
            }
            if (Double.parseDouble(visaChangeUp.getProportionContract())<60){
                visaChangeUp.setProportionContract(null);
            }

            visaChangeMapper.updateByPrimaryKeySelective(visaChangeUp);


            VisaChange visaChangeDown = visaChangeVo.getVisaChangeDown();
            Example example3 = new Example(VisaChange.class);
            Example.Criteria c3 = example3.createCriteria();
            c3.andEqualTo("baseProjectId", visaChangeVo.getBaseId());
            c3.andEqualTo("state", "0");
            c3.andEqualTo("upAndDownMark", "1");
            VisaChange visaChange2 = visaChangeMapper.selectOneByExample(example3);
            visaChangeDown.setId(visaChange2.getId());

            if ("".equals(visaChangeDown.getAmountVisaChange())){
                visaChangeDown.setAmountVisaChange(null);
            }
            if ("".equals(visaChangeDown.getContractAmount())){
                visaChangeDown.setContractAmount(null);
            }
            if ("".equals(visaChangeDown.getOutsourcingAmount())){
                visaChangeDown.setOutsourcingAmount(null);
            }
            if (Double.parseDouble(visaChangeDown.getProportionContract())<60){
                visaChangeDown.setProportionContract(null);
            }
            visaChangeMapper.updateByPrimaryKeySelective(visaChangeDown);

            if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")) {
                BaseProject baseProject1 = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                baseProject1.setVisaStatus("1");
                Example example4 = new Example(AuditInfo.class);
                Example.Criteria c4 = example4.createCriteria();
                if (visaChangeDown.getId() != null && !visaChangeDown.equals("")) {
                    c4.andEqualTo("baseProjectId", visaChangeDown.getId());
                } else if (visaChangeUp.getId() != null && !visaChangeUp.getId().equals("")) {
                    c4.andEqualTo("baseProjectId", visaChangeUp.getId());
                }
                c4.andEqualTo("status", "0");
                c4.andEqualTo("auditResult", "2");
                AuditInfo auditInfo = auditInfoDao.selectOneByExample(example4);
                auditInfo.setAuditResult("0");
                auditInfo.setAuditOpinion("");
                auditInfo.setAuditTime("");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                auditInfo.setUpdateTime(simpleDateFormat.format(new Date()));
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);

                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
                baseProject.setVisaStatus("1");
                baseProjectDao.updateByPrimaryKeySelective(baseProject);
             //消息通知
//                String username = "造价业务员三";
                String username = loginUser.getUsername();
                String projectName = baseProject.getProjectName();
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId());
                //审核人名字
                String name = memberManage.getMemberName();
                MessageVo messageVo = new MessageVo();
                messageVo.setId("A13");
                messageVo.setType("1"); // 通知
                messageVo.setUserId(auditInfo.getAuditorId());
                messageVo.setTitle("您有一个签证变更项目待审核！");
                messageVo.setDetails(name + "您好！【" + username + "】已将【" + projectName + "】的签证/变更项目提交给您，请审批！");
                //调用消息Service
                messageService.sendOrClose(messageVo);
            }
        }
        // 操作日志
        String userId = loginUser.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(visaChangeVo.getBaseId());
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("5"); //签证变更项目
        operationLog.setContent(memberManage.getMemberName()+"新增了"+baseProject.getProjectName()+"项目【"+baseProject.getId()+"】");
        // 如果上下家都有就存两家，如果只有一家就只存一家
        if (visaChangeVo.getVisaChangeUp() != null && visaChangeVo.getVisaChangeDown() != null){
            String upId = visaChangeVo.getVisaChangeUp().getId();
            String downId = visaChangeVo.getVisaChangeDown().getId();
            operationLog.setDoObject(upId+","+downId); // 项目标识
        }else if (visaChangeVo.getVisaChangeUp() != null){
            operationLog.setDoObject(visaChangeVo.getVisaChangeUp().getId()); // 项目标识
        }else if (visaChangeVo.getVisaChangeDown() != null){
            operationLog.setDoObject(visaChangeVo.getVisaChangeDown().getId()); // 项目标识
        }
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
        if (visaChangeVo.getAuditNumber() != null && !visaChangeVo.getAuditNumber().equals("")){



        }

//

    }
}

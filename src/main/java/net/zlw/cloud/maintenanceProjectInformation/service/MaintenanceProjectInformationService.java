package net.zlw.cloud.maintenanceProjectInformation.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.budgeting.mapper.SurveyInformationDao;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.mapper.OutSourceMapper;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.designProject.model.OutSource;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.*;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.mapper.InvestigationOfTheAmountDao;
import net.zlw.cloud.settleAccounts.mapper.OtherInfoMapper;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.service.MessageService;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author dell
 * @Date 2020/9/27 10:52
 * @Version 1.0
 */
@Service
@Transactional
public class MaintenanceProjectInformationService {

    @Resource
    private MaintenanceProjectInformationMapper maintenanceProjectInformationMapper;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;

    @Resource
    private AuditInfoDao auditInfoDao;

    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private MemberService memberService;

    @Resource
    private OperationLogDao operationLogDao;

    @Resource
    private SurveyInformationDao surveyInformationDao;

    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;

    @Resource
    private InvestigationOfTheAmountDao investigationOfTheAmountDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private BaseProjectDao baseProjectDao;

    @Autowired
    private OtherInfoMapper otherInfoMapper;

    @Resource
    private MessageService messageService;

    @Resource
    private OutSourceMapper outSourceMapper;
    @Resource
    private MkyUserMapper mkyUserMapper;

    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh;  //芜湖造造价领导
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm; //芜湖造造价经理

    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh; //吴江造价领导
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm; //吴江造价经理



    /**
     * 分页查询所有
     *
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformationReturnVo> findAllMaintenanceProjectInformation(PageRequest pageRequest, UserInfo userInfo) {
        //获得当前登入人
        //todo userInfo.getId();
        String userInfoId = userInfo.getId();
//        String userInfoId = "200101005";
        pageRequest.setUid(userInfoId);

        //设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        //查看当前审核人与当前创建人
        List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos0 = maintenanceProjectInformationMapper.selectAllByDelFlag0(pageRequest);
        //查看所有
        List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos1 = maintenanceProjectInformationMapper.selectAllByDelFlag1(pageRequest);

        PageInfo<MaintenanceProjectInformationReturnVo> projectInformationPageInfo = new PageInfo<>();

        //如果是待审核
        if("1".equals(pageRequest.getType())){
            //如果是部门经理或者部门领导查看列表则看所有
            if(whzjh.equals(userInfoId)||whzjm.equals(userInfoId)||wjzjh.equals(userInfoId)||wjzjm.equals(userInfoId)){

                for (MaintenanceProjectInformationReturnVo thisVo : maintenanceProjectInformationReturnVos1) {
                    Example example2 = new Example(AuditInfo.class);
                    example2.createCriteria().andEqualTo("baseProjectId",thisVo.getId())
                                                .andEqualTo("maintenanceFlag", "0")
                                             .andEqualTo("status","0");
                    List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
                    if (auditInfos.size() >0){
                        thisVo.setMaintenanceFlag("检维修确认审核");
                    }else {
                        thisVo.setMaintenanceFlag("检维修审核");
                    }
                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(thisVo.getPreparePeople());
                    if (memberManage1 != null){
                        thisVo.setPreparePeople(memberManage1.getMemberName());
                    }
                    //获取处理人
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria()
                            .andEqualTo("baseProjectId", thisVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo != null){
                        //获得当前处理人
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if(memberManage!=null){
                            thisVo.setCurrentHandler(memberManage.getMemberName());
                        }else{
                            thisVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos1);
            }else{
                //获取处理人
                for (MaintenanceProjectInformationReturnVo thisVo : maintenanceProjectInformationReturnVos0) {
                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(thisVo.getPreparePeople());
                    if (memberManage1 != null){
                        thisVo.setPreparePeople(memberManage1.getMemberName());
                    }
                    Example example = new Example(AuditInfo.class);
                    example.createCriteria()
                            .andEqualTo("baseProjectId", thisVo.getId())
                            .andEqualTo("auditResult","0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo != null){
                        //获得当前处理人
                        Example example1 = new Example(MemberManage.class);
                        example1.createCriteria().andEqualTo("id",auditInfo.getAuditorId());
                        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                        if(memberManage!=null){
                            thisVo.setCurrentHandler(memberManage.getMemberName());
                        }else{
                            thisVo.setCurrentHandler(memberManage.getMemberName());
                        }
                    }
                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos0);
            }
        }else{
            //已完成
            if("5".equals(pageRequest.getType())){
                for (MaintenanceProjectInformationReturnVo vo : maintenanceProjectInformationReturnVos1) {
                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(vo.getPreparePeople());
                    if (memberManage1 != null){
                        vo.setPreparePeople(memberManage1.getMemberName());
                    }
                    if(vo.getFounderId().equals(userInfoId)){
                        vo.setFounderId("1");
                    }
                    if (userInfoId.equals(whzjm)){
                        vo.setYinShow("1");
                    }
                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos1);
                //待确认
            }else if("4".equals(pageRequest.getType())){
                //待确认
                List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos2 = maintenanceProjectInformationMapper.selectAllByUn(pageRequest);

                for (MaintenanceProjectInformationReturnVo vo : maintenanceProjectInformationReturnVos2) {
                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(vo.getPreparePeople());
                    if (memberManage1 != null) {
                        vo.setPreparePeople(memberManage1.getMemberName());
                    }
                    if (vo.getFounderId().equals(userInfoId)) {
                        vo.setFounderId("1");
                    }

                    Example example = new Example(AuditInfo.class);
                    Example.Criteria cc = example.createCriteria();
                    cc.andEqualTo("baseProjectId",vo.getId());
                    cc.andEqualTo("status","0");
                    cc.andEqualTo("auditType","1");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
                    if (auditInfo.getAuditorId().equals(userInfo.getId())){
                        vo.setBackShow("1");
                    }

                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos2);
                //未通过
            } else if("3".equals(pageRequest.getType())){

                if (userInfoId.equals(whzjm) || userInfoId.equals(whzjh) || userInfoId.equals(wjzjh)){
                    pageRequest.setUid("");
                }

                //未通过
                List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos3 = maintenanceProjectInformationMapper.selectAllByBack(pageRequest);

                for (MaintenanceProjectInformationReturnVo vo : maintenanceProjectInformationReturnVos3) {
                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(vo.getPreparePeople());
                    if (memberManage1 != null) {
                        vo.setPreparePeople(memberManage1.getMemberName());
                    }
                    if (vo.getFounderId().equals(userInfoId)) {
                        vo.setEditFlag("0");
                        vo.setFounderId("1");
                    }

                    MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectByPrimaryKey(vo.getId());
                    if ("6".equals(maintenanceProjectInformation.getType())){
                        vo.setUnShow("1");
                    }
                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos3);
            } else{
                pageRequest.setMaintenanceItemType("");

                if (userInfoId.equals(whzjm) || userInfoId.equals(whzjh) || userInfoId.equals(wjzjh)){
                    pageRequest.setUid("");
                }

                //查看当前创建人
                List<MaintenanceProjectInformationReturnVo> maintenanceProjectInformationReturnVos = maintenanceProjectInformationMapper.selectAllByDelFlag(pageRequest);
                //处理中  所有(根据创建人)
                for (MaintenanceProjectInformationReturnVo vo : maintenanceProjectInformationReturnVos) {
                    // 审核状态
                    Example example2 = new Example(AuditInfo.class);
                    example2.createCriteria().andEqualTo("baseProjectId", vo.getId())
                            .andEqualTo("maintenanceFlag", "0")
                            .andNotEqualTo("auditResult", "2")
                            .andEqualTo("auditType", "2")
                            .andEqualTo("status", "0");
                    AuditInfo auditInfo = auditInfoDao.selectOneByExample(example2);

                    if (auditInfo != null) {
                        vo.setMaintenanceFlag("检维修确认未通过");
                    } else {
                        vo.setMaintenanceFlag("检维修未通过");
                    }

                    // 编制人
                    MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(vo.getPreparePeople());
                    if (memberManage1 != null) {
                        vo.setPreparePeople(memberManage1.getMemberName());
                    }
                    if (vo.getFounderId().equals(userInfoId)) {
                        vo.setEditFlag("0");
                        vo.setFounderId("1");
                    }
                }
                projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformationReturnVos);
            }
        }
        System.out.println("list:"+projectInformationPageInfo.getList().toString());
        return projectInformationPageInfo;
}

    /**
     * 统计分析 列表
     *
     * @param userInfo
     * @return
     */
    public PageInfo<MaintenanceProjectInformation> list(PageRequest pageRequest, UserInfo userInfo) {
        //todo        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        Example example = new Example(MaintenanceProjectInformation.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag", "0");
        // 查询条件  状态
        if (pageRequest.getType() != null && (!"".equals(pageRequest.getType()))) {
            criteria.andEqualTo("type", pageRequest.getType());
        }

        // TODO 登陆人
//        criteria.andEqualTo("founderId",userInfo.getId());
        //  地区
        if (pageRequest.getDistrict() != null && (!"".equals(pageRequest.getDistrict()))) {
            criteria.andEqualTo("projectAddress", pageRequest.getDistrict());
        }


        // 查询条件 开始时间
        if (pageRequest.getStartTime() != null && (!"".equals(pageRequest.getStartTime()))) {
            String startTime = pageRequest.getStartTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.andGreaterThanOrEqualTo("createTime", date);

        }


        // 查询条件 结束时间
        if (pageRequest.getEndTime() != null && (!"".equals(pageRequest.getEndTime()))) {
            String endTime = pageRequest.getEndTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            criteria.andLessThanOrEqualTo("createTime", date);

        }

        // 查询条件 内容
        if (pageRequest.getKeyWord() != null && (!"".equals(pageRequest.getKeyWord()))) {
            criteria.andLike("maintenanceItemName", "%" + pageRequest.getKeyWord() + "%");
        }

        List<MaintenanceProjectInformation> maintenanceProjectInformations = maintenanceProjectInformationMapper.selectByExample(example);
//        List<MaintenanceProjectInformation> maintenanceProjectInformations = maintenanceProjectInformationMapper.selectAllByDelFlag();

        for (MaintenanceProjectInformation maintenanceProjectInformation : maintenanceProjectInformations) {

            //滤空判断
            if (maintenanceProjectInformation.getConstructionUnitId() != null && (!"".equals(maintenanceProjectInformation.getConstructionUnitId()))) {
                ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectById(maintenanceProjectInformation.getConstructionUnitId());
//                maintenanceProjectInformation.setConstructionUnitManagement(constructionUnitManagement);
                //获取施工单位名字
                if (null != constructionUnitManagement && null != constructionUnitManagement.getConstructionUnitName()) {
                    maintenanceProjectInformation.setConstructionUnitName(constructionUnitManagement.getConstructionUnitName());
                }
            }


            if (maintenanceProjectInformation.getPreparePeople() != null && (!"".equals(maintenanceProjectInformation.getPreparePeople()))) {
                MemberManage memberManage = memberManageDao.selectByIdAndStatus(maintenanceProjectInformation.getPreparePeople());
                if (memberManage != null) {
                    maintenanceProjectInformation.setPreparePeople(memberManage.getMemberName());
                }
            }

//            maintenanceProjectInformation.setFounderId(userInfo.getId());
//            maintenanceProjectInformation.setFounderCompanyId(userInfo.getCompanyId());
        }

        PageInfo<MaintenanceProjectInformation> projectInformationPageInfo = new PageInfo<>(maintenanceProjectInformations);

        System.err.println("list:" + projectInformationPageInfo.getList().toString());

        return projectInformationPageInfo;

    }


    /**
     * 检维修--删除
     *
     * @param id
     */
    public void deleteMaintenanceProjectInformation(String id,UserInfo userInfo,HttpServletRequest request) {
        maintenanceProjectInformationMapper.deleteMaintenanceProjectInformation(id);
        MaintenanceProjectInformation mainInfo = maintenanceProjectInformationMapper.selectByPrimaryKey(id);
        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("7"); //检维修项目
        operationLog.setContent(memberManage.getMemberName()+"删除了"+mainInfo.getMaintenanceItemName()+"项目【"+mainInfo.getId()+"】");
        operationLog.setDoObject(mainInfo.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
    }


    /**
     * 检维修--新增--提交
     *
     * @param maintenanceProjectInformation
     * @param userInfo
     */
    public void addMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation, UserInfo userInfo,HttpServletRequest request) {

        if ("".equals(maintenanceProjectInformation.getPreparePeople())){
            maintenanceProjectInformation.setPreparePeople(userInfo.getId());
        }else {
            String nameById = memberManageDao.findNameById(maintenanceProjectInformation.getPreparePeople());
            maintenanceProjectInformation.setPreparePeople(nameById);
        }
        if ("".equals(maintenanceProjectInformation.getPreparePeople2())){
            maintenanceProjectInformation.setPreparePeople2(userInfo.getId());
        }else {
            String nameById = memberManageDao.findNameById(maintenanceProjectInformation.getPreparePeople2());
            maintenanceProjectInformation.setPreparePeople2(nameById);
        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.err.println(uuid);
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());



        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();

        information.setId(uuid);
//        information.setId(maintenanceProjectInformation.getId());
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        if (StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())) {
            information.setType("1");
        } else {
            information.setType("2");
        }
        // 项目编号格式 （WJ-202012-随机ID)
        int randomMainId = new Random().nextInt(998) + 1;
        String data = new SimpleDateFormat("yyyydd").format(new Date());
        // 如果是芜湖
        if ("1".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("WH-"+data+"-"+randomMainId);
        // 如果是马鞍山
        }else if ("2".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("MAS-"+data+"-"+randomMainId);
        }else if ("3".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("JB-"+data+"-"+randomMainId);
        }else if ("4".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("WJ-"+data+"-"+randomMainId);
        }
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setArea(maintenanceProjectInformation.getArea());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setCustomerName(maintenanceProjectInformation.getCustomerName());
        // todo `review_amount` decimal 判断如不传值，判断空字符串，赋值0
        if ("".equals(maintenanceProjectInformation.getReviewAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            information.setReviewAmount(bd);
        } else {
            information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        }

        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        if (userInfo != null) {
            information.setFounderId(userInfo.getId());
            information.setFounderCompanyId(userInfo.getCompanyId());
        } else {
            information.setFounderId("user312");
        }


        //结算审核信息
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        String saiId = UUID.randomUUID().toString().replace("-", "");
        settlementAuditInformation.setId(saiId);
        settlementAuditInformation.setCreateTime(simpleDateFormat.format(new Date()));

        // todo 检维修项目信息id
        settlementAuditInformation.setMaintenanceProjectInformation(information.getId());

        //  `authorized_number` decimal
        if ("".equals(maintenanceProjectInformation.getAuthorizedNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAuthorizedNumber(bd);
        } else {
            settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
        }
        // `subtract_the_number` decimal
        if ("".equals(maintenanceProjectInformation.getSubtractTheNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setSubtractTheNumber(bd);
        } else {
            settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
        }

        // `nuclear_number` decimal
        if ("".equals(maintenanceProjectInformation.getNuclearNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setNuclearNumber(bd);
        } else {
            settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());
        }

        // `contract_amount` decimal
        if ("".equals(maintenanceProjectInformation.getContractAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setContractAmount(bd);
        } else {
            settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
        }

        settlementAuditInformation.setBaseProjectId(uuid);
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
        // `amount_outsourcing` decimal
        if ("".equals(maintenanceProjectInformation.getAmountOutsourcing())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAmountOutsourcing(bd);
        } else {
            settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
        }

        settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

        if (userInfo != null) {
            settlementAuditInformation.setFounderId(userInfo.getId());
            settlementAuditInformation.setFounderCompanyId(userInfo.getCompanyId());
        } else {
            settlementAuditInformation.setFounderId("user312");
        }

        // 计算核减率
        BigDecimal reviewAmount = maintenanceProjectInformation.getReviewAmount(); // 送审金额
        BigDecimal subtractTheNumber = settlementAuditInformation.getSubtractTheNumber(); // 核减数
        // 核减数 / 送审金额 * 100 = 核减率 (使用除法要精确到固定的值，这里精确到小数后10位)
        BigDecimal divide = subtractTheNumber.divide(reviewAmount,10,BigDecimal.ROUND_HALF_UP);
        BigDecimal subtractRate = divide.multiply(new BigDecimal(100));
        // 保留两位小数。四舍五入
        BigDecimal setScale = subtractRate.setScale(2, BigDecimal.ROUND_HALF_UP);
        settlementAuditInformation.setSubtractRate(setScale);
        settlementAuditInformationDao.insertSelective(settlementAuditInformation);


        // 勘探金额

        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
        String ioaId = UUID.randomUUID().toString().replace("-", "");

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
        investigationOfTheAmount.setId(ioaId);
        investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
        investigationOfTheAmount.setDelFlag("0");

        if (userInfo != null) {
            investigationOfTheAmount.setFounderId(userInfo.getId());
            investigationOfTheAmount.setFounderCompanyId(userInfo.getCompanyId());
        }
//        else {
//            investigationOfTheAmount.setFounderId("user312");
//        }

        investigationOfTheAmount.setMaintenanceProjectInformation(information.getId());
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(information.getId());

//        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.get);
        // `unbalanced_quotation_adjustment` decimal
        if ("".equals(maintenanceProjectInformation.getUnbalancedQuotationAdjustment())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(bd);
        } else {
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        }

//        `punish_amount` decimal
        if ("".equals(maintenanceProjectInformation.getPunishAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setPunishAmount(bd);
        } else {
            investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        }

//        `outbound_amount` decimal
        if ("".equals(maintenanceProjectInformation.getOutboundAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setOutboundAmount(bd);
        } else {
            investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        }

//        `material_difference_amount` decimal
        if ("".equals(maintenanceProjectInformation.getMaterialDifferenceAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setMaterialDifferenceAmount(bd);
        } else {
            investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());
        }


        investigationOfTheAmountDao.insert(investigationOfTheAmount);


        // 审核信息

        if (StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())) {
            AuditInfo auditInfo = new AuditInfo();
            String s = UUID.randomUUID().toString().replaceAll("-", "");
            auditInfo.setId(s);
            auditInfo.setBaseProjectId(information.getId());
            auditInfo.setAuditResult("0");
            auditInfo.setAuditType("0");
            auditInfo.setStatus("0");
            if (userInfo != null) {
                auditInfo.setFounderId(userInfo.getId());
                auditInfo.setCompanyId(userInfo.getCompanyId());
            }
//            auditInfo.setFounderId("user312");
            auditInfo.setFounderId(userInfo.getId());
            // 审核人id
            auditInfo.setAuditorId(maintenanceProjectInformation.getAuditorId());
            auditInfo.setCreateTime(createTime);
            auditInfo.setMaintenanceFlag("1"); //不是二次审核
            auditInfoDao.insertSelective(auditInfo);
            //通过发消息
            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
            //检维修名字
            String maintenanceItemName = maintenanceProjectInformation.getMaintenanceItemName();
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A21");
            messageVo.setUserId(auditInfo.getAuditorId());
            messageVo.setType("1"); // 通知
            messageVo.setTitle("您有一个检维修项目已通过！");
            messageVo.setDetails(name+"您好！【"+userInfo.getUsername()+"】提交的【"+maintenanceItemName+"】项目已通过，请查看详情!");
            messageService.sendOrClose(messageVo);
        }
        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("7"); //检维修项目
        operationLog.setContent(memberManage.getMemberName()+"新增提交了了"+information.getMaintenanceItemName()+"项目【"+information.getId()+"】");
        operationLog.setDoObject(information.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);

        // 其他信息
        //其他信息表
        if (!"".equals(maintenanceProjectInformation.getComs()) && maintenanceProjectInformation.getComs() != null){
            Json coms = maintenanceProjectInformation.getComs();
            String json = coms.value();
            int num = 1;
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(information.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(createTime);
                    otherInfo1.setStatus("0");
                    otherInfo1.setFoundId(userInfo.getId());
                    otherInfo1.setFounderCompany(userInfo.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    num++;
                    otherInfoMapper.insertSelective(otherInfo1);
                }
            }
        }

//        MemberManage memberManage = memberManageDao.selectByIdAndStatus(auditInfo.getId());
        maintenanceProjectInformationMapper.insertSelective(information);

        // type
        List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType());

        for (FileInfo fileInfo : byFreignAndType) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type1
        List<FileInfo> byFreignAndType1 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType1());

        for (FileInfo fileInfo : byFreignAndType1) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type2
        List<FileInfo> byFreignAndType2 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType2());

        for (FileInfo fileInfo : byFreignAndType2) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type3
        List<FileInfo> byFreignAndType3 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType3());

        for (FileInfo fileInfo : byFreignAndType3) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }
        // type4
        List<FileInfo> byFreignAndType4 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType4());

        for (FileInfo fileInfo : byFreignAndType4) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type5
        List<FileInfo> byFreignAndType5 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType5());

        for (FileInfo fileInfo : byFreignAndType5) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type6
        List<FileInfo> byFreignAndType6 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType6());

        for (FileInfo fileInfo : byFreignAndType6) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type7
        List<FileInfo> byFreignAndType7 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType7());

        for (FileInfo fileInfo : byFreignAndType7) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type8
        List<FileInfo> byFreignAndType8 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType8());

        for (FileInfo fileInfo : byFreignAndType8) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }


        // type9
        List<FileInfo> byFreignAndType9 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType9());

        for (FileInfo fileInfo : byFreignAndType9) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }


    }


    /**
     * 批量审核
     *
     * @param batchReviewVo
     */
    public void batchReview(BatchReviewVo batchReviewVo,UserInfo userInfo,HttpServletRequest request) {
        //todo userInfo.getId(); userInfo.getUsername(); userInfo.getCompanyId();
        String id = userInfo.getId();
        String username = userInfo.getUsername();
        String companyId = userInfo.getCompanyId();

        //查询当前审核信息(未审核信息)
        Example example = new Example(AuditInfo.class);
        example.createCriteria()
                .andEqualTo("baseProjectId",batchReviewVo.getBatchAll())
                .andEqualTo("auditResult", "0");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);

        //根据主键id查询当前项目
        MaintenanceProjectInformation maintenanceProjectInformation =
                maintenanceProjectInformationMapper.selectById(batchReviewVo.getBatchAll());

        //根据当前用户创建人来判断走那个流程
        MemberManage createMember = memberManageDao.selectByPrimaryKey(maintenanceProjectInformation.getFounderId());

        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        if(id.equals(whzjh)||id.equals(wjzjh)) {
            //二审
            if ("1".equals(batchReviewVo.getAuditResult())) {
                //如果为通过
                //创建一条三审审核信息
                AuditInfo newAuditInfo = new AuditInfo();
                String auditInfouuid = UUID.randomUUID().toString().replaceAll("-", "");
                newAuditInfo.setId(auditInfouuid);
                newAuditInfo.setBaseProjectId(maintenanceProjectInformation.getId());
                //如果当前检维修项目为2次审核
                if ("0".equals(auditInfo.getMaintenanceFlag())) {

                } else {
                    //当前状态为三审
                    newAuditInfo.setMaintenanceFlag("1");
                    newAuditInfo.setAuditType("4");
                }
                //审核结果 结果待审核
                newAuditInfo.setAuditResult("0");
                //判断当前项目走那套流程
                if ("1".equals(createMember.getWorkType())) {
                    newAuditInfo.setAuditorId(whzjm);
                } else {
                    newAuditInfo.setAuditorId(wjzjm);
                }
                newAuditInfo.setCreateTime(createTime);
                newAuditInfo.setFounderId(id);
                newAuditInfo.setCompanyId(companyId);
                newAuditInfo.setStatus("0");
                maintenanceProjectInformation.setType("4"); //修改当前项目状态为 待确认
//                auditInfoDao.insert(newAuditInfo);
            } else {
                //如果未通过
                maintenanceProjectInformation.setType("3"); //修改当前项目状态为 未通过
            }
            //修改之前的审核信息
            if("0".equals(auditInfo.getMaintenanceFlag())){

            }else{
                //信息变为二审
                auditInfo.setAuditType("1");
                auditInfo.setMaintenanceFlag("1");
            }
            auditInfo.setAuditResult(batchReviewVo.getAuditResult());
            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
            auditInfo.setAuditTime(createTime);
            auditInfo.setUpdateTime(createTime);
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
        }else if(id.equals(whzjm)||id.equals(wjzjm)){
            String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            //三审
            if("1".equals(batchReviewVo.getAuditResult())){
                if("0".equals(auditInfo.getMaintenanceFlag())){
                    //审核信息写入 变更三审
//                    auditInfo.setAuditType("5");
//                    auditInfo.setMaintenanceFlag("0");
                }else{
                    //审核信息写入 三审
                    auditInfo.setAuditType("4");
                    auditInfo.setMaintenanceFlag("1");
                }

                //判断当前项目是否未变更项目
                if("0".equals(auditInfo.getMaintenanceFlag())){
                    //如果是 说明已经流程结束
                    maintenanceProjectInformation.setType("5");
                }else{
                    maintenanceProjectInformation.setType("5");
                }
                Example example1 = new Example(SettlementAuditInformation.class);
                example1.createCriteria().andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());
                SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
                OutSource outSource = new OutSource();
                outSource.setId(UUID.randomUUID().toString().replaceAll("-",""));
                if ("1".equals(settlementAuditInformation.getOutsourcing())){
                    outSource.setOutMoney(settlementAuditInformation.getAmountOutsourcing().toString());
                }else {
                    outSource.setOutMoney("0");
                }
                outSource.setDept("2"); //1.设计 2.造价
                outSource.setDelFlag("0"); //0.正常 1.删除
                outSource.setOutType("7"); // 检维修委外金额
                outSource.setProjectNum(maintenanceProjectInformation.getId()); //跟踪审计信息外键
                outSource.setCreateTime(data);
                outSource.setUpdateTime(data);
                outSource.setFounderId(maintenanceProjectInformation.getFounderId()); //项目创建人
//                outSource.setFounderCompanyId(maintenanceProjectInformation.getFounderCompanyId()); //公司
                outSourceMapper.insertSelective(outSource);
            }else{
                //如果未通过
                maintenanceProjectInformation.setType("3");
            }
            auditInfo.setAuditResult(batchReviewVo.getAuditResult());
            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
            auditInfo.setUpdateTime(createTime);
            auditInfo.setAuditTime(createTime);
            auditInfo.setAuditTime(createTime);
            maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
        }else{
            //互审
            if("1".equals(batchReviewVo.getAuditResult())){
                //如果为通过
                //创建一条二审审核信息
                AuditInfo newAuditInfo = new AuditInfo();
                String auditInfouuid = UUID.randomUUID().toString().replaceAll("-","");
                newAuditInfo.setId(auditInfouuid);
                newAuditInfo.setBaseProjectId(maintenanceProjectInformation.getId());
                //如果当前检维修项目为2次审核
                if("0".equals(auditInfo.getMaintenanceFlag())){
                    //将当前状态为 变更二审
//                    newAuditInfo.setMaintenanceFlag("0");
//                    newAuditInfo.setAuditType("3");
                }else{
                    //当前状态为二审
                    newAuditInfo.setMaintenanceFlag("1");
                    newAuditInfo.setAuditType("1");
                }
                //审核结果 结果待审核
                newAuditInfo.setAuditResult("0");
                //判断当前项目走那套流程
                if("1".equals(createMember.getWorkType())){
                    newAuditInfo.setAuditorId(whzjh);
                }else{
                    newAuditInfo.setAuditorId(wjzjh);
                }
                newAuditInfo.setCreateTime(createTime);
                newAuditInfo.setFounderId(id);
                newAuditInfo.setCompanyId(companyId);
                newAuditInfo.setStatus("0");
                maintenanceProjectInformation.setType("1"); //修改当前项目状态为 待审核
                auditInfoDao.insert(newAuditInfo);
            }else{
                //如果未通过
                maintenanceProjectInformation.setType("3"); //修改当前项目状态为 未通过
            }
            //修改之前的审核信息
            auditInfo.setAuditResult(batchReviewVo.getAuditResult());
            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
            auditInfo.setAuditTime(createTime);
            auditInfo.setUpdateTime(createTime);
            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
            maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
        }

        if(batchReviewVo.getAuditResult().equals("1")){
            // 操作日志
            String userId = userInfo.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("7"); //检维修项目
            operationLog.setContent(memberManage.getMemberName()+"审核通过了"+maintenanceProjectInformation.getMaintenanceItemName()+"项目【"+maintenanceProjectInformation.getId()+"】");
            operationLog.setDoObject(maintenanceProjectInformation.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            //通过发消息
            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
            //检维修名字
            String maintenanceItemName = maintenanceProjectInformation.getMaintenanceItemName();
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A23");
            messageVo.setUserId(id);
            messageVo.setType("1"); // 通知
            messageVo.setTitle("您有一个检维修项目已通过！");
            messageVo.setDetails(name+"您好！【"+username+"】提交的【"+maintenanceItemName+"】项目已通过，请查看详情!");
            messageService.sendOrClose(messageVo);
        }else if(batchReviewVo.getAuditResult().equals("2")){
            // 操作日志
            String userId = userInfo.getId();
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
            OperationLog operationLog = new OperationLog();
            operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
            operationLog.setName(userId);
            operationLog.setType("7"); //检维修项目
            operationLog.setContent(maintenanceProjectInformation.getMaintenanceItemName()+"项目【"+maintenanceProjectInformation.getId()+"】"+memberManage.getMemberName()+"审核未通过");
            operationLog.setDoObject(maintenanceProjectInformation.getId()); // 项目标识
            operationLog.setStatus("0");
            operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String ip = memberService.getIp(request);
            operationLog.setIp(ip);
            operationLogDao.insertSelective(operationLog);
            //未通过发消息
            String maintenanceItemName = maintenanceProjectInformation.getMaintenanceItemName();
            //检维修名字
            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
            MessageVo messageVo1 = new MessageVo();
            messageVo1.setId("A23");
            messageVo1.setType("1"); // 通知
            messageVo1.setUserId(id);
            messageVo1.setTitle("您有一个检维修项目未通过！");
            messageVo1.setDetails(name+"您好！【"+username+"】已将【"+maintenanceItemName+"】的项目未通过，请查看详情!");
            //调用消息Service
            messageService.sendOrClose(messageVo1);
        }

//        // 未审核
//        maintenanceProjectInformation.setType("1");
//
//        // 判断更改状态
//        if (batchReviewVo.getAuditResult().equals("1")) {
//            // 0 一审
//            if (auditInfo.getAuditType().equals("0")) {
//                // 审核通过
//                auditInfo.setAuditResult("1");
//                //一级审批的意见，时间
//                auditInfo.setAuditTime(sdf.format(date));
//                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                //修改审批状态
//                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                // 待确认
//                maintenanceProjectInformation.setType("4");
//
////                            auditInfo.setAuditTime(format);
////                            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//
//
//                maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
//                Date date = new Date();
//                String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
//                //                            一审通过在审核表插入一条数据
//                AuditInfo auditInfo1 = new AuditInfo();
//                auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
//                auditInfo1.setBaseProjectId(s);
//                auditInfo1.setAuditResult("0");
//                auditInfo1.setAuditType("1");
//                auditInfo1.setCreateTime(format);
//                Example example1 = new Example(MemberManage.class);
//                example1.createCriteria().andEqualTo("status", "0").andEqualTo("depId", "2").andEqualTo("depAdmin", "1");
//                MemberManage memberManage = memberManageDao.selectOneByExample(example1);
//                auditInfo1.setAuditorId(memberManage.getId());
//                auditInfoDao.insertSelective(auditInfo1);
//            } else if (auditInfo.getAuditType().equals("1")) {//二审
//                auditInfo.setAuditResult("1");
//                maintenanceProjectInformation.setType("5");
//                Date date = new Date();
//                String format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(date);
//                auditInfo.setAuditTime(format);
//                auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//                maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
//            }
//            String projectName = baseProjectDao.selectByPrimaryKey(auditInfo.getAuditorId()).getProjectName();
//            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
//            //通过一次发送一次
//            MessageVo messageVo = new MessageVo();
//            messageVo.setId("A23");
//            messageVo.setUserId(id);
//            messageVo.setTitle("您有一个检维修项目已通过！");
//            messageVo.setDetails(name+"您好！【"+username+"】提交的【"+projectName+"】项目已通过，请查看详情!");
//            messageService.sendOrClose(messageVo);
//        } else if (batchReviewVo.getAuditResult().equals("2")) {
//            auditInfo.setAuditResult("2");
//            auditInfo.setAuditOpinion(batchReviewVo.getAuditOpinion());
//            maintenanceProjectInformation.setType("3");
//            auditInfo.setAuditTime(sdf.format(date));
//            auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//            maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);//未通过发送消息
//            String projectName = baseProjectDao.selectByPrimaryKey(auditInfo.getAuditorId()).getProjectName();
//            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
//            MessageVo messageVo1 = new MessageVo();
//            messageVo1.setId("A23");
//            messageVo1.setUserId(id);
//            messageVo1.setTitle("您有一个检维修项目未通过！");
//            messageVo1.setDetails(name+"您好！【"+username+"】已将【"+projectName+"】的项目未通过，请查看详情!");
//            //调用消息Service
//            messageService.sendOrClose(messageVo1);
//        }
    }

    /**
     * 编辑
     *
     * @param maintenanceProjectInformationVo
     * @param userInfo
     */
    public void updateMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformationVo, UserInfo userInfo,HttpServletRequest request) {

        //修改时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());
        // 转换
         // 检维修类型
        if ("道路恢复工程".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("0");
        } else if ("表位改造".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("1");
        } else if ("故障换表".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("2");
        } else if ("水表周检换表".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("3");
        } else if ("DN300以上管道抢维修".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("4");
        } else if ("DN300以下管道抢维修".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("5");
        } else if ("设备维修购置".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("6");
        } else if ("房屋修缮".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("7");
        } else if ("绿化种植".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("8");
        } else if ("装饰及装修".equals(maintenanceProjectInformationVo.getMaintenanceItemType())) {
            maintenanceProjectInformationVo.setMaintenanceItemType("9");
        }
        // 施工单位
        if ("施工单位2".equals(maintenanceProjectInformationVo.getConstructionUnitId())) {
            maintenanceProjectInformationVo.setConstructionUnitId("1041");
        } else if ("施工单位3".equals(maintenanceProjectInformationVo.getConstructionUnitId())) {
            maintenanceProjectInformationVo.setConstructionUnitId("7643");
        } else if ("施工单位5".equals(maintenanceProjectInformationVo.getConstructionUnitId())) {
            maintenanceProjectInformationVo.setConstructionUnitId("7645");
        } else if ("施工单位7".equals(maintenanceProjectInformationVo.getConstructionUnitId())) {
            maintenanceProjectInformationVo.setConstructionUnitId("7661");
        }
        // 根据检维修项目id  查询到的 检维修对象
        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectBymaintenanceItemId(maintenanceProjectInformationVo.getId());
        // 检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(maintenanceProjectInformation.getId());
        // 修改时间
        information.setUpdateTime(updateTime);

//        information.setMaintenanceItemId(maintenanceProjectInformationVo.getMaintenanceItemId());
        information.setMaintenanceItemName(maintenanceProjectInformationVo.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformationVo.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformationVo.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformationVo.getSubmitTime());
//        编制人
        information.setPreparePeople(maintenanceProjectInformationVo.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformationVo.getProjectAddress());
        information.setArea(maintenanceProjectInformationVo.getArea());
        information.setConstructionUnitId(maintenanceProjectInformationVo.getConstructionUnitId());
        // todo `review_amount` decimal 判断如不传值，判断空字符串，赋值0
        if ("".equals(maintenanceProjectInformation.getReviewAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            information.setReviewAmount(bd);
        } else {
            information.setReviewAmount(maintenanceProjectInformationVo.getReviewAmount());
        }
        information.setRemarkes(maintenanceProjectInformationVo.getRemarkes());
        information.setCustomerName(maintenanceProjectInformationVo.getCustomerName());
        //        information.setFounderId(userInfo.getId());

//        information.setFounderCompanyId(userInfo.getCompanyId());


        //结算审核信息
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        settlementAuditInformation.setUpdateTime(simpleDateFormat.format(new Date()));

        // todo 检维修项目信息id
        settlementAuditInformation.setMaintenanceProjectInformation(information.getId());


        //  `authorized_number` decimal
        if ("".equals(maintenanceProjectInformationVo.getAuthorizedNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAuthorizedNumber(bd);
        } else {
            settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformationVo.getAuthorizedNumber());
        }
        // `subtract_the_number` decimal
        if ("".equals(maintenanceProjectInformationVo.getSubtractTheNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setSubtractTheNumber(bd);
        } else {
            settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformationVo.getSubtractTheNumber());
        }

        // `nuclear_number` decimal
        if ("".equals(maintenanceProjectInformationVo.getNuclearNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setNuclearNumber(bd);
        } else {
            settlementAuditInformation.setNuclearNumber(maintenanceProjectInformationVo.getNuclearNumber());
        }

        // `contract_amount` decimal
        if ("".equals(maintenanceProjectInformationVo.getContractAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setContractAmount(bd);
        } else {
            settlementAuditInformation.setContractAmount(maintenanceProjectInformationVo.getContractAmount());
        }
        settlementAuditInformation.setBaseProjectId(information.getId());
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformationVo.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformationVo.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformationVo.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformationVo.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformationVo.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformationVo.getContactPhone());
        // `amount_outsourcing` decimal
        if ("".equals(maintenanceProjectInformationVo.getAmountOutsourcing())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAmountOutsourcing(bd);
        } else {
            settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformationVo.getAmountOutsourcing());

        }
        settlementAuditInformation.setCompileTime(maintenanceProjectInformationVo.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformationVo.getRemark());

        Example example = new Example(SettlementAuditInformation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("maintenanceProjectInformation", maintenanceProjectInformationVo.getId());

        SettlementAuditInformation selectOneByExample = settlementAuditInformationDao.selectOneByExample(example);

        // 计算核减率
        BigDecimal reviewAmount = maintenanceProjectInformationVo.getReviewAmount(); // 送审金额
        BigDecimal subtractTheNumber = settlementAuditInformation.getSubtractTheNumber(); // 核减数
        // 核减数 / 送审金额 * 100 = 核减率 (使用除法要精确到固定的值，这里精确到小数后10位)
        BigDecimal divide = subtractTheNumber.divide(reviewAmount,10,BigDecimal.ROUND_HALF_UP);
        BigDecimal subtractRate = divide.multiply(new BigDecimal(100));
        // 保留两位小数。四舍五入
        BigDecimal setScale = subtractRate.setScale(2, BigDecimal.ROUND_HALF_UP);
        settlementAuditInformation.setSubtractRate(setScale);

        if (selectOneByExample!=null){
            settlementAuditInformation.setId(selectOneByExample.getId());// 计算核减率
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        } else {
            settlementAuditInformation.setId(UUID.randomUUID().toString().replace("-",""));
            settlementAuditInformationDao.insertSelective(settlementAuditInformation);
        }



        // 勘探金额

        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();


        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformationVo.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformationVo.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformationVo.getSurveyBriefly());

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformationVo.getAmountRemarks());
        investigationOfTheAmount.setUpdateTime(simpleDateFormat.format(new Date()));

        investigationOfTheAmount.setMaintenanceProjectInformation(information.getId());
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(information.getId());

//        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.get);

        // `unbalanced_quotation_adjustment` decimal
        if ("".equals(maintenanceProjectInformationVo.getUnbalancedQuotationAdjustment())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(bd);
        } else {
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformationVo.getUnbalancedQuotationAdjustment());
        }

//        `punish_amount` decimal
        if ("".equals(maintenanceProjectInformationVo.getPunishAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setPunishAmount(bd);
        } else {
            investigationOfTheAmount.setPunishAmount(maintenanceProjectInformationVo.getPunishAmount());
        }

//        `outbound_amount` decimal
        if ("".equals(maintenanceProjectInformationVo.getOutboundAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setOutboundAmount(bd);
        } else {
            investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformationVo.getOutboundAmount());
        }

//        `material_difference_amount` decimal
        if ("".equals(maintenanceProjectInformationVo.getMaterialDifferenceAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setMaterialDifferenceAmount(bd);
        } else {
            investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformationVo.getMaterialDifferenceAmount());
        }


        Example example1 = new Example(InvestigationOfTheAmount.class);
        Example.Criteria example1Criteria = example1.createCriteria();
        example1Criteria.andEqualTo("maintenanceProjectInformation", maintenanceProjectInformationVo.getId());

        InvestigationOfTheAmount ofTheAmount = investigationOfTheAmountDao.selectOneByExample(example1);

        if (ofTheAmount != null){
            investigationOfTheAmount.setId(ofTheAmount.getId());
            investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);
        } else {
            investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-", ""));
            investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);
        }


        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("7"); //检维修项目
        operationLog.setContent(memberManage.getMemberName()+"修改提交了"+information.getMaintenanceItemName()+"项目【"+information.getId()+"】");
        operationLog.setDoObject(information.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
        //新审核判断

        Example example2 = new Example(AuditInfo.class);
        Example.Criteria criteria1 = example2.createCriteria();
        criteria1.andEqualTo("baseProjectId", information.getId());
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);

        //进行中判断 如果为空说明处理中提交
        if(maintenanceProjectInformationVo.getAuditorId()!=null){

            MaintenanceProjectInformation maintenanceProjectInformation1 = maintenanceProjectInformationMapper.selectByPrimaryKey(maintenanceProjectInformationVo.getId());
            if ("6".equals(maintenanceProjectInformation1.getType())){
                Example example3 = new Example(AuditInfo.class);
                Example.Criteria criteria2 = example3.createCriteria();
                criteria2.andEqualTo("baseProjectId",maintenanceProjectInformation1.getId());
                criteria2.andEqualTo("status","0");
                List<AuditInfo> auditInfos1 = auditInfoDao.selectByExample(example3);
                for (AuditInfo auditInfo : auditInfos1) {
                    auditInfoDao.deleteByPrimaryKey(auditInfo);
                }
            }

            //如果当前审核人不为空 说明是处理中提交
            //创建一条新的审核信息
            AuditInfo auditInfo1 = new AuditInfo();
            String s = UUID.randomUUID().toString().replaceAll("-", "");
            String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
            auditInfo1.setId(s);
            auditInfo1.setBaseProjectId(information.getId());
            auditInfo1.setAuditResult("0");
            //如果当前审核信息有三条说明审核流程一轮已完成

                auditInfo1.setAuditType("0");
                auditInfo1.setMaintenanceFlag("1");

            auditInfo1.setStatus("0");
            auditInfo1.setAuditOpinion("");
            auditInfo1.setAuditTime("");
            auditInfo1.setAuditorId(maintenanceProjectInformationVo.getAuditorId()); // 审核人id
            auditInfo1.setCreateTime(createDate);
            auditInfoDao.insertSelective(auditInfo1);
            information.setType("1");
            //通过发消息
            String name = memberManageDao.selectByPrimaryKey(auditInfo1.getAuditorId()).getMemberName();
            //检维修名字
            String maintenanceItemName = maintenanceProjectInformation.getMaintenanceItemName();
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A22");
            messageVo.setUserId(auditInfo1.getAuditorId());
            messageVo.setType("1"); // 通知
            messageVo.setTitle("您有一个检维修项目已通过！");
            messageVo.setDetails(name+"您好！【"+userInfo.getUsername()+"】提交的【"+maintenanceItemName+"】项目已通过，请查看详情!");
            messageService.sendOrClose(messageVo);
        }else{
            //如果为空说明是未通过提交
            //修改未通过的数据
            Example example3 = new Example(AuditInfo.class);
            Example.Criteria criteria2 = example3.createCriteria();
            //查找到该条未通过的数据
            criteria2.andEqualTo("baseProjectId", information.getId())
                    .andEqualTo("auditResult","2");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example3);
            if(auditInfo!=null){
                //将审核状态改为待审核
                auditInfo.setAuditResult("0");
                auditInfo.setAuditOpinion("");
                auditInfo.setAuditTime("");
                auditInfoDao.updateByPrimaryKeySelective(auditInfo);
                information.setType("1");
            }
            //未通过发消息
            String name = memberManageDao.selectByPrimaryKey(auditInfo.getAuditorId()).getMemberName();
            //检维修名字
            String maintenanceItemName = maintenanceProjectInformation.getMaintenanceItemName();
            MessageVo messageVo = new MessageVo();
            messageVo.setId("A22");
            messageVo.setUserId(auditInfo.getAuditorId());
            messageVo.setType("1"); // 通知
            messageVo.setTitle("您有一个检维修项目未通过！");
            messageVo.setDetails(userInfo.getUsername()+"您好！您提交的【"+maintenanceItemName+"】项目【"+name+"】审核未通过，请及时查看详情!");
            messageService.sendOrClose(messageVo);
        }
        // json转换
        Json coms = maintenanceProjectInformationVo.getComs();
        if (coms != null){
            String json = coms.value();
            Example example3 = new Example(OtherInfo.class);
            example3.createCriteria().andEqualTo("foreignKey",information.getId());
            List<OtherInfo> otherInfos1 = otherInfoMapper.selectByExample(example3);
            for (OtherInfo thisOther : otherInfos1) {
                thisOther.setStatus("1");
                otherInfoMapper.updateByPrimaryKeySelective(thisOther);
            }
            int num = 1;
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(information.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    otherInfo1.setStatus("0");
                    otherInfo1.setFoundId(userInfo.getId());
                    otherInfo1.setFounderCompany(userInfo.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    num++;
                    otherInfoMapper.insertSelective(otherInfo1);
                }
            }
        }

        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);
//        // 审核信息
//
//        Example example2 = new Example(AuditInfo.class);
//        Example.Criteria criteria1 = example2.createCriteria();
//        criteria1.andEqualTo("baseProjectId", information.getId());
//
//        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example2);
//
//        for (AuditInfo auditInfo : auditInfos) {
//            if ("2".equals(auditInfo.getAuditResult())) {
//
//                Example example3 = new Example(AuditInfo.class);
//                Example.Criteria criteria2 = example3.createCriteria();
//                criteria2.andEqualTo("baseProjectId", information.getId());
//
//                List<AuditInfo> auditInfos1 = auditInfoDao.selectByExample(example3);
//                for (AuditInfo info : auditInfos1) {
//                    if ("1".equals(info.getAuditType())) {
//                        auditInfoDao.deleteByPrimaryKey(info);
//                    }
//                    info.setBaseProjectId(information.getId());
//                    info.setAuditResult("0");
//
//                    info.setStatus("0");
//
//                    String updateDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
//
//                    info.setUpdateTime(updateDate);
//
//                    auditInfoDao.updateByPrimaryKeySelective(info);
//                }
//
////                auditInfo.setAuditType("0");
//
//                information.setType("1");
//            }
//        }
//        //进行中状态下提交
//        if (auditInfos.size() <= 0) {
//            AuditInfo auditInfo1 = new AuditInfo();
//
//            String s = UUID.randomUUID().toString().replaceAll("-", "");
//            auditInfo1.setId(s);
//            auditInfo1.setBaseProjectId(information.getId());
//
//            auditInfo1.setAuditResult("0");
//
//            auditInfo1.setAuditType("0");
//
//            auditInfo1.setStatus("0");
//
//            // 审核人id
//            auditInfo1.setAuditorId(maintenanceProjectInformationVo.getAuditorId());
//
//            String createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
//
//            auditInfo1.setCreateTime(createDate);
//
//            auditInfoDao.insertSelective(auditInfo1);
//            information.setType("1");
//        }


//        MemberManage memberManage = memberManageDao.selectByIdAndStatus(auditInfo.getId());

//        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);
    }

    /**
     * 根据id,查找回显数据
     *
     * @param id
     * @return
     */
    public MaintenanceVo selectMaintenanceProjectInformationById(String id, String userId, UserInfo userInfo) {

        MaintenanceVo maintenanceVo = new MaintenanceVo();

        // 回显消息
        String json = "[";
        Example examples = new Example(OtherInfo.class);
        examples.createCriteria().andEqualTo("foreignKey",id)
                .andEqualTo("status","0");
        examples.orderBy("changeNum").asc();
        List<OtherInfo> otherInfos = otherInfoMapper.selectByExample(examples);
        for (int i = 0; i < otherInfos.size(); i++) {
            json += "{" +
                    "\"serialNumber\" : \""+otherInfos.get(i).getSerialNumber()+"\"," +
                    "\"num\": \""+otherInfos.get(i).getNum()+"\","+
                    "},";
        }
        json+="]";
        maintenanceVo.setJson(json);

        //todo userInfo.getId();
        String userInfoId = userInfo.getId();
//        String userInfoId = "user320";
        MaintenanceProjectInformation information = maintenanceProjectInformationMapper.selectIdByMain(id);
        if (information != null) {
            String idByName = memberManageDao.findIdByName(information.getPreparePeople());
            information.setPreparePeople(idByName);
            maintenanceVo.setMaintenanceProjectInformation(information);
        } else {
            maintenanceVo.setMaintenanceProjectInformation(new MaintenanceProjectInformation());
        }


        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation", information.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation != null) {
            String idByName = memberManageDao.findIdByName(settlementAuditInformation.getPreparePeople());
            settlementAuditInformation.setPreparePeople(idByName);
            maintenanceVo.setSettlementAuditInformation(settlementAuditInformation);
        } else {
            maintenanceVo.setSettlementAuditInformation(new SettlementAuditInformation());
        }

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation", information.getId());
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example2);
        if (investigationOfTheAmount != null) {
            maintenanceVo.setInvestigationOfTheAmount(investigationOfTheAmount);
        } else {
            maintenanceVo.setInvestigationOfTheAmount(new InvestigationOfTheAmount());
        }

        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId", information.getId());

        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
        for (AuditInfo auditInfo : auditInfos) {
            if ("0".equals(auditInfo.getAuditResult())) {
                maintenanceVo.setAuditType(auditInfo.getAuditType());
            }
        }

        maintenanceVo.setAuditInfos(auditInfos);

        //查找审核数据
        Example auditExample = new Example(AuditInfo.class);
        Example.Criteria criteria1 = auditExample.createCriteria();
        criteria1.andEqualTo("baseProjectId", information.getId());
//        criteria1.andEqualTo("auditType",'0');
        // 未审批
        criteria1.andEqualTo("auditResult", '0');
        criteria1.andEqualTo("auditorId",userInfoId);


        AuditInfo auditInfo = auditInfoDao.selectOneByExample(auditExample);
        if (auditInfo != null) {
            maintenanceVo.setAuditAgainFlag("1");
            maintenanceVo.setAuditInfo(auditInfo);
            // 0 代表一审，未审批
            if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType())) {
                maintenanceVo.setAuditNumber("0");
            }else{
                maintenanceVo.setAuditNumber("1");
            }

        }
        String preparePeople = maintenanceVo.getMaintenanceProjectInformation().getPreparePeople();
        MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
        if (mkyUser!=null){
            maintenanceVo.setPre1(mkyUser.getUserName());
        }
        String preparePeople1 = maintenanceVo.getSettlementAuditInformation().getPreparePeople();
        MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(preparePeople1);
        if (mkyUser1!=null){
            maintenanceVo.setPre2(mkyUser1.getUserName());
        }

        return maintenanceVo;
    }

    /**
     * 新增--保存
     */
    public void saveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation, UserInfo userInfo, HttpServletRequest request) {

        if ("".equals(maintenanceProjectInformation.getPreparePeople2())){
            maintenanceProjectInformation.setPreparePeople2(userInfo.getId());
        }else {
            String nameById = memberManageDao.findNameById(maintenanceProjectInformation.getPreparePeople2());
            maintenanceProjectInformation.setPreparePeople2(nameById);
        }
        if ("".equals(maintenanceProjectInformation.getPreparePeople())){
            maintenanceProjectInformation.setPreparePeople(userInfo.getId());
        }else {
            String nameById = memberManageDao.findNameById(maintenanceProjectInformation.getPreparePeople());
            maintenanceProjectInformation.setPreparePeople(nameById);
        }

        String id = UUID.randomUUID().toString().replaceAll("-", "");
        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());


        //检维修对象
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(id);
        information.setCreateTime(createTime);
        information.setDelFlag("0");
        if (StringUtils.isNotEmpty(maintenanceProjectInformation.getAuditorId())) {
            information.setType("1");
        } else {
            information.setType("2");
        }
        // 项目编号格式 （WJ-202012-随机ID)
        int randomMainId = new Random().nextInt(998) + 1;
        String data = new SimpleDateFormat("yyyydd").format(new Date());
        // 如果是芜湖
        if ("1".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("WH-"+data+"-"+randomMainId);
            // 如果是马鞍山
        }else if ("2".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("MAS-"+data+"-"+randomMainId);
        }else if ("3".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("JB-"+data+"-"+randomMainId);
        }else if ("4".equals(maintenanceProjectInformation.getProjectAddress())){
            information.setMaintenanceItemId("WJ-"+data+"-"+randomMainId);
        }
        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        information.setArea(maintenanceProjectInformation.getArea());
        information.setCustomerName(maintenanceProjectInformation.getCustomerName());
        // todo `review_amount` decimal 判断如不传值，判断空字符串，赋值0
        if ("".equals(maintenanceProjectInformation.getReviewAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            information.setReviewAmount(bd);
        } else {
            information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        }

        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
        information.setFounderId(userInfo.getId());

        information.setFounderCompanyId(userInfo.getCompanyId());


        //结算审核信息
        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();

        String saiId = UUID.randomUUID().toString().replace("-", "");
        settlementAuditInformation.setId(saiId);
        settlementAuditInformation.setCreateTime(simpleDateFormat.format(new Date()));

        // todo 检维修项目信息id
        settlementAuditInformation.setMaintenanceProjectInformation(information.getId());

        // `authorized_number` decimal
        if ("".equals(maintenanceProjectInformation.getAuthorizedNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAuthorizedNumber(bd);
        } else {
            settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
        }
        // `subtract_the_number` decimal
        if ("".equals(maintenanceProjectInformation.getSubtractTheNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setSubtractTheNumber(bd);
        } else {
            settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
        }

        // `nuclear_number` decimal
        if ("".equals(maintenanceProjectInformation.getNuclearNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setNuclearNumber(bd);
        } else {
            settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());
        }

        // `contract_amount` decimal
        if ("".equals(maintenanceProjectInformation.getContractAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setContractAmount(bd);
        } else {
            settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
        }
        settlementAuditInformation.setBaseProjectId(id);
        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
        // `amount_outsourcing` decimal
        if ("".equals(maintenanceProjectInformation.getAmountOutsourcing())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAmountOutsourcing(bd);
        } else {
            settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
        }
        settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());

        // 计算核减率
        BigDecimal reviewAmount = maintenanceProjectInformation.getReviewAmount(); // 送审金额
        BigDecimal subtractTheNumber = settlementAuditInformation.getSubtractTheNumber(); // 核减数
        // 核减数 / 送审金额 * 100 = 核减率 (使用除法要精确到固定的值，这里精确到小数后10位)
        BigDecimal divide = subtractTheNumber.divide(reviewAmount,10,BigDecimal.ROUND_HALF_UP);
        BigDecimal subtractRate = divide.multiply(new BigDecimal(100));
        // 保留两位小数。四舍五入
        BigDecimal setScale = subtractRate.setScale(2, BigDecimal.ROUND_HALF_UP);
        settlementAuditInformation.setSubtractRate(setScale);
        settlementAuditInformationDao.insertSelective(settlementAuditInformation);


        // 勘探金额

        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();
        String ioaId = UUID.randomUUID().toString().replace("-", "");

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());

        investigationOfTheAmount.setId(ioaId);
        investigationOfTheAmount.setCreateTime(simpleDateFormat.format(new Date()));
        investigationOfTheAmount.setDelFlag("0");

        investigationOfTheAmount.setFounderId(userInfo.getId());
        investigationOfTheAmount.setFounderCompanyId(userInfo.getCompanyId());
        investigationOfTheAmount.setMaintenanceProjectInformation(id);
        // todo 待修改
        investigationOfTheAmount.setBaseProjectId(id);

        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());

        // `unbalanced_quotation_adjustment` decimal
        if ("".equals(maintenanceProjectInformation.getUnbalancedQuotationAdjustment())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(bd);
        } else {
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        }

//        `punish_amount` decimal
        if ("".equals(maintenanceProjectInformation.getPunishAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setPunishAmount(bd);
        } else {
            investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        }

//        `outbound_amount` decimal
        if ("".equals(maintenanceProjectInformation.getOutboundAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setOutboundAmount(bd);
        } else {
            investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        }

//        `material_difference_amount` decimal
        if ("".equals(maintenanceProjectInformation.getMaterialDifferenceAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setMaterialDifferenceAmount(bd);
        } else {
            investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());
        }

        investigationOfTheAmountDao.insert(investigationOfTheAmount);


        maintenanceProjectInformationMapper.insertSelective(information);
        // 其他信息
        //其他信息表
        if (!"".equals(maintenanceProjectInformation.getComs()) && maintenanceProjectInformation.getComs() != null){
            Json coms = maintenanceProjectInformation.getComs();
            String json = coms.value();
            int num =1;
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(information.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(createTime);
                    otherInfo1.setStatus("0");
                    otherInfo1.setFoundId(userInfo.getId());
                    otherInfo1.setFounderCompany(userInfo.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    num++;
                    otherInfoMapper.insertSelective(otherInfo1);
                }
            }
        }
        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("7"); //检维修项目
        operationLog.setContent(memberManage.getMemberName()+"新增保存了"+information.getMaintenanceItemName()+"项目【"+information.getId()+"】");
        operationLog.setDoObject(information.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);

        // type
        List<FileInfo> byFreignAndType = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType());

        for (FileInfo fileInfo : byFreignAndType) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type1
        List<FileInfo> byFreignAndType1 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType1());

        for (FileInfo fileInfo : byFreignAndType1) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type2
        List<FileInfo> byFreignAndType2 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType2());

        for (FileInfo fileInfo : byFreignAndType2) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type3
        List<FileInfo> byFreignAndType3 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType3());

        for (FileInfo fileInfo : byFreignAndType3) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }
        // type4
        List<FileInfo> byFreignAndType4 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType4());

        for (FileInfo fileInfo : byFreignAndType4) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type5
        List<FileInfo> byFreignAndType5 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType5());

        for (FileInfo fileInfo : byFreignAndType5) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type6
        List<FileInfo> byFreignAndType6 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType6());

        for (FileInfo fileInfo : byFreignAndType6) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type7
        List<FileInfo> byFreignAndType7 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType7());

        for (FileInfo fileInfo : byFreignAndType7) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }

        // type8
        List<FileInfo> byFreignAndType8 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType8());

        for (FileInfo fileInfo : byFreignAndType8) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }


        // type9
        List<FileInfo> byFreignAndType9 = fileInfoMapper.findByFreignAndType(maintenanceProjectInformation.getKey(), maintenanceProjectInformation.getType9());

        for (FileInfo fileInfo : byFreignAndType9) {
            fileInfo.setPlatCode(information.getId());

            fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        }


    }

    /**
     * 编辑--保存
     */
    public void updateSaveMaintenanceProjectInformation(MaintenanceProjectInformationVo maintenanceProjectInformation, UserInfo userInfo,HttpServletRequest request) {

        //修改时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());

        // 转换
        // 检维修类型
        if ("道路恢复工程".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("0");
        } else if ("表位改造".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("1");
        } else if ("故障换表".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("2");
        } else if ("水表周检换表".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("3");
        } else if ("DN300以上管道抢维修".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("4");
        } else if ("DN300以下管道抢维修".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("5");
        } else if ("设备维修购置".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("6");
        } else if ("房屋修缮".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("7");
        } else if ("绿化种植".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("8");
        } else if ("装饰及装修".equals(maintenanceProjectInformation.getMaintenanceItemType())) {
            maintenanceProjectInformation.setMaintenanceItemType("9");
        }
        // 施工单位
        if ("施工单位2".equals(maintenanceProjectInformation.getConstructionUnitId())) {
            maintenanceProjectInformation.setConstructionUnitId("1041");
        } else if ("施工单位3".equals(maintenanceProjectInformation.getConstructionUnitId())) {
            maintenanceProjectInformation.setConstructionUnitId("7643");
        } else if ("施工单位5".equals(maintenanceProjectInformation.getConstructionUnitId())) {
            maintenanceProjectInformation.setConstructionUnitId("7645");
        } else if ("施工单位7".equals(maintenanceProjectInformation.getConstructionUnitId())) {
            maintenanceProjectInformation.setConstructionUnitId("7661");
        }
        //检维修对象
        System.err.println(maintenanceProjectInformation);
        MaintenanceProjectInformation information = new MaintenanceProjectInformation();
        information.setId(maintenanceProjectInformation.getId());
        information.setUpdateTime(updateTime);
//        information.setMaintenanceItemId(maintenanceProjectInformation.getMaintenanceItemId());

        information.setMaintenanceItemName(maintenanceProjectInformation.getMaintenanceItemName());
        information.setMaintenanceItemType(maintenanceProjectInformation.getMaintenanceItemType());
        information.setSubmittedDepartment(maintenanceProjectInformation.getSubmittedDepartment());
        information.setSubmitTime(maintenanceProjectInformation.getSubmitTime());
        information.setPreparePeople(maintenanceProjectInformation.getPreparePeople());
        information.setProjectAddress(maintenanceProjectInformation.getProjectAddress());
        information.setArea(maintenanceProjectInformation.getArea());
        information.setConstructionUnitId(maintenanceProjectInformation.getConstructionUnitId());
        // todo `review_amount` decimal 判断如不传值，判断空字符串，赋值0
        if ("".equals(maintenanceProjectInformation.getReviewAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            information.setReviewAmount(bd);
        } else {
            information.setReviewAmount(maintenanceProjectInformation.getReviewAmount());
        }
        information.setRemarkes(maintenanceProjectInformation.getRemarkes());
//        information.setFounderId(userInfo.getId());
//
//        information.setFounderCompanyId(userInfo.getCompanyId());


        //结算审核信息

        SettlementAuditInformation settlementAuditInformation = new SettlementAuditInformation();
        settlementAuditInformation.setUpdateTime(simpleDateFormat.format(new Date()));

        //  `authorized_number` decimal
        if ("".equals(maintenanceProjectInformation.getAuthorizedNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAuthorizedNumber(bd);
        } else {
            settlementAuditInformation.setAuthorizedNumber(maintenanceProjectInformation.getAuthorizedNumber());
        }
        // `subtract_the_number` decimal
        if ("".equals(maintenanceProjectInformation.getSubtractTheNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setSubtractTheNumber(bd);
        } else {
            settlementAuditInformation.setSubtractTheNumber(maintenanceProjectInformation.getSubtractTheNumber());
        }

        // `nuclear_number` decimal
        if ("".equals(maintenanceProjectInformation.getNuclearNumber())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setNuclearNumber(bd);
        } else {
            settlementAuditInformation.setNuclearNumber(maintenanceProjectInformation.getNuclearNumber());
        }

        // `contract_amount` decimal
        if ("".equals(maintenanceProjectInformation.getContractAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setContractAmount(bd);
        } else {
            settlementAuditInformation.setContractAmount(maintenanceProjectInformation.getContractAmount());
        }

        settlementAuditInformation.setContractRemarkes(maintenanceProjectInformation.getContractRemarkes());
        settlementAuditInformation.setPreparePeople(maintenanceProjectInformation.getPreparePeople2());
        settlementAuditInformation.setOutsourcing(maintenanceProjectInformation.getOutsourcing());
        settlementAuditInformation.setNameOfTheCost(maintenanceProjectInformation.getNameOfTheCost());
        settlementAuditInformation.setContact(maintenanceProjectInformation.getContact());
        settlementAuditInformation.setContactPhone(maintenanceProjectInformation.getContactPhone());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());
        // `amount_outsourcing` decimal
        if ("".equals(maintenanceProjectInformation.getAmountOutsourcing())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            settlementAuditInformation.setAmountOutsourcing(bd);
        } else {
            settlementAuditInformation.setAmountOutsourcing(maintenanceProjectInformation.getAmountOutsourcing());
        }
        settlementAuditInformation.setCompileTime(maintenanceProjectInformation.getCompileTime());
        settlementAuditInformation.setRemarkes(maintenanceProjectInformation.getRemark());
        // 计算核减率
        BigDecimal reviewAmount = maintenanceProjectInformation.getReviewAmount(); // 送审金额
        BigDecimal subtractTheNumber = settlementAuditInformation.getSubtractTheNumber(); // 核减数
        // 核减数 / 送审金额 * 100 = 核减率 (使用除法要精确到固定的值，这里精确到小数后10位)
        BigDecimal divide = subtractTheNumber.divide(reviewAmount,10,BigDecimal.ROUND_HALF_UP);
        BigDecimal subtractRate = divide.multiply(new BigDecimal(100));
        // 保留两位小数。四舍五入
        BigDecimal setScale = subtractRate.setScale(2, BigDecimal.ROUND_HALF_UP);
        settlementAuditInformation.setSubtractRate(setScale);

        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation", maintenanceProjectInformation.getId());
        SettlementAuditInformation settlementAuditInformation2 = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation2 != null){
            settlementAuditInformation.setId(settlementAuditInformation2.getId());
            settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
        } else {
            settlementAuditInformation.setId(UUID.randomUUID().toString().replace("-", ""));
            settlementAuditInformationDao.insertSelective(settlementAuditInformation);
        }




        // 勘探金额
        InvestigationOfTheAmount investigationOfTheAmount = new InvestigationOfTheAmount();

        investigationOfTheAmount.setSurveyDate(maintenanceProjectInformation.getSurveyDate());
        //勘察人员
        investigationOfTheAmount.setInvestigationPersonnel(maintenanceProjectInformation.getInvestigationPersonnel());
        investigationOfTheAmount.setSurveyBriefly(maintenanceProjectInformation.getSurveyBriefly());


        investigationOfTheAmount.setRemarkes(maintenanceProjectInformation.getAmountRemarks());
        investigationOfTheAmount.setUpdateTime(simpleDateFormat.format(new Date()));

        // `unbalanced_quotation_adjustment` decimal
        if ("".equals(maintenanceProjectInformation.getUnbalancedQuotationAdjustment())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(bd);
        } else {
            investigationOfTheAmount.setUnbalancedQuotationAdjustment(maintenanceProjectInformation.getUnbalancedQuotationAdjustment());
        }

//        `punish_amount` decimal
        if ("".equals(maintenanceProjectInformation.getPunishAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setPunishAmount(bd);
        } else {
            investigationOfTheAmount.setPunishAmount(maintenanceProjectInformation.getPunishAmount());
        }

//        `outbound_amount` decimal
        if ("".equals(maintenanceProjectInformation.getOutboundAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setOutboundAmount(bd);
        } else {
            investigationOfTheAmount.setOutboundAmount(maintenanceProjectInformation.getOutboundAmount());
        }

//        `material_difference_amount` decimal
        if ("".equals(maintenanceProjectInformation.getMaterialDifferenceAmount())) {
            String str1 = "0";
            BigDecimal bd = new BigDecimal(str1);
            investigationOfTheAmount.setMaterialDifferenceAmount(bd);
        } else {
            investigationOfTheAmount.setMaterialDifferenceAmount(maintenanceProjectInformation.getMaterialDifferenceAmount());
        }

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation", maintenanceProjectInformation.getId());
        InvestigationOfTheAmount investigationOfTheAmount2 = investigationOfTheAmountDao.selectOneByExample(example2);

        if (investigationOfTheAmount2 != null){
            investigationOfTheAmount.setId(investigationOfTheAmount2.getId());
            investigationOfTheAmountDao.updateByPrimaryKeySelective(investigationOfTheAmount);
        } else {
            investigationOfTheAmount.setId(UUID.randomUUID().toString().replace("-", ""));
            investigationOfTheAmountDao.insertSelective(investigationOfTheAmount);
        }

        // 操作日志
        String userId = userInfo.getId();
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(userId);
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUID.randomUUID().toString().replaceAll("-",""));
        operationLog.setName(userId);
        operationLog.setType("7"); //检维修项目
        operationLog.setContent(memberManage.getMemberName()+"修改保存了"+information.getMaintenanceItemName()+"项目【"+information.getId()+"】");
        operationLog.setDoObject(information.getId()); // 项目标识
        operationLog.setStatus("0");
        operationLog.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String ip = memberService.getIp(request);
        operationLog.setIp(ip);
        operationLogDao.insertSelective(operationLog);
        // json转换
        Json coms = maintenanceProjectInformation.getComs();
        if (coms != null){
            String json = coms.value();
            Example example = new Example(OtherInfo.class);
            example.createCriteria().andEqualTo("foreignKey",information.getId());
            List<OtherInfo> otherInfos1 = otherInfoMapper.selectByExample(example);
            for (OtherInfo thisOther : otherInfos1) {
                thisOther.setStatus("1");
                otherInfoMapper.updateByPrimaryKeySelective(thisOther);
            }
            List<OtherInfo> otherInfos = JSONObject.parseArray(json, OtherInfo.class);
            int num = 1;
            if (otherInfos.size() > 0){
                for (OtherInfo thisInfo : otherInfos) {
                    OtherInfo otherInfo1 = new OtherInfo();
                    otherInfo1.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    otherInfo1.setForeignKey(information.getId());
                    otherInfo1.setSerialNumber(thisInfo.getSerialNumber());
                    otherInfo1.setNum(thisInfo.getNum());
                    otherInfo1.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    otherInfo1.setStatus("0");
                    otherInfo1.setFoundId(userInfo.getId());
                    otherInfo1.setFounderCompany(userInfo.getCompanyId());
                    otherInfo1.setChangeNum(num);
                    num++;
                    otherInfoMapper.insertSelective(otherInfo1);
                }
            }
        }


        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(information);

    }

    /**
     * @Author Armyman
     * @Description //检维修任务统计数量
     * @Date 11:26 2020/10/8
     **/
    public StatisticalNumberVo statisticalNumber(String projectAddress) {
        return maintenanceProjectInformationMapper.statisticalNumber(projectAddress);
    }

    public List<StatisticalFigureVo> statisticalFigure(String projectAddress, String startDate, String endDate, UserInfo loginUser) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<StatisticalFigureVo> statisticalFigureVos = new ArrayList<>();

        Calendar start = new GregorianCalendar();   // 开始时间
        Calendar end = new GregorianCalendar();     // 结束时间
        try{
            start.setTime(simpleDateFormat.parse(startDate));
            end.setTime(simpleDateFormat.parse(endDate));

            for (;start.compareTo(end) <= 0;) {

                // 按月查询数据
                startDate = (simpleDateFormat.format(start.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start.getTime());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                endDate = (simpleDateFormat.format(calendar.getTime()));

                List<StatisticalFigureVo> statisticalFigureVoList = maintenanceProjectInformationMapper.statisticalFigure(projectAddress, startDate, endDate);
                // 如果当前月数据为空，添加一条
                if (!(statisticalFigureVoList.size() > 0)){
                    StatisticalFigureVo statisticalFigureVo = new StatisticalFigureVo(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    statisticalFigureVo.setYeartime(simpleDateFormat.format(start.getTime()).substring(0,4));
                    statisticalFigureVo.setMonthtime(Integer.parseInt(simpleDateFormat.format(start.getTime()).substring(5,7)) + "");
                    statisticalFigureVoList.add(statisticalFigureVo);
                }
                statisticalFigureVos.addAll(statisticalFigureVoList);
                start.set(Calendar.DAY_OF_MONTH,1);
                start.add(Calendar.MONTH, 1);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return statisticalFigureVos;
    }

    /**
     * @Author Armyman
     * @Description //本月任务
     * @Date 20:18 2020/10/8
     **/
    public Integer month() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);

        String day = year + "-" + month;
        return maintenanceProjectInformationMapper.monthCount(day);
    }

    /**
     * @Author Armyman
     * @Description // 本年任务
     * @Date 20:18 2020/10/8
     **/
    public Integer year() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        return maintenanceProjectInformationMapper.yearCount(year);
    }

    /**
     * @Author Armyman
     * @Description //上月任务
     * @Date 20:18 2020/10/8
     **/
    public Integer month2() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH));
        String month = DateUtil.getNowMonth().substring(4);

        String day = year + "-" + month;
        return maintenanceProjectInformationMapper.monthCount(day);
    }

    /**
     * @Author Armyman
     * @Description // 上年任务
     * @Date 20:18 2020/10/8
     **/
    public Integer year2() {
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR) - 1);
        return maintenanceProjectInformationMapper.yearCount(year);
    }

    public MaintenanceVo selectMainById(String id, String userId, UserInfo userInfo) {

        MaintenanceVo maintenanceVo = new MaintenanceVo();

        // 回显消息
        String json = "[";
        Example examples = new Example(OtherInfo.class);
        examples.createCriteria().andEqualTo("foreignKey",id)
                .andEqualTo("status","0");
        List<OtherInfo> otherInfos = otherInfoMapper.selectByExample(examples);
        for (int i = 0; i < otherInfos.size(); i++) {
            json += "{" +
                    "\"serialNumber\" : \""+otherInfos.get(i).getSerialNumber()+"\"," +
                    "\"num\": \""+otherInfos.get(i).getNum()+"\","+
                    "},";
        }
        json+="]";
        maintenanceVo.setJson(json);

        //todo userInfo.getId();
        String userInfoId = userInfo.getId();
//        String userInfoId = "user320";
        MaintenanceProjectInformation information = maintenanceProjectInformationMapper.selectIdByMain2(id);
        if (information != null) {
            maintenanceVo.setMaintenanceProjectInformation(information);
        } else {
            maintenanceVo.setMaintenanceProjectInformation(new MaintenanceProjectInformation());
        }


        Example example1 = new Example(SettlementAuditInformation.class);
        example1.createCriteria().andEqualTo("maintenanceProjectInformation", information.getId());
        SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example1);
        if (settlementAuditInformation != null) {
            maintenanceVo.setSettlementAuditInformation(settlementAuditInformation);
        } else {
            maintenanceVo.setSettlementAuditInformation(new SettlementAuditInformation());
        }

        Example example2 = new Example(InvestigationOfTheAmount.class);
        example2.createCriteria().andEqualTo("maintenanceProjectInformation", information.getId());
        InvestigationOfTheAmount investigationOfTheAmount = investigationOfTheAmountDao.selectOneByExample(example2);
        if (investigationOfTheAmount != null) {
            maintenanceVo.setInvestigationOfTheAmount(investigationOfTheAmount);
        } else {
            maintenanceVo.setInvestigationOfTheAmount(new InvestigationOfTheAmount());
        }

        Example example = new Example(AuditInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId", information.getId());

        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
        for (AuditInfo auditInfo : auditInfos) {
            if ("0".equals(auditInfo.getAuditResult())) {
                maintenanceVo.setAuditType(auditInfo.getAuditType());
            }
        }

        maintenanceVo.setAuditInfos(auditInfos);

        //查找审核数据
        Example auditExample = new Example(AuditInfo.class);
        Example.Criteria criteria1 = auditExample.createCriteria();
        criteria1.andEqualTo("baseProjectId", information.getId());
//        criteria1.andEqualTo("auditType",'0');
        // 未审批
        criteria1.andEqualTo("auditResult", '0');
        criteria1.andEqualTo("auditorId",userInfoId);


        AuditInfo auditInfo = auditInfoDao.selectOneByExample(auditExample);
        if (auditInfo != null) {
            maintenanceVo.setAuditAgainFlag("1");
            maintenanceVo.setAuditInfo(auditInfo);
            // 0 代表一审，未审批
            if ("0".equals(auditInfo.getAuditType()) || "1".equals(auditInfo.getAuditType()) || "4".equals(auditInfo.getAuditType())) {
                maintenanceVo.setAuditNumber("0");
            }else{
                maintenanceVo.setAuditNumber("1");
            }

        }
        String preparePeople = maintenanceVo.getMaintenanceProjectInformation().getPreparePeople();
        MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(preparePeople);
        if (mkyUser!=null){
            maintenanceVo.setPre1(mkyUser.getUserName());
        }
        String preparePeople1 = maintenanceVo.getSettlementAuditInformation().getPreparePeople();
        MkyUser mkyUser1 = mkyUserMapper.selectByPrimaryKey(preparePeople1);
        if (mkyUser1!=null){
            maintenanceVo.setPre2(mkyUser1.getUserName());
        }

        return maintenanceVo;
    }

    public MaintenanceProjectInformation selectPeople(String id) {
        String idByName = memberManageDao.findIdByName(id);
        MaintenanceProjectInformation maintenanceProjectInformation = new MaintenanceProjectInformation();
        maintenanceProjectInformation.setPreparePeople(idByName);
        return maintenanceProjectInformation;
    }

    public void editOutMoney(String id, String outsourcingMoney) {
        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectByPrimaryKey(id);
        if (maintenanceProjectInformation != null){
            Example example = new Example(SettlementAuditInformation.class);
            example.createCriteria().andEqualTo("maintenanceProjectInformation",maintenanceProjectInformation.getId());
            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example);
            if (settlementAuditInformation != null){
                settlementAuditInformation.setAmountOutsourcing(new BigDecimal(outsourcingMoney));
                settlementAuditInformationDao.updateByPrimaryKeySelective(settlementAuditInformation);
            }
        }
    }

    public void maintenanceProjectSuccess(String id, String id1) {
        String[] split = id.split(",");
        for (String s1 : split) {
            MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectByPrimaryKey(s1);

            Example example = new Example(AuditInfo.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",maintenanceProjectInformation.getId());
            c.andEqualTo("status","0");
            c.andEqualTo("auditType","1");
            AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
            if (auditInfo!=null) {
                if (!auditInfo.getAuditorId().equals(id1)) {
                    throw new RuntimeException("此操作只能由所选项目部门领导来完成");
                } else {
                    AuditInfo auditInfo1 = new AuditInfo();
                    auditInfo1.setId(UUID.randomUUID().toString().replace("-", ""));
                    auditInfo1.setBaseProjectId(maintenanceProjectInformation.getId());
                    auditInfo1.setAuditResult("0");
                    auditInfo1.setAuditType("4");
                    String founderId = maintenanceProjectInformation.getFounderId();
                    Example example1 = new Example(MemberManage.class);
                    Example.Criteria cc = example1.createCriteria();
                    cc.andEqualTo("id", founderId);
                    MemberManage memberManage = memberManageDao.selectOneByExample(example1);
                    //1芜湖
                    if (memberManage.getWorkType().equals("1")) {
                        auditInfo1.setAuditorId(whzjm);
                        //吴江
                    } else if (memberManage.getWorkType().equals("2")) {
                        auditInfo1.setAuditorId(wjzjm);
                    }
                    auditInfo1.setFounderId(id1);
                    auditInfo1.setStatus("0");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    auditInfo1.setCreateTime(simpleDateFormat.format(new Date()));
                    auditInfo1.setUpdateTime(simpleDateFormat.format(new Date()));
                    auditInfoDao.insertSelective(auditInfo1);

                    maintenanceProjectInformation.setType("1");
                    maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);
                }
            }
        }
    }

    public void maintenanceProjectBack(String id, String backOpnion) {
        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        c.andEqualTo("status","0");
        c.andEqualTo("auditType","1");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        auditInfo.setAuditTime("");
        auditInfo.setAuditResult("2");
        auditInfo.setAuditOpinion(backOpnion);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        auditInfo.setUpdateTime(s.format(new Date()));
        auditInfoDao.updateByPrimaryKeySelective(auditInfo);

        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectByPrimaryKey(id);
        maintenanceProjectInformation.setType("6");
        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);

    }

    public void maintenanceProjectBackB(String id, String backOpnion, String id1) {
        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        c.andEqualTo("status","0");
        c.andEqualTo("auditType","4");
        AuditInfo auditInfo = auditInfoDao.selectOneByExample(example);
        auditInfo.setAuditTime("");
        auditInfo.setAuditResult("2");
        auditInfo.setAuditOpinion(backOpnion);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        auditInfo.setUpdateTime(s.format(new Date()));
        auditInfoDao.updateByPrimaryKeySelective(auditInfo);

        MaintenanceProjectInformation maintenanceProjectInformation = maintenanceProjectInformationMapper.selectByPrimaryKey(id);
        maintenanceProjectInformation.setType("6");
        maintenanceProjectInformationMapper.updateByPrimaryKeySelective(maintenanceProjectInformation);





        //创建人

        String projectName = maintenanceProjectInformation.getMaintenanceItemName();

        MessageVo messageVo = new MessageVo();
        messageVo.setId("A24");
        messageVo.setUserId(maintenanceProjectInformation.getFounderId());
        messageVo.setType("1"); //风险
        messageVo.setTitle("您有一个检维修项目已被总经理退回！");
        // 「接收人姓名」您好！您提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！
        messageVo.setSnsContent("您好！"+projectName+"项目检维修任务已被总经理退回，请重新编制！");
        messageVo.setContent("您好！"+projectName+"项目检维修任务已被总经理退回，请重新编制！");
        messageVo.setDetails("您好！"+projectName+"项目检维修任务已被总经理退回，请重新编制！");
        messageService.sendOrClose(messageVo);

        // 互审人
        Example example1 = new Example(AuditInfo.class);
        Example.Criteria cc = example1.createCriteria();
        cc.andEqualTo("baseProjectId",maintenanceProjectInformation.getId());
        cc.andEqualTo("status","0");
        cc.andEqualTo("auditType","0");
        AuditInfo auditInfo1 = auditInfoDao.selectOneByExample(example1);

        MemberManage memberManage1 = memberManageDao.selectByPrimaryKey(auditInfo1.getAuditorId());
        MessageVo messageVo1 = new MessageVo();
        messageVo1.setId("A24");
        messageVo1.setType("1"); // 风险
        messageVo1.setUserId(auditInfo1.getAuditorId());
        messageVo1.setPhone(memberManage1.getPhone());
        messageVo1.setReceiver(memberManage1.getEmail());
        messageVo1.setTitle("您有一个检维修项目已被总经理退回！");
        // 「接收人姓名」您好！【提交人】提交给您的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
        messageVo1.setSnsContent("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
        messageVo1.setContent("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
        messageVo1.setDetails("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
//                messageVo1.setSnsContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setContent(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo1.setDetails(name2 + "您好！【sjf】提交给您的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
        messageService.sendOrClose(messageVo1);

        // 部门领导
        Example example2 = new Example(AuditInfo.class);
        Example.Criteria cc1 = example2.createCriteria();
        cc1.andEqualTo("baseProjectId",maintenanceProjectInformation.getId());
        cc1.andEqualTo("status","0");
        cc1.andEqualTo("auditType","1");
        AuditInfo auditInfo2 = auditInfoDao.selectOneByExample(example2);
        MemberManage memberManage2 = memberManageDao.selectByPrimaryKey(auditInfo2.getAuditorId());

        MessageVo messageVo2 = new MessageVo();
        messageVo2.setId("A24");
        messageVo2.setType("1"); // 风险
        messageVo2.setUserId(auditInfo2.getAuditorId());
        messageVo2.setPhone(memberManage2.getPhone());
        messageVo2.setReceiver(memberManage2.getEmail());
        messageVo2.setTitle("您有一个检维修项目已被总经理退回！");
        // 「接收人姓名」您好！【提交人】提交的【所选项目名称】的结算项目，结算金额超过造价金额，请及时查看详情！
        messageVo2.setSnsContent("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
        messageVo2.setContent("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
        messageVo2.setDetails("您好！"+projectName+"项目检维修任务已被总经理退回，请注意关注！");
//                messageVo2.setSnsContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setContent(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
//                messageVo2.setDetails(name3 + "您好！【sjf】提交的【" + projectName + "】的结算项目，结算金额超过造价金额，请及时登录造价管理平台查看详情！");
        messageService.sendOrClose(messageVo2);


    }
}

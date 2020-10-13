package net.zlw.cloud.designProject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.VisaApplyChangeInformation.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.budgeting.mapper.CostPreparationDao;
import net.zlw.cloud.budgeting.mapper.VeryEstablishmentDao;
import net.zlw.cloud.budgeting.model.CostPreparation;
import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.index.mapper.MessageNotificationDao;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.And;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectService {
    @Resource
    private ProjectMapper projectMapper;  //基本信息

    @Resource
    private DesignInfoMapper designInfoMapper; //设计信息

    @Resource
    private DesignChangeInfoMapper designChangeInfoMapper;  //设计变更信息

    @Resource
    private BudgetingMapper budgetingMapper; //预算编译信息

    @Resource
    private WujiangMoneyInfoMapper wujiangMoneyInfoMapper; //吴江设计费\

    @Resource
    private AnhuiMoneyinfoMapper anhuiMoneyinfoMapper;

    @Resource
    private AuditInfoDao auditInfoDao;

    @Resource
    private PackageCameMapper packageCameMapper;

    @Resource
    private ProjectExplorationMapper projectExplorationMapper;

    @Resource
    private MemberManageDao memberManageDao;

    @Resource
    private MunicipalNgineerDesignMapper municipalNgineerDesignMapper;

    @Resource
    private CostPreparationDao costPreparationDao;

    @Resource
    private VeryEstablishmentDao veryEstablishmentDao;

    @Resource
    private MessageNotificationDao messageNotificationDao;

    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;

    @Resource
    private SettlementAuditInformationDao settlementAuditInformationDao;

    @Resource
    private LastSettlementReviewDao  lastSettlementReviewDao;

    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;

    @Resource
    private VisaApplyChangeInformationMapper visaApplyChangeInformationMapper;


    /**
     * 设计页面展示
     * @param pageVo
     * @return
     */
    public PageInfo<DesignInfo> designProjectSelect(DesignPageVo pageVo, UserInfo loginUser){
        MemberManage memberManage = memberManageDao.memberManageById();
        //分页插件
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        //展示集合
        List<DesignInfo> designInfos = new ArrayList<>();
        //前台获取的登录信息
        //如果设计状态为'未审核' 则展示当前用户需要审核的信息
        if("1".equals(pageVo.getDesginStatus())){
            //则根据登录用户id展示于其身份对应的数据
            //todo getLoginUser().getId()
            pageVo.setUserId("ceshi01");
            designInfos = designInfoMapper.designProjectSelect(pageVo);
            if(designInfos.size()>0){
                for (DesignInfo designInfo : designInfos) {
                    //展示设计变更时间 如果为空展示 /
                    if(designInfo.getDesignChangeTime()==null || designInfo.getDesignChangeTime().equals("")){
                        designInfo.setDesignChangeTime("/");
                    }
                    //根据地区判断相应的设计费 应付金额 实付金额
                    //如果为安徽
                    if(!designInfo.getDistrict().equals("4")){
                        Example anhui = new Example(AnhuiMoneyinfo.class);
                        Example.Criteria c2 = anhui.createCriteria();
                        c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                        AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                        if(anhuiMoneyinfo!=null){
                            designInfo.setRevenue(anhuiMoneyinfo.getRevenue());
                            designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                            designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                            designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
                        }
                        //如果为吴江
                    }else{
                        Example wujiang = new Example(WujiangMoneyInfo.class);
                        Example.Criteria c2 = wujiang.createCriteria();
                        c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                        WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                        if(wujiangMoneyInfo!=null){
                            designInfo.setRevenue(wujiangMoneyInfo.getRevenue());
                            designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                            designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                            designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());
                        }
                    }
                    //获取预算表中的造价金额
                    Example example = new Example(Budgeting.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                    Budgeting budgeting = budgetingMapper.selectOneByExample(example);
                    if(budgeting!=null){
                        designInfo.setAmountCost(budgeting.getAmountCost());
                    }else{
                        designInfo.setAmountCost(new BigDecimal(0));
                    }
                }
            }
            PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(designInfos);
            return designInfoPageInfo;
        }

        //如果状态为出图中
        if("2".equals(pageVo.getDesginStatus())){
            //todo loginUser.getId()
            pageVo.setUserId("ceshi01");
            designInfos = designInfoMapper.designProjectSelect2(pageVo);
            if(designInfos!=null){
                if(designInfos.size()>0){
                    for (DesignInfo designInfo : designInfos) {
                        //展示设计变更时间 如果为空展示 /
                        if(designInfo.getDesignChangeTime()==null || designInfo.getDesignChangeTime().equals("")){
                            designInfo.setDesignChangeTime("/");
                        }
                        //根据地区判断相应的设计费 应付金额 实付金额
                        //如果为安徽
                        if(!designInfo.getDistrict().equals("4")){
                            Example anhui = new Example(AnhuiMoneyinfo.class);
                            Example.Criteria c2 = anhui.createCriteria();
                            c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                            AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                            if(anhuiMoneyinfo!=null){
                                designInfo.setRevenue(anhuiMoneyinfo.getRevenue());
                                designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                                designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                                designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
                            }
                            //如果为吴江
                        }else{
                            Example wujiang = new Example(WujiangMoneyInfo.class);
                            Example.Criteria c2 = wujiang.createCriteria();
                            c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                            WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                            if(wujiangMoneyInfo!=null){
                                designInfo.setRevenue(wujiangMoneyInfo.getRevenue());
                                designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                                designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                                designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());}
                        }

                        //获取预算表中的造价金额
                        Example example = new Example(Budgeting.class);
                        Example.Criteria c = example.createCriteria();
                        c.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                        Budgeting budgeting = budgetingMapper.selectOneByExample(example);
                        if(budgeting!=null){
                            designInfo.setAmountCost(budgeting.getAmountCost());
                        }else{
                            designInfo.setAmountCost(new BigDecimal(0));
                        }
                    }
                }
            }
            PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(designInfos);
            return designInfoPageInfo;
        }

        if("3".equals(pageVo.getDesginStatus())||"4".equals(pageVo.getDesginStatus())){
            //如果状态为未通过 或者 已完成
            //获取当前设计部门负责人
//            Example admin = new Example(MemberManage.class);
//            Example.Criteria adminc = admin.createCriteria();
//            adminc.andEqualTo("depAdmin","1");
//            adminc.andEqualTo("depId","1");
//            MemberManage memberManage = memberManageDao.selectOneByExample(admin);
            //将部门负责人传入
            pageVo.setAdminId(memberManage.getId());
            //todo loginUser.getId()
            pageVo.setUserId("1");
            designInfos = designInfoMapper.designProjectSelect3(pageVo);
            if(designInfos.size()>0){
                for (DesignInfo designInfo : designInfos) {
                    //展示设计变更时间 如果为空展示 /
                    if(designInfo.getDesignChangeTime()==null || designInfo.getDesignChangeTime().equals("")){
                        designInfo.setDesignChangeTime("/");
                    }
                    //根据地区判断相应的设计费 应付金额 实付金额
                    //如果为安徽
                    if(!designInfo.getDistrict().equals("4")){
                        Example anhui = new Example(AnhuiMoneyinfo.class);
                        Example.Criteria c2 = anhui.createCriteria();
                        c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                        AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                        if(anhuiMoneyinfo!=null){
                            designInfo.setRevenue(anhuiMoneyinfo.getRevenue());
                            designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                            designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                            designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
                        }
                        //如果为吴江
                    }else{
                        Example wujiang = new Example(WujiangMoneyInfo.class);
                        Example.Criteria c2 = wujiang.createCriteria();
                        c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                        WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                        if(wujiangMoneyInfo!=null){
                            designInfo.setRevenue(wujiangMoneyInfo.getRevenue());
                            designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                            designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                            designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());
                        }
                    }
                    //获取预算表中的造价金额
                    Example example = new Example(Budgeting.class);
                    Example.Criteria c = example.createCriteria();
                    c.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                    Budgeting budgeting = budgetingMapper.selectOneByExample(example);
                    if(budgeting!=null){
                        designInfo.setAmountCost(budgeting.getAmountCost());
                    }else{
                        designInfo.setAmountCost(new BigDecimal(0));
                    }
                }
            }
        }else{
            //若果为空 说明查询所有
//            Example admin = new Example(MemberManage.class);
//            Example.Criteria adminc = admin.createCriteria();
//            adminc.andEqualTo("depAdmin","1");
//            adminc.andEqualTo("depId","1");
            //将部门负责人传入
            pageVo.setAdminId(memberManage.getId());
            //todo loginUser.getId()
            pageVo.setUserId("1");
            designInfos = designInfoMapper.designProjectSelect3(pageVo);
            for (DesignInfo designInfo : designInfos) {
                //展示设计变更时间 如果为空展示 /
                if(designInfo.getDesignChangeTime()==null || designInfo.getDesignChangeTime().equals("")){
                    designInfo.setDesignChangeTime("/");
                }
                //根据地区判断相应的设计费 应付金额 实付金额
                //如果为安徽
                if(!designInfo.getDistrict().equals("4")){
                    Example anhui = new Example(AnhuiMoneyinfo.class);
                    Example.Criteria c2 = anhui.createCriteria();
                    c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                    AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                    if(anhuiMoneyinfo!=null){
                        designInfo.setRevenue(anhuiMoneyinfo.getRevenue());
                        designInfo.setOfficialReceipts(anhuiMoneyinfo.getOfficialReceipts());
                        designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                        designInfo.setPayTerm(anhuiMoneyinfo.getPayTerm());
                    }
                    //如果为吴江
                }else{
                    Example wujiang = new Example(WujiangMoneyInfo.class);
                    Example.Criteria c2 = wujiang.createCriteria();
                    c2.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                    WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                    if(wujiangMoneyInfo!=null){
                        designInfo.setRevenue(wujiangMoneyInfo.getRevenue());
                        designInfo.setOfficialReceipts(wujiangMoneyInfo.getOfficialReceipts());
                        designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                        designInfo.setPayTerm(wujiangMoneyInfo.getPayTerm());
                    }
                }
                //获取预算表中的造价金额
                Example example = new Example(Budgeting.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",designInfo.getBaseProjectId());
                Budgeting budgeting = budgetingMapper.selectOneByExample(example);
                if(budgeting!=null){
                    designInfo.setAmountCost(budgeting.getAmountCost());
                }else{
                    designInfo.setAmountCost(new BigDecimal(0));
                }
            }
        }
        PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(designInfos);
        return designInfoPageInfo;
    }

    public Double wujiangMoney(WujiangMoneyInfo wujiangMoneyInfo){
        BigDecimal cost = wujiangMoneyInfo.getCost(); // 造价费用
        BigDecimal designRate = wujiangMoneyInfo.getDesignRate(); //设计费率
        BigDecimal preferentialPolicy = wujiangMoneyInfo.getPreferentialPolicy();//优惠政策
        //造价*设计费
        BigDecimal CostAndDesignRate = cost.multiply(designRate);
        //上述积 * 政策
        BigDecimal multiply = CostAndDesignRate.multiply(preferentialPolicy);
        return multiply.doubleValue();
    }

    public Double anhuiMoney(AnhuiMoneyinfo anhuiMoneyinfo){
        //管道费用
        BigDecimal pipelineCost = anhuiMoneyinfo.getPipelineCost();
        //泵房费用
        BigDecimal pumpRoomCost = anhuiMoneyinfo.getPumpRoomCost();

        //如果未填写泵房 则只计算管道费用 (管道不计算bim)
        if(pumpRoomCost==null||"".equals(pumpRoomCost)){
            //如果小于200万
            if(pipelineCost.compareTo(new BigDecimal("2000000")) == -1){
                //插值法则  低于200万 直接乘以0.0575 * 优惠政策
                BigDecimal PipelineCost = pipelineCost.multiply(new BigDecimal(0.0575));
                BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PipelineCost);
                return PreferentialPolicy.doubleValue();
            }

            //高于10000万
            if(pipelineCost.compareTo(new BigDecimal("100000000000"))==1){
                //插值法则   直接乘以0.016 * 优惠政策
                BigDecimal PipelineCost = pipelineCost.multiply(new BigDecimal(0.016));
                BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PipelineCost);
                return PreferentialPolicy.doubleValue();
            }
            //管道插值法
            BigDecimal pipelineCostCount = this.pipelineCostCount(anhuiMoneyinfo);
            BigDecimal professionalAdjustmentFactor = anhuiMoneyinfo.getProfessionalAdjustmentFactor().multiply(pipelineCostCount);
            BigDecimal complexAdjustmentFactor = anhuiMoneyinfo.getComplexAdjustmentFactor().multiply(professionalAdjustmentFactor);
            BigDecimal multiply = anhuiMoneyinfo.getPreferentialPolicy().multiply(complexAdjustmentFactor);
            return multiply.doubleValue() * 10000;
        }else if(pipelineCost == null||"".equals(pipelineCost)){
            //如果小于200万
            if(pumpRoomCost.compareTo(new BigDecimal("2000000")) == -1){
                //插值法则  低于200万 直接乘以0.0575 * 优惠政策
                BigDecimal PumpRoomCost = pumpRoomCost.multiply(new BigDecimal(0.0575));
                BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PumpRoomCost);
                return PreferentialPolicy.doubleValue();
            }

            //高于10000万
            if(pumpRoomCost.compareTo(new BigDecimal("100000000000"))==1){
                //插值法则   直接乘以0.016 * 优惠政策
                BigDecimal PumpRoomCost = pumpRoomCost.multiply(new BigDecimal(0.016));
                BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PumpRoomCost);
                return PreferentialPolicy.doubleValue();
            }
            //泵房插值法
            BigDecimal pumpRoomCostCount = this.pumpRoomCostCount(anhuiMoneyinfo);
            BigDecimal professionalAdjustmentFactor = anhuiMoneyinfo.getProfessionalAdjustmentFactor().multiply(pumpRoomCostCount);
            BigDecimal complexAdjustmentFactor = anhuiMoneyinfo.getComplexAdjustmentFactor().multiply(professionalAdjustmentFactor);
            BigDecimal multiply = anhuiMoneyinfo.getPreferentialPolicy().multiply(complexAdjustmentFactor);
            return multiply.doubleValue() * 10000;
        }
        //水管算法
        BigDecimal pipelineCostCount = this.pipelineCostCount(anhuiMoneyinfo);
        //泵房算法
        BigDecimal pumpRoomCostCount = this.pumpRoomCostCount(anhuiMoneyinfo);

        // 当泵房 和 管道都填写时
        //泵房 * bim
        BigDecimal pipelineCostCountBim = pumpRoomCostCount.multiply(anhuiMoneyinfo.getBim());
        //泵房Bim + 管道费用
        BigDecimal add = pipelineCostCount.add(pipelineCostCountBim);
        //复杂
        BigDecimal multiply1 = anhuiMoneyinfo.getProfessionalAdjustmentFactor().multiply(add);
        //专业
        BigDecimal multiply2 = anhuiMoneyinfo.getComplexAdjustmentFactor().multiply(multiply1);
        //优惠
        BigDecimal multiply3 = anhuiMoneyinfo.getPreferentialPolicy().multiply(multiply2);
        BigDecimal multiply4 = multiply3.multiply(new BigDecimal(10000));
        return multiply4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        return multiply3.doubleValue() * 10000;
    }
    /**
     * 管道算法
     * @return
     */
    public BigDecimal pipelineCostCount(AnhuiMoneyinfo anhuiMoneyinfo){
        //管道费用
        BigDecimal pipelineCost = anhuiMoneyinfo.getPipelineCost();
        //传输过来的参数值/10000
        pipelineCost = pipelineCost.divide(new BigDecimal(10000));

        //如果小于200万
        if(pipelineCost.compareTo(new BigDecimal("200")) == -1){
            //插值法则  低于200万 直接乘以0.0575 * 优惠政策
            BigDecimal PipelineCost = pipelineCost.multiply(new BigDecimal(0.0575));
            BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PipelineCost);
            return PreferentialPolicy;
        }

        //高于10000000万
        if(pipelineCost.compareTo(new BigDecimal("10000000"))==1){
            //插值法则   直接乘以0.016 * 优惠政策
            BigDecimal PipelineCost = pipelineCost.multiply(new BigDecimal(0.016));
            BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PipelineCost);
            return PreferentialPolicy;
        }

        //如果值为 表的区间内 则使用插值法计算
        List<MunicipalNgineerDesign> municipalNgineerDesigns = municipalNgineerDesignMapper.designMoney(pipelineCost);
        if(municipalNgineerDesigns.size()>1){
            BigDecimal dis1 = new BigDecimal(municipalNgineerDesigns.get(0).getDesignBasicCost());
            BigDecimal dis2 = new BigDecimal(municipalNgineerDesigns.get(1).getDesignBasicCost());

            BigDecimal project1 = new BigDecimal(municipalNgineerDesigns.get(0).getProjectCost());
            BigDecimal project2 = new BigDecimal(municipalNgineerDesigns.get(1).getProjectCost());

            //设计费差
            BigDecimal DisSubtract = dis1.subtract(dis2);
            //工程差
            BigDecimal ProjectSubtract = project1.subtract(project2);
            //插值
            BigDecimal divide = DisSubtract.divide(ProjectSubtract,6,BigDecimal.ROUND_HALF_UP);
            //管道差
            BigDecimal prjectMoney = pipelineCost.subtract(project2);
            //管道插值 * 插值
            BigDecimal multiply = divide.multiply(prjectMoney);
            //最后管道结果
            BigDecimal add = multiply.add(dis2);
            return add;
        }
            Example example = new Example(MunicipalNgineerDesign.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projectCost",pipelineCost);
            MunicipalNgineerDesign municipalNgineerDesign = municipalNgineerDesignMapper.selectOneByExample(example);
            //返回应收金额
            return new BigDecimal(municipalNgineerDesign.getDesignBasicCost());
    }

    /**
     * 泵房算法
     * @param anhuiMoneyinfo
     * @return
     */
    public BigDecimal pumpRoomCostCount(AnhuiMoneyinfo anhuiMoneyinfo){
        //泵房费用
        BigDecimal pumpRoomCost = anhuiMoneyinfo.getPumpRoomCost();
        //传输过来的参数值/10000
        pumpRoomCost = pumpRoomCost.divide(new BigDecimal(10000));

        //如果小于200万
        if(pumpRoomCost.compareTo(new BigDecimal("200")) == -1){
            //插值法则  低于200万 直接乘以0.0575 * 优惠政策
            BigDecimal PumpRoomCost = pumpRoomCost.multiply(new BigDecimal(0.0575));
            BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PumpRoomCost);
            return PreferentialPolicy;
        }

        //高于10000万
        if(pumpRoomCost.compareTo(new BigDecimal("10000000"))==1){
            //插值法则   直接乘以0.016 * 优惠政策
            BigDecimal PumpRoomCost = pumpRoomCost.multiply(new BigDecimal(0.016));
            BigDecimal PreferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(PumpRoomCost);
            return PreferentialPolicy;
        }
        //如果值为 表的区间内 则使用插值法计算
        List<MunicipalNgineerDesign> municipalNgineerDesigns = municipalNgineerDesignMapper.designMoney(pumpRoomCost);
        if(municipalNgineerDesigns.size()>1){
            BigDecimal dis1 = new BigDecimal(municipalNgineerDesigns.get(0).getDesignBasicCost());
            BigDecimal dis2 = new BigDecimal(municipalNgineerDesigns.get(1).getDesignBasicCost());

            BigDecimal project1 = new BigDecimal(municipalNgineerDesigns.get(0).getProjectCost());
            BigDecimal project2 = new BigDecimal(municipalNgineerDesigns.get(1).getProjectCost());

            //设计费差
            BigDecimal DisSubtract = dis1.subtract(dis2);
            //工程差
            BigDecimal ProjectSubtract = project1.subtract(project2);
            //插值
            BigDecimal divide = DisSubtract.divide(ProjectSubtract,6,BigDecimal.ROUND_HALF_UP);
            //管道差
            BigDecimal prjectMoney = pumpRoomCost.subtract(project2);
            //管道插值 * 插值
            BigDecimal multiply = divide.multiply(prjectMoney);
            //最后管道结果
            BigDecimal add = multiply.add(dis2);
            //乘以复杂
            BigDecimal complexAdjustmentFactor = anhuiMoneyinfo.getComplexAdjustmentFactor().multiply(add);
            //乘以优惠
            BigDecimal preferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(complexAdjustmentFactor);
            //乘以专业
            BigDecimal professionalAdjustmentFactor = anhuiMoneyinfo.getProfessionalAdjustmentFactor().multiply(preferentialPolicy);
            //乘以bim
            BigDecimal bim = anhuiMoneyinfo.getBim().multiply(professionalAdjustmentFactor);
//            return multiply3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return bim;
        }
            Example example = new Example(MunicipalNgineerDesign.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("projectCost",pumpRoomCost);
            MunicipalNgineerDesign municipalNgineerDesign = municipalNgineerDesignMapper.selectOneByExample(example);
            //返回应收金额
            Double designBasicCost = municipalNgineerDesign.getDesignBasicCost();
            //乘以复杂
            BigDecimal complexAdjustmentFactor = anhuiMoneyinfo.getComplexAdjustmentFactor().multiply(new BigDecimal(designBasicCost));
            //乘以优惠
            BigDecimal preferentialPolicy = anhuiMoneyinfo.getPreferentialPolicy().multiply(complexAdjustmentFactor);
            //乘以专业
            BigDecimal multiply1 = anhuiMoneyinfo.getProfessionalAdjustmentFactor().multiply(preferentialPolicy);
            //乘以bim
            BigDecimal bim = anhuiMoneyinfo.getBim().multiply(multiply1);
//            return bim.doubleValue() * 10000;
            return bim;
    }
    /**
     * 项目合并
     * @param mergeName
     * @param mergeNum
     * @param id
     */

    public void mergeProject(String mergeName,String mergeNum,String id){
            DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(id);
            if(designInfo != null){
                BaseProject baseProject = projectMapper.selectByPrimaryKey(designInfo.getBaseProjectId());
                if(baseProject != null){
                    //如果项目名称 与 项目编号 与数据库一致 则为主表 X=虚拟编号     是否为主表,是否删除状态
                    if(baseProject.getProjectName().equals(mergeName)&&baseProject.getProjectNum().equals(mergeNum)){
                        String x = "x"+mergeNum;
                        projectMapper.updataMerga(x,"0",baseProject.getId(),"0");
                    }else{
                        String x = "x"+mergeNum;
                        projectMapper.updataMerga(x,"1",baseProject.getId(),"1");
                    }
                }
            }
    }

    /**
     * 合并列表展示
     * @return
     */
    public DesignInfo mergeProjectList(String id){
        //根据id查找 合并信息
        DesignInfo designInfo = designInfoMapper.designProjectSelectOne(id);
        return designInfo;
    }

    /**
     * 更新成非主表
     */
    public void updateMergeProject0(String id){
        projectMapper.updateMergeProject0(id);
    }

    /**
     * 跟新为主表
     * @param id
     */
    public void updateMergeProject1(String id){
        projectMapper.updateMergeProject1(id);
    }

    /**
     * 批量审核
     *
     * @param id
     * @param auditInfo
     */
    public void batchAudit(String id, AuditInfo auditInfo, UserInfo loginUser){
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(loginUser.getId());
        DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(id);
        BaseProject baseProject = projectMapper.selectByPrimaryKey(designInfo.getBaseProjectId());
        //说明他是设计部门负责人
        if("4".equals(memberManage.getMemberRoleId())&&"1".equals(memberManage.getDepAdmin())){
            //如果为通过 则从待审核状态变为已完成 如果为未通过则状态改为未通过
            if("1".equals(auditInfo.getAuditResult())){
                baseProject.setDesginStatus("4");
                projectMapper.updateByPrimaryKeySelective(baseProject);
            }else if("2".equals(auditInfo.getAuditResult())){
                baseProject.setDesginStatus("3");
                projectMapper.updateByPrimaryKeySelective(baseProject);
            }
        }
        if("2".equals(auditInfo.getAuditResult())){
            baseProject.setDesginStatus("3");
            projectMapper.updateByPrimaryKeySelective(baseProject);
        }else if("1".equals(auditInfo.getAuditResult())){
            //如果为通过则 审核状态变为一审
            auditInfo.setAuditType("1");
        }
        auditInfoDao.updateByPrimaryKeySelective(auditInfo);
//    }
//        //获取id后 根据id 修改审核状态和审核意见
//        DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(id);
//        auditInfoDao.batchAudit(designInfo.getId(),auditResult,auditOpinion);
    }

    /**
     *合并状态还原
     */
    public void reduction(String id){
        DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(id);
        if(designInfo!=null){
            BaseProject baseProject = projectMapper.selectByPrimaryKey(designInfo.getBaseProjectId());
            if(baseProject!=null){
                projectMapper.reduction( baseProject.getVirtualCode());
            }
        }
    }

    /**
     * 删除设计项目
     * @param id
     */
    public void deleteProject(String id) {
        designInfoMapper.deleteProject(id);
        designChangeInfoMapper.deleteProject(id);
        DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(id);
        projectMapper.deleteProject(designInfo.getBaseProjectId());
    }

    /**
     * 提交设计项目
     * @param projectVo
     */
    public void disProjectSubmit(ProjectVo projectVo, UserInfo loginUser) {
        //baseProject, designInfo, packageCame, projectExploration
        String projectuuid = UUID.randomUUID().toString().replaceAll("-", "");
        String DesignInfouuid = UUID.randomUUID().toString().replaceAll("-", "");
        String packageCameuuId = UUID.randomUUID().toString().replaceAll("-", "");
        String projectExplorationuuid = UUID.randomUUID().toString().replaceAll("-", "");
        //时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());


        projectVo.getBaseProject().setId(projectuuid);
        projectVo.getBaseProject().setCreateTime(createTime);
        //todo loginUser.getId()
        projectVo.getBaseProject().setFounderId("ceshi01");
        projectVo.getBaseProject().setFounderCompanyId(loginUser.getCompanyId());
        projectVo.getBaseProject().setProjectFlow("1");
        projectVo.getBaseProject().setDelFlag("0");

        //提交后状态为未审核  添加后状态为出图中 如果没有互审人 说明时保存
        if(projectVo.getBaseProject().getReviewerId()==null||"".equals(projectVo.getBaseProject().getReviewerId())){
            projectVo.getBaseProject().setDesginStatus("2");
        }else{
            AuditInfo auditInfo = new AuditInfo();
            String auditInfouuid = UUID.randomUUID().toString().replaceAll("-","");
            auditInfo.setId(auditInfouuid);
            auditInfo.setBaseProjectId(DesignInfouuid);
            auditInfo.setAuditType("0");
            auditInfo.setAuditResult("0");
            auditInfo.setAuditorId(projectVo.getBaseProject().getReviewerId());
            auditInfo.setCreateTime(createTime);
            //todo   loginUser.getId()
            auditInfo.setFounderId("ceshi01");
            auditInfo.setCompanyId(loginUser.getCompanyId());
            auditInfo.setStatus("0");
            auditInfoDao.insert(auditInfo);
            projectVo.getBaseProject().setDesginStatus("1");
        }

        projectMapper.insert(projectVo.getBaseProject());

        //设计表添加
        projectVo.getDesignInfo().setId(DesignInfouuid);
        projectVo.getDesignInfo().setBaseProjectId(projectuuid);
        //todo   loginUser.getId()
        projectVo.getDesignInfo().setFounderId("ceshi01");
        projectVo.getDesignInfo().setCompanyId(loginUser.getCompanyId());
        projectVo.getDesignInfo().setStatus("0");
        projectVo.getDesignInfo().setCreateTime(createTime);
        designInfoMapper.insert(projectVo.getDesignInfo());


        //方案会审
        if(projectVo.getPackageCame().getParticipant()!=null&&!"".equals(projectVo.getPackageCame().getParticipant())){
            projectVo.getPackageCame().setId(packageCameuuId);
            projectVo.getPackageCame().setBassProjectId(DesignInfouuid);
            //todo   loginUser.getId()
            projectVo.getPackageCame().setFounderId("ceshi01");
            projectVo.getPackageCame().setCompanyId(loginUser.getCompanyId());
            projectVo.getPackageCame().setStatus("0");
            projectVo.getPackageCame().setCreateTime(createTime);
            packageCameMapper.insert(projectVo.getPackageCame());
        }


        //项目踏勘
        if(projectVo.getProjectExploration().getScout()!=null&&!"".equals(projectVo.getPackageCame().getParticipant())){
            projectVo.getProjectExploration().setId(projectExplorationuuid);
            projectVo.getProjectExploration().setBaseProjectId(DesignInfouuid);
            //todo   loginUser.getId()
            projectVo.getProjectExploration().setFounderId("ceshi01");
            projectVo.getProjectExploration().setCompany_id(loginUser.getCompanyId());
            projectVo.getProjectExploration().setStatus("0");
            projectVo.getProjectExploration().setCreateTime(createTime);
            projectExplorationMapper.insert(projectVo.getProjectExploration());
        }
    }

    public BaseProject BaseProjectByid(String id){
        BaseProject baseProject = projectMapper.selectByPrimaryKey(id);
        return baseProject;
    }

    public AnhuiMoneyinfo anhuiMoneyinfoByid(String id){
        Example example = new Example(AnhuiMoneyinfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(example);
        return anhuiMoneyinfo;
    }

    public WujiangMoneyInfo wujiangMoneyInfoByid(String id){
        Example example = new Example(WujiangMoneyInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(example);
        return wujiangMoneyInfo;
    }

    public ProjectExploration ProjectExplorationByid(String id){
        Example example = new Example(ProjectExploration.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        ProjectExploration projectExploration = projectExplorationMapper.selectOneByExample(example);
        return projectExploration;
    }

    public PackageCame PackageCameByid(String id){
        Example example = new Example(PackageCame.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("bassProjectId",id);
        PackageCame packageCame = packageCameMapper.selectOneByExample(example);
        return packageCame;
    }

    /**
     * 根据基本信息id查询设计表信息
     * @param id
     * @return
     */
    public DesignInfo designInfoByid(String id){
        Example example = new Example(DesignInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        DesignInfo designInfo = designInfoMapper.selectOneByExample(example);
        return designInfo;
    }

    public List<DesignChangeInfo> designChangeInfosByid(String id){
        Example example = new Example(DesignChangeInfo.class);
        example.setOrderByClause("design_change_time desc");
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("designInfoId",id);
        List<DesignChangeInfo> designChangeInfos = designChangeInfoMapper.selectByExample(example);
        return designChangeInfos;
    }

    public DesignChangeInfo designChangeInfoByid(String id){
        List<DesignChangeInfo> designChangeInfos = this.designChangeInfosByid(id);
        if(designChangeInfos.size()>0){
            DesignChangeInfo designChangeInfo = designChangeInfos.get(0);
            return designChangeInfo;
        }
        return null;
    }

    public List<AuditInfo> auditInfoList(String id){
        Example example = new Example(AuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        c.andNotEqualTo("auditResult","0");
        List<AuditInfo> auditInfos = auditInfoDao.selectByExample(example);
        return auditInfos;
    }

    /**
     * 项目提交-编辑-保存
     * @param projectVo
     */
    public void projectEdit(ProjectVo projectVo, UserInfo loginUser){
        //BaseProject baseProject, DesignInfo designInfo, ProjectExploration projectExploration, PackageCame packageCame
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());
        if(projectVo.getBaseProject().getDesginStatus() == "2"){
            //如果提交人为空 说明时保存状态未出图中 反之状态未待审核
            if(projectVo.getBaseProject().getReviewerId() == null||"".equals(projectVo.getBaseProject().getReviewerId())){
                projectVo.getBaseProject().setDesginStatus("2");
            }else{
                AuditInfo auditInfo = new AuditInfo();
                String auditInfouuid = UUID.randomUUID().toString().replaceAll("-","");
                //编辑完成 写入互审人
                auditInfo.setId(auditInfouuid);
                auditInfo.setBaseProjectId(projectVo.getDesignInfo().getId());
                auditInfo.setAuditType("0");
                auditInfo.setAuditResult("0");
                auditInfo.setAuditorId(projectVo.getBaseProject().getReviewerId());
                auditInfo.setCreateTime(updateTime);
                auditInfo.setFounderId(loginUser.getId());
                auditInfo.setCompanyId(loginUser.getCompanyId());
                auditInfo.setStatus("0");
                //将互审人信息写入审核表
                auditInfoDao.insert(auditInfo);
                //审核状态从出图中变为待审核
                projectVo.getBaseProject().setDesginStatus("1");
            }
            //添加修改时间
            projectVo.getBaseProject().setUpdateTime(updateTime);
            projectMapper.updateByPrimaryKeySelective(projectVo.getBaseProject());
            //添加设计表修改时间
            projectVo.getDesignInfo().setUpdateTime(updateTime);
            designInfoMapper.updateByPrimaryKeySelective(projectVo.getDesignInfo());
            //添加勘探表时间
            if(projectVo.getProjectExploration()!=null){
                projectVo.getProjectExploration().setUpdateTime(updateTime);
                projectExplorationMapper.updateByPrimaryKeySelective(projectVo.getProjectExploration());
            }
            //方案会审
            if(projectVo.getProjectExploration()!=null){
                projectVo.getPackageCame().setUpdateTime(updateTime);
                packageCameMapper.updateByPrimaryKeySelective(projectVo.getPackageCame());
            }
        }
        if(projectVo.getBaseProject().getDesginStatus() == "3"){
            //如果按钮状态为1 说明点击的是提交
            if("1".equals(projectVo.getBaseProject().getOrsubmit())){
                //审核状态从未通过中变为待审核
                projectVo.getBaseProject().setDesginStatus("1");
            }else {
                //如果不为1 则为保存 状态依旧是出图中
                projectVo.getBaseProject().setDesginStatus("3");
            }
                //添加修改时间
                projectVo.getBaseProject().setUpdateTime(updateTime);
                projectMapper.updateByPrimaryKeySelective(projectVo.getBaseProject());
                //添加设计表修改时间
                projectVo.getDesignInfo().setUpdateTime(updateTime);
                designInfoMapper.updateByPrimaryKeySelective(projectVo.getDesignInfo());
                //添加勘探表时间
                projectVo.getProjectExploration().setUpdateTime(updateTime);
                projectExplorationMapper.updateByPrimaryKeySelective(projectVo.getProjectExploration());
                //方案会审
                projectVo.getPackageCame().setUpdateTime(updateTime);
                packageCameMapper.updateByPrimaryKeySelective(projectVo.getPackageCame());
        }
    }

    @Resource
    private ProjectSumService projectSumService;
    /**
     * 添加安徽信息
     * @param anhuiMoneyinfo
     */
    public void anhuiMoneyInfoAdd(AnhuiMoneyinfo anhuiMoneyinfo, UserInfo loginUser) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Example example = new Example(AnhuiMoneyinfo.class);
        Example.Criteria c = example.createCriteria();
        IncomeInfo incomeInfo = new IncomeInfo();

        anhuiMoneyinfo.setFounderId(loginUser.getId());
        anhuiMoneyinfo.setCompanyId(loginUser.getCompanyId());
        anhuiMoneyinfo.setStatus("0");
        anhuiMoneyinfo.setCreateTime(simpleDateFormat.format(new Date()));

        //根据设计表id 查询数据取出代收金额
        c.andEqualTo("baseProjectId",anhuiMoneyinfo.getBaseProjectId());
        if(anhuiMoneyinfo.getPayTerm() == "1"){
            //获取应收金额
            BigDecimal officialReceipts = anhuiMoneyinfo.getOfficialReceipts();
            AnhuiMoneyinfo anhuiMoneyinfo1 = anhuiMoneyinfoMapper.selectOneByExample(example);
            //获取代收金额信息
            String collectionMoney = anhuiMoneyinfo1.getCollectionMoney();
            //如果代收金额为空 说明第一次代收
            if(anhuiMoneyinfo1.getCollectionMoney()!=null||!"".equals(anhuiMoneyinfo1.getCollectionMoney())){
                //将代收信息拼接 保存到对象中
                String newcollectionMoney =  collectionMoney + officialReceipts+",";
                anhuiMoneyinfo1.setCollectionMoney(newcollectionMoney);
                String[] split = collectionMoney.split(",");
                Double total = 0.0;
                for (String s : split) {
                    total+=Double.parseDouble(s);
                }
                //如果代收金额超过或者等于 应收金额后
                if(anhuiMoneyinfo1.getRevenue().compareTo(new BigDecimal(total))<1){
                    designInfoMapper.updateFinalAccount(anhuiMoneyinfo.getBaseProjectId());
                }
            }else{
                anhuiMoneyinfo.setCollectionMoney(collectionMoney+",");
                //保存收入表信息
                incomeInfo.setBaseProjectId(anhuiMoneyinfo.getBaseProjectId());
                incomeInfo.setDesignMoney(new BigDecimal(collectionMoney));
                projectSumService.addIncomeInfo(incomeInfo);
            }
            anhuiMoneyinfoMapper.updateByPrimaryKeySelective(anhuiMoneyinfo);
        }else{
            //如果是实收 则直接添加到表中
            anhuiMoneyinfoMapper.insert(anhuiMoneyinfo);
            designInfoMapper.updateFinalAccount(anhuiMoneyinfo.getBaseProjectId());

            //同时将设计费添加到总收入表中
            incomeInfo.setBaseProjectId(anhuiMoneyinfo.getBaseProjectId());
            incomeInfo.setDesignMoney(anhuiMoneyinfo.getOfficialReceipts());
            projectSumService.addIncomeInfo(incomeInfo);
        }
    }

    /**
     * 添加吴江信息
     * @param wujiangMoneyInfo
     */
    public void wujiangMoneyInfoAdd(WujiangMoneyInfo wujiangMoneyInfo, UserInfo loginUser) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Example example = new Example(WujiangMoneyInfo.class);
        Example.Criteria c = example.createCriteria();
        //根据设计表id 查询数据取出代收金额
        c.andEqualTo("baseProjectId",wujiangMoneyInfo.getBaseProjectId());

        IncomeInfo incomeInfo = new IncomeInfo();

        wujiangMoneyInfo.setFounderId(loginUser.getId());
        wujiangMoneyInfo.setCompanyId(loginUser.getCompanyId());
        wujiangMoneyInfo.setStatus("0");
        wujiangMoneyInfo.setCreateTime(simpleDateFormat.format(new Date()));

        if(wujiangMoneyInfo.getPayTerm() == "1"){
            //获取应收金额
            BigDecimal officialReceipts = wujiangMoneyInfo.getOfficialReceipts();
            WujiangMoneyInfo wujiangMoneyInfo1 = wujiangMoneyInfoMapper.selectOneByExample(example);
            //获取代收金额信息
            String collectionMoney = wujiangMoneyInfo1.getCollectionMoney();
            //如果代收金额为空 说明第一次代收
            if(wujiangMoneyInfo1.getCollectionMoney()!=null||!"".equals(wujiangMoneyInfo1.getCollectionMoney())){
                //将代收信息拼接 保存到对象中
                String newcollectionMoney =  collectionMoney + officialReceipts+",";
                wujiangMoneyInfo.setCollectionMoney(newcollectionMoney);
                String[] split = collectionMoney.split(",");
                Double total = 0.0;
                for (String s : split) {
                    total+=Double.parseDouble(s);
                }
                //如果代收金额超过或者等于 应收金额后
                if(wujiangMoneyInfo.getRevenue().compareTo(new BigDecimal(total))<1){
                    designInfoMapper.updateFinalAccount(wujiangMoneyInfo.getBaseProjectId());
                }
            }else{
                wujiangMoneyInfo.setCollectionMoney(collectionMoney+",");

                //保存收入表信息
                incomeInfo.setBaseProjectId(wujiangMoneyInfo.getBaseProjectId());
                incomeInfo.setDesignMoney(new BigDecimal(collectionMoney));
                projectSumService.addIncomeInfo(incomeInfo);
            }
            wujiangMoneyInfoMapper.updateByPrimaryKeySelective(wujiangMoneyInfo);
        }else{
            wujiangMoneyInfoMapper.insert(wujiangMoneyInfo);
            designInfoMapper.updateFinalAccount(wujiangMoneyInfo.getBaseProjectId());

            //同时将设计费添加到总收入表中
            incomeInfo.setBaseProjectId(wujiangMoneyInfo.getBaseProjectId());
            incomeInfo.setDesignMoney(wujiangMoneyInfo.getOfficialReceipts());
            projectSumService.addIncomeInfo(incomeInfo);
        }
    }

    /**
     * 吴江到账信息回显
     * @param id
     * @return
     */
    public WujiangMoneyInfo wujiangMoneyInfopayterm(String id){
        Example example = new Example(WujiangMoneyInfo.class);
        Example.Criteria c = example.createCriteria();
        //根据设计表id 查询数据取出代收金额
        c.andEqualTo("baseProjectId",id);
        WujiangMoneyInfo wujiangMoneyInfo1 = wujiangMoneyInfoMapper.selectOneByExample(example);
        if(wujiangMoneyInfo1!=null){
            Double total = 0.0;
            //获取代收金额记录
            String collectionMoney = wujiangMoneyInfo1.getCollectionMoney();
            String[] split = collectionMoney.split(",");
            ArrayList<PayItem> strings = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                PayItem payItem = new PayItem();
                payItem.setNum("第"+ (i+1) +"次收款");
                payItem.setSize(split[i]+"元");
                strings.add(payItem);
                total+= Double.parseDouble(split[i]);
            }
            //将数组返回
            wujiangMoneyInfo1.setStrings(strings);
            //将总金额返回
            wujiangMoneyInfo1.setTotalMoney(new BigDecimal(total));
            return wujiangMoneyInfo1;
        }
        return null;
    }

    /**
     * 安徽到账信息回显
     * @param id
     * @return
     */
    public AnhuiMoneyinfo anhuiMoneyInfopayterm(String id) {
        Example example = new Example(AnhuiMoneyinfo.class);
        Example.Criteria c = example.createCriteria();
        //根据设计表id 查询数据取出代收金额
        c.andEqualTo("baseProjectId",id);
        AnhuiMoneyinfo anhuiMoneyinfo1 = anhuiMoneyinfoMapper.selectOneByExample(example);
        if(anhuiMoneyinfo1!=null){
            Double total = 0.0;
            //获取代收金额记录
            String collectionMoney = anhuiMoneyinfo1.getCollectionMoney();
            String[] split = collectionMoney.split(",");
            ArrayList<PayItem> strings = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                PayItem payItem = new PayItem();
                payItem.setNum("第"+ (i+1) +"次收款");
                payItem.setSize(split[i]+"元");
                strings.add(payItem);
                total+= Double.parseDouble(split[i]);
            }
            //将数组返回
            anhuiMoneyinfo1.setStrings(strings);
            //将总金额返回
            anhuiMoneyinfo1.setTotalMoney(new BigDecimal(total));
            return anhuiMoneyinfo1;
        }
        return null;
    }

    /**
     * 设计变更编辑
     * @param projectVo
     */
    public void disProjectChangeEdit(ProjectVo projectVo, UserInfo loginUser) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updateTime = simpleDateFormat.format(new Date());
            //如果提交人为空 说明时保存状态未出图中 反之状态未待审核
            if(projectVo.getBaseProject().getReviewerId() == null||"".equals(projectVo.getBaseProject().getReviewerId())){
                projectVo.getBaseProject().setDesginStatus("2");
            }else{
                AuditInfo auditInfo = new AuditInfo();
                String auditInfouuid = UUID.randomUUID().toString().replaceAll("-","");
                //编辑完成 写入互审人
                auditInfo.setId(auditInfouuid);
                auditInfo.setBaseProjectId(projectVo.getDesignInfo().getId());
                auditInfo.setAuditType("0");
                auditInfo.setAuditResult("0");
                auditInfo.setAuditorId(projectVo.getBaseProject().getReviewerId());
                auditInfo.setCreateTime(updateTime);
                auditInfo.setFounderId(loginUser.getId());
                auditInfo.setCompanyId(loginUser.getCompanyId());
                auditInfo.setStatus("0");
                //将互审人信息写入审核表
                auditInfoDao.insert(auditInfo);
                //审核状态从出图中变为待审核
                projectVo.getBaseProject().setDesginStatus("1");
            }

            //添加设计变更信息
            packageCameMapper.updateByPrimaryKeySelective(projectVo.getPackageCame());
            String DesignChangeInfoid = UUID.randomUUID().toString().replaceAll("-","");
            projectVo.getDesignChangeInfo().setCreateTime(updateTime);
            projectVo.getDesignChangeInfo().setId(DesignChangeInfoid);
            projectVo.getDesignChangeInfo().setDesignInfoId(projectVo.getDesignInfo().getId());
            projectVo.getDesignChangeInfo().setFounderId(loginUser.getId());
            projectVo.getDesignChangeInfo().setCompanyId(loginUser.getCompanyId());
            projectVo.getDesignChangeInfo().setStatus("0");
            designChangeInfoMapper.updateByPrimaryKeySelective(projectVo.getDesignChangeInfo());

            //添加设计表修改时间
            projectVo.getDesignInfo().setUpdateTime(updateTime);
            designInfoMapper.updateByPrimaryKeySelective(projectVo.getDesignInfo());
            //添加勘探表时间
            if(projectVo.getProjectExploration()!=null){
                projectVo.getProjectExploration().setUpdateTime(updateTime);
                projectExplorationMapper.updateByPrimaryKeySelective(projectVo.getProjectExploration());
            }
            //方案会审
            if(projectVo.getPackageCame()!=null){
            projectVo.getPackageCame().setUpdateTime(updateTime);
            packageCameMapper.updateByPrimaryKeySelective(projectVo.getPackageCame());
            }
    }

    /**
     * 展示当前项目的所有合并项目
     * @param id 虚拟编号
     * @return
     */
    public List<BaseProject> DesProjectInfoSelect(String id) {
        Example baseProjectexample = new Example(BaseProject.class);
        Example.Criteria baseProjectc = baseProjectexample.createCriteria();
        baseProjectc.andEqualTo("virtualCode",id);
        List<BaseProject> baseProjects = projectMapper.selectByExample(baseProjectexample);
        return baseProjects;
    }

    /**
     * 设计审核 与 设计变更信息审核
     */
    public void DesginAudandChangeAud(AuditInfo auditInfo, UserInfo loginUser){
        //获取当前用户
        //判断当前用户角色
        MemberManage memberManage = memberManageDao.selectByPrimaryKey(loginUser.getId());
        //根据审核id获取设计表
        DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(auditInfo.getBaseProjectId());
        //通过设计表获得基础信息表
        BaseProject baseProject = projectMapper.selectByPrimaryKey(designInfo.getBaseProjectId());
        //如果虚拟编号不为空 说明是合并项目
        if(!"".equals(baseProject.getVirtualCode())&& baseProject.getVirtualCode()!=null){
            List<BaseProject> baseProjects = this.DesProjectInfoSelect(baseProject.getVirtualCode());

            for (BaseProject project : baseProjects) {
                if("4".equals(memberManage.getMemberRoleId())&&"1".equals(memberManage.getDepAdmin())){
                    //如果为通过 则从待审核状态变为已完成 如果为未通过则状态改为未通过
                    if("1".equals(auditInfo.getAuditResult())){
                        //修改当前基本信息中设计审核状态
                        project.setDesginStatus("4");
                        auditInfo.setAuditType("1");
                        projectMapper.updateByPrimaryKeySelective(project);
                    }else if("2".equals(auditInfo.getAuditResult())){
                        project.setDesginStatus("3");
                        auditInfo.setAuditType("0");
                        projectMapper.updateByPrimaryKeySelective(project);
                    }
                }
                if("2".equals(auditInfo.getAuditResult())){
                    project.setDesginStatus("3");
                    auditInfo.setAuditType("2");
                    projectMapper.updateByPrimaryKeySelective(project);
                }else if("1".equals(auditInfo.getAuditResult())){
                    //如果为通过则 审核状态变为一审
                    auditInfo.setAuditType("1");
                    project.setDesginStatus("1");
                    projectMapper.updateByPrimaryKeySelective(project);
                }
                DesignInfo param = this.designInfoByid(project.getId());
                Example example = new Example(AuditInfo.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("baseProjectId",param.getId());
                auditInfoDao.updateByExample(auditInfo,example);
            }
        }else{
            //虚拟编号为空 说明不是合并项目
            //说明他是设计部门负责人
            if("4".equals(memberManage.getMemberRoleId())&&"1".equals(memberManage.getDepAdmin())){
                //如果为通过 则从待审核状态变为已完成 如果为未通过则状态改为未通过
                if("1".equals(auditInfo.getAuditResult())){
                    baseProject.setDesginStatus("4");
                    auditInfo.setAuditType("1");
                    projectMapper.updateByPrimaryKeySelective(baseProject);
                }else if("2".equals(auditInfo.getAuditResult())){
                    baseProject.setDesginStatus("3");
                    projectMapper.updateByPrimaryKeySelective(baseProject);
                }
            }
            if("2".equals(auditInfo.getAuditResult())){
                baseProject.setDesginStatus("3");
                auditInfo.setAuditType("0");
                projectMapper.updateByPrimaryKeySelective(baseProject);
            }else if("1".equals(auditInfo.getAuditResult())){
                //如果为通过则 审核状态变为一审
                auditInfo.setAuditType("1");
            }
            Example example = new Example(AuditInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("baseProjectId",designInfo.getId());
            auditInfoDao.updateByExample(auditInfo,example);
        }
    }

    public String desginStatusSensus(String id) {
        Integer integer = projectMapper.desginStatusSensus1(id);
        Integer integer1 = projectMapper.desginStatusSensus2(id);
            String s3 =
                    "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
            return s3;
    }

    public String budgetStatusSensus(String id) {
        Integer integer = projectMapper.budgetStatusSensus1(id);
        Integer integer1 = projectMapper.budgetStatusSensus2(id);
        String s3 =
                "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
        return s3;
    }

    public String trackStatusSensus(String id) {
        Integer integer = projectMapper.trackStatusSensus1(id);
        Integer integer1 = projectMapper.trackStatusSensus2(id);
        String s3 =
                "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
        return s3;
    }

    public String visaStatusSensus(String id) {
        Integer integer = projectMapper.visaStatusSensus1(id);
        Integer integer1 = projectMapper.visaStatusSensus2(id);
        String s3 =
                "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
        return s3;
    }

    public String progressPaymentStatusSensus(String id) {
        Integer integer = projectMapper.progressPaymentStatusSensus1(id);
        Integer integer1 = projectMapper.progressPaymentStatusSensus2(id);
        String s3 =
                "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
        return s3;
    }

    public String settleAccountsStatusSensus(String id) {
        Integer integer = projectMapper.settleAccountsStatusSensus1(id);
        Integer integer1 = projectMapper.settleAccountsStatusSensus2(id);
        String s3 =
                "[{\"value1\":\""+integer+"\",\"name1\":\"进行中\"},{\"value1\":\""+integer1+"\",\"name1\":\"已完成\"}]";
        return s3;
    }

    public Long buildDay(String id) throws ParseException {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 日期字符串
        String dateStr1 = projectMapper.buildingStartTime(id);
        String dateStr2 = projectMapper.buildingEndTime(id);

        // 获取日期
        Date start = DateFormat.parse(dateStr1);
        Date end = DateFormat.parse(dateStr2);

        // 获取相差的天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(end);
        long timeInMillis2 = calendar.getTimeInMillis();

        long betweenDays =  (timeInMillis2 - timeInMillis1) / (1000L*3600L*24L);
        System.out.println(betweenDays);
        return betweenDays;
    }

    public String projectCount(String id){
        return projectMapper.projectCount(id);
    }

    public int missionCount(String id) {
        int count =
                projectMapper.settleAccountsStatusSensus1(id)+projectMapper.settleAccountsStatusSensus2(id)+
                projectMapper.progressPaymentStatusSensus1(id)+ projectMapper.progressPaymentStatusSensus2(id)+
                        projectMapper.visaStatusSensus1(id)+projectMapper.visaStatusSensus2(id)+
        projectMapper.trackStatusSensus1(id)+projectMapper.trackStatusSensus2(id)+
        +projectMapper.budgetStatusSensus1(id)
        +projectMapper.budgetStatusSensus2(id)
        +projectMapper.desginStatusSensus1(id)
        +projectMapper.desginStatusSensus2(id);
        return count;
    }

    public BigDecimal desMoneySum(String id){
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("buildingProjectId",id);
        BigDecimal officialReceipts = new BigDecimal(0);
        BigDecimal officialReceipts1 = new BigDecimal(0);
        List<BaseProject> baseProjects = projectMapper.selectByExample(example);
        for (BaseProject baseProject : baseProjects) {
            DesignInfo designInfo = this.designInfoByid(baseProject.getId());

            AnhuiMoneyinfo anhuiMoneyinfo = this.anhuiMoneyinfoByid(designInfo.getId());
            WujiangMoneyInfo wujiangMoneyInfo = this.wujiangMoneyInfoByid(designInfo.getId());
            if(wujiangMoneyInfo!=null){
                officialReceipts.add(anhuiMoneyinfo.getOfficialReceipts().add(officialReceipts)) ;
            }
            if(anhuiMoneyinfo!=null){
                officialReceipts1.add( wujiangMoneyInfo.getOfficialReceipts().add(officialReceipts1));
            }
        }
        return officialReceipts.add(officialReceipts1);
    }

    public BigDecimal outsourceMoneySum(String id){
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("buildingProjectId",id);
        BigDecimal outsourceMoney = new BigDecimal(0);
        List<BaseProject> baseProjects = projectMapper.selectByExample(example);
        for (BaseProject baseProject : baseProjects) {
            DesignInfo designInfo = this.designInfoByid(baseProject.getId());
            if(designInfo!=null){
                outsourceMoney.add(designInfo.getOutsourceMoney().add(outsourceMoney));
            }
        }
        return outsourceMoney;
    }

    public BigDecimal costTotalAmountSum(String id){
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("buildingProjectId",id);
        BigDecimal costTotalAmount = new BigDecimal(0);
        List<BaseProject> baseProjects = projectMapper.selectByExample(example);
        for (BaseProject baseProject : baseProjects) {
            CostPreparation costPreparation = this.costPreparationById(baseProject.getId());
            if(costPreparation!=null){
                costTotalAmount.add(costPreparation.getCostTotalAmount().add(costTotalAmount));
            }
        }
        return costTotalAmount;
    }

    public CostPreparation costPreparationById(String id){
        Example example1 = new Example(CostPreparation.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("budgetingId",id);
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);
        return costPreparation;
    }

    public CostPreparation costPreparationById2(String id){
        Example example1 = new Example(CostPreparation.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("baseProjectId",id);
        CostPreparation costPreparation = costPreparationDao.selectOneByExample(example1);
        return costPreparation;
    }

    public BigDecimal amountCostAmountSum(String id){
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("buildingProjectId",id);
        BigDecimal costTotalAmount = new BigDecimal(0);
        List<BaseProject> baseProjects = projectMapper.selectByExample(example);
        for (BaseProject baseProject : baseProjects) {
            Example example1 = new Example(Budgeting.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("baseProjectId",baseProject.getId());
            Budgeting budgeting = budgetingMapper.selectOneByExample(example1);
            if(budgeting!=null){
                costTotalAmount.add(budgeting.getAmountCost().add(costTotalAmount));
            }
        }
        return costTotalAmount;
    }
    public BigDecimal biddingPriceControlSum(String id){
        Example example = new Example(BaseProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("buildingProjectId",id);
        BigDecimal costTotalAmount = new BigDecimal(0);
        List<BaseProject> baseProjects = projectMapper.selectByExample(example);
        for (BaseProject baseProject : baseProjects) {
            VeryEstablishment veryEstablishment = this.veryEstablishmentById(baseProject.getId());
            if(veryEstablishment!=null){
                costTotalAmount.add(veryEstablishment.getBiddingPriceControl().add(costTotalAmount));
            }
        }
        return costTotalAmount;
    }

    public VeryEstablishment veryEstablishmentById(String id){
        Example example1 = new Example(VeryEstablishment.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("baseProjectId",id);
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example1);
        return veryEstablishment;
    }

    public VeryEstablishment veryEstablishmentById2(String id){
        Example example1 = new Example(VeryEstablishment.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andEqualTo("budgetingId",id);
        VeryEstablishment veryEstablishment = veryEstablishmentDao.selectOneByExample(example1);
        return veryEstablishment;
    }

    public List<MessageNotification> messageList(UserInfo userInfo) {
        Example example = new Example(MessageNotification.class);
        Example.Criteria c = example.createCriteria();
//        userInfo.getId() 1212
        c.andEqualTo("acceptId",userInfo.getId());
        List<MessageNotification> messageNotifications = messageNotificationDao.selectByExample(example);
        return messageNotifications;
    }

    public List<OneCensus> OneCensusList(CostVo2 costVo2){
        List<OneCensus> oneCensuses = projectMapper.censusList(costVo2);
        return oneCensuses;
    }
    public PageInfo<BaseProject> individualList(IndividualVo individualVo){
        PageHelper.startPage(individualVo.getPageNum(),individualVo.getPageSize());
        List<BaseProject> baseProjects = projectMapper.individualList(individualVo);
        PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
        return baseProjectPageInfo;
    }

    public Budgeting budgetingByid(String id){
        Example example = new Example(Budgeting.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        Budgeting budgeting = budgetingMapper.selectOneByExample(example);
        return budgeting;
    }

    public TrackAuditInfo trackAuditInfoByid(String id){
        Example example = new Example(TrackAuditInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectOneByExample(example);
        return trackAuditInfo;
    }

    public net.zlw.cloud.settleAccounts.model.SettlementAuditInformation SettlementAuditInformationByid(String id){
        Example example = new Example(
                net.zlw.cloud.settleAccounts.model.SettlementAuditInformation.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        net.zlw.cloud.settleAccounts.model.SettlementAuditInformation settlementAuditInformation = settlementAuditInformationDao.selectOneByExample(example);
        return settlementAuditInformation;
    }

    public net.zlw.cloud.settleAccounts.model.LastSettlementReview lastSettlementReviewbyid(String id){
        Example example = new Example(LastSettlementReview.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("baseProjectId",id);
        net.zlw.cloud.settleAccounts.model.LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
        return lastSettlementReview;
    }

    public ProjectVo3 progressPaymentInformationSum(String id){
        List<String> strings = progressPaymentInformationDao.NewcurrentPaymentInformation(id);
        List<String> strings1 = progressPaymentInformationDao.SumcurrentPaymentInformation(id);
        String s2 = progressPaymentInformationDao.cumulativePaymentTimes(id);
        String s3 = progressPaymentInformationDao.currentPaymentRatio(id);
        ProjectVo3 projectVo3 = new ProjectVo3();
        if(strings.size() > 0){
            String s = strings.get(0);
            projectVo3.setNewcurrentPaymentInformation(s);

        }else{
            projectVo3.setNewcurrentPaymentInformation("-");
        }
        if(strings1.size() > 0){
            String s1 = strings1.get(0);
            projectVo3.setSumcurrentPaymentInformation(s1);
        }else{
            projectVo3.setSumcurrentPaymentInformation("-");
        }

        projectVo3.setCumulativePaymentTimes(s2);
        projectVo3.setCurrentPaymentRatio(s3);
        return projectVo3;
    }

    public ProjectVo3 visaApplyChangeInformationSum(String id){
        String s = visaApplyChangeInformationMapper.amountVisaChangeSum(id);
        String s1 = visaApplyChangeInformationMapper.changeCount(id);
        String s2 = visaApplyChangeInformationMapper.contractAmount(id);
        ProjectVo3 projectVo3 = new ProjectVo3();
        projectVo3.setAmountVisaChangeSum(s);
        projectVo3.setChangeCount(s1);
        projectVo3.setContractAmount(s2);
        return projectVo3;
    }

    /**
     * 应技提金额
     * @return
     */
    public BigDecimal accruedAmount(BigDecimal desMoney){
        BigDecimal multiply = desMoney.multiply(new BigDecimal(0.05));
        BigDecimal divide = multiply.divide(new BigDecimal(1.06),2, BigDecimal.ROUND_HALF_UP);
        return divide;
    }
    /**
     * 建议计提金额
     */
    public BigDecimal proposedAmount(BigDecimal accruedAmount){
        BigDecimal multiply = accruedAmount.multiply(new BigDecimal(0.8));
        return multiply;
    }

    /**
     * 余额
     * @param accruedAmount
     * @param proposedAmount
     * @return
     */
    public BigDecimal surplus(BigDecimal accruedAmount,BigDecimal proposedAmount){
        BigDecimal subtract = accruedAmount.subtract(proposedAmount);
        return subtract;
    }
    /**
     * 当前用户代办预算编制预算编制个数
     */
    public String budgetingCount(String id,String district){
        String budgetingCount = projectMapper.budgetingCount(id,district);
        return budgetingCount;
    }

    /**
     * 当前用户代办进度款支付个数
     * @param id
     * @return
     */
    public String progressPaymentInformationCount(String id,String district){
        return projectMapper.progressPaymentInformationCount(id,district);
    }

    /**
     * 当前用户代办签证变更个数
     * @param id
     * @return
     */
    public String visaApplyChangeInformationCount(String id,String district){
        return projectMapper.visaApplyChangeInformationCount(id,district);
    }

    /**
     * 当前用户代办跟踪审计个数
     * @param id
     * @return
     */
    public String trackAuditInfoCount(String id,String district){
        return projectMapper.trackAuditInfoCount(id,district);
    }

    /**
     * 当前用户代办结算编制个数
     * @param id
     * @return
     */
    public String settleAccountsCount(String id,String district){
        return projectMapper.settleAccountsCount(id,district);
    }

    /**
     * 获取当前年份
     */
    public String getSysYear(){
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

    /**
     * 获取当前月份
     */
    public int getSysMouth(){
        Calendar date = Calendar.getInstance();
        int month = date.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 造价年表
     * @param costVo2
     * @return
     */
    public OneCensus2 costCensus(CostVo2 costVo2){
        String sysYear = this.getSysYear();
        //注入当前时间
        costVo2.setYear(sysYear);
        OneCensus2 oneCensus2 = projectMapper.costCensus(costVo2);
        return oneCensus2;
    }

    /**
     * 造价月表
     * @param costVo2
     * @return
     */
    public List<OneCensus2> costCensusList(CostVo2 costVo2){
        List<OneCensus2> oneCensus2s = projectMapper.costCensusList(costVo2);
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            Integer budget = oneCensus2.getBudget();
            Integer track = oneCensus2.getTrack();
            Integer visa = oneCensus2.getVisa();
            Integer progresspayment = oneCensus2.getProgresspayment();
            Integer settleaccounts = oneCensus2.getSettleaccounts();
            Integer total =budget + track + visa + progresspayment + settleaccounts;
            oneCensus2.setTotal(total);
        }
        return oneCensus2s;
    }

    public Integer yearTaskCount(CostVo2 costVo2) {
        OneCensus2 oneCensus2 = projectMapper.costCensus(costVo2);
        Integer budget = oneCensus2.getBudget();
        Integer track = oneCensus2.getTrack();
        Integer visa = oneCensus2.getVisa();
        Integer progresspayment = oneCensus2.getProgresspayment();
        Integer settleaccounts = oneCensus2.getSettleaccounts();
        Integer total =budget + track + visa + progresspayment + settleaccounts;
        return total;
    }

    public Integer mouthDesCount(CostVo2 costVo2) {
        List<OneCensus> oneCensuses = projectMapper.censusList(costVo2);
        OneCensus oneCensus = oneCensuses.get(0);
        Integer municipalPipeline = oneCensus.getMunicipalPipeline();
        Integer networkReconstruction = oneCensus.getNetworkReconstruction();
        Integer newCommunity = oneCensus.getNewCommunity();
        Integer secondaryWater = oneCensus.getSecondaryWater();
        Integer commercialHouseholds = oneCensus.getCommercialHouseholds();
        Integer waterResidents = oneCensus.getWaterResidents();
        Integer administration = oneCensus.getAdministration();
        Integer total = municipalPipeline + networkReconstruction + newCommunity + secondaryWater + commercialHouseholds + waterResidents + administration;
        return total;
    }

    public Integer yearDesCount(CostVo2 costVo2) {
        Integer total = 0;
        List<OneCensus> oneCensuses = projectMapper.censusList(costVo2);
        for (OneCensus oneCensus : oneCensuses) {
            total +=oneCensus.getMunicipalPipeline();
            total += oneCensus.getNetworkReconstruction();
            total += oneCensus.getNewCommunity();
            total += oneCensus.getSecondaryWater();
            total += oneCensus.getCommercialHouseholds();
            total += oneCensus.getWaterResidents();
            total += oneCensus.getAdministration();
        }
        return total;
    }
}

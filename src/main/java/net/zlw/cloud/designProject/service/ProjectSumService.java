package net.zlw.cloud.designProject.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProjectSumService {

    @Resource
    private ProjectMapper projectMapper;  //基本信息

    @Resource
    private AnhuiBudgetingChargeMapper anhuiBudgetingChargeMapper; //安徽预算编制

    @Resource
    private WujiangBudgetingChargeMapper wujiangBudgetingChargeMapper; //吴江结算编制

    @Resource
    private AnhuiLastSettlementReviewChargeMapper anhuiLastSettlementReviewChargeMapper; //安徽上家结算编制

    @Resource
    private WujiangLastSettlementReviewChargeMapper wujiangLastSettlementReviewChargeMapper;//吴江上家结算编制

    @Resource
    private AnhuiSettlementAuditInformationChargeMapper anhuiSettlementAuditInformationChargeMapper; //安徽下家结算编制

    @Resource
    private WujiangSettlementAuditInformationChargeMapper wujiangSettlementAuditInformationChargeMapper;//吴江下家结算编制

    @Resource
    private AnhuiTrackChargeMapper anhuiTrackChargeMapper; //安徽跟踪审计

    @Resource
    private WujiangTrackChargeMapper wujiangTrackChargeMapper;//吴江跟踪审计

    @Resource
    private BudgetingMapper budgetingMapper;

    @Resource
    private LastSettlementReviewMapper lastSettlementReviewMapper;

    @Resource
    private SettlementAuditInformationMapper settlementAuditInformationMapper;

    @Resource
    private AchievementsInfoMapper achievementsInfoMapper;

    @Resource
    private IncomeInfoMapper incomeInfoMapper;
    @Resource
    private WujiangMoneyInfoMapper wujiangMoneyInfoMapper;
    @Resource
    private AnhuiMoneyinfoMapper anhuiMoneyinfoMapper;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private DesignChangeInfoMapper designChangeInfoMapper;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private VisaChangeMapper visaChangeMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private MemberManageDao memberManageDao;

    /**
     * 全部项目
     * @return
     */
    public Integer AllprojectCount(CostVo2 costVo2){
        CostVo3 costVo3 = projectMapper.AllprojectCount1(costVo2);
//        Integer budgetingCount = costVo3.getBudgetingCount();
//        Integer desginStatus = costVo3.getDesginStatus();
//        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
//        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
//        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
//        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
//        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        Integer total = costVo3.getTotal();
        return total;
    }

    /**
     * 待审核项目
     * @return
     */
    public Integer withAuditCount(CostVo2 costVo2){
        CostVo3 costVo3 = projectMapper.withAuditCount(costVo2);
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }

    /**
     * 未完成的项目
     * @return
     */
    public Integer conductCount(CostVo2 costVo2){
        CostVo3 costVo3 = projectMapper.conductCount1(costVo2);
//        Integer budgetingCount = costVo3.getBudgetingCount();
//        Integer desginStatus = costVo3.getDesginStatus();
//        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
//        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
//        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
//        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
   //     Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
    //    Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        Integer total = costVo3.getTotal();
        return total;
    }

    /**
     * 已完成
     * @return
     */
    public Integer completeCount(CostVo2 costVo2){
        CostVo3 costVo3 = projectMapper.completeCount(costVo2);
        Integer budgetingCount = costVo3.getBudgetingCount();
        Integer desginStatus = costVo3.getDesginStatus();
        Integer progressPaymentInformation = costVo3.getProgressPaymentInformation();
        Integer settleAccountsCount = costVo3.getSettleAccountsCount();
        Integer trackAuditInfoCount = costVo3.getTrackAuditInfoCount();
        Integer visaApplyChangeInformationCount = costVo3.getVisaApplyChangeInformationCount();
        Integer total = budgetingCount+desginStatus+progressPaymentInformation+settleAccountsCount+trackAuditInfoCount+visaApplyChangeInformationCount;
        return total;
    }

    /**
     * 安徽工程造价咨询费 + 标底金额
     * @param cost
     * @return
     */
    public Double anhuiBudgetingMoney(BigDecimal cost){
        //如果造价金额低于30000万 则收费400
        if(cost.compareTo(new BigDecimal("30000"))<1){
            return 400.0;
        }
        //若果造价金额大于30000小于60000则收费800
        if(cost.compareTo(new BigDecimal("30000"))==1&&cost.compareTo(new BigDecimal("60000"))<1){
            return 800.0;
        }
        //如果造价金额低于60000万 则按照阶梯法计算
        if(cost.compareTo(new BigDecimal("60000"))==1){
            //除一万 跟数据库中的数据比对
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            AnhuiBudgetingCharge anhuiBudgetingCharge = anhuiBudgetingChargeMapper.anhuiBudgetingMoney(divide);
            Double rateCost = anhuiBudgetingCharge.getRateCost();
            Double rateontroller = anhuiBudgetingCharge.getRateController();
            //编制
            BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
            //标底
            BigDecimal multiply2 = divide.multiply(new BigDecimal(rateontroller));
            BigDecimal add = multiply1.add(multiply2);
            return add.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //如果造价超过一个亿
        if(cost.compareTo(new BigDecimal("100000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.0026"));
            BigDecimal multiply2 = cost.multiply(new BigDecimal("0.0012"));
            BigDecimal add = multiply1.add(multiply2);
            return add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }

    /**
     * 吴江工程造价咨询费 + 标底金额
     * @param cost
     * @return
     */
    public Double wujiangBudgetingMoney(BigDecimal cost){
        //如果造价金额低于50000万 则收费400
        if(cost.compareTo(new BigDecimal("50000"))<1){
            return 400.0;
        }
        //若果造价金额大于50000小于100000则收费800
        if(cost.compareTo(new BigDecimal("50000"))==1&&cost.compareTo(new BigDecimal("100000"))<1){
            return 800.0;
        }
        //如果造价金额低于100000万 则按照阶梯法计算
        if(cost.compareTo(new BigDecimal("100000"))==1){
            //除一万 跟数据库中的数据比对
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            WujiangBudgetingCharge wujiangBudgetingCharge = wujiangBudgetingChargeMapper.wujiangBudgetingMoney(divide);
            Double rateCost = wujiangBudgetingCharge.getRateCost();
            //编制
            BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
            BigDecimal multiply2 = divide.multiply(new BigDecimal(rateCost));
            BigDecimal add = multiply1.add(multiply2);
            return add.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //如果造价超过五个亿
        if(cost.compareTo(new BigDecimal("500000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.0016"));
            BigDecimal multiply2 = cost.multiply(new BigDecimal("0.0016"));
            BigDecimal add = multiply1.add(multiply2);
            return add.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }

    /**
     * 预算咨询费基数
     * @param cost1
     * @param cost2
     * @return
     */
    public Double BudgetingBase(Double cost1,Double cost2){
        if(cost1>=cost2){
            return cost1;
        }else if(cost2>=cost1){
            return cost2;
        }
        return null;
    }

    /**
     * 计算预算编制计提
     * @param cost
     * @return
     */
    public Double technicalImprovement(Double cost){
        return cost*0.1/1.06;
    }

    /**
     * 预算编制列表
     * @param costVo2
     * @return
     */
    public List<Budgeting> budgetingList(CostVo2 costVo2){
        return budgetingMapper.BudgetingList(costVo2);
    }

    /*
    * 员工绩效统计预算编制列表
    * */
    public List<Budgeting> EmployeeBudgetingList(EmployeeVo employeeVo){
        return budgetingMapper.EmployeeBudgetingList(employeeVo);
    }

    /**
     * 上家结算编制列表
     * @param costVo2
     * @return
     */
    public List<LastSettlementReview> lastSettlementReviewChargeList(CostVo2 costVo2){
        return lastSettlementReviewMapper.lastSettlementReviewList(costVo2);
    }
    public List<LastSettlementReview> EmployeelastSettlementReviewChargeList(EmployeeVo employeeVo){
        return lastSettlementReviewMapper.EmployeelastSettlementReviewChargeList(employeeVo);
    }

    /**
     * 安徽上家结算咨询费
     * @param cost
     * @return
     */
    public Double anhuiLastSettlementReviewChargeMoney(BigDecimal cost){
        //如果造价金额低于30000万 则收费400
        if(cost.compareTo(new BigDecimal("30000"))<1){
            return 400.0;
        }
        //若果造价金额大于30000小于60000则收费800
        if(cost.compareTo(new BigDecimal("30000"))==1&&cost.compareTo(new BigDecimal("60000"))<1){
            return 800.0;
        }
        //如果造价金额低于60000万 则按照阶梯法计算
        if(cost.compareTo(new BigDecimal("60000"))==1){
            //除一万 跟数据库中的数据比对
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            AnhuiLastSettlementReviewCharge anhuiLastSettlementReviewCharge = anhuiLastSettlementReviewChargeMapper.AnhuiLastSettlementReviewChargeMoney(divide);
            Double rateCost = anhuiLastSettlementReviewCharge.getRateCost();
            //编制
            BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
            return multiply1.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //如果造价超过一个亿
        if(cost.compareTo(new BigDecimal("100000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.001"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }
    /**
     * 吴江上家结算咨询费
     * @param cost
     * @return
     */
    public Double wujiangLastSettlementReviewChargeMoney(BigDecimal cost){
        //如果造价金额低于50000万 则收费400
        if (cost == null){
            cost = new BigDecimal(0);
        }
        if(new BigDecimal("50000").compareTo(cost)<1){
            return 400.0;
        }
        //若果造价金额大于50000小于100000则收费800
        if(cost.compareTo(new BigDecimal("50000"))==1&&cost.compareTo(new BigDecimal("100000"))<1){
            return 800.0;
        }
        //如果造价金额低于100000万 则按照阶梯法计算
        if(cost.compareTo(new BigDecimal("100000"))==1){
            //除一万 跟数据库中的数据比对
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            WujiangLastSettlementReviewCharge wujiangLastSettlementReviewCharge = wujiangLastSettlementReviewChargeMapper.WujiangLastSettlementReviewChargeMoney(divide);
            Double rateCost = wujiangLastSettlementReviewCharge.getRateCost();
            //编制
            BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
            return multiply1.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //如果造价超过五个亿
        if(cost.compareTo(new BigDecimal("500000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.0005"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return 0.0;
    }

    /**
     * 安徽下家基本费
     * @param cost
     * @return
     */
    public Double anhuiSettlementAuditInformationChargeBase(BigDecimal cost){
        //如果造价金额低于30000 则收费400
        if(cost.compareTo(new BigDecimal("30000"))<1){
            return 400.0;
        }
        //若果造价金额大于30000小于60000则收费800
        if(cost.compareTo(new BigDecimal("30000"))==1&&cost.compareTo(new BigDecimal("60000"))<1){
            return 800.0;
        }
        //如果造价超过一个亿
        if(cost.compareTo(new BigDecimal("100000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.0018"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //除一万 跟数据库中的数据比对
        BigDecimal divide = cost.divide(new BigDecimal("10000"));
        AnhuiSettlementAuditInformationCharge anhuiSettlementAuditInformationCharge = anhuiSettlementAuditInformationChargeMapper.anhuiSettlementAuditInformationChargeMoney(divide);
        Double rateCost = anhuiSettlementAuditInformationCharge.getRateCost();
        BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
        return multiply1.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 安徽核检费
     * @param cost
     * @return
     */
    public Double anhuiSubtractTheNumberMoney(BigDecimal cost){
        BigDecimal multiply = cost.multiply(new BigDecimal("0.05"));
        return multiply.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 安徽下家应收金额
     * @param anhuiSubtractTheNumber
     * @param anhuiSettlementAuditChargeBase
     * @return
     */
    public Double anhuiSettlementAuditInformationChargeMoney(Double anhuiSubtractTheNumber,Double anhuiSettlementAuditChargeBase){
        Double money = anhuiSubtractTheNumber+anhuiSettlementAuditChargeBase;
        return money;
    }

    /**
     * 吴江下家基本费
     * @param cost
     * @return
     */
    public Double wujiangSettlementAuditInformationChargeBase(BigDecimal cost){
        //如果造价金额低于50000 则收费400
        if(cost.compareTo(new BigDecimal("50000"))<1){
            return 400.0;
        }
        //若果造价金额大于50000小于100000则收费800
        if(cost.compareTo(new BigDecimal("50000"))==1&&cost.compareTo(new BigDecimal("100000"))<1){
            return 800.0;
        }
        //如果造价超过五个亿
        if(cost.compareTo(new BigDecimal("500000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.0005"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        //除一万 跟数据库中的数据比对
        BigDecimal divide = cost.divide(new BigDecimal("10000"));
        WujiangSettlementAuditInformationCharge wujiangSettlementAuditInformationCharge = wujiangSettlementAuditInformationChargeMapper.wujiangSettlementAuditInformationChargeMoney(divide);
        Double rateCost = wujiangSettlementAuditInformationCharge.getRateCost();
        //编制
        BigDecimal multiply1 = divide.multiply(new BigDecimal(rateCost));
        return multiply1.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 吴江核检费
     * @param cost
     * @return
     */
    public Double wujiangSubtractTheNumberMoney(BigDecimal cost){
        BigDecimal multiply = cost.multiply(new BigDecimal("0.06"));
        return multiply.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 吴江下家应收金额
     * @param anhuiSubtractTheNumber
     * @param anhuiSettlementAuditChargeBase
     * @return
     */
    public Double wujiangSettlementAuditInformationChargeMoney(Double anhuiSubtractTheNumber,Double anhuiSettlementAuditChargeBase){
        Double money = anhuiSubtractTheNumber+anhuiSettlementAuditChargeBase;
        if(money<1000){
            money = 1000.00;
        }
        return money;
    }

    /**
     * 计提
     * @param baseMoney
     * @param SubtractMoney
     * @param ChargeMoney
     * @return
     */
    public Double settlementAuditImprovement(Double baseMoney,Double SubtractMoney,Double ChargeMoney){
        Double mprovement1 = (baseMoney*0.1+SubtractMoney*0.2)/1.06;
        Double mprovement2 = ChargeMoney*0.1/1.06;
        if(mprovement1>mprovement2){
            return mprovement1;
        }else{
            return mprovement2;
        }
    }

    public List<SettlementAuditInformation> settlementAuditInformationList(CostVo2 costVo2) {
        return settlementAuditInformationMapper.settlementAuditInformationList(costVo2);
    }
    public List<SettlementAuditInformation> EmployeesettlementAuditInformationList(EmployeeVo employeeVo){
        return settlementAuditInformationMapper.EmployeesettlementAuditInformationList(employeeVo);
    }

    /**
     * 安徽分档计算额
     */
    public Double anhuiTrackChargeBaseRate(BigDecimal cost){
        //如果造价超过一个亿
        if(cost.compareTo(new BigDecimal("100000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.003"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }else {
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            AnhuiTrackCharge anhuiTrackCharge = anhuiTrackChargeMapper.anhuiTrackChargeMoney(divide);
            Double rateCost = anhuiTrackCharge.getRateCost();
            BigDecimal multiply = divide.multiply(new BigDecimal(rateCost));
             return multiply.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * 吴江分档计算额
     * @param cost
     * @return
     */
    public Double wujiangTrackChargeBaseRate(BigDecimal cost){
        //如果造价超过五个亿
        if(cost.compareTo(new BigDecimal("500000000"))==1){
            BigDecimal multiply1 = cost.multiply(new BigDecimal("0.003"));
            return multiply1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }else{
            BigDecimal divide = cost.divide(new BigDecimal("10000"));
            WujiangTrackCharge wujiangTrackCharge = wujiangTrackChargeMapper.wujiangTrackChargeMoney(divide);
            Double rateCost = wujiangTrackCharge.getRateCost();
            BigDecimal multiply = divide.multiply(new BigDecimal(rateCost));
            BigDecimal multiply2 = divide.multiply(new BigDecimal("0.04"));
            BigDecimal add = multiply.add(multiply2);
            return add.multiply(new BigDecimal("10")).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * 跟踪审计计提
     * @param cost
     * @return
     */
    public Double trackImprovement(Double cost){
        return cost*0.1/1.06;
    }

    public List<Budgeting> trackList(CostVo2 costVo2){
        return budgetingMapper.trackList(costVo2);
    }
    public List<Budgeting> EmployeetrackList(EmployeeVo employeeVo){
        return budgetingMapper.EmployeetrackList(employeeVo);
    }

    /**
     * 绩效表添加
     * @param achievementsInfo
     */
    public void addAchievements(AchievementsInfo achievementsInfo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Example example = new Example(AchievementsInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberId",achievementsInfo.getMemberId());
        criteria.andEqualTo("baseProjectId",achievementsInfo.getBaseProjectId());
        AchievementsInfo achievementsInfo1 = achievementsInfoMapper.selectOneByExample(example);
        if(achievementsInfo1!=null){
            achievementsInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            achievementsInfoMapper.updateByExampleSelective(achievementsInfo,example);
        }else {
            achievementsInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
            achievementsInfo.setCreateTime(simpleDateFormat.format(new Date()));
            achievementsInfoMapper.insert(achievementsInfo);
        }
    }

    /**
     * 收入表添加
     * @param incomeInfo
     */
    public void addIncomeInfo(IncomeInfo incomeInfo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Example example = new Example(IncomeInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",incomeInfo.getBaseProjectId());
        IncomeInfo incomeInfo1 = incomeInfoMapper.selectOneByExample(example);
        if(incomeInfo1!=null){
            incomeInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            incomeInfoMapper.updateByExampleSelective(incomeInfo,example);
        }else {
            incomeInfo.setId(UUID.randomUUID().toString().replaceAll("-",""));
            incomeInfo.setCreateTime(simpleDateFormat.format(new Date()));
            incomeInfoMapper.insert(incomeInfo);
        }
    }

    /**
     * 总收入
     * @param costVo2
     * @return
     */
    public Double totalRevenue(CostVo2 costVo2){
        //如果没有筛选时间 默认展示今年
        if(costVo2.getStartTime()==null&&"".equals(costVo2.getStartTime()) ||
               "".equals(costVo2.getEndTime())&&costVo2.getEndTime()==null){
            costVo2 = this.NowYear(costVo2);
        }
        //总收入
        Double totalRevenue = incomeInfoMapper.newTotalRevenue(costVo2);

        return totalRevenue;
//        List<IncomeInfo> incomeInfos = incomeInfoMapper.totalRevenue(costVo2);
//        BigDecimal total = new BigDecimal(0);
//        for (IncomeInfo incomeInfo : incomeInfos) {
//            if (incomeInfo.getBudgetMoney()!=null){
//                total = total.add(incomeInfo.getBudgetMoney());
//            }
//            if (incomeInfo.getUpsubmitMoney()!=null){
//                total = total.add(incomeInfo.getUpsubmitMoney());
//            }
//            if (incomeInfo.getDownsubmitMoney()!=null){
//                total = total.add(incomeInfo.getDownsubmitMoney());
//            }
//            if (incomeInfo.getTruckMoney()!=null){
//                total = total.add(incomeInfo.getTruckMoney());
//            }
//        }
//        return total;
    }
    /**
     * 总支出
     */
    public Double totalexpenditure(CostVo2 costVo2){
        if(costVo2.getStartTime()==null&&"".equals(costVo2.getStartTime()) ||
                "".equals(costVo2.getEndTime())&&costVo2.getEndTime()==null){
            costVo2 = this.NowYear(costVo2);
        }
        //委外金额
        Double totalexpenditure = incomeInfoMapper.newTotalexpenditure(costVo2);

        //员工绩效
        Double Totalexpenditure2 = achievementsInfoMapper.newTotalexpenditure2(costVo2);

        return totalexpenditure+Totalexpenditure2;
//        List<DesignInfo> totalexpenditure = designInfoMapper.totalexpenditure(costVo2);
//        BigDecimal total = new BigDecimal(0);
//        for (DesignInfo designInfo : totalexpenditure) {
//            if(designInfo!=null){
//                total = total.add(designInfo.getOutsourceMoney());
//            }
//        }
//        List<Budgeting> totalexpenditure1 = budgetingMapper.totalexpenditure(costVo2);
//        for (Budgeting budgeting : totalexpenditure1) {
//            if(budgeting!=null){
//                total = total.add(budgeting.getAmountOutsourcing());
//            }
//        }
//        List<ProgressPaymentInformation> totalexpenditure2 = progressPaymentInformationDao.totalexpenditure(costVo2);
//        for (ProgressPaymentInformation progressPaymentInformation : totalexpenditure2) {
//            if(progressPaymentInformation!=null){
//                total = total.add(progressPaymentInformation.getAmountOutsourcing());
//            }
//        }
////        List<VisaChange> totalexpenditure3 = visaChangeMapper.totalexpenditure(costVo2);
////        for (VisaChange visaChange : totalexpenditure3) {
////            if(visaChange!=null){
////                total = total.add(new BigDecimal(visaChange.getOutsourcingAmount()));
////            }
////        }
//        List<LastSettlementReview> totalexpenditure4 = lastSettlementReviewMapper.totalexpenditure(costVo2);
//        for (LastSettlementReview lastSettlementReview : totalexpenditure4) {
//            if(lastSettlementReview!=null){
//                total = total.add(lastSettlementReview.getAmountOutsourcing());
//            }
//        }
//        List<SettlementAuditInformation> totalexpenditure5 = settlementAuditInformationMapper.totalexpenditure(costVo2);
//        for (SettlementAuditInformation settlementAuditInformation : totalexpenditure5) {
//            if(settlementAuditInformation!=null){
//                total = total.add(settlementAuditInformation.getAmountOutsourcing());
//            }
//        }
//        List<TrackAuditInfo> totalexpenditure6 = trackAuditInfoDao.totalexpenditure(costVo2);
//        for (TrackAuditInfo trackAuditInfo : totalexpenditure6) {
//            if(trackAuditInfo!=null){
//                total = total.add(trackAuditInfo.getOutsourceMoney());
//            }
//        }
//
//        List<AchievementsInfo> totalexpenditure7 = achievementsInfoMapper.totalexpenditure(costVo2);
//        for (AchievementsInfo achievementsInfo : totalexpenditure7) {
//            if(achievementsInfo!=null){
//                total = total.add(new BigDecimal(achievementsInfo.getDesginAchievements()));
//                total = total.add(new BigDecimal(achievementsInfo.getBudgetAchievements()));
//                total = total.add(new BigDecimal(achievementsInfo.getUpsubmitAchievements()));
//                total = total.add(new BigDecimal(achievementsInfo.getDownsubmitAchievements()));
//                total = total.add(new BigDecimal(achievementsInfo.getTruckAchievements()));
//            }
//        }
//        return total;
    }

    /**
     * 展示项目流程
     * @param costVo2
     * @return
     */
    public List<BaseProject> projectFlow(CostVo2 costVo2){
        List<BaseProject> baseProjects = projectMapper.projectFlow(costVo2);
        return baseProjects;
    }

    /**
     * 项目统计
     * @param costVo2
     * @return
     */
    public List<CostVo3> prjectCensus(CostVo2 costVo2){
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
//            Integer total = costVo3.getBudgetingCount()+
//            costVo3.getDesginStatus()+
//            costVo3.getProgressPaymentInformation()+
//            costVo3.getSettleAccountsCount()+
//            costVo3.getVisaApplyChangeInformationCount()+
//            costVo3.getTrackAuditInfoCount();
            total += costVo3.getTotal();
            //获取当前全部数量
            costVo3.setTotal(total);
        }
        return costVo3s;
    }

    /**
     * 获取该项目当前年的个数
     * @param costVo2
     */
    public Integer prjectCensusYear(CostVo2 costVo2){
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int mouth = now.getMonth().getValue();
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
//            total += costVo3.getBudgetingCount()+
//                    costVo3.getDesginStatus()+
//                    costVo3.getProgressPaymentInformation()+
//                    costVo3.getSettleAccountsCount()+
//                    costVo3.getVisaApplyChangeInformationCount()+
//                    costVo3.getTrackAuditInfoCount();
            total += costVo3.getTotal();
        }
        return total;
    }

    /**
     * 获取该项目当前月的个数
     * @param costVo2
     */
    public Integer prjectCensusMonth(CostVo2 costVo2){
        SimpleDateFormat sf_m=new SimpleDateFormat("MM");
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);

        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //当前一年时间
        costVo2.setStartTime(year+"-"+month+"-01");
        costVo2.setEndTime(year+"-"+month+"-"+day);
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
//            total += costVo3.getBudgetingCount()+
//                    costVo3.getDesginStatus()+
//                    costVo3.getProgressPaymentInformation()+
//                    costVo3.getSettleAccountsCount()+
//                    costVo3.getVisaApplyChangeInformationCount()+
//                    costVo3.getTrackAuditInfoCount();
            total += costVo3.getTotal();
        }
        return total;
    }

    /**
     * 获取该项目当前上个月的个数
     * @param costVo2
     */
    public Integer lastPrjectCensusMonth(CostVo2 costVo2){
        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        costVo2.setStartTime(fristDay);
        costVo2.setEndTime(lastDay);

        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
//            total += costVo3.getBudgetingCount()+
//                    costVo3.getDesginStatus()+
//                    costVo3.getProgressPaymentInformation()+
//                    costVo3.getSettleAccountsCount()+
//                    costVo3.getVisaApplyChangeInformationCount()+
//                    costVo3.getTrackAuditInfoCount();
            total += costVo3.getTotal();
        }
        return total;
    }

    /**
     * 获取该项目当前上一年的个数
     * @param costVo2
     */
    public Integer lastPrjectCensusYear(CostVo2 costVo2){
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int mouth = now.getMonth().getValue();
        costVo2.setStartTime(year-1+"-01-01");
        costVo2.setEndTime(year-1+"-12-31");
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
//            total += costVo3.getBudgetingCount()+
//                    costVo3.getDesginStatus()+
//                    costVo3.getProgressPaymentInformation()+
//                    costVo3.getSettleAccountsCount()+
//                    costVo3.getVisaApplyChangeInformationCount()+
//                    costVo3.getTrackAuditInfoCount();
            total += costVo3.getTotal();
        }
        return total;
    }

    public Integer prjectCensusRast(Integer A,Integer B){
        //A今年(月) B上年(年)
        return A-B;
    }

    /**
     * 总收入构成
     * @param costVo2
     * @return
     */
    public OneCensus projectIncomeCensus(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            OneCensus oneCensus = projectMapper.projectIncomeCensus(costVo2);
            return oneCensus;
        }else{
            OneCensus oneCensus = projectMapper.projectIncomeCensus(this.NowYear(costVo2));
            return oneCensus;
        }
    }

    /**
     * 总支出构成
     * @param costVo2
     * @return
     */
    public OneCensus projectExpenditureCensus(CostVo2 costVo2) {
        //如果筛选时间为空 则根据当前年查询
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            costVo2 = this.NowYear(costVo2);
        }
        //委外金额
        OneCensus oneCensus = projectMapper.projectExpenditureCensus(costVo2);
        //员工绩效
        OneCensus oneCensus2 = projectMapper.projectExpenditureCensus2(costVo2);
        //累计一起就是总支出

        //市政管道
        oneCensus.setMunicipalPipeline(oneCensus.getMunicipalPipeline()+oneCensus2.getMunicipalPipeline());
        //管网改造
        oneCensus.setNetworkReconstruction(oneCensus.getNetworkReconstruction()+oneCensus2.getNetworkReconstruction());
        //新建小区
        oneCensus.setNewCommunity(oneCensus.getNewCommunity()+oneCensus2.getNewCommunity());
        //二次供水项目
        oneCensus.setSecondaryWater(oneCensus.getSecondaryWater()+oneCensus2.getSecondaryWater());
        //工商户
        oneCensus.setCommercialHouseholds(oneCensus.getCommercialHouseholds()+oneCensus2.getCommercialHouseholds());
        //居民装接水
        oneCensus.setWaterResidents(oneCensus.getWaterResidents()+oneCensus2.getWaterResidents());
        //行政事业
        oneCensus.setAdministration(oneCensus.getAdministration()+oneCensus2.getAdministration());
        return oneCensus;
    }

    /**
     * 支出分析计算
     * @param costVo2
     * @return
     */
    public List<OneCensus3> expenditureAnalysis(CostVo2 costVo2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(StringUtils.isEmpty(costVo2.getStartTime()) || StringUtils.isEmpty(costVo2.getEndTime())){
            costVo2 = this.NowYear(costVo2);
        }

        List<OneCensus3> oneCensus3s = new ArrayList<>();

        Calendar start = new GregorianCalendar();   // 开始时间
        Calendar end = new GregorianCalendar();     // 结束时间
        try{
            start.setTime(simpleDateFormat.parse(costVo2.getStartTime()));
            end.setTime(simpleDateFormat.parse(costVo2.getEndTime()));

            for (;start.compareTo(end) <= 0;) {

                // 按月查询数据
                costVo2.setStartTime(simpleDateFormat.format(start.getTime()));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start.getTime());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                costVo2.setEndTime(simpleDateFormat.format(calendar.getTime()));

                List<OneCensus3> oneCensus3List = projectMapper.expenditureAnalysis(costVo2);
                // 如果当前月数据为空，添加一条
                if (!(oneCensus3List.size() > 0)){
                    OneCensus3 oneCensus3 = new OneCensus3();
                    oneCensus3.setYearTime(simpleDateFormat.format(start.getTime()).substring(0,4));
                    oneCensus3.setMonthTime(Integer.parseInt(simpleDateFormat.format(start.getTime()).substring(5,7)) + "");
                    oneCensus3.setAdvMoney("0");
                    oneCensus3.setOutMoney("0");
                    oneCensus3List.add(oneCensus3);
                }
                oneCensus3s.addAll(oneCensus3List);
                start.add(Calendar.MONTH, 1);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return oneCensus3s;
    }

    public PageInfo<BaseProject> BaseProjectExpenditureList(CostVo2 costVo2) {

        //设置分页助手
        PageHelper.startPage(costVo2.getPageNum(), costVo2.getPageSize());
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            List<BaseProject> baseProjects = projectMapper.BaseProjectExpenditureList(costVo2);
            PageInfo<BaseProject> projectPageInfo = new PageInfo<>(baseProjects);
            return projectPageInfo;
        }else{
            List<BaseProject> baseProjects = projectMapper.BaseProjectExpenditureList(this.NowYear(costVo2));
            PageInfo<BaseProject> projectPageInfo = new PageInfo<>(baseProjects);
            return projectPageInfo;
        }
    }

    public PageInfo<BaseProject> BaseProjectInfoCensus(CostVo2 costVo2) {
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            List<BaseProject> baseProjects = projectMapper.BaseProjectInfoCensus(costVo2);
            Double total = 0.00;
            for (BaseProject baseProject : baseProjects) {
                List<VisaChange> visaChanges = this.visaChangesById(baseProject.getId());
                for (VisaChange visaChange : visaChanges) {
                    total += visaChange.getAmountVisaChange().doubleValue();
                }
                baseProject.setVisaMoney(new BigDecimal(total));
            }
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }else{
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            costVo2.setStartTime(year + "-01-01");
            costVo2.setEndTime(year + "-12-31");
            List<BaseProject> baseProjects = projectMapper.BaseProjectInfoCensus(costVo2);
            Double total = 0.00;
            for (BaseProject baseProject : baseProjects) {
                List<VisaChange> visaChanges = this.visaChangesById(baseProject.getId());
                for (VisaChange visaChange : visaChanges) {
                    if(visaChange.getAmountVisaChange()!=null){
                        total += visaChange.getAmountVisaChange().doubleValue();
                    }
                }
                baseProject.setVisaMoney(new BigDecimal(total));
            }
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }
    }

    public List<VisaChange> visaChangesById(String id){
        Example example = new Example(VisaChange.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("baseProjectId",id);
        List<VisaChange> visaChanges = visaChangeMapper.selectByExample(example);
        return visaChanges;
    }
    
    /**
     * 计算设计项目总数
     * @param costVo2
     * @return
     */
    public Integer designInfoCount(CostVo2 costVo2){
        return designInfoMapper.designInfoCount(costVo2);
    }

    /**
     * 计算设计变更总数
     * @param costVo2
     * @return
     */
    public Integer designChangeInfoCount(CostVo2 costVo2){
        return designChangeInfoMapper.designChangeInfoCount(costVo2);
    }

    /**
     * 设计变更统计
     * @param costVo2
     * @return
     */
    public PageInfo<BaseProject> projectDesignChangeList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        List<BaseProject> baseProjects;
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())||
        costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            baseProjects = projectMapper.BaseProjectDesignList(costVo2);

        }else{
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            costVo2.setStartTime(year + "-01-01");
            costVo2.setEndTime(year + "-12-31");
            baseProjects = projectMapper.BaseProjectDesignList(costVo2);
        }
        for (BaseProject baseProject : baseProjects) {
            MemberManage memberManage = memberManageDao.selectByPrimaryKey(baseProject.getDesigner());
            if(memberManage != null){
                baseProject.setDesigner(memberManage.getMemberName());
            }else{
                baseProject.setDesigner("-");
            }
        }
        PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
        return baseProjectPageInfo;
    }

    /**
     * 进度款支付列表
     * @param costVo2
     * @return
     */
    public PageInfo<BaseProject> progressPaymentList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())||
        costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            List<BaseProject> baseProjects = projectMapper.progressPaymentList(costVo2);
            for (BaseProject baseProject : baseProjects) {
                Double accumulativePaymentProportion = baseProject.getAccumulativePaymentProportion();
                Double surplus = 100 - accumulativePaymentProportion;
                if(surplus<0){
                    baseProject.setSurplusPaymentProportion(0.00);
                }else{
                    baseProject.setSurplusPaymentProportion(surplus);
                }
            }
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }else{
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            costVo2.setStartTime(year + "-01-01");
            costVo2.setEndTime(year + "-12-31");
            List<BaseProject> baseProjects = projectMapper.progressPaymentList(costVo2);
            for (BaseProject baseProject : baseProjects) {
                Double accumulativePaymentProportion = baseProject.getAccumulativePaymentProportion();
                Double surplus = 100 - accumulativePaymentProportion;
                if(surplus<0){
                    baseProject.setSurplusPaymentProportion(0.00);
                }else{
                    baseProject.setSurplusPaymentProportion(surplus);
                }
            }
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }
    }
    public Integer progressPaymentCount(CostVo2 costVo2){
        //如果输入时间为空 则默认展示今年的区间
        if(costVo2.getStartTime()!=null&&"".equals(costVo2.getStartTime())){
            return projectMapper.progressPaymentCount(costVo2);
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            return projectMapper.progressPaymentCount(costVo21);
        }
    }
    public Double progressPaymentSum(CostVo2 costVo2){
        //如果输入时间为空 则默认展示今年的区间
        if(costVo2.getStartTime()!=null&&"".equals(costVo2.getStartTime())){
            return projectMapper.progressPaymentSum(costVo2);
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            return projectMapper.progressPaymentSum(costVo21);
        }
    }
    public PageInfo<BaseProject> projectVisaChangeList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            List<BaseProject> baseProjects = projectMapper.projectVisaChangeList(costVo2);
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }else{
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            costVo2.setStartTime(year + "-01-01");
            costVo2.setEndTime(year + "-12-31");
            List<BaseProject> baseProjects = projectMapper.projectVisaChangeList(costVo2);
            PageInfo<BaseProject> baseProjectPageInfo = new PageInfo<>(baseProjects);
            return baseProjectPageInfo;
        }
    }

    public Integer VisaChangeCount(CostVo2 costVo2){
        return projectMapper.VisaChangeCount(costVo2);
    }

    public Double VisaChangeMoney(CostVo2 costVo2){
        return projectMapper.VisaChangeMoney(costVo2);
    }

    public List<OneCensus4> projectSettlementCensus(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            List<OneCensus4> oneCensus4s = projectMapper.projectSettlementCensus(costVo2);
            return oneCensus4s;
        }else {
            CostVo2 costVo21 = this.NowYear(costVo2);
            List<OneCensus4> oneCensus4s = projectMapper.projectSettlementCensus(costVo21);
            return oneCensus4s;
        }
    }

    public OneCensus4 projectSettlementCount(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())
                ||costVo2.getEndTime()!=null&&!"".equals(costVo2.getEndTime())){
            return projectMapper.projectSettlementCount(costVo2);
        }else {
            CostVo2 costVo21 = this.NowYear(costVo2);
            return projectMapper.projectSettlementCount(costVo21);
        }
    }

    public OneCensus4 projectSettlementSum(CostVo2 costVo2){
        return projectMapper.projectSettlementSum(costVo2);
    }

    public OneCensus3 projectDesginCount(CostVo2 costVo2){
        return projectMapper.projectDesginCount(costVo2);
    }

    public List<OneCensus3> projectDesginStatus(CostVo2 costVo2){
        return projectMapper.projectDesginStatus(costVo2);
    }

    public List<OneCensus> censusList2(CostVo2 costVo2){
        return projectMapper.censusList2(costVo2);
    }

    /**
     * 当前月份任务总数
     * @param costVo2
     * @return
     */
    public Integer censusList2MonthCount(CostVo2 costVo2) {
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        // String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        costVo2.setStartTime(year+"-"+month+"-"+"01");
        costVo2.setEndTime(year+"-"+month+"-"+day);

            List<OneCensus> oneCensuses = projectMapper.censusList2(costVo2);
            Integer total = 0;
            for (OneCensus oneCensus : oneCensuses) {
                total += oneCensus.getAdministration()+
                        oneCensus.getWaterResidents()+
                        oneCensus.getCommercialHouseholds()+
                        oneCensus.getSecondaryWater()+
                        oneCensus.getNewCommunity()+
                        oneCensus.getMunicipalPipeline()+
                        oneCensus.getNetworkReconstruction();
            }
            return total;
    }

    /**
     * 上个月份任务总数
     * @param costVo2
     * @return
     */
    public Integer censusList2lastMonthCount(CostVo2 costVo2) {
        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        costVo2.setStartTime(fristDay);
        costVo2.setEndTime(lastDay);
        Integer total = 0;
        List<OneCensus> oneCensuses = projectMapper.censusList2(costVo2);
        for (OneCensus oneCensus : oneCensuses) {
            total += oneCensus.getAdministration()+
                    oneCensus.getWaterResidents()+
                    oneCensus.getCommercialHouseholds()+
                    oneCensus.getSecondaryWater()+
                    oneCensus.getNewCommunity()+
                    oneCensus.getMunicipalPipeline()+
                    oneCensus.getNetworkReconstruction();
        }
        return total;
    }

    /**
     * 获取当前年总数
     * @param costVo2
     * @return
     */
    public Integer censusList2YearCount(CostVo2 costVo2) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");
        Integer total = 0;
        List<OneCensus> oneCensuses = projectMapper.censusList2(costVo2);
        for (OneCensus oneCensus : oneCensuses) {
            total += oneCensus.getAdministration()+
                    oneCensus.getWaterResidents()+
                    oneCensus.getCommercialHouseholds()+
                    oneCensus.getSecondaryWater()+
                    oneCensus.getNewCommunity()+
                    oneCensus.getMunicipalPipeline()+
                    oneCensus.getNetworkReconstruction();
        }
        return total;
    }

    /**
     * 获取去前年总数
     * @param costVo2
     * @return
     */
    public Integer censusList2LastYearCount(CostVo2 costVo2) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year-1+"-01-01");
        costVo2.setEndTime(year-1+"-12-31");
        Integer total = 0;
        List<OneCensus> oneCensuses = projectMapper.censusList2(costVo2);
        for (OneCensus oneCensus : oneCensuses) {
            total += oneCensus.getAdministration()+
                    oneCensus.getWaterResidents()+
                    oneCensus.getCommercialHouseholds()+
                    oneCensus.getSecondaryWater()+
                    oneCensus.getNewCommunity()+
                    oneCensus.getMunicipalPipeline()+
                    oneCensus.getNetworkReconstruction();
        }
        return total;
    }

    /**
     * 获取设计统计
     * @param costVo2
     * @return
     */
    public OneCensus5 desiginMoneyCensus(CostVo2 costVo2){
        OneCensus5 oneCensus5 = new OneCensus5();
        //未到账数
        Integer integer = projectMapper.desiginMoneyCensus(costVo2);
        oneCensus5.setWujiangAmount(integer);
        oneCensus5.setAnhuiAnount(0);
        //到账数
        Integer integer1 = projectMapper.desiginMoneyCensus2(costVo2);
        oneCensus5.setNotAmount(integer1);
        return oneCensus5;
    }

    /**
     * 委外设计统计
     * @param costVo2
     * @return
     */
    public OneCensus5 desiginoutsource(CostVo2 costVo2){
        return projectMapper.desiginoutsource(costVo2);
    }

    /**
     * 设计任务分析
     * @param costVo2
     * @return
     */
    public PageInfo<DesignInfo> desginCensusList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        List<DesignInfo> designInfos = designInfoMapper.desginCensusList(costVo2);
        for (DesignInfo designInfo : designInfos) {
            if(!designInfo.getDistrict().equals("4")){
                Example anhui = new Example(AnhuiMoneyinfo.class);
                Example.Criteria c2 = anhui.createCriteria();
                c2.andEqualTo("baseProjectId",designInfo.getId());
                AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                if(anhuiMoneyinfo!=null){
                    designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                }
                // 设计人名字
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
                if (memberManage != null){
                    designInfo.setDesigner(memberManage.getMemberName());
                }
                //如果为吴江
            }else{
                Example wujiang = new Example(WujiangMoneyInfo.class);
                Example.Criteria c2 = wujiang.createCriteria();
                c2.andEqualTo("baseProjectId",designInfo.getId());
                WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                if(wujiangMoneyInfo!=null){designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());}
                // 设计人名字
                MemberManage memberManage = memberManageDao.selectByPrimaryKey(designInfo.getDesigner());
                if (memberManage != null){
                    designInfo.setDesigner(memberManage.getMemberName());
                }
            }
            //获取预算表中的造价金额
            Example example = new Example(Budgeting.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",designInfo.getId());
            Budgeting budgeting = budgetingMapper.selectOneByExample(example);
            if(budgeting!=null){
                designInfo.setAmountCost(budgeting.getAmountCost()+"");
                if(null == budgeting.getAmountCost()){
                    designInfo.setAmountCost("0");
                }
            }else{
                designInfo.setAmountCost("0");
            }
        }
        PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(designInfos);
        return designInfoPageInfo;
    }

    /**
     * 根据设计人查询
     * @param costVo2
     * @return
     */
    public PageInfo<DesignInfo> desginCensusListByDesigner(CostVo2 costVo2){
        // 把成员id传给VO 查找集合
        Example example1 = new Example(MemberManage.class);
        example1.createCriteria().andEqualTo("memberName",costVo2.getDesigner());
        MemberManage memberManage = memberManageDao.selectOneByExample(example1);
        costVo2.setDesigner(memberManage.getId());
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        List<DesignInfo> designInfos = designInfoMapper.desginCensusListByDesigner(costVo2);
        for (DesignInfo designInfo : designInfos) {
            if(!designInfo.getDistrict().equals("4")){
                Example anhui = new Example(AnhuiMoneyinfo.class);
                Example.Criteria c2 = anhui.createCriteria();
                c2.andEqualTo("baseProjectId",designInfo.getId());
                AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                if(anhuiMoneyinfo!=null){
                    designInfo.setDisMoney(anhuiMoneyinfo.getRevenue());
                }else {
                    designInfo.setDisMoney(new BigDecimal(0));
                }
                if (designInfo.getDesignUnit() != null && !"".equals(designInfo.getDesignUnit())){
                    designInfo.setDesignUnit(designInfo.getDesignUnit());
                }else {
                    designInfo.setDesignUnit("-");
                }
                if (designInfo.getBlueprintStartTime() != null && !"".equals(designInfo.getBlueprintStartTime())){
                    designInfo.setBlueprintStartTime(designInfo.getBlueprintStartTime());
                }else {
                    designInfo.setBlueprintStartTime("-");
                }
                //如果为吴江
            }else{
                Example wujiang = new Example(WujiangMoneyInfo.class);
                Example.Criteria c2 = wujiang.createCriteria();
                c2.andEqualTo("baseProjectId",designInfo.getId());
                WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                if(wujiangMoneyInfo!=null){
                    designInfo.setDisMoney(wujiangMoneyInfo.getRevenue());
                }else {
                    designInfo.setDisMoney(new BigDecimal(0));
                }
                if (designInfo.getDesignUnit() != null && !"".equals(designInfo.getDesignUnit())){
                    designInfo.setDesignUnit(designInfo.getDesignUnit());
                }else {
                    designInfo.setDesignUnit("-");
                }
                if (designInfo.getBlueprintStartTime() != null && !"".equals(designInfo.getBlueprintStartTime())){
                    designInfo.setBlueprintStartTime(designInfo.getBlueprintStartTime());
                }else {
                    designInfo.setBlueprintStartTime("-");
                }
            }
            //获取预算表中的造价金额
            Example example = new Example(Budgeting.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("baseProjectId",designInfo.getId());
            Budgeting budgeting = budgetingMapper.selectOneByExample(example);
            if(budgeting!=null){
                designInfo.setAmountCost(budgeting.getAmountCost()+"");
            }else{
                designInfo.setAmountCost("0");
            }
        }
        PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(designInfos);
        return designInfoPageInfo;
    }

    /**
     * 设计绩效统计信息
     * @param costVo2
     * @return
     */
    public List<OneCensus6> desiginAchievementsCensus(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            return projectMapper.desiginAchievementsCensus(costVo2);
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            return projectMapper.desiginAchievementsCensus(costVo21);
        }
    }

    /**
     * 当前月的设计绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginMonthAchievements(CostVo2 costVo2){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        costVo2.setStartTime(year+"-"+month+"-"+"01");
        costVo2.setEndTime(year+"-"+month+"-"+day);

        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 当前年的设计绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginYearAchievements(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");

        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 上个月的设计绩效
     * @return
     */
    public BigDecimal desiginLastMonthAchievements(CostVo2 costVo2){

        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        costVo2.setStartTime(fristDay);
        costVo2.setEndTime(lastDay);
        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 上一年的设计绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginLastYearAchievements(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year-1+"-01-01");
        costVo2.setEndTime(year-1+"-12-31");
        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 计算设计的同比上个月
     * @param A
     * @param B
     * @return
     */
    public BigDecimal desiginCensusRast(BigDecimal A,BigDecimal B){
        //A今年 b上年
//        if(A.compareTo(new BigDecimal(0)) == 0){
//            A = new BigDecimal(1);
//        }
        BigDecimal subtract = A.subtract(B);
//        BigDecimal divide = subtract.divide(A,2,BigDecimal.ROUND_HALF_UP);
//        BigDecimal multiply = divide.multiply(new BigDecimal(100));
        return subtract;
    }

    /**
     * 个人月度绩效统计
     * @param costVo2
     * @return
     */
    public List<OneCensus6> desiginAchievementsOneCensus(CostVo2 costVo2){
        return projectMapper.desiginAchievementsOneCensus(costVo2);
    }
    /**
     * 本月发放的绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginAchievementsOneCount(CostVo2 costVo2){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        costVo2.setStartTime(year+"-"+month+"-"+"01");
        costVo2.setEndTime(year+"-"+month+"-"+day);

        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsOneCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }


    /**
     * 上个月的设计绩效
     * @return
     */
    public BigDecimal desiginLastAchievementsOneCount(CostVo2 costVo2){
        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        costVo2.setStartTime(fristDay);
        costVo2.setEndTime(lastDay);
        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsOneCensus(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 个人年度绩效统计
     * @param costVo2
     * @return
     */
    public List<OneCensus6> desiginAchievementsOneCensus2(CostVo2 costVo2){

        if ((null == costVo2.getStartTime() || "" == costVo2.getStartTime())
                && (null == costVo2.getEndTime() || "" == costVo2.getEndTime())){
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            costVo2.setYear(year+"");
            costVo2.setStartTime(year-9+"-01-01");
            costVo2.setEndTime(year+"-12-31");
        }
            return projectMapper.desiginAchievementsOneCensus2(costVo2);
    }

    /**
     * 本年发布的绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginAchievementsOneCount2(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");
        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsOneCensus2(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 上一年发布的绩效
     * @param costVo2
     * @return
     */
    public BigDecimal desiginLastAchievementsOneCount2(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year-1+"-01-01");
        costVo2.setEndTime(year-1+"-12-31");
        List<OneCensus6> oneCensus6s = projectMapper.desiginAchievementsOneCensus2(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total = total.add(oneCensus6.getDesginAchievements());
        }
        return total;
    }

    /**
     * 任务总数
     * @param costVo2
     * @return
     */
    public Integer costTaskTotal(CostVo2 costVo2){
        OneCensus6 oneCensus6 = projectMapper.costTaskTotal(costVo2);
        Integer total = oneCensus6.getBudgetStatus()
                +oneCensus6.getProgressPaymentStatus()
                +oneCensus6.getSettleAccountsStatus()
                +oneCensus6.getVisaStatus()
                +oneCensus6.getTrackStatus();
        return total;
    }

    /**
     * 造价待审核的任务
     * @param costVo2
     * @return
     */
    public Integer costTaskReviewed(CostVo2 costVo2){
        OneCensus6 oneCensus6 = projectMapper.costTaskReviewed(costVo2);
        Integer total = oneCensus6.getBudgetStatus()
                +oneCensus6.getProgressPaymentStatus()
                +oneCensus6.getSettleAccountsStatus()
                +oneCensus6.getVisaStatus()
                +oneCensus6.getTrackStatus();
        return total;
    }

    /**
     * 造价进行中的任务
     * @param costVo2
     * @return
     */
    public Integer costTaskHandle(CostVo2 costVo2){
        OneCensus6 oneCensus6 = projectMapper.costTaskHandle(costVo2);
        Integer total = oneCensus6.getBudgetStatus()
                +oneCensus6.getProgressPaymentStatus()
                +oneCensus6.getSettleAccountsStatus()
                +oneCensus6.getVisaStatus();
        return total;
    }

    /**
     * 造价完成任务个数
     * @param costVo2
     * @return
     */
    public Integer costTaskComple(CostVo2 costVo2){
        OneCensus6 oneCensus6 = projectMapper.costTaskComple(costVo2);
        Integer total = oneCensus6.getBudgetStatus()
                +oneCensus6.getProgressPaymentStatus()
                +oneCensus6.getSettleAccountsStatus()
                +oneCensus6.getVisaStatus()
                +oneCensus6.getTrackStatus();
        return total;
    }

    /**
     * 造价任务统计图
     * @param costVo2
     * @return
     */
    public List<OneCensus6> costTaskCensus(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo2);
            for (OneCensus6 oneCensus6 : oneCensus6s) {
                oneCensus6.setTotal(oneCensus6.getBudgetStatus() + oneCensus6.getProgressPaymentStatus() + oneCensus6.getVisaStatus() + oneCensus6.getSettleAccountsStatus() + oneCensus6.getTrackStatus());
            }
            return oneCensus6s;
        }
    else {
            CostVo2 costVo21 = this.NowYear(costVo2);
            List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo21);

            for (OneCensus6 oneCensus6 : oneCensus6s) {
                oneCensus6.setTotal(oneCensus6.getBudgetStatus() + oneCensus6.getProgressPaymentStatus() + oneCensus6.getVisaStatus() + oneCensus6.getSettleAccountsStatus() + oneCensus6.getTrackStatus());
            }
            return oneCensus6s;
        }
    }

    /**
     * 本月造价任务数量
     * @param costVo2
     * @return
     */
    public Integer costTaskMonthTotal(CostVo2 costVo2){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        costVo2.setStartTime(year+"-"+month+"-"+"01");
        costVo2.setEndTime(year+"-"+month+"-"+day);
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo2);
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus();
        }
        return total;
    }

    /**
     * 本年造价任务数量
     * @param costVo2
     * @return
     */
    public Integer costTaskYearTotal(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo2);
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus();
        }
        return total;
    }

    /**
     * 上个月造价任务数量
     * @return
     */
    public Integer costTaskLastMonthTotal(CostVo2 costVo2){
        CostVo2 costVo21 = this.lastMonth(costVo2);
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo2);
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus();
        }
        return total;
    }

    public Integer costTaskLastYearTotal(CostVo2 costVo2){
        CostVo2 costVo21 = this.lastYear(costVo2);
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(costVo2);
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus();
        }
        return total;
    }

    /**
     * 获取上个月
     * @param costVo2
     * @return
     */
    public CostVo2 lastMonth(CostVo2 costVo2){
        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        costVo2.setStartTime(fristDay);
        costVo2.setEndTime(lastDay);
        return costVo2;
    }

    /**
     * 获取上一年
     * @param costVo2
     * @return
     */
    public CostVo2 lastYear(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year-1+"-01-01");
        costVo2.setEndTime(year-1+"-12-31");
        return costVo2;
    }

    /**
     * 获取本月
     * @param costVo2
     * @return
     */
    public CostVo2 NowMonth(CostVo2 costVo2){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        costVo2.setStartTime(year+"-"+month+"-"+"01");
        costVo2.setEndTime(year+"-"+month+"-"+day);
        return costVo2;
    }

    /**
     * 获取本年
     * @param costVo2
     * @return
     */
    public CostVo2 NowYear(CostVo2 costVo2){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        costVo2.setYear(year+"");
        costVo2.setStartTime(year+"-01-01");
        costVo2.setEndTime(year+"-12-31");
        return costVo2;
    }

    /**
     * 造价任务汇总统计
     * @param costVo2
     * @return
     */
    public OneCensus7 costTaskSummary(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null
                &&!"".equals(costVo2.getStartTime())||
                costVo2.getEndTime()!=null
                &&!"".equals(costVo2.getEndTime())){
            OneCensus7 oneCensus7 = projectMapper.costTaskSummary(costVo2);
            return oneCensus7;
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            OneCensus7 oneCensus7 = projectMapper.costTaskSummary(costVo21);
            return oneCensus7;
        }
    }

    /**
     * 委外
     * @param costVo2
     * @return
     */
    public Integer costTaskOutsourcingCount(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null
                &&!"".equals(costVo2.getStartTime())||
                costVo2.getEndTime()!=null
                        &&!"".equals(costVo2.getEndTime())){
            OneCensus7 oneCensus7 = projectMapper.costTaskOutsourcingCount(costVo2);
            Integer total = oneCensus7.getBudgeting() + oneCensus7.getLastSettlementReview() + oneCensus7.getSettlementAuditInformation()
                    +oneCensus7.getVisaChangeInformation() + oneCensus7.getProgressPaymentInformation();
            return total;
        }else{
            OneCensus7 oneCensus7 = projectMapper.costTaskOutsourcingCount(this.NowYear(costVo2));
            Integer total = oneCensus7.getBudgeting() + oneCensus7.getLastSettlementReview() + oneCensus7.getSettlementAuditInformation()
                    +oneCensus7.getVisaChangeInformation() + oneCensus7.getProgressPaymentInformation();
            return total;
        }
    }

    /**
     * 内部
     * @param costVo2
     * @return
     */
    public Integer costTaskNoOutsourcingCount(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null
                &&!"".equals(costVo2.getStartTime())||
                costVo2.getEndTime()!=null
                        &&!"".equals(costVo2.getEndTime())){
            OneCensus7 oneCensus7 = projectMapper.costTaskNoOutsourcingCount(costVo2);
            Integer total = oneCensus7.getBudgeting() + oneCensus7.getLastSettlementReview() + oneCensus7.getSettlementAuditInformation()
                    +oneCensus7.getVisaChangeInformation() + oneCensus7.getProgressPaymentInformation();
            return total;
        }else{
            OneCensus7 oneCensus7 = projectMapper.costTaskNoOutsourcingCount(this.NowYear(costVo2));
            Integer total = oneCensus7.getBudgeting() + oneCensus7.getLastSettlementReview() + oneCensus7.getSettlementAuditInformation()
                    +oneCensus7.getVisaChangeInformation() + oneCensus7.getProgressPaymentInformation();
            return total;
        }
    }

    /**
     * 设计绩效统计列表
     * @param costVo2
     * @return
     */
    public List<OneCensus8> DesginAchievementsList(CostVo2 costVo2){
            if(costVo2.getStartTime()!=null&&"".equals(costVo2.getStartTime())||costVo2.getEndTime()!=null
                    &&"".equals(costVo2.getEndTime())){
                List<OneCensus8> oneCensus8s = projectMapper.DesginAchievementsList(costVo2);
                return oneCensus8s;
            }else{
                CostVo2 costVo21 = NowMonth(costVo2);
                List<OneCensus8> oneCensus8s = projectMapper.DesginAchievementsList(costVo21);
                return oneCensus8s;
            }
    }

    /**
     * 月度绩效计提统计列表
     * @param costVo2
     * @return
     */
    public PageInfo<OneCensus9> DesginMonthAchievementsList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        List<OneCensus9> oneCensus9s = projectMapper.DesginMonthAchievementsList(costVo2);
        for (OneCensus9 oneCensus9 : oneCensus9s) {
            //设计费填入
            if(!oneCensus9.getDistrict().equals("4")){
                Example anhui = new Example(AnhuiMoneyinfo.class);
                Example.Criteria c2 = anhui.createCriteria();
                c2.andEqualTo("baseProjectId",oneCensus9.getId());
                AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                if(anhuiMoneyinfo!=null){
                    oneCensus9.setDesginMoney(anhuiMoneyinfo.getRevenue());
                    if(anhuiMoneyinfo.getOfficialReceipts()!=null&&!"".equals(anhuiMoneyinfo.getOfficialReceipts())){
                        //如果实收金额不为空 则到账
                        oneCensus9.setIsAmount("1");
                        oneCensus9.setIsConfirm("1");
                    }
                    //递延项目判断
                    String BlueprintStartTime = "";
                    if(oneCensus9.getBlueprintStartTime()!=null){
                        BlueprintStartTime = oneCensus9.getBlueprintStartTime().substring(0, 7);
                    }
                    String CreateTime = "";
                    if(anhuiMoneyinfo.getCreateTime()!=null){
                        CreateTime = anhuiMoneyinfo.getCreateTime().substring(0, 7);
                    }
                    if(BlueprintStartTime.equals(CreateTime)){
                        oneCensus9.setProjectDeferral("2");
                    }else{
                        oneCensus9.setProjectDeferral("1");
                    }
                }else{
                    //如果无法找到对应支付信息 则未到账
                    oneCensus9.setIsAmount("2");
                    oneCensus9.setIsConfirm("2");
                }
                //如果为吴江
            }else{
                Example wujiang = new Example(WujiangMoneyInfo.class);
                Example.Criteria c2 = wujiang.createCriteria();
                c2.andEqualTo("baseProjectId",oneCensus9.getId());
                WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                if(wujiangMoneyInfo!=null){
                    oneCensus9.setDesginMoney(wujiangMoneyInfo.getRevenue());
                    if(wujiangMoneyInfo.getOfficialReceipts()!=null&&!"".equals(wujiangMoneyInfo.getOfficialReceipts())){
                        //如果实收金额不为空 则到账
                        oneCensus9.setIsAmount("1");
                        oneCensus9.setIsConfirm("1");
                    }
                    //递延项目判断
                    String BlueprintStartTime = "";
                    if(oneCensus9.getBlueprintStartTime()!=null){
                        BlueprintStartTime = oneCensus9.getBlueprintStartTime().substring(0, 7);
                    }
                    String CreateTime = "";
                    if(wujiangMoneyInfo.getCreateTime()!=null){
                        CreateTime = wujiangMoneyInfo.getCreateTime().substring(0, 7);
                    }
                    if(BlueprintStartTime.equals(CreateTime)){
                        oneCensus9.setProjectDeferral("2");
                    }else{
                        oneCensus9.setProjectDeferral("1");
                    }
                }else{
                    //如果无法找到对应支付信息 则未到账
                    oneCensus9.setIsAmount("2");
                    oneCensus9.setIsConfirm("2");
                }
            }
        }
        PageInfo<OneCensus9> oneCensus9PageInfo = new PageInfo<>(oneCensus9s);
        return oneCensus9PageInfo;
    }

    /**
     * 年度绩效计提统计列表
     * @param costVo2
     * @return
     */
    public PageInfo<OneCensus9> DesginYearAchievementsList(CostVo2 costVo2){
        PageHelper.startPage(costVo2.getPageNum(),costVo2.getPageSize());
        List<OneCensus9> oneCensus9s = projectMapper.DesginMonthAchievementsList(costVo2);
        for (OneCensus9 oneCensus9 : oneCensus9s) {
            //设计费填入
            if(!oneCensus9.getDistrict().equals("4")){
                Example anhui = new Example(AnhuiMoneyinfo.class);
                Example.Criteria c2 = anhui.createCriteria();
                c2.andEqualTo("baseProjectId",oneCensus9.getId());
                AnhuiMoneyinfo anhuiMoneyinfo = anhuiMoneyinfoMapper.selectOneByExample(anhui);
                if(anhuiMoneyinfo!=null){
                    oneCensus9.setDesginMoney(anhuiMoneyinfo.getRevenue());
                    if(anhuiMoneyinfo.getOfficialReceipts()!=null&&!"".equals(anhuiMoneyinfo.getOfficialReceipts())){
                        //如果实收金额不为空 则到账
                        oneCensus9.setIsAmount("1");
                        oneCensus9.setIsConfirm("1");
                    }
                    //递延项目判断
                    String BlueprintStartTime = "";
                    if(oneCensus9.getBlueprintStartTime()!=null){
                        BlueprintStartTime = oneCensus9.getBlueprintStartTime().substring(0, 7);
                    }
                    String CreateTime = "";
                    if(anhuiMoneyinfo.getCreateTime()!=null){
                        CreateTime = anhuiMoneyinfo.getCreateTime().substring(0, 7);
                        oneCensus9.setAccountTime(anhuiMoneyinfo.getCreateTime());
                    }
                    if(BlueprintStartTime.equals(CreateTime)){
                        oneCensus9.setProjectDeferral("2");
                    }else{
                        oneCensus9.setProjectDeferral("1");
                    }
                }else{
                    //如果无法找到对应支付信息 则未到账
                    oneCensus9.setIsAmount("2");
                    oneCensus9.setIsConfirm("2");
                }
                //如果为吴江
            }else{
                Example wujiang = new Example(WujiangMoneyInfo.class);
                Example.Criteria c2 = wujiang.createCriteria();
                c2.andEqualTo("baseProjectId",oneCensus9.getId());
                WujiangMoneyInfo wujiangMoneyInfo = wujiangMoneyInfoMapper.selectOneByExample(wujiang);
                if(wujiangMoneyInfo!=null){
                    oneCensus9.setDesginMoney(wujiangMoneyInfo.getRevenue());
                    if(wujiangMoneyInfo.getOfficialReceipts()!=null&&!"".equals(wujiangMoneyInfo.getOfficialReceipts())){
                        //如果实收金额不为空 则到账
                        oneCensus9.setIsAmount("1");
                        oneCensus9.setIsConfirm("1");
                    }
                    //递延项目判断
                    String BlueprintStartTime = "";
                    if(oneCensus9.getBlueprintStartTime()!=null){
                        BlueprintStartTime = oneCensus9.getBlueprintStartTime().substring(0, 7);
                    }
                    String CreateTime = "";
                    if(wujiangMoneyInfo.getCreateTime()!=null){
                        CreateTime = wujiangMoneyInfo.getCreateTime().substring(0, 7);
                        oneCensus9.setAccountTime(wujiangMoneyInfo.getCreateTime());
                    }
                    if(BlueprintStartTime.equals(CreateTime)){
                        oneCensus9.setProjectDeferral("2");
                    }else{
                        oneCensus9.setProjectDeferral("1");
                    }
                }else{
                    //如果无法找到对应支付信息 则未到账
                    oneCensus9.setIsAmount("2");
                    oneCensus9.setIsConfirm("2");
                }
            }
            Double balance = oneCensus9.getDesginAchievements()-oneCensus9.getDesginAchievements2();
            balance= (double)Math.round(balance*100)/100;
            oneCensus9.setBalance(balance);
        }
        PageInfo<OneCensus9> oneCensus9PageInfo = new PageInfo<>(oneCensus9s);
        return oneCensus9PageInfo;
    }

    public List<OneCensus10> costTaskCensusList(CostVo2 costVo2){
        List<OneCensus10> oneCensus10s = projectMapper.costTaskCensusList(costVo2);
        List<OneCensus10> oneCensus10s1 = projectMapper.costTaskCensusList2(costVo2);
        for (OneCensus10 oneCensus10 : oneCensus10s) {
            for (OneCensus10 census10 : oneCensus10s1) {
                if(oneCensus10.getYearTime().equals(census10.getYearTime())){
                    if(oneCensus10.getMonthTime().equals(census10.getMonthTime())){
                        oneCensus10.setBudCountB(census10.getBudCountB());
                        oneCensus10.setCostTotalAmountB(census10.getCostTotalAmountB());
                    }else{
                        OneCensus10 oneCensus101 = new OneCensus10();

                    }
                }
            }
        }
        return oneCensus10s;
    }

    public List<OneCensus2> memberAchievementsCensus(CostVo2 costVo2){
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo2);
            return oneCensus2s;
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo21);
            return oneCensus2s;
        }
    }

    public BigDecimal memberAchievementsYearCount(CostVo2 costVo2){
        CostVo2 costVo21 = this.NowYear(costVo2);
        List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo21);
        Double total = 0.0;
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            total += oneCensus2.getTotal();
        }
        return new BigDecimal(total);
    }

    public BigDecimal memberAchievementsMonthCount(CostVo2 costVo2){
        CostVo2 costVo21 = this.NowMonth(costVo2);
        List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo21);
        Double total = 0.0;
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            total += oneCensus2.getTotal();
        }
        return new BigDecimal(total);
    }

    public BigDecimal memberAchievementsLastYearCount(CostVo2 costVo2){
        CostVo2 costVo21 = this.lastYear(costVo2);
        List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo21);
        Double total = 0.0;
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            total += oneCensus2.getTotal();
        }
        return new BigDecimal(total);
    }

    public BigDecimal memberAchievementsLastMonthCount(CostVo2 costVo2){
        CostVo2 costVo21 = this.lastMonth(costVo2);
        List<OneCensus2> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus(costVo21);
        Double total = 0.0;
        for (OneCensus2 oneCensus2 : oneCensus2s) {
            total += oneCensus2.getTotal();
        }
        return new BigDecimal(total);
    }

    public List<OneCensus4> MemberAchievementsCensus2(CostVo2 costVo2){
        //如果是设计
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            List<OneCensus4> oneCensus2s = achievementsInfoMapper.MemberAchievementsCensus2(costVo2);
            return oneCensus2s;
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            return achievementsInfoMapper.MemberAchievementsCensus2(costVo21);
        }
    }

    /**
     * 查询所有设计业务员
     * @return
     */
    public List<MemberManage> desginPerson() {
        Example example = new Example(MemberManage.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("depId","1");
        c.andEqualTo("memberRoleId","6");
        List<MemberManage> memberManages = memberManageDao.selectByExample(example);
        return memberManages;
    }

    /**
     * 造价绩效饼状图
     * @param costVo2
     * @return
     */
    public PerformanceDistributionChart findBTAll2(CostVo2 costVo2) {
        if(costVo2.getStartTime()!=null&&!"".equals(costVo2.getStartTime())){
            PerformanceDistributionChart btAll2 = achievementsInfoMapper.findBTAll2(costVo2);
            return btAll2;
        }else{
            CostVo2 costVo21 = this.NowYear(costVo2);
            PerformanceDistributionChart btAll2 = achievementsInfoMapper.findBTAll2(costVo21);
            return btAll2;
        }
    }

    public Integer NowMonthCostTaskCensusCount(CostVo2 costVo2) {
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(this.NowMonth(costVo2));
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus()+oneCensus6.getTrackStatus();
        }
        return total;
    }

    public Integer LastMonthCostTaskCensusCount(CostVo2 costVo2) {
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(this.lastMonth(costVo2));
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus()+oneCensus6.getTrackStatus();
        }
        return total;
    }

    public Integer NowYearCostTaskCensusCount(CostVo2 costVo2) {
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(this.NowYear(costVo2));
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus()+oneCensus6.getTrackStatus();
        }
        return total;
    }

    public Integer LastYearCostTaskCensusCount(CostVo2 costVo2) {
        List<OneCensus6> oneCensus6s = projectMapper.costTaskCensus(this.lastYear(costVo2));
        Integer total = 0;
        for (OneCensus6 oneCensus6 : oneCensus6s) {
            total += oneCensus6.getBudgetStatus()+oneCensus6.getProgressPaymentStatus()+oneCensus6.getVisaStatus()+oneCensus6.getSettleAccountsStatus()+oneCensus6.getTrackStatus();
        }
        return total;
    }
}

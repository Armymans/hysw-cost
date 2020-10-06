package net.zlw.cloud.designProject.service;


import net.zlw.cloud.VisaApplyChangeInformation.mapper.VisaApplyChangeInformationMapper;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaApplyChangeInformation;
import net.zlw.cloud.VisaApplyChangeInformation.model.VisaChangeInformation;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.UUID;

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
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private VisaChangeMapper visaChangeMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;


    /**
     * 全部项目
     * @return
     */
    public Integer AllprojectCount(){
        CostVo3 costVo3 = projectMapper.AllprojectCount();
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
     * 待审核项目
     * @return
     */
    public Integer withAuditCount(){
        CostVo3 costVo3 = projectMapper.withAuditCount();
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
     * 已完成的项目
     * @return
     */
    public Integer conductCount(){
        CostVo3 costVo3 = projectMapper.conductCount();
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
     * 进行中项目
     * @return
     */
    public Integer completeCount(){
        CostVo3 costVo3 = projectMapper.completeCount();
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
        if(cost1>cost2){
            return cost1;
        }else if(cost2>cost1){
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

    /**
     * 上家结算编制列表
     * @param costVo2
     * @return
     */
    public List<LastSettlementReview> lastSettlementReviewChargeList(CostVo2 costVo2){
        return lastSettlementReviewMapper.lastSettlementReviewList(costVo2);
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
        return null;
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
    public BigDecimal totalRevenue(CostVo2 costVo2){
        if(costVo2.getStartTime()==null&&costVo2.getEndTime()==null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            String year = format.format(new Date());
            costVo2.setStartTime(year+"-01-01");
            costVo2.setEndTime(year+"-12-30");
        }
        List<IncomeInfo> incomeInfos = incomeInfoMapper.totalRevenue(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (IncomeInfo incomeInfo : incomeInfos) {
            total = total.add(incomeInfo.getBudgetMoney());
            total = total.add(incomeInfo.getUpsubmitMoney());
            total = total.add(incomeInfo.getDownsubmitMoney());
            total = total.add(incomeInfo.getTruckMoney());
        }
        List<WujiangMoneyInfo> wujiangMoneyInfos = wujiangMoneyInfoMapper.totalRevenue(costVo2);
        for (WujiangMoneyInfo wujiangMoneyInfo : wujiangMoneyInfos) {
            if(wujiangMoneyInfo.getOfficialReceipts()!=null){
                total = total.add(wujiangMoneyInfo.getOfficialReceipts());
            }
        }
        List<AnhuiMoneyinfo> anhuiMoneyinfos = anhuiMoneyinfoMapper.totalRevenue(costVo2);
        for (AnhuiMoneyinfo anhuiMoneyinfo : anhuiMoneyinfos) {
            if(anhuiMoneyinfo.getOfficialReceipts()!=null){
                total = total.add(anhuiMoneyinfo.getOfficialReceipts());
            }
        }
        return total;
    }
    /**
     * 总支出
     */
    public BigDecimal totalexpenditure(CostVo2 costVo2){
        if(costVo2.getStartTime()==null&&costVo2.getEndTime()==null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            String year = format.format(new Date());
            costVo2.setStartTime(year+"-01-01");
            costVo2.setEndTime(year+"-12-30");
        }


        List<DesignInfo> totalexpenditure = designInfoMapper.totalexpenditure(costVo2);
        BigDecimal total = new BigDecimal(0);
        for (DesignInfo designInfo : totalexpenditure) {
            if(designInfo!=null){
                total = total.add(designInfo.getOutsourceMoney());
            }
        }
        List<Budgeting> totalexpenditure1 = budgetingMapper.totalexpenditure(costVo2);
        for (Budgeting budgeting : totalexpenditure1) {
            if(budgeting!=null){
                total = total.add(budgeting.getAmountOutsourcing());
            }
        }
        List<ProgressPaymentInformation> totalexpenditure2 = progressPaymentInformationDao.totalexpenditure(costVo2);
        for (ProgressPaymentInformation progressPaymentInformation : totalexpenditure2) {
            if(progressPaymentInformation!=null){
                total = total.add(progressPaymentInformation.getAmountOutsourcing());
            }
        }
        List<VisaChange> totalexpenditure3 = visaChangeMapper.totalexpenditure(costVo2);
        for (VisaChange visaChange : totalexpenditure3) {
            if(visaChange!=null){
                total = total.add(new BigDecimal(visaChange.getOutsourcingAmount()));
            }
        }
        List<LastSettlementReview> totalexpenditure4 = lastSettlementReviewMapper.totalexpenditure(costVo2);
        for (LastSettlementReview lastSettlementReview : totalexpenditure4) {
            if(lastSettlementReview!=null){
                total = total.add(lastSettlementReview.getAmountOutsourcing());
            }
        }
        List<SettlementAuditInformation> totalexpenditure5 = settlementAuditInformationMapper.totalexpenditure(costVo2);
        for (SettlementAuditInformation settlementAuditInformation : totalexpenditure5) {
            if(settlementAuditInformation!=null){
                total = total.add(settlementAuditInformation.getAmountOutsourcing());
            }
        }
        List<TrackAuditInfo> totalexpenditure6 = trackAuditInfoDao.totalexpenditure(costVo2);
        for (TrackAuditInfo trackAuditInfo : totalexpenditure6) {
            if(trackAuditInfo!=null){
                total = total.add(trackAuditInfo.getOutsourceMoney());
            }
        }

        List<AchievementsInfo> totalexpenditure7 = achievementsInfoMapper.totalexpenditure(costVo2);
        for (AchievementsInfo achievementsInfo : totalexpenditure7) {
            if(achievementsInfo!=null){
                total = total.add(new BigDecimal(achievementsInfo.getDesginAchievements()));
                total = total.add(new BigDecimal(achievementsInfo.getBudgetAchievements()));
                total = total.add(new BigDecimal(achievementsInfo.getUpsubmitAchievements()));
                total = total.add(new BigDecimal(achievementsInfo.getDownsubmitAchievements()));
                total = total.add(new BigDecimal(achievementsInfo.getTruckAchievements()));
            }
        }
        return total;
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
        for (CostVo3 costVo3 : costVo3s) {
            Integer total = costVo3.getBudgetingCount()+
            costVo3.getDesginStatus()+
            costVo3.getProgressPaymentInformation()+
            costVo3.getSettleAccountsCount()+
            costVo3.getVisaApplyChangeInformationCount()+
            costVo3.getTrackAuditInfoCount();
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
        costVo2.setYear(year+"");
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
            total = costVo3.getBudgetingCount()+
                    costVo3.getDesginStatus()+
                    costVo3.getProgressPaymentInformation()+
                    costVo3.getSettleAccountsCount()+
                    costVo3.getVisaApplyChangeInformationCount()+
                    costVo3.getTrackAuditInfoCount();
        }
        return total;
    }

    /**
     * 获取该项目当前月的个数
     * @param costVo2
     */
    public Integer prjectCensusMonth(CostVo2 costVo2){
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int mouth = now.getMonth().getValue();
        //当前一年时间
        costVo2.setYear(year+"");
        costVo2.setMonth(mouth+"");
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
            total = costVo3.getBudgetingCount()+
                    costVo3.getDesginStatus()+
                    costVo3.getProgressPaymentInformation()+
                    costVo3.getSettleAccountsCount()+
                    costVo3.getVisaApplyChangeInformationCount()+
                    costVo3.getTrackAuditInfoCount();
        }
        return total;
    }

    /**
     * 获取该项目当前上个月的个数
     * @param costVo2
     */
    public Integer lastPrjectCensusMonth(CostVo2 costVo2){
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int mouth = now.getMonth().getValue();
        if(mouth == 1){
            //如果当前月份为1 则去年12月
            costVo2.setYear(year-1+"");
            costVo2.setMonth("12");
        }else{
            costVo2.setYear(year+"");
            costVo2.setMonth(mouth-1+"");
        }
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
            total = costVo3.getBudgetingCount()+
                    costVo3.getDesginStatus()+
                    costVo3.getProgressPaymentInformation()+
                    costVo3.getSettleAccountsCount()+
                    costVo3.getVisaApplyChangeInformationCount()+
                    costVo3.getTrackAuditInfoCount();
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
        costVo2.setYear(year-1+"");
        List<CostVo3> costVo3s = projectMapper.prjectCensus(costVo2);
        Integer total = 0;
        for (CostVo3 costVo3 : costVo3s) {
            total = costVo3.getBudgetingCount()+
                    costVo3.getDesginStatus()+
                    costVo3.getProgressPaymentInformation()+
                    costVo3.getSettleAccountsCount()+
                    costVo3.getVisaApplyChangeInformationCount()+
                    costVo3.getTrackAuditInfoCount();
        }
        return total;
    }

    public Integer prjectCensusRast(Integer A,Integer B){
        return (B-A)/B*100;
    }
}

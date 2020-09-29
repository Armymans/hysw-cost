package net.zlw.cloud.designProject.service;


import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
    private BudgetingMapper budgetingMapper;

    @Resource
    private LastSettlementReviewMapper lastSettlementReviewMapper;

    @Resource
    private SettlementAuditInformationMapper settlementAuditInformationMapper;

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
        //如果造价金额低于30000万 则收费400
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
        if(money<1000){
            money = 1000.00;
        }
        return money;
    }

    /**
     * 吴江下家基本费
     * @param cost
     * @return
     */
    public Double wujiangSettlementAuditInformationChargeBase(BigDecimal cost){
        //如果造价金额低于50000万 则收费400
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
        Double mprovement1 = baseMoney*0.1+SubtractMoney*0.2;
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
}

package net.zlw.cloud.budgetTask.domain.vo;

import lombok.Data;
import net.zlw.cloud.budgetTask.domain.CostMeansList;
import net.zlw.cloud.budgetTask.domain.LabelMeansList;
import net.zlw.cloud.budgetTask.domain.TotalMeansList;

import java.util.List;


@Data
public class BudgetVo {
    private String id;
    private String baseProjectId;
    private String applicationNum;
    private String amountCost;
    private String budgetingPeople;
    private String addedTaxAmount;
    private String budgetingTime;
    private String distinct;
    private String costExamineResult;
    private String costExamineOpinion;
    private String costExaminer;
    private String costExamineTime;
    private String costBy;
    private String costPreparationTime;
    private String totalCostAmount;
    private String totalVatAmount;
    private String outsourcingCostAmount;
    private String sourcingCost;
    private String otherExpensesOne;
    private String otherExpensesTwo;
    private String otherExpensesThree;
    private String costControlRemark;
    private String priceControlNumber;
    private String totalPriceControlLabel;
    private String labelVatAmount;
    private String priceControlCompilers;
    private String priceControlTime;
    private String priceControlRemark;
    private String priceExamineResult;
    private String priceExamineOpinion;
    private String priceExaminer;
    private String priceExamineTime;
    private String status;

    private List<CostMeansList> costMeansList;
    private List<LabelMeansList> labelMeansList;
    private List<TotalMeansList> totalMeansList;

}

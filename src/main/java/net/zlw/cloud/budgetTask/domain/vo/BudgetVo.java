package net.zlw.cloud.budgetTask.domain.vo;

import lombok.Data;
import net.zlw.cloud.budgetTask.domain.CostMeansList;
import net.zlw.cloud.budgetTask.domain.TotalMeansList;

import java.util.List;

/**
    * @Author sjf
    * @Description //预算封装类
    * @Date 18:04 2020/11/6
    * @Param
    * @return
 **/
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
    private String costExamine;
    private String costBy;
    private String costPreparationTime;
    private String totalCostAmount;
    private String outsourcingCostAmount;
    private String sourcingCost;
    private String otherExpensesOne;
    private String otherExpensesTwo;
    private String otherExpensesThree;
    private String priceControlRemark;

    private List<CostMeansList> costMeansList;
//    private List<LabelMeansList> labelMeansList;
    private List<TotalMeansList> totalMeansList;

}

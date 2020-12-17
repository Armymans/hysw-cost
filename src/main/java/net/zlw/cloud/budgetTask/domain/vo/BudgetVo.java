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
    private String base_broject_id;
    private String application_num;
    private String amount_cost;
    private String budgeting_people;
    private String added_tax_amount;
    private String budgeting_time;
    private String distinct;
    private String cost_examine_result;
    private String cost_examine_opinion;
    private String cost_examine;
    private String cost_by;
    private String cost_preparation_time;
    private String total_cost_amount;
    private String outsourcing_cost_amount;
    private String sourcing_cost;
    private String other_expenses_one;
    private String other_expenses_two;
    private String other_expenses_three;
    private String price_control_remark;

    private List<CostMeansList> CostMeansList;
//    private List<LabelMeansList> labelMeansList;
    private List<TotalMeansList> TotalMeansList;

}

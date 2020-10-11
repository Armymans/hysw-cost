package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.progressPayment.model.BaseProject;

import java.util.ArrayList;
import java.util.List;

@Data
public class UnionQueryVo {
    private List<String> codeAll = new ArrayList<>();
    private BaseProject baseProject;
    private BudgetingVo budgeting;

}

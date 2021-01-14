package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JbBudgetVoF {

    @JsonProperty("BudgetVo")
    private JbBudgetVo BudgetVo;
    @JsonProperty("Account")
    private String Account;
}

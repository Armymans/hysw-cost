package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjBudgetVoA {

    @JsonProperty("BudgetVo")
    private BudgetVo BudgetVo;
    @JsonProperty("Account")
    private String Account;
}

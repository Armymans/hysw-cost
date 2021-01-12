package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjBudgetVoF {

    @JsonProperty("BudgetVo")
    private WjBudgetVo BudgetVo;

    @JsonProperty("Account")
    private String Account;
}

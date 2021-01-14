package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AmountVo {
    @JsonProperty("Account")
    private String Account;
    private String project_id;
    private String budget_amount;

}

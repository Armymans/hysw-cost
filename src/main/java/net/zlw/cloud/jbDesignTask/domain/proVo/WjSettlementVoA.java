package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjSettlementVoA {

    @JsonProperty("Account")
    private String Account;
    @JsonProperty("SettlementVo")
    private SettlementVo SettlementVo;

}

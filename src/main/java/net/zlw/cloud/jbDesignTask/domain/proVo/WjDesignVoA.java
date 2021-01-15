package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjDesignVoA {

    @JsonProperty("DesignVo")
    private DesignVo DesignVo;
    @JsonProperty("Account")
    private String Account;
}

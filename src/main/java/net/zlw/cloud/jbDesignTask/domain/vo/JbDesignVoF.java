package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JbDesignVoF {

    @JsonProperty("DesignVo")
    private JbDesignVo DesignVo;
    @JsonProperty("Account")
    private String Account;
}

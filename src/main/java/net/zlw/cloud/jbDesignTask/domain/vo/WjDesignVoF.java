package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjDesignVoF {

    @JsonProperty("DesignVo")
    private WjDesignVo DesignVo;
    @JsonProperty("Account")
    private String Account;


}

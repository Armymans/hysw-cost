package net.zlw.cloud.jbDesignTask.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CEAVo {

    @JsonProperty("Account")
    private String Account;
    private String project_id;
    private String cea;
}

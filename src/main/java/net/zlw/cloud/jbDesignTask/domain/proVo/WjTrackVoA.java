package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WjTrackVoA {

    @JsonProperty("Account")
    private String Account;
    @JsonProperty("TrackVo")
    private TrackVo TrackVo;
}

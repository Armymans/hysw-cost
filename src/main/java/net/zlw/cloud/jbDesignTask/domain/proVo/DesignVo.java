package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DesignVo {

    private String id;
    private String base_project_id;
    private String apply_by;
    private String apply_dept;
    private String apply_time;
    private String project_name;
    private String surveyor;
    private String survey_time;
    private String address;
    private String survey_remark;
    private String scene_time;
    private String participants;
    private String remark;
    private String annual_design_uti;
    private String design_util;
    private String free_type;
    private String design_rate;
    private String single_design_cost;
    private String examine_dept;
    @JsonProperty("OpinionsList")
    private List<OpinionsList> OpinionsList;
    @JsonProperty("ScenePhotosList")
    private List<ScenePhotosList> ScenePhotosList;
    @JsonProperty("DesignDrawingsList")
    private List<DesignDrawingsList> DesignDrawingsList;
}

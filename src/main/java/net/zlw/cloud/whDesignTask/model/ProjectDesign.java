package net.zlw.cloud.whDesignTask.model;

import lombok.Data;

/**
 * @Classname ProjectDesign
 * @Description TODO
 * @Date 2020/10/29 9:58
 * @Created sjf
 */
@Data
public class ProjectDesign {


    private String id;
    private String base_project_id;
    private String project_drawing;
    private String project_file_name;
    private String project_up_time;
    private String project_engineering_design_notes;
    private String project_uploaded_by;
}

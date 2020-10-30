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
    private String baseProjectId;
    private String projectDrawing;
    private String projectFileName;
    private String projectUpTime;
    private String projectEngineeringDesignNotes;
    private String projectUploadedBy;
}

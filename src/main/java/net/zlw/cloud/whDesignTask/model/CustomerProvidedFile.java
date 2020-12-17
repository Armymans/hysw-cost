package net.zlw.cloud.whDesignTask.model;

import lombok.Data;

/**
 * @Classname CustomerProvidedFile
 * @Description TODO
 * @Date 2020/10/29 10:03
 * @Created sjf
 */
@Data
public class CustomerProvidedFile {

    private String id;
    private String base_project_id;
    private String customer_provided_name;
    private String customer_provided_file_name;
    private String customer_provided_time;
    private String customer_provided_by;
    private String customer_provided_drawing;
}

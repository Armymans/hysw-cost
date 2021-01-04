package net.zlw.cloud.jbDesignTask.domain.vo;

import lombok.Data;

/***
 * 文件公用对象
 */
@Data
public class FileInfos {

    private String id;
    private String project_id;
    private String type;
    private String opinions_file_name;
    private String opinions_up_time;
    private String opinions_up_by;
    private String opinions_link;

}

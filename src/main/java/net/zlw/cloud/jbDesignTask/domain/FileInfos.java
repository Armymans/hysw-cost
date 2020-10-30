package net.zlw.cloud.jbDesignTask.domain;

import lombok.Data;

/***
 * 文件公用对象
 */
@Data
public class FileInfos {

    private String id;
    private String projectId;
    private String opinionsFileName;
    private String opinionsUpTime;
    private String opinionsUpBy;
    private String opinionsLink;

}

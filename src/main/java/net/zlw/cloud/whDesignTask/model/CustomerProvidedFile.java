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
    private String baseProjectId;
    private String customerProvidedName;
    private String customerProvidedFileName;
    private String customerProvidedTime;
    private String customerProvidedBy;
    private String customerProvidedDrawing;
}

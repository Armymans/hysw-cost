package net.zlw.cloud.designTask.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

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

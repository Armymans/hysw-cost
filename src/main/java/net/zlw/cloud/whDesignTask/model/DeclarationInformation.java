package net.zlw.cloud.whDesignTask.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname DeclarationInformation
 * @Description TODO
 * @Date 2020/10/29 10:07
 * @Created sjf
 */
@Data
@Table(name = "declaration_information")
public class DeclarationInformation {

    @Column(name = "id")
    private String id;
    @Column(name = "base_project_id")
    private String base_project_id;
    @Column(name = "declared_diameter")
    private String declared_diameter;
    @Column(name = "user_of_life")
    private String user_of_life;
    @Column(name = "water_use")
    private String water_use;
    @Column(name = "declaration_count")
    private String declaration_count;
    @Column(name = "setting_area")
    private String setting_area;
    @Column(name = "declaration_type")
    private String declaration_type;
}

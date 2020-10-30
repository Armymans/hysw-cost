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
    private String baseProjectId;
    @Column(name = "declared_diameter")
    private String declaredDiameter;
    @Column(name = "user_of_life")
    private String userOfLife;
    @Column(name = "water_use")
    private String waterUse;
    @Column(name = "declaration_count")
    private String declarationCount;
    @Column(name = "setting_area")
    private String settingArea;
    @Column(name = "declaration_type")
    private String declarationType;
}

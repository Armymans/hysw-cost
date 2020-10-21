package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "building_project")
@Data
public class BuildingProject {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识
    @Column(name = "building_project_name")
    private String buildingProjectName;
    @Column(name = "building_project_code")
    private String buildingProjectCode;
    @Column(name = "building_unit")
    private String buildingUnit;
    @Column(name = "supervisor_unit")
    private String supervisorUnit;
    @Column(name = "construction_units")
    private String constructionUnits;
    @Column(name = "design_units")
    private String designUnits;
    @Column(name = "project_type")
    private String projectType;
    @Column(name = "project_nature")
    private String project_nature;
}

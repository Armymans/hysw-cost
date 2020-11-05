package net.zlw.cloud.buildingProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description 建设项目
 * @Date 2020/10/11 10:40
 **/
@Data
@Table(name = "building_project")
public class BuildingProject {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "building_project_name")
    private String buildingProjectName;

    @Column(name = "building_project_code")
    private String buildingProjectCode;

    @Column(name = "building_unit")
    private String buildingUnit;

    @Column(name = "supervisor_unit")
    private String supervisorUnit;

    @Column(name = "design_units")
    private String designUnits;

    @Column(name = "project_type")
    private String projectType;

    @Column(name = "project_nature")
    private String projectNature;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "cost_amount")
    private String costAmount;

    @Column(name = "water_type")
    private String waterType;

    @Column(name = "client_nature")
    private String clientNature;

    @Column(name = "water_factory")
    private String waterFactory;

    @Column(name = "project_site")
    private String projectSite;

    @Column(name = "construction_units")
    private String constructionUnits;

    @Column(name = "declare")
    private String declare;

    @Column(name = "contract_start_time")
    private String contractStartTime;

    @Column(name = "contract_end_time")
    private String contractEndTime;

    @Column(name = "actual_start_time")
    private String actualStartTime;

    @Column(name = "actual_end_time")
    private String actualEndTime;

    @Column(name = "compile_date")
    private String compileDate;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "or_submit")
    private String orSubmit;

    @Column(name = "merge_flag")
    private String mergeFlag;

    @Column(name = "accomplish_status")
    private String accomplishStatus;

}

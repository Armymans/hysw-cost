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

    /**
     *   `design_units` varchar(120) DEFAULT NULL COMMENT '设计单位',
     *   `project_type` varchar(120) DEFAULT NULL COMMENT '项目类别',
     *   `project_nature` varchar(120) DEFAULT NULL COMMENT '项目性质',
     *   `client_name` varchar(120) DEFAULT NULL COMMENT '客户名称',
     *   `cost_amount` decimal(60,2) DEFAULT NULL COMMENT '造价金额',
     *   `water_type` varchar(120) DEFAULT NULL COMMENT '供水类型',
     *   `client_nature` varchar(120) DEFAULT NULL COMMENT '客户属性',
     *   `water_factory` varchar(120) DEFAULT NULL COMMENT '供水所',
     *   `project_site` varchar(120) DEFAULT NULL COMMENT '项目地址',
     *   `declare` varchar(255) DEFAULT NULL COMMENT '申报内容',
     *   `contract_start_time` varchar(20) DEFAULT NULL COMMENT '合同开工时间',
     *   `contract_end_time` varchar(20) DEFAULT NULL COMMENT '合同竣工时间',
     *   `actual_start_time` varchar(20) DEFAULT NULL COMMENT '实际开工时间',
     *   `actual_end_time` varchar(20) DEFAULT NULL COMMENT '实际竣工时间',
     *   `compile date` varchar(20) DEFAULT NULL COMMENT '编制日期',
     *   `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人Id',
     *   `company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司Id',
     *   `status` varchar(1) DEFAULT NULL COMMENT '数据状态',
     *   `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
     *   `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
     *   `or_submit` varchar(1) DEFAULT NULL COMMENT '是否保存提交 0保存 1提交',
     **/


}

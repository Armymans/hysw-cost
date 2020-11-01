package net.zlw.cloud.demo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/30 17:58
 **/
@Data
@Table(name = "final_report")
public class FinalReport {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "job_number")
    private String jobNumber;

    @Column(name = "project_client")
    private String projectClient;

    @Column(name = "class_service")
    private String classService;

    @Column(name = "project_full_name")
    private String projectFullName;

    @Column(name = "operational_period")
    private String operationalPeriod;

    @Column(name = "project_id")
    private String projectId;


}

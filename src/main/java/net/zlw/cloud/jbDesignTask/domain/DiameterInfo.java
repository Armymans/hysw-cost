package net.zlw.cloud.jbDesignTask.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "diameter_info")
public class DiameterInfo {

    @Column(name = "id")
    private String id;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "diameter_meter")
    private String diameterMeter;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "status")
    private String status;

}

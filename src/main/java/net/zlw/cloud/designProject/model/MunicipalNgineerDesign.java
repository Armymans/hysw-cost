package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "municipal_ngineer_design")
@Data
public class MunicipalNgineerDesign {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "project_cost")
    private Integer projectCost;

    @Column(name = "design_basic_cost")
    private Double designBasicCost;

    @Column(name = "status")
    private String status;
}

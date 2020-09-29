package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "anhui_budgeting_charge")
@Data
public class AnhuiBudgetingCharge {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "project_cost")
    private Integer projectCost;

    @Column(name = "rate_cost")
    private Double rateCost;

    @Column(name = "rate_controller")
    private Double rateController;

    @Column(name = "status")
    private String status;
}

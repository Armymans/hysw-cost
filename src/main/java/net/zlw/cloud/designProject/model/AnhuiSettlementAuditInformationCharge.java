package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "anhui_settlement_audit_information_charge")
@Data
public class AnhuiSettlementAuditInformationCharge {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "project_cost")
    private Integer projectCost;

    @Column(name = "rate_cost")
    private Double rateCost;

    @Column(name = "status")
    private String status;
}

package net.zlw.cloud.budgeting.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "cost_preparation")
@Data
public class CostPreparation implements Serializable {
    @Id
    private String id;
    @Column(name = "cost_total_amount")
    private BigDecimal costTotalAmount;
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    @Column(name = "total_package_material")
    private BigDecimal totalPackageMaterial;
    @Column(name = "outsourcing_cost_amount")
    private BigDecimal outsourcingCostAmount;
    @Column(name = "other_cost_1")
    private BigDecimal otherCost1;
    @Column(name = "other_cost_2")
    private BigDecimal otherCost2;
    @Column(name = "other_cost_3")
    private BigDecimal otherCost3;
    @Column(name = "cost_together")
    private String costTogether;
    @Column(name = "receiving_time")
    private String receivingTime;
    @Column(name = "cost_preparation_time")
    private String costPreparationTime;
    private String remarkes;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "budgeting_id")
    private String budgetingId;

    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String delFlag;

}

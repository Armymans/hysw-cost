package net.zlw.cloud.excelLook.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class QuantitiesPartialWorks {
    @Id
    private String id;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_description")
    private String projectDescription;
    @Column(name = "measuring_unit")
    private String measuringUnit;
    @Column(name = "quantities")
    private String quantities;
    @Column(name = "comprehensive_unit_price")
    private BigDecimal comprehensiveUnitPrice;
    @Column(name = "and_price")
    private BigDecimal andPrice;
    @Column(name = "rate_artificial_cost")
    private BigDecimal rateArtificialCost;
    @Column(name = "fixed_mechanical_fee")
    private BigDecimal fixedMechanicalFee;
    @Column(name = "temporary_valuation")
    private BigDecimal temporaryValuation;
    @Column(name = "budgeting_id")
    private String budgetingId;
    @Column(name = "item_name")
    private String itemName;




}

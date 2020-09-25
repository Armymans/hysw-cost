package net.zlw.cloud.budgeting.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "budgeting")
@Data
public class Budgeting implements Serializable {
    @Id
    private String id;
    @Column(name = "amount_cost")
    private BigDecimal amountCost;
    @Column(name = "budgeting_people")
    private String budgetingPeople;
    @Column(name = "added_tax_amount")
    private BigDecimal addedTaxAmount;
    @Column(name = "budgeting_time")
    private String budgetingTime;
    private String outsourcing;
    @Column(name = "name_of_cost_unit")
    private String nameOfCostUnit;
    private String contact;
    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(name = "amount_outsourcing")
    private BigDecimal amountOutsourcing;
    @Column(name = "receipt_time")
    private String receiptTime;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "cost_preparation_id")
    private String costPreparationId;
    @Column(name = "very_establishment_id")
    private String veryEstablishmentId;
    @Column(name = "survey_information_id")
    private String surveyInformationId;
    private String remarkes;
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

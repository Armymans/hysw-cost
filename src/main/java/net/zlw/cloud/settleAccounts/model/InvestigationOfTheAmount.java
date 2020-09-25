package net.zlw.cloud.settleAccounts.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "investigation_of_the_amount")
@Data
public class InvestigationOfTheAmount implements Serializable {

    @Id
    private String id;
    @Column(name = "survey_date")
    private String surveyDate;
    @Column(name = "investigation_personnel")
    private String investigationPersonnel;
    @Column(name = "survey_briefly")
    private String surveyBriefly;
    @Column(name = "unbalanced_quotation_adjustment")
    private BigDecimal unbalancedQuotationAdjustment;
    @Column(name = "punish_amount")
    private BigDecimal punishAmount;
    @Column(name = "outbound_amount")
    private BigDecimal outboundAmount;
    private String remarkes;
    @Column(name = "material_difference_amount")
    private BigDecimal materialDifferenceAmount;
    @Column(name = "maintenance_project_information")
    private String maintenanceProjectInformation;
    @Column(name = "base_project_id")
    private String baseProjectId;
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

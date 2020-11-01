package net.zlw.cloud.excelLook.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "settlement_report_text")
public class SettlementReportText {

    @Id
    private String id;
    @Column(name = "company")
    private String company;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "development_organization")
    private String developmentOrganization;
    @Column(name = "construction_content")
    private String constructionContent;
    @Column(name = "cost_construction")
    private String costConstruction;
    @Column(name = "cost_examination")
    private String costExamination;
    @Column(name = "reduction_amount")
    private String reductionAmount;
    @Column(name = "material_amount")
    private String materialAmount;
    @Column(name = "examination_amount")
    private String examinationAmount;
    @Column(name = "subtract_rate")
    private String subtractRate;
    @Column(name = "foreign_key")
    private String foreignKey;
    @Column(name = "auditContent")
    private String auditContent;
    @Column(name = "cause")
    private String cause;
}

package net.zlw.cloud.costAssessmentDesign.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by xulei on 2020/9/18.
 */
@Table(name = "cost_assessment_design")
@Data
public class CostAssessmentDesing {
    @Id
    @Column(name = "id")
    private Integer  id;

    @Column(name = "assessment_name")
    private String  assessmentName;

    @Column(name = "calculation_formula")
    private String  calculationFormula;

    @Column(name = "remarkes")
    private String  remarkes;


    @Column(name = "create_time")
    private String  createTime;

    @Column(name = "update_time")
    private String  updateTime;

    @Column(name = "founder_id")
    private String  founderId;

    @Column(name = "founder_company_id")
    private String  founderCompanyId;

    @Column(name = "del_flag")
    private String  delFlag;
    @Transient
    private String parame1;
    @Transient
    private String parame2;
}

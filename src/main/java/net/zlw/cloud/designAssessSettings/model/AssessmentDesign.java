package net.zlw.cloud.designAssessSettings.model;

import javax.persistence.*;

/**
 * @Author Armyman
 * @Description 设计-考核设计
 * @Date 2020/9/18 20:57
 **/
@Entity
@Table(name = "assessment_design")
public class AssessmentDesign {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "assessment_name")
    private String assessmentName;

    @Column(name = "calculation_formula")
    private String calculationFormula;

    @Column(name = "remarkes")
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

    @Transient
    private String parame1;

    @Transient
    private String parame2;

    @Transient
    private String parame3;

    @Transient
    private String parame4;

    @Transient
    private String parame5;

    @Transient
    private String parame6;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getCalculationFormula() {
        return calculationFormula;
    }

    public void setCalculationFormula(String calculationFormula) {
        this.calculationFormula = calculationFormula;
    }

    public String getRemarkes() {
        return remarkes;
    }

    public void setRemarkes(String remarkes) {
        this.remarkes = remarkes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getFounderCompanyId() {
        return founderCompanyId;
    }

    public void setFounderCompanyId(String founderCompanyId) {
        this.founderCompanyId = founderCompanyId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParame1() {
        return parame1;
    }

    public void setParame1(String parame1) {
        this.parame1 = parame1;
    }

    public String getParame2() {
        return parame2;
    }

    public void setParame2(String parame2) {
        this.parame2 = parame2;
    }

    public String getParame3() {
        return parame3;
    }

    public void setParame3(String parame3) {
        this.parame3 = parame3;
    }

    public String getParame4() {
        return parame4;
    }

    public void setParame4(String parame4) {
        this.parame4 = parame4;
    }

    public String getParame5() {
        return parame5;
    }

    public void setParame5(String parame5) {
        this.parame5 = parame5;
    }

    public String getParame6() {
        return parame6;
    }

    public void setParame6(String parame6) {
        this.parame6 = parame6;
    }
}

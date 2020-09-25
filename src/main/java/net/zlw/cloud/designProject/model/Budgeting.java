package net.zlw.cloud.designProject.model;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "budgeting")
public class Budgeting{
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "amount_cost")
    private BigDecimal amountCost; //造价金额

    @Column(name = "budgeting_people")
    private String budgeting_people;

    @Column(name = "added_tax_amount")
    private String addedTaxAmount;

    @Column(name = "budgeting_time")
    private String budgetingTime;

    @Column(name = "amount_outsourcing")
    private String amountOutsourcing;

    @Column(name = "receipt_time")
    private String receiptTime;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "remarkes")
    private String remarkes;

    @Column(name = "contact_phone")
    private String contactPhone;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmountCost() {
        return amountCost;
    }

    public void setAmountCost(BigDecimal amountCost) {
        this.amountCost = amountCost;
    }

    public String getBudgeting_people() {
        return budgeting_people;
    }

    public void setBudgeting_people(String budgeting_people) {
        this.budgeting_people = budgeting_people;
    }

    public String getAddedTaxAmount() {
        return addedTaxAmount;
    }

    public void setAddedTaxAmount(String addedTaxAmount) {
        this.addedTaxAmount = addedTaxAmount;
    }

    public String getBudgetingTime() {
        return budgetingTime;
    }

    public void setBudgetingTime(String budgetingTime) {
        this.budgetingTime = budgetingTime;
    }

    public String getAmountOutsourcing() {
        return amountOutsourcing;
    }

    public void setAmountOutsourcing(String amountOutsourcing) {
        this.amountOutsourcing = amountOutsourcing;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getBaseProjectId() {
        return baseProjectId;
    }

    public void setBaseProjectId(String baseProjectId) {
        this.baseProjectId = baseProjectId;
    }

    public String getRemarkes() {
        return remarkes;
    }

    public void setRemarkes(String remarkes) {
        this.remarkes = remarkes;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
}

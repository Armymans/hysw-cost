package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @Author dell
 * @Date 2020/9/27 15:47
 * @Version 1.0
 */
@Data
public class MaintenanceProjectInformationVo {
    private String id;
    //维修项目编号
    private String maintenanceItemId;
    //维修项目名称
    private String maintenanceItemName;
    //维修项目类型
    private String maintenanceItemType;
    // 报送部门
    private String submittedDepartment;

    /**
     * 报送时间
     */
    private String submitTime;

    /**
     * 编制人
     */
    private String preparePeople;

    /**
     * 项目地址
     */
    private String projectAddress;

    /**
     * 施工单位Id
     */
    private String constructionUnitId;

    /**
     * 送审金额
     */
    private BigDecimal reviewAmount;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 备注
     */

    private String remarkes;

    /**
     * 创建时间
     */

    private String createTime;

    /**
     * 修改时间
     */

    private String updateTime;

    /**
     * 创建人id
     */

    private String founderId;

    /**
     * 创建人公司id
     */

    private String founderCompanyId;

    /**
     *  状态 0,正常 1,删除
     */

    private String delFlag;

    /**
     * 变更状态1待审核2处理中3未通过4待确认5已完成
     */
    private String type;


    private String constructionUnitName;


    private String memberName;

    // 勘察信息
    private SurveyInformation surveyInformation;

    // 结算审核信息
    private SettlementAuditInformation settlementAuditInformation;

    //审核信息
    private AuditInfo auditInfo;

    public MaintenanceProjectInformationVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintenanceItemId() {
        return maintenanceItemId;
    }

    public void setMaintenanceItemId(String maintenanceItemId) {
        this.maintenanceItemId = maintenanceItemId;
    }

    public String getMaintenanceItemName() {
        return maintenanceItemName;
    }

    public void setMaintenanceItemName(String maintenanceItemName) {
        this.maintenanceItemName = maintenanceItemName;
    }

    public String getMaintenanceItemType() {
        return maintenanceItemType;
    }

    public void setMaintenanceItemType(String maintenanceItemType) {
        this.maintenanceItemType = maintenanceItemType;
    }

    public String getSubmittedDepartment() {
        return submittedDepartment;
    }

    public void setSubmittedDepartment(String submittedDepartment) {
        this.submittedDepartment = submittedDepartment;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getPreparePeople() {
        return preparePeople;
    }

    public void setPreparePeople(String preparePeople) {
        this.preparePeople = preparePeople;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getConstructionUnitId() {
        return constructionUnitId;
    }

    public void setConstructionUnitId(String constructionUnitId) {
        this.constructionUnitId = constructionUnitId;
    }

    public BigDecimal getReviewAmount() {
        return reviewAmount;
    }

    public void setReviewAmount(BigDecimal reviewAmount) {
        this.reviewAmount = reviewAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConstructionUnitName() {
        return constructionUnitName;
    }

    public void setConstructionUnitName(String constructionUnitName) {
        this.constructionUnitName = constructionUnitName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}

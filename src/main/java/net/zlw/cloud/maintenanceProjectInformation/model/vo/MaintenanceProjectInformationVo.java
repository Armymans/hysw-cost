package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.clearProject.model.BaseProject;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.model.InvestigationOfTheAmount;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    //报送时间
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
    // 勘察日期
    private String surveyDate;
    //勘察人员
    private String investigationPersonnel;
    //勘察简述
    private String surveyBriefly;

    // 结算审核信息
//    private SettlementAuditInformation settlementAuditInformation;
    private BigDecimal authorizedNumber;
    private BigDecimal subtractTheNumber;
    private BigDecimal nuclearNumber;
    // 核减率
    private BigDecimal subtractRate;
    private BigDecimal contractAmount;
    private String contractRemarkes;
    private String preparePeople2;
    private String outsourcing;
    private String nameOfTheCost;
    private String contact;
    private String contactPhone;
    private BigDecimal amountOutsourcing;
    private String compileTime;
    private String remark;

    // 勘探金额
    private BigDecimal unbalancedQuotationAdjustment;
    private BigDecimal punishAmount;
    private BigDecimal outboundAmount;
    private BigDecimal materialDifferenceAmount;
    //审核信息
//    private AuditInfo auditInfo;
    private String auditResult;
    private String auditOpinion;
    private String auditType;

    private List<AuditInfo> auditInfos;

}

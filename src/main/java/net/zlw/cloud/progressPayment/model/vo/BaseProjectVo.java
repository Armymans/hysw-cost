package net.zlw.cloud.progressPayment.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProjectVo {
    //进度款id
    private String id;
    private String applicationNum;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String projectId;
    private String district;
    private String designCategory;
    private String constructionUnit;
    private String contacts;
    private String contactNumber;
    private String customerName;
    private String subject;
    private String customerPhone;
    private String constructionOrganization;
    private String projectNature;
    private String projectCategory;
    private String waterAddress;
    private String waterSupplyType;
    private String thisDeclaration;
    private String agent;
    private String agentPhone;
    private String applicationDate;
    private String businessLocation;
    private String businessTypes;
    private String aB;
    private String waterUse;
    private String fireTableSize;
    private String classificationCaliber;
    private String waterMeterDiameter;
    private String site;
    private String systemNumber;
    private String proposer;
    private String applicationNumber;
    private String desginStatus;
    private String budgetStatus;
    private String trackStatus;
    private String visaStatus;
    private String progressPaymentStatus;
    private String settleAccountsStatus;
    private String status;
    private String buildingProjectId;
    private String virtualCode;
    private String mergeFlag;
    private String projectFlow;
    private String shouldBe;
    private String whetherAccount;
    private String auditNumber;

    private String remarkes;
    private String baseProjectId;
    private BigDecimal currentPaymentInformation;
    private String cumulativePaymentTimes;
    private String currentPaymentRatio;
    private String currentPeriodAccording;
    private BigDecimal contractAmount;
    private String projectType;
    private String receivingTime;
    private String compileTime;
    private String outsourcing;
    private String nameOfCostUnit;
    private String contact;
    private String contactPhone;
    private BigDecimal amountOutsourcing;
    private String situation;
    private String remarkes1;
    private BigDecimal totalPaymentAmount;
    private BigDecimal cumulativeNumberPayment;
    private String accumulativePaymentProportion;
    private String auditorId;
    private String auditType;

    private String setManagementTable;

    //上传文件用的 key,type
    private String key;
    private String type;
    private String type1;

    private String baseId;

    private List<AuditInfo> auditInfos;

    private AuditInfo auditInfo;

    public void setManagementTable(String managementTable) {
    }
}

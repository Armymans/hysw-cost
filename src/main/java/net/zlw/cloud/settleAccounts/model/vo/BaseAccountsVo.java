package net.zlw.cloud.settleAccounts.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.settleAccounts.model.SettlementInfo;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAccountsVo {
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
    private String surveyDate;
    private String investigationPersonnel;
    private String surveyBriefly;
    private BigDecimal unbalancedQuotationAdjustment;
    private BigDecimal punishAmount;
    private BigDecimal outboundAmount;
    private String remarkes;
    private BigDecimal materialDifferenceAmount;
    private String maintenanceProjectInformation;
    private BigDecimal reviewNumber;
    private String preparePeople;
    private String projectTime;
    private String projectPeople;
    private BigDecimal contractAmount;
    private String contractRemarkes;
    private String outsourcing;
    private String nameOfTheCost;
    private String contact;
    private String contactPhone;
    private BigDecimal amountOutsourcing;
    private BigDecimal authorizedNumber;
    private BigDecimal subtractTheNumber;
    private BigDecimal nuclearNNumber;
    private String downRemarkes;
    private BigDecimal downContractAmount;
    private String downContractRemarkes;
    private String downPreparePeople;
    private String downOutsourcing;
    private String downNameOfTheCost;
    private String downContact;
    private String downContactPhone;
    private BigDecimal downAmountOutsourcing;
    private String settleAccountsStatus;
    private String auditOpinion;
    private String auditorId;
    private SettlementInfo lastSettleinfo;
    private SettlementInfo settleSettleInfo;




}

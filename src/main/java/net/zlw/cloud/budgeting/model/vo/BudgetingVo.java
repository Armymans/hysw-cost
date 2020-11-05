package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.math.BigDecimal;

@Data
public class BudgetingVo {
    private String id;
    private String baseId;
    private String projectNum;
    private BigDecimal amountCost;
    private String budgetingPeople;
    private BigDecimal addedTaxAmount;
    private String budgetingTime;
    private String outsourcing;
    private String nameOfCostUnit;
    private String contact;
    private String contactPhone;
    private BigDecimal amountOutsourcing;
    private String receiptTime;
    private String bremarkes;
    private String surveyDate;
    private String investigationPersonnel;
    private String surveyBriefly;
    private String priceInformationName;
    private String priceInformationNper;
    private BigDecimal costTotalAmount;
    private BigDecimal cVatAmount;
    private BigDecimal totalPackageMaterial;
    private BigDecimal outsourcingCostAmount;
    private BigDecimal otherCost1;
    private BigDecimal otherCost2;
    private BigDecimal otherCost3;
    private String costTogether;
    private String receivingTime;
    private String costPreparationTime;
    private String cRemarkes;
    private BigDecimal biddingPriceControl;
    private BigDecimal vVatAmount;
    private String pricingTogether;
    private String vReceivingTime;
    private String establishmentTime;
    private String vRemarkes;
    private String auditorId;
    private String auditNumber;

    private AuditInfo auditInfo;
}

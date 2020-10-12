package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetingListVo {
    private String id;
    private String baseId;
    private String shouldBe;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String budgetStatus;
    private String district;
    private String waterAddress;
    private String constructionUnit;
    private String projectNature;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String budgetingPeople;
    private String costTogether;
    private String pricingTogether;
    private String outsourcing;
    private String nameOfCostUnit;
    private BigDecimal amountCost;
    private BigDecimal costTotalAmount;
    private BigDecimal biddingPriceControl;
    private String receiptTime;
    private String budgetingTime;
    private String receivingTime;
    private String costPreparationTime;
    private String veryReceivingTime;
    private String establishmentTime;
}

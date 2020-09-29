package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProgressListVo {
    private String id;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String progressPaymentStatus;
    private String district;
    private String waterAddress;
    private String constructionUnit;
    private String projectCategory;
    private String projectNature;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String username;
    private String outsourcing;
    private String nameOfCostUnit;
    private BigDecimal amountCost;
    private BigDecimal contractAmount;
    private BigDecimal totalPaymentAmount;
    private String accumulativePaymentProportion;
    private BigDecimal currentPaymentInformation;
    private String currentPaymentRatio;
    private BigDecimal cumulativeNumberPayment;
    private String receivingTime;
    private String compileTime;
}

package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

@Data
public class PaymentListVo {
    private String id;
    private String baseId;
    private String currentPaymentInformation;
    private String currentPaymentRatio;
    private String currentPeriodAccording;
    private String contractAmount;
    private String projectType;
    private String founderId;
    private String receivingTime;
    private String compileTime;
    private String outsourcing;
    private String nameOfCostUnit;
    private String contact;
    private String contactPhone;
    private String constructionOrganization;
    private String amountOutsourcing;
    private String progressPaymentStatus;
}

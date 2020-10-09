package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

/***
 * 签证变更所选项目封装类
 */
@Data
public class VisaBaseProjectVo {
    private int pageNum;
    private int pageSize;
    private String shouldBe;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String district;
    private String waterAddress;
    private String construction;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String budgetingPeople;
    private String costTogether;
    private String pricingTogether;
    private String amountCost;
    private String costTotalAmount;
    private String biddingPriceControl;
    private String keyWord;
}

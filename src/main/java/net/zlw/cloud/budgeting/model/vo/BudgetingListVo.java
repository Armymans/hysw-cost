package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

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
    //当前处理人
    private String currentHandler;
    //审核人id
    private String auditorId;
    //审核结果
    private String auditResult;
    //是否到账
    private String whetherAccount;
    private BigDecimal amountOutsourcing;
    //创建人
    private String founderId;
    //是否显示到账
    private String showWhether;

    private String isShow; // 判断CEA显隐
    //是否退回
    private String showUnsanctioned;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetingListVo that = (BudgetingListVo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

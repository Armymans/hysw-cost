package net.zlw.cloud.settleAccounts.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountsVo {
    private String id;
    private String ceaNum;
    private String projectNum;
    private String projectName;
    private String settleAccountsStatus;
    private String district;
    private String waterAddress;
    private String constructionUnit;
    private String projectCategory;
    private String projectNature;
    private String designCategory;
    private String waterSupplyType;
    private String customerName;
    private String budgetingPeople;
    private String outsourcing;
    private String nameOfCostUnit;
    private String amountCost;
    private BigDecimal contractAmount;
    private BigDecimal CumulativePayment; //累计支付金额
    private String takeTime;
    private String compileTime;

}

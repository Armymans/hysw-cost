package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneCensus6 {
    private String yearTime;
    private String monthTime;
    private BigDecimal desginAchievements;

    private Integer budgetStatus;
    private Integer progressPaymentStatus;
    private Integer visaStatus;
    private Integer settleAccountsStatus;
    private Integer total;
}

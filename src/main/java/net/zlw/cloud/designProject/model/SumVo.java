package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SumVo {
    private BigDecimal biddingPriceControlSum;
    private BigDecimal desMoneySum;
    private BigDecimal costTotalAmountSum;
    private BigDecimal amountCostAmountSum;
    private BigDecimal outsourceMoneySum;
}

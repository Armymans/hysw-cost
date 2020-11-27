package net.zlw.cloud.progressPayment.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalVo {
    private BigDecimal  totalPaymentAmount;
    private BigDecimal cumulativeNumberPayment;
    private String accumulativePaymentProportion;
}

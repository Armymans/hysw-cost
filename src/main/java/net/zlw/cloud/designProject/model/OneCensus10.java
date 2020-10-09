package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneCensus10 {
    private String yearTime;
    private String monthTime;

    private Integer budCountA;
    private BigDecimal amountCost;
    private BigDecimal costTotalAmountA;

    private Integer budCountB;
    private BigDecimal costTotalAmountB;


    private Integer lastCount;
    private BigDecimal reviewNumber;

    private Integer settCount;
    private BigDecimal sumbitMoney;
    private BigDecimal authorizedNumber;
    private BigDecimal subtractTheNumber;

    private Integer trackCount;
    private BigDecimal contractAmount;
}

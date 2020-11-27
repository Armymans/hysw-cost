package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OneCensus4 {
    private String yearTime;
    private String monthTime;
    private BigDecimal reviewNumber;
    private BigDecimal sumbitMoney;
    private BigDecimal authorizedNumber;
    private BigDecimal subtractTheNumber;
    private String memberName;
    private BigDecimal total;
    private BigDecimal total2;
    private BigDecimal total3;
}

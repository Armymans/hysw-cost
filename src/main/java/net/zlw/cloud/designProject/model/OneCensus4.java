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
}

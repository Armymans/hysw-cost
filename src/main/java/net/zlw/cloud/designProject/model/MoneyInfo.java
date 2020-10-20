package net.zlw.cloud.designProject.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class MoneyInfo {
    //实收展示
    private String costType;
    private String designRate;
    private String oneDesMoney;
    private String revenue;
    private String officialReceipts;
    private String costTime;

    //代收展示
    private BigDecimal collection01;
    private String collection01Time;

    private BigDecimal collection02;
    private String collection02Time;

    private BigDecimal collection03;
    private String collection03Time;

    private BigDecimal collection04;
    private String collection04Time;

    private BigDecimal collection05;
    private String collection05Time;

    private BigDecimal total;
}

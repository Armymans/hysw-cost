package net.zlw.cloud.statisticAnalysis.model;

import lombok.Data;

@Data
public class StatisticAnalysis {
    //本月发放绩效
    private Double PaymentPerformanceThisMonth;
    //本年发放绩效
    private Double CurrentYearPaymentPerformance;
    //折线图
    private String picture;
    //饼状图
    private String pieChart;


}

package net.zlw.cloud.statisticAnalysis.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class StatisticAnalysis {
    //本月发放绩效
    private Double paymentPerformanceThisMonth;
    //同比上月
    private Double comparedWithLastMonth;
    //本年发放绩效
    private Double currentYearPaymentPerformance;
    //同比上年
    private Double comparedWithThePreviousYear;
    //折线图
    private JSONArray picture;
    //饼状图
    private JSONArray pieChart;


}

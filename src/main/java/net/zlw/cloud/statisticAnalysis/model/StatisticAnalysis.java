package net.zlw.cloud.statisticAnalysis.model;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class StatisticAnalysis {
    //本月发放绩效
    private Double PaymentPerformanceThisMonth;
    //同比上月
    private Double ComparedWithLastMonth;
    //本年发放绩效
    private Double CurrentYearPaymentPerformance;
    //同比上年
    private Double ComparedWithThePreviousYear;
    //折线图
    private JSONArray picture;
    //饼状图
    private JSONArray pieChart;


}

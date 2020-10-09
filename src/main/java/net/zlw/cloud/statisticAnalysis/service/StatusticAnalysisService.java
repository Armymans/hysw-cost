package net.zlw.cloud.statisticAnalysis.service;

import com.alibaba.fastjson.JSON;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.designProject.mapper.AchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.LastSettlementReviewMapper;
import net.zlw.cloud.designProject.mapper.SettlementAuditInformationMapper;
import net.zlw.cloud.designProject.model.BaseProject;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.statisticAnalysis.model.PerformanPeople;
import net.zlw.cloud.statisticAnalysis.model.PerformanceAccrualAndSummaryList;
import net.zlw.cloud.statisticAnalysis.model.StatisticAnalysis;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatusticAnalysisService {
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private LastSettlementReviewMapper lastSettlementReviewMapper;
    @Resource
    private SettlementAuditInformationMapper settlementAuditInformationMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private AchievementsInfoMapper achievementsInfoMapper;


    public StatisticAnalysis findAnalysis(pageVo pageVo) {
        List<PerformanceDistributionChart> cost = achievementsInfoMapper.findCostPerformanceDistributionChart(pageVo);

        //本月发放
        Double thisMonthPerform = 0.00;
        //本年发放
        Double thisYearPerform = 0.00;
        ArrayList<PerformanceDistributionChart> thisYearChart = new ArrayList<>();
        //上月发放
        Double lastMonthPerform = 0.00;
        //上年发放
        Double lastYearPerform = 0.00;
        Calendar cal = Calendar.getInstance();
        //本月
        int thisMonth = cal.get(Calendar.MONTH)+1;
        //本年
        int thisYear = cal.get(Calendar.YEAR);
        //上月
        int lastMonth = cal.get(Calendar.MONTH);
        //上年
        int lastYear = cal.get(Calendar.YEAR)-1;
        for (PerformanceDistributionChart performanceDistributionChart : cost) {
            //本年
            if (performanceDistributionChart.getYearTime().equals(thisYear+"")){
                thisYearPerform += performanceDistributionChart.getIssuedDuringMmonth().doubleValue();
                thisYearChart.add(performanceDistributionChart);
            }
            //本月
            if (performanceDistributionChart.getMonthTime().equals(thisMonth+"")){
                thisMonthPerform += performanceDistributionChart.getIssuedDuringMmonth().doubleValue();
            }
            //上月
            if (performanceDistributionChart.getMonthTime().equals(lastMonth+"")){
                lastMonthPerform += performanceDistributionChart.getIssuedDuringMmonth().doubleValue();
            }
            //上年
            if (performanceDistributionChart.getYearTime().equals(lastYear+"")){
                lastYearPerform += performanceDistributionChart.getIssuedDuringMmonth().doubleValue();
            }

        }
        StatisticAnalysis statisticAnalysis = new StatisticAnalysis();

        statisticAnalysis.setPaymentPerformanceThisMonth(thisMonthPerform);
        statisticAnalysis.setCurrentYearPaymentPerformance(thisYearPerform);
        //同比上月
        double v = (thisMonthPerform - lastMonthPerform) / lastMonthPerform;
        System.err.println(thisMonthPerform);
        System.err.println(lastMonthPerform);
        statisticAnalysis.setComparedWithLastMonth(v);
        //同比上年
        double v1 = (thisYearPerform - lastYearPerform) / lastYearPerform;
        statisticAnalysis.setComparedWithThePreviousYear(v1);
        System.err.println(thisYearPerform);
        System.err.println(lastYearPerform);

        //折线图
        String picture = "[{\n" +
                "\t\"companyName\": \"造价绩效\",\n" +
                "\t\"imageAmmount\": [";
        for (PerformanceDistributionChart performanceDistributionChart : thisYearChart) {
            picture+="{\n" +
                    "\t\t\"time\": \""+performanceDistributionChart.getYearTime()+"-"+performanceDistributionChart.getMonthTime()+"\",\n" +
                    "\t\t\"truckAmmount\": \""+performanceDistributionChart.getPerformanceProvision()+"\"\n" +
                    "\t}, ";
        }
        picture = picture.substring(0,picture.length() -1);
        picture+="]\n" +
                "}]";
        statisticAnalysis.setPicture(JSON.parseArray(picture));
        //饼状图

        return statisticAnalysis;
    }

    public String performanceAccrualAndSummary(pageVo pageVo) {

        List<PerformanceDistributionChart> costPerformanceDistributionChart = achievementsInfoMapper.findCostPerformanceDistributionChart(pageVo);
        ArrayList<PerformanceDistributionChart> thisYear = new ArrayList<>();

        if (pageVo.getStatTime()==null || pageVo.getStatTime().equals("") && pageVo.getEndTime() == null || pageVo.getEndTime().equals("")){
            Calendar cal = Calendar.getInstance();
           int i = cal.get(Calendar.YEAR);
            for (PerformanceDistributionChart performanceDistributionChart : costPerformanceDistributionChart) {
                if (performanceDistributionChart.getYearTime().equals(i+"")){
                    thisYear.add(performanceDistributionChart);
                }
            }
        }
        if (thisYear.size()!=0){
            String aa = "[{\n" +
                    "\t\"companyName\": \"绩效计提\",\n" +
                    "\t\"imageAmmount\": [";

            for (PerformanceDistributionChart performanceDistributionChart : thisYear) {
                aa+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getYearTime()+"-"+performanceDistributionChart.getMonthTime()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getPerformanceProvision()+"\"\n" +
                        "\t}, ";
            }
            aa = aa.substring(0,aa.length() -1);
            aa+="]\n" +
                    "}, {\n" +
                    "\t\"companyName\": \"当月发放\",\n" +
                    "\t\"imageAmmount\": [";
            for (PerformanceDistributionChart performanceDistributionChart : thisYear) {
                aa+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getYearTime()+"-"+performanceDistributionChart.getMonthTime()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getIssuedDuringMmonth()+"\"\n" +
                        "\t}, ";
            }
            aa = aa.substring(0,aa.length() -1);
            aa+="]\n" +
                    "}]";
            return aa;
        }else{
            String aa = "[{\n" +
                    "\t\"companyName\": \"绩效计提\",\n" +
                    "\t\"imageAmmount\": [";

            for (PerformanceDistributionChart performanceDistributionChart : costPerformanceDistributionChart) {
                aa+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getYearTime()+"-"+performanceDistributionChart.getMonthTime()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getPerformanceProvision()+"\"\n" +
                        "\t}, ";
            }
            aa = aa.substring(0,aa.length() -1);
            aa+="]\n" +
                    "}, {\n" +
                    "\t\"companyName\": \"当月发放\",\n" +
                    "\t\"imageAmmount\": [";
            for (PerformanceDistributionChart performanceDistributionChart : costPerformanceDistributionChart) {
                aa+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getYearTime()+"-"+performanceDistributionChart.getMonthTime()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getIssuedDuringMmonth()+"\"\n" +
                        "\t}, ";
            }
            aa = aa.substring(0,aa.length() -1);
            aa+="]\n" +
                    "}]";
            return aa;
        }
    }

    public PerformanceAccrualAndSummaryList performanceAccrualAndSummaryList(pageVo pageVo) {
        PerformanceAccrualAndSummaryList performanceAccrualAndSummaryList = new PerformanceAccrualAndSummaryList();
        List<PerformanPeople> list =  achievementsInfoMapper.performanceAccrualAndSummaryList(pageVo);
        Double PerformanceProvisionTotal = 0.00;
        Double IssuedMonthTotal = 0.00;
        for (PerformanPeople performanPeople : list) {
            PerformanceProvisionTotal+=performanPeople.getPerformanceProvision();
            IssuedMonthTotal+=performanPeople.getIssuedMonth();
        }
        performanceAccrualAndSummaryList.setPeopleList(list);
        performanceAccrualAndSummaryList.setPerformanceProvisionTotal(PerformanceProvisionTotal);
        performanceAccrualAndSummaryList.setIssuedMonthTotal(IssuedMonthTotal);
        return performanceAccrualAndSummaryList;
    }
}

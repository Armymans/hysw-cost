package net.zlw.cloud.statisticAnalysis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.designProject.mapper.AchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.LastSettlementReviewMapper;
import net.zlw.cloud.designProject.mapper.ProjectMapper;
import net.zlw.cloud.designProject.mapper.SettlementAuditInformationMapper;
import net.zlw.cloud.designProject.model.BaseProject;
import net.zlw.cloud.designProject.model.OneCensus10;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.statisticAnalysis.model.*;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.Global.Infinity;

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
    @Resource
    private ProjectMapper projectMapper;

    public StatisticAnalysis findAnalysis(pageVo pageVo) {
        List<PerformanceDistributionChart> cost = achievementsInfoMapper.findCostPerformanceChart(pageVo);

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
        if (thisMonthPerform<=0){
            thisMonthPerform = 1.00;
        }
        double v = (thisMonthPerform - lastMonthPerform) / thisMonthPerform;
        BigDecimal bigDecimal1 = new BigDecimal(v);
        double v3 = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.err.println(thisMonthPerform);
        System.err.println(lastMonthPerform);
        statisticAnalysis.setComparedWithLastMonth(v3);
        //同比上年
        if (thisYearPerform<=0){
            thisYearPerform = 1.00;
        }
        double v1 = (thisYearPerform - lastYearPerform) / thisYearPerform;
        BigDecimal bigDecimal = new BigDecimal(v1);
        double v2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        statisticAnalysis.setComparedWithThePreviousYear(v2);
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
       PerformanceDistributionChart performanceDistributionChart =  achievementsInfoMapper.findBTAll();

        String aa = "[{value1:"+performanceDistributionChart.getBudgetAchievements()+",name1:'预算编制'},{value1:"+performanceDistributionChart.getUpsubmitAchievements()+",name1:'上家结算送审'},{value1:"+performanceDistributionChart.getDownsubmitAchievements()+",name1:'下家结算审核'},{value1:"+performanceDistributionChart.getTruckAchievements()+",name1:'跟踪审计'}]";
        JSONArray objects = JSONArray.parseArray(aa);
        statisticAnalysis.setPieChart(objects);


        return statisticAnalysis;
    }
    //折线图
    public JSONArray picture1(pageVo pageVo) {
        return findAnalysis(pageVo).getPicture();
    }

    //饼状图
    public JSONArray pieChar(pageVo pageVo) {
        return findAnalysis(pageVo).getPieChart();
    }

    public String performanceAccrualAndSummary(pageVo pageVo) {

        List<PerformanceDistributionChart> costPerformanceDistributionChart = achievementsInfoMapper.findCostPerformanceDistributionChart(pageVo);
        ArrayList<PerformanceDistributionChart> thisYear = new ArrayList<>();

        if (pageVo.getStatTime()==null || pageVo.getStatTime().equals("") && pageVo.getEndTime() == null || pageVo.getEndTime().equals("")){
            Calendar cal = Calendar.getInstance();
           int i = cal.get(Calendar.YEAR);
            for (PerformanceDistributionChart performanceDistributionChart : costPerformanceDistributionChart) {
                System.err.println(performanceDistributionChart);;
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

    public ReturnEmployeePerformance EmployeePerformanceAnalysis(EmployeeVo employeeVo) {
        EmployeeVo employeeVo1 = new EmployeeVo();
        employeeVo1.setDistrict("");
        employeeVo1.setMemberId("");
        //创建返回值
        ReturnEmployeePerformance returnEmployeePerformance = new ReturnEmployeePerformance();
        //获取月初和月末
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(new Date());
        //当前月 月初
        calendar.add(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String firstMonth = si.format(calendar.getTime());
        //当前月 月末
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        String lastMonth = si.format(calendar.getTime());
        //上个月 月初
        calendar.add(Calendar.MONTH,-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String earlyLastMonth = si.format(calendar.getTime());
        //上个月月末
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH,1);
        instance.add(Calendar.DATE,-1);
        String lateLastMonth = si.format(instance.getTime());

        //本月绩效与数量
        employeeVo1.setStatTime(firstMonth);
        employeeVo1.setEndTime(lastMonth);
        List<EmployeePerformance> list = settlementAuditInformationMapper.EmployeePerformanceAnalysis(employeeVo1);
        //上月绩效与数量
        employeeVo1.setStatTime(earlyLastMonth);
        employeeVo1.setEndTime(lateLastMonth);
        List<EmployeePerformance> list1 = settlementAuditInformationMapper.EmployeePerformanceAnalysis(employeeVo1);
        //本月项目数求和
        Integer thisMonthNumber = list.stream().collect(Collectors.summingInt(EmployeePerformance::getProjectNum));
        //上月项目数求和
        Integer lastMonthNumber = list1.stream().collect(Collectors.summingInt(EmployeePerformance::getProjectNum));
        //本月绩效
        Double thisMonthAchievemen = list.stream().collect(Collectors.summingDouble(EmployeePerformance::getAchievemen));
        //上月绩效
        Double lastMonthAchievemen = list1.stream().collect(Collectors.summingDouble(EmployeePerformance::getAchievemen));

        //本月项目赋值
        returnEmployeePerformance.setProjectNum(thisMonthNumber);
        //对比上月赋值
        System.out.println(thisMonthNumber);
        System.out.println(lastMonthNumber);
        double v = (Double.parseDouble(thisMonthNumber+"") - Double.parseDouble(lastMonthNumber+"")) / Double.parseDouble(lastMonthNumber+"")*100;
        returnEmployeePerformance.setComparedMonthProjectNum(v+"%");
        //本月绩效赋值
        returnEmployeePerformance.setAchievemen(thisMonthAchievemen);
        //对比上月绩效赋值
        double v1 = (thisMonthAchievemen - lastMonthAchievemen) / lastMonthAchievemen * 100;
        returnEmployeePerformance.setComparedMonthAchievemen(v1+"%");

        if (employeeVo.getStatTime()==null || employeeVo.getStatTime().equals("")){
            //如果为空则设置为本年月初和月末
            Calendar instance1 = Calendar.getInstance();
            employeeVo.setStatTime(instance1.get(Calendar.YEAR)+"-01");
            employeeVo.setEndTime(instance1.get(Calendar.YEAR)+"-12");
        }
        List<EmployeePerformance> employeePerformance =  settlementAuditInformationMapper.EmployeePerformanceAnalysis(employeeVo);
        String returnPicture = "[{\n" +
                "\t\"companyName\": \"绩效计提\",\n" +
                "\t\"imageAmmount\": [";
        for (EmployeePerformance performance : employeePerformance) {
            returnPicture+="{\n" +
                    "\t\t\"time\": \""+performance.getMonthTIme()+"月\",\n" +
                    "\t\t\"truckAmmount\": \""+performance.getAchievemen()+"\"\n" +
                    "\t}, ";
        }
        returnPicture = returnPicture.substring(0,returnPicture.length()-1);
        returnPicture += "]\n" +
                "}, {\n" +
                "\t\"companyName\": \"项目数\",\n" +
                "\t\"imageAmmount\": [";
        for (EmployeePerformance performance : employeePerformance) {
            returnPicture+="{\n" +
                    "\t\t\"time\": \""+performance.getMonthTIme()+"月\",\n" +
                    "\t\t\"truckAmmount\": \""+performance.getProjectNum()+"\"\n" +
                    "\t}, ";
        }
        returnPicture = returnPicture.substring(0,returnPicture.length()-1);
        returnPicture += "]\n" +
                "}]";
        JSONArray objects = JSON.parseArray(returnPicture);
        returnEmployeePerformance.setPicture(objects);

        return returnEmployeePerformance;
    }

    public List<OneCensus10> EmployeePerformanceAnalysisList(EmployeeVo employeeVo) {
        List<OneCensus10> oneCensus10s = projectMapper.EmployeecostTaskCensusList(employeeVo);
        List<OneCensus10> oneCensus10s1 = projectMapper.EmployeecostTaskCensusList2(employeeVo);
        for (OneCensus10 oneCensus10 : oneCensus10s) {
            for (OneCensus10 census10 : oneCensus10s1) {
                if(oneCensus10.getYearTime().equals(census10.getYearTime())){
                    if(oneCensus10.getMonthTime().equals(census10.getMonthTime())){
                        oneCensus10.setBudCountB(census10.getBudCountB());
                        oneCensus10.setCostTotalAmountB(census10.getCostTotalAmountB());
                        oneCensus10.setPerformB(census10.getPerformB());
                    }
                }
            }
        }
        for (OneCensus10 oneCensus10 : oneCensus10s) {
            if (oneCensus10.getPerformB()!=null && oneCensus10.getPerformA()!=null){
                oneCensus10.setABTotal(oneCensus10.getPerformA().add(oneCensus10.getPerformB()));
            }
        }
        return oneCensus10s;

    }
}

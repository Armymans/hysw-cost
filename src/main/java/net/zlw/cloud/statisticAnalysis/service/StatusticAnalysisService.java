package net.zlw.cloud.statisticAnalysis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.designProject.mapper.AchievementsInfoMapper;
import net.zlw.cloud.designProject.mapper.LastSettlementReviewMapper;
import net.zlw.cloud.designProject.mapper.ProjectMapper;
import net.zlw.cloud.designProject.mapper.SettlementAuditInformationMapper;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.OneCensus10;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.statisticAnalysis.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        System.err.println(pageVo);
        //
        List<PerformanceDistributionChart> cost = achievementsInfoMapper.findCostPerformanceChart(pageVo);
        //
        ArrayList<PerformanceDistributionChart> thisYearChart = new ArrayList<>();

        //本月发放
        Double thisMonthPerform = 0.00;
        //本年发放
        Double thisYearPerform = 0.00;
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
            System.out.println(performanceDistributionChart);
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

    /**
     * 外来统计接口拆分(员工绩效统计)
     * @param costVo2
     * @return
     */
    public StatisticAnalysis    findYear(CostVo2 costVo2){
        StatisticAnalysis statisticAnalysis = new StatisticAnalysis();

        List<PerformanceDistributionChart> cost = achievementsInfoMapper.findCostPerformanceChart2(costVo2);
        //本年发放
        Double thisYearPerform = 0.00;
        ArrayList<PerformanceDistributionChart> thisYearChart = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        //本年
        int thisYear = cal.get(Calendar.YEAR);
        for (PerformanceDistributionChart performanceDistributionChart : cost) {
            //本年
            if (performanceDistributionChart.getYearTime().equals(thisYear+"")){
                thisYearPerform += performanceDistributionChart.getIssuedDuringMmonth().doubleValue();
                thisYearChart.add(performanceDistributionChart);
            }
        }
        //本年绩效数量总汇
        statisticAnalysis.setCurrentYearPaymentPerformance(thisYearPerform);
        //造价绩效统计图
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
        returnEmployeePerformance.setComparedMonthProjectNum(v);
        //本月绩效赋值
        returnEmployeePerformance.setAchievemen(thisMonthAchievemen);
        //对比上月绩效赋值
        double v1 = (thisMonthAchievemen - lastMonthAchievemen) / lastMonthAchievemen * 100;
        returnEmployeePerformance.setComparedMonthAchievemen(v1);

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

    /**
     * 获取上个月
     * @param pageVo
     * @return
     */
    public pageVo lastMonth(pageVo pageVo){
        //获取上个月第一天
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar first=Calendar.getInstance();
        first.add(Calendar.MONTH, -1);
        first.set(Calendar.DAY_OF_MONTH, 1);
        //上个月第一天
        String fristDay = format.format(first.getTime());
        //获取上个月的最后一天
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar last =Calendar.getInstance();
        int month=last.get(Calendar.MONTH);
        last.set(Calendar.MONTH, month-1);
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(last.DAY_OF_MONTH));
        //上个月最后一天
        String lastDay = sf.format(last.getTime());
        //开始时间结束时间
        pageVo.setStatTime(fristDay);
        pageVo.setEndTime(lastDay);
        return pageVo;
    }

    /**
     * 获取上一年
     * @param pageVo
     * @return
     */
    public pageVo lastYear(pageVo pageVo){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        pageVo.setStatTime(year-1+"-01-01");
        pageVo.setEndTime(year-1+"-12-31");
        return pageVo;
    }

    /**
     * 当前年份
     * @param pageVo
     * @return
     */
    public pageVo NowYear(pageVo pageVo){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        pageVo.setStatTime(year+"-01-01");
        pageVo.setEndTime(year+"-12-31");
        return pageVo;
    }

    /**
     * 当前月份
     * @param pageVo
     * @return
     */
    public pageVo NowMonth(pageVo pageVo){
        SimpleDateFormat sf=new SimpleDateFormat("dd");
        Calendar now = Calendar.getInstance();
        //当前年
        String year = String.valueOf(now.get(Calendar.YEAR));
        //当前月
        //String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        String month = DateUtil.getNowMonth().substring(4);
        //当前月最后一天
        //设置日期为本月最大日期
        now.set(Calendar.DATE, now.getActualMaximum(now.DATE));
        String day = sf.format(now.getTime());
        //开始时间 结束时间
        pageVo.setStatTime(year+"-"+month+"-"+"01");
        pageVo.setEndTime(year+"-"+month+"-"+day);
        return pageVo;
    }

    /**
     * 新造价绩效统计图 折
     * @param pageVo
     * @return
     */
    public JSONArray newPicture(pageVo pageVo) {
        List<PerformanceDistributionChart> performanceDistributionCharts = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(StringUtils.isEmpty(pageVo.getStatTime()) || StringUtils.isEmpty(pageVo.getEndTime())){
            pageVo = this.NowYear(pageVo);
        }

        Calendar start = new GregorianCalendar();   // 开始时间
        Calendar end = new GregorianCalendar();     // 结束时间
        try{
            start.setTime(simpleDateFormat.parse(pageVo.getStatTime()));
            end.setTime(simpleDateFormat.parse(pageVo.getEndTime()));

            for (;start.compareTo(end) <= 0;) {

                // 按月查询数据
                pageVo.setStatTime(simpleDateFormat.format(start.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start.getTime());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                pageVo.setEndTime(simpleDateFormat.format(calendar.getTime()));

                List<PerformanceDistributionChart> performanceDistributionChartList = achievementsInfoMapper.newPicture(pageVo);
                // 如果当前月数据为空，添加一条
                if (!(performanceDistributionChartList.size() > 0)){
                    PerformanceDistributionChart performanceDistributionChart = new PerformanceDistributionChart();
                    performanceDistributionChart.setYearTime(simpleDateFormat.format(start.getTime()).substring(0,4));
                    performanceDistributionChart.setMonthTime(Integer.parseInt(simpleDateFormat.format(start.getTime()).substring(5,7)) + "");
                    performanceDistributionChart.setPerformanceProvision(new BigDecimal(0));
                    performanceDistributionChartList.add(performanceDistributionChart);
                }
                performanceDistributionCharts.addAll(performanceDistributionChartList);
                start.set(Calendar.DAY_OF_MONTH,1);
                start.add(Calendar.MONTH, 1);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        String year = pageVo.getStatTime().substring(0,4);
        String month = pageVo.getStatTime().substring(5,7);
        //json拼接
        String json =
                "[{" +
                        "\"companyName\": \"绩效发放数据\"," +
                        "\"imageAmmount\": [";
        if(performanceDistributionCharts.size()>0){
            for (PerformanceDistributionChart chart : performanceDistributionCharts) {
                json +=
                        "{\"time\": \""+chart.getYearTime()+"-"+chart.getMonthTime()+"\"," +
                                "\"truckAmmount\": \""+chart.getPerformanceProvision()+"\"" +
                                "},";
            }
            json = json.substring(0,json.length()-1);
        }else{
            json+="{\"time\": \""+year+ month+"\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }
        json += "]}]";
        JSONArray objects = JSON.parseArray(json);
        return objects;
    }

    /**
     * 新造价绩效统计图 饼
     * @param pageVo
     * @return
     */
    public JSONArray newPieChar(pageVo pageVo) {
        PerformanceDistributionChart performanceDistributionChart = new PerformanceDistributionChart();
        if (pageVo.getStatTime()==null || pageVo.getStatTime().equals("") && pageVo.getEndTime() == null || pageVo.getEndTime().equals("")){
            performanceDistributionChart = achievementsInfoMapper.newPieChar(this.NowYear(pageVo));
        }else{
            performanceDistributionChart = achievementsInfoMapper.newPieChar(pageVo);
        }
        String json = "[{value1:"+performanceDistributionChart.getBudgetAchievements()+",name1:'预算编制'}," +
                "{value1:"+performanceDistributionChart.getUpsubmitAchievements()+",name1:'上家结算送审'}," +
                "{value1:"+performanceDistributionChart.getDownsubmitAchievements()+",name1:'下家结算审核'}," +
                "{value1:"+performanceDistributionChart.getTruckAchievements()+",name1:'跟踪审计'}]";
        JSONArray objects = JSONArray.parseArray(json);
        return objects;
    }

    /**
     * 新绩效计提汇总
      * @param pageVo
     * @return
     */
    public JSONArray newPerformaPnceAccrualAndSummary(pageVo pageVo) {
        //如果为删选默认展示本月
        List<PerformanceDistributionChart> performanceDistributionCharts = new ArrayList<>();
        if(pageVo.getStatTime()==null || pageVo.getStatTime().equals("") && pageVo.getEndTime() == null || pageVo.getEndTime().equals("")){
            performanceDistributionCharts = achievementsInfoMapper.newPerformaPnceAccrualAndSummary(this.NowMonth(pageVo));
        }else{
            performanceDistributionCharts = achievementsInfoMapper.newPerformaPnceAccrualAndSummary(pageVo);
        }
        String year = pageVo.getStatTime().substring(0,4);
        String month = pageVo.getStatTime().substring(5,7);

        String json = "[{\n" +
                "\t\"companyName\": \"绩效计提\",\n" +
                "\t\"imageAmmount\": [";
        if(performanceDistributionCharts.size()>0){
            for (PerformanceDistributionChart performanceDistributionChart : performanceDistributionCharts) {
                json+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getMemberName()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getPerformanceProvision()+"\"\n" +
                        "\t}, ";
            }
            json = json.substring(0,json.length() -1);
        }else{
            json+="{\"time\": \""+ year + month +"\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        json+="]\n" +
                "}, {\n" +
                "\t\"companyName\": \"当月发放\",\n" +
                "\t\"imageAmmount\": [";
        if(performanceDistributionCharts.size()>0){
            for (PerformanceDistributionChart performanceDistributionChart : performanceDistributionCharts) {
                json+="{\n" +
                        "\t\t\"time\": \""+performanceDistributionChart.getMemberName()+"\",\n" +
                        "\t\t\"truckAmmount\": \""+performanceDistributionChart.getIssuedDuringMmonth()+"\"\n" +
                        "\t}, ";
            }
            json = json.substring(0,json.length() -1);
        }else{
            json+="{\"time\": \""+ year + month +"\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        json+="]\n" +
                "}]";
        JSONArray objects = JSONArray.parseArray(json);

        return objects;
    }

    /**
     * 新员工绩效分析
     * @param pageVo
     * @return
     */
    public JSONArray newEmployeePerformanceAnalysis(pageVo pageVo) {
        List<PerformanceDistributionChart> returnEmployeePerformances = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(StringUtils.isEmpty(pageVo.getStatTime()) || StringUtils.isEmpty(pageVo.getEndTime())){
            pageVo = this.NowYear(pageVo);
        }

        Calendar start = new GregorianCalendar();   // 开始时间
        Calendar end = new GregorianCalendar();     // 结束时间
        try{
            start.setTime(simpleDateFormat.parse(pageVo.getStatTime()));
            end.setTime(simpleDateFormat.parse(pageVo.getEndTime()));

            for (;start.compareTo(end) <= 0;) {

                // 按月查询数据
                pageVo.setStatTime(simpleDateFormat.format(start.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start.getTime());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                pageVo.setEndTime(simpleDateFormat.format(calendar.getTime()));

                List<PerformanceDistributionChart> performanceDistributionChartList = achievementsInfoMapper.newEmployeePerformanceAnalysis(pageVo);
                // 如果当前月数据为空，添加一条
                if (CollectionUtils.isEmpty(performanceDistributionChartList)){
                    PerformanceDistributionChart performanceDistributionChart = new PerformanceDistributionChart();
                    performanceDistributionChart.setYearTime(simpleDateFormat.format(start.getTime()).substring(0,4));
                    performanceDistributionChart.setMonthTime(Integer.parseInt(simpleDateFormat.format(start.getTime()).substring(5,7)) + "");
                    performanceDistributionChart.setPerformanceProvision(new BigDecimal(0));
                    performanceDistributionChartList.add(performanceDistributionChart);
                }
                returnEmployeePerformances.addAll(performanceDistributionChartList);
                start.set(Calendar.DAY_OF_MONTH,1);
                start.add(Calendar.MONTH, 1);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        String returnPicture = "[{\n" +
                "\t\"companyName\": \"绩效计提\",\n" +
                "\t\"imageAmmount\": [";
        if(returnEmployeePerformances.size()>0){
            for (PerformanceDistributionChart returnEmployeePerformance : returnEmployeePerformances) {
                returnPicture+="{\n" +
                        "\t\t\"time\": \""+returnEmployeePerformance.getMonthTime()+"月\",\n" +
                        "\t\t\"truckAmmount\": \""+returnEmployeePerformance.getPerformanceProvision()+"\"\n" +
                        "\t}, ";
            }
            returnPicture = returnPicture.substring(0,returnPicture.length()-1);
        }else {
            returnPicture+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }
        returnPicture += "]\n" +
                "}]";
        JSONArray objects = JSON.parseArray(returnPicture);
        return objects;
    }

    public Map<String,Object> newFindFAnalysis(pageVo pageVo) {
        //本月发放绩效(计提)
        BigDecimal nowSum1 = new BigDecimal(0);
        //上月发放绩效(计提)
        BigDecimal lastSum1 = new BigDecimal(0);
        //本年发放绩效
        BigDecimal nowSum2 = new BigDecimal(0);
        //上年发放绩效
        BigDecimal lastSum2 = new BigDecimal(0);


        //本月 - 上月
        List<PerformanceDistributionChart> now1 = achievementsInfoMapper.newPicture(this.NowMonth(pageVo));
        if(now1.size()>0){
            nowSum1 = now1.get(0).getPerformanceProvision();
        }
        List<PerformanceDistributionChart> last1 = achievementsInfoMapper.newPicture(this.lastMonth(pageVo));
        if(last1.size()>0){
            lastSum1 = last1.get(0).getPerformanceProvision();
        }
        //同比上月
        BigDecimal Sum1_2 = nowSum1.subtract(lastSum1);

        //本年 - 上年
        List<PerformanceDistributionChart> now2 = achievementsInfoMapper.newPicture(this.NowYear(pageVo));
        if(now2.size()>0){
            for (PerformanceDistributionChart performanceDistributionChart : now2) {
                nowSum2 = nowSum2.add(performanceDistributionChart.getPerformanceProvision());
            }
        }
        List<PerformanceDistributionChart> last2 = achievementsInfoMapper.newPicture(this.lastYear(pageVo));
        if(last2.size()>0){
            for (PerformanceDistributionChart performanceDistributionChart : last2) {
                lastSum2 = lastSum2.add(performanceDistributionChart.getPerformanceProvision());
            }
        }
        //同比上年
        BigDecimal Sum2_3 = nowSum2.subtract(lastSum2);

        //本年
        HashMap<String, Object> map = new HashMap<>();
        map.put("nowSum1",nowSum1);
        map.put("Sum1_2",Sum1_2);
        map.put("nowSum2",nowSum2);
        map.put("Sum2_3",Sum2_3);

        return map;
    }
}

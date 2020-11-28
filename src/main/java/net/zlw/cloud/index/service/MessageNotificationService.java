package net.zlw.cloud.index.service;

import com.alibaba.fastjson.JSONArray;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.designProject.mapper.*;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.IncomeInfo;
import net.zlw.cloud.designProject.model.LastSettlementReview;
import net.zlw.cloud.designProject.model.SettlementAuditInformation;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.index.mapper.MessageNotificationDao;
import net.zlw.cloud.index.model.CostSum;
import net.zlw.cloud.index.model.DesignSum;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.StatisticalData;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import org.hibernate.engine.jdbc.Size;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageNotificationService {
    @Resource
    private MessageNotificationDao messageNotificationDao;
    @Resource
    private BudgetingDao budgetingDao;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private DesignInfoMapper designInfoMapper;
    @Resource
    private IncomeInfoMapper incomeInfoMapper;
    @Resource
    private LastSettlementReviewMapper lastSettlementReviewMapper;
    @Resource
    private SettlementAuditInformationMapper settlementAuditInformationMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;
    @Resource
    private AchievementsInfoMapper achievementsInfoMapper;


    public List<MessageNotification> findMessage(UserInfo userInfo) {
        String id = userInfo.getId();
        return messageNotificationDao.findMessage(id);
    }

    public StatisticalData findStatisticalData(pageVo pageVo) {
        //月初与月末
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simd = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //第一天
        String firstDayOfMonth = simd.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        //最后一天
        String lastDayOfMonth = simd.format(calendar.getTime());

        //返回值对象
        StatisticalData statisticalData = new StatisticalData();


        //设计数量
        int projectCount = baseProjectDao.selectProjectCount(pageVo);
        statisticalData.setNumberProjects(projectCount);
        //总收入
        double inCome = baseProjectDao.selectIncome(pageVo);
        statisticalData.setGeneralIncome(inCome);
        //委外支出
        double outMoney = baseProjectDao.selectOutMoney(pageVo);
        statisticalData.setOutsourcingSpending(outMoney);
        //员工绩效
        double achievements = baseProjectDao.selectAchievements(pageVo);
        statisticalData.setAchievements(achievements);
        //经营收入
        double operInCome = inCome-outMoney-achievements;
        statisticalData.setOperatingIncome(operInCome);


        //设计数量
        Integer projectCount2 = baseProjectDao.selectProjectCount(pageVo);
        statisticalData.setNumberProjects2(projectCount2);
        //总收入
        double inCome2 = baseProjectDao.selectIncome2(pageVo);
        statisticalData.setGeneralIncome2(inCome2);
        //委外支出
        double outMoney2 = baseProjectDao.selectOutMoney2(pageVo);
        statisticalData.setOutsourcingSpending2(outMoney2);
        //员工绩效
        double achievements2 = baseProjectDao.selectAchievements2(pageVo);
        statisticalData.setAchievements2(achievements2);
        //经营收入
        double operInCome2 = inCome2-outMoney2-achievements2;
        statisticalData.setOperatingIncome2(operInCome2);

        return statisticalData;

//        //造价部门
//        //项目数量
//        Integer costNumber = 0;
//        //总收入
//        Double costGeneralIncome = 0.00;
//        //委外支出
//        Double costOutsourcingSpending = 0.00;
//        Example example = new Example(BaseProject.class);
//        Example.Criteria c = example.createCriteria();
//        if (pageVo.getDistrict()!=null && !pageVo.getDistrict().equals("")){
//            c.andEqualTo("district",pageVo.getDistrict());
//        }
//        List<BaseProject> baseProjects = baseProjectDao.selectByExample(example);
//        for (BaseProject baseProject : baseProjects) {
//            //预算编制
//            Example example1 = new Example(Budgeting.class);
//            Example.Criteria c2 = example1.createCriteria();
//            c2.andEqualTo("baseProjectId",baseProject.getId());
//            c2.andEqualTo("delFlag","0");
//            //上家结算送审
//            Example example2 = new Example(LastSettlementReview.class);
//            Example.Criteria c3 = example2.createCriteria();
//            c3.andEqualTo("baseProjectId",baseProject.getId());
//            c3.andEqualTo("delFlag","0");
//            //下家结算审核
//            Example example3 = new Example(SettlementAuditInformation.class);
//            Example.Criteria c4 = example3.createCriteria();
//            c4.andEqualTo("baseProjectId",baseProject.getId());
//            c4.andEqualTo("delFlag","0");
//            //跟踪审计
//            Example example4 = new Example(TrackAuditInfo.class);
//            Example.Criteria c5 = example4.createCriteria();
//            c5.andEqualTo("baseProjectId",baseProject.getId());
//            c5.andEqualTo("status","0");
//            //如果开始时间不为空的话则进行筛选
//            if (pageVo.getStatTime()!=null && !pageVo.getStatTime().equals("")){
//                c2.andGreaterThanOrEqualTo("budgetingTime",pageVo.getStatTime());
//                c3.andGreaterThanOrEqualTo("compileTime",pageVo.getStatTime());
//                c4.andGreaterThanOrEqualTo("compileTime",pageVo.getStatTime());
//                c5.andGreaterThanOrEqualTo("createTime",pageVo.getStatTime());
//            }else{
//                c2.andGreaterThanOrEqualTo("budgetingTime",firstDayOfMonth);
//                c3.andGreaterThanOrEqualTo("compileTime",firstDayOfMonth);
//                c4.andGreaterThanOrEqualTo("compileTime",firstDayOfMonth);
//                c5.andGreaterThanOrEqualTo("createTime",firstDayOfMonth);
//            }
//            //如果结束时间不为空的话则进行筛选
//            if (pageVo.getEndTime()!=null && !pageVo.getEndTime().equals("")){
//                c2.andLessThanOrEqualTo("budgetingTime",pageVo.getEndTime());
//                c3.andLessThanOrEqualTo("compileTime",pageVo.getEndTime());
//                c4.andLessThanOrEqualTo("compileTime",pageVo.getEndTime());
//                c5.andLessThanOrEqualTo("createTime",pageVo.getEndTime());
//            }else{
//                c2.andLessThanOrEqualTo("budgetingTime",lastDayOfMonth);
//                c3.andLessThanOrEqualTo("compileTime",lastDayOfMonth);
//                c4.andLessThanOrEqualTo("compileTime",lastDayOfMonth);
//                c5.andLessThanOrEqualTo("createTime",lastDayOfMonth);
//            }
//            Budgeting budgeting = budgetingDao.selectOneByExample(example1);
//            LastSettlementReview lastSettlementReview = lastSettlementReviewMapper.selectOneByExample(example2);
//            SettlementAuditInformation settlementAuditInformation = settlementAuditInformationMapper.selectOneByExample(example3);
//            TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectOneByExample(example4);
//            //预算编制
//            if (budgeting!=null){
//                costNumber++;
//                Example example5 = new Example(IncomeInfo.class);
//                Example.Criteria c6 = example5.createCriteria();
//                c6.andEqualTo("baseProjectId",baseProject.getId());
//                IncomeInfo incomeInfo = incomeInfoMapper.selectOneByExample(example5);
//                if (incomeInfo!=null){
//                    //总金额累加
//                    costGeneralIncome+=incomeInfo.getBudgetMoney().doubleValue();
//                    //委外金额累加
//                    costOutsourcingSpending+=budgeting.getAmountOutsourcing().doubleValue();
//                }
//            }
//            //上家审核
//            if (lastSettlementReview!=null){
//                System.err.println(lastSettlementReview.getBaseProjectId());
//                costNumber++;
//                Example example5 = new Example(IncomeInfo.class);
//                Example.Criteria c6 = example5.createCriteria();
//                c6.andEqualTo("baseProjectId",baseProject.getId());
//                IncomeInfo incomeInfo = incomeInfoMapper.selectOneByExample(example5);
//                if (incomeInfo!=null){
//                    //总金额累加
//                    costGeneralIncome+=incomeInfo.getBudgetMoney().doubleValue();
//                    //委外金额累加
//                    costOutsourcingSpending+=lastSettlementReview.getAmountOutsourcing().doubleValue();
//                }
//            }
//            //下家审核
//            if (settlementAuditInformation!=null){
//                costNumber++;
//                Example example5 = new Example(IncomeInfo.class);
//                Example.Criteria c6 = example5.createCriteria();
//                c6.andEqualTo("baseProjectId",baseProject.getId());
//                IncomeInfo incomeInfo = incomeInfoMapper.selectOneByExample(example5);
//                if (incomeInfo!=null){
//                    //总金额累加
//                    if (incomeInfo.getBudgetMoney()!=null){
//                        costGeneralIncome+=incomeInfo.getBudgetMoney().doubleValue();
//                    }
//                    //委外金额累加
//                    if (settlementAuditInformation.getAmountOutsourcing()!=null){
//                        costOutsourcingSpending+=settlementAuditInformation.getAmountOutsourcing().doubleValue();
//                    }
//                }
//            }
//            //跟踪审计
//            if (trackAuditInfo!=null){
//                costNumber++;
//                Example example5 = new Example(IncomeInfo.class);
//                Example.Criteria c6 = example5.createCriteria();
//                c6.andEqualTo("baseProjectId",baseProject.getId());
//                IncomeInfo incomeInfo = incomeInfoMapper.selectOneByExample(example5);
//                if (incomeInfo!=null){
//                    //总金额累加
//                    if (incomeInfo.getBudgetMoney()!=null){
//                        costGeneralIncome+=incomeInfo.getBudgetMoney().doubleValue();
//                    }
//                    //委外金额累加
//                    if (trackAuditInfo.getOutsourceMoney()!=null){
//                        costOutsourcingSpending+=trackAuditInfo.getOutsourceMoney().doubleValue();
//                    }
//                }
//            }
//
//
//        }
//        //造价所有进行赋值
//        statisticalData.getCostSum().setNumberProjects(costNumber);
//        statisticalData.getCostSum().setGeneralIncome(costGeneralIncome);
//        statisticalData.getCostSum().setOutsourcingSpending(costOutsourcingSpending);
//        statisticalData.getCostSum().setOperatingIncome(costGeneralIncome-costOutsourcingSpending);
//
//
//        //设计部门
//        Integer designNumber = 0;
//        Double designGeneralIncome = 0.00;
//        Double designOutsourcingSpending = 0.00;
//        for (BaseProject baseProject : baseProjects) {
//            Example example1 = new Example(DesignInfo.class);
//            Example.Criteria c1 = example1.createCriteria();
//            if (pageVo.getStatTime()!=null && !pageVo.getStatTime().equals("")){
//                c1.andEqualTo("baseProjectId",baseProject.getId());
//                c1.andGreaterThanOrEqualTo("blueprintStartTime",pageVo.getStatTime());
//            }else{
//                c1.andEqualTo("baseProjectId",baseProject.getId());
//                c1.andGreaterThanOrEqualTo("blueprintStartTime",firstDayOfMonth);
//            }
//            if (pageVo.getEndTime()!=null && !pageVo.getEndTime().equals("")){
//                c1.andEqualTo("baseProjectId",baseProject.getId());
//                c1.andLessThanOrEqualTo("blueprintStartTime",pageVo.getEndTime());
//            }else{
//                c1.andEqualTo("baseProjectId",baseProject.getId());
//                c1.andLessThanOrEqualTo("blueprintStartTime",lastDayOfMonth);
//            }
//            DesignInfo designInfo = designInfoMapper.selectOneByExample(example1);
//            if (designInfo!=null){
//                System.err.println(designInfo.getBaseProjectId());
//                designNumber++;
//                Example example2 = new Example(IncomeInfo.class);
//                Example.Criteria c2 = example2.createCriteria();
//                c2.andEqualTo("baseProjectId",baseProject.getId());
//                IncomeInfo incomeInfo = incomeInfoMapper.selectOneByExample(example2);
//                if (incomeInfo!=null){
//                    System.err.println(incomeInfo);
//                    if (incomeInfo.getDesignMoney()!=null){
//                        designGeneralIncome+=incomeInfo.getDesignMoney().doubleValue();
//                    }
//                    if (designInfo.getOutsourceMoney()!=null){
//                        designOutsourcingSpending+=designInfo.getOutsourceMoney().doubleValue();
//                    }
//                }
//            }
//        }
//        statisticalData.getDesignSum().setNumberProjects(designNumber);
//        statisticalData.getDesignSum().setGeneralIncome(designGeneralIncome);
//        statisticalData.getDesignSum().setOutsourcingSpending(designOutsourcingSpending);
//        statisticalData.getDesignSum().setOperatingIncome(designGeneralIncome-designOutsourcingSpending);
//        return statisticalData;
    }


    public String findPerformanceDistributionChart(pageVo pageVo) {
//        List<PerformanceDistributionChart> list1 = new ArrayList<PerformanceDistributionChart>();
//        //全部
//        if (id.equals("0")){
//           List<PerformanceDistributionChart> list =  achievementsInfoMapper.findAllPerformanceDistributionChart(pageVo);
//           list1 = list;
//            //设计部门
//        }else if(id.equals("1")){
//            List<PerformanceDistributionChart> list =  achievementsInfoMapper.findDesignPerformanceDistributionChart(pageVo);
//            list1 = list;
//        //造价部门
//        }else if(id.equals("2")){
//            List<PerformanceDistributionChart> list =  achievementsInfoMapper.findCostPerformanceDistributionChart(pageVo);
//            list1 = list;
//        }


        List<PerformanceDistributionChart> performanceDistributionCharts = achievementsInfoMapper.newFindAllPerformanceDistributionChart(pageVo);
//
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
            json+="{\"time\": \"0\""+
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
            json+="{\"time\": \"0\""+
                    ",\"truckAmmount\": \"0\"" +
                    "}";
        }

        json+="]\n" +
                "}]";
        return json;
    }
}

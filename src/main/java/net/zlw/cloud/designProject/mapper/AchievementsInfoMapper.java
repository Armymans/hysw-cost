package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.AchievementsInfo;
import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.OneCensus2;
import net.zlw.cloud.designProject.model.OneCensus4;
import net.zlw.cloud.index.model.vo.PerformanceDistributionChart;
import net.zlw.cloud.index.model.vo.pageVo;
import net.zlw.cloud.statisticAnalysis.model.PerformanPeople;
import net.zlw.cloud.statisticAnalysis.model.PerformanceAccrualAndSummaryList;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AchievementsInfoMapper extends Mapper<AchievementsInfo> {
    @Select(
            "SELECT    " +
                    "desgin_achievements,   " +
                    "budget_achievements,   " +
                    "upsubmit_achievements,   " +
                    "downsubmit_achievements,   " +
                    "truck_achievements   " +
                    "FROM    " +
                    "achievements_info s1,   " +
                    "base_project s2   " +
                    "where   " +
                    "s1.base_project_id = s2.id   " +
                    "and   " +
                    "(s2.district=#{district} or  #{district}  = '')   " +
                    "and   " +
                    "s1.create_time>=#{startTime}   " +
                    "and   " +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<AchievementsInfo> totalexpenditure(CostVo2 costVo2);


    //    @Select("select    " +
//            "(select member_name  from member_manage where id = a.member_id ) memberName,   " +
//            "SUM(a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,   " +
//            "SUM((a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth   " +
//            "from    " +
//            "achievements_info a     " +
//            "left join base_project b on b.id = a.base_project_id   " +
//            "left join budgeting bt on b.id = bt.base_project_id   " +
//            "left join last_settlement_review l on b.id = l.base_project_id   " +
//            "left join settlement_audit_information s on b.id = s.base_project_id   " +
//            "left join track_audit_info t on b.id = t.base_project_id   " +
//            "left join design_info d on b.id = d.base_project_id   " +
//            "where    " +
//            "(b.district = #{district} or #{district} = '') and    " +
//            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
//            "(l.compile_time > #{statTime} or #{statTime} = '') and    " +
//            "(s.compile_time > #{statTime} or #{statTime} = '') and    " +
//            "(t.create_time > #{statTime} or #{statTime} = '') and    " +
//            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and    " +
//            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
//            "(l.compile_time < #{endTime} or #{endTime} = '') and    " +
//            "(s.compile_time < #{endTime} or #{endTime} = '') and    " +
//            "(t.create_time < #{endTime} or #{endTime} = '') and    " +
//            "(d.blueprint_start_time < #{endTime} or #{endTime} = '')    " +
//            "group by    " +
//            "member_id   ")
//    List<PerformanceDistributionChart> findAllPerformanceDistributionChart(pageVo pageVo);
    @Select("select    " +
            "(select member_name  from member_manage where id = a.member_id ) memberName,   " +
            "SUM(a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,   " +
            "SUM((a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth   " +
            "from    " +
            "achievements_info a     " +
            "left join base_project b on b.id = a.base_project_id   " +
            "left join budgeting bt on b.id = bt.base_project_id   " +
            "left join last_settlement_review l on b.id = l.base_project_id   " +
            "left join settlement_audit_information s on b.id = s.base_project_id   " +
            "left join track_audit_info t on b.id = t.base_project_id   " +
            "left join design_info d on b.id = d.base_project_id   " +
            "where    " +
            "(b.district = #{district} or #{district} = '') and    " +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
            "(l.compile_time > #{statTime} or #{statTime} = '') and    " +
            "(s.compile_time > #{statTime} or #{statTime} = '') and    " +
            "(t.create_time > #{statTime} or #{statTime} = '') and    " +
            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and    " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
            "(l.compile_time < #{endTime} or #{endTime} = '') and    " +
            "(s.compile_time < #{endTime} or #{endTime} = '') and    " +
            "(t.create_time < #{endTime} or #{endTime} = '') and    " +
            "(d.blueprint_start_time < #{endTime} or #{endTime} = '')    " +
            "group by    " +
            "a.member_id   ")
    List<PerformanceDistributionChart> findAllPerformanceDistributionChart(pageVo pageVo);


    //    @Select("select    " +
//            "(select member_name  from member_manage where id = a.member_id ) memberName,   " +
//            "SUM(a.desgin_achievements) as PerformanceProvision,   " +
//            "SUM((a.desgin_achievements)*0.8) as IssuedDuringMmonth   " +
//            "from    " +
//            "achievements_info a     " +
//            "left join base_project b on b.id = a.base_project_id   " +
//            "left join design_info d on b.id = d.base_project_id   " +
//            "where    " +
//            "(b.district = #{district} or #{district} = '') and    " +
//            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and    " +
//            "(d.blueprint_start_time < #{endTime} or #{endTime} = '')    " +
//            "group by    " +
//            "member_id   ")
//    List<PerformanceDistributionChart> findDesignPerformanceDistributionChart(pageVo pageVo);
    @Select(
            "select (select member_name from member_manage where id = a.member_id ) memberName, SUM(a.desgin_achievements) as PerformanceProvision, SUM((a.desgin_achievements)*0.8) as IssuedDuringMmonth from achievements_info a INNER join base_project b on b.id = a.base_project_id INNER join design_info d on b.id = d.base_project_id inner join (select id from member_manage where member_role_id ='4' or member_role_id = '6') aaa on a.member_id = aaa.id    " +
            "where    " +
            "(b.district = #{district} or #{district} = '') and    " +
            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and    " +
            "(d.blueprint_start_time < #{endTime} or #{endTime} = '')    " +
            "group by    " +
            "a.member_id   ")
    List<PerformanceDistributionChart> findDesignPerformanceDistributionChart(pageVo pageVo);

    //    @Select("select    " +
//            "(select member_name  from member_manage where id = a.member_id ) memberName,   " +
//            "SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,   " +
//            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth   " +
//            "from    " +
//            "achievements_info a     " +
//            "left join base_project b on b.id = a.base_project_id   " +
//            "left join budgeting bt on b.id = bt.base_project_id   " +
//            "left join last_settlement_review l on b.id = l.base_project_id   " +
//            "left join settlement_audit_information s on b.id = s.base_project_id   " +
//            "left join track_audit_info t on b.id = t.base_project_id   " +
//            "left join member_manage mm on mm.id = a.member_id    " +
//            "where    " +
//            "(b.district = #{district} or #{district} = '') and    " +
//            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
//            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
//            "mm.dep_id = '2'    " +
//            "group by    " +
//            "member_id   ")
//    List<PerformanceDistributionChart> findCostPerformanceDistributionChart(pageVo pageVo);
    @Select("select    " +
            "(select member_name  from member_manage where id = a.member_id ) memberName,   " +
            "YEAR(bt.budgeting_time) yearTime,   " +
            "MONTH(bt.budgeting_time) monthTime,   " +
            "SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,   " +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth   " +
            "from    " +
            "achievements_info a     " +
            "left join base_project b on b.id = a.base_project_id   " +
            "left join budgeting bt on b.id = bt.base_project_id   " +
            "left join last_settlement_review l on b.id = l.base_project_id   " +
            "left join settlement_audit_information s on b.id = s.base_project_id   " +
            "left join track_audit_info t on b.id = t.base_project_id   " +
            "left join member_manage mm on mm.id = a.member_id    " +
            "where    " +
            "(b.district = #{district} or #{district} = '') and    " +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
            "mm.dep_id = '2'    " +
            "group by    " +
            "a.member_id   ")
    List<PerformanceDistributionChart> findCostPerformanceDistributionChart(pageVo pageVo);

    @Select("select    " +
            "m.member_name username,   " +
            "SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,   " +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedMonth   " +
            "from achievements_info a     " +
            "left join base_project b on b.id = a.base_project_id   " +
            "left join budgeting bt on b.id = bt.base_project_id   " +
            "left join last_settlement_review l on b.id = l.base_project_id   " +
            "left join settlement_audit_information s on b.id = s.base_project_id   " +
            "left join track_audit_info t on b.id = t.base_project_id    " +
            "left join member_manage m on m.id = a.member_id   " +
            "where    " +
            "(b.district = #{district} or #{district} = '') and    " +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
            "(bt.budgeting_time < #{endTime} or #{endTime}  = '')   " +
            "group by    " +
            "a.member_id")
    List<PerformanPeople> performanceAccrualAndSummaryList(pageVo pageVo);

    @Select(
            "SELECT   " +
                    "YEAR(create_time) yeartime,   " +
                    "MONTH(create_time) monthTime,   " +
                    "SUM(IFNULL(actual_amount,0)) total   " +
                    "FROM   " +
                    "employee_achievements_info   " +
                    "WHERE   " +
                    "del_flag = '0'   " +
                    "and   " +
                    "(district = #{district} or #{district} = '')   " +
                    "and   " +
                    "create_time >= #{startTime}   " +
                    "and   " +
                    "(create_time <= #{endTime} or  #{endTime} = '')   " +
                    "GROUP BY   " +
                    "YEAR(create_time),   " +
                    "MONTH(create_time)"
    )
    List<OneCensus2> MemberAchievementsCensus(CostVo2 costVo2);

    @Select(
            "SELECT \n" +
                    "YEAR(create_time) yearTime,\n" +
                    "MONTH(create_time) monthTime,\n" +
                    "SUM(accrued_amount) total,\n" +
                    "SUM(actual_amount) total2,\n" +
                    "SUM(balance) total3\n" +
                    "FROM\n" +
                    "employee_achievements_info\n" +
                    "WHERE\n" +
                    "del_flag = '0'\n" +
                    "and\n" +
                    "(achievements_type = #{id} or #{id} = '') \n" +
                    "and\n" +
                    "(achievements_type != #{id2} or #{id2} = '')\n" +
                    "and\n" +
                    "(district = #{district} or #{district} = '') \n" +
                    "and\n" +
                    "create_time >= #{startTime}\n" +
                    "and\n" +
                    "(create_time <= #{endTime} or  #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "YEAR(create_time),\n" +
                    "MONTH(create_time)"
    )
    List<OneCensus4> MemberAchievementsCensus2(CostVo2 costVo2);


    @Select("select SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision ,   " +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth,   " +
            "YEAR(bt.budgeting_time) yearTime,   " +
            "MONTH(bt.budgeting_time) monthTime,   " +
            "SUM(a.budget_achievements) budgetAchievements,     " +
            "SUM(a.upsubmit_achievements) upsubmitAchievements,     " +
            "SUM(a.downsubmit_achievements) downsubmitAchievements,     " +
            "SUM(a.truck_achievements) truckAchievements     " +
            "from achievements_info a    " +
            "LEFT JOIN base_project b ON a.base_project_id = b.id   " +
            "LEFT JOIN budgeting bt ON bt.base_project_id = b.id   " +
            "where    " +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and    " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
            "(b.district = #{district} or #{district} = '')   " +
            "group by    " +
            "YEAR(bt.budgeting_time),   " +
            "MONTH(bt.budgeting_time)   ")
    List<PerformanceDistributionChart> findCostPerformanceChart(pageVo pageVo);

    @Select("select SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision ,   " +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth,   " +
            "YEAR(bt.budgeting_time) yearTime,   " +
            "MONTH(bt.budgeting_time) monthTime,   " +
            "SUM(a.budget_achievements) budgetAchievements,     " +
            "SUM(a.upsubmit_achievements) upsubmitAchievements,     " +
            "SUM(a.downsubmit_achievements) downsubmitAchievements,     " +
            "SUM(a.truck_achievements) truckAchievements     " +
            "from achievements_info a    " +
            "LEFT JOIN base_project b ON a.base_project_id = b.id   " +
            "LEFT JOIN budgeting bt ON bt.base_project_id = b.id   " +
            "where    " +
            "(bt.budgeting_time > #{startTime} or #{startTime} = '') and    " +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and    " +
            "(b.district = #{district} or #{district} = '')   " +
            "group by    " +
            "YEAR(bt.budgeting_time),   " +
            "MONTH(bt.budgeting_time)   ")
    List<PerformanceDistributionChart> findCostPerformanceChart2(CostVo2 costVo2);

    @Select("SELECT SUM(a.budget_achievements) budgetAchievements,SUM(a.upsubmit_achievements) upsubmitAchievements,SUM(a.downsubmit_achievements) downsubmitAchievements,SUM(a.truck_achievements) truckAchievements FROM `achievements_info` a")
    PerformanceDistributionChart findBTAll();

    @Select(
            "SELECT  " +
                    "IFNULL(sum(case when achievements_type != '1' then IFNULL(actual_amount,0) end ),0) total, " +
                    "IFNULL(sum(case when achievements_type = '2' then IFNULL(actual_amount,0) end ),0) budgetAchievements, " +
                    "IFNULL(sum(case when achievements_type = '3' then IFNULL(actual_amount,0) end ),0) upsubmitAchievements, " +
                    "IFNULL(sum(case when achievements_type = '4' then IFNULL(actual_amount,0) end ),0) downsubmitAchievements, " +
                    "IFNULL(sum(case when achievements_type = '5' then IFNULL(actual_amount,0) end ),0) truckAchievements " +
                    "FROM " +
                    "employee_achievements_info " +
                    "WHERE " +
                    "del_flag = '0' " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time >= #{startTime} " +
                    "and " +
                    "(create_time <= #{endTime} or  #{endTime} = '')"
    )
    PerformanceDistributionChart findBTAll2(CostVo2 costVo2);
}

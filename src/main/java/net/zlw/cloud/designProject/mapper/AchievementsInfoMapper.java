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
            "SELECT \n" +
                    "desgin_achievements,\n" +
                    "budget_achievements,\n" +
                    "upsubmit_achievements,\n" +
                    "downsubmit_achievements,\n" +
                    "truck_achievements\n" +
                    "FROM \n" +
                    "achievements_info s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(s2.district=#{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<AchievementsInfo> totalexpenditure(CostVo2 costVo2);


    @Select("select \n" +
            "year(bt.budgeting_time) yearTime,\n" +
            "month(bt.budgeting_time) monthTime,\n" +
            "SUM(a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,\n" +
            "SUM((a.desgin_achievements+a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth\n" +
            "from \n" +
            "achievements_info a  \n" +
            "left join base_project b on b.id = a.base_project_id\n" +
            "left join budgeting bt on b.id = bt.base_project_id\n" +
            "left join last_settlement_review l on b.id = l.base_project_id\n" +
            "left join settlement_audit_information s on b.id = s.base_project_id\n" +
            "left join track_audit_info t on b.id = t.base_project_id\n" +
            "left join design_info d on b.id = d.base_project_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and \n" +
            "(l.compile_time > #{statTime} or #{statTime} = '') and \n" +
            "(s.compile_time > #{statTime} or #{statTime} = '') and \n" +
            "(t.create_time > #{statTime} or #{statTime} = '') and \n" +
            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and \n" +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and \n" +
            "(l.compile_time < #{endTime} or #{endTime} = '') and \n" +
            "(s.compile_time < #{endTime} or #{endTime} = '') and \n" +
            "(t.create_time < #{endTime} or #{endTime} = '') and \n" +
            "(d.blueprint_start_time < #{endTime} or #{endTime} = '') \n" +
            "group by \n" +
            "year(bt.budgeting_time),\n" +
            "month(bt.budgeting_time)")
    List<PerformanceDistributionChart> findAllPerformanceDistributionChart(pageVo pageVo);

    @Select("select \n" +
            "year(d.blueprint_start_time) yearTime,\n" +
            "month(d.blueprint_start_time) monthTime,\n" +
            "SUM(a.desgin_achievements) as PerformanceProvision,\n" +
            "SUM((a.desgin_achievements)*0.8) as IssuedDuringMmonth\n" +
            "from \n" +
            "achievements_info a  \n" +
            "left join base_project b on b.id = a.base_project_id\n" +
            "left join design_info d on b.id = d.base_project_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(d.blueprint_start_time > #{statTime} or #{statTime} = '') and \n" +
            "(d.blueprint_start_time < #{endTime} or #{endTime} = '') \n" +
            "group by \n" +
            "year(d.blueprint_start_time),\n" +
            "month(d.blueprint_start_time)")
    List<PerformanceDistributionChart> findDesignPerformanceDistributionChart(pageVo pageVo);

    @Select("select \n" +
            "year(bt.budgeting_time) yearTime,\n" +
            "month(bt.budgeting_time) monthTime,\n" +
            "SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,\n" +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedDuringMmonth\n" +
            "from \n" +
            "achievements_info a  \n" +
            "left join base_project b on b.id = a.base_project_id\n" +
            "left join budgeting bt on b.id = bt.base_project_id\n" +
            "left join last_settlement_review l on b.id = l.base_project_id\n" +
            "left join settlement_audit_information s on b.id = s.base_project_id\n" +
            "left join track_audit_info t on b.id = t.base_project_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and \n" +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '')  \n" +
            "group by \n" +
            "year(bt.budgeting_time),\n" +
            "month(bt.budgeting_time)")
    List<PerformanceDistributionChart> findCostPerformanceDistributionChart(pageVo pageVo);

    @Select("select \n" +
            "m.member_name username,\n" +
            "SUM(a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements) as PerformanceProvision,\n" +
            "SUM((a.budget_achievements+a.upsubmit_achievements+a.downsubmit_achievements+a.truck_achievements)*0.8) as IssuedMonth\n" +
            "from achievements_info a  \n" +
            "left join base_project b on b.id = a.base_project_id\n" +
            "left join budgeting bt on b.id = bt.base_project_id\n" +
            "left join last_settlement_review l on b.id = l.base_project_id\n" +
            "left join settlement_audit_information s on b.id = s.base_project_id\n" +
            "left join track_audit_info t on b.id = t.base_project_id \n" +
            "left join member_manage m on m.id = a.member_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and \n" +
            "(bt.budgeting_time < #{endTime} or #{endTime}  = '')\n" +
            "group by \n" +
            "a.member_id")
    List<PerformanPeople> performanceAccrualAndSummaryList(pageVo pageVo);

    @Select(
            "SELECT\n" +
                    "YEAR(s1.create_time) yeartime,\n" +
                    "MONTH(s1.create_time) monthTime,\n" +
                    "SUM(IFNULL(desgin_achievements,0)+IFNULL(budget_achievements,0)+IFNULL(upsubmit_achievements,0)+IFNULL(downsubmit_achievements,0)+IFNULL(truck_achievements,0)) total\n" +
                    "FROM\t\n" +
                    "base_project s1 LEFT JOIN achievements_info s2 ON s1.id = s2.base_project_id\t\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\t\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "YEAR(s1.create_time),\n" +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus2> MemberAchievementsCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s3.member_name memberName, \n" +
                    "SUM(IFNULL(desgin_achievements,0)\n" +
                    "+IFNULL(budget_achievements,0)\n" +
                    "+IFNULL(upsubmit_achievements,0)\n" +
                    "+IFNULL(downsubmit_achievements,0)\n" +
                    "+IFNULL(truck_achievements,0)\n" +
                    ") total\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN achievements_info s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN member_manage s3 ON s2.member_id = s3.id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(dep_id = #{id} or #{id} ='')\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "member_id\n" +
                    "ORDER BY \n" +
                    "member_id desc"
    )
    List<OneCensus4> MemberAchievementsCensus2(CostVo2 costVo2);
}

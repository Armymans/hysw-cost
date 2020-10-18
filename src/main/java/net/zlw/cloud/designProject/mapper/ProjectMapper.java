package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProjectMapper extends Mapper<BaseProject> {
    @Update("" +
            "UPDATE base_project set  " +
            "virtual_code = #{mergeNum}, " +
            "merge_flag = #{falg}, " +
            "del_flag = #{status} " +
            "where  " +
            "id = #{id}")
    void updataMerga(@Param("mergeNum") String mergeNum, @Param("falg") String flag, @Param("id") String id, @Param("status") String status);

    @Update(
            "update  base_project  " +
                    "set  " +
                    "virtual_code = Null, " +
                    "merge_flag = Null, " +
                    "del_flag = \"0\" " +
                    "where  " +
                    "virtual_code=#{code}"
    )
    void reduction(@Param("code") String virtualCode);

    @Update(
            "update  base_project  " +
                    "set  " +
                    "del_flag = \"1\" " +
                    "where  " +
                    "id=#{id}"
    )
    void deleteProject(@Param("id") String baseProjectId);

    @Select(
            "select  " +
                    "count(desgin_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "desgin_status != \"4\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer desginStatusSensus1(@Param("id") String id);

    @Select(
            "select  " +
                    "count(desgin_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "desgin_status = \"4\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer desginStatusSensus2(@Param("id") String id);

    @Select(
            "select  " +
                    "count(budget_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "budget_status != \"4\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer budgetStatusSensus1(@Param("id") String id);

    @Select(
            "select  " +
                    "count(budget_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "budget_status = \"4\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer budgetStatusSensus2(@Param("id") String id);

    @Select(
            "select  " +
                    "count(track_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "track_status != \"5\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer trackStatusSensus1(@Param("id") String id);

    @Select(
            "select  " +
                    "count(track_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "track_status = \"5\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer trackStatusSensus2(@Param("id") String id);

    @Select(
            "select  " +
                    "count(visa_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "visa_status != \"6\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer visaStatusSensus1(@Param("id") String id);
    @Select(
            "select  " +
                    "count(visa_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "visa_status = \"6\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer visaStatusSensus2(@Param("id") String id);
    @Select(
            "select  " +
                    "count(progress_payment_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "progress_payment_status != \"6\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer progressPaymentStatusSensus1(@Param("id") String id);
    @Select(
            "select  " +
                    "count(progress_payment_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "progress_payment_status = \"6\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer progressPaymentStatusSensus2(@Param("id") String id);

    @Select(
            "select  " +
                    "count(settle_accounts_status) 未完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "settle_accounts_status != \"5\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer settleAccountsStatusSensus1(@Param("id") String id);

    @Select(
            "select  " +
                    "count(settle_accounts_status) 已完成 " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "settle_accounts_status = \"5\"" +
                    "and " +
                    "building_project_id = #{id}" +
                    "and " +
                    "del_flag = '0'"
    )
    Integer settleAccountsStatusSensus2(@Param("id") String id);

    @Select(
            "SELECT " +
                    "actual_start_time " +
                    "FROM building_project " +
                    "where  " +
                    "id = #{id}"
    )
    String buildingStartTime(@Param("id") String id);

    @Select(
            "SELECT " +
                    "actual_end_time " +
                    "FROM building_project " +
                    "where  " +
                    "id = #{id}"
    )
    String buildingEndTime(@Param("id") String id);

    @Select(
            "SELECT " +
                    "year(bp.create_time) yeartime, " +
                    "month(bp.create_time) monthtime, " +
                    "SUM(design_category = 1) AS municipalPipeline, " +
                    "SUM(design_category = 2) AS networkReconstruction, " +
                    "SUM(design_category = 3) AS newCommunity, " +
                    "SUM(design_category = 4) AS secondaryWater, " +
                    "SUM(design_category = 5) AS commercialHouseholds, " +
                    "SUM(design_category = 6) AS waterResidents, " +
                    "SUM(design_category = 7) AS administration " +
                    "FROM " +
                    "base_project bp " +
                    "where " +
                    "founder_id = #{id} " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time>= #{startTime} " +
                    "and  " +
                    "(create_time<= #{endTime} or  #{endTime} = '')  " +
                    "and " +
                    "del_flag = '0' " +
                    "group by year(bp.create_time), " +
                    "month(bp.create_time) " +
                    "HAVING " +
                    "(yeartime = #{year} or #{year} = '') " +
                    "and " +
                    "(monthtime = #{month} or #{month} = '')"
    )
    List<OneCensus> censusList(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.district, " +
                    "s2.blueprint_start_time " +
                    "FROM " +
                    "base_project s1, " +
                    "design_info s2 " +
                    "where " +
                    "s1.id = s2.base_project_id " +
                    "and " +
                    "s1.founder_id = #{id} " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "blueprint_start_time>= #{startTime} " +
                    "and  " +
                    "(blueprint_start_time<= #{endTime} or  #{endTime} = '') and " +
                    "s1.del_flag = '0'"
    )
    List<BaseProject> individualList(IndividualVo individualVo);

    @Select(
            "select  " +
                    "count(budget_status) " +
                    "from " +
                    "base_project " +
                    "where " +
                    "budget_status != '4' " +
                    "and " +
                    "founder_id = #{id} " +
                    "and " +
                    "del_flag = '0'" +
                    "and " +
                    "(district = #{district} or  #{district} = '')"
    )
    String budgetingCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "count(progress_payment_status) " +
                    "from " +
                    "base_project " +
                    "where " +
                    "progress_payment_status != '6' " +
                    "and " +
                    "founder_id = #{id} " +
                    "and " +
                    "del_flag = '0'" +
                    "and " +
                    "(district = #{district} or  #{district} = '')"
    )
    String progressPaymentInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "count(visa_status) " +
                    "from " +
                    "base_project " +
                    "where " +
                    "visa_status != '6' " +
                    "and " +
                    "founder_id =  #{id} " +
                    "and " +
                    "del_flag = '0'" +
                    "and " +
                    "(district = #{district} or  #{district} = '')"
    )
    String visaApplyChangeInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "count(track_status) " +
                    "from " +
                    "base_project " +
                    "where " +
                    "track_status != '5' " +
                    "and " +
                    "founder_id = #{id} " +
                    "and " +
                    "del_flag = '0'" +
                    "and " +
                    "(district = #{district} or  #{district} = '')"
    )
    String trackAuditInfoCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "count(settle_accounts_status) " +
                    "from " +
                    "base_project " +
                    "where " +
                    "settle_accounts_status != '5' " +
                    "and " +
                    "founder_id = #{id} " +
                    "and " +
                    "del_flag = '0'" +
                    "and " +
                    "(district = #{district} or  #{district} = '')"
    )
    String settleAccountsCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select\n" +
                    "year(s1.create_time) yeartime,\n" +
                    "count(budget_status) budget,\n" +
                    "count(track_status) track,\n" +
                    "count(visa_status) visa,\n" +
                    "count(progress_payment_status) progresspayment,\n" +
                    "count(settle_accounts_status) settleaccounts\n" +
                    "from\n" +
                    "base_project s1\n" +
                    "where\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                        "create_time>= #{startTime}\n" +
                    "and \n" +
                    "(create_time<= #{endTime} or  #{endTime} = '') \n" +
                    "and\n" +
                    "del_flag = '0'\n" +
                    "group by year(s1.create_time)\n" +
                    "HAVING\n" +
                    "(yeartime = #{year} or #{year} = '')"
    )
    OneCensus2 costCensus(CostVo2 costVo2);

    @Select(
            "select " +
                    "year(bp.create_time) yeartime, " +
                    "month(bp.create_time) monthTime , " +
                    "count(budget_status) budget, " +
                    "count(track_status) track, " +
                    "count(visa_status) visa, " +
                    "count(progress_payment_status) progresspayment, " +
                    "count(settle_accounts_status) settleaccounts " +
                    "from " +
                    "base_project bp " +
                    " where " +
                    " founder_id = #{id} " +
                    " and " +
                    " (district = #{district} or #{district} = '') " +
                    " and " +
                    " create_time>= #{startTime} " +
                    " and  " +
                    " (create_time<= #{endTime} or  #{endTime} = '')  " +
                    " and " +
                    "del_flag = '0' " +
                    "group by year(bp.create_time), " +
                    "month(bp.create_time)  " +
                    "HAVING " +
                    " (yeartime = #{year} or #{year} = '') " +
                    " and " +
                    " (monthTime = #{month} or #{month} = '')"
    )
    List<OneCensus2> costCensusList(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "COUNT(s2.id) " +
                    "FROM " +
                    "building_project s1, " +
                    "base_project s2 " +
                    "WHERE " +
                    "s1.id = s2.building_project_id " +
                    "and " +
                    "s1.id = #{id}"
    )
    String projectCount(@Param("id") String id);


    @Select(
            "select " +
                    "ifnull(count(desgin_status ),0) desginStatus, " +
                    "ifnull(count(budget_status),0) budgetingCount, " +
                    "ifnull(count(track_status),0) trackAuditInfoCount, " +
                    "ifnull(count(visa_status),0) visaApplyChangeInformationCount, " +
                    "ifnull(count(progress_payment_status),0) progressPaymentInformation, " +
                    "ifnull(count(settle_accounts_status),0) settleAccountsCount " +
                    "from " +
                    "base_project "+
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 AllprojectCount(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "ifnull(sum(desgin_status = 1),0) desginStatus,\n" +
                    "ifnull(sum(budget_status = 1),0) budgetingCount,\n" +
                    "ifnull(sum(track_status = 1),0) trackAuditInfoCount,\n" +
                    "ifnull(sum(visa_status = 1),0) visaApplyChangeInformationCount,\n" +
                    "ifnull(sum(progress_payment_status = 1),0) progressPaymentInformation,\n" +
                    "ifnull(sum(settle_accounts_status = 1),0) settleAccountsCount\n" +
                    "from\n" +
                    "base_project \n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 withAuditCount(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "ifnull(sum(desgin_status = 4),0) desginStatus,\n" +
                    "ifnull(sum(budget_status = 4),0) budgetingCount,\n" +
                    "ifnull(sum(track_status = 5),0) trackAuditInfoCount,\n" +
                    "ifnull(sum(visa_status = 6),0) visaApplyChangeInformationCount,\n" +
                    "ifnull(sum(progress_payment_status = 6),0) progressPaymentInformation,\n" +
                    "ifnull(sum(settle_accounts_status = 5),0) settleAccountsCount\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 conductCount(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "ifnull(sum(desgin_status != 4),0) desginStatus,\n" +
                    "ifnull(sum(budget_status != 4),0) budgetingCount,\n" +
                    "ifnull(sum(track_status != 5),0) trackAuditInfoCount,\n" +
                    "ifnull(sum(visa_status != 6),0) visaApplyChangeInformationCount,\n" +
                    "ifnull(sum(progress_payment_status != 6),0) progressPaymentInformation,\n" +
                    "ifnull(sum(settle_accounts_status != 5),0) settleAccountsCount\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 completeCount(CostVo2 costVo2);


    @Select(
            "SELECT " +
                    "id, " +
                    "project_name, " +
                    "project_flow " +
                    "FROM  " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time >= #{startTime} " +
                    "and " +
                    "(create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "del_flag = '0' " +
                    "and " +
                    "( " +
                    "project_num like CONCAT('%',#{keyword},'%') or " +
                    "project_name like  CONCAT('%',#{keyword},'%')" +
                    ")"
    )
    List<BaseProject> projectFlow(CostVo2 costVo2);


    @Select(
            "\tselect " +
                    "\tyear(create_time) yearTime, " +
                    "\tMONTH(create_time) monthTime, " +
                    "\tcount(desgin_status ) desginStatus, " +
                    "\tcount(budget_status) budgetingCount, " +
                    "\tcount(track_status) trackAuditInfoCount, " +
                    "\tcount(visa_status) visaApplyChangeInformationCount, " +
                    "\tcount(progress_payment_status) progressPaymentInformation, " +
                    "\tcount(settle_accounts_status) settleAccountsCount " +
                    "\tfrom " +
                    "\tbase_project s1 " +
                    "\twhere " +
                    "\t(district = #{district} or #{district} = '') " +
                    "\tand " +
                    "\tcreate_time >= #{startTime} " +
                    "\tand " +
                    "\t(create_time <= #{endTime} or #{endTime} = '') " +
                    "\tGROUP BY " +
                    "\tyear(s1.create_time), " +
                    "\tMONTH(s1.create_time) " +
                    "\thaving " +
                    "\t(yearTime = #{year} or #{year} = '') " +
                    "\tand " +
                    "\t(monthTime = #{month} or #{month}= '')"
    )
    List<CostVo3> prjectCensus(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(s2.create_time) yeartime, " +
                    "sum(design_category = 1) municipalPipeline, " +
                    "sum(design_category = 2) networkReconstruction, " +
                    "sum(design_category = 3) newCommunity, " +
                    "sum(design_category = 4) secondaryWater, " +
                    "sum(design_category = 5) commercialHouseholds, " +
                    "sum(design_category = 6) waterResidents, " +
                    "sum(design_category = 7) administration " +
                    "FROM  " +
                    "income_info s1, " +
                    "base_project s2 " +
                    "where " +
                    "s1.base_project_id = s2.id " +
                    "and " +
                    "(s2.district = #{district} or #{district} = '') " +
                    "and " +
                    "s2.create_time >= #{startTime} " +
                    "and " +
                    "(s2.create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "s2.del_flag = '0' " +
                    "GROUP BY " +
                    "YEAR(s2.create_time) " +
                    "HAVING " +
                    "(yeartime = #{year} or #{year} = '')"
    )
    OneCensus projectIncomeCensus(CostVo2 costVo2);
    @Select(
            "SELECT " +
                    "sum(design_category = 1) municipalPipeline, " +
                    "sum(design_category = 2) networkReconstruction, " +
                    "sum(design_category = 3) newCommunity, " +
                    "sum(design_category = 4) secondaryWater, " +
                    "sum(design_category = 5) commercialHouseholds, " +
                    "sum(design_category = 6) waterResidents, " +
                    "sum(design_category = 7) administration " +
                    "FROM " +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id " +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id " +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id " +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id " +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id " +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id " +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id " +
                    "where " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus projectExpenditureCensus(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(s1.create_time) yearTime, " +
                    "MONTH(s1.create_time) monthTime, " +
                    "sum( " +
                    "IFNULL(s2.outsource_money,0)+ " +
                    "IFNULL(s3.amount_outsourcing,0)+ " +
                    "IFNULL(s4.amount_outsourcing,0)+ " +
                    "IFNULL(s5.outsourcing_amount,0)+ " +
                    "IFNULL(s6.amount_outsourcing,0)+ " +
                    "IFNULL(s7.amount_outsourcing,0)+ " +
                    "IFNULL(s8.outsource_money,0) " +
                    ") outMoney, " +
                    "sum( " +
                    "IFNULL(s9.desgin_achievements,0)+ " +
                    "IFNULL(s9.budget_achievements,0)+ " +
                    "IFNULL(s9.upsubmit_achievements,0)+ " +
                    "IFNULL(s9.downsubmit_achievements,0)+ " +
                    "IFNULL(s9.truck_achievements,0) " +
                    ") advMoney " +
                    "FROM " +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id " +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id " +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id " +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id " +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id " +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id " +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id " +
                    "where " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    "GROUP BY " +
                    "YEAR(s1.create_time), " +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus3> expenditureAnalysis(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_name, " +
                    "( CASE s1.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
                    "(\n" +
                    "\tCASE\n" +
                    "\t\t\ts1.design_category \n" +
                    "\t\t\tWHEN '1' THEN\n" +
                    "\t\t\t'市政管道' \n" +
                    "\t\t\tWHEN '2' THEN\n" +
                    "\t\t\t'管网改造' \n" +
                    "\t\t\tWHEN '3' THEN\n" +
                    "\t\t\t'新建小区' \n" +
                    "\t\t\tWHEN '4' THEN\n" +
                    "\t\t\t'二次供水项目' \n" +
                    "\t\t\tWHEN '5' THEN\n" +
                    "\t\t\t'工商户' \n" +
                    "\t\t\tWHEN '6' THEN\n" +
                    "\t\t\t'居民装接水' \n" +
                    "\t\t\tWHEN '7' THEN\n" +
                    "\t\t\t'行政事业' \n" +
                    "\t\tEND \n" +
                    "\t\t) AS designCategory,\n" +
                    "s6.review_number, " +
                    "s7.authorized_number, " +
                    "sum( " +
                    "IFNULL(s2.outsource_money,0)+ " +
                    "IFNULL(s3.amount_outsourcing,0)+ " +
                    "IFNULL(s4.amount_outsourcing,0)+ " +
                    "IFNULL(s5.outsourcing_amount,0)+ " +
                    "IFNULL(s6.amount_outsourcing,0)+ " +
                    "IFNULL(s7.amount_outsourcing,0)+ " +
                    "IFNULL(s8.outsource_money,0) " +
                    ") outMoney, " +
                    "sum( " +
                    "IFNULL(s9.desgin_achievements,0)+ " +
                    "IFNULL(s9.budget_achievements,0)+ " +
                    "IFNULL(s9.upsubmit_achievements,0)+ " +
                    "IFNULL(s9.downsubmit_achievements,0)+ " +
                    "IFNULL(s9.truck_achievements,0) " +
                    ") advMoney " +
                    "from " +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id " +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id " +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id " +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id " +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id " +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id " +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id " +
                    "where " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    "GROUP BY " +
                    "s1.id "
    )
    List<BaseProject> BaseProjectExpenditureList(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_name, " +
                    "( CASE s1.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
                    "s2.amount_cost amountCost, " +
                    "s3.cost_total_amount costTotalAmount, " +
                    "s4.bidding_price_control biddingPriceControl, " +
                    "s5.total_payment_amount totalPaymentAmount, " +
                    "s6.review_number reviewNumber, " +
                    "s7.authorized_number authorizedNumber " +
                    "FROM " +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN cost_preparation s3 ON s1.id = s3.base_project_id " +
                    "LEFT JOIN very_establishment s4 ON s1.id = s4.base_project_id " +
                    "LEFT JOIN progress_payment_total_payment s5 ON s1.id = s5.base_project_id " +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id " +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id " +
                    "where " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and " +
                    "( " +
                    "cea_num like  CONCAT('%',#{keyword},'%')  or " +
                    "project_name  like  CONCAT('%',#{keyword},'%')   " +
                    ")"
    )
    List<BaseProject> BaseProjectInfoCensus(CostVo2 costVo2);

    @Select(
            "select  " +
                    "s2.id, " +
                    "s2.cea_num, " +
                    "s2.project_num, " +
                    "s2.project_name, " +
                    "s2.design_category, " +
                    "s2.district, " +
                    "s3.designer, " +
                    "s3.design_change_time designChangeTime " +
                    "from " +
                    "design_info s1, " +
                    "base_project s2, " +
                    "design_change_info s3 " +
                    "where " +
                    "s1.base_project_id = s2.id " +
                    "and " +
                    "s1.id = s3.design_info_id " +
                    "and " +
                    "(district = #{district} or #{district} =  '') " +
                    "and " +
                    "s3.design_change_time >= #{startTime} " +
                    "and " +
                    "(s3.design_change_time <= #{endTime} or #{endTime} = '')\t " +
                    "and " +
                    "( " +
                    "s2.cea_num like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.project_num like  CONCAT('%',#{keyword},'%')  or " +
                    "s2.project_name like  CONCAT('%',#{keyword},'%') or " +
                    "s3.designer like  CONCAT('%',#{keyword},'%')  " +
                    ")" +
                    "and " +
                    "s2.del_flag = '0'"
    )
    List<BaseProject> BaseProjectDesignList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.design_category,\n" +
                    "s1.district,\n" +
                    "IFNULL(s2.total_payment_amount,0) totalPaymentAmount,\n" +
                    "IFNULL(s2.accumulative_payment_proportion,0) accumulativePaymentProportion\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN progress_payment_total_payment s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "and " +
                    "s1.del_flag = '0'"
    )
    List<BaseProject> progressPaymentList(CostVo2 costVo2);

    @Select(
            "SELECT \n" +
                    "COUNT(*)\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN \n" +
                    "progress_payment_information s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "AND\n" +
                    "s1.del_flag = '0'"
    )
    Integer progressPaymentCount(CostVo2 costVo2);

    @Select(
            "SELECT \n" +
                    "SUM(IFNULL(current_payment_Information,0))\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN \n" +
                    "progress_payment_information s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "AND\n" +
                    "s1.del_flag = '0'"
    )
    Double progressPaymentSum(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.design_category,\n" +
                    "s1.district,\n" +
                    "s2.amount_visa_change,\n" +
                    "s2.contract_amount,\n" +
                    "s2.compile_time\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "visa_change_information s2\n" +
                    "where\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time  <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")"
    )
    List<BaseProject> projectVisaChangeList(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "COUNT(*) " +
                    "FROM " +
                    "base_project s1 LEFT JOIN visa_change_information s2 ON s1.id = s2.base_project_id " +
                    "WHERE " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "s2.compile_time >= #{startTime} " +
                    "and " +
                    "(s2.compile_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "( " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or " +
                    "project_name  like  CONCAT('%',#{keyword},'%')  " +
                    ")" +
                    "and " +
                    "s1.del_flag = '0'"
    )
    Double VisaChangeCount(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "SUM(s2.amount_visa_change) " +
                    "FROM " +
                    "base_project s1 LEFT JOIN visa_change_information s2 ON s1.id = s2.base_project_id " +
                    "WHERE " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "s2.compile_time >= #{startTime} " +
                    "and " +
                    "(s2.compile_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "( " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or " +
                    "project_name  like  CONCAT('%',#{keyword},'%')  " +
                    ")" +
                    "and " +
                    "s1.del_flag = '0'"
    )
    Double VisaChangeMoney(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(s1.create_time) yeartime, " +
                    "MONTH(s1.create_time) monthtime, " +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber, " +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney, " +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber, " +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber " +
                    "FROM " +
                    "base_project s1 LEFT JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id  " +
                    "LEFT JOIN settlement_info s4 ON s1.id = s4.base_project_Id " +
                    "where " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "s1.del_flag = '0'"+
                    "GROUP BY year(s1.create_time),MONTH(s1.create_time)"
    )
    List<OneCensus4>projectSettlementCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber,\n" +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney,\n" +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber,\n" +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id \n" +
                    "LEFT JOIN settlement_info s4 ON s1.id = s4.base_project_Id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus4 projectSettlementCount(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(s1.create_time) yeartime, " +
                    "MONTH(s1.create_time) monthtime, " +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber, " +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney, " +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber, " +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber " +
                    "FROM " +
                    "base_project s1 LEFT JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id  " +
                    "LEFT JOIN settlement_info s4 ON s1.id = s4.base_project_Id " +
                    "where " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and " +
                    "s1.del_flag = '0'"
    )
    OneCensus4 projectSettlementSum(CostVo2 costVo2);

    @Select(
            "SELECT  " +
                    "sum(desgin_status = '1') revie, " +
                    "sum(desgin_status = '2') plot, " +
                    "sum(desgin_status = '4') comple, " +
                    "sum(desgin_status != '20') total " +
                    "FROM  " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or  #{district} = '')" +
                    "and " +
                    "del_flag = '0'"
    )
    OneCensus3 projectDesginCount(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "id, " +
                    "project_name projectName, " +
                    "(desgin_status != '2') plot, " +
                    "(desgin_status = '4') comple " +
                    "from " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time >= #{startTime} " +
                    "and " +
                    "(create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "(project_name like  CONCAT('%',#{keyword},'%'))" +
                    "and " +
                    "del_flag = '0'"
    )
    List<OneCensus3> projectDesginStatus(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "year(bp.create_time) yeartime, " +
                    "month(bp.create_time) monthtime, " +
                    "SUM(design_category = 1) AS municipalPipeline, " +
                    "SUM(design_category = 2) AS networkReconstruction, " +
                    "SUM(design_category = 3) AS newCommunity, " +
                    "SUM(design_category = 4) AS secondaryWater, " +
                    "SUM(design_category = 5) AS commercialHouseholds, " +
                    "SUM(design_category = 6) AS waterResidents, " +
                    "SUM(design_category = 7) AS administration " +
                    "FROM " +
                    "base_project bp " +
                    "where " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time>= #{startTime} " +
                    "and  " +
                    "(create_time<= #{endTime} or  #{endTime} = '')  " +
                    "and " +
                    "del_flag = '0' " +
                    "group by year(bp.create_time), " +
                    "month(bp.create_time) " +
                    "HAVING " +
                    "(yeartime = #{year} or #{year} = '') " +
                    "and " +
                    "(monthtime = #{month} or #{month} = '')"
    )
    List<OneCensus> censusList2(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "count(s1.id) notAmount,\n" +
                    "count(s3.official_receipts) wujiangAmount,\n" +
                    "count(s4.official_receipts) anhuiAnount\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN design_info s2 on s1.id = s2.base_project_id\n" +
                    "LEFT JOIN wujiang_money_info s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN anhui_money_info s4 ON s1.id = s4.base_project_id\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    OneCensus5 desiginMoneyCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "sum(s2.outsource = 0) outsourceYes,\n" +
                    "sum(s2.outsource = 1) outsourceNo\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    OneCensus5 desiginoutsource(CostVo2 costVo2);

    @Select(
                "\tSELECT\n" +
                        "\tYEAR(s1.create_time) yearTime,\n" +
                        "\tMONTH(s1.create_time) monthTime,\n" +
                        "\tSUM(IFNULL(s2.desgin_achievements,0)) desginAchievements\n" +
                        "\tFROM\n" +
                        "\tbase_project s1 LEFT JOIN achievements_info s2 ON s1.id = s2.base_project_id\n" +
                        "\twhere\n" +
                        "\t(s1.district = #{district} or #{district} = '')\n" +
                        "\tand\n" +
                        "\ts1.create_time >= #{startTime}\n" +
                        "\tand\n" +
                        "\t(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                        "\tand\n" +
                        "\ts1.del_flag = '0'\n" +
                        "\tGROUP BY\n" +
                        "\tYEAR(s1.create_time),\n" +
                        "\tMONTH(s1.create_time)"
    )
    List<OneCensus6> desiginAchievementsCensus(CostVo2 costVo2);

    @Select(
            "\tSELECT\n" +
                    "\tYEAR(s1.create_time) yearTime,\n" +
                    "\tMONTH(s1.create_time) monthTime,\n" +
                    "\tSUM(IFNULL(s2.desgin_achievements,0)) desginAchievements\n" +
                    "\tFROM\n" +
                    "\tbase_project s1 LEFT JOIN achievements_info s2 ON s1.id = s2.base_project_id\n" +
                    "\twhere\n" +
                    "\ts2.member_id = #{id}\n" +
                    "\tand\n" +
                    "\t(s1.district = #{district} or #{district} = '')\n" +
                    "\tand\n" +
                    "\ts1.create_time >= #{startTime}\n" +
                    "\tand\n" +
                    "\t(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "\tand\n" +
                    "\ts1.del_flag = '0'\n" +
                    "\tGROUP BY\n" +
                    "\tYEAR(s1.create_time),\n" +
                    "\tMONTH(s1.create_time)"
    )
    List<OneCensus6> desiginAchievementsOneCensus(CostVo2 costVo2);

    @Select(
            "\tSELECT\n" +
                    "\tYEAR(s1.create_time) yearTime,\n" +
                    "\tSUM(IFNULL(s2.desgin_achievements,0)) desginAchievements\n" +
                    "\tFROM\n" +
                    "\tbase_project s1 LEFT JOIN achievements_info s2 ON s1.id = s2.base_project_id\n" +
                    "\twhere\n" +
                    "\ts2.member_id = #{id}\n" +
                    "\tand\n" +
                    "\t(s1.district = #{district} or #{district} = '')\n" +
                    "\tand\n" +
                    "\ts1.create_time >= #{startTime}\n" +
                    "\tand\n" +
                    "\t(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "\tand\n" +
                    "\ts1.del_flag = '0'\n" +
                    "\tGROUP BY\n" +
                    "\tYEAR(s1.create_time)"
    )
    List<OneCensus6> desiginAchievementsOneCensus2(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "\tifnull( COUNT( budget_status ), 0 ) budgetStatus,\n" +
                    "\tifnull( COUNT( progress_payment_status ), 0 ) progressPaymentStatus,\n" +
                    "\tifnull( COUNT( visa_status ), 0 ) visaStatus,\n" +
                    "\tifnull( COUNT( settle_accounts_status ), 0 ) settleAccountsStatus \n" +
                    "FROM\n" +
                    "\tbase_project \n" +
                    "WHERE\n" +
                    "\t(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskTotal(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "\t\tifnull( sum( budget_status = '1' ), 0 ) budgetStatus,\n" +
                    "\t\tifnull( sum( progress_payment_status = '1' ), 0 ) progressPaymentStatus,\n" +
                    "\t\tifnull( sum( visa_status = '1' ), 0 ) visaStatus,\n" +
                    "\t\tifnull( sum( settle_accounts_status = '1' ), 0 ) settleAccountsStatus \n" +
                    "FROM\n" +
                    "\t\tbase_project \n" +
                    "WHERE\n" +
                    "\t\t(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskReviewed(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "ifnull(sum(budget_status='2'),0) budgetStatus,\n" +
                    "ifnull(sum(progress_payment_status='2'),0) progressPaymentStatus,\n" +
                    "ifnull(sum(visa_status='2'),0) visaStatus,\n" +
                    "ifnull(sum(settle_accounts_status='2'),0) settleAccountsStatus\n" +
                    "FROM\n" +
                    "base_project"
    )
    OneCensus6 costTaskHandle(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "ifnull(sum(budget_status='4'),0) budgetStatus,\n" +
                    "ifnull(sum(progress_payment_status='6'),0) progressPaymentStatus,\n" +
                    "ifnull(sum(visa_status='6'),0) visaStatus,\n" +
                    "ifnull(sum(settle_accounts_status='5'),0) settleAccountsStatus\n" +
                    "FROM\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskComple(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "YEAR(create_time) yearTime,\n" +
                    "MONTH(create_time) monthTime,\n" +
                    "count(budget_status) budgetStatus,\n" +
                    "count(progress_payment_status) progressPaymentStatus,\n" +
                    "count(visa_status) visaStatus ,\n" +
                    "count(settle_accounts_status) settleAccountsStatus\n" +
                    "FROM\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "create_time >= #{startTime}\n" +
                    "and\n" +
                    "(create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "del_flag = '0'\n" +
                    "GROUP BY\n" +
                    "YEAR(create_time),\n" +
                    "MONTH(create_time)"
    )
    List<OneCensus6> costTaskCensus(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "count(s2.id) budgeting,\n" +
                    "count(s3.id) lastSettlementReview,\n" +
                    "count(s4.id) settlementAuditInformation,\n" +
                    "count(s5.id) trackAuditInfo,\n" +
                    "count(s6.id) visaChangeInformation,\n" +
                    "count(s7.id) progressPaymentInformation\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN visa_change_information s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s7 ON s1.id = s7.base_project_id\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    OneCensus7 costTaskSummary(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "SUM(IFNULL(s2.outsourcing = '1',0)) budgeting,\n" +
                    "SUM(IFNULL(s3.outsourcing = '1',0)) lastSettlementReview,\n" +
                    "SUM(IFNULL(s4.outsourcing = '1',0)) settlementAuditInformation,\n" +
                    "SUM(IFNULL(s5.outsource = '1',0)) trackAuditInfo,\n" +
                    "SUM(IFNULL(s6.outsourcing = '1',0)) visaChangeInformation,\n" +
                    "SUM(IFNULL(s7.outsourcing = '1',0)) progressPaymentInformation\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN visa_change_information s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s7 ON s1.id = s7.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus7 costTaskOutsourcingCount(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "SUM(IFNULL(s2.outsourcing = '2',0)) budgeting,\n" +
                    "SUM(IFNULL(s3.outsourcing = '2',0)) lastSettlementReview,\n" +
                    "SUM(IFNULL(s4.outsourcing = '2',0)) settlementAuditInformation,\n" +
                    "SUM(IFNULL(s5.outsource = '2',0)) trackAuditInfo,\n" +
                    "SUM(IFNULL(s6.outsourcing = '2',0)) visaChangeInformation,\n" +
                    "SUM(IFNULL(s7.outsourcing = '2',0)) progressPaymentInformation\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN visa_change_information s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s7 ON s1.id = s7.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus7 costTaskNoOutsourcingCount(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s2.member_name memberName,\n" +
                    "s1.desgin_achievements desginAchievements,\n" +
                    "(s1.desgin_achievements*0.8)  desginAchievements2,\n" +
                    "(s1.desgin_achievements-(s1.desgin_achievements*0.8)) balance\n" +
                    "FROM\n" +
                    "achievements_info s1,\n" +
                    "member_manage s2,\n" +
                    "base_project s3\n" +
                    "WHERE\n" +
                    "s1.member_id = s2.id\n" +
                    "and\n" +
                    "s3.id = s1.base_project_id\n" +
                    "and\n" +
                    "s1.desgin_achievements IS NOT NULL\n" +
                    "and\n" +
                    "(s3.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s3.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s3.create_time <= #{endTime} or #{endTime} = '')"
    )
    List<OneCensus8> DesginAchievementsList(CostVo2 costVo2);
    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.project_name projectName,\n" +
                    "s1.district,\n" +
                    "s1.a_b aB,\n" +
                    "s2.take_time takeTime,\n" +
                    "s2.blueprint_start_time blueprintStartTime,\n" +
                    "s3.amount_cost amountCost,\n" +
                    "s4.desgin_achievements desginAchievements,\n" +
                    "(s4.desgin_achievements*0.8) desginAchievements2\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN budgeting s3 ON s1.id = s3.base_project_id \n" +
                    "LEFT JOIN achievements_info s4 ON s1.id = s4.base_project_id\n" +
                    "WHERE\n" +
                    "s1.desgin_status = '4'\n" +
                    "and\n" +
                    "s4.member_id = #{id}\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')"
    )
    List<OneCensus9> DesginMonthAchievementsList(CostVo2 costVo2);


    @Select(
            "select\n" +
                    "YEAR(s1.create_time) yearTime,\n" +
                    "MONTH(s1.create_time) monthTime,\n" +
                    "COUNT(s2.id) budCountA,\n" +
                    "SUM(IFNULL(s2.amount_cost,0)) amountCost,\n" +
                    "SUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountA,\n" +
                    "COUNT(s3.id) lastCount,\n" +
                    "SUM(IFNULL(s3.review_number,0)) reviewNumber,\n" +
                    "COUNT(s4.id) settCount,\n" +
                    "SUM(IFNULL(s7.sumbit_money,0)) sumbitMoney,\n" +
                    "SUM(IFNULL(s4.authorized_number,0)) authorizedNumber,\n" +
                    "SUM(IFNULL(s4.subtract_the_number,0)) subtractTheNumber,\n" +
                    "COUNT(s5.id) trackCount,\n" +
                    "SUM(IFNULL(s5.contract_amount,0)) contractAmount\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN settlement_info s7 ON s1.id = s7.base_project_Id\n" +
                    "WHERE\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "s1.a_b = '1'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "YEAR(s1.create_time),\n" +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus10> costTaskCensusList(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "YEAR(s1.create_time) yearTime,\n" +
                    "MONTH(s1.create_time) monthTime,\n" +
                    "COUNT(s2.id) budCountB,\n" +
                    "SUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountB\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id\n" +
                    "WHERE\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "s1.a_b = '2'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "YEAR(s1.create_time),\n" +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus10> costTaskCensusList2(CostVo2 costVo2);

    @Select("select\n" +
            "\t\t\tYEAR(s1.create_time) yearTime,\n" +
            "\t\t\tMONTH(s1.create_time) monthTime,\n" +
            "\t\t\tCOUNT(s2.id) budCountB,\n" +
            "\t\t\tSUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountB,\n" +
            "\t\t\ts7.budget_achievements performB\n" +
            "\t\t\tFROM\n" +
            "\t\t\tbase_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
            "\t\t\tLEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id\n" +
            "\t\t\tLEFT JOIN achievements_info s7 ON s1.id = s7.base_project_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\ts1.del_flag = '0'\n" +
            "\t\t\tand\n" +
            "\t\t\ts1.a_b = '2'\n" +
            "\t\t\tand\n" +
            "\t\t\t(s1.district = #{district} or #{district} = '')\n" +
            "\t\t\tand\n" +
            "\t\t\ts1.create_time >= #{statTime}\n" +
            "\t\t\tand\n" +
            "\t\t\t(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\tYEAR(s1.create_time),\n" +
            "\t\t\tMONTH(s1.create_time)")
    List<OneCensus10> EmployeecostTaskCensusList2(EmployeeVo employeeVo);

    @Select("select\n" +
            "\tYEAR(s1.create_time) yearTime,\n" +
            "\tMONTH(s1.create_time) monthTime,\n" +
            "\tCOUNT(s2.id) budCountA,\n" +
            "\tSUM(IFNULL(s2.amount_cost,0)) amountCost,\n" +
            "\tSUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountA,\n" +
            "\tCOUNT(s3.id) lastCount,\n" +
            "\tSUM(IFNULL(s3.review_number,0)) reviewNumber,\n" +
            "\tCOUNT(s4.id) settCount,\n" +
            "\tSUM(IFNULL(s7.sumbit_money,0)) sumbitMoney,\n" +
            "\tSUM(IFNULL(s4.authorized_number,0)) authorizedNumber,\n" +
            "\tSUM(IFNULL(s4.subtract_the_number,0)) subtractTheNumber,\n" +
            "\tCOUNT(s5.id) trackCount,\n" +
            "\tSUM(IFNULL(s5.contract_amount,0)) contractAmount,\n" +
            "\ts8.budget_achievements performA,\n" +
            "\ts8.upsubmit_achievements lastPerform,\n" +
            "\ts8.downsubmit_achievements settlePerform,\n" +
            "\ts8.truck_achievements truckPerform\n" +
            "\tFROM\n" +
            "\tbase_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
            "\tLEFT JOIN last_settlement_review s3 ON s1.id = s2.base_project_id\n" +
            "\tLEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
            "\tLEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
            "\tLEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id\n" +
            "\tLEFT JOIN settlement_info s7 ON s1.id = s7.base_project_Id\n" +
            "\tLEFT JOIN achievements_info s8 ON s1.id = s8.base_project_id\n" +
            "\tWHERE\n" +
            "\ts1.del_flag = '0'\n" +
            "\tand\n" +
            "\ts1.a_b = '1'\n" +
            "\tand\n" +
            "\t(s1.district = #{district} or #{district} = '')\n" +
            "\tand\n" +
            "\ts1.create_time >= #{statTime}\n" +
            "\tand\n" +
            "\t(s1.create_time <= #{endTime} or  #{endTime} = '')\n" +
            "\tGROUP BY\n" +
            "\tYEAR(s1.create_time),\n" +
            "\tMONTH(s1.create_time)")
    List<OneCensus10> EmployeecostTaskCensusList(EmployeeVo employeeVo);

    @Select(
            "UPDATE \n" +
                    "base_project\n" +
                    "SET \n" +
                    "merge_flag = '0' \n" +
                    "WHERE\n" +
                    "id = #{id}"
    )
    void updateMergeProject0(@Param("id") String id);

    @Select(
            "UPDATE \n" +
                    "base_project\n" +
                    "SET \n" +
                    "merge_flag = '1' \n" +
                    "WHERE\n" +
                    "id = #{id}"
    )
    void updateMergeProject1(@Param("id") String id);

    @Select("select  " +
            "application_num applicationNum, " +
            "cea_num ceaNum, " +
            "id id, " +
            "project_num projectNum, " +
            "project_name projectName, " +
            "( " +
            "case district " +
            "  when '1' then '芜湖' " +
            "  when '2' then '马鞍山' " +
            "  when '3' then '江北' " +
            "  when '4' then '吴江' " +
            "  end " +
            ") as district, " +
            "( " +
            "case design_category " +
            "  when '1' then '市政管道' " +
            "  when '2' then '管网改造' " +
            "  when '3' then '新建小区' " +
            "  when '4' then '二次供水项目' " +
            "  when '5' then '工商户' " +
            "  when '6' then '居民装接水' " +
            "  when '7' then '行政事业' " +
            "  end " +
            ") as designCategory, " +
            "construction_unit constructionUnit, " +
            "contacts contacts, " +
            "contact_number contactNumber, " +
            "customer_name customerName, " +
            "(case subject " +
            "  when '1' then '居民住户' " +
            "  when '2' then '开发商' " +
            "  when '3' then '政府事业' " +
            "  when '4' then '工商户' " +
            "  when '5' then '芜湖华衍' " +
            "  end " +
            ") as subject, " +
            "customer_phone customerPhone, " +
            "construction_organization constructionOrganization, " +
            "( " +
            "case project_nature " +
            "  when '1' then '新建' " +
            "  when '2' then '改造' " +
            "  end " +
            ") as projectNature, " +
            "( " +
            "case project_category " +
            "  when '1' then '住宅区配套' " +
            "  when '2' then '商业区配套' " +
            "  when '3' then '工商区配套' " +
            "  end " +
            ") as projectCategory, " +
            "water_address waterAddress, " +
            "( " +
            "case water_supply_type " +
            "  when '1' then '直供水' " +
            "  when '2' then '二次供水' " +
            "  end " +
            ") as waterSupplyType, " +
            "this_declaration thisDeclaration, " +
            "agent agent, " +
            "agent_phone agentPhone, " +
            "application_date applicationDate, " +
            "business_location businessLocation, " +
            "business_types businessTypes, " +
            "( " +
            "case a_b " +
            "  when '1' then 'A' " +
            "  when '2' then 'B' " +
            "  end " +
            ") as aB, " +
            "water_use waterUse, " +
            "fire_table_size fireTableSize, " +
            "classification_caliber classificationCaliber, " +
            "water_meter_diameter waterMeterDiameter, " +
            "site site, " +
            "system_number systemNumber, " +
            "proposer proposer, " +
            "application_number applicationNumber " +
            "from base_project " +
            "where id = #{id}")
    BaseProject selectById(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(IFNULL(budget_money,0)+IFNULL(upsubmit_money,0)+IFNULL(downsubmit_money,0)+IFNULL(truck_money,0))\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "income_info s2\n" +
                    "WHERE\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingIncome(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(IFNULL(s2.amount_outsourcing,0)+IFNULL(s3.amount_outsourcing,0)+IFNULL(s4.amount_outsourcing,0)+IFNULL(s5.outsource_money,0))\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN budgeting  s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id\n" +
                    "WHERE\n" +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingExpenditure1(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(IFNULL(budget_achievements,0)+IFNULL(upsubmit_achievements,0)+IFNULL(downsubmit_achievements,0)+IFNULL(truck_achievements,0))\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "achievements_info s2\n" +
                    "WHERE\n" +
                    "s1.id = s2.base_project_id\n" +
                    "AND\n" +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingExpenditure2(@Param("id") String id);
}

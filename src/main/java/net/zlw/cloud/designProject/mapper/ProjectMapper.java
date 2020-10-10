package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import javax.persistence.Id;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProjectMapper extends Mapper<BaseProject> {
    @Update("" +
            "UPDATE base_project set \n" +
            "virtual_code = #{mergeNum},\n" +
            "merge_flag = #{falg},\n" +
            "del_flag = #{status}\n" +
            "where \n" +
            "id = #{id}")
    void updataMerga(@Param("mergeNum") String mergeNum, @Param("falg") String flag, @Param("id") String id, @Param("status") String status);

    @Update(
            "update  base_project \n" +
                    "set \n" +
                    "virtual_code = Null,\n" +
                    "merge_flag = Null,\n" +
                    "del_flag = \"0\"\n" +
                    "where \n" +
                    "virtual_code=#{code}"
    )
    void reduction(@Param("code") String virtualCode);

    @Update(
            "update  base_project \n" +
                    "set \n" +
                    "del_flag = \"1\"\n" +
                    "where \n" +
                    "id=#{id}"
    )
    void deleteProject(@Param("id") String baseProjectId);

    @Select(
            "select \n" +
                    "count(desgin_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "desgin_status != \"4\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer desginStatusSensus1(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(desgin_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "desgin_status = \"4\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer desginStatusSensus2(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(budget_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "budget_status != \"4\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer budgetStatusSensus1(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(budget_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "budget_status = \"4\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer budgetStatusSensus2(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(track_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "track_status != \"5\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer trackStatusSensus1(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(track_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "track_status = \"5\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer trackStatusSensus2(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(visa_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "visa_status != \"6\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer visaStatusSensus1(@Param("id") String id);
    @Select(
            "select \n" +
                    "count(visa_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "visa_status = \"6\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer visaStatusSensus2(@Param("id") String id);
    @Select(
            "select \n" +
                    "count(progress_payment_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "progress_payment_status != \"6\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer progressPaymentStatusSensus1(@Param("id") String id);
    @Select(
            "select \n" +
                    "count(progress_payment_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "progress_payment_status = \"6\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer progressPaymentStatusSensus2(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(settle_accounts_status) 未完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "settle_accounts_status != \"5\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer settleAccountsStatusSensus1(@Param("id") String id);

    @Select(
            "select \n" +
                    "count(settle_accounts_status) 已完成\n" +
                    "from \n" +
                    "base_project \n" +
                    "where \n" +
                    "settle_accounts_status = \"5\"" +
                    "and\n" +
                    "building_project_id = #{id}" +
                    "and\n" +
                    "del_flag = '0'"
    )
    Integer settleAccountsStatusSensus2(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "actual_start_time\n" +
                    "FROM building_project\n" +
                    "where \n" +
                    "id = #{id}"
    )
    String buildingStartTime(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "actual_end_time\n" +
                    "FROM building_project\n" +
                    "where \n" +
                    "id = #{id}"
    )
    String buildingEndTime(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "year(bp.create_time) yeartime,\n" +
                    "month(bp.create_time) monthtime,\n" +
                    "SUM(design_category = 1) AS municipalPipeline,\n" +
                    "SUM(design_category = 2) AS networkReconstruction,\n" +
                    "SUM(design_category = 3) AS newCommunity,\n" +
                    "SUM(design_category = 4) AS secondaryWater,\n" +
                    "SUM(design_category = 5) AS commercialHouseholds,\n" +
                    "SUM(design_category = 6) AS waterResidents,\n" +
                    "SUM(design_category = 7) AS administration\n" +
                    "FROM\n" +
                    "base_project bp\n" +
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
                    "group by year(bp.create_time),\n" +
                    "month(bp.create_time)\n" +
                    "HAVING\n" +
                    "(yeartime = #{year} or #{year} = '')\n" +
                    "and\n" +
                    "(monthtime = #{month} or #{month} = '')"
    )
    List<OneCensus> censusList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.district,\n" +
                    "s2.blueprint_start_time\n" +
                    "FROM\n" +
                    "base_project s1,\n" +
                    "design_info s2\n" +
                    "where\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "s1.founder_id = #{id}\n" +
                    "and\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "blueprint_start_time>= #{startTime}\n" +
                    "and \n" +
                    "(blueprint_start_time<= #{endTime} or  #{endTime} = '') and\n" +
                    "s1.del_flag = '0'"
    )
    List<BaseProject> individualList(IndividualVo individualVo);

    @Select(
            "select \n" +
                    "count(budget_status)\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "budget_status != '4'\n" +
                    "and\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "del_flag = '0'" +
                    "and\n" +
                    "(district = #{district} or  #{district} = '')"
    )
    String budgetingCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select \n" +
                    "count(progress_payment_status)\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "progress_payment_status != '6'\n" +
                    "and\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "del_flag = '0'" +
                    "and\n" +
                    "(district = #{district} or  #{district} = '')"
    )
    String progressPaymentInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select \n" +
                    "count(visa_status)\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "visa_status != '6'\n" +
                    "and\n" +
                    "founder_id =  #{id}\n" +
                    "and\n" +
                    "del_flag = '0'" +
                    "and\n" +
                    "(district = #{district} or  #{district} = '')"
    )
    String visaApplyChangeInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select \n" +
                    "count(track_status)\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "track_status != '5'\n" +
                    "and\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "del_flag = '0'" +
                    "and\n" +
                    "(district = #{district} or  #{district} = '')"
    )
    String trackAuditInfoCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select \n" +
                    "count(settle_accounts_status)\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "settle_accounts_status != '5'\n" +
                    "and\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "del_flag = '0'" +
                    "and\n" +
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
                    "del_flag = '0'\n" +
                    "group by year(s1.create_time)\n" +
                    "HAVING\n" +
                    "yeartime = #{year}"
    )
    OneCensus2 costCensus(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "year(bp.create_time) yeartime,\n" +
                    "month(bp.create_time) monthTime ,\n" +
                    "count(budget_status) budget,\n" +
                    "count(track_status) track,\n" +
                    "count(visa_status) visa,\n" +
                    "count(progress_payment_status) progresspayment,\n" +
                    "count(settle_accounts_status) settleaccounts\n" +
                    "from\n" +
                    "base_project bp\n" +
                    " where\n" +
                    " founder_id = #{id}\n" +
                    " and\n" +
                    " (district = #{district} or #{district} = '')\n" +
                    " and\n" +
                    " create_time>= #{startTime}\n" +
                    " and \n" +
                    " (create_time<= #{endTime} or  #{endTime} = '') \n" +
                    " and\n" +
                    "del_flag = '0'\n" +
                    "group by year(bp.create_time),\n" +
                    "month(bp.create_time) \n" +
                    "HAVING\n" +
                    " (yeartime = #{year} or #{year} = '')\n" +
                    " and\n" +
                    " (monthTime = #{month} or #{month} = '')"
    )
    List<OneCensus2> costCensusList(CostVo2 costVo2);


    String projectCount(String id);


    @Select(
            "select\n" +
                    "count(desgin_status ) desginStatus,\n" +
                    "count(budget_status) budgetingCount,\n" +
                    "count(track_status) trackAuditInfoCount,\n" +
                    "count(visa_status) visaApplyChangeInformationCount,\n" +
                    "count(progress_payment_status) progressPaymentInformation,\n" +
                    "count(settle_accounts_status) settleAccountsCount\n" +
                    "from\n" +
                    "base_project\n"
    )
    CostVo3 AllprojectCount();

    @Select(
            "select\n" +
                    "sum(desgin_status = 1) desginStatus,\n" +
                    "sum(budget_status = 1) budgetingCount,\n" +
                    "sum(track_status = 1) trackAuditInfoCount,\n" +
                    "sum(visa_status = 1) visaApplyChangeInformationCount,\n" +
                    "sum(progress_payment_status = 1) progressPaymentInformation,\n" +
                    "sum(settle_accounts_status = 1) settleAccountsCount\n" +
                    "from\n" +
                    "base_project"
    )
    CostVo3 withAuditCount();

    @Select(
            "select\n" +
                    "sum(desgin_status = 4) desginStatus,\n" +
                    "sum(budget_status = 4) budgetingCount,\n" +
                    "sum(track_status = 5) trackAuditInfoCount,\n" +
                    "sum(visa_status = 6) visaApplyChangeInformationCount,\n" +
                    "sum(progress_payment_status = 6) progressPaymentInformation,\n" +
                    "sum(settle_accounts_status = 5) settleAccountsCount\n" +
                    "from\n" +
                    "base_project"
    )
    CostVo3 conductCount();

    @Select(
            "select\n" +
                    "sum(desgin_status != 4) desginStatus,\n" +
                    "sum(budget_status != 4) budgetingCount,\n" +
                    "sum(track_status != 5) trackAuditInfoCount,\n" +
                    "sum(visa_status != 6) visaApplyChangeInformationCount,\n" +
                    "sum(progress_payment_status != 6) progressPaymentInformation,\n" +
                    "sum(settle_accounts_status != 5) settleAccountsCount\n" +
                    "from\n" +
                    "base_project"
    )
    CostVo3 completeCount();


    @Select(
            "SELECT\n" +
                    "id,\n" +
                    "project_name,\n" +
                    "project_flow\n" +
                    "FROM \n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "create_time >= #{startTime}\n" +
                    "and\n" +
                    "(create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "del_flag = '0'\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like CONCAT('%',#{keyword},'%') or\n" +
                    "project_name like  CONCAT('%',#{keyword},'%')" +
                    ")"
    )
    List<BaseProject> projectFlow(CostVo2 costVo2);


    @Select(
            "\tselect\n" +
                    "\tyear(create_time) yearTime,\n" +
                    "\tMONTH(create_time) monthTime,\n" +
                    "\tcount(desgin_status ) desginStatus,\n" +
                    "\tcount(budget_status) budgetingCount,\n" +
                    "\tcount(track_status) trackAuditInfoCount,\n" +
                    "\tcount(visa_status) visaApplyChangeInformationCount,\n" +
                    "\tcount(progress_payment_status) progressPaymentInformation,\n" +
                    "\tcount(settle_accounts_status) settleAccountsCount\n" +
                    "\tfrom\n" +
                    "\tbase_project s1\n" +
                    "\twhere\n" +
                    "\t(district = #{district} or #{district} = '')\n" +
                    "\tand\n" +
                    "\tcreate_time >= #{startTime}\n" +
                    "\tand\n" +
                    "\t(create_time <= #{endTime} or #{endTime} = '')\n" +
                    "\tGROUP BY\n" +
                    "\tyear(s1.create_time),\n" +
                    "\tMONTH(s1.create_time)\n" +
                    "\thaving\n" +
                    "\t(yearTime = #{year} or #{year} = '')\n" +
                    "\tand\n" +
                    "\t(monthTime = #{month} or #{month}= '')"
    )
    List<CostVo3> prjectCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "YEAR(s2.create_time) yeartime,\n" +
                    "sum(design_category = 1) municipalPipeline,\n" +
                    "sum(design_category = 2) networkReconstruction,\n" +
                    "sum(design_category = 3) newCommunity,\n" +
                    "sum(design_category = 4) secondaryWater,\n" +
                    "sum(design_category = 5) commercialHouseholds,\n" +
                    "sum(design_category = 6) waterResidents,\n" +
                    "sum(design_category = 7) administration\n" +
                    "FROM \n" +
                    "income_info s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(s2.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s2.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "s2.del_flag = '0'\n" +
                    "GROUP BY\n" +
                    "YEAR(s2.create_time)\n" +
                    "HAVING\n" +
                    "(yeartime = #{year} or #{year} = '')"
    )
    OneCensus projectIncomeCensus(CostVo2 costVo2);
    @Select(
            "SELECT\n" +
                    "sum(design_category = 1) municipalPipeline,\n" +
                    "sum(design_category = 2) networkReconstruction,\n" +
                    "sum(design_category = 3) newCommunity,\n" +
                    "sum(design_category = 4) secondaryWater,\n" +
                    "sum(design_category = 5) commercialHouseholds,\n" +
                    "sum(design_category = 6) waterResidents,\n" +
                    "sum(design_category = 7) administration\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id\n" +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id\n" +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus projectExpenditureCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "YEAR(s1.create_time) yearTime,\n" +
                    "MONTH(s1.create_time) monthTime,\n" +
                    "sum(\n" +
                    "IFNULL(s2.outsource_money,0)+\n" +
                    "IFNULL(s3.amount_outsourcing,0)+\n" +
                    "IFNULL(s4.amount_outsourcing,0)+\n" +
                    "IFNULL(s5.outsourcing_amount,0)+\n" +
                    "IFNULL(s6.amount_outsourcing,0)+\n" +
                    "IFNULL(s7.amount_outsourcing,0)+\n" +
                    "IFNULL(s8.outsource_money,0)\n" +
                    ") outMoney,\n" +
                    "sum(\n" +
                    "IFNULL(s9.desgin_achievements,0)+\n" +
                    "IFNULL(s9.budget_achievements,0)+\n" +
                    "IFNULL(s9.upsubmit_achievements,0)+\n" +
                    "IFNULL(s9.downsubmit_achievements,0)+\n" +
                    "IFNULL(s9.truck_achievements,0)\n" +
                    ") advMoney\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id\n" +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id\n" +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "YEAR(s1.create_time),\n" +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus3> expenditureAnalysis(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_name,\n" +
                    "s1.district,\n" +
                    "s1.design_category,\n" +
                    "s6.review_number,\n" +
                    "s7.authorized_number,\n" +
                    "sum(\n" +
                    "IFNULL(s2.outsource_money,0)+\n" +
                    "IFNULL(s3.amount_outsourcing,0)+\n" +
                    "IFNULL(s4.amount_outsourcing,0)+\n" +
                    "IFNULL(s5.outsourcing_amount,0)+\n" +
                    "IFNULL(s6.amount_outsourcing,0)+\n" +
                    "IFNULL(s7.amount_outsourcing,0)+\n" +
                    "IFNULL(s8.outsource_money,0)\n" +
                    ") outMoney,\n" +
                    "sum(\n" +
                    "IFNULL(s9.desgin_achievements,0)+\n" +
                    "IFNULL(s9.budget_achievements,0)+\n" +
                    "IFNULL(s9.upsubmit_achievements,0)+\n" +
                    "IFNULL(s9.downsubmit_achievements,0)+\n" +
                    "IFNULL(s9.truck_achievements,0)\n" +
                    ") advMoney\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN budgeting s3 on s1.id = s3.base_project_id\n" +
                    "LEFT JOIN progress_payment_information s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN visa_change_information s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id\n" +
                    "LEFT JOIN track_audit_info s8 ON s1.id = s8.base_project_id\n" +
                    "LEFT JOIN achievements_info s9 ON s1.id = s9.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "GROUP BY\n" +
                    "s1.id\n"
    )
    List<BaseProject> BaseProjectExpenditureList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.cea_num,\n" +
                    "s1.project_name,\n" +
                    "s1.district,\n" +
                    "s2.amount_cost amountCost,\n" +
                    "s3.cost_total_amount costTotalAmount,\n" +
                    "s4.bidding_price_control biddingPriceControl,\n" +
                    "s5.total_payment_amount totalPaymentAmount,\n" +
                    "s6.review_number reviewNumber,\n" +
                    "s7.authorized_number authorizedNumber\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN cost_preparation s3 ON s1.id = s3.base_project_id\n" +
                    "LEFT JOIN very_establishment s4 ON s1.id = s4.base_project_id\n" +
                    "LEFT JOIN progress_payment_total_payment s5 ON s1.id = s5.base_project_id\n" +
                    "LEFT JOIN last_settlement_review s6 ON s1.id = s6.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s7 ON s1.id = s7.base_project_id\n" +
                    "where\n" +
                    "s1.del_flag = '0'\n" +
                    "and\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and\n" +
                    "(\n" +
                    "cea_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%')  \n" +
                    ")"
    )
    List<BaseProject> BaseProjectInfoCensus(CostVo2 costVo2);

    @Select(
            "select \n" +
                    "s2.id,\n" +
                    "s2.cea_num,\n" +
                    "s2.project_num,\n" +
                    "s2.project_name,\n" +
                    "s2.design_category,\n" +
                    "s2.district,\n" +
                    "s3.designer,\n" +
                    "s3.design_change_time designChangeTime\n" +
                    "from\n" +
                    "design_info s1,\n" +
                    "base_project s2,\n" +
                    "design_change_info s3\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "s1.id = s3.design_info_id\n" +
                    "and\n" +
                    "(district = #{district} or #{district} =  '')\n" +
                    "and\n" +
                    "s3.design_change_time >= #{startTime}\n" +
                    "and\n" +
                    "(s3.design_change_time <= #{endTime} or #{endTime} = '')\t\n" +
                    "and\n" +
                    "(\n" +
                    "s2.cea_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s2.project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "s2.project_name like  CONCAT('%',#{keyword},'%') or\n" +
                    "s3.designer like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "and\n" +
                    "s2.del_flag = '0'"
    )
    List<BaseProject> BaseProjectDesignList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
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
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    List<BaseProject> progressPaymentList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "s1.id,\n" +
                    "s1.project_num,\n" +
                    "s1.project_name,\n" +
                    "s1.design_category,\n" +
                    "s1.district,\n" +
                    "s2.amount_visa_change,\n" +
                    "s2.contract_amount,\n" +
                    "s2.compile_time\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN visa_change_information s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s2.compile_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.compile_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    List<BaseProject> projectVisaChangeList(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "COUNT(*)\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN visa_change_information s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s2.compile_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.compile_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    Double VisaChangeCount(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "SUM(s2.amount_visa_change)\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN visa_change_information s2 ON s1.id = s2.base_project_id\n" +
                    "WHERE\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s2.compile_time >= #{startTime}\n" +
                    "and\n" +
                    "(s2.compile_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(\n" +
                    "project_num like  CONCAT('%',#{keyword},'%')  or\n" +
                    "project_name  like  CONCAT('%',#{keyword},'%') \n" +
                    ")" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    Double VisaChangeMoney(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "YEAR(s1.create_time) yeartime,\n" +
                    "MONTH(s1.create_time) monthtime,\n" +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber,\n" +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney,\n" +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber,\n" +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id \n" +
                    "LEFT JOIN settlement_info s4 ON s1.id = s4.base_project_Id\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "s1.del_flag = '0'"+
                    "GROUP BY year(s1.create_time),MONTH(s1.create_time)"
    )
    List<OneCensus4>projectSettlementCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "YEAR(s1.create_time) yeartime,\n" +
                    "MONTH(s1.create_time) monthtime,\n" +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber,\n" +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney,\n" +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber,\n" +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber\n" +
                    "FROM\n" +
                    "base_project s1 LEFT JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id\n" +
                    "LEFT JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id \n" +
                    "LEFT JOIN settlement_info s4 ON s1.id = s4.base_project_Id\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and\n" +
                    "s1.del_flag = '0'"
    )
    OneCensus4 projectSettlementSum(CostVo2 costVo2);

    @Select(
            "SELECT \n" +
                    "sum(desgin_status = '1') revie,\n" +
                    "sum(desgin_status = '2') plot,\n" +
                    "sum(desgin_status = '4') comple,\n" +
                    "sum(desgin_status != '20') total\n" +
                    "FROM \n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or  #{district} = '')" +
                    "and\n" +
                    "del_flag = '0'"
    )
    OneCensus3 projectDesginCount(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "id,\n" +
                    "project_name projectName,\n" +
                    "(desgin_status != '2') plot,\n" +
                    "(desgin_status = '4') comple\n" +
                    "from\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "create_time >= #{startTime}\n" +
                    "and\n" +
                    "(create_time <= #{endTime} or #{endTime} = '')\n" +
                    "and\n" +
                    "(project_name like  CONCAT('%',#{keyword},'%'))" +
                    "and\n" +
                    "del_flag = '0'"
    )
    List<OneCensus3> projectDesginStatus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "year(bp.create_time) yeartime,\n" +
                    "month(bp.create_time) monthtime,\n" +
                    "SUM(design_category = 1) AS municipalPipeline,\n" +
                    "SUM(design_category = 2) AS networkReconstruction,\n" +
                    "SUM(design_category = 3) AS newCommunity,\n" +
                    "SUM(design_category = 4) AS secondaryWater,\n" +
                    "SUM(design_category = 5) AS commercialHouseholds,\n" +
                    "SUM(design_category = 6) AS waterResidents,\n" +
                    "SUM(design_category = 7) AS administration\n" +
                    "FROM\n" +
                    "base_project bp\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "create_time>= #{startTime}\n" +
                    "and \n" +
                    "(create_time<= #{endTime} or  #{endTime} = '') \n" +
                    "and\n" +
                    "del_flag = '0'\n" +
                    "group by year(bp.create_time),\n" +
                    "month(bp.create_time)\n" +
                    "HAVING\n" +
                    "(yeartime = #{year} or #{year} = '')\n" +
                    "and\n" +
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
                    "COUNT(budget_status) budgetStatus,\n" +
                    "COUNT(progress_payment_status) progressPaymentStatus,\n" +
                    "COUNT(visa_status) visaStatus,\n" +
                    "COUNT(settle_accounts_status) settleAccountsStatus\t\n" +
                    "FROM\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskTotal(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "sum(budget_status='1') budgetStatus,\n" +
                    "sum(progress_payment_status='1') progressPaymentStatus,\n" +
                    "sum(visa_status='1') visaStatus,\n" +
                    "sum(settle_accounts_status='1') settleAccountsStatus\n" +
                    "FROM\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskReviewed(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "sum(budget_status='2') budgetStatus,\n" +
                    "sum(progress_payment_status='2') progressPaymentStatus,\n" +
                    "sum(visa_status='2') visaStatus,\n" +
                    "sum(settle_accounts_status='2') settleAccountsStatus\n" +
                    "FROM\n" +
                    "base_project\n" +
                    "where\n" +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskHandle(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "sum(budget_status='4') budgetStatus,\n" +
                    "sum(progress_payment_status='6') progressPaymentStatus,\n" +
                    "sum(visa_status='6') visaStatus,\n" +
                    "sum(settle_accounts_status='5') settleAccountsStatus\n" +
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
}

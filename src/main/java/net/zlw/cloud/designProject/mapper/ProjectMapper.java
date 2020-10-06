package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.*;
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
                    "project_name like  CONCAT('%',#{keyword},'%')  or\n" +
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
                    "\t(district = '' or '' = '')\n" +
                    "\tand\n" +
                    "\tcreate_time >= ''\n" +
                    "\tand\n" +
                    "\t(create_time <= '' or '' = '')\n" +
                    "\tGROUP BY\n" +
                    "\tyear(s1.create_time),\n" +
                    "\tMONTH(s1.create_time)\n" +
                    "\thaving\n" +
                    "\t(yearTime = #{year} or #{year} = '')\n" +
                    "\tand\n" +
                    "\t(monthTime = #{month} or #{month}= '')"
    )
    List<CostVo3> prjectCensus(CostVo2 costVo2);
}

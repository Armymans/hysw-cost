package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.*;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

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
                    "year(s2.create_time) yeartime, " +
                    "month(s2.create_time) monthtime, " +
                    "SUM(design_category = 1) AS municipalPipeline, " +
                    "SUM(design_category = 2) AS networkReconstruction, " +
                    "SUM(design_category = 3) AS newCommunity, " +
                    "SUM(design_category = 4) AS secondaryWater, " +
                    "SUM(design_category = 5) AS commercialHouseholds, " +
                    "SUM(design_category = 6) AS waterResidents, " +
                    "SUM(design_category = 7) AS administration " +
                    "FROM " +
                    "base_project s1, " +
                    "design_info s2 " +
                    "where " +
                    "s1.id = s2.base_project_id " +
                    "and " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "s2.`status` = '0' " +
                    "and " +
                    "(s2.founder_id = #{id} or #{id} = '') " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "s2.create_time>= #{startTime} " +
                    "and  " +
                    "(s2.create_time<= #{endTime} or  #{endTime} = '')  " +
                    "group by  " +
                    "year(s2.create_time), " +
                    "month(s2.create_time)"
    )
    List<OneCensus> censusList(CostVo2 costVo2);

    @Select(
            "SELECT   " +
                    "s1.id,   " +
                    "s1.project_num,   " +
                    "s1.project_name,   " +
                    "s1.district,   " +
                    "s2.blueprint_start_time   " +
                    "FROM   " +
                    "base_project s1,   " +
                    "design_info s2   " +
                    "where   " +
                    "s1.id = s2.base_project_id   " +
                    "and   " +
                    "s1.del_flag = '0'  " +
                    "and   " +
                    "(s1.founder_id = #{id} or #{id} = '')  " +
                    "and   " +
                    "(district = #{district} or #{district} = '')   " +
                    "and   " +
                    "blueprint_start_time>= #{startTime}   " +
                    "and    " +
                    "(blueprint_start_time<= #{endTime} or  #{endTime} = '') "
    )
    List<BaseProject> individualList(IndividualVo individualVo);

    @Select(
            "select  " +
                    "IFNULL(count(*) ,0)  " +
                    "from   " +
                    "budgeting s1 LEFT JOIN audit_info s2 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN base_project s3 ON s1.base_project_id = s3.id  " +
                    "where   " +
                    "s2.audit_result = '0'   " +
                    "and   " +
                    "s1.del_flag = '0'  " +
                    "and   " +
                    "(s2.auditor_id = #{id} or #{id} = '')  " +
                    "and   " +
                    "(district = #{district} or  #{district} = '')"
    )
    String budgetingCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select   " +
                    "IFNULL(count(*),0)  " +
                    "from   " +
                    "progress_payment_information s1 LEFT JOIN base_project s2 ON s1.base_project_id = s2.id  " +
                    "LEFT JOIN audit_info s3 ON s1.id = s3.base_project_id  " +
                    "where  " +
                    "s1.del_flag = '0'  " +
                    "and  " +
                    "s3.audit_result = '0'  " +
                    "and  " +
                    "(s3.auditor_id = '' or '' = '')  " +
                    "and  " +
                    "(district = #{district} or  #{district} = '')"
    )
    String progressPaymentInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    " IFNULL(COUNT(DISTINCT b.id),0)  " +
                    " from   " +
                    " base_project b   " +
                    " LEFT JOIN visa_change_information v on b.id = v.base_project_id   " +
                    " LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id  " +
                    " LEFT JOIN visa_change_information v2 on b.id = v2.base_project_id  " +
                    " LEFT JOIN audit_info a on a.base_project_id = IFNULL(v2.id,v.id)   " +
                    " where   " +
                    " b.del_flag = '0'  " +
                    " and  " +
                    " v.state = '0'  " +
                    " and  " +
                    " vv.state = '0'  " +
                    " and  " +
                    " v2.state = '0'  " +
                    " and  " +
                    " v.up_and_down_mark = '0'   " +
                    " and  " +
                    " v2.up_and_down_mark = '1'   " +
                    " and  " +
                    " a.audit_result = '0'   " +
                    " and   " +
                    " (a.auditor_id = #{id} or #{id} = '')   " +
                    " and  " +
                    " (b.district = #{district} or #{district} = '') "
    )
    String visaApplyChangeInformationCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "count(track_status)  " +
                    "from  " +
                    "base_project s1 LEFT JOIN track_audit_info s2 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN audit_info s3 ON s1.id = s3.base_project_id  " +
                    "where  " +
                    "s1.del_flag = '0'  " +
                    "and  " +
                    "s2.`status` = '0'  " +
                    "and  " +
                    "s3.audit_result = '0'  " +
                    "and  " +
                    "(s3.auditor_id = #{id} or #{id} = '')  " +
                    "and  " +
                    "(district = #{district} or #{district} = '')"
    )
    String trackAuditInfoCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select  " +
                    "COUNT(*)  " +
                    "from  " +
                    "budgeting bt   " +
                    "LEFT JOIN base_project b on bt.base_project_id = b.id   " +
                    "LEFT JOIN last_settlement_review l on l.base_project_id = bt.base_project_id  " +
                    "LEFT JOIN settlement_audit_information s on s.base_project_id = bt.base_project_id  " +
                    "LEFT JOIN settlement_info si on si.base_project_id = bt.base_project_id  " +
                    "LEFT JOIN audit_info a on a.base_project_id = IFNULL(s.id,l.id)  " +
                    "where   " +
                    " si.up_and_down = '2'   " +
                    "and   " +
                    "bt.del_flag = '0'   " +
                    "and   " +
                    "b.del_flag = '0'   " +
                    "and   " +
                    "si.state = '0'   " +
                    "and  " +
                    "IFNULL(l.del_flag,'0') = '0'   " +
                    "and  " +
                    "IFNULL(s.del_flag,'0') = '0'   " +
                    "and  " +
                    "a.audit_result = '0'   " +
                    "and  " +
                    "(a.auditor_id = #{id} or a.auditor_id = #{id})  " +
                    " and  " +
                    "(b.district = #{district} or #{district} = '' ) "
    )
    String settleAccountsCount(@Param("id") String id,@Param("district") String district);

    @Select(
            "select " +
                    "year(s1.create_time) yeartime, " +
                    "count(budget_status) budget, " +
                    "count(track_status) track, " +
                    "count(visa_status) visa, " +
                    "count(progress_payment_status) progresspayment, " +
                    "count(settle_accounts_status) settleaccounts " +
                    "from " +
                    "base_project s1 " +
                    "where " +
                    "del_flag = '0' " +
                    "and " +
                    "create_time>= #{startTime} " +
                    "and  " +
                    "(create_time<= #{endTime} or  #{endTime} = '')  " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "group by  " +
                    "year(s1.create_time)"
    )
    OneCensus2 costCensus(CostVo2 costVo2);

    @Select(
            "select  " +
                    "year(bp.create_time) yeartime,  " +
                    "month(bp.create_time) monthTime ,  " +
                    "count(budget_status) budget,  " +
                    "count(track_status) track,  " +
                    "count(visa_status) visa,  " +
                    "count(progress_payment_status) progresspayment,  " +
                    "count(settle_accounts_status) settleaccounts  " +
                    "from  " +
                    "base_project bp  " +
                    "where  " +
                    "del_flag = '0'  " +
                    "and  " +
                    "create_time>= #{startTime} " +
                    "and   " +
                    "(create_time<= #{endTime} or  #{endTime} = '')   " +
                    "and  " +
                    "(district = #{district} or #{district} = '')  " +
                    "group by  " +
                    "year(bp.create_time),  " +
                    "month(bp.create_time)  "
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
                    "where  " +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 AllprojectCount(CostVo2 costVo2);

    @Select(
            "select  " +
                    "ifnull(sum(desgin_status = 1),0) desginStatus,  " +
                    "ifnull(sum(budget_status = 1),0) budgetingCount,  " +
                    "ifnull(sum(track_status = 1),0) trackAuditInfoCount,  " +
                    "ifnull(sum(visa_status = 1),0) visaApplyChangeInformationCount,  " +
                    "ifnull(sum(progress_payment_status = 1),0) progressPaymentInformation,  " +
                    "ifnull(sum(settle_accounts_status = 1),0) settleAccountsCount  " +
                    "from  " +
                    "base_project   " +
                    "where  " +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 withAuditCount(CostVo2 costVo2);

    @Select(
            "select  " +
                    "ifnull(sum(desgin_status = 4),0) desginStatus,  " +
                    "ifnull(sum(budget_status = 4),0) budgetingCount,  " +
                    "ifnull(sum(track_status = 5),0) trackAuditInfoCount,  " +
                    "ifnull(sum(visa_status = 6),0) visaApplyChangeInformationCount,  " +
                    "ifnull(sum(progress_payment_status = 6),0) progressPaymentInformation,  " +
                    "ifnull(sum(settle_accounts_status = 5),0) settleAccountsCount  " +
                    "from  " +
                    "base_project  " +
                    "where  " +
                    "(district = #{district} or #{district} = '')"
    )
    CostVo3 conductCount(CostVo2 costVo2);

    @Select(
            "select  " +
                    "ifnull(sum(desgin_status != 4),0) desginStatus,  " +
                    "ifnull(sum(budget_status != 4),0) budgetingCount,  " +
                    "ifnull(sum(track_status != 5),0) trackAuditInfoCount,  " +
                    "ifnull(sum(visa_status != 6),0) visaApplyChangeInformationCount,  " +
                    "ifnull(sum(progress_payment_status != 6),0) progressPaymentInformation,  " +
                    "ifnull(sum(settle_accounts_status != 5),0) settleAccountsCount  " +
                    "from  " +
                    "base_project  " +
                    "where  " +
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
                    "IFNULL(YEAR(s2.create_time),'-') yeartime, " +
                    "IFNULL(SUM(CASE WHEN design_category = '1' THEN in_money END),0) municipalPipeline, " +
                    "IFNULL(SUM(CASE WHEN design_category = '2' THEN in_money END),0) networkReconstruction, " +
                    "IFNULL(SUM(CASE WHEN design_category = '3' THEN in_money END),0) newCommunity, " +
                    "IFNULL(SUM(CASE WHEN design_category = '4' THEN in_money END),0) secondaryWater, " +
                    "IFNULL(SUM(CASE WHEN design_category = '5' THEN in_money END),0) commercialHouseholds, " +
                    "IFNULL(SUM(CASE WHEN design_category = '6' THEN in_money END),0) waterResidents, " +
                    "IFNULL(SUM(CASE WHEN design_category = '7' THEN in_money END),0) administration " +
                    "FROM  " +
                    "in_come s1, " +
                    "base_project s2 " +
                    "WHERE " +
                    "s1.base_project_id = s2.id " +
                    "AND " +
                    "s2.del_flag = '0' " +
                    "AND " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s2.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus projectIncomeCensus(CostVo2 costVo2);
    @Select(
            "SELECT " +
                    "IFNULL(YEAR(s2.create_time),'-') yeartime, " +
                    "IFNULL(SUM(CASE WHEN design_category = '2' THEN out_money END),0) municipalPipeline, " +
                    "IFNULL(SUM(CASE WHEN design_category = '2' THEN out_money END),0) networkReconstruction, " +
                    "IFNULL(SUM(CASE WHEN design_category = '3' THEN out_money END),0) newCommunity, " +
                    "IFNULL(SUM(CASE WHEN design_category = '4' THEN out_money END),0) secondaryWater, " +
                    "IFNULL(SUM(CASE WHEN design_category = '5' THEN out_money END),0) commercialHouseholds, " +
                    "IFNULL(SUM(CASE WHEN design_category = '6' THEN out_money END),0) waterResidents, " +
                    "IFNULL(SUM(CASE WHEN design_category = '7' THEN out_money END),0) administration " +
                    "FROM  " +
                    "out_source s1, " +
                    "base_project s2 " +
                    "WHERE " +
                    "s1.base_project_id = s2.id " +
                    "AND " +
                    "s2.del_flag = '0' " +
                    "AND " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s2.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus projectExpenditureCensus(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "IFNULL(YEAR(s2.create_time),'-') yeartime, " +
                    "IFNULL(SUM(CASE WHEN design_category = '2' THEN accrued_amount END),0) municipalPipeline, " +
                    "IFNULL(SUM(CASE WHEN design_category = '2' THEN accrued_amount END),0) networkReconstruction, " +
                    "IFNULL(SUM(CASE WHEN design_category = '3' THEN accrued_amount END),0) newCommunity, " +
                    "IFNULL(SUM(CASE WHEN design_category = '4' THEN accrued_amount END),0) secondaryWater, " +
                    "IFNULL(SUM(CASE WHEN design_category = '5' THEN accrued_amount END),0) commercialHouseholds, " +
                    "IFNULL(SUM(CASE WHEN design_category = '6' THEN accrued_amount END),0) waterResidents, " +
                    "IFNULL(SUM(CASE WHEN design_category = '7' THEN accrued_amount END),0) administration " +
                    "FROM  " +
                    "employee_achievements_info s1, " +
                    "base_project s2 " +
                    "WHERE " +
                    "s1.base_project_id = s2.id " +
                    "AND " +
                    "s2.del_flag = '0' " +
                    "AND " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s2.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus projectExpenditureCensus2(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(s2.create_time) yearTime, " +
                    "MONTH(s2.create_time) monthTime, " +
                    "SUM(s2.out_money) outMoney, " +
                    "SUM(s3.accrued_amount) advMoney " +
                    "FROM " +
                    "base_project s1 LEFT JOIN out_source s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN employee_achievements_info s3 ON s1.id = s3.base_project_id " +
                    "WHERE " +
                    "s1.del_flag = '0' " +
                    "AND " +
                    "s2.del_flag = '0' " +
                    "AND " +
                    "s3.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    "GROUP BY " +
                    "YEAR(s2.create_time), " +
                    "MONTH(s2.create_time)"
    )
    List<OneCensus3> expenditureAnalysis(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_name, " +
                    "( CASE s1.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,  " +
                    "(  " +
                    "CASE  " +
                    "s1.design_category   " +
                    "WHEN '1' THEN  " +
                    "'市政管道'   " +
                    "WHEN '2' THEN  " +
                    "'管网改造'   " +
                    "WHEN '3' THEN  " +
                    "'新建小区'   " +
                    "WHEN '4' THEN  " +
                    "'二次供水项目'   " +
                    "WHEN '5' THEN  " +
                    "'工商户'   " +
                    "WHEN '6' THEN  " +
                    "'居民装接水'   " +
                    "WHEN '7' THEN  " +
                    "'行政事业'   " +
                    "END   " +
                    ") AS designCategory,  " +
                    "sum(IFNULL(s2.in_money,0)) inCome, " +
                    "sum(IFNULL(s3.actual_amount,0)) advMoney, " +
                    "sum(IFNULL(s4.out_money,0)) outMoney " +
                    "from " +
                    "base_project s1  " +
                    "LEFT JOIN in_come s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN employee_achievements_info s3 ON s1.id = s3.base_project_id " +
                    "LEFT JOIN out_source s4 ON s1.id = s4.base_project_id " +
                    "where " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    " and " +
                    "( " +
                    "s1.cea_num like concat('%',#{keyword},'%') or  " +
                    "s1.project_name like concat('%',#{keyword},'%') " +
                    ") " +
                    "GROUP BY " +
                    "s1.id "
    )
    List<BaseProject> BaseProjectExpenditureList(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_name, " +
                    "( CASE s1.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,  " +
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
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.design_category, " +
                    "s1.district, " +
                    "SUM(IFNULL(current_payment_Information,0)) totalPaymentAmount, " +
                    "SUM(IFNULL(current_payment_ratio,0)) accumulativePaymentProportion " +
                    "FROM " +
                    "base_project s1 LEFT JOIN progress_payment_information s2 ON s1.id = s2.base_project_id " +
                    "WHERE " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "s2.del_flag != '1' " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "( " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or " +
                    "project_name  like  CONCAT('%',#{keyword},'%')  " +
                    ") " +
                    "GROUP BY " +
                    "s1.id"
    )
    List<BaseProject> progressPaymentList(CostVo2 costVo2);

    @Select(
            "SELECT   " +
                    "COUNT(*)  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN   " +
                    "progress_payment_information s2 ON s1.id = s2.base_project_id  " +
                    "WHERE  " +
                    "(district = #{district} or #{district} = '')  " +
                    "and  " +
                    "s1.create_time >= #{startTime}  " +
                    "and  " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and  " +
                    "(  " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or  " +
                    "project_name  like  CONCAT('%',#{keyword},'%')   " +
                    ")" +
                    "AND  " +
                    "s1.del_flag = '0'"
    )
    Integer progressPaymentCount(CostVo2 costVo2);

    @Select(
            "SELECT   " +
                    "SUM(IFNULL(current_payment_Information,0))  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN   " +
                    "progress_payment_information s2 ON s1.id = s2.base_project_id  " +
                    "WHERE  " +
                    "(district = #{district} or #{district} = '')  " +
                    "and  " +
                    "s1.create_time >= #{startTime}  " +
                    "and  " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')  " +
                    "and  " +
                    "(  " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or  " +
                    "project_name  like  CONCAT('%',#{keyword},'%')   " +
                    ")" +
                    "AND  " +
                    "s1.del_flag = '0'"
    )
    Double progressPaymentSum(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.cea_num, " +
                    "s1.project_num, " +
                    "s1.project_name, " +
                    "s1.design_category, " +
                    "s1.district, " +
                    "s2.amount_visa_change, " +
                    "s2.contract_amount, " +
                    "s2.compile_time " +
                    "FROM " +
                    "base_project s1, " +
                    "visa_change_information s2 " +
                    "where " +
                    "s1.id = s2.base_project_id " +
                    "and " +
                    "s1.del_flag = '0' " +
                    "and " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "( " +
                    "project_num like  CONCAT('%',#{keyword},'%')  or " +
                    "project_name  like  CONCAT('%',#{keyword},'%')  " +
                    ") " +
                    "GROUP BY " +
                    "s1.id " +
                    "HAVING " +
                    "s2.compile_time >= #{startTime} " +
                    "and " +
                    "(s2.compile_time  <= #{endTime} or #{endTime} = '') " +
                    "ORDER BY " +
                    "s2.compile_time desc"
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
                    "base_project s1 inner JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id " +
                    "inner JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id  " +
                    "inner JOIN settlement_info s4 ON s1.id = s4.base_project_Id " +
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
                    "YEAR(s1.create_time) yeartime,\n" +
                    "MONTH(s1.create_time) monthtime,\n" +
                    "SUM(IFNULL(s2.review_number,0)) reviewNumber,\n" +
                    "SUM(IFNULL(s4.sumbit_money,0)) sumbitMoney,\n" +
                    "SUM(IFNULL(s3.authorized_number,0)) authorizedNumber,\n" +
                    "SUM(IFNULL(s3.subtract_the_number,0)) subtractTheNumber\n" +
                    "FROM\n" +
                    "base_project s1 inner JOIN last_settlement_review  s2 ON s1.id = s2.base_project_id\n" +
                    "inner JOIN settlement_audit_information s3 ON s1.id = s3.base_project_id \n" +
                    "inner JOIN settlement_info s4 ON s1.id = s4.base_project_Id\n" +
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
                    "IFNULL(sum(desgin_status = '1' or desgin_status = '3') ,0) revie,  " +
                    "IFNULL(sum(desgin_status = '2'),0) plot,  " +
                    "IFNULL(sum(desgin_status = '4'),0) comple,  " +
                    "IFNULL(sum(desgin_status != '20'),0) total  " +
                    "FROM  " +
                    "base_project  " +
                    "where  " +
                    "(district = #{district} or  #{district} = '')  " +
                    "and  " +
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
            "SELECT\n" +
                    "IFNULL(COUNT(*),0)\n" +
                    "FROM\n" +
                    "design_info s1,\n" +
                    "base_project s2\n" +
                    "WHERE\n" +
                    "s1.base_project_id = s2.id\n" +
                    "AND\n" +
                    "s1.`status` = '0'\n" +
                    "AND\n" +
                    "s2.del_flag = '0'\n" +
                    "AND\n" +
                    "s1.isaccount is NULL\n" +
                    "AND\n" +
                    "s2.create_time >= #{startTime}\n" +
                    "AND\n" +
                    "(s2.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "AND\n" +
                    "(s2.district = #{district} or #{district} = '')"
    )
    Integer desiginMoneyCensus(CostVo2 costVo2);

    @Select(
            "SELECT\n" +
                    "IFNULL(COUNT(*),0)\n" +
                    "FROM\n" +
                    "design_info s1,\n" +
                    "base_project s2\n" +
                    "WHERE\n" +
                    "s1.base_project_id = s2.id\n" +
                    "AND\n" +
                    "s1.`status` = '0'\n" +
                    "AND\n" +
                    "s2.del_flag = '0'\n" +
                    "AND\n" +
                    "s1.isaccount = '1'\n" +
                    "AND\n" +
                    "s2.create_time >= #{startTime}\n" +
                    "AND\n" +
                    "(s2.create_time <= #{endTime} or #{endTime} = '')\n" +
                    "AND\n" +
                    "(s2.district = #{district} or #{district} = '')"
    )
    Integer desiginMoneyCensus2(CostVo2 costVo2);

    @Select(
            "SELECT  " +
                    "sum(s2.outsource = 0) outsourceYes,  " +
                    "sum(s2.outsource = 1) outsourceNo  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id  " +
                    "where  " +
                    "(s1.district = #{district} or #{district} = '')  " +
                    "and  " +
                    "s1.create_time >= #{startTime}  " +
                    "and  " +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')" +
                    "and  " +
                    "s1.del_flag = '0'"
    )
    OneCensus5 desiginoutsource(CostVo2 costVo2);

    @Select(
                "SELECT  " +
                        "YEAR(create_time) yearTime,  " +
                        "MONTH(create_time) monthTime,  " +
                        "IFNULL(SUM(actual_amount),0) desginAchievements,  " +
                        "IFNULL(SUM(accrued_amount),0) desginAchievements2  " +
                        "FROM  " +
                        "employee_achievements_info  " +
                        "WHERE  " +
                        "del_flag = '0'  " +
                        "and  " +
                        "achievements_type = '1'  " +
                        "and  " +
                        "(district = #{district} or #{district} = '')  " +
                        "and  " +
                        "create_time >= #{startTime}  " +
                        "and  " +
                        "(create_time <= #{endTime} or #{endTime} = '')  " +
                        "GROUP BY  " +
                        "YEAR(create_time),  " +
                        "MONTH(create_time)"
    )
    List<OneCensus6> desiginAchievementsCensus(CostVo2 costVo2);

    @Select(
            "SELECT  " +
                    "YEAR(create_time) yearTime,  " +
                    "MONTH(create_time) monthTime,  " +
                    "SUM(IFNULL(actual_amount,0)) desginAchievements  " +
                    "FROM  " +
                    "employee_achievements_info  " +
                    "WHERE  " +
                    "del_flag = '0'  " +
                    "and  " +
                    "achievements_type = '1'  " +
                    "and  " +
                    "member_id = #{id}  " +
                    "and  " +
                    "(district = #{district} or #{district} = '')  " +
                    "and  " +
                    "create_time >= #{startTime}  " +
                    "and  " +
                    "(create_time <= #{endTime} or #{endTime} = '')  " +
                    "GROUP BY  " +
                    "YEAR(create_time),  " +
                    "MONTH(create_time)"
    )
    List<OneCensus6> desiginAchievementsOneCensus(CostVo2 costVo2);

    @Select(
            "SELECT   " +
                    "YEAR(create_time) yearTime,   " +
                    "SUM(IFNULL(actual_amount,0)) desginAchievements   " +
                    "FROM   " +
                    "employee_achievements_info   " +
                    "WHERE   " +
                    "del_flag = '0'   " +
                    "and   " +
                    "achievements_type = '1'   " +
                    "and   " +
                    "member_id = #{id}   " +
                    "and   " +
                    "(district = #{district} or #{district} = '')   " +
                    "and   " +
                    "create_time >= #{startTime}   " +
                    "and   " +
                    "(create_time <= #{endTime} or #{endTime} = '')   " +
                    "GROUP BY   " +
                    "YEAR(create_time)"
    )
    List<OneCensus6> desiginAchievementsOneCensus2(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "ifnull( COUNT( budget_status ), 0 ) budgetStatus, " +
                    "ifnull( COUNT( track_status ), 0 ) trackStatus, " +
                    "ifnull( COUNT( progress_payment_status ), 0 ) progressPaymentStatus, " +
                    "ifnull( COUNT( visa_status ), 0 ) visaStatus, " +
                    "ifnull( COUNT( settle_accounts_status ), 0 ) settleAccountsStatus  " +
                    "FROM " +
                    "base_project  " +
                    "WHERE " +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskTotal(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "ifnull(sum(budget_status!='4'),0) budgetStatus, " +
                    "ifnull(sum(track_status!='5'),0) trackStatus, " +
                    "ifnull(sum(progress_payment_status!='6'),0) progressPaymentStatus, " +
                    "ifnull(sum(visa_status!='6'),0) visaStatus, " +
                    "ifnull(sum(settle_accounts_status!='5'),0) settleAccountsStatus " +
                    "FROM " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskReviewed(CostVo2 costVo2);

    @Select(
            "SELECT  " +
                    "ifnull(sum(budget_status='2'),0) budgetStatus,  " +
                    "ifnull(sum(progress_payment_status='2'),0) progressPaymentStatus,  " +
                    "ifnull(sum(visa_status='2'),0) visaStatus,  " +
                    "ifnull(sum(settle_accounts_status='2'),0) settleAccountsStatus  " +
                    "FROM  " +
                    "base_project"
    )
    OneCensus6 costTaskHandle(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "ifnull(sum(budget_status='4'),0) budgetStatus, " +
                    "ifnull(sum(track_status='5'),0) trackStatus, " +
                    "ifnull(sum(progress_payment_status='6'),0) progressPaymentStatus, " +
                    "ifnull(sum(visa_status='6'),0) visaStatus, " +
                    "ifnull(sum(settle_accounts_status='5'),0) settleAccountsStatus " +
                    "FROM " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or #{district} = '')"
    )
    OneCensus6 costTaskComple(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "YEAR(create_time) yearTime, " +
                    "MONTH(create_time) monthTime, " +
                    "count(budget_status) budgetStatus, " +
                    "count(progress_payment_status) progressPaymentStatus, " +
                    "count(visa_status) visaStatus , " +
                    "count(settle_accounts_status) settleAccountsStatus,  " +
                    "count(track_status) trackStatus " +
                    "FROM " +
                    "base_project " +
                    "where " +
                    "(district = #{district} or #{district} = '') " +
                    "and " +
                    "create_time >= #{startTime} " +
                    "and " +
                    "(create_time <= #{endTime} or #{endTime} = '') " +
                    "and " +
                    "del_flag = '0' " +
                    "GROUP BY " +
                    "YEAR(create_time), " +
                    "MONTH(create_time)"
    )
    List<OneCensus6> costTaskCensus(CostVo2 costVo2);

    @Select(
            "select\n" +
                    "count(DISTINCT s2.id) budgeting,\n" +
                    "count(DISTINCT s3.id) lastSettlementReview,\n" +
                    "count(DISTINCT s4.id) settlementAuditInformation,\n" +
                    "count(DISTINCT s5.id) trackAuditInfo,\n" +
                    "count(DISTINCT s6.id) visaChangeInformation,\n" +
                    "count(DISTINCT s7.id) progressPaymentInformation\n" +
                    "from\n" +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id AND s2.del_flag = '0'\n" +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id  AND s3.del_flag = '0'\n" +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id  AND s4.del_flag = '0'\n" +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id  AND s5.`status` = '0'\n" +
                    "LEFT JOIN visa_change_information s6 ON s1.id = s6.base_project_id AND s6.state = '0'\n" +
                    "LEFT JOIN progress_payment_information s7 ON s1.id = s7.base_project_id AND s7.del_flag = '0'\n" +
                    "where\n" +
                    "(s1.district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "s1.create_time >= #{startTime}\n" +
                    "and\n" +
                    "(s1.create_time <= #{endTime} or #{endTime} = '')"
    )
    OneCensus7 costTaskSummary(CostVo2 costVo2);

    @Select(
            "select\n" +
                    " s1.num budgeting,\n" +
                    " s2.num lastSettlementReview,\n" +
                    " s3.num settlementAuditInformation,\n" +
                    " s4.num trackAuditInfo,\n" +
                    " s5.num visaChangeInformation,\n" +
                    " s6.num progressPaymentInformation\n" +
                    "from \n" +
                    "(select count(*) num from base_project s1 , budgeting s2 where s1.id = s2.base_project_id and s2.outsourcing = '1' and s1.del_flag = '0' and s2.del_flag = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s1,\n" +
                    "(select count(*) num from base_project s1 , last_settlement_review s2 where s1.id = s2.base_project_id and s2.outsourcing = '1' and s1.del_flag = '0' and s2.del_flag = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s2,\n" +
                    "(select count(*) num from base_project s1 , settlement_audit_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '1' and s1.del_flag = '0' and s2.del_flag = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s3,\n" +
                    "(select count(*) num from base_project s1 , track_audit_info s2 where s1.id = s2.base_project_id and s2.outsource = '1' and s1.del_flag = '0' and s2.status = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s4,\n" +
                    "(select count(*) num from base_project s1 , visa_change_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '1' and s1.del_flag = '0' and s2.state = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s5,\n" +
                    "(select count(*) num from base_project s1 , progress_payment_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '1' and s1.del_flag = '0' and s2.del_flag = '0' AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s6"
    )
    OneCensus7 costTaskOutsourcingCount(CostVo2 costVo2);

    @Select(
            "select\n" +
                    " s1.num budgeting,\n" +
                    " s2.num lastSettlementReview,\n" +
                    " s3.num settlementAuditInformation,\n" +
                    " s4.num trackAuditInfo,\n" +
                    " s5.num visaChangeInformation,\n" +
                    " s6.num progressPaymentInformation\n" +
                    "from \n" +
                    "(select count(*) num from base_project s1 , budgeting s2 where s1.id = s2.base_project_id and s2.outsourcing = '2' and s1.del_flag = '0' and s2.del_flag = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s1,\n" +
                    "(select count(*) num from base_project s1 , last_settlement_review s2 where s1.id = s2.base_project_id and s2.outsourcing = '2' and s1.del_flag = '0' and s2.del_flag = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s2,\n" +
                    "(select count(*) num from base_project s1 , settlement_audit_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '2' and s1.del_flag = '0' and s2.del_flag = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s3,\n" +
                    "(select count(*) num from base_project s1 , track_audit_info s2 where s1.id = s2.base_project_id and s2.outsource = '2' and s1.del_flag = '0' and s2.status = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s4,\n" +
                    "(select count(*) num from base_project s1 , visa_change_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '2' and s1.del_flag = '0' and s2.state = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s5,\n" +
                    "(select count(*) num from base_project s1 , progress_payment_information s2 where s1.id = s2.base_project_id and s2.outsourcing = '2' and s1.del_flag = '0' and s2.del_flag = '0'  AND (s1.district = #{district} or #{district} = '') AND s1.create_time >= #{startTime} AND (s1.create_time <= #{endTime} or #{endTime} = '')) s6"
    )
    OneCensus7 costTaskNoOutsourcingCount(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "s2.member_name memberName, " +
                    "SUM(IFNULL(s1.accrued_amount,0)) desginAchievements, " +
                    "SUM(IFNULL(s1.actual_amount,0)) desginAchievements2, " +
                    "SUM(IFNULL(s1.balance,0)) balance " +
                    "FROM " +
                    "employee_achievements_info s1, " +
                    "member_manage s2 " +
                    "WHERE " +
                    "s1.member_id = s2.id " +
                    "and " +
                    "s1.dept = '1' " +
                    "GROUP BY " +
                    "member_name"
    )
    List<OneCensus8> DesginAchievementsList(CostVo2 costVo2);
    @Select(
            "SELECT " +
                    "s1.id, " +
                    "s1.project_name projectName, " +
                    "s1.district, " +
                    "s1.a_b aB, " +
                    "s2.take_time takeTime, " +
                    "s2.blueprint_start_time blueprintStartTime, " +
                    "s3.amount_cost amountCost, " +
                    "s4.accrued_amount desginAchievements, " +
                    "s4.actual_amount desginAchievements2 " +
                    "FROM " +
                    "base_project s1 LEFT JOIN design_info s2 ON s1.id = s2.base_project_id " +
                    "LEFT JOIN budgeting s3 ON s1.id = s3.base_project_id  " +
                    "LEFT JOIN employee_achievements_info s4 ON s1.id = s4.base_project_id " +
                    "WHERE " +
                    "s1.desgin_status = '4' " +
                    "and " +
                    "(s1.district = #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time >= #{startTime} " +
                    "and " +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')"
    )
    List<OneCensus9> DesginMonthAchievementsList(CostVo2 costVo2);


    @Select(
            "select  " +
                    "YEAR(s1.create_time) yearTime,  " +
                    "MONTH(s1.create_time) monthTime,  " +
                    "COUNT(s2.id) budCountA,  " +
                    "SUM(IFNULL(s2.amount_cost,0)) amountCost,  " +
                    "SUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountA,  " +
                    "COUNT(s3.id) lastCount,  " +
                    "SUM(IFNULL(s3.review_number,0)) reviewNumber,  " +
                    "COUNT(s4.id) settCount,  " +
                    "SUM(IFNULL(s7.sumbit_money,0)) sumbitMoney,  " +
                    "SUM(IFNULL(s4.authorized_number,0)) authorizedNumber,  " +
                    "SUM(IFNULL(s4.subtract_the_number,0)) subtractTheNumber,  " +
                    "COUNT(s5.id) trackCount,  " +
                    "SUM(IFNULL(s5.contract_amount,0)) contractAmount  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id  " +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id  " +
                    "LEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id  " +
                    "LEFT JOIN settlement_info s7 ON s1.id = s7.base_project_Id  " +
                    "WHERE  " +
                    "s1.del_flag = '0'  " +
                    "and  " +
                    "s1.a_b = '1'  " +
                    "and  " +
                    "(s1.district = #{district} or #{district} = '')  " +
                    "and  " +
                    "s1.create_time >= #{startTime}  " +
                    "and  " +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')  " +
                    "GROUP BY  " +
                    "YEAR(s1.create_time),  " +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus10> costTaskCensusList(CostVo2 costVo2);

    @Select(
            "select  " +
                    "YEAR(s1.create_time) yearTime,  " +
                    "MONTH(s1.create_time) monthTime,  " +
                    "COUNT(s2.id) budCountB,  " +
                    "SUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountB  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id  " +
                    "WHERE  " +
                    "s1.del_flag = '0'  " +
                    "and  " +
                    "s1.a_b = '2'  " +
                    "and  " +
                    "(s1.district = #{district} or #{district} = '')  " +
                    "and  " +
                    "s1.create_time >= #{startTime}  " +
                    "and  " +
                    "(s1.create_time <= #{endTime} or  #{endTime} = '')  " +
                    "GROUP BY  " +
                    "YEAR(s1.create_time),  " +
                    "MONTH(s1.create_time)"
    )
    List<OneCensus10> costTaskCensusList2(CostVo2 costVo2);

    @Select("select  " +
            "\t\t\tYEAR(s1.create_time) yearTime,  " +
            "\t\t\tMONTH(s1.create_time) monthTime,  " +
            "\t\t\tCOUNT(s2.id) budCountB,  " +
            "\t\t\tSUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountB,  " +
            "\t\t\ts7.budget_achievements performB  " +
            "\t\t\tFROM  " +
            "\t\t\tbase_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id  " +
            "\t\t\tLEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id  " +
            "\t\t\tLEFT JOIN achievements_info s7 ON s1.id = s7.base_project_id  " +
            "\t\t\tWHERE  " +
            "\t\t\ts1.del_flag = '0'  " +
            "\t\t\tand  " +
            "\t\t\ts1.a_b = '2'  " +
            "\t\t\tand  " +
            "\t\t\t(s1.district = #{district} or #{district} = '')  " +
            "\t\t\tand  " +
            "\t\t\ts1.create_time >= #{statTime}  " +
            "\t\t\tand  " +
            "\t\t\t(s1.create_time <= #{endTime} or  #{endTime} = '')  " +
            "\t\t\tGROUP BY  " +
            "\t\t\tYEAR(s1.create_time),  " +
            "\t\t\tMONTH(s1.create_time)")
    List<OneCensus10> EmployeecostTaskCensusList2(EmployeeVo employeeVo);

    @Select("select  " +
            "\tYEAR(s1.create_time) yearTime,  " +
            "\tMONTH(s1.create_time) monthTime,  " +
            "\tCOUNT(s2.id) budCountA,  " +
            "\tSUM(IFNULL(s2.amount_cost,0)) amountCost,  " +
            "\tSUM(IFNULL(s6.cost_total_amount,0)) costTotalAmountA,  " +
            "\tCOUNT(s3.id) lastCount,  " +
            "\tSUM(IFNULL(s3.review_number,0)) reviewNumber,  " +
            "\tCOUNT(s4.id) settCount,  " +
            "\tSUM(IFNULL(s7.sumbit_money,0)) sumbitMoney,  " +
            "\tSUM(IFNULL(s4.authorized_number,0)) authorizedNumber,  " +
            "\tSUM(IFNULL(s4.subtract_the_number,0)) subtractTheNumber,  " +
            "\tCOUNT(s5.id) trackCount,  " +
            "\tSUM(IFNULL(s5.contract_amount,0)) contractAmount,  " +
            "\ts8.budget_achievements performA,  " +
            "\ts8.upsubmit_achievements lastPerform,  " +
            "\ts8.downsubmit_achievements settlePerform,  " +
            "\ts8.truck_achievements truckPerform  " +
            "\tFROM  " +
            "\tbase_project s1 LEFT JOIN budgeting s2 ON s1.id = s2.base_project_id  " +
            "\tLEFT JOIN last_settlement_review s3 ON s1.id = s2.base_project_id  " +
            "\tLEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id  " +
            "\tLEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id  " +
            "\tLEFT JOIN cost_preparation s6 ON s1.id = s6.base_project_id  " +
            "\tLEFT JOIN settlement_info s7 ON s1.id = s7.base_project_Id  " +
            "\tLEFT JOIN achievements_info s8 ON s1.id = s8.base_project_id  " +
            "\tWHERE  " +
            "\ts1.del_flag = '0'  " +
            "\tand  " +
            "\ts1.a_b = '1'  " +
            "\tand  " +
            "\t(s1.district = #{district} or #{district} = '')  " +
            "\tand  " +
            "\ts1.create_time >= #{statTime}  " +
            "\tand  " +
            "\t(s1.create_time <= #{endTime} or  #{endTime} = '')  " +
            "\tGROUP BY  " +
            "\tYEAR(s1.create_time),  " +
            "\tMONTH(s1.create_time)")
    List<OneCensus10> EmployeecostTaskCensusList(EmployeeVo employeeVo);

    @Select(
            "UPDATE   " +
                    "base_project  " +
                    "SET   " +
                    "merge_flag = '0'   " +
                    "WHERE  " +
                    "id = #{id}"
    )
    void updateMergeProject0(@Param("id") String id);

    @Select(
            "UPDATE   " +
                    "base_project  " +
                    "SET   " +
                    "merge_flag = '1'   " +
                    "WHERE  " +
                    "id = #{id}"
    )
    void updateMergeProject1(@Param("id") String id);

    @Select("select   " +
            "application_num applicationNum,  " +
            "cea_num ceaNum,  " +
            "id id,  " +
            "project_num projectNum,  " +
            "project_name projectName,  " +
            "(  " +
            " case  should_be  " +
            "\twhen '0' then '是'\t  " +
            "\twhen '1' then '否'\t  " +
            "\tend  " +
            ") shouldBe,  " +
            "(  " +
            "case district  " +
            "  when '1' then '芜湖'  " +
            "  when '2' then '马鞍山'  " +
            "  when '3' then '江北'  " +
            "  when '4' then '吴江'  " +
            "  end  " +
            ") as district,  " +
            "(  " +
            "case design_category  " +
            "  when '1' then '市政管道'  " +
            "  when '2' then '管网改造'  " +
            "  when '3' then '新建小区'  " +
            "  when '4' then '二次供水项目'  " +
            "  when '5' then '工商户'  " +
            "  when '6' then '居民装接水'  " +
            "  when '7' then '行政事业'  " +
            "  end  " +
            ") as designCategory,  " +
            "desgin_status desginStatus ,  " +
            "construction_unit constructionUnit,  " +
            "contacts contacts,  " +
            "contact_number contactNumber,  " +
            "customer_name customerName,  " +
            "(case subject  " +
            "  when '1' then '居民住户'  " +
            "  when '2' then '开发商'  " +
            "  when '3' then '政府事业'  " +
            "  when '4' then '工商户'  " +
            "  when '5' then '芜湖华衍'  " +
            "  end  " +
            ") as subject,  " +
            "customer_phone customerPhone,  " +
            "construction_organization constructionOrganization,  " +
            "(  " +
            "case project_nature  " +
            "  when '1' then '新建'  " +
            "  when '2' then '改造'  " +
            "  end  " +
            ") as projectNature,  " +
            "(  " +
            "case project_category  " +
            "  when '1' then '住宅区配套'  " +
            "  when '2' then '商业区配套'  " +
            "  when '3' then '工商区配套'  " +
            "  end  " +
            ") as projectCategory, " +
            " ( " +
            "  case construction_organization " +
            "  when '1' then 'xxx有限公司' " +
            "  when '2' then 'xxx有限公司' " +
            "  when '3' then 'xxx有限公司' " +
            "  end  " +
            "   ) as constructionOrganization,   " +
            "water_address waterAddress,  " +
            "(  " +
            "case water_supply_type  " +
            "  when '1' then '直供水'  " +
            "  when '2' then '二次供水'  " +
            "  end  " +
            ") as waterSupplyType,  " +
            "this_declaration thisDeclaration,  " +
            "agent agent,  " +
            "agent_phone agentPhone,  " +
            "application_date applicationDate,  " +
            "business_location businessLocation,  " +
            "business_types businessTypes,  " +
            "(  " +
            "case a_b  " +
            "  when '1' then 'A'  " +
            "  when '2' then 'B'  " +
            "  end  " +
            ") as aB,  " +
            "water_use waterUse,  " +
            "fire_table_size fireTableSize,  " +
            "classification_caliber classificationCaliber,  " +
            "water_meter_diameter waterMeterDiameter,  " +
            "virtual_code virtualCode ,  " +
            "site site,  " +
            "system_number systemNumber,  " +
            "proposer proposer,  " +
            "application_number applicationNumber,  " +
            "management_table managementTable  " +
            "from base_project  " +
            "where id = #{id}")
    BaseProject selectById(@Param("id") String id);


    @Select(
            "select  " +
                    "id,  " +
                    "application_num applicationNum,  " +
                    "cea_num ceaNum,  " +
                    "should_be shouldBe,  " +
                    "id id,  " +
                    "virtual_code virtualCode ," +
                    "project_num projectNum,  " +
                    "project_name projectName,  " +
                    "district district,  " +
                    "design_category designCategory,  " +
                    "construction_unit constructionUnit,  " +
                    "contacts contacts,  " +
                    "contact_number contactNumber,  " +
                    "customer_name customerName,  " +
                    "subject subject,  " +
                    "customer_phone customerPhone,  " +
                    "construction_organization constructionOrganization,  " +
                    "project_nature projectNature,  " +
                    "project_category projectCategory,  " +
                    "water_address waterAddress,  " +
                    "water_supply_type waterSupplyType,  " +
                    "this_declaration thisDeclaration,  " +
                    "agent agent,  " +
                    "agent_phone agentPhone,  " +
                    "application_date applicationDate,  " +
                    "business_location businessLocation,  " +
                    "business_types businessTypes,  " +
                    "a_b aB,  " +
                    "water_use waterUse,  " +
                    "fire_table_size fireTableSize,  " +
                    "classification_caliber classificationCaliber,  " +
                    "water_meter_diameter waterMeterDiameter,  " +
                    "site site,  " +
                    "system_number systemNumber,  " +
                    "proposer proposer,  " +
                    "application_number applicationNumber,  " +
                    "management_table managementTable  " +
                    "from base_project  " +
                    "where id = #{id}"
    )
    BaseProject selectById2(@Param("id") String id);


    @Select(
            "SELECT  " +
                    "SUM(IFNULL(budget_money,0)+IFNULL(upsubmit_money,0)+IFNULL(downsubmit_money,0)+IFNULL(truck_money,0))  " +
                    "FROM  " +
                    "base_project s1,  " +
                    "income_info s2  " +
                    "WHERE  " +
                    "s1.id = s2.base_project_id  " +
                    "and  " +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingIncome(@Param("id") String id);

    @Select(
            "SELECT  " +
                    "SUM(IFNULL(s2.amount_outsourcing,0)+IFNULL(s3.amount_outsourcing,0)+IFNULL(s4.amount_outsourcing,0)+IFNULL(s5.outsource_money,0))  " +
                    "FROM  " +
                    "base_project s1 LEFT JOIN budgeting  s2 ON s1.id = s2.base_project_id  " +
                    "LEFT JOIN last_settlement_review s3 ON s1.id = s3.base_project_id  " +
                    "LEFT JOIN settlement_audit_information s4 ON s1.id = s4.base_project_id  " +
                    "LEFT JOIN track_audit_info s5 ON s1.id = s5.base_project_id  " +
                    "WHERE  " +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingExpenditure1(@Param("id") String id);

    @Select(
            "SELECT  " +
                    "SUM(IFNULL(budget_achievements,0)+IFNULL(upsubmit_achievements,0)+IFNULL(downsubmit_achievements,0)+IFNULL(truck_achievements,0))  " +
                    "FROM  " +
                    "base_project s1,  " +
                    "achievements_info s2  " +
                    "WHERE  " +
                    "s1.id = s2.base_project_id  " +
                    "AND  " +
                    "s1.building_project_id = #{id}"
    )
    BigDecimal consultingExpenditure2(@Param("id") String id);

    @Select("select * from base_project where project_num = #{projectNum} or project_name = #{projectName}")
    List<BaseProject> duplicateChecking(BaseProject baseProject);

    @Select("select * from base_project where (project_num = #{projectNum} or project_name = #{projectName}) and id != #{id}")
    List<BaseProject> duplicateCheckingByUpdate(BaseProject baseProject);

    @Select("select * from base_project where project_num = #{projectNum} or project_name = #{projectName}")
    List<net.zlw.cloud.progressPayment.model.BaseProject> duplicateChecking2(net.zlw.cloud.progressPayment.model.BaseProject baseProject);

    @Select("select * from base_project where (project_num = #{projectNum} or project_name = #{projectName}) and id != #{id}")
    List<BaseProject> duplicateCheckingByUpdate2(net.zlw.cloud.progressPayment.model.BaseProject baseProject);
}

package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.BaseProject;
import net.zlw.cloud.designProject.model.IndividualVo;
import net.zlw.cloud.designProject.model.OneCensus;
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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
                    "building_project_id = #{id}"
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

    String projectCount(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    " year(bp.create_time) yeartime,\n" +
                    " month(bp.create_time) monthtime,\n" +
                    " SUM(design_category = 1) AS municipalPipeline,\n" +
                    " SUM(design_category = 2) AS networkReconstruction,\n" +
                    " SUM(design_category = 3) AS newCommunity,\n" +
                    " SUM(design_category = 4) AS secondaryWater,\n" +
                    " SUM(design_category = 5) AS commercialHouseholds,\n" +
                    " SUM(design_category = 6) AS waterResidents,\n" +
                    " SUM(design_category = 7) AS administration\n" +
                    "FROM\n" +
                    " base_project bp\n" +
                    "where\n" +
                    "founder_id = #{id}\n" +
                    "and\n" +
                    "(district = #{district} or #{district} = '')\n" +
                    "and\n" +
                    "create_time>= #{startTime}\n" +
                    "and \n" +
                    "(create_time<= #{endTime} or  #{endTime} = '') \n" +
                    " group by year(bp.create_time),\n" +
                    " month(bp.create_time)"
    )
    List<OneCensus> censusList(@Param("district") String district,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("id") String id);

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
                    "(blueprint_start_time<= #{endTime} or  #{endTime} = '') "
    )
    List<BaseProject> individualList(IndividualVo individualVo);
}

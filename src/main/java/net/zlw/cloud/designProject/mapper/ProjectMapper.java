package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.BaseProject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

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
}

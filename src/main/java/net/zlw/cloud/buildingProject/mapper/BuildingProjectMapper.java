package net.zlw.cloud.buildingProject.mapper;

import net.zlw.cloud.buildingProject.model.BuildingProject;
import net.zlw.cloud.buildingProject.model.vo.BaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/11 11:48
 **/
@Mapper
public interface BuildingProjectMapper extends tk.mybatis.mapper.common.Mapper<BuildingProject> {

    @Select("SELECT     " +
            "   distinct bp.id,     " +
            "   building_project_name buildingProjectName,    " +
            "   building_project_code buildingProjectCode    " +
            "   FROM     " +
            "   building_project bp     " +
            "   left join base_project b on b.building_project_id != bp.id     " +
            " where bp.del_flag = 0")
    List<BuildingProject> findBuildingProject();

    @Select("select * from building_project where id = #{id}")
    BuildingProject selectById(@Param("id") String id);

    @Select("SELECT" +
            "building_project_name buildingProjectName," +
            "building_project_code buildingProjectCode," +
            "building_unit  buildingUnit," +
            "supervisor_unit supervisorUnit," +
            "construction_units constructionUnits," +
            "design_units designUnits," +
            "(" +
            "   case project_type" +
            "        when '1' then '住宅区配套'" +
            "when '2' then '商业区配套'" +
            "        when '3' then '工商区配套'" +
            "end" +
            ") as projectType," +
            "(" +
            "   case project_nature" +
            "when '0' then '新建'" +
            "when '1' then '改造'" +
            "end" +
            ") as projectNature," +
            "client_name clientName," +
            "cost_amount costAmount," +
            "project_site projectSite," +
            "contract_start_time contractStartTime," +
            "contract_end_time contractEndTime," +
            "actual_start_time actualStartTime," +
            "actual_end_time  actualEndTime" +
            "FROM building_project where id = #{id}")
    BuildingProject findOne(String id);


    @Select("SELECT * FROM building_project WHERE building_project_name = #{name} OR  building_project_code = #{code}")
    List<BuildingProject> findNameAndCode(@Param("name") String name ,@Param("code") String code);

    @Select("SELECT * FROM building_project WHERE (building_project_name = #{name} OR  building_project_code = #{code}) AND id != #{id} ")
    List<BuildingProject> findNameAndCodeAndId(@Param("id") String id, @Param("name") String name ,@Param("code") String code);

    @Update("UPDATE building_project SET del_flag= '1' WHERE id = #{id}")
    void deleteBuilding(@Param("id") String id);

    @Select("select * from building_project where id = #{id}")
    BuildingProject selectOneBuilding(@Param("id") String id);

    @Select("SELECT" +
            "  b2.id," +
            "  b2.cea_num ceaNum," +
            "  b2.project_num projectNum," +
            "  b2.project_name projectName," +
            "  b2.construction_unit constructionUnit," +
            "  b2.customer_name customerName," +
            "   ( CASE b2.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) projectNature," +
            "   (" +
            "  CASE" +
            "  b2.design_category " +
            "  WHEN '1' THEN" +
            "   '市政管道' " +
            "  WHEN '2' THEN" +
            "   '管网改造' " +
            "  WHEN '3' THEN" +
            "   '新建小区' " +
            "  WHEN '4' THEN" +
            "   '二次供水项目' " +
            "  WHEN '5' THEN" +
            "   '工商户' " +
            "  WHEN '6' THEN" +
            "   '居民装接水' " +
            "  WHEN '7' THEN" +
            "   '行政事业' " +
            "  END " +
            "   ) designCategory," +
            "  b3.amount_cost amountCost," +
            "  p1.contract_amount contractAmount," +
            "  p1.cumulative_payment_times cumulativePaymentTimes," +
            "  t1.cea_total_money ceaTotalMoney," +
            "  v1.cumulative_change_amount cumulativeChangeAmount," +
            "  s1.authorized_number authorizedNumber," +
            "  e1.actual_amount actualAmount " +
            "  FROM" +
            "  building_project b1" +
            "  LEFT JOIN base_project b2 ON b2.building_project_id = b1.id" +
            "  LEFT JOIN budgeting b3 ON b3.base_project_id = b2.id" +
            "  LEFT JOIN progress_payment_information p1 ON p1.base_project_id = b2.id" +
            "  LEFT JOIN track_audit_info t1 ON t1.base_project_id = b2.id" +
            "  LEFT JOIN visa_change_information v1 ON v1.base_project_id = b2.id" +
            "  LEFT JOIN settlement_audit_information s1 ON s1.base_project_id = b2.id " +
            "  LEFT JOIN employee_achievements_info e1 ON e1.base_project_id = b2.id " +
            "  WHERE" +
            "  b1.del_flag = '0' " +
            "  AND b2.del_flag = '0'" +
            "  AND b1.id = #{id}")
    List<BaseVo> selectBaseProjectList(@Param("id") String id);
}

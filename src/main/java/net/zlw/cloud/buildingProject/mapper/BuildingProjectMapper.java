package net.zlw.cloud.buildingProject.mapper;

import net.zlw.cloud.buildingProject.model.BuildingProject;
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

    @Select("SELECT  " +
            "   distinct bp.id,  " +
            "   building_project_name buildingProjectName, " +
            "   building_project_code buildingProjectCode " +
            "   FROM  " +
            "   building_project bp  " +
            "   left join base_project b on b.building_project_id != bp.id  " +
            "   where bp.status = 0 and bp.or_submit = 0 and bp.merge_flag = 2")
    List<BuildingProject> findBuildingProject();

    @Select("select * from building_project where id = #{id}")
    BuildingProject selectById(@Param("id") String id);

    @Select("SELECT\n" +
            "building_project_name buildingProjectName,\n" +
            "building_project_code buildingProjectCode,\n" +
            "building_unit  buildingUnit,\n" +
            "supervisor_unit supervisorUnit,\n" +
            "construction_units constructionUnits,\n" +
            "design_units designUnits,\n" +
            "(\n" +
            "   case project_type\n" +
            "        when '1' then '住宅区配套'\n" +
            "\twhen '2' then '商业区配套'\n" +
            "        when '3' then '工商区配套'\n" +
            "\tend\n" +
            ") as projectType,\n" +
            "(\n" +
            "   case project_nature\n" +
            "\twhen '0' then '新建'\n" +
            "\twhen '1' then '改造'\n" +
            "\tend\n" +
            ") as projectNature,\n" +
            "client_name clientName,\n" +
            "cost_amount costAmount,\n" +
            "project_site projectSite,\n" +
            "contract_start_time contractStartTime,\n" +
            "contract_end_time contractEndTime,\n" +
            "actual_start_time actualStartTime,\n" +
            "actual_end_time  actualEndTime\n" +
            "FROM building_project where id = #{id}")
    BuildingProject findOne(String id);


    @Select("SELECT * FROM building_project WHERE building_project_name = #{name} OR  building_project_code = #{code}")
    List<BuildingProject> findNameAndCode(@Param("name") String name ,@Param("code") String code);

    @Select("SELECT * FROM building_project WHERE (building_project_name = #{name} OR  building_project_code = #{code}) AND id != #{id} ")
    List<BuildingProject> findNameAndCodeAndId(@Param("id") String id, @Param("name") String name ,@Param("code") String code);

    @Update("UPDATE building_project SET `status`= '1' WHERE id = #{id}")
    void deleteBuilding(@Param("id") String id);

    @Select("select * from building_project where id = #{id}")
    BuildingProject selectOneBuilding(@Param("id") String id);
}

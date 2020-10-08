package net.zlw.cloud.maintenanceProjectInformation.mapper;

import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.StatisticalFigureVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.StatisticalNumberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MaintenanceProjectInformationMapper extends tk.mybatis.mapper.common.Mapper<MaintenanceProjectInformation> {


    //查询状态正常的所有检维修项目信息
//    @Select("select * from maintenance_project_information where del_flag = '0'")
//    List<MaintenanceProjectInformation> selectAllByDelFlag();


    /**
     * 检维修-删除
     * @param id
     */
    @Update(
            "update  maintenance_project_information set del_flag = '1' where id = #{id}"
    )
    void deleteMaintenanceProjectInformation(@Param("id") String id);


    @Select("select * from maintenance_project_information where id = #{id}")
    MaintenanceProjectInformation selectById(@Param("id") String id);

    @Select("select * from maintenance_project_information where maintenance_item_id = #{maintenanceItemId}")
    MaintenanceProjectInformation selectBymaintenanceItemId(@Param("maintenanceItemId") String maintenanceItemId);

    @Select("SELECT " +
            "count(*) total, " +
            "count(if(type=1,true,null)) loading, " +
            "count(if(type=2,true,null)) run, " +
            "count(if(type=5,true,null)) over " +
            "FROM " +
            "maintenance_project_information " +
            "where " +
            "del_flag = '0' " +
            "and " +
            "(project_address = #{projectAddress} or #{projectAddress}='')")
    StatisticalNumberVo statisticalNumber(@Param("projectAddress")String projectAddress);

    @Select("SELECT YEAR " +
            "( bp.create_time ) yeartime, " +
            "MONTH ( bp.create_time ) monthtime, " +
            "SUM( maintenance_item_type = 0 ) AS param1, " +
            "SUM( maintenance_item_type = 1 ) AS param2, " +
            "SUM( maintenance_item_type = 2 ) AS param3, " +
            "SUM( maintenance_item_type = 3 ) AS param4, " +
            "SUM( maintenance_item_type = 4 ) AS param5, " +
            "SUM( maintenance_item_type = 5 ) AS param6, " +
            "SUM( maintenance_item_type = 6 ) AS param7, " +
            "SUM( maintenance_item_type = 7 ) AS param8, " +
            "SUM( maintenance_item_type = 8 ) AS param9, " +
            "SUM( maintenance_item_type = 9 ) AS param10  " +
            "FROM " +
            "maintenance_project_information bp  " +
            "WHERE " +
            "( project_address = #{projectAddress} OR #{projectAddress} = '' )  " +
            "AND create_time >= #{startDate}  " +
            "AND ( create_time <= #{endDate} OR #{endDate} = '' )  " +
            "AND del_flag = '0'  " +
            "GROUP BY " +
            "YEAR ( bp.create_time ), " +
            "MONTH ( bp.create_time )")
    List<StatisticalFigureVo> statisticalFigure(@Param("projectAddress") String projectAddress, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT  " +
            "count( * ) " +
            "FROM " +
            "maintenance_project_information " +
            "WHERE " +
            "del_flag = '0' " +
            "AND date_format(create_time ,'%Y-%m' ) = #{day}")
    Integer monthCount(@Param("day")String day);

    @Select("SELECT  " +
            "count( * ) " +
            "FROM " +
            "maintenance_project_information " +
            "WHERE " +
            "del_flag = '0' " +
            "AND date_format(create_time ,'%Y' ) = #{year}")
    Integer yearCount(String year);
}
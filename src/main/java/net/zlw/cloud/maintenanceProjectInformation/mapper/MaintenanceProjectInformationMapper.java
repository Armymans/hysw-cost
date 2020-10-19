package net.zlw.cloud.maintenanceProjectInformation.mapper;

import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationReturnVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
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

    @Select("  SELECT \n" +
            "                          m.id id, \n" +
            "                          m.maintenance_item_id maintenanceItemId, \n" +
            "                          m.maintenance_item_name maintenanceItemName,  \n" +
            "                          ( CASE m.maintenance_item_type WHEN '0' THEN '道路恢复工程' WHEN '1' THEN '表位改造' WHEN '2' THEN '故障换表' WHEN '3' THEN '水表周检换表' \n" +
            "                          WHEN '4' THEN 'DN300以上管道抢维修' WHEN '5' THEN 'DN300以下管道抢维修' WHEN '6' THEN '设备维修购置' WHEN '7' THEN '房屋修缮' WHEN '8' THEN '绿化种植' \n" +
            "                          WHEN '9' THEN '装饰及装修' END ) AS maintenanceItemType, \n" +
            "                          ( CASE m.type WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '已完成' END ) AS type, \n" +
            "                          ( CASE m.project_address WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district, \n" +
            "                          m.customer_name customerName, \n" +
            "                          m.prepare_people preparePeople, \n" +
            "                          c.construction_unit_name constructionUnitName, \n" +
            "                          m.review_amount reviewAmount, \n" +
            "                          p.authorized_number contractAmount, \n" +
            "                          m.submit_time submitTime, \n" +
            "                          p.compile_time compileTime  \n" +
            "                         FROM \n" +
            "                          maintenance_project_information m \n" +
            "                          LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id \n" +
            "                          LEFT JOIN settlement_audit_information p ON m.id = p.maintenance_project_information  "+
            "             WHERE\n" +
            "              ( m.del_flag = '0' )  \n" +
            "              AND ( m.maintenance_item_type = #{maintenanceItemType} OR #{maintenanceItemType} = '' )  \n" +
            "              AND ( p.compile_time > #{startTime} OR #{startTime} = '' )  \n" +
            "              AND ( p.compile_time < #{endTime} OR #{endTime} = '' )  \n" +
            "              AND ( m.maintenance_item_name LIKE concat( '%', #{keyWord}, '%' ) OR m.customer_name LIKE concat( '%', #{keyWord}, '%' ) )  \n" +
            "              AND ( \n" +
            "              m.type = #{type} OR #{type} = '') ")
    List<MaintenanceProjectInformationReturnVo> selectAllByDelFlag(PageRequest pageRequest);





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
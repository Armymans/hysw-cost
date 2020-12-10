package net.zlw.cloud.maintenanceProjectInformation.mapper;

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

    @Select(
            "SELECT " +
                    " distinct m.id id, " +
                    "m.maintenance_item_id maintenanceItemId, " +
                    "m.maintenance_item_name maintenanceItemName," +
                    "m.area area, " +
                    "( " +
                    "CASE " +
                    "m.maintenance_item_type  " +
                    "WHEN '0' THEN " +
                    "'道路恢复工程'  " +
                    "WHEN '1' THEN " +
                    "'表位改造'  " +
                    "WHEN '2' THEN " +
                    "'故障换表'  " +
                    "WHEN '3' THEN " +
                    "'水表周检换表'  " +
                    "WHEN '4' THEN " +
                    "'DN300以上管道抢维修'  " +
                    "WHEN '5' THEN " +
                    "'DN300以下管道抢维修'  " +
                    "WHEN '6' THEN " +
                    "'设备维修购置'  " +
                    "WHEN '7' THEN " +
                    "'房屋修缮'  " +
                    "WHEN '8' THEN " +
                    "'绿化种植'  " +
                    "WHEN '9' THEN " +
                    "'装饰及装修'  " +
                    "END  " +
                    ") AS maintenanceItemType, " +
                    "( CASE ai.maintenance_flag WHEN '1' THEN '检维修未通过' WHEN '0' THEN '检维修确认未通过' END) AS maintenanceFlag," +
                    "( CASE m.type WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '已完成' END ) AS type, " +
                    "( CASE m.project_address WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS projectAddress, " +
                    "m.customer_name customerName, " +
                    "m.prepare_people preparePeople, " +
                    "c.construction_unit_name constructionUnitName, " +
                    "m.review_amount reviewAmount, " +
                    "p.authorized_number contractAmount, " +
                    "m.submit_time submitTime, " +
                    "p.compile_time compileTime , " +
                    "m.founder_id founderId " +
                    "FROM " +
                    "maintenance_project_information m " +
                    "LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id " +
                    "LEFT JOIN settlement_audit_information p ON m.id = p.maintenance_project_information  " +
                    "LEFT JOIN audit_info ai ON m.id = ai.base_project_id " +
                    "WHERE " +
                    "( m.del_flag = '0' )  " +
                    "AND " +
                    "( m.founder_id = #{uid} or #{uid} = '')  " +
                    "AND ( " +
                    "m.maintenance_item_type = #{maintenanceItemType} or #{maintenanceItemType} = '') " +
                    "AND ( " +
                    "p.compile_time >= #{startTime} OR #{startTime} = '' ) " +
                    "AND ( " +
                    "p.compile_time <= #{endTime} OR #{endTime} = '' ) " +
                    "AND ( " +
                    "m.maintenance_item_name LIKE concat( " +
                    "'%',#{keyWord}, '%' ) OR m.customer_name LIKE concat( '%', #{keyWord}, '%' ) ) " +
                    "AND ( " +
                    "m.type = #{type} OR #{type} = '') " +
                    "ORDER BY m.create_time DESC"
    )
    List<MaintenanceProjectInformationReturnVo> selectAllByDelFlag(PageRequest pageRequest);

    @Select(
            "SELECT " +
                    "distinct m.id id , " +
                    "m.maintenance_item_id maintenanceItemId, " +
                    "m.maintenance_item_name maintenanceItemName, " +
                    "m.area area," +
                    "( " +
                    "CASE " +
                    "m.maintenance_item_type  " +
                    "WHEN '0' THEN " +
                    "'道路恢复工程'  " +
                    "WHEN '1' THEN " +
                    "'表位改造'  " +
                    "WHEN '2' THEN " +
                    "'故障换表'  " +
                    "WHEN '3' THEN " +
                    "'水表周检换表'  " +
                    "WHEN '4' THEN " +
                    "'DN300以上管道抢维修'  " +
                    "WHEN '5' THEN " +
                    "'DN300以下管道抢维修'  " +
                    "WHEN '6' THEN " +
                    "'设备维修购置'  " +
                    "WHEN '7' THEN " +
                    "'房屋修缮'  " +
                    "WHEN '8' THEN " +
                    "'绿化种植'  " +
                    "WHEN '9' THEN " +
                    "'装饰及装修'  " +
                    "END  " +
                    ") AS maintenanceItemType, " +
                    "( CASE ai.maintenance_flag WHEN '1' THEN '检维修审核' WHEN '0' THEN '检维修确认审核' END) AS maintenanceFlag," +
                    "( CASE m.type WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '已完成' END ) AS type, " +
                    "( CASE m.project_address WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS projectAddress, " +
                    "m.customer_name customerName, " +
                    "m.prepare_people preparePeople, " +
                    "c.construction_unit_name constructionUnitName, " +
                    "m.review_amount reviewAmount, " +
                    "p.authorized_number contractAmount, " +
                    "m.submit_time submitTime, " +
                    "p.compile_time compileTime  " +
                    "FROM " +
                    "maintenance_project_information m " +
                    "LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id " +
                    "LEFT JOIN settlement_audit_information p ON m.id = p.maintenance_project_information  " +
                    "LEFT JOIN audit_info ai on m.id = ai.base_project_id " +
                    "WHERE " +
                    "( m.del_flag = '0' )  " +
                    "AND " +
                    "(ai.audit_result = '0')  " +
                    "AND " +
                    "(ai.auditor_id = #{currentPeople} OR #{currentPeople} = '' ) and " +
                    "( m.founder_id = #{uid} or ai.auditor_id = #{uid})  " +
                    "AND ( " +
                    "m.maintenance_item_type = #{maintenanceItemType} or #{maintenanceItemType} = '') " +
                    "AND ( " +
                    "p.compile_time >= #{startTime} OR #{startTime} = '' ) " +
                    "AND ( " +
                    "p.compile_time <= #{endTime} OR #{endTime} = '' ) " +
                    "AND ( " +
                    "m.maintenance_item_name LIKE " +
                    "concat( '%',#{keyWord},'%' ) OR m.customer_name LIKE concat( '%', #{keyWord}, '%' )) " +
                    "AND " +
                    "(m.type = #{type} OR #{type} = '') " +
                    "ORDER BY " +
                    "m.create_time DESC"
    )
    List<MaintenanceProjectInformationReturnVo> selectAllByDelFlag0(PageRequest pageRequest);

    @Select("SELECT " +
            "distinct m.id id, " +
            "m.maintenance_item_id maintenanceItemId, " +
            "m.maintenance_item_name maintenanceItemName," +
            "m.area area, " +
            "( " +
            "CASE " +
            "m.maintenance_item_type  " +
            "WHEN '0' THEN " +
            "'道路恢复工程'  " +
            "WHEN '1' THEN " +
            "'表位改造'  " +
            "WHEN '2' THEN " +
            "'故障换表'  " +
            "WHEN '3' THEN " +
            "'水表周检换表'  " +
            "WHEN '4' THEN " +
            "'DN300以上管道抢维修'  " +
            "WHEN '5' THEN " +
            "'DN300以下管道抢维修'  " +
            "WHEN '6' THEN " +
            "'设备维修购置'  " +
            "WHEN '7' THEN " +
            "'房屋修缮'  " +
            "WHEN '8' THEN " +
            "'绿化种植'  " +
            "WHEN '9' THEN " +
            "'装饰及装修'  " +
            "END  " +
            ") AS maintenanceItemType, " +
            "( CASE a.maintenance_flag WHEN '1' THEN '检维修审核' WHEN '0' THEN '检维修确认审核' END) AS maintenanceFlag,  " +
            "( CASE m.type WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '已完成' END ) AS type, " +
            "( CASE m.project_address WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS projectAddress, " +
            "m.customer_name customerName, " +
            "m.prepare_people preparePeople, " +
            "c.construction_unit_name constructionUnitName, " +
            "m.review_amount reviewAmount, " +
            "p.authorized_number contractAmount, " +
            "m.submit_time submitTime, " +
            "p.compile_time compileTime, " +
            "m.founder_id founderId " +
            "FROM " +
            "maintenance_project_information m " +
            "LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id " +
            "LEFT JOIN settlement_audit_information p ON m.id = p.maintenance_project_information  " +
            "LEFT JOIN audit_info a ON m.id = a.base_project_id   " +
            "WHERE " +
            "( m.del_flag = '0' )  " +
            "AND ( " +
            "m.maintenance_item_type = #{maintenanceItemType} or #{maintenanceItemType} = '') " +
            "AND ( " +
            "p.compile_time >= #{startTime} OR #{startTime} = '' ) " +
            "AND ( " +
            "p.compile_time <= #{endTime} OR #{endTime} = '' ) " +
            "AND ( " +
            "m.maintenance_item_name LIKE concat( " +
            "'%',#{keyWord}, '%' ) OR m.customer_name LIKE concat( '%', #{keyWord}, '%' ) ) " +
            "AND ( " +
            "m.type = #{type} OR #{type} = '') " +
            "ORDER BY m.create_time DESC")
    List<MaintenanceProjectInformationReturnVo> selectAllByDelFlag1(PageRequest pageRequest);




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

    @Select("SELECT  " +
            "count(*) total,  " +
            "count(if(type=1 or type=3 or type = 4,true,null)) loading,  " +
            "count(if(type=2,true,null)) run,  " +
            "count(if(type=5,true,null)) over  " +
            "FROM  " +
            "maintenance_project_information  " +
            "where  " +
            "del_flag = '0'  " +
            "and  " +
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
    
    @Select("SELECT  " +
            "  m.id,  " +
            "  m.maintenance_item_id maintenanceItemId," +
            "  m.area area,  " +
            "  maintenance_item_name maintenanceItemName, " +
            "  m.project_address projectAddress,     " +
            "  (  " +
            "  CASE  " +
            "  m.maintenance_item_type   " +
            "  WHEN '0' THEN  " +
            "  '道路恢复工程'   " +
            "  WHEN '1' THEN  " +
            "  '表位改造'   " +
            "  WHEN '2' THEN  " +
            "  '故障换表'   " +
            "  WHEN '3' THEN  " +
            "  '水表周检换表'   " +
            "  WHEN '4' THEN  " +
            "  'DN300以上管道抢维修'   " +
            "  WHEN '5' THEN  " +
            "  'DN300以下管道抢维修'   " +
            "  WHEN '6' THEN  " +
            "  '设备维修购置'   " +
            "  WHEN '7' THEN  " +
            "  '房屋修缮'   " +
            "  WHEN '8' THEN  " +
            "  '绿化种植'   " +
            "  WHEN '9' THEN  " +
            "  '装饰及装修'   " +
            "  END   " +
            "  ) maintenanceItemType,  " +
            "  m.submitted_department submittedDepartment,  " +
            "  m.submit_time submitTime,  " +
            "  m.prepare_people preparePeople,  " +
            "  m.review_amount reviewAmount,  " +
            "  m.customer_name customerName,  " +
            "  m.remarkes remarkes,  " +
            "  m.create_time createTime,  " +
            "  m.update_time updateTime,  " +
            "  c.construction_unit_name constructionUnitName,  " +
            "  c.id constructionUnitId,  " +
            "  m.del_flag delFlag,  " +
            "  m.type,  " +
            "  type   " +
            "  FROM  " +
            "  maintenance_project_information m  " +
            "  LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id   " +
            "  WHERE  " +
            "  m.id = #{id}")
    MaintenanceProjectInformation selectIdByMain(@Param("id") String id);

    @Select("  SELECT        " +
            " m.id,        " +
            " m.maintenance_item_id maintenanceItemId,      " +
            " m.area area,        " +
            " maintenance_item_name maintenanceItemName,       " +
            " ( " +
            " CASE        " +
            " m.project_address        " +
            " WHEN '1' THEN        " +
            " '芜湖'         " +
            " WHEN '2' THEN        " +
            " '马鞍山'         " +
            " WHEN '3' THEN        " +
            " '江北'         " +
            " WHEN '4' THEN        " +
            " '吴江'           " +
            " END         " +
            " ) projectAddress,           " +
            " (        " +
            " CASE        " +
            " m.maintenance_item_type         " +
            " WHEN '0' THEN        " +
            " '道路恢复工程'         " +
            " WHEN '1' THEN        " +
            " '表位改造'         " +
            " WHEN '2' THEN        " +
            " '故障换表'         " +
            " WHEN '3' THEN        " +
            " '水表周检换表'         " +
            " WHEN '4' THEN        " +
            " 'DN300以上管道抢维修'         " +
            " WHEN '5' THEN        " +
            " 'DN300以下管道抢维修'         " +
            " WHEN '6' THEN        " +
            " '设备维修购置'         " +
            " WHEN '7' THEN        " +
            " '房屋修缮'         " +
            " WHEN '8' THEN        " +
            " '绿化种植'         " +
            " WHEN '9' THEN        " +
            " '装饰及装修'         " +
            " END         " +
            " ) maintenanceItemType,        " +
            " m.submitted_department submittedDepartment,        " +
            " m.submit_time submitTime,        " +
            " m.prepare_people preparePeople,        " +
            " m.review_amount reviewAmount,        " +
            " m.customer_name customerName,        " +
            " m.remarkes remarkes,        " +
            " m.create_time createTime,        " +
            " m.update_time updateTime,        " +
            " c.construction_unit_name constructionUnitName,        " +
            " c.id constructionUnitId,        " +
            " m.del_flag delFlag,        " +
            " m.type,        " +
            " type         " +
            " FROM        " +
            " maintenance_project_information m        " +
            " LEFT JOIN construction_unit_management c ON m.construction_unit_id = c.id         " +
            " WHERE        " +
            " m.id = #{id}")
    MaintenanceProjectInformation selectIdByMain2(@Param("id") String id);
}
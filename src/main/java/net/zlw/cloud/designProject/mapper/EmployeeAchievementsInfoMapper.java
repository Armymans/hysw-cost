package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.EmployeeAchievementsInfo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpPageVo;
import net.zlw.cloud.employeePerformance.domain.vo.EmpVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname EmployeeAchievementsInfoMapper
 * @Description TODO
 * @Date 2020/11/27 14:23
 * @Created by sjf
 */
@org.apache.ibatis.annotations.Mapper
public interface EmployeeAchievementsInfoMapper extends Mapper<EmployeeAchievementsInfo> {


    @Select("SELECT    " +
            "   e.id,    " +
            "   m.member_name memberName ," +
            "   ( CASE e.dept WHEN '1' THEN '设计' WHEN '2' THEN '造价' END ) dept," +
            "   ( CASE e.over_flag WHEN '0' THEN '否' WHEN '1' THEN '是' END ) overFlag,   " +
            "   ( CASE e.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山'  WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) district , " +
            "   ( CASE e.achievements_type WHEN '1' THEN '设计绩效' WHEN '2' THEN '预算编制绩效' WHEN '3' THEN '上家送审绩效' WHEN '4' THEN '下家送审绩效' WHEN '5' THEN '跟踪审计绩效' END ) achievementsType,    " +
            "   e.accrued_amount accruedAmount,    " +
            "   e.actual_amount actualAmount,    " +
            "   e.balance balance,    " +
            "   b.project_num projectNum,    " +
            "   b.project_name projectName     " +
            "   FROM    " +
            "   employee_achievements_info e    " +
            "   LEFT JOIN base_project b ON e.base_project_id = b.id    " +
            "   LEFT JOIN member_manage m ON m.id = e.member_id     " +
            "   WHERE    " +
            "   e.del_flag = '0'     " +
            "   AND ( e.dept = #{dept} OR #{dept} = '' )     " +
            "   AND ( e.achievements_type = #{achievementsType} OR #{achievementsType} = '' )     " +
            "   AND ( e.over_flag = #{overFlag} OR #{overFlag} = '' )     " +
            "   AND (    " +
            "   m.member_name LIKE concat ( '%', #{keword}, '%' )     " +
            "   OR b.project_num LIKE concat ( '%', #{keword}, '%' )     " +
            "   OR b.project_name LIKE concat ( '%', #{keword}, '%' )     " +
            "   )     " +
            "   ORDER BY    " +
            "   e.create_time DESC")
    List<EmpVo> employeePerformanceFindAll(EmpPageVo empPageVo);

    @Select("SELECT * FROM employee_achievements_info WHERE id = #{id}")
    EmployeeAchievementsInfo selectEmpById(@Param("id") String id);
}

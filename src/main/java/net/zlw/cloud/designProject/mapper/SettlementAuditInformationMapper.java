package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.SettlementAuditInformation;
import net.zlw.cloud.statisticAnalysis.model.EmployeePerformance;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementAuditInformationMapper extends Mapper<SettlementAuditInformation> {
    @Select(
            "select\n" +
                    "month(s2.compile_time) monthTime,\n" +
                    "s1.id,\n" +
                    "s1.district,\n" +
                    "s1.project_name projectName,\n" +
                    "s2.take_time,\n" +
                    "s2.compile_time,\n" +
                    "s3.sumbit_money sumbitMoney,\n" +
                    "s2.authorized_number,\n" +
                    "s2.subtract_the_number,\n" +
                    "s2.whether_account\n" +
                    "from\n" +
                    "base_project s1,\n" +
                    "settlement_audit_information s2,\n" +
                    "settlement_info s3\n" +
                    "where\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "s1.id = s3.base_project_id\n" +
                    "and\n" +
                    "(s2.founder_id =#{id} or #{id} = '')\n" +
                    "and\n" +
                    "compile_time>=#{startTime}\n" +
                    "and\n" +
                    "(compile_time<=#{endTime} or  #{endTime} = '')\n" +
                    "and\n" +
                    "(district=#{district} or  #{district} = '')"
    )
    List<SettlementAuditInformation> settlementAuditInformationList(CostVo2 costVo2);

    @Select(
            "  select\n" +
                    "\tmonth(s2.compile_time) monthTime,\n" +
                    "\ts1.id,\n" +
                    "\ts1.district,\n" +
                    "\ts1.project_name projectName,\n" +
                    "\ts2.take_time,\n" +
                    "\ts2.compile_time,\n" +
                    "\ts3.sumbit_money sumbitMoney,\n" +
                    "\ts2.authorized_number,\n" +
                    "\ts2.subtract_the_number,\n" +
                    "\ts2.whether_account\n" +
                    "\tfrom\n" +
                    "\tbase_project s1,\n" +
                    "\tsettlement_audit_information s2,\n" +
                    "\tsettlement_info s3\n" +
                    "\twhere\n" +
                    "\ts1.id = s2.base_project_id\n" +
                    "\tand\n" +
                    "\ts1.id = s3.base_project_id\n" +
                    "\tand\n" +
                    "\ts2.prepare_people =#{memberId}\n" +
                    "\tand\n" +
                    " compile_time>=#{statTime}\n" +
                    "\tand\n" +
                    " (compile_time<=#{endTime} or  #{endTime} = '')\n" +
                    " and\n" +
                    " (district=#{district} or  #{district} = '')"
    )
    List<SettlementAuditInformation> EmployeesettlementAuditInformationList(EmployeeVo employeeVo);

    @Select(
            "SELECT \n" +
                    "amount_outsourcing\n" +
                    "FROM \n" +
                    "settlement_audit_information s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(s2.district=#{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<SettlementAuditInformation> totalexpenditure(CostVo2 costVo2);

    @Select("SELECT\n" +
            "COUNT(1) projectNum,\n" +
            "SUM(IFNULL( a.budget_achievements, 0 )+ IFNULL( a.upsubmit_achievements, 0 )+ IFNULL( a.downsubmit_achievements, 0 )+ IFNULL( a.truck_achievements, 0 )) achievemen,\n" +
            "YEAR(bt.budgeting_time) yearTime,\n" +
            "MONTH(bt.budgeting_time) monthTIme\n" +
            "FROM `achievements_info` a\n" +
            "LEFT JOIN base_project b on a.base_project_id = b.id\n" +
            "LEFT JOIN budgeting bt on bt.base_project_id = b.id\n" +
            "LEFT JOIN member_manage m on m.id = a.member_id\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(bt.budgeting_time > #{statTime} or #{statTime} = '') and \n" +
            "(bt.budgeting_time < #{endTime} or #{endTime} = '') and \n" +
            "(a.member_id = #{memberId} or #{memberId} = '' )\n" +
            "GROUP BY\n" +
            "YEAR(bt.budgeting_time),\n" +
            "MONTH(bt.budgeting_time)")
    List<EmployeePerformance> EmployeePerformanceAnalysis(EmployeeVo employeeVo);
}

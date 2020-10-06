package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.SettlementAuditInformation;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementAuditInformationMapper extends Mapper<SettlementAuditInformation> {
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
                    "\ts1.founder_id =#{id}\n" +
                    "\tand\n" +
                    " compile_time>=#{startTime}\n" +
                    "\tand\n" +
                    " (compile_time<=#{endTime} or  #{endTime} = '')\n" +
                    " and\n" +
                    " (district=#{district} or  #{district} = '')"
    )
    List<SettlementAuditInformation> settlementAuditInformationList(CostVo2 costVo2);

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
}

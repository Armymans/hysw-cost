package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.LastSettlementReview;
import net.zlw.cloud.statisticAnalysis.model.EmployeeVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface LastSettlementReviewMapper extends Mapper<LastSettlementReview> {
    @Select(
            "select\n" +
                    "month(s2.take_time) monthTime,\n" +
                    "s1.id,\n" +
                    "s1.district,\n" +
                    "s1.project_name projectName,\n" +
                    "s2.take_time,\n" +
                    "s2.compile_time,\n" +
                    "s2.review_number,\n" +
                    "s2.whether_account\n" +
                    "from\n" +
                    "base_project s1,\n" +
                    "last_settlement_review s2\n" +
                    "where\n" +
                    "s1.id = s2.base_project_id\n" +
                    "and\n" +
                    "(s2.founder_id = #{id} or #{id} = '')\n" +
                    "and\n" +
                    "compile_time>=#{startTime}\n" +
                    "and\n" +
                    "(compile_time<=#{endTime} or  #{endTime} = '')\n" +
                    "and\n" +
                    "(district=#{district} or  #{district} = '')"
    )
    List<LastSettlementReview> lastSettlementReviewList(CostVo2 costVo2);

    @Select(
            " select\n" +
                    "\tmonth(s2.take_time) monthTime,\n" +
                    "\ts1.id,\n" +
                    "\ts1.district,\n" +
                    "\ts1.project_name projectName,\n" +
                    "\ts2.take_time,\n" +
                    "\ts2.compile_time,\n" +
                    "\ts2.review_number,\n" +
                    "\ts2.whether_account\n" +
                    "\tfrom\n" +
                    "\tbase_project s1,\n" +
                    "\tlast_settlement_review s2\n" +
                    "\twhere\n" +
                    "\ts1.id = s2.base_project_id\n" +
                    "\tand\n" +
                    "\ts2.prepare_people =#{memberId}\n" +
                    "\tand\n" +
                    " (s2.compile_time>=#{statTime} or #{statTime} = '')\n" +
                    "\tand\n" +
                    " (s2.compile_time<=#{endTime} or  #{endTime} = '')\n" +
                    " and\n" +
                    " (district=#{district} or  #{district} = '')"
    )
    List<LastSettlementReview> EmployeelastSettlementReviewChargeList(EmployeeVo employeeVo);

    @Select(
            "SELECT \n" +
                    "amount_outsourcing\n" +
                    "FROM \n" +
                    "last_settlement_review s1,\n" +
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
    List<LastSettlementReview> totalexpenditure(CostVo2 costVo2);
}

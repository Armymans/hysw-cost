package net.zlw.cloud.designProject.mapper;


import net.zlw.cloud.designProject.model.Budgeting;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@org.apache.ibatis.annotations.Mapper
public interface BudgetingMapper extends Mapper<Budgeting> {

    @Select("select * from budgeting where id = #{id}")
    Budgeting findById(@Param("id")String id);

    @Select(
            "\tselect\n" +
                    "\tmonth(s2.receipt_time) monthTime,\n" +
                    "\ts1.id,\n" +
                    "\ts1.district,\n" +
                    "\ts1.project_name projectName,\n" +
                    "\ts1.a_b aB,\n" +
                    "\ts2.receipt_time,\n" +
                    "\ts2.budgeting_time,\n" +
                    "\ts2.amount_cost,\n" +
                    "\ts3.bidding_price_control biddingPriceControl,\n" +
                    "\ts2.whether_account\n" +
                    "\tfrom\n" +
                    "\tbase_project s1,\n" +
                    "\tbudgeting s2,\n" +
                    "\tvery_establishment s3\n" +
                    "\twhere\n" +
                    "\ts1.id = s2.base_project_id\n" +
                    "\tand\n" +
                    "\ts2.id = s3.budgeting_id\n" +
                    "\tand\n" +
                    "\ts2.founder_id = #{id}\n" +
                    "\tand\n" +
                    " budgeting_time>=#{startTime}\n" +
                    "\tand\n" +
                    " (budgeting_time<=#{endTime} or  #{endTime} = '')\n" +
                    " and\n" +
                    " (district=#{district} or  #{district} = '')"
    )
    List<Budgeting> BudgetingList(CostVo2 costVo2);

    @Select(
            "  select\n" +
                    "\tmonth(s2.receipt_time) monthTime,\n" +
                    "\ts1.id,\n" +
                    "\ts1.district,\n" +
                    "\ts1.project_name projectName,\n" +
                    "\ts2.amount_cost,\n" +
                    "\ts2.receipt_time,\n" +
                    "  s2.budgeting_time\n" +
                    "\tfrom\n" +
                    "\tbase_project s1,\n" +
                    "\tbudgeting s2\n" +
                    "\twhere\n" +
                    "\ts1.id = s2.base_project_id\n" +
                    "\tand\n" +
                    "\ts1.founder_id =#{id}\n" +
                    "\tand\n" +
                    " budgeting_time>=#{startTime}\n" +
                    "\tand\n" +
                    " (budgeting_time<=#{endTime} or  #{endTime} = '')\n" +
                    " and\n" +
                    " (district=#{district} or  #{district} = '')"
    )
    List<Budgeting> trackList(CostVo2 costVo2);

    @Select(
            "SELECT \n" +
                    "amount_outsourcing\n" +
                    "FROM \n" +
                    "budgeting s1,\n" +
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
    List<Budgeting> totalexpenditure(CostVo2 costVo2);
}

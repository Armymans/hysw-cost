package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.IncomeInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface IncomeInfoMapper extends Mapper<IncomeInfo> {
    @Select(
           "SELECT\n" +
                   "s1.id,\n" +
                   "s1.base_project_id,\n" +
                   "s1.budget_money,\n" +
                   "s1.upsubmit_money,\n" +
                   "s1.downsubmit_money,\n" +
                   "s1.truck_money,\n" +
                   "s1.create_time,\n" +
                   "s1.update_time,\n" +
                   "s1.founder_id,\n" +
                   "s1.founder_company_id,\n" +
                   "s1.del_flag\n" +
                   "FROM\n" +
                   "income_info s1,\n" +
                   "base_project s2\n" +
                   "where\n" +
                   "s1.base_project_id = s2.id\n" +
                   "and\n" +
                   "(s2.district= #{district} or #{district} = '')\n" +
                   "and\n" +
                   "s1.create_time>=#{startTime}\n" +
                   "and\n" +
                   "(s1.create_time<=#{endTime} or #{endTime} = '')"
    )
    List<IncomeInfo> totalRevenue(CostVo2 costVo2);
}

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
           "SELECT " +
                   "s1.id, " +
                   "s1.base_project_id, " +
                   "s1.budget_money, " +
                   "s1.upsubmit_money, " +
                   "s1.downsubmit_money, " +
                   "s1.truck_money, " +
                   "s1.create_time, " +
                   "s1.update_time, " +
                   "s1.founder_id, " +
                   "s1.founder_company_id, " +
                   "s1.del_flag " +
                   "FROM " +
                   "income_info s1, " +
                   "base_project s2 " +
                   "where " +
                   "s1.base_project_id = s2.id " +
                   "and " +
                   "(s2.district= #{district} or #{district} = '') " +
                   "and " +
                   "s1.create_time>=#{startTime} " +
                   "and " +
                   "(s1.create_time<=#{endTime} or #{endTime} = '')"
    )
    List<IncomeInfo> totalRevenue(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(in_money,0)),0) " +
                    "FROM " +
                    "in_come s1, " +
                    "base_project s2 " +
                    "where " +
                    "s1.base_project_id = s2.id " +
                    "and " +
                    "(s2.district= #{district} or #{district} = '') " +
                    "and " +
                    "s1.create_time>=#{startTime} " +
                    "and " +
                    "(s1.create_time<=#{endTime} or #{endTime} = '')"
    )
    Double newTotalRevenue(CostVo2 costVo2);

    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(out_money,0)),0) " +
                    "FROM " +
                    "out_source s1, " +
                    "base_project s2 " +
                    "WHERE " +
                    "s1.base_project_id = s2.id " +
                    "AND " +
                    "(s2.district= #{district} or  #{district}  = '') " +
                    "AND " +
                    "s1.create_time>=#{startTime} " +
                    "AND " +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    Double newTotalexpenditure(CostVo2 costVo2);
}

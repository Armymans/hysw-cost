package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.AnhuiBudgetingCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

@org.apache.ibatis.annotations.Mapper
public interface AnhuiBudgetingChargeMapper extends Mapper<AnhuiBudgetingCharge> {
    @Select(
            "SELECT \n" +
                    "id,\n" +
                    "project_cost,\n" +
                    "rate_cost,\n" +
                    "rate_controller,\n" +
                    "status\n" +
                    "FROM \n" +
                    "anhui_budgeting_charge\n" +
                    "where\n" +
                    "project_cost >= #{cost}\n" +
                    "ORDER BY\n" +
                    "project_cost asc\n" +
                    "LIMIT 0,1"
    )
    AnhuiBudgetingCharge anhuiBudgetingMoney(@Param("cost") BigDecimal cost);
}

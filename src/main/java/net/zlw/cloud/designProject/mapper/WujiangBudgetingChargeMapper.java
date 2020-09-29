package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.WujiangBudgetingCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

@org.apache.ibatis.annotations.Mapper
public interface WujiangBudgetingChargeMapper extends Mapper<WujiangBudgetingCharge> {
    @Select(
            "SELECT \n" +
                    "id,\n" +
                    "project_cost,\n" +
                    "rate_cost,\n" +
                    "status\n" +
                    "FROM \n" +
                    "wujiang_budgeting_charge\n" +
                    "where\n" +
                    "project_cost >= #{cost}\n" +
                    "ORDER BY\n" +
                    "project_cost asc\n" +
                    "LIMIT 0,1"
    )
    WujiangBudgetingCharge wujiangBudgetingMoney(@Param("cost") BigDecimal cost);
}

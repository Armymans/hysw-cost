package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.WujiangLastSettlementReviewCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

@org.apache.ibatis.annotations.Mapper
public interface WujiangLastSettlementReviewChargeMapper extends Mapper<WujiangLastSettlementReviewCharge> {

    @Select(
            "SELECT \n" +
                    "id,\n" +
                    "project_cost,\n" +
                    "rate_cost,\n" +
                    "status\n" +
                    "FROM \n" +
                    "wujiang_last_settlement_review_charge\n" +
                    "where\n" +
                    "project_cost >= #{cost}\n" +
                    "ORDER BY\n" +
                    "project_cost asc\n" +
                    "LIMIT 0,1"
    )
    WujiangLastSettlementReviewCharge WujiangLastSettlementReviewChargeMoney(@Param("cost") BigDecimal cost);
}

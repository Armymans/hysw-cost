package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.WujiangTrackCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

@org.apache.ibatis.annotations.Mapper
public interface WujiangTrackChargeMapper extends Mapper<WujiangTrackCharge> {

    @Select(
            "SELECT \n" +
                    "id,\n" +
                    "project_cost,\n" +
                    "rate_cost,\n" +
                    "status\n" +
                    "FROM \n" +
                    "wujiang_track_charge\n" +
                    "where\n" +
                    "project_cost >= #{cost}\n" +
                    "ORDER BY\n" +
                    "project_cost asc\n" +
                    "LIMIT 0,1"
    )
    WujiangTrackCharge wujiangTrackChargeMoney(@Param("cost") BigDecimal cost);
}

package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.designProject.model.WujiangMoneyInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface WujiangMoneyInfoMapper extends Mapper<WujiangMoneyInfo> {
    @Select(
            "SELECT\n" +
                    "IFNULL(SUM(IFNULL(official_receipts,0)),0) wujiangMoney\n" +
                    "from\n" +
                    "wujiang_money_info s1,\n" +
                    "design_info s2,\n" +
                    "base_project s3\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "AND\n" +
                    "s3.id = s2.base_project_id\n" +
                    "AND\n" +
                    "(s3.district= #{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    Double totalRevenue(CostVo2 costVo2);
//    List<WujiangMoneyInfo> totalRevenue(CostVo2 costVo2);

}

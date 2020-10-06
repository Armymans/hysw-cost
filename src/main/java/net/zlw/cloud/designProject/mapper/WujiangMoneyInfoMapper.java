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
                    "official_receipts\n" +
                    "from\n" +
                    "wujiang_money_info s1,\n" +
                    "base_project s2,\n" +
                    "design_info s3\n" +
                    "where\n" +
                    "s1.base_project_id = s3.id\n" +
                    "and\n" +
                    "s2.id = s3.base_project_id\n" +
                    "and\n" +
                    "(s2.district=#{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<WujiangMoneyInfo> totalRevenue(CostVo2 costVo2);

}

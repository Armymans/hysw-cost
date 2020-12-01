package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.AnhuiMoneyinfo;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AnhuiMoneyinfoMapper extends Mapper<AnhuiMoneyinfo> {
    @Select(
            "SELECT " +
                    "IFNULL(SUM(IFNULL(official_receipts,0)),0) " +
                    "from " +
                    "anhui_money_info s1, " +
                    "base_project s2, " +
                    "design_info s3 " +
                    "where " +
                    "s1.base_project_id = s3.id " +
                    "and " +
                    "s2.id = s3.base_project_id " +
                    "and " +
                    "(s2.district=#{district} or  #{district}  = '') " +
                    "and " +
                    "s1.create_time>=#{startTime} " +
                    "and " +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    Double totalRevenue(CostVo2 costVo2);
//    List<AnhuiMoneyinfo> totalRevenue(CostVo2 costVo2);
}

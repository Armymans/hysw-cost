package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.AchievementsInfo;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AchievementsInfoMapper extends Mapper<AchievementsInfo> {
    @Select(
            "SELECT \n" +
                    "desgin_achievements,\n" +
                    "budget_achievements,\n" +
                    "upsubmit_achievements,\n" +
                    "downsubmit_achievements,\n" +
                    "truck_achievements\n" +
                    "FROM \n" +
                    "achievements_info s1,\n" +
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
    List<AchievementsInfo> totalexpenditure(CostVo2 costVo2);
}

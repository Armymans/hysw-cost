package net.zlw.cloud.followAuditing.mapper;

import net.zlw.cloud.followAuditing.model.TrackMonthly;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface TrackMonthlyDao extends Mapper<TrackMonthly> {
    @Select(
            "SELECT " +
                    "track_id " +
                    "FROM " +
                    "track_monthly " +
                    "WHERE " +
                    "`status` = '0' " +
                    "and " +
                    "track_id = #{id} " +
                    "ORDER BY " +
                    "audit_count DESC " +
                    "limit 1"
    )
    String selectByTrickId(@Param("id") String id);
}

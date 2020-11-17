package net.zlw.cloud.clearProject.mapper;

import net.zlw.cloud.clearProject.model.CallForBids;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CallForBidsMapper extends tk.mybatis.mapper.common.Mapper<CallForBids> {

    @Select("SELECT * FROM call_for_bids where status = 0 and bid_project_num = #{projectCode}")
    CallForBids selectByProjectNum(@Param("projectCode") String projectCode);
}
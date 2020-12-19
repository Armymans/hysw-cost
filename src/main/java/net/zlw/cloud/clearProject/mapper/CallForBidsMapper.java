package net.zlw.cloud.clearProject.mapper;

import net.zlw.cloud.clearProject.model.CallForBids;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CallForBidsMapper extends tk.mybatis.mapper.common.Mapper<CallForBids> {

    @Select("SELECT * FROM call_for_bids where status = 0 and bid_project_num = #{projectCode}")
    CallForBids selectByProjectNum(@Param("projectCode") String projectCode);

    @Update("UPDATE call_for_bids set clear_project_id = null ,bid_project_address  = null where clear_project_id = #{id}")
    void updateByClearProjectId(@Param("id") String id);

    @Select("SELECT * FROM `call_for_bids` WHERE clear_project_id = #{id}")
    CallForBids selectByCallForBids(@Param("id") String id);
}
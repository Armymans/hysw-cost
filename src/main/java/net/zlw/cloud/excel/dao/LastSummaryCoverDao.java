package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.LastSummaryCover;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface LastSummaryCoverDao extends Mapper<LastSummaryCover> {


    @Select("SELECT * FROM last_summary_cover WHERE del_flag = 0 AND last_settlement_id = #{id}")
    LastSummaryCover selectLastSummaryCoverById (@Param("id") String id);
}

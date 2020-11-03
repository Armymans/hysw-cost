package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.SummaryTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SummaryTableDao extends Mapper<SummaryTable> {


    @Select("SELECT * FROM summary_table WHERE  del_flag = 0 AND settlement_id = #{id}")
    List<SummaryTable> selectSummaryTableById(@Param("id") String id);
}

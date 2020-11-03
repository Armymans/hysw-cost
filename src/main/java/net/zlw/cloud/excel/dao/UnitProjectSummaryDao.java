package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.UnitProjectSummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface UnitProjectSummaryDao extends Mapper<UnitProjectSummary> {


    @Select("SELECT * FROM unit_project_summary WHERE del_flag = 0 AND last_settlement_id =#{id}")
    List<UnitProjectSummary> selectUnitProjectSummaryListById(@Param("id") String id);
}

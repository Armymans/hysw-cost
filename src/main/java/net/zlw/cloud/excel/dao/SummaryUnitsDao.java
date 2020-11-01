package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.SummaryUnits;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SummaryUnitsDao extends Mapper<SummaryUnits> {

    @Select("SELECT s1.*,s2.* FROM summary_units s1 LEFT JOIN budgeting s2 ON s1.budgeting_id = s2.id where s1.del_flag = 0 AND s1.budgeting_id = #{id}")
    List<SummaryUnits> findSumMaryUnitsList(@Param("id") String id);
}


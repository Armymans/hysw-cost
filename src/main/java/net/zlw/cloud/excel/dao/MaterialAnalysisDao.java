package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.MaterialAnalysis;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MaterialAnalysisDao extends Mapper<MaterialAnalysis> {


    @Select("SELECT * FROM material_analysis WHERE del_flag = 0 AND settlement_id = #{id}")
    List<MaterialAnalysis> selectMaterialAnalysisById(@Param("id") String id);

}

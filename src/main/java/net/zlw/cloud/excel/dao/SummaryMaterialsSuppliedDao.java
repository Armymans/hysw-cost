package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.SummaryMaterialsSupplied;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname SummaryMaterialsSuppliedDao
 * @Description TODO
 * @Date 2020/11/11 15:36
 * @Created by sjf
 */
@org.apache.ibatis.annotations.Mapper
public interface SummaryMaterialsSuppliedDao extends Mapper<SummaryMaterialsSupplied> {

    @Select("SELECT * FROM summary_materials_supplied WHERE a_b = '1' AND foreign_key = #{id}")
    List<SummaryMaterialsSupplied> findAList(@Param("id") String id);

    @Select("SELECT * FROM summary_materials_supplied WHERE a_b = '2' AND foreign_key = #{id}")
    List<SummaryMaterialsSupplied> findBList(@Param("id") String id);
}

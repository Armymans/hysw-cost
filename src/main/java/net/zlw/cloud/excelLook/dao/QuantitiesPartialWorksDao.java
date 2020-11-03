package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface QuantitiesPartialWorksDao extends Mapper<QuantitiesPartialWorks> {

    @Select("SELECT s1.*,s2.* FROM quantities_partial_works s1 LEFT JOIN budgeting s2 ON s1.budgeting_id = s2.id WHERE s1.budgeting_id = #{id}")
    List<QuantitiesPartialWorks> getList(@Param("id") String id);

    @Select("SELECT * FROM quantities_partial_works WHERE  budgeting_id = #{id}")
    List<QuantitiesPartialWorks> selectQuantitiespartialWorksById(@Param("id") String id);
}

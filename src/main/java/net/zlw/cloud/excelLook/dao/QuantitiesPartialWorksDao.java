package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface QuantitiesPartialWorksDao extends Mapper<QuantitiesPartialWorks> {

    @Select("SELECT * FROM quantities_partial_works  WHERE budgeting_id = #{id}")
    List<QuantitiesPartialWorks> getList(@Param("id") String id);

    @Select("SELECT * FROM quantities_partial_works WHERE  budgeting_id = #{id}")
    List<QuantitiesPartialWorks> selectQuantitiespartialWorksById(@Param("id") String id);

    @Select("SELECT * FROM quantities_partial_works WHERE `status` = '0' AND type= '2' AND budgeting_id = #{id}")
    List<QuantitiesPartialWorks> findWorksLists(String id);


}

package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.CompetitiveItemValuation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname CompetitiveItemValuationDao
 * @Description TODO
 * @Date 2020/11/11 14:11
 * @Created by sjf
 */

@org.apache.ibatis.annotations.Mapper
public interface CompetitiveItemValuationDao extends Mapper<CompetitiveItemValuation> {


    @Select("SELECT * FROM competitive_item_valuation WHERE foreign_key = #{id}")
    List<CompetitiveItemValuation> findList(@Param("id") String id);
}

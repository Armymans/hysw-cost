package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.TaxStatement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname TaxStatementDao
 * @Description TODO
 * @Date 2020/11/11 15:09
 * @Created by sjf
 */

@org.apache.ibatis.annotations.Mapper
public interface TaxStatementDao extends Mapper<TaxStatement> {

    @Select("SELECT * FROM tax_statement WHERE foreign_key = #{id}")
    List<TaxStatement> findTaxStatementList(@Param("id") String id);
}

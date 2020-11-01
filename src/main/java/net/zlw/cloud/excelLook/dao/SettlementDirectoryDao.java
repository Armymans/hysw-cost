package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.SettlementDirectory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementDirectoryDao extends Mapper<SettlementDirectory> {


    @Select("SELECT * FROM settlement_directory WHERE foreign_key = #{id}")
    List<SettlementDirectory> getList(@Param("id") String id);
}

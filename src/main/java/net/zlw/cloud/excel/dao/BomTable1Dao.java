package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.BomTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BomTable1Dao extends Mapper<BomTable> {

    @Select("SELECT * FROM bom_table where del_flag = 0 and bom_table_infomation_id = #{id}")
    List<BomTable> selectByBomTableInfomationId(String id);

    @Select("SELECT s1.*,s2.* FROM bom_table s1 LEFT JOIN budgeting s2 ON s1.bom_table_infomation_id = s2.id WHERE s1.del_flag= 0 AND s1.bom_table_infomation_id = #{id}")
    List<BomTable> list(@Param("id") String id);
}

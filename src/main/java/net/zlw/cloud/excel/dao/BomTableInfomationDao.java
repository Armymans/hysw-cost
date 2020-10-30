package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.BomTableInfomation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface BomTableInfomationDao extends Mapper<BomTableInfomation> {

    @Select("SELECT * FROM bom_table_infomation where del_flag = 0 and budget_id = #{id}")
    BomTableInfomation selectByBudeingId(@Param("id") String id);
}

package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.BomTableInfomation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BomTableInfomation1Dao extends Mapper<BomTableInfomation> {

    @Select("SELECT * FROM bom_table_infomation where del_flag = 0 and budget_id = #{id}")
    BomTableInfomation selectByBudeingId(@Param("id") String id);

    //查出所有的外键
    @Select("SELECT project_categories_coding FROM bom_table_infomation where del_flag = 0")
    List<BomTableInfomation> findId();
}

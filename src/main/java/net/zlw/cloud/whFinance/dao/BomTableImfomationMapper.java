package net.zlw.cloud.whFinance.dao;

import net.zlw.cloud.whFinance.domain.BomTableInfomation1;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BomTableImfomationMapper extends Mapper<BomTableInfomation1> {

    //查出所有的外键
    @Select("SELECT project_categories_coding FROM bom_table_infomation")
    List<BomTableInfomation1> findId();
}

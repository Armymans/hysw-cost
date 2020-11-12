package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.AnhuiSummarySheet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname AnhuiSummarySheetDao
 * @Description TODO
 * @Date 2020/11/11 10:53
 * @Created by sjf
 */
@org.apache.ibatis.annotations.Mapper
public interface AnhuiSummarySheetDao extends Mapper<AnhuiSummarySheet> {

    @Select("SELECT * FROM anhui_summary_sheet WHERE `status` = '0' AND type= '2' AND base_project_id = #{id}")
    List<AnhuiSummarySheet> findList(@Param("id") String id);
}

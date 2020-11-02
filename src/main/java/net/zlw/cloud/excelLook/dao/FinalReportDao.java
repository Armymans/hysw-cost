package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.demo.FinalReport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FinalReportDao extends Mapper<FinalReport> {


    @Select("SELECT * FROM final_report WHERE type = 0 AND del_flag = 0 AND project_id =#{id}")
    FinalReport getList(@Param("id") String id);

    @Select("SELECT * FROM final_report WHERE type = 1 AND del_flag = 0 AND project_id =#{id}")
    FinalReport getFinalReport(@Param("id") String id);

}

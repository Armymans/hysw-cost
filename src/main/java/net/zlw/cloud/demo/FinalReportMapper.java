package net.zlw.cloud.demo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface FinalReportMapper extends Mapper<FinalReport> {



    @Select("SELECT * FROM final_report where project_id =#{id}")
    FinalReport selectByProjectId(@Param("id") String id);
}

package net.zlw.cloud.clearProject.mapper;

import net.zlw.cloud.clearProject.model.ClearProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClearProjectMapper extends tk.mybatis.mapper.common.Mapper<ClearProject> {

    @Update(
            "update  clear_project set del_flag = '1' where id = #{id}"
    )
    void deleteClearProject(@Param("id") String id);
}
package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.VerificationSheetProject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface VerificationSheetProjectDao extends Mapper<VerificationSheetProject> {

    @Select("SELECT * FROM verification_sheet_project WHERE  del_flag = 0 AND verification_sheet_id = #{id}")
    List<VerificationSheetProject> getVerificationSheetProjectList(@Param("id") String id);
}

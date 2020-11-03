package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.VerificationSheet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
@org.apache.ibatis.annotations.Mapper
public interface VerificationSheetDao extends Mapper<VerificationSheet> {

    @Select("SELECT * FROM verification_sheet WHERE  del_flag = 0 AND settlement_id = #{id}")
    VerificationSheet selectVerificationSheetById(@Param("id") String id);
}

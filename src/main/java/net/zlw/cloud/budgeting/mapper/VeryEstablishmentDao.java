package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.VeryEstablishment;
import net.zlw.cloud.excel.model.VerificationSheet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface VeryEstablishmentDao extends Mapper<VeryEstablishment> {

    @Select("SELECT * FROM verification_sheet WHERE id = #{id}")
    VerificationSheet findIdByStatus(@Param("id") String id);

}

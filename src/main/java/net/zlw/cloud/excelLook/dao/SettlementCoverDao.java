package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.SettlementCover;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementCoverDao extends Mapper<SettlementCover> {



    @Select("SELECT * FROM settlement_cover WHERE settlement_id = #{id}")
    List<SettlementCover> settletmentCoverList(@Param("id")String id);
}

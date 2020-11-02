package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.SettlementReportText;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


@org.apache.ibatis.annotations.Mapper
public interface SettlementReportTextDao extends Mapper<SettlementReportText> {

    @Select("SELECT * FROM settlement_report_text WHERE type =0 AND del_flag = 0 AND foreign_key = #{id}")
    SettlementReportText getList(@Param("id") String id);


    @Select("SELECT * FROM settlement_report_text WHERE type =1 AND del_flag = 0 AND foreign_key = #{id}")
    SettlementReportText getSettlementReportText(@Param("id") String id);
}

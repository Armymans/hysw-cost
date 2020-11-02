package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.SettlementReportTextReductionReasons;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementReportTextReductionReasonsDao extends Mapper<SettlementReportTextReductionReasons> {



    @Select("SELECT * FROM settlement_report_text_reduction_reasons WHERE del_flag = 0 AND settlement_report_text_id =#{id}")
    List<SettlementReportTextReductionReasons> getReductionList(@Param("id") String id);

    @Select("select * from settlement_report_text_reduction_reasons where id = #{id}")
    SettlementReportTextReductionReasons selectByPrimaryKeyReasons(@Param("id") String id);
}

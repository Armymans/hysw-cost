package net.zlw.cloud.excelLook.dao;

import net.zlw.cloud.excelLook.domain.SettlementReportTextAttachment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementReportTextAttachmentDao extends Mapper<SettlementReportTextAttachment> {

    @Select("SELECT * FROM settlement_report_text_attachment WHERE del_flag = 0 AND settlement_report_text_id =#{id}")
    List<SettlementReportTextAttachment> getArrachmentList(@Param("id") String id);
}

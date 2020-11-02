package net.zlw.cloud.excelLook.dao;


import net.zlw.cloud.excelLook.domain.SettlementAuditReportTextXontent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementAuditReportTextXontentDao extends Mapper<SettlementAuditReportTextXontent> {

    @Select("SELECT * FROM settlement_audit_report_text_content WHERE del_flag = 0 AND settlement_report_text_id =#{id}")
    List<SettlementAuditReportTextXontent> getAuditList(@Param("id") String id);

    @Select("select * from settlement_audit_report_text_content where id = #{id}")
    SettlementAuditReportTextXontent selectByPrimaryKeyAudit(@Param("id") String id);
}

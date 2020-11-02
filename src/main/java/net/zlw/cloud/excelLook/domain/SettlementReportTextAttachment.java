package net.zlw.cloud.excelLook.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "settlement_report_text_attachment")
public class SettlementReportTextAttachment {
    @Id
    private String id;
    @Column(name = "name_attachment")
    private String nameAttachment;
    @Column(name = "settlement_report_text_id")
    private String settlementReportTextId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String delFlag;
}

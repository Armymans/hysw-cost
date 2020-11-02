package net.zlw.cloud.excelLook.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "settlement_report_text_reduction_reasons")
public class SettlementReportTextReductionReasons {

    @Column(name = "id")
    private String id;
    @Column(name = "reduction_reason")
    private String reductionReason;
    @Column(name = "settlement_report_text_id")
    private String settlementReportTextId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updatTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String delFlag;
}

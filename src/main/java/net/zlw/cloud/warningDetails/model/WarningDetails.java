package net.zlw.cloud.warningDetails.model;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by xulei on 2020/9/21.
 */
@Table(name = "warning_details")
@Data
public class WarningDetails {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "instructions")
    private String instructions;
    @Column(name = "status")
    private String status;
    @Column(name = "send_time")
    private String sendTime;
    @Column(name = "sender")
    private String sender;
    @Column(name = "title")
    private String title;
    @Column(name = "risk_type")
    private String riskType;
    @Column(name = "elaborate")
    private String elaborate;
    @Column(name = "risk_notification")
    private String riskNotification;
    @Column(name = "recipient_id")
    private String recipientId;
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
    @Column(name = "risk_time")
    private String riskTime;

    @Transient
    private AuditInfo auditInfo;

    private String checkAudit;

}

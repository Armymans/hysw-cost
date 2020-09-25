package net.zlw.cloud.warningDetails.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xulei on 2020/9/21.
 */
@Table(name = "audit_info")
@Data
public class AuditInfo {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "audit_result")
    private String auditResult;
    @Column(name = "audit_type")
    private String auditType;
    @Column(name = "auditor_id")
    private String auditorId;
    @Column(name = "audit_opinion")
    private String auditOpinion;
    @Column(name = "audit_time")
    private String auditTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "company_id")
    private String companyId;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;

}

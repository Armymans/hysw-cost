package net.zlw.cloud.progressPayment.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "audit_info")
@Data
public class AuditInfo implements Serializable {
    /*
    *   `id` varchar(60) NOT NULL COMMENT '唯一标识',
  `base_project_id` varchar(60) DEFAULT NULL COMMENT '外键',
  `audit_result` varchar(1) DEFAULT NULL COMMENT '审核结果 0未审批 1通过 2未通过',
  `audit_type` varchar(120) DEFAULT NULL COMMENT '审核类型',
  `auditor_id` varchar(60) DEFAULT NULL COMMENT '审核人id',
  `audit_opinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `audit_time` varchar(20) DEFAULT NULL COMMENT '审核时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人Id',
  `company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司',
  `status` varchar(1) DEFAULT NULL COMMENT '数据状态 0:正常 1:删除',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
    * */
    @Id
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
    @Column(name = "change_flag")
    private String changeFlag;
    @Column(name = "maintenance_flag")
    private String maintenanceFlag;
}

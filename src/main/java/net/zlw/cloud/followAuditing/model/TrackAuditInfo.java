package net.zlw.cloud.followAuditing.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "track_audit_info")
@Data
public class TrackAuditInfo implements Serializable {
    @Id
    private String id;
    @Column(name = "cea_total_money")
    private BigDecimal ceaTotalMoney;
    private String pm;
    @Column(name = "design_organization_id")
    private String designOrganizationId;
    @Column(name = "contract_amount")
    private BigDecimal contractAmount;
    private String outsource;
    @Column(name = "audit_unit_name_id")
    private String auditUnitNameId;
    private String contacts;
    private String phone;
    @Column(name = "outsource_money")
    private BigDecimal outsourceMoney;
    private String remark;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "company_Id")
    private String companyId;
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "base_project_id")
    private String baseProjectId;

}

package net.zlw.cloud.designProject.model;


import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name = "design_change_info")
@Data
public class DesignChangeInfo{

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "design_change_time")
    private String designChangeTime;

    @Column(name = "designer")
    private String designer;

    @Column(name = "design_change_cause")
    private String designChangeCause;

    @Column(name = "remark")
    private String remark;

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

    @Column(name = "design_info_id")
    private String designInfoId;

    private AuditInfo auditInfo;
    @Transient
    private String idNumber; //次序
}

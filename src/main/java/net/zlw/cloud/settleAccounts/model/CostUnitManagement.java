package net.zlw.cloud.settleAccounts.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@Table(name = "cost_unit_management")
public class CostUnitManagement  {
    @Id
    private String id;
    @Column(name = "cost_unit_name")
    private String costUnitName;
    private String status;
    private String contact;
    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(name = "company_address")
    private String companyAddress;
    private String remarkes;
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

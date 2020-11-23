package net.zlw.cloud.settleAccounts.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "settlement_info")
public class SettlementInfo {
    @Id
    private String id;
    @Column(name = "sumbit_name")
    private String sumbitName;
    private String remark;
    @Column(name = "sumbit_money")
    private String sumbitMoney;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "fouder_id")
    private String fouderId;
    @Column(name = "company_id")
    private String companyId;
    private String state;
    @Column(name = "up_and_down")
    private String upAndDown;

}

package net.zlw.cloud.VisaChange.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "visa_apply_change_information")
@Data
public class VisaApplyChangeInformation {
    @Id
    private String id;
    @Column(name = "applicant_name")
    private String applicantName;
    private String remark;
    @Column(name = "submit_money")
    private BigDecimal submitMoney;
    @Column(name = "up_and_down_mark")
    private String upAndDownMark;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "fouder_id")
    private String fouderId;
    @Column(name = "fouder_company")
    private String fouderCompany;
    private String state;
    @Column(name = "base_project_id")
    private String baseProjectId;
    private String status;
}

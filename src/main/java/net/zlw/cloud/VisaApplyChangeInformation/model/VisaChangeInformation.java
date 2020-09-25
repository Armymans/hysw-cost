package net.zlw.cloud.VisaApplyChangeInformation.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 上下家签证/变更申请表
 * Created by xulei on 2020/9/22.
 */
@Table(name = "visa_change_information")
@Data
public class VisaChangeInformation {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "applicant_name")
    private String applicantName;
    @Column(name = "remark")
    private String remark;
    @Column(name = "submit_money")
    private String submitMoney;
    @Column(name = "up_and_down_mark")
    private String upAndDownMark;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "fouder_name")
    private String fouderName;
    @Column(name = "fouder_company")
    private String fouderCompany;
    @Column(name = "state")
    private String state;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "status")
    private String status;

}

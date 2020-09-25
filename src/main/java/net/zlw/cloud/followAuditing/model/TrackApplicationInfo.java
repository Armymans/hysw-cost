package net.zlw.cloud.followAuditing.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "track_application_info")
@Data
public class TrackApplicationInfo implements Serializable {
    @Id
    private String id;
    @Column(name = "applicant_name")
    private String applicantName;
    private String remark;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "fouder_id")
    private String fouderId;
    @Column(name = "company_id")
    private String companyId;
    private String state;
    @Column(name = "track_audit")
    private String trackAudit;

}

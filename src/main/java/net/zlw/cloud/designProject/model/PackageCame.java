package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


//方案会审
@Table(name = "package_came")
@Data
public class PackageCame {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "participant")
    private String participant;

    @Column(name = "part_time")
    private String partTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "bass_project_id")
    private String bassProjectId;

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

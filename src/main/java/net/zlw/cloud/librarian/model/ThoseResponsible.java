package net.zlw.cloud.librarian.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "those_responsible")
@Data
public class ThoseResponsible {
    @Id
    private String id;
    @Column(name = "person_responsible")
    private String personResponsible;
    private String personnel;
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

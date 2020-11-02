package net.zlw.cloud.excelLook.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "settlement_directory")
public class SettlementDirectory {

    @Id
    private String id;
    @Column(name = "file_content")
    private String fileContent;
    @Column(name = "file_author")
    private String fileAuthor;
    @Column(name = "pagination")
    private String pagination;
    @Column(name = "remark")
    private String remark;
    @Column(name = "foreign_key")
    private String foreignKey;
    @Column(name = "type")
    private String type;
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

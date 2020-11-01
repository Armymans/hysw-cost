package net.zlw.cloud.excelLook.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
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
}

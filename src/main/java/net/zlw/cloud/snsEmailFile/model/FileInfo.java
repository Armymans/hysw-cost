package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author Armyman
 * @Description 文件信息表
 * @Date 2020/10/9 16:26
 **/
@Table(name = "file_info")
@Data
public class FileInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "plat_code")
    private String platCode;

    @Column(name = "name")
    private String name;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "type")
    private String type;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "status")
    private String status;

    @Column(name = "remark")
    private String remark;

    @Transient
    private String userName;
    @Transient
    private String imgurl;

}

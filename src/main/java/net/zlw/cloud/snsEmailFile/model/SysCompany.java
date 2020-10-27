package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description 公司实体
 * @Date 2020/10/27 17:54
 **/
@Data
@Table(name = "file_info")
public class SysCompany {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "identify_flag")
    private String identifyFlag;

    @Column(name = "logo")
    private String logo;

    @Column(name = "type")
    private String type;

    @Column(name = "group_flag")
    private String groupFlag;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "company_type")
    private String companyType;

    @Column(name = "platform_id")
    private String platformId;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "update_date")
    private String updateDate;

    @Column(name = "status")
    private String status;

    @Column(name = "auth_level")
    private String authLevel;

    @Column(name = "source")
    private String source;

    @Column(name = "purchase_center")
    private String purchaseCenter;

    @Column(name = "company_keyword")
    private String companyKeyword;

    @Column(name = "remark")
    private String remark;

    @Column(name = "old_name")
    private String oldName;
}

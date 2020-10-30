package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/29 16:29
 **/
@Table(name = "em_org")
@Data
public class EmOrg {

    @Id
    @Column(name= "id")
    private String id;

    @Column(name= "name")
    private String name;

    @Column(name= "remark")
    private String remark;

    @Column(name= "pid")
    private String pid;

    @Column(name= "company_id")
    private String companyId;

    @Column(name= "top_com")
    private String topCom;

    @Column(name= "create_date")
    private String createDate;

    @Column(name= "update_date")
    private String updateDate;

    @Column(name= "del_flag")
    private String delFlag;

    @Column(name= "org_type")
    private String orgType;

    @Column(name= "top_org")
    private String topOrg;

    @Column(name= "sort_num")
    private String sortNum;

}

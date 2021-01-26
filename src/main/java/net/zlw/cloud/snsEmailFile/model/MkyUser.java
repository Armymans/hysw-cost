package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/29 18:44
 **/
@Data
@Table(name = "mky_user")
public class MkyUser {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "passwd")
    private String passwd;

    @Column(name = "email")
    private String email;

    @Column(name = "salt")
    private String salt;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "com_id")
    private String comId;

    @Column(name = "dept_id")
    private String deptId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "top_org")
    private String topOrg;

    @Column(name = "top_com")
    private String topCom;

    @Column(name = "key_id")
    private String keyId;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "status")
    private String status;

    @Column(name = "create_datetime")
    private String createDatetime;

    @Column(name = "update_datetime")
    private String updateDatetime;

    @Column(name = "del_flag")
    private String delFlag;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "user_name")
    private String memberName;

}

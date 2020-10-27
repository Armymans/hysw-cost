package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/27 20:45
 **/
@Data
@Table(name = "information_log")
public class InformationLog {

    @Id
    @Column(name="id", nullable=true, unique=false, updatable=false)
    private String id;

    @Column(name="company_id", length=64, nullable=true)
    private String companyId;

    @Column(name="code", length=30, nullable=true)
    private String code;

    @Column(name="company_name", length=200, nullable=true)
    private String companyName;

    @Column(name="user_id", length=64, nullable=true)
    private String userId;

    @Column(name="user_name", length=200, nullable=true)
    private String userName;

    @Column(name="log_type", length=10, nullable=true)
    private String logType;

    @Column(name="login_time", length=1000, nullable=true)
    private String loginTime;

    @Column(name="type", nullable=true)
    private String type;

    @Column(name="login_ip_address", length=30, nullable=true)
    private String loginIpAddress;

    @Column(name="business_type", length=10, nullable=true)
    private String businessType;

    @Column(name="platform_id", length=64, nullable=true)
    private String platformId;

    @Column(name="create_time", length=20, nullable=true)
    private String createTime;

    @Column(name="update_time", length=20, nullable=true)
    private String updateTime;

    @Column(name="status", length=1, nullable=true)
    private String status;

}

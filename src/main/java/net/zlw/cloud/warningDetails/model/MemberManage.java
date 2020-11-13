package net.zlw.cloud.warningDetails.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Armyman
 * @Description // 用户表实体
 * @Date 14:02 2020/5/22
 * @Param
 * @return
 **/
@Data
@Table(name = "member_manage")
public class MemberManage {

    private static final long serialVersionUID = -1943961352036134112L;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "password")
    private String password;

    @Column(name = "member_sex")
    private String memberSex;

    @Column(name = "member_account")
    private String memberAccount;

    @Column(name = "member_role_id")
    private String memberRoleId;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "status")
    private String status;

    @Column(name = "dep_id")
    private String depId;

    @Column(name = "dep_admin")
    private String depAdmin;

    @Column(name = "work_type")
    private String workType;

    @Column(name = "account_auth")
    private BigDecimal accountAuth;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "update_date")
    private String updateDate;


    @Column(name = "salt")
    private String salt;

    @Transient
    private String roleName;

    @Transient
    private Set<AuthorityManagement> roles = new HashSet<>();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(String memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(String memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getDepAdmin() {
        return depAdmin;
    }

    public void setDepAdmin(String depAdmin) {
        this.depAdmin = depAdmin;
    }

    public BigDecimal getAccountAuth() {
        return accountAuth;
    }

    public void setAccountAuth(BigDecimal accountAuth) {
        this.accountAuth = accountAuth;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public MemberManage(String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }

    public Set<AuthorityManagement> getRoles() {
        return roles;
    }

    public void setRoles(Set<AuthorityManagement> roles) {
        this.roles = roles;
    }

    public MemberManage() {
    }
}
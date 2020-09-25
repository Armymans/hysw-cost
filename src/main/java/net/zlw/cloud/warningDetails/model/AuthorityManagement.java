package net.zlw.cloud.warningDetails.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "authority_management")
public class AuthorityManagement implements java.io.Serializable {

    private static final long serialVersionUID = -1943961352036134112L;

    @Id
	@Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name="auth_tree", length=65535, nullable=true)
    private String authTree;

    @Column(name="role_name")
    private String roleName;

    @Column(name="role_desc")
    private String roleDesc;

    @Column(name="status")
    private String status;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name="create_date")
    private String createDate;

    @Column(name="update_date")
    private String updateDate;

}

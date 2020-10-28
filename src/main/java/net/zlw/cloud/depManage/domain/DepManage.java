package net.zlw.cloud.depManage.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.zlw.cloud.warningDetails.model.MemberManage;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by xulei on 2020/9/18.
 */
@Table(name = "dep_manage")
@Data
public class DepManage {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "dep_name")
    private String depName;
    @Column(name = "dep_principal")
    private String depPrincipal;
    @Column(name = "dep_code")
    private String depCode;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "company_id")
    private String companyId;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;

    private List<MemberManage>  memberManages;


}

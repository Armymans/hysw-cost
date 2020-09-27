package net.zlw.cloud.maintenanceProjectInformation.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "construction_unit_management")
public class ConstructionUnitManagement implements Serializable {
    /**
     * 唯一标识
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 施工单位名称
     */
    @Column(name = "construction_unit_name")
    private String constructionUnitName;

    /**
     * 状态 1,启用 2,停用
     */
    @Column(name = "`status`")
    private String status;

    /**
     * 联系人
     */
    @Column(name = "contact")
    private String contact;

    /**
     * 联系人电话
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 单位地址
     */
    @Column(name = "company_address")
    private String companyAddress;

    /**
     * 备注
     */
    @Column(name = "remarkes")
    private String remarkes;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 创建人id
     */
    @Column(name = "founder_id")
    private String founderId;

    /**
     * 创建人公司id
     */
    @Column(name = "founder_company_id")
    private String founderCompanyId;

    /**
     * 状态 0,正常 1,删除
     */
    @Column(name = "del_flag")
    private String delFlag;

    private static final long serialVersionUID = 1L;
}
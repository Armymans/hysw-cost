package net.zlw.cloud.maintenanceProjectInformation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "maintenance_project_information")
public class MaintenanceProjectInformation implements Serializable {
    /**
     * 唯一标识
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 维修项目编号
     */
    @Column(name = "maintenance_item_id")
    private String maintenanceItemId;

    /**
     * 维修项目名称
     */
    @Column(name = "maintenance_item_name")
    private String maintenanceItemName;

    /**
     * 维修项目类型 0道路恢复工程 1表位改造 2故障换表 3水表周检换表 4DN300以上管道抢维修 5DN300以下管道抢维修 6设备维修购置 7房屋修缮 8绿化种植 9装饰及装修
     */
    @Column(name = "maintenance_item_type")
    private String maintenanceItemType;

    /**
     * 报送部门
     */
    @Column(name = "`submitted_ department`")
    private String submittedDepartment;

    /**
     * 报送时间
     */
    @Column(name = "submit_time")
    private String submitTime;

    /**
     * 编制人
     */
    @Column(name = "prepare_people")
    private String preparePeople;

    /**
     * 项目地址
     */
    @Column(name = "project_address")
    private String projectAddress;

    /**
     * 施工单位Id
     */
    @Column(name = "construction_unit_id")
    private String constructionUnitId;

    /**
     * 送审金额
     */
    @Column(name = "review_amount")
    private BigDecimal reviewAmount;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

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
     *  状态 0,正常 1,删除
     */
    @Column(name = "del_flag")
    private String delFlag;

    /**
     * 变更状态1待审核2处理中3未通过4待确认5已完成
     */
    @Column(name = "`type`")
    private String type;

//    @Transient
//    private ConstructionUnitManagement constructionUnitManagement;

    @Transient
    private String constructionUnitName;

    @Transient
    private String memberName;

    private static final long serialVersionUID = 1L;
}
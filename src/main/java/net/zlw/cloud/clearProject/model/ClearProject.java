package net.zlw.cloud.clearProject.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "clear_project")
public class ClearProject implements Serializable {
    /**
     * 唯一标识
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 项目编号
     */
    @Column(name = "project_num")
    private String projectNum;

    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 招标人
     */
    @Column(name = "tenderer")
    private String tenderer;

    /**
     * 招标代理机构
     */
    @Column(name = "procuratorial_agency")
    private String procuratorialAgency;

    /**
     * 投标人
     */
    @Column(name = "bidder")
    private String bidder;

    /**
     * 投标总价
     */
    @Column(name = "bid_price")
    private BigDecimal bidPrice;

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

    @Column(name = "budgeting_id")
    private String budgetingId;

    @Column(name = "construction_unit_id")
    private String constructionUnitId;

    @Transient
    private String constructionUnitName;

    @Column(name = "project_address")
    private String projectAddress;

    @Transient
    private String founderName;
    @Transient
    private String founderTime;

    private static final long serialVersionUID = 1L;
}
package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;

@Table(name = "wujiang_money_info")
@Data
public class WujiangMoneyInfo{
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "pay_term")
    private String payTerm;

    @Column(name = "collection_money")
    private String collectionMoney; //代收金额字符串

    @Column(name = "collection_money_time")
    private String collectionMoneyTime; //代收时间字符串

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "cost")
    private BigDecimal cost;  //造价费用

    @Column(name = "design_rate")
    private BigDecimal designRate; //设计费率

    @Column(name = "preferential_policy")
    private BigDecimal preferentialPolicy; //优惠政策

    @Column(name = "revenue")
    private BigDecimal revenue; //应收收金额',

    @Column(name = "official_receipts")
    private BigDecimal officialReceipts; //'实收金额',

    @Column(name = "cost_type")
    private String costType;  //费用类型

    @Column(name = "one_desmoney")
    private BigDecimal oneDesmoney; //党项设计费

    @Column(name = "collection_time")
    private String collectionTime;  //收款日期

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

    @Transient
    private ArrayList<PayItem> strings = new ArrayList<>();

    @Transient
    private String selectFlag; //列表是否展示
}

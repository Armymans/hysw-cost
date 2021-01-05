package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;


@Table(name = "anhui_money_info")
@Data
public class AnhuiMoneyinfo{

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识'

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "collection_money")
    private String collectionMoney; //代收金额/次数

    @Column(name = "collection_money_time")
    private String collectionMoneyTime; //代收时间字符串

    @Column(name = "total_money")
    private BigDecimal totalMoney; //总计付款

    @Column(name = "pump_room_cost")
    private BigDecimal pumpRoomCost;  //泵房费用

    @Column(name = "pay_term")
    private String payTerm;  //收费方式

    @Column(name = "bim")
    private BigDecimal bim; //bim系数

    @Column(name = "pipeline_cost")
    private BigDecimal pipelineCost; //管道费用

    @Column(name = "professional_adjustment_factor")
    private BigDecimal professionalAdjustmentFactor;  //专业调整系数

    @Column(name = "complex_adjustment_factor")
    private BigDecimal complexAdjustmentFactor; //复杂调整系数

    @Column(name = "preferential_policy")
    private BigDecimal preferentialPolicy; //优惠政策

    @Column(name = "revenue")
    private BigDecimal revenue; //应收金额

    @Column(name = "official_receipts")
    private BigDecimal officialReceipts; //实收金额

    @Column(name = "collection_time")
    private String collectionTime; //收款日期

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

    @Column(name = "contract_amount")
    private String contractAmount;

    @Transient
    private ArrayList<PayItem> strings = new ArrayList<>();

    @Transient
    private String selectFlag; //列表是否展示

    //判断其他信息是否显示
    @Transient
    private String unShow;
}

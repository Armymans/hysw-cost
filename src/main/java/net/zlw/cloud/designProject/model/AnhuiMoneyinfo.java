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
    private String collectionMoney;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "pump_room_cost")
    private BigDecimal pumpRoomCost;

    @Column(name = "pay_term")
    private String payTerm;

    @Column(name = "bim")
    private BigDecimal bim;

    @Column(name = "pipeline_cost")
    private BigDecimal pipelineCost;

    @Column(name = "professional_adjustment_factor")
    private BigDecimal professionalAdjustmentFactor;

    @Column(name = "complex_adjustment_factor")
    private BigDecimal complexAdjustmentFactor;

    @Column(name = "preferential_policy")
    private BigDecimal preferentialPolicy;

    @Column(name = "revenue")
    private BigDecimal revenue;

    @Column(name = "official_receipts")
    private BigDecimal officialReceipts;

    @Column(name = "collection_time")
    private String collectionTime;

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

}

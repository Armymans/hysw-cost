package net.zlw.cloud.excelLook.domain;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
public class QuantitiesPartialWorks {

   /*   `id` varchar(60) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL COMMENT '工程名称',
            `project_code` varchar(120) DEFAULT NULL COMMENT '项目编码',
            `project_name` varchar(120) DEFAULT NULL COMMENT '项目名称',
            `project_description` varchar(120) DEFAULT NULL COMMENT '项目特征描述',
            `measuring_unit` varchar(120) DEFAULT NULL COMMENT '计量单位',
            `quantities` varchar(120) DEFAULT NULL COMMENT '工程量',
            `comprehensive_unit_price` decimal(20,2) NOT NULL COMMENT '综合单价',
            `and_price` decimal(20,2) NOT NULL COMMENT '合价',
            `rate_artificial_cost` decimal(20,2) DEFAULT NULL COMMENT '定额人工费',
            `fixed_mechanical_fee` decimal(20,2) DEFAULT NULL COMMENT '定额机械费',
            `temporary_valuation` decimal(20,2) DEFAULT NULL COMMENT '暂估价',
            `budgeting_id` varchar(20) DEFAULT NULL COMMENT '预算外键',
            `status` varchar(1) DEFAULT NULL COMMENT '0正常1删除',
            `type` varchar(1) DEFAULT NULL COMMENT '1神机2新点',*/
    @Id
    private String id;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "project_description")
    private String projectDescription;
    @Column(name = "measuring_unit")
    private String measuringUnit;
    @Column(name = "quantities")
    private String quantities;
    @Column(name = "comprehensive_unit_price")
    private BigDecimal comprehensiveUnitPrice;
    @Column(name = "and_price")
    private BigDecimal andPrice;
    @Column(name = "rate_artificial_cost")
    private BigDecimal rateArtificialCost;
    @Column(name = "fixed_mechanical_fee")
    private BigDecimal fixedMechanicalFee;
    @Column(name = "temporary_valuation")
    private BigDecimal temporaryValuation;
    @Column(name = "budgeting_id")
    private String budgetingId;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "status")
    private String status;
    @Column(name = "type")
    private String type;
}

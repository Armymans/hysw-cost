package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "material_analysis")
@Data
public class MaterialAnalysis {
    /*
    *`id` varchar(60) NOT NULL,
  `project_name` varchar(60) DEFAULT NULL COMMENT '工程名称',
  `serial_number` varchar(60) DEFAULT NULL COMMENT '序号',
  `material_name` varchar(60) DEFAULT NULL COMMENT '材料名称',
  `specifications` varchar(60) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(60) DEFAULT NULL COMMENT '单位',
  `outbound_order_quantity` varchar(60) DEFAULT NULL COMMENT '出库单量',
  `contract_amount` decimal(20,2) DEFAULT NULL COMMENT '合同量',
  `change_visa` decimal(20,2) DEFAULT NULL COMMENT '变更签证量',
  `completion_figure_amount` decimal(20,2) DEFAULT NULL COMMENT '竣工图量',
  `contract_price` decimal(20,2) DEFAULT NULL COMMENT '合同单价',
  `outbound_differences` decimal(20,2) DEFAULT NULL COMMENT '出库量与竣工量差异',
  `turn_for_quantity` decimal(20,2) DEFAULT NULL COMMENT '预计甲供转乙供量',
  `turn_for_increase_costs` decimal(20,2) DEFAULT NULL COMMENT '预计甲供转乙供增加施工费',
  `outbound_price` decimal(20,2) DEFAULT NULL COMMENT '出库单价',
  `outbound_us_price` decimal(20,2) DEFAULT NULL COMMENT '出库合价',
  `more_number` decimal(20,2) DEFAULT NULL COMMENT '超领数量',
  `more_amount` decimal(20,2) DEFAULT NULL COMMENT '超领金额',
  `settlement_id` varchar(60) DEFAULT NULL COMMENT '下家外键',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',

    * */
    private String id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "material_name")
    private String materialName;
    private String specifications;
    private String unit;
    @Column(name = "outbound_order_quantity")
    private String outboundOrderQuantity;
    @Column(name = "contract_amount")
    private BigDecimal contractAmount;
    @Column(name = "change_visa")
    private BigDecimal changeVisa;
    @Column(name = "completion_figure_amount")
    private BigDecimal completionFigureAmount;
    @Column(name = "contract_price")
    private BigDecimal contractPrice;
    @Column(name = "outbound_differences")
    private BigDecimal outboundDifferences;
    @Column(name = "turn_for_quantity")
    private BigDecimal turnForQuantity;
    @Column(name = "turn_for_increase_costs")
    private BigDecimal turnForIncreaseCosts;
    @Column(name = "outbound_price")
    private BigDecimal outboundPrice;
    @Column(name = "outbound_us_price")
    private BigDecimal outboundUsPrice;
    @Column(name = "more_number")
    private BigDecimal moreNumber;
    @Column(name = "more_amount")
    private BigDecimal moreAmount;
    @Column(name = "settlement_id")
    private String settlementId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String delFlag;

}

package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "part_table_quantities")
/*
*  `id` varchar(60) NOT NULL,
  `serial_number` varchar(60) DEFAULT NULL COMMENT '序号',
  `project_code` varchar(60) DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(60) DEFAULT NULL COMMENT '项目名称',
  `measurement` varchar(60) DEFAULT NULL COMMENT '计量',
  `engineering` varchar(60) DEFAULT NULL COMMENT '工程',
  `comprehensive_price` decimal(20,2) DEFAULT NULL COMMENT '综合单价',
  `combined_price` decimal(20,2) DEFAULT NULL COMMENT '合价',
  `artificial_cost` decimal(20,2) DEFAULT NULL COMMENT '其中人工费',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',

* */
public class PartTableQuantities {
    @Id
    private String id;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_name")
    private String projectName;
    private String measurement;
    private String engineering;
    @Column(name = "comprehensive_price")
    private BigDecimal comprehensivePrice;
    @Column(name = "combined_price")
    private BigDecimal combinedPrice;
    @Column(name = "artificial_cost")
    private BigDecimal artificialCost;
    @Column(name = "foreign_key")
    private String foreignKey;
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
    @Column(name = "signalment")
    private String signalment;



}

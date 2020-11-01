package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "summary_table")
@Data
public class SummaryTable {
    /*
    * `id` varchar(60) NOT NULL,
  `project_name` varchar(60) DEFAULT NULL COMMENT '项目名称',
  `review_amount` decimal(20,2) DEFAULT NULL COMMENT '送审金额',
  `authorized_amount` decimal(20,2) DEFAULT NULL COMMENT '审定金额',
  `nuclear_increasing_or_decreasing` decimal(20,2) DEFAULT NULL COMMENT '核增(减)金额',
  `remark` varchar(120) DEFAULT NULL COMMENT '备注',
  `settlement_id` varchar(60) DEFAULT NULL COMMENT '下家外键',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',

    * */
    @Id
    private String id;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "engineering_name")
    private String engineeringName;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "review_amount")
    private BigDecimal reviewAmount;
    @Column(name = "authorized_amount")
    private BigDecimal authorizedAmount;
    @Column(name = "nuclear_increasing_or_decreasing")
    private BigDecimal nuclearIncreasingOrDecreasing;
    private String remark;
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

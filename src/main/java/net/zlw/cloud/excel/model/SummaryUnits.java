package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "summary_units")
@Data
public class SummaryUnits {
    /*
    * `id` varchar(60) NOT NULL COMMENT '序号',
  `project_name` varchar(60) DEFAULT NULL COMMENT '工程名称',
  `aggregate_content` varchar(120) DEFAULT NULL COMMENT '汇总内容',
  `amount` decimal(60,2) DEFAULT NULL COMMENT '金额',
  `valuation` decimal(60,2) DEFAULT NULL COMMENT '其中:材料,设备暂估价',
  `create_time` varchar(60) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(60) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '状态0正常1删除'
    * */
    @Id
    private String id;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "aggregate_content")
    private String aggregateContent;
    private BigDecimal amount;
    private BigDecimal valuation;
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

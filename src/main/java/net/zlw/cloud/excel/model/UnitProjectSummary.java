package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "unit_project_summary")
@Data
public class UnitProjectSummary {
    /*
    *  `id` varchar(60) NOT NULL,
  `serial_number` varchar(60) DEFAULT NULL COMMENT '序号',
  `project_name` varchar(60) DEFAULT NULL COMMENT '项目名称',
  `amount` decimal(20,2) DEFAULT NULL COMMENT '金额',
  `budget_id` varchar(60) DEFAULT NULL COMMENT '预算外键',
  `last_settlement_id` varchar(60) DEFAULT NULL COMMENT '上家表id',
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
    private BigDecimal amount;
    @Column(name = "budget_id")
    private String budgetId;
    @Column(name = "last_settlement_id")
    private String lastSettlementId;
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

package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "verification_sheet_project")
@Data
public class VerificationSheetProject {
    /*
    * `id` varchar(60) NOT NULL,
  `project_unit` varchar(60) DEFAULT NULL COMMENT '单位工程名称',
  `review_number` decimal(20,0) DEFAULT NULL COMMENT '送审数',
  `authorized_number` decimal(20,0) DEFAULT NULL COMMENT '审定数',
  `subtract_number` decimal(20,0) DEFAULT NULL COMMENT '核减数',
  `nuclear_number` decimal(20,0) DEFAULT NULL COMMENT '核增数',
  `increase_reduction` decimal(20,0) DEFAULT NULL COMMENT '净增减额',
  `verification_sheet_id` varchar(60) DEFAULT NULL COMMENT '核定单id',
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
    @Column(name = "project_unit")
    private String projectUnit;
    @Column(name = "review_number")
    private BigDecimal reviewNumber;
    @Column(name = "authorized_number")
    private BigDecimal authorizedNumber;
    @Column(name = "subtract_number")
    private BigDecimal subtractNumber;
    @Column(name = "nuclear_number")
    private BigDecimal nuclearNumber;
    @Column(name = "increase_reduction")
    private BigDecimal increaseReduction;
    @Column(name = "verification_sheet_id")
    private String verificationSheetId;
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

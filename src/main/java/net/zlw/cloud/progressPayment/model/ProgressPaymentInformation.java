package net.zlw.cloud.progressPayment.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "progress_payment_information")
@Data
public class ProgressPaymentInformation implements Serializable {
    /*
 `id` varchar(60) NOT NULL COMMENT '唯一标识',
  `current_payment_Information` decimal(60,0) DEFAULT NULL COMMENT '本期支付金额',
  `cumulative_payment_times` varchar(60) DEFAULT NULL COMMENT '累计支付次数',
  `current_payment_ratio` varchar(60) DEFAULT NULL COMMENT '本期支付比例',
  `current_period_according` varchar(60) DEFAULT NULL COMMENT '当前期数',
  `contract_amount` decimal(60,0) DEFAULT NULL COMMENT '合同金额',
  `project_type` varchar(1) DEFAULT NULL COMMENT '1,合同内进度款支付 2,合同外进度款支付',
  `receiving_time` varchar(120) DEFAULT NULL COMMENT '接收时间',
  `compile_time` varchar(120) DEFAULT NULL COMMENT '编制时间',
  `outsourcing` varchar(1) DEFAULT NULL COMMENT '是否委外 1是 2否',
  `name_of_cost_unit` varchar(1) DEFAULT NULL COMMENT '造价单位名称 1,XXX有限公司 2,XXX有限公司',
  `contact` varchar(120) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(120) DEFAULT NULL COMMENT '联系电话',
  `amount_outsourcing` decimal(60,0) DEFAULT NULL COMMENT '委外金额',
  `situation` varchar(120) DEFAULT NULL COMMENT '说明情况',
  `remarkes` varchar(120) DEFAULT NULL COMMENT '备注',
  `base_project_id` varchar(60) DEFAULT NULL COMMENT '项目基本信息外键id',
  `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '状态 0,正常 1,删除',
     */
    @Id
    private String id;
    @Column(name = "current_payment_Information")
    private BigDecimal currentPaymentInformation;
    @Column(name = "cumulative_payment_times")
    private String cumulativePaymentTimes;
    @Column(name = "current_payment_ratio")
    private String currentPaymentRatio;
    @Column(name = "current_period_according")
    private String currentPeriodAccording;
    @Column(name = "contract_amount")
    private BigDecimal contractAmount;
    @Column(name = "project_type")
    private String projectType;
    @Column(name = "receiving_time")
    private String receivingTime;
    @Column(name = "compile_time")
    private String compileTime;
    private String outsourcing;
    @Column(name = "name_of_cost_unit")
    private String nameOfCostUnit;
    private String contact;
    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(name = "amount_outsourcing")
    private BigDecimal amountOutsourcing;
    private String situation;
    private String remarkes;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "progress_payment_id")
    private String progressPaymentId;
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
    @Column(name = "change_num")
    private Integer changeNum;
    @Transient
    private String typeS;
}

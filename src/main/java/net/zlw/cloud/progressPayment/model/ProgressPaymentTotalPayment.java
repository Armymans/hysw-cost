package net.zlw.cloud.progressPayment.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "progress_payment_total_payment")
@Data
public class ProgressPaymentTotalPayment implements Serializable {
    /*
    `total_payment_amount` decimal(60,0) DEFAULT NULL COMMENT '累计支付金额',
  `cumulative_number_payment` decimal(60,0) DEFAULT NULL COMMENT '累计支付次数',
  `accumulative_payment_proportion` varchar(60) DEFAULT NULL COMMENT '累计支付比例',
  `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '删除表示 0正常 1删除'
     */
    @Id
    private String id;
    @Column(name = "total_payment_amount")
    private BigDecimal totalPaymentAmount;
    @Column(name = "cumulative_number_payment")
    private BigDecimal cumulativeNumberPayment;
    @Column(name = "accumulative_payment_proportion")
    private String accumulativePaymentProportion;
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

}

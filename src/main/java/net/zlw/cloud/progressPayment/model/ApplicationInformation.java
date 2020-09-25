package net.zlw.cloud.progressPayment.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "application_information")
@Data
public class ApplicationInformation implements Serializable {
    /*
    `id` varchar(60) NOT NULL COMMENT '唯一标识',
  `remarkes` varchar(120) DEFAULT NULL COMMENT '备注',
  `base_project_id` varchar(60) DEFAULT NULL COMMENT '工程项目外键id\r\n',
  `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '状态 0,正常 1,删除'
     */
    @Id
    private String id;
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
}

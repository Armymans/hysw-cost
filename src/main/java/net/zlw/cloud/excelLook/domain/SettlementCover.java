package net.zlw.cloud.excelLook.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "settlement_cover")
public class SettlementCover {

//     `id` varchar(60) NOT NULL,
//  `project_name` varchar(60) DEFAULT NULL COMMENT '工程名称',
//            `control_price_lowercase` decimal(20,2) DEFAULT NULL COMMENT '控制价小写',
//            `control_price_capital` varchar(60) DEFAULT NULL COMMENT '控制价大写',
//            `engineering_cost_consultation` varchar(60) DEFAULT NULL COMMENT '工程造价咨询单位',
//            `legal_representative_authorized` varchar(60) DEFAULT NULL COMMENT '法定代表或其授权人',
//            `auditor` varchar(60) DEFAULT NULL COMMENT '审核人',
//            `prepare_people` varchar(60) DEFAULT NULL COMMENT '编制人',
//            `compile_time` varchar(60) DEFAULT NULL COMMENT '编制时间',
//            `design_amount` decimal(20,2) DEFAULT NULL COMMENT '设计费',
//            `construction_consulting` decimal(20,2) DEFAULT NULL COMMENT '造价咨询费',
//            `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
//            `update_time` varchar(255) DEFAULT NULL COMMENT '修改时间',
//            `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
//            `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
//            `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',
//            `budegting_id` varchar(255) DEFAULT NULL COMMENT '预算Id',
    @Id
    private String id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "control_price_lowercase")
    private String controlPriceLowercase;
    @Column(name = "control_price_capital")
    private String controlPriceCapital;
    @Column(name = "engineering_cost_consultation")
    private String engineeringCostConsultation;
    @Column(name = "legal_representative_authorized")
    private String legalRepresentativeAuthorized;
    @Column(name = "auditor")
    private String auditor;
    @Column(name = "prepare_people")
    private String preparePeople;
    @Column(name = "compile_time")
    private String compileTime;
    @Column(name = "design_amount")
    private String designAmount;
    @Column(name = "construction_consulting")
    private String constructionConsulting;
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
    @Column(name = "settlement_id")
    private String settlementId;


}

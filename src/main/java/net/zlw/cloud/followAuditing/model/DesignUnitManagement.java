package net.zlw.cloud.followAuditing.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Classname DesignUnitManagement
 * @Description TODO
 * @Date 2020/12/17 14:32
 * @Created by sjf
 */
@Data
@Table(name = "design_unit_management")
public class DesignUnitManagement {

        /*    `id` varchar(60) NOT NULL COMMENT '主键',
            `design_unit_name` varchar(120) DEFAULT NULL COMMENT '设计单位名称',
            `status` varchar(1) DEFAULT NULL COMMENT '状态 1,启用 2,停用',
            `contact` varchar(60) DEFAULT NULL COMMENT '联系人',
            `concact_phone` varchar(120) DEFAULT NULL COMMENT '联系人电话',
            `company_address` varchar(120) DEFAULT NULL COMMENT '单位地址',
            `remarkes` varchar(120) DEFAULT NULL COMMENT '备注',
            `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
            `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
            `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
            `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
            `del_flag` varchar(1) DEFAULT NULL COMMENT '状态 0,正常 1,删除',*/
    @Id
    private String id;
    @Column(name = "design_unit_name")
    private String designUnitName;
    @Column(name = "status")
    private String status;
    @Column(name = "contact")
    private String contact;
    @Column(name = "concact_phone")
    private String concactPhone;
    @Column(name = "company_address")
    private String companyAddress;
    @Column(name = "remarkes")
    private String remarkes;
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

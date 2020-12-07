package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Classname InCome
 * @Description TODO
 * @Date 2020/12/7 14:55
 * @Created by sjf
 */
@Data
@Table(name = "in_come")
public class InCome {
           /* `id` int(11)                       '唯一标识',
            `in_money` varchar(60)          '收入金额',
            `income_type` varchar(1)        '类型1.设计费2.预算编制咨询费3.控价编制咨询费4.上家结算编制咨询费5.下家结算编制咨询费',
            `district` varchar(1)           '地区 1芜湖2马鞍山3江北4吴江',
            `dept` varchar(1)               '部门 1.设计 2.造价',
            `del_flag` varchar(1)           '是否删除 0正常 1删除',
            `base_project_id` varchar(60)   'base_project外键',
            `project_num` varchar(60)       '任务编号',
            `create_time` varchar(60)       '创建时间',
            `update_time` varchar(60)       '修改时间',
            `founder_id` varchar(60)        '创建人',
            `founder_company_id` varchar(60)'创建人公司',*/

    @Id
    private String id;
    @Column(name = "in_money")
    private String inMoney;
    @Column(name = "income_type")
    private String incomeType;
    @Column(name = "district")
    private String district;
    @Column(name = "dept")
    private String dept;
    @Column(name = "del_flag")
    private String delFlag;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "project_num")
    private String projectNum;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
}

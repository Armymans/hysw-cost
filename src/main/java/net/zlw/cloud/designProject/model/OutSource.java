package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname 委外金额
 * @Description TODO
 * @Date 2020/11/28 17:07
 * @Created by sjf
 */
@Data
@Table(name = "out_source")
public class OutSource {
/*
                `id` varchar(60) '唯一标识',
            `out_money` varchar(60) '委外金额',
            `district` varchar(1)  '地区 1芜湖2马鞍山3江北4吴江',
            `dept` varchar(1) '部门 1.设计 2.造价',
            `del_flag` varchar(1)'是否删除 0正常 1删除',
            `out_type` varchar(1) '类型1.设计委外金额2.预算委外金额3.进度款支付委外金额4.签证/变更委外金额5.跟踪审计委外金额6.结算委外金额7检维修委外金额',
            `base_project_id` varchar(60) 'base_project外键',
            `project_num` varchar(60) '任务编号',
            `create_time` varchar(60) '创建时间',
            `update_time` varchar(60)'修改时间',
                `founder_id` varchar(60)'创建人',
            `founder_company_id` varchar(60) '创建人公司',*/
    @Column(name = "id")
    private String id;
    @Column(name = "out_money")
    private String outMoney;
    @Column(name = "district")
    private String district;
    @Column(name = "dept")
    private String dept;
    @Column(name = "del_flag")
    private String delFlag;
    @Column(name = "out_type")
    private String outType;
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

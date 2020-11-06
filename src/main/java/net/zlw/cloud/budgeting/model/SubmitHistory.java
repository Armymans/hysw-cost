package net.zlw.cloud.budgeting.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname SubmitHistory
 * @Description 提交历史接口
 * @Date 2020/11/6 21:36
 * @Created by sjf
 */
@Data
@Table(name = "submit_history")
public class SubmitHistory {
//      `id` varchar(60) NOT NULL COMMENT '唯一标识',
//            `operation_department` varchar(60) DEFAULT NULL COMMENT '操作部门',
//            `submitter` varchar(60) DEFAULT NULL COMMENT '提交人',
//            `submission_time` varchar(120) DEFAULT NULL COMMENT '提交时间',
//            `audit_opinion` varchar(30) DEFAULT NULL COMMENT '审核意见',
//            `audit_department` varchar(60) DEFAULT NULL COMMENT '审核部门',
//            `audit_time` varchar(120) DEFAULT NULL COMMENT '审核时间',
//            `request_note_id` varchar(60) DEFAULT NULL COMMENT '申请单信息外键id',
//            `create_time` varchar(120) DEFAULT NULL COMMENT '创建时间',
//            `update_time` varchar(120) DEFAULT NULL COMMENT '修改时间',
//            `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
//            `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
//            `del_flag` varchar(1) DEFAULT NULL COMMENT '状态 0,正常 1,删除',
    @Column(name = "id")
    private String id;
    @Column(name = "operationDepartment")
    private String operationDepartment;
    @Column(name = "submitter")
    private String submitter;
    @Column(name = "submission_time")
    private String submissionTime;
    @Column(name = "audit_opinion")
    private String auditOpinion;
    @Column(name = "audit_department")
    private String auditDepartment;
    @Column(name = "audit_time")
    private String auditTime;
    @Column(name = "request_note_id")
    private String requestNoteId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String del_flag;
}

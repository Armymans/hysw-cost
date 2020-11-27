package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Classname 绩效统计表
 * @Description TODO
 * @Date 2020/11/27 14:18
 * @Created by sjf
 */

@Data
@Table(name = "employee_achievements_info")
public class EmployeeAchievementsInfo {

    /*  `id` varchar(60) NOT NULL COMMENT '唯一标识',
      `member_id` varchar(60) DEFAULT NULL COMMENT '成员外键',
      `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
      `update_time` varchar(20) DEFAULT NULL COMMENT '修改时间',
      `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人',
      `founder_company_id` varchar(60) DEFAULT NULL COMMENT '创建人公司',
      `del_flag` varchar(1) DEFAULT NULL COMMENT '是否删除 0正常 1删除',
      `district` varchar(1) DEFAULT NULL COMMENT '地区 1芜湖2马鞍山3江北4吴江',
      `dept` varchar(1) DEFAULT NULL COMMENT '部门 1.设计 2.造价',
      `achievements_type` varchar(1) DEFAULT NULL COMMENT '1设计绩效 2.预算编制绩效 3.上家送审绩效 4.下家送审绩效 5.跟踪审计绩效',
      `accrued_amount` decimal(60,2) DEFAULT NULL COMMENT '应计提金额',
      `actual_amount` decimal(60,2) DEFAULT NULL COMMENT '实际计提金额',
      `balance` decimal(60,2) DEFAULT NULL COMMENT '余额',
      `base_project_id` varchar(60) DEFAULT NULL COMMENT 'base_project外键',
      `project_num` varchar(60) DEFAULT NULL COMMENT '任务编号',
      `over_flag` varchar(1) DEFAULT NULL COMMENT '绩效是否发送完结0否1是',
*/
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "member_id")
    private String memberId;

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

    @Column(name = "district")
    private String district;

    @Column(name = "dept")
    private String dept;

    @Column(name = "achievements_type")
    private String achievementsType;

    @Column(name = "accrued_amount")
    private BigDecimal accruedAmount;

    @Column(name = "actual_amount")
    private BigDecimal actualAmount;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "project_num")
    private String projectNum;

    @Column(name = "over_flag")
    private String overFlag;

}

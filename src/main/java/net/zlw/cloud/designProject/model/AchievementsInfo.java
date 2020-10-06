package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "achievements_info")
@Data
public class AchievementsInfo {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id; //唯一标识

    @Column(name = "member_id")
    private String memberId; //成员外键

    @Column(name = "desgin_achievements")
    private Double desginAchievements; //设计绩效

    @Column(name = "budget_achievements")
    private Double budgetAchievements; //预算编辑绩效

    @Column(name = "upsubmit_achievements")
    private Double upsubmitAchievements; //上家送审绩效

    @Column(name = "downsubmit_achievements")
    private Double downsubmitAchievements; //下家送审绩效

    @Column(name = "truck_achievements")
    private Double truckAchievements; //预算编制绩效绩效

    @Column(name = "create_time")
    private String createTime; //创建时间

    @Column(name = "update_time")
    private String updateTime; //修改时间

    @Column(name = "founder_id")
    private String founderId; //创建人

    @Column(name = "base_project_id")
    private String baseProjectId; //创建人

    @Column(name = "founder_company_id")
    private String founderCompanyId; //创建人公司

    @Column(name = "del_flag")
    private String delFlag; //是否删除 0正常 1删除
}

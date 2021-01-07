package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

//项目勘探
@Table(name = "project_exploration")
@Data
public class ProjectExploration {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "scout")
    private String scout; //勘探人

    @Column(name = "exploration_time")
    private String explorationTime; //勘探时间

    @Column(name = "site")
    private String site; //地点

    @Column(name = "exploration_ideal")
    private String explorationIdeal; //勘探意见

    @Column(name = "remark")
    private String remark;  //备注

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String company_id;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "survey_results")
    private String surveyResults;
}

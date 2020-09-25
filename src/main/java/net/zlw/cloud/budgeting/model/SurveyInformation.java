package net.zlw.cloud.budgeting.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 勘察_信息(SurveyInformation)表实体类
 *
 * @author makejava
 * @since 2020-09-23 16:39:32
 */
@Data
@Table(name = "survey_information")
public class SurveyInformation implements Serializable{
    //唯一标识
    private String id;
    //勘察日期
    @Column(name = "survey_date")
    private String surveyDate;
    //勘察人员
    @Column(name = "investigation_personnel")
    private String investigationPersonnel;
    //勘察简述
    @Column(name = "survey_briefly")
    private String surveyBriefly;
    //信息价名称
    @Column(name = "price_information_name")
    private String priceInformationName;
    //信息价期数 1,第一期 2,第二期 3,第三期
    @Column(name = "price_information_nper")
    private String priceInformationNper;
    //项目基本信息外键id
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "budgeting_id")
    private String budgetingId;
    //创建时间
    @Column(name = "create_time")
    private String createTime;
    //修改时间
    @Column(name = "update_time")
    private String updateTime;
    //创建人id
    @Column(name = "founder_id")
    private String founderId;
    //创建人公司id
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    //状态 0,正常 1,删除
    @Column(name = "del_flag")
    private String delFlag;

}
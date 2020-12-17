package net.zlw.cloud.whDesignTask.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname SubmitOperationDept
 * @Description TODO
 * @Date 2020/10/29 9:57
 * @Created sjf
 */
@Data
@Table(name = "SubmitOperationDept")
public class SubmitOperationDept {

    @Column(name = "id")
    private String id;
    @Column(name = "base_project_id")
    private String base_project_id;
    @Column(name = "submit_dept")
    private String submit_dept;
    @Column(name = "submitter")
    private String submitter;
    @Column(name = "submit_time")
    private String submit_time;
    @Column(name = "examine_opinion")
    private String examine_opinion;
    @Column(name = "examine_dep")
    private String examine_dep;
    @Column(name = "reviewer")
    private String reviewer;
    @Column(name = "examine_time")
    private String examine_time;
}

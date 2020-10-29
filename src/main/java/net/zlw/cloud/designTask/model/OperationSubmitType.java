package net.zlw.cloud.designTask.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname OperationSubmitType
 * @Description TODO
 * @Date 2020/10/29 9:55
 * @Created sjf
 */
@Data
@Table(name = "OperationSubmitType")
public class OperationSubmitType {

    @Column(name = "id")
    private String id;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "operation_tpe")
    private String operationTpe;
    @Column(name = "resreon")
    private String resreon;
    @Column(name = "operation_time")
    private String operationTime;
    @Column(name = "operation_dept")
    private String operationDept;
    @Column(name = "operator")
    private String operator;

}

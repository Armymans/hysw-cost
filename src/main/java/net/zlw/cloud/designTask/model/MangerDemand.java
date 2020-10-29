package net.zlw.cloud.designTask.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname MangerDemand
 * @Description TODO
 * @Date 2020/10/29 10:05
 * @Created sjf
 */
@Data
@Table(name = "MangerDemand")
public class MangerDemand {

    @Column(name = "id")
    private String id;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "type")
    private String type;
    @Column(name = "caliber")
    private String caliber;
    @Column(name = "count")
    private String count;

}

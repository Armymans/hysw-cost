package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Classname operationLog
 * @Description TODO
 * @Date 2020/12/24 10:36
 * @Created by sjf
 */
@Data
@Table(name = "operation_log")
public class OperationLog {
           /* `id` varchar(60) NOT NULL COMMENT '唯一标识',
            `name` varchar(60) DEFAULT NULL COMMENT '操作人',
            `type` varchar(1) DEFAULT NULL COMMENT '类型',
            `content` longtext COMMENT '操作内容',
            `do_time` varchar(60) DEFAULT NULL COMMENT '操作时间',
            `do_object` varchar(200) DEFAULT NULL COMMENT '项目标识',
            `status` varchar(1) DEFAULT NULL COMMENT '状态',
            `ip` varchar(255) DEFAULT NULL COMMENT 'ip',*/
    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "content")
    private String content;
    @Column(name = "do_time")
    private String doTime;
    @Column(name = "do_object")
    private String doObject;
    @Column(name = "status")
    private String status;
    @Column(name = "ip")
    private String ip;
}

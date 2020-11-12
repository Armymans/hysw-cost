package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname 安徽单位汇总表
 * @Description TODO
 * @Date 2020/11/11 10:46
 * @Created by sjf
 */

@Data
@Table(name = "anhui_summary_sheet")
public class AnhuiSummarySheet {
          /*  `id` varchar(60) NOT NULL COMMENT 'id',
            `project_name` varchar(60) DEFAULT NULL COMMENT '项目名称',
            `item_name` varchar(60) DEFAULT NULL COMMENT '工程名称',
            `serial_number` varchar(60) DEFAULT NULL COMMENT '序号',
            `summarizing` varchar(60) DEFAULT NULL COMMENT '汇总内容',
            `price` varchar(60) DEFAULT NULL COMMENT '金额',
            `provisional_estimate` varchar(60) DEFAULT NULL COMMENT '其中：材料、设备暂估价',
            `create_time` varchar(60) DEFAULT NULL COMMENT '创建时间',
            `update_time` varchar(60) DEFAULT NULL COMMENT '修改时间',
            `base_project_id` varchar(60) DEFAULT NULL COMMENT '外键',
            `type` varchar(1) DEFAULT NULL COMMENT '类型1 神机 2 新点',
            `status` varchar(1) DEFAULT NULL COMMENT '状态 0正常1删除'*/

    @Column(name = "id")
    private String id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "summarizing")
    private String summarizing;
    @Column(name = "price")
    private String price;
    @Column(name = "provisional_estimate")
    private String provisionalEstimate;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "type")
    private String type;


}

package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname 安徽封面表
 * @Description TODO
 * @Date 2020/11/11 9:48
 * @Created by sjf
 */
@Data
@Table(name = "anhui_cover")
public class AnhuiCover {

          /*  `id` varchar(60) NOT NULL COMMENT 'id\r\n',
            `project_name` varchar(60) DEFAULT NULL COMMENT '项目名称',
            `price_big` varchar(60) DEFAULT NULL COMMENT '招标控制价（大写）',
            `price_small` varchar(60) DEFAULT NULL COMMENT '招标控制价（小写）',
            `consulting_unit` varchar(60) DEFAULT NULL COMMENT '工程造价咨询单位',
            `auditor` varchar(60) DEFAULT NULL COMMENT '审核人',
            `prepare_the_people` varchar(60) DEFAULT NULL COMMENT '编制人',
            `compile_time` varchar(60) DEFAULT NULL COMMENT '编制时间',
            `create_time` varchar(60) DEFAULT NULL COMMENT '创建时间',
            `update_time` varchar(60) DEFAULT NULL COMMENT '修改时间',
            `base_project_id` varchar(60) DEFAULT NULL COMMENT '外键',
            `design_fee` decimal(10,2) DEFAULT NULL COMMENT '设计费',
            `cost_consulting_fee` decimal(10,2) DEFAULT NULL COMMENT '造价咨询费',
            `type` varchar(1) DEFAULT NULL COMMENT '1神机2新点',
            `status` varchar(1) DEFAULT NULL COMMENT '0正常1删除',*/

    @Column(name = "id")
    private String id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "price_big")
    private String priceBig;
    @Column(name = "price_small")
    private String priceSmall;
    @Column(name = "consulting_unit")
    private String consultingUnit;
    @Column(name = "auditor")
    private String auditor;
    @Column(name = "prepare_the_people")
    private String prepareThePeople;
    @Column(name = "compile_time")
    private String compileTime;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "base_project_id")
    private String baseProjectId;
    @Column(name = "design_fee")
    private String designFee;
    @Column(name = "cost_consulting_fee")
    private String costConsultingFee;
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
}

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

           /* `id`
            `project_name` '项目名称',
            `price_big`  '招标控制价（大写）',
            `price_small`  '招标控制价（小写）',
            `consulting_unit`  '工程造价咨询单位',
            `auditor`  '审核人',
            `prepare_the_people`  '编制人',
            `compile_time`  '编制时间',
            `create_time`  '创建时间',
            `update_time`'修改时间',
            `base_project_id`  '外键',*/
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



}

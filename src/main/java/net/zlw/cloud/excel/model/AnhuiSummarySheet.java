package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Classname 安徽单位汇总表
 * @Description TODO
 * @Date 2020/11/11 10:46
 * @Created by sjf
 */

@Data
@Table(name = "anhui_summary_sheet")
public class AnhuiSummarySheet {
/*
            `id` varchar(60)                   ',
            `project_name` varchar(60)          '项目名称',
            `serial_number` varchar(60)         '序号',
            `summarizing` varchar(60)           '汇总内容',
            `price` varchar(60)                 '金额',
            `provisional_estimate` varchar(60)  '其中：材料、设备暂估价',
            `item_name` varchar(60)             '工程名称',
            `price_big` varchar(60)             '招标控制价（大写）',
            `price_small` varchar(60)           '招标控制价（小写）',
            `consulting_unit` varchar(60)       '工程造价咨询单位',
            `auditor` varchar(60)               '审核人',
            `prepare_the_people` varchar(60)    '编制人',
            `project_code` varchar(255)         '项目编码',
            `project_description` varchar(255)  '项目特征描述',
            `measuring_unit` varchar(255)       '计量单位',
            `quantities` varchar(255)           '工程量',
            `comprehensive_unit_price` decimal(10,2) '综合单价',
            `and_price` decimal(10,2)            '合价',
            `rate_artificial_cost` decimal(10,2)T '定额人工费',
            `fixed_mechanical_fee` varchar(255)  '定额机械费',
            `temporary_valuation` varchar(255)   '暂估价',
            `create_time` varchar(60)            '创建时间',
            `update_time` varchar(60)            '修改时间',
            `base_project_id` varchar(60)        '外键',
            `compile_time` varchar(60)          时间',
            `type` varchar(1)                    '类型1 神机 2 新点',
            `status` varchar(1)                  '状态 0正常1删除',*/

    @Column(name = "id")
    private String id;
    @Column(name = "project_name")
    private String projectName;
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
    @Column(name = "item_name")
    private String itemName;
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
    @Column(name = "compileTime")
    private String compileTime;
    @Column(name = "type")
    private String type;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_description")
    private String projectDescription;
    @Column(name = "measuring_unit")
    private String measuringUnit;
    @Column(name = "quantities")
    private String quantities;
    @Column(name = "comprehensive_unit_price")
    private BigDecimal comprehensiveUnitPrice;
    @Column(name = "fixed_mechanical_fee")
    private BigDecimal fixedMechanicalFee;
    @Column(name = "temporary_valuation")
    private BigDecimal temporaryValuation;
    @Column(name = "and_price")
    private BigDecimal andPrice;
    @Column(name = "rate_artificial_cost")
    private BigDecimal rateArtificialCost;

}

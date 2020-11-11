package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Classname CompetitiveItemValuation
 * @Description TODO
 * @Date 2020/11/11 14:06
 * @Created by sjf
 */
@Data
@Table(name = "competitive_item_valuation")
public class CompetitiveItemValuation {

         /*   `id`
            `project_code`  '项目编码',
            `project_name`  '项目名称',
            `computational_base` '计算基数',
            `rate` decimal(20,2) '费率',
            `amount` decimal(20,2)   '金额',
            `item_name` varchar(255) '工程名称',
            */
    @Column(name = "id")
    private String id;
    @Column(name = "project_code")
    private String projectCode;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "computational_base")
    private BigDecimal computationalBase;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "foreign_key")
    private String foreignKey;

}

package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Classname TaxStatement
 * @Description TODO
 * @Date 2020/11/11 14:55
 * @Created by sjf
 */
@Data
@Table(name = "tax_statement")
public class TaxStatement {

            /*`id` varchar(60) NOT NULL,
            `project_name` varchar(120) DEFAULT NULL COMMENT '项目名称',
            `calculation_basis` varchar(120) DEFAULT NULL COMMENT '计算基础',
            `computational_base` decimal(20,0) DEFAULT NULL COMMENT '计算基数',
            `rate` decimal(20,2) DEFAULT NULL COMMENT '费率',
            `amount` decimal(20,2) DEFAULT NULL COMMENT '金额',
            `item_name` varchar(255) DEFAULT NULL COMMENT '工程名称',*/

    @Column(name = "id")
    private String id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "calculation_basis")
    private String calculationBasis;
    @Column(name = "computational_base")
    private String computationalBase;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "foreign_key")
    private String foreignKey;

}

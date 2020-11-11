package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Classname 税金计价表
 * @Description TODO
 * @Date 2020/11/11 15:31
 * @Created by sjf
 */
@Data
@Table(name = "summary_materials_supplied")
public class SummaryMaterialsSupplied {
    /* `id`
     `name_material`  '材料名称',
     `special_requirements` '规格,型号等特殊要求',
     `unit`  '单位',
     `number_of`   '数量',
     `price`       '单价',
     `and_price`  '合价',
     `a_b`        '1a 2b 甲乙',
     `item_name`   '工程名称',
     `foreign_key` '外键',*/
    @Column(name = "id")
    private String id;
    @Column(name = "name_material")
    private String nameMaterial;
    @Column(name = "special_requirements")
    private String specialRequirements;
    @Column(name = "unit")
    private String unit;
    @Column(name = "number_of")
    private String numberOf;
    @Column(name = "and_price")
    private BigDecimal andPrice;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "a_b")
    private String aB;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "foreign_key")
    private String foreignKey;



}

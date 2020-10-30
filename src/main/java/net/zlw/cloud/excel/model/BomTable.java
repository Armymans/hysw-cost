package net.zlw.cloud.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bom_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomTable {
    /*
    *     `id` varchar(60) NOT NULL,
  `item_name` varchar(120) DEFAULT NULL COMMENT '物料名称',
  `specifications_models` varchar(120) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(120) DEFAULT NULL COMMENT '单位',
  `univalence` decimal(20,2) DEFAULT NULL COMMENT '单价',
  `quantity` decimal(20,0) DEFAULT NULL COMMENT '数量',
  `combined_price` decimal(20,2) DEFAULT NULL COMMENT '合价',
  `bom_table_infomation_id` varchar(60) DEFAULT NULL COMMENT '物料清单基本信息外键',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `update_tme` varchar(255) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `founder_company_id` varchar(255) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(255) DEFAULT NULL COMMENT '状态0正常1删除',

    * */
    @Id
    private String id;
    @Column(name = "material_code")
    private String materialCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "specifications_models")
    private String specificationsModels;
    private String unit;
    private String univalence;
    private String quantity;
    @Column(name = "combined_price")
    private String combinedPrice;
    private String remark;
    @Column(name = "bom_table_infomation_id")
    private String bomTableInfomationId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company_id")
    private String founderCompanyId;
    @Column(name = "del_flag")
    private String delFlag;

    public BomTable(String id, String itemName, String specificationsModels, String unit, String univalence, String quantity, String combinedPrice, String bomTableInfomationId) {
        this.id = id;
        this.itemName = itemName;
        this.specificationsModels = specificationsModels;
        this.unit = unit;
        this.univalence = univalence;
        this.quantity = quantity;
        this.combinedPrice = combinedPrice;
        this.bomTableInfomationId = bomTableInfomationId;
    }
}

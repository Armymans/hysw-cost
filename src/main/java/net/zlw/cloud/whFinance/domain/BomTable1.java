package net.zlw.cloud.whFinance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
@Data
@Table(name = "bom_table")
public class BomTable1 {


  @Column(name = "id")
  private String id;
  @Column(name = "material_code")
  private String materialCode;
  @Column(name = "item_name")
  private String itemName;
  @Column(name = "specifications_models")
  private String specificationsModels;
  @Column(name = "unit")
  private String unit;
  @Column(name = "univalence")
  private String univalence;
  @Column(name = "quantity")
  private String quantity;
  @Column(name = "combined_price")
  private String combinedPrice;
  @Column(name = "remark")
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



}

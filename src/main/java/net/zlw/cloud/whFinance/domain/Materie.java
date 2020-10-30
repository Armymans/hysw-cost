package net.zlw.cloud.whFinance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "material_Info")
public class Materie {
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
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "status")
    private String status;


}

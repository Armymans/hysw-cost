package net.zlw.cloud.whFinance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "material_info")
public class Materie {
    @Id
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
    @Column(name = "del_flag")
    private String delFlag;
    @Column(name = "area")
    private String area;
    @Column(name = "status")
    private String status;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSpecificationsModels() {
        return specificationsModels;
    }

    public void setSpecificationsModels(String specificationsModels) {
        this.specificationsModels = specificationsModels;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}

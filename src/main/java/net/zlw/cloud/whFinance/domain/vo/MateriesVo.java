package net.zlw.cloud.whFinance.domain.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class MateriesVo {

    private String material_code;
    private String item_name;
    private String specifications_models;
    private String unit;
    private String remark;
    private String status;
}

package net.zlw.cloud.jbDesignTask.domain.proVo;

import lombok.Data;

@Data
public class MaterialInformation {
    private String id;
    private String material_name;
    private String specification_model;
    private String company_of_util;
    private String amount_provided_a;
    private String actual_consumption;
    private String over_picked_quantity;
    private String price_by_a;
    private String total_materials_by_a;
    private String super_collar_materials;
}

package net.zlw.cloud.jbDesignTask.domain.vo;

import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.WjFileInfo;

import java.util.List;

@Data
public class WjDesignVo {
    private String id;
    private String base_project_id;
    private String application_num;
    private String customer_name;
    private String fire_table_size;
    private String classification_caliber;
    private String water_meter_diameter;
    private String status;
    private String customer_address;
    private String acceptance_unit;
    private String legal_representative;
    private String customer_results;
    private String survey_results;
    private String total_amount_quotation;
    private String living_surface_diameter;
    private String customer_category;
    private String land_certificate;
    private String charge_amount;
    private String site;
    private String postcode;
    private String agent;
    private String phone;
    private String phone_num;
    private String fax;
    private String nature_water_use;
    private String survey_option;
    private String surveyor;
    private String survey_time;
    private String remark;
    private List<WjFileInfo> DesignFileList;

}

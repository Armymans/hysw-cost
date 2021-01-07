package net.zlw.cloud.jbDesignTask.domain.vo;

import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.BudgetFileInfo;

import java.util.List;

@Data
public class WjBudgetVo {

    private String id;
    private String design_id;
    private String base_project_id;
    private String application_num;
    private String customer_name;
    private String status;
    private String fire_table_size;
    private String classification_caliber;
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
    private String phon_num;
    private String fax;
    private String water_project_cost;
    private String remark;
    private String operator;
    private String operation_time;
    private String remarks;
    private List<BudgetFileInfo> FileList;

}

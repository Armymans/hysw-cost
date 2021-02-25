package net.zlw.cloud.whFinance.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/***
 * 财务对接Vo
 */
@Data
public class BomTableVo {

    private String id;
    private String projectId;
    private String business_code;
    private String business_process;
    private String date_of;
    private String sales_organization;
    private String inventory_organization;
    private String cea_num;
    private String acquisition_types;
    private String contractor;
    private String project_categories_coding;
    private String project_types;
    private String item_coding;
    private String project_name;
    private String acquisition_department;

    @JsonProperty("BomTable")
    private List<BomTableVo2> BomTable;
}

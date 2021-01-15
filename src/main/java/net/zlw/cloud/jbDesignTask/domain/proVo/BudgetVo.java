package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BudgetVo {
    private String id;
    private String base_project_id;
    private String apply_by;
    private String apply_time;
    private String project_name;
    private String build_util;
    private String construction_unit;
    private String xiajia_cost;
    private String shangjia_cost;
    private String project_controller;
    private String budgeting_time;
    private String name_of_cost_unit;
    private String remark;
    @JsonProperty("MaterialsAList")
    private List<MaterialsAList> MaterialsAList;
    @JsonProperty("BudgetFileList")
    private List<BudgetFileList> BudgetFileList;

}

package net.zlw.cloud.jbDesignTask.domain.proVo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrackVo {
    private String id;
    private String cea_num;
    private String applicationNum;
    private String project_name;
    private String construction_unit;
    private String construction_organization;
    private String supervisor_unit;
    private String design_unit;
    private String audit_unit;
    private String contract_amount;
    @JsonProperty("MonthlyAuditReport")
    private List<MonthlyAuditReport> MonthlyAuditReport;
    private String base_project_id;
    private String status;
}

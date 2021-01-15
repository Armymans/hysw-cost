package net.zlw.cloud.jbDesignTask.domain.proVo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SettlementVo {

    private String id;
    private String base_project_id;
    private String project_name;
    private String cea_num;
    private String review_number;
    private String remark;
    private String audit_fee;
    private String audit_fee_materials;
    private String audi_construction;
    private String bid_section;
    private String remarks;
    @JsonProperty("ProjectInformation")
    private List<ProjectInformation> ProjectInformation;
    @JsonProperty("LastContractAudit")
    private List<LastContractAudit> LastContractAudit;
    @JsonProperty("OtherMeans")
    private List<OtherMeans> OtherMeans;
    @JsonProperty("ShangExamineMeans")
    private List<ShangExamineMeans> ShangExamineMeans;
    @JsonProperty("BidSectionAudit")
    private List<BidSectionAudit> BidSectionAudit;
    @JsonProperty("MaterialInformation")
    private List<MaterialInformation> MaterialInformation;
    @JsonProperty("AuditReport")
    private List<AuditReport> AuditReport;
    @JsonProperty("AcceptanceReport")
    private List<AcceptanceReport> AcceptanceReport;
    @JsonProperty("XiaExamineMeans")
    private List<XiaExamineMeans> XiaExamineMeans;

}

package net.zlw.cloud.general.model;

import lombok.Data;

@Data
public class AuditChekedVo {
    //审核时间
    private String auditTime;
    //审核结果
    private String auditResult;
    //审核类型
    private String auditType;
    //审核人
    private String auditor;
    //审核意见
    private String auditOpinion;
}

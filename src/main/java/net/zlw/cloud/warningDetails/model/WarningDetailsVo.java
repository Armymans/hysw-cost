package net.zlw.cloud.warningDetails.model;

import lombok.Data;

@Data
public class WarningDetailsVo {
    private String id;
    private String riskType;
    private String riskNotification;
    private String riskTime;
    private String status;
    private String sender;

    private String founderId;
    private String auditorId;

    //是否能说明
    private String checkInstructions;
    //是否能审核
    private String checkAudit;

}

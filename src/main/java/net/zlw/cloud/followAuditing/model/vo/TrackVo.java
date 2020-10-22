package net.zlw.cloud.followAuditing.model.vo;

import lombok.Data;
import net.zlw.cloud.followAuditing.model.TrackApplicationInfo;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.progressPayment.model.BaseProject;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrackVo  {
    private BaseProject baseProject;
    //跟踪审计信息
    private TrackAuditInfo auditInfo;
    private TrackApplicationInfo trackApplicationInfo;
    private List<TrackMonthly> monthlyList = new ArrayList<>();
    private List<AuditInfo> auditInfos;
    private String trackStatus;
    //保存提交状态
    private String status;
}

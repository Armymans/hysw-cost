package net.zlw.cloud.designProject.model;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.util.List;

@Data
public class ProjectVo {
    private BaseProject baseProject; //基本信息
    private DesignInfo designInfo;  //设计信息
    private ProjectExploration projectExploration;
    private PackageCame packageCame;
    //设计审核信息
    private List<AuditInfo> auditInfos;
    //设计变更审核信息
    private List<AuditInfo> auditInfos2;
    private AuditInfo auditInfo;
    private String desginStatus;
    private WujiangMoneyInfo wujiangMoneyInfo;
    private AnhuiMoneyinfo anhuiMoneyinfo;
    private DesignChangeInfo designChangeInfo;
    private List<DesignChangeInfo> DesignChangeInfos;
}

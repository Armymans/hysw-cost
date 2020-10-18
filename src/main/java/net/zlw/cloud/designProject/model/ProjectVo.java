package net.zlw.cloud.designProject.model;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.AuditInfo;

import java.util.List;

@Data
public class ProjectVo {
    private BaseProject baseProject; //基本信息
    private DesignInfo designInfo;  //设计信息
    private ProjectExploration projectExploration; //项目踏勘
    private PackageCame packageCame;//跟踪审计
    //设计审核信息
    private List<AuditInfo> auditInfos;  //审核状态
    //设计变更审核信息
    private List<AuditInfo> auditInfos2; //设计变更审核状态
    //设计合并信息
    private List<BaseProject> mergeBaseProject;
    private AuditInfo auditInfo; //审核信息
    private String desginStatus;
    private WujiangMoneyInfo wujiangMoneyInfo;
    private AnhuiMoneyinfo anhuiMoneyinfo;
    private DesignChangeInfo designChangeInfo;
    private List<DesignChangeInfo> DesignChangeInfos;
}

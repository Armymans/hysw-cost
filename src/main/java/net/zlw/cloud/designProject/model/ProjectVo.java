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

    private WujiangMoneyInfo wujiangMoneyInfo;//吴江
    private AnhuiMoneyinfo anhuiMoneyinfo;//安徽
    private MoneyInfo moneyInfo;//额外支付卡片信息

    private AuditInfo auditInfo; //审核信息
    private String desginStatus;
    private DesignChangeInfo designChangeInfo; //设计变更
    private List<DesignChangeInfo> DesignChangeInfos;
    private String type1;
    private String type2;
    private String type3;
    private String type4;
    private String key;
}

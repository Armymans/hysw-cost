package net.zlw.cloud.designProject.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;


@Table(name = "design_info")
@Data
public class DesignInfo{
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 60)
    private String id;

    @Column(name = "take_time")
    private String takeTime;

    @Column(name = "blueprint_countersign_time")
    private String blueprintCountersignTime;

    @Column(name = "blueprint_start_time")
    private String blueprintStartTime;

    @Column(name = "outsource")
    private String outsource;

    @Column(name = "design_unit")
    private String designUnit;

    @Column(name = "year_design_unit")
    private String yearDesignUnit;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "phone")
    private String phone;

    @Column(name = "outsource_money")
    private String outsourceMoney;

    @Column(name = "designer")
    private String designer;

    @Column(name = "remark")
    private String remark;

    @Column(name = "base_project_id")
    private String baseProjectId;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "status")
    private String status;

    @Column(name = "isaccount")
    private String isaccount;

    @Column(name = "isdeschange")
    private String isdeschange;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "isfinalaccount")
    private String isfinalaccount;


    //新加字段
    @Column(name = "project_remark")
    private String projectRemark;

    @Column(name = "water_supply")
    private String waterSupply;

    @Column(name = "special_user")
    private String specialUser;

    @Column(name = "user_of_day")
    private String userOfDay;

    @Column(name = "time_of_user")
    private String timeOfUser;

    @Column(name = "number_of_building")
    private String numberOfBuilding;

    @Column(name = "num_building")
    private String numBuilding;

    @Column(name = "building_layers")
    private String buildingLayers;

    @Column(name = "notes_drawing_time")
    private String notesDrawingTime;

    @Column(name = "attribution_show")
    private String attributionShow;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

//    private BaseProject baseProject;
//    private List<DesignChangeInfo> designChangeInfolist;
//    private AnhuiMoneyinfo anhuiMoneyinfo;
//    private WujiangMoneyInfo wujiangMoneyInfo;
//    private PackageCame packageCame;
//    private ProjectExploration projectExploration;
//    private AuditInfo auditInfo;
//    private List<DesignChangeInfo> designChangeInfos;

    @Transient
    private String designChangeTime;  //设计更改时间

    @Transient
    private String designUnitName; //设计单位名称

    @Transient
    private String ischange;  //是否更改

    @Transient
    private String amountCost;  //造价金额
    @Transient
    private String shouldBe; //营商0紧急 1不紧急
    @Transient
    private String ceaNum; //CEA编号
    @Transient
    private String projectNum; //项目编号
    @Transient
    private String projectName; //项目名称
    @Transient
    private String desginStatus; //设计状态 1待审核 2出图中 3未通过 4未到账 5已到账'
    @Transient
    private String district; //所属地区1芜湖2马鞍山3江北4吴江
    @Transient
    private String waterAddress;//用水地址
    @Transient
    private String constructionUnit;//建设单位
    @Transient
    private String BaseProjectContacts;//基本联系人
    @Transient
    private String contactNumber;//联系电话
    @Transient
    private String projectNature;//项目性质 0xx 1xx
    @Transient
    private String designCategory; //设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业
    @Transient
    private String subject;//主体 1居民住户2开发商 3政府事业 4工商户 5芜湖华衍',
    @Transient
    private String projectCategory; //项目类别
    @Transient
    private String aB;
    @Transient
    private String revenue; //应收收金额',
    @Transient
    private BigDecimal officialReceipts; //'实收金额',
    @Transient
    private BigDecimal DisMoney; //设计金额
    @Transient
    private String payTerm;
    @Transient
    private String mergeFlag;
    @Transient
    private String waterSupplyType;
    @Transient
    private String customerName;

    @Transient
    private String currentHandler;

    @Transient
    private String contacts1;

    @Transient
    private String customerPhone;

    @Transient
    private String ascriptionFlag; //归属标识

    @Transient
    private String desChangeFlag; //是否设计变更 0:是 1:否

    @Transient
    private String affiliationShow;
    @Transient
    private String contractAmount;

    @Transient
    private String editFlag;
}

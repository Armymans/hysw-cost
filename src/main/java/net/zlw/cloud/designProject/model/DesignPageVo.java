package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class DesignPageVo {
    private Integer pageNum;
    private Integer pageSize;
    private String district; //所属地区1芜湖2马鞍山3江北4吴江
    private String designCategory; //设计类别1市政管道2管网改造3新建小区4二次供水项目5工商户6居民装接水7行政事业
    private String projectNature;//项目性质 0新建 1改造
    private String shouldBe; //营商0紧急 1不紧急
    private String desginStartTime;
    private String desginEndTime;
    private String keyword;
    private String desginStatus; //设计状态 1待审核 2出图中 3未通过 4未到账 5已到账',
    private String userId; //当前登录用户id
    private String adminId; //领导id
    private String isaccount;

    private String currentPeople;
}

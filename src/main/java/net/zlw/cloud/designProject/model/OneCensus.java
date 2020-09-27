package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class OneCensus {
    private Integer municipalPipeline; //市政管道
    private Integer networkReconstruction; //管网改造
    private Integer newCommunity; //新建小区
    private Integer secondaryWater; //二次供水项目
    private Integer commercialHouseholds; //工商户
    private Integer waterResidents; //居民装接水
    private Integer administration; //行政事业
    private String yeartime; //年份
    private String monthtime; //月份
}

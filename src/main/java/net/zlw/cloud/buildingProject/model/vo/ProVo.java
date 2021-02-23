package net.zlw.cloud.buildingProject.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname ProVo
 * @Description 工程列表
 * @Date 2020/12/17 10:19
 * @Created by sjf
 */
@Data
public class ProVo {
    private String id; //工程id
    private String ceaNum; //CEA编号
    private String projectNum; // 项目编号
    private String projectName; // 项目名称
    private String projectNature; // 项目性质
    private String constructionUnit; // 建设单位
    private String mergeFlag; // 合并状态
    private String designCategory; // 设计类别
    private String waterSupplyType; // 供水类型
    private String customerName; // 客户名称
    private String waterAddress; //用水地址
    private String amountCost; //造价金额
    private BigDecimal officialReceipts; // 设计金额
    private String compileTime; //完成时间
    private String projectFlow; // 项目流程
    private String createTime; //创建时间
    private String district; //地区
    private String founderId; //创建人

    private String fShow;

    private String deleteShow; //0显示
}

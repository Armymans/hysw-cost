package net.zlw.cloud.buildingProject.model.vo;

import lombok.Data;

/**
 * @Classname baseProjectVo
 * @Description TODO
 * @Date 2020/12/11 15:38
 * @Created by sjf
 */
@Data
public class BaseVo {

    private String id; // 建设id
    private String ceaNum; // cea编号
    private String projectNum; // 项目编号
    private String projectName;// 项目名称
    private String constructionUnit; // 建设单位
    private String customerName; // 客户名称
    private String actualAmount; //设计费
    private String projectNature; // 项目性质
    private String designCategory; // 设计类别
    private String amountCost; // 造价金额
    private String contractAmount; // 合同金额
    private String cumulativePaymentTimes; // 进度款支付次数累计
    private String ceaTotalMoney; // 跟踪审计CEA金额
    private String cumulativeChangeAmount; // 签证/变更累计金额（元）
    private String authorizedNumber; // 结算审定金额（元）
    private String projectFlow; // 项目流程


}

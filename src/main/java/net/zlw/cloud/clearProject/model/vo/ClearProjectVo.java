package net.zlw.cloud.clearProject.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author dell
 * @Date 2020/9/28 17:14
 * @Version 1.0
 */
@Data
public class ClearProjectVo {
    /**
     * 唯一标识
     */
    private String id;

    /**
     * 项目编号
     */
    private String projectNum;

    /**
     * 项目id
     */
    private String baseId;
    /**
     * 项目名称id
     */
    private String projectName;

    /**
     * 招标人
     */
    private String tenderer;

    /**
     * 招标代理机构
     */
    private String procuratorialAgency;

    /**
     * 投标人
     */
    private String bidder;

    /**
     * 投标总价
     */
    private BigDecimal bidPrice;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 创建人id
     */
    private String founderId;

    /**
     * 创建人公司id
     */
    private String founderCompanyId;

    /**
     * 状态 0,正常 1,删除
     */
    private String delFlag;

    /**
     * 采购项目--预算编制
     */
    private String budgetingId;


}

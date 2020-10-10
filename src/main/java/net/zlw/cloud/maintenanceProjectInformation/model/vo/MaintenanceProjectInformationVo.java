package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;
import net.zlw.cloud.budgeting.model.SurveyInformation;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @Author dell
 * @Date 2020/9/27 15:47
 * @Version 1.0
 */
@Data
public class MaintenanceProjectInformationVo {
    private String id;
    //维修项目编号
    private String maintenanceItemId;
    //维修项目名称
    private String maintenanceItemName;
    //维修项目类型
    private String maintenanceItemType;
    // 报送部门
    private String submittedDepartment;

    /**
     * 报送时间
     */
    private String submitTime;

    /**
     * 编制人
     */
    private String preparePeople;

    /**
     * 项目地址
     */
    private String projectAddress;

    /**
     * 施工单位Id
     */
    private String constructionUnitId;

    /**
     * 送审金额
     */
    private BigDecimal reviewAmount;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 备注
     */

    private String remarkes;

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
     *  状态 0,正常 1,删除
     */

    private String delFlag;

    /**
     * 变更状态1待审核2处理中3未通过4待确认5已完成
     */
    private String type;


    private String constructionUnitName;


    private String memberName;

    // 勘察信息
    private SurveyInformation surveyInformation;

    // 结算审核信息
    private SettlementAuditInformation settlementAuditInformation;

    //审核信息
    private AuditInfo auditInfo;


}

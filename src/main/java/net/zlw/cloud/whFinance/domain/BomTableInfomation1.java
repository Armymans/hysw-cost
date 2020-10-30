package net.zlw.cloud.whFinance.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "bom_table_infomation")
public class BomTableInfomation1 {
    @Column(name = "id")
    private String id;
    @Column(name = "business_process")
    private String businessProcess;
    @Column(name = "date_of")
    private String dateOf;
    @Column(name = "sales_organization")
    private String salesOrganization;
    @Column(name = "inventory_organization")
    private String inventoryOrganization;
    @Column(name = "cea_num")
    private String ceaNum;
    @Column(name = "acquisition_types")
    private String acquisitionTypes;
    @Column(name = "contractor")
    private String contractor;

    @Column(name = "project_categories_coding")
    private String projectCategoriesCoding;
    @Column(name = "project_types")
    private String projectTypes;
    @Column(name = "item_coding")
    private String itemCoding;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "acquisition_department")
    private String acquisitionDepartment;
    @Column(name = "remark")
    private String remark;
    @Column(name = "budget_id")
    private String budgetId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "founder_company")
    private String founderCompany;
    @Column(name = "del_flag")
    private String delFlag;
    @Column(name = "business_code")
    private String businessCode;


}

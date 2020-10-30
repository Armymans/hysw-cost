package net.zlw.cloud.excel.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Table(name = "bom_table_infomation")
@Data
public class BomTableInfomation {
    /*
    * `id` varchar(60) NOT NULL,
  `business_process` varchar(120) DEFAULT NULL COMMENT '业务流程',
  `date_of` varchar(120) DEFAULT NULL COMMENT '日期',
  `sales_organization` varchar(120) DEFAULT NULL COMMENT '销售组织',
  `inventory_organization` varchar(120) DEFAULT NULL COMMENT '库存组织',
  `cea_num` varchar(120) DEFAULT NULL COMMENT 'cea编号',
  `acquisition_types` varchar(120) DEFAULT NULL COMMENT '领料类型',
  `contractor` varchar(120) DEFAULT NULL COMMENT '承建商',
  `project_categories_coding` varchar(120) DEFAULT NULL COMMENT '项目工程大类编码',
  `project_types` varchar(120) DEFAULT NULL COMMENT '项目工程大类',
  `item_coding` varchar(120) DEFAULT NULL COMMENT '项目编码',
  `project_name` varchar(120) DEFAULT NULL COMMENT '项目名称',
  `acquisition_department` varchar(120) DEFAULT NULL COMMENT '领料部门',
  `create_time` varchar(60) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(60) DEFAULT NULL COMMENT '修改时间',
  `founder_id` varchar(60) DEFAULT NULL COMMENT '创建人id',
  `founder_company` varchar(60) DEFAULT NULL COMMENT '创建人公司id',
  `del_flag` varchar(1) DEFAULT NULL COMMENT '状态',

    * */
    @Id
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

    private List<BomTable> bomTableList = new ArrayList<BomTable>();


}

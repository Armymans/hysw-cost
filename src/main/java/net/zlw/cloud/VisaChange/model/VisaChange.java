package net.zlw.cloud.VisaChange.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


//上下家签证/变更信息
@Data
@Table(name = "visa_change_information")
public class VisaChange implements Serializable {

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "create_time")
  private String createTime;
  @Column(name = "update_time")
  private String updateTime;
  @Column(name = "contract_amount")
  private String contractAmount;
  @Column(name = "creator_id")
  private String creatorId;
  @Column(name = "creator_company_id")
  private String creatorCompanyId;
  @Column(name = "state")
  private String state;
  @Column(name = "amount_visa_change")
  private double amountVisaChange;
  @Column(name = "compile_time")
  private String compileTime;
  @Column(name = "completion_time")
  private String completionTime;
  @Column(name = "outsourcing")
  private String outsourcing;
  @Column(name = "name_of_cost_unit_id")
  private String nameOfCostUnit;
  @Column(name = "contact")
  private String contact;
  @Column(name = "contact_number")
  private String contactNumber;
  @Column(name = "outsourcing_amount")
  private BigDecimal outsourcingAmount;
  @Column(name = "visa_change_reason")
  private String visaChangeReason;
  @Column(name = "base_project_id")
  private String baseProjectId;
  @Column(name = "apply_change_info_id")
  private String applyChangeInfoId;
  @Column(name = "up_and_down_mark")
  private String upAndDownMark;
  @Column(name = "status")
  private String status;
  @Column(name = "proportion_contract")
  private String proportionContract;
  @Column(name = "change_num")
  private String changeNum;






}

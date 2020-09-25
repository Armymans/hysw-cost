package net.zlw.cloud.budgeting.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table(name = "very_establishment")
public class VeryEstablishment implements Serializable {
  @Id
  private String id;
  @Column(name = "bidding_price_control")
  private BigDecimal biddingPriceControl;
  @Column(name = "vat_amount")
  private BigDecimal vatAmount;
  @Column(name = "pricing_together")
  private String pricingTogether;
  @Column(name = "receiving_time")
  private String receivingTime;
  @Column(name = "establishment_time")
  private String establishmentTime;
  private String remarkes;
  @Column(name = "base_project_id")
  private String baseProjectId;
  @Column(name = "budgeting_id")
  private String budgetingId;
  @Column(name = "create_time")
  private String createTime;
  @Column(name = "update_time")
  private String updateTime;
  @Column(name = "founder_id")
  private String founderId;
  @Column(name = "founder_company_id")
  private String founderCompanyId;
  @Column(name = "del_flag")
  private String delFlag;

}

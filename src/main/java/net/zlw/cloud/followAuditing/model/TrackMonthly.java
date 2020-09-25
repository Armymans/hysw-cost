package net.zlw.cloud.followAuditing.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "track_monthly")
@Data
public class TrackMonthly implements Serializable {
    @Id
    private String id;
    private String time;
    private String title;
    @Column(name = "perform_amount")
    private BigDecimal performAmount;
    @Column(name = "fill_time")
    private String fillTime;
    private String writter;
    @Column(name = "track_id")
    private String trackId;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "company_id")
    private String companyId;
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;

}

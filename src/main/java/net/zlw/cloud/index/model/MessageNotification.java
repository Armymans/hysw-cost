package net.zlw.cloud.index.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "message_notification")
@Data
public class MessageNotification implements Serializable {
    @Id
    private String id;
    @Column(name = "submit_time")
    private String submitTime;
    private String inform;
    @Column(name = "inform_status")
    private String informStatus;
    private String sender;
    @Column(name = "founder_id")
    private String founderId;
    @Column(name = "company_id")
    private String companyId;
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    private String title;
    private String details;
    @Column(name = "accept_id")
    private String acceptId;

}

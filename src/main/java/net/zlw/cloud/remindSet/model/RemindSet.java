package net.zlw.cloud.remindSet.model;

import lombok.Data;
import net.zlw.cloud.warningDetails.model.MemberManage;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by xulei on 2020/9/20.
 */
@Table(name = "remind_set")
@Data
public class RemindSet {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "remind_type")
    private String remindType;

    @Column(name = "remeber_id")
    private String remeberId;

    @Column(name = "message")
    private String message;

    @Column(name = "note_message")
    private String noteMessage;

    @Column(name = "email_message")
    private String emailMessage;

    @Column(name = "founder_id")
    private String founderId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    private List<MemberManage> memberManages;

    @Transient
    private String memberName;
}

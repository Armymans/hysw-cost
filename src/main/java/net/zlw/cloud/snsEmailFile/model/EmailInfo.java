package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description 邮件实体
 * @Date 2020/10/9 16:34
 **/
@Table(name = "email_info")
@Data
public class EmailInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "subject")
    private String subject;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "send_status")
    private String sendStatus;

}

package net.zlw.cloud.snsEmailFile.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Armyman
 * @Description 短信实体
 * @Date 2020/10/9 16:32
 **/
@Table(name = "sns_info")
@Data
public class SnsInfo {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "content")
    private String content;

    @Column(name = "business")
    private String business;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

}

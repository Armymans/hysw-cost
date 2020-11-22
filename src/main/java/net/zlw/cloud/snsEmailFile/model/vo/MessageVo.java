package net.zlw.cloud.snsEmailFile.model.vo;

import lombok.Data;

/**
 * @Classname MessageVo
 * @Description TODO
 * @Date 2020/11/19 14:00
 * @Created by sjf
 */
@Data
public class MessageVo {

    private String id;
    private String content; //邮件内容
    private String receiver; //接收者邮箱
    private String title; //站内消息标题
    private String details; //站内消息内容
    private String userId; //用户id
    private String phone; //手机号
    private String snsContent; //短信内容

}

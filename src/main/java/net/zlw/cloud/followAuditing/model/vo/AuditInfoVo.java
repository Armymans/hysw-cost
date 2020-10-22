package net.zlw.cloud.followAuditing.model.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Author dell
 * @Date 2020/10/22 18:39
 * @Version 1.0
 */
@Data
public class AuditInfoVo {

    private String id;
    private String baseProjectId;
    private String auditWord;
    private String auditResult;
    private String auditType;
    private String auditorId;
    private String auditOpinion;
    private String auditTime;
    private String memberName;
}

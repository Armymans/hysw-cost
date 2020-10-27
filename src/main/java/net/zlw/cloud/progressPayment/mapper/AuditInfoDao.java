package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AuditInfoDao extends Mapper<AuditInfo> {
    @Update(
            "update  audit_info \n" +
                    "set \n" +
                    "audit_result = #{auditResult},\n" +
                    "audit_opinion = #{auditOpinion}\n" +
                    "where \n" +
                    "base_project_id=#{id}"
    )
    void batchAudit(@Param("id") String id, @Param("auditResult") String auditResult, @Param("auditOpinion") String auditOpinion);

    @Select("select * from audit_info where base_project_id = #{id} and audit_type = 'risk' and audit_result ='0' and auditor_id = #{userId}")
    AuditInfo findByTypeAndAuditorIdAndAuditResult(@Param("userId") String userId, @Param("id") String id);

    @Select("select * from audit_info where base_project_id = #{id} and audit_type = 'risk'")
    AuditInfo findByTypeAnd(String id);

    @Select("select \n" +
            "IFNULL(audit_time,'-') auditTime,\n" +
            "(case audit_result\n" +
            "\t\twhen '0' then '待审核'\n" +
            "\t\twhen '1' then '已通过'\n" +
            "\t\twhen '2' then '未通过'\n" +
            "\t\tend\n" +
            ") auditResult," +
            "(case audit_type\n" +
            "\t\twhen '0' then '互审'\n" +
            "\t\twhen '1' then '上级审核'\n" +
            "\t\twhen '2' then '变更一审'\n" +
            "\t\twhen '3' then '变更二审'\n" +
            "\t\twhen '4' then '三审'\n" +
            "\t\tend\n" +
            ") auditType,\n" +
            "(select member_name from member_manage where id = auditor_id)  auditor,\n" +
            "IFNULL(audit_opinion,'-') auditOpinion\n" +
            "from audit_info \n" +
            "where \n" +
            "base_project_id = #{id}")
    List<AuditChekedVo> auditChek(@Param("id") String id);
}

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

    @Select("select * from audit_info where base_project_id = #{id}  and audit_result ='0' and auditor_id = #{userId}")
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
            "\t\twhen '1' then '上级领导审核'\n" +
            "\t\twhen '2' then '变更互审'\n" +
            "\t\twhen '3' then '变更上级领导审核'\n" +
            "\t\twhen '4' then '总经理审核'\n" +
            "\t\tend\n" +
            ") auditType,\n" +
            "(select member_name from member_manage where id = auditor_id)  auditor,\n" +
            "IFNULL(audit_opinion,'-') auditOpinion\n" +
            "from audit_info \n" +
            "where \n" +
            "base_project_id = #{id}\n" +
            "order by\n" +
            "audit_result desc")
    List<AuditChekedVo> auditChek(@Param("id") String id);

    @Select(
            "select\n" +
                    "IFNULL(audit_time,'-') auditTime,\n" +
                    "(case audit_result\n" +
                    "when '0' then '待审核'\n" +
                    "when '1' then '已通过'\n" +
                    "when '2' then '未通过'\n" +
                    "end\n" +
                    ") auditResult,\n" +
                    "(case audit_type\n" +
                    "when '0' then '互审'\n" +
                    "when '1' then '上级领导审核'\n" +
                    "when '2' then '变更互审'\n" +
                    "when '3' then '变更上级领导审核'\n" +
                    "when '4' then '部门主管审核'\n" +
                    "when '5' then '变更部门主管审核'\n" +
                    "end\n" +
                    ") auditType,\n" +
                    "(select member_name from member_manage where id = auditor_id)  auditor,\n" +
                    "IFNULL(audit_opinion,'-') auditOpinion\n" +
                    "from audit_info\n" +
                    "where\n" +
                    "base_project_id = #{id}\n" +
                    "and\n" +
                    "change_flag = '1'\n" +
                    "and\n" +
                    "status = '0'\n" +
                    "order by\n" +
                    "create_time"
    )
    List<AuditChekedVo> auditDesginChek(@Param("id") String id);

    @Select(
            "select\n" +
                    "IFNULL(audit_time,'-') auditTime,\n" +
                    "(case audit_result\n" +
                    "when '0' then '待审核'\n" +
                    "when '1' then '已通过'\n" +
                    "when '2' then '未通过'\n" +
                    "end\n" +
                    ") auditResult,\n" +
                    "(case audit_type\n" +
                    "when '0' then '互审'\n" +
                    "when '1' then '上级领导审核'\n" +
                    "when '2' then '变更互审'\n" +
                    "when '3' then '变更上级领导审核'\n" +
                    "when '4' then '部门主管审核'\n" +
                    "when '5' then '变更部门主管审核'\n" +
                    "end\n" +
                    ") auditType,\n" +
                    "(select member_name from member_manage where id = auditor_id)  auditor,\n" +
                    "IFNULL(audit_opinion,'-') auditOpinion\n" +
                    "from audit_info\n" +
                    "where\n" +
                    "base_project_id = #{id}\n" +
                    "and\n" +
                    "change_flag = '0'\n" +
                    "and\n" +
                    "status = '0'\n" +
                    "order by\n" +
                    "create_time"
    )
    List<AuditChekedVo> auditChangeDesginChek(@Param("id") String id);

    @Update(
            "update audit_info set `status` = '1' where id=#{id}"
    )
    void deleteChangeOld(@Param("id") String id);
}

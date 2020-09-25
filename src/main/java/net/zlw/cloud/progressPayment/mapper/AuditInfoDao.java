package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.progressPayment.model.AuditInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

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
}

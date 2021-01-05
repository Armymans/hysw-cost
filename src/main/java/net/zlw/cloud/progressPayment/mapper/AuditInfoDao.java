package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.general.model.AuditChekedVo;
import net.zlw.cloud.progressPayment.model.AuditInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AuditInfoDao extends Mapper<AuditInfo> {
    @Update(
            "update  audit_info   " +
                    "set   " +
                    "audit_result = #{auditResult},  " +
                    "audit_opinion = #{auditOpinion}  " +
                    "where   " +
                    "base_project_id=#{id}"
    )
    void batchAudit(@Param("id") String id, @Param("auditResult") String auditResult, @Param("auditOpinion") String auditOpinion);

    @Select("select * from audit_info where base_project_id = #{id}  and audit_result ='0' and auditor_id = #{userId}")
    AuditInfo findByTypeAndAuditorIdAndAuditResult(@Param("userId") String userId, @Param("id") String id);

    @Select("select * from audit_info where base_project_id = #{id} and audit_type = 'risk'")
    AuditInfo findByTypeAnd(String id);

    @Select("select   " +
            "IFNULL(audit_time,'-') auditTime,  " +
            "(case audit_result  " +
            "  when '0' then '待审核'  " +
            "  when '1' then '已通过'  " +
            "  when '2' then '未通过'  " +
            "  end  " +
            ") auditResult," +
            "(case audit_type  " +
            "  when '0' then '互审'  " +
            "  when '1' then '上级领导审核'  " +
            "  when '2' then '确认互审'  " +
            "  when '3' then '确认上级领导审核'  " +
            "  when '4' then '总经理审核'  " +
            "  when '5' then '确认总经理审核'  " +
            "  end  " +
            ") auditType,  " +
            "(select member_name from member_manage where id = auditor_id)  auditor,  " +
            "IFNULL(audit_opinion,'-') auditOpinion  " +
            "from audit_info   " +
            "where   " +
            "base_project_id = #{id}  " +
            "order by  " +
            "audit_type asc")
    List<AuditChekedVo> auditChek(@Param("id") String id);

    @Select(
            "select  " +
                    "IFNULL(audit_time,'-') auditTime,  " +
                    "(case audit_result  " +
                    "when '0' then '待审核'  " +
                    "when '1' then '已通过'  " +
                    "when '2' then '未通过'  " +
                    "end  " +
                    ") auditResult,  " +
                    "(case audit_type  " +
                    "when '0' then '互审'  " +
                    "when '1' then '上级领导审核'  " +
                    "when '2' then '变更互审'  " +
                    "when '3' then '变更上级领导审核'  " +
                    "when '4' then '部门主管审核'  " +
                    "when '5' then '变更部门主管审核'  " +
                    "end  " +
                    ") auditType,  " +
                    "(select member_name from member_manage where id = auditor_id)  auditor,  " +
                    "IFNULL(audit_opinion,'-') auditOpinion  " +
                    "from audit_info  " +
                    "where  " +
                    "base_project_id = #{id}  " +
                    "and  " +
                    "change_flag = '1'  " +
                    "and  " +
                    "status = '0'  " +
                    "order by  " +
                    "create_time"
    )
    List<AuditChekedVo> auditDesginChek(@Param("id") String id);

    @Select(
            "select  " +
                    "IFNULL(audit_time,'-') auditTime,  " +
                    "(case audit_result  " +
                    "when '0' then '待审核'  " +
                    "when '1' then '已通过'  " +
                    "when '2' then '未通过'  " +
                    "end  " +
                    ") auditResult,  " +
                    "(case audit_type  " +
                    "when '0' then '互审'  " +
                    "when '1' then '上级领导审核'  " +
                    "when '2' then '变更互审'  " +
                    "when '3' then '上级领导审核'  " +
                    "when '4' then '部门主管审核'  " +
                    "when '5' then '部门主管审核'  " +
                    "end  " +
                    ") auditType,  " +
                    "(select member_name from member_manage where id = auditor_id)  auditor,  " +
                    "IFNULL(audit_opinion,'-') auditOpinion  " +
                    "from audit_info  " +
                    "where  " +
                    "base_project_id = #{id}  " +
                    "and  " +
                    "change_flag = '0'  " +
                    "and  " +
                    "status = '0'  " +
                    "order by  " +
                    "create_time"
    )
    List<AuditChekedVo> auditChangeDesginChek(@Param("id") String id);

    @Update(
            "update audit_info set `status` = '1' where id=#{id}"
    )
    void deleteChangeOld(@Param("id") String id);

    @Select(
            "select " +
                    "IFNULL(audit_time,'-') auditTime,  " +
                    "(case audit_result  " +
                    "  when '0' then '待审核'  " +
                    "  when '1' then '已通过'  " +
                    "  when '2' then '未通过'  " +
                    "  end  " +
                    ") auditResult, " +
                    "(case audit_type  " +
                    "  when '0' then '互审'  " +
                    "  when '1' then '上级领导审核'  " +
                    "  when '2' then '互审'  " +
                    "  when '3' then '上级领导审核'  " +
                    "  when '4' then '总经理审核'  " +
                    "  when '5' then '总经理审核'  " +
                    "  end  " +
                    ") auditType,  " +
                    "(select member_name from member_manage where id = auditor_id)  auditor,  " +
                    "IFNULL(audit_opinion,'-') auditOpinion  " +
                    "from audit_info " +
                    "where " +
                    "base_project_id = #{id} " +
                    "order by  " +
                    "audit_type"
    )
    List<AuditChekedVo> auditMaintenanceChek(String id);

    @Select(
            "select  " +
                    "IFNULL(audit_time,'-') auditTime,   " +
                    "(case audit_result   " +
                    "  when '0' then '待审核'   " +
                    "  when '1' then '已通过'   " +
                    "  when '2' then '未通过'   " +
                    "  end   " +
                    ") auditResult,  " +
                    "(case audit_type   " +
                    "  when '0' then '互审'   " +
                    "  when '1' then '上级领导审核'   " +
                    "  when '2' then '互审'   " +
                    "  when '3' then '上级领导审核'   " +
                    "  when '4' then '总经理审核'   " +
                    "  when '5' then '总经理审核'   " +
                    "  end   " +
                    ") auditType,   " +
                    "(select member_name from member_manage where id = auditor_id)  auditor,   " +
                    "IFNULL(audit_opinion,'-') auditOpinion   " +
                    "from audit_info  " +
                    "where  " +
                    "base_project_id = #{id}  " +
                    "and  " +
                    "maintenance_flag = '0'  " +
                    "order by   " +
                    "audit_type"
    )
    List<AuditChekedVo> auditAgainMaintenanceChek(String id);

    @Select("SELECT   " +
            "            id  id,   " +
            "            user_name userName " +
            "            FROM mky_user   " +
            "            WHERE   " +
            "            del_flag = '0'   " +
            "            and ( role_id = 'role7618' or role_id = 'role7616' or role_id = 'role7636' or role_id = 'role7637')   " +
            "            and ( job_id = (select job_id from mky_user where id = #{userId} ) or #{userId} = '198910006' or #{userId} = '201803018' )")
    List<MkyUser> findCurrent(@Param("userId") String id);


    @Select("SELECT " +
            "           id  id, " +
            "           user_name userName, " +
            "           job_id " +
            "           FROM mky_user " +
            "           WHERE " +
            "           del_flag = '0' " +
            "           and ( role_id = 'role7617' or role_id = 'role7615' or role_id = 'role7639' or role_id = 'role7614' or role_id = 'role7638') " +
            "           and ( ( job_id = (select job_id from mky_user where id = #{userId} ) or job_id is null) or #{userId} = '200610002' or #{userId} = '200101005' or #{userId} = '201411001') ")
    List<MkyUser> findCurrentCost(@Param("userId") String id);

    @Select("SELECT  " +
            "           id  id,  " +
            "           user_name userName,  " +
            "           job_id  " +
            "           FROM mky_user  " +
            "           WHERE  " +
            "           del_flag = '0'  " +
            "           and ( role_id = 'role7617' or role_id = 'role7635' or role_id = 'role7639' or role_id = 'role7614' or role_id = 'role7638')  " +
            "           and ( ( job_id = (select job_id from mky_user where id = #{uid} ) or job_id is null)) ")
    List<MkyUser> findPreparePeople(@Param("uid") String id);

@Select("SELECT      " +
        "                                id  id,      " +
        "                                user_name userName   , " +
        "                                  job_id " +
        "                                FROM mky_user      " +
        "                                WHERE      " +
        "                                del_flag = '0'      " +
        "                                and ( role_id = 'role7618' or role_id = 'role7616' or role_id = 'role7636' or role_id = 'role7637')      " +
        "                                and ( job_id = (select job_id from mky_user where id = #{userId} ) or job_id is null )")
    List<MkyUser> findDesignAll(@Param("userId") String id);

    @Select("SELECT " +
            " *  " +
            "FROM " +
            " audit_info  " +
            "WHERE " +
            " base_project_id = #{key}  " +
            " AND `status` = '0'  " +
            " AND audit_result = '0'  " +
            " AND audit_type IN ( 0, 2 )")
    List<net.zlw.cloud.warningDetails.model.AuditInfo> selectAuditInfoList(@Param("key") String key);
}

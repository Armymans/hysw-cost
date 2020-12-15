package net.zlw.cloud.warningDetails.mapper;

import net.zlw.cloud.warningDetails.model.PageVo;
import net.zlw.cloud.warningDetails.model.WarningDetails;
import net.zlw.cloud.warningDetails.model.WarningDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by xulei on 2020/9/21.
 */
@org.apache.ibatis.annotations.Mapper
public interface WarningDetailsMapper extends Mapper<WarningDetails> {

    @Update("update " +
            "warning_details " +
            "set " +
            "update_time = #{updateTime}, " +
            "status = #{status}, " +
            "WHERE " +
            "id = #{id} ")
    WarningDetails updateWarningDetails();

    @Select("select " +
            "w.id id, " +
            "w.risk_type riskType, " +
            "w.risk_notification riskNotification, " +
            "w.risk_time riskTime, " +
            "(case w.status " +
            " when '1' then '未读' " +
            " when '2' then '已读' " +
            " when '3' then '已说明' " +
            " when '4' then '未通过' " +
            " when '5' then '已通过' " +
            " end " +
            ") status, " +
            "w.sender sender, " +
            "w.founder_id founderId, " +
            "a.auditor_id auditorId " +
            "from  " +
            "warning_details w LEFT JOIN audit_info a on w.id = a.base_project_id and a.audit_result = '0' " +
            "where  " +
            "(w.recipient_id = #{id}) and  " +
            "(w.status = #{p.status} or #{p.status} = '') and  " +
            "(w.send_time >= #{p.startTime} or #{p.startTime} = '') and  " +
            "(w.send_time <= #{p.endTime} or #{p.endTime} = '') and " +
            "( " +
            "w.risk_type like concat('%',#{p.keyword},'%') or " +
            "w.risk_notification like concat('%',#{p.keyword},'%')  " +
            ")")
    List<WarningDetailsVo> findDetails(@Param("p") PageVo pageVo, @Param("id") String id);
}

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

    @Update("update\n" +
            "warning_details\n" +
            "set\n" +
            "update_time = #{updateTime},\n" +
            "status = #{status},\n" +
            "WHERE\n" +
            "id = #{id}\n")
    WarningDetails updateWarningDetails();

    @Select("select\n" +
            "w.id id,\n" +
            "w.risk_type riskType,\n" +
            "w.risk_notification riskNotification,\n" +
            "w.risk_time riskTime,\n" +
            "(case w.status\n" +
            "\twhen '1' then '未读'\n" +
            "\twhen '2' then '已读'\n" +
            "\twhen '3' then '已说明'\n" +
            "\twhen '4' then '未通过'\n" +
            "\twhen '5' then '已通过'\n" +
            "\tend\n" +
            ") status,\n" +
            "w.sender sender,\n" +
            "w.founder_id founderId,\n" +
            "a.auditor_id auditorId\n" +
            "from \n" +
            "warning_details w LEFT JOIN audit_info a on w.id = a.base_project_id and a.audit_result = '0'\n" +
            "where \n" +
            "(w.recipient_id = #{id}) and \n" +
            "(w.status = #{p.status} or #{p.status} = '') and \n" +
            "(w.send_time >= #{p.startTime} or #{p.startTime} = '') and \n" +
            "(w.send_time <= #{p.endTime} or #{p.endTime} = '') and\n" +
            "(\n" +
            "w.risk_type like concat('%',#{p.keyword},'%') or\n" +
            "w.risk_notification like concat('%',#{p.keyword},'%') \n" +
            ")")
    List<WarningDetailsVo> findDetails(@Param("p") PageVo pageVo, @Param("id") String id);
}

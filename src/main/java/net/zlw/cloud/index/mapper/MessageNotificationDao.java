package net.zlw.cloud.index.mapper;

import net.zlw.cloud.index.model.MessageNotification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MessageNotificationDao extends Mapper<MessageNotification> {

    @Select(
            "select \n" +
                    "id id, \n" +
                    "submit_time submitTime,\n" +
                    "inform inform,\n" +
                    "(case inform_status when '0' then '未读' when '1' then '已读' end ) informStatus , \n" +
                    "(case inform_type when '1' then '通知' when '2' then '提醒' when '3' then '风险' end) informType , \n" +
                    "sender sender  \n" +
                    "from \n" +
                    "message_notification \n" +
                    "where\n" +
                    "`status` = '0'\n" +
                    "and\n" +
                    "(accept_id = #{id} or #{id} = '')\n" +
                    "ORDER BY \n" +
                    "submit_time desc \n" +
                    "limit 0,4"
    )
    List<MessageNotification> findMessage(@Param("id") String id);
}

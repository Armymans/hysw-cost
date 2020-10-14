package net.zlw.cloud.index.mapper;

import net.zlw.cloud.index.model.MessageNotification;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MessageNotificationDao extends Mapper<MessageNotification> {

    @Select("select id id, submit_time submitTime,inform inform,(case inform_status when '0' then '未读' when '1' then '已读' end ) informStatus , (case inform_type when '1' then '通知' when '2' then '提醒' when '3' then '风险' end) informType , sender sender  from message_notification ORDER BY submit_time desc limit 0,2")
    List<MessageNotification> findMessage();
}

package net.zlw.cloud.index.mapper;

import net.zlw.cloud.index.model.MessageNotification;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MessageNotificationDao extends Mapper<MessageNotification> {

    @Select("select * from message_notification ORDER BY submit_time desc limit 0,2")
    List<MessageNotification> findMessage();
}

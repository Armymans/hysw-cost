package net.zlw.cloud.remindSet.mapper;

import net.zlw.cloud.remindSet.model.RemindSet;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**  提醒设置列表
 * Created by xulei on 2020/9/20.
 */
@org.apache.ibatis.annotations.Mapper
public interface RemindSetMapper extends Mapper<RemindSet> {

    @Select("SELECT  * from  remind_set  WHERE  status = '0'")
    List<RemindSet> findAllByStatus();
}

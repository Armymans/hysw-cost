package net.zlw.cloud.warningDetails.mapper;

import net.zlw.cloud.warningDetails.model.WarningDetails;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

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

}

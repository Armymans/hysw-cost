package net.zlw.cloud.snsEmailFile.mapper;

import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.warningDetails.model.AuditInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author Armyman
 * @Description //低代码用户
 * @Date 19:14 2020/10/29
 **/
public interface MkyUserMapper extends Mapper<MkyUser> {

    @Select("SELECT * FROM mky_user WHERE user_name = #{name}")
    MkyUser selectOneUser(@Param("name") String name);
}

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

    @Select("SELECT   \n" +
            "                       id  id,   \n" +
            "                       user_name userName,   \n" +
            "                       job_id   \n" +
            "                       FROM mky_user   \n" +
            "                       WHERE   \n" +
            "                       del_flag = '0'   \n" +
            "                       and ( role_id = 'role7617' or role_id = 'role7635' or role_id = 'role7639' or role_id = 'role7614' or role_id = 'role7638' or role_id = 'role7618' or role_id = 'role7616' or role_id = 'role7636' or role_id = 'role7637')  and job_id = #{dept}  ")
    List<MkyUser> findPersonAll(@Param("dept") String deptId);
}

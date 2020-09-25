package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface MemberManageDao extends Mapper<MemberManage> {

    @Select("SELECT  member_name from  member_manage  WHERE  status = '0' and id = #{id}  ")
    MemberManage selectByIdAndStatus(@Param("id") String id);

    @Select("select * from member_manage where dep_id = '2' and dep_admin = '1'")
    MemberManage selectAdmin();
}

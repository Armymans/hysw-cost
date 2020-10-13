package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface MemberManageDao extends Mapper<MemberManage> {

    @Select("SELECT  member_name from  member_manage  WHERE  status = '0' and id = #{id}  ")
    MemberManage selectByIdAndStatus(@Param("id") String id);

    @Select("select * from member_manage where dep_id = '1' and dep_admin = '1'")
    MemberManage selectAdmin();

    @Select(
            "SELECT id,member_name,password,member_sex,member_account,member_role_id,email,phone,founder_id,company_id,status,dep_id,dep_admin,account_auth,create_date,update_date,salt FROM member_manage WHERE ( dep_admin = '1' and dep_id ='1' )"
    )
    MemberManage memberManageById();

}

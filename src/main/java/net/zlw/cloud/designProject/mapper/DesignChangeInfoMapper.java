package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.DesignChangeInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface DesignChangeInfoMapper extends Mapper<DesignChangeInfo> {
    @Update(
            "update  design_change_info \n" +
                    "set \n" +
                    "status = \"1\"\n" +
                    "where \n" +
                    "design_info_id=#{id}"
    )
    void deleteProject(@Param("id") String id);

    @Select(
            "select \n" +
                    "design_change_time,\n" +
                    "design_change_cause\n" +
                    "from \n" +
                    "design_change_info \n" +
                    "where \n" +
                    "design_info_id = \"#{id}\" \n" +
                    "order BY design_change_time"
    )
    List<DesignChangeInfo> designChangeByid(@Param("id") String id);

    @Select(
            "select \n" +
                    "id,\n" +
                    "design_change_time,\n" +
                    "designer,\n" +
                    "design_change_cause,\n" +
                    "remark,\n" +
                    "founder_id,\n" +
                    "company_id,\n" +
                    "status,\n" +
                    "create_time,\n" +
                    "update_time,\n" +
                    "design_info_id\n" +
                    "from \n" +
                    "design_change_info\n" +
                    "ORDER BY \n" +
                    "design_change_time desc\n" +
                    "where\n" +
                    "design_info_id = #{id}"
    )
    List<DesignChangeInfo> designChangeInfoByid(@Param("id") String id);
}

package net.zlw.cloud.maintenanceProjectInformation.mapper;

import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MaintenanceProjectInformationMapper extends tk.mybatis.mapper.common.Mapper<MaintenanceProjectInformation> {


    //查询状态正常的所有检维修项目信息
//    @Select("select * from maintenance_project_information where del_flag = '0'")
//    List<MaintenanceProjectInformation> selectAllByDelFlag();


    /**
     * 检维修-删除
     * @param id
     */
    @Update(
            "update  maintenance_project_information set del_flag = '1' where id = #{id}"
    )
    void deleteMaintenanceProjectInformation(@Param("id") String id);


    @Select("select * from maintenance_project_information where id = #{id}")
    MaintenanceProjectInformation selectById(@Param("id") String id);

}
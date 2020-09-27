package net.zlw.cloud.maintenanceProjectInformation.mapper;

import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConstructionUnitManagementMapper extends tk.mybatis.mapper.common.Mapper<ConstructionUnitManagement> {

    /**
     * 根据id查询施工单位
     * @param id
     * @return
     */
    @Select("select * from construction_unit_management where id = #{id}")
    ConstructionUnitManagement selectById(@Param("id") String id);


}
package net.zlw.cloud.buildingProject.mapper;

import net.zlw.cloud.buildingProject.model.BuildingProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/11 11:48
 **/
@Mapper
public interface BuildingProjectMapper extends tk.mybatis.mapper.common.Mapper<BuildingProject> {

    @Select("SELECT " +
            " distinct bp.id, " +
            " building_project_name buildingProjectName" +
            " FROM " +
            " building_project bp " +
            " left join base_project b on b.building_project_id != bp.id " +
            " where bp.status = 0 and bp.or_submit = 1 and b.building_project_id != bp.id")
    List<BuildingProject> findBuildingProject();

}

package net.zlw.cloud.clearProject.mapper;

import net.zlw.cloud.clearProject.model.ClearProject;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ClearProjectMapper extends tk.mybatis.mapper.common.Mapper<ClearProject> {

    @Update(
            "update  clear_project set del_flag = '1' where id = #{id}"
    )
    void deleteClearProject(@Param("id") String id);


    @Select("SELECT    " +
            "c1.id,   " +
            "c1.project_num projectNum,   " +
            "c1.project_name projectName,   " +
            "c2.construction_unit constructionUnit,   " +
            "c2.tenderer tenderer,   " +
            "c2.bid_money bidMoney,   " +
            "c2.bid_project_address bidProjectAddress,   " +
            "c1.create_time createTime,   " +
            "c1.founder_id founderId   " +
            "FROM   " +
            "clear_project c1   " +
            "LEFT JOIN call_for_bids c2 ON c2.clear_project_id = c1.id   " +
            "WHERE   " +
            "c1.del_flag = '0' and  ( " +
            "c1.project_num like concat ('%',#{keyWord},'%') or   " +
            "c1.project_name like concat ('%',#{keyWord},'%')) ")
    List<ClearProject> findAllClearProject(PageRequest pageRequest);
}
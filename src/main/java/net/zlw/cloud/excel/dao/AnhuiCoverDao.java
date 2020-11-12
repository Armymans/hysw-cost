package net.zlw.cloud.excel.dao;

import net.zlw.cloud.excel.model.AnhuiCover;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Classname AnhuiCoverDao
 * @Description TODO
 * @Date 2020/11/11 9:58
 * @Created by sjf
 */
@org.apache.ibatis.annotations.Mapper
public interface AnhuiCoverDao extends Mapper<AnhuiCover> {

    @Select("SELECT * FROM anhui_cover WHERE type = '2' AND `status` = '0' AND base_project_id =#{id}")
    AnhuiCover findOne(String id);
}

package net.zlw.cloud.whFinance.mapper;

import net.zlw.cloud.whFinance.domain.Materie;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MaterialMapper extends Mapper<Materie> {
    
    @Select("SELECT  " +
            " id, " +
            " material_code materialCode, " +
            " item_name itemName, " +
            " specifications_models specificationsModels, " +
            " unit " +
            " FROM material_info " +
            " WHERE " +
            " del_flag = '0' " +
            " AND " +
            " (material_code LIKE  CONCAT('%',#{keyWord},'%') or " +
            " item_name LIKE CONCAT( '%', #{keyWord}, '%'  ) or " +
            " specifications_models LIKE CONCAT('%',#{keyWord},'%'))")
    List<Materie> findAllMaterie(@Param("keyWord") String keyWord);

    @Select("select * from material_info where material_code =#{materialCode}")
    Materie findByCode(@Param("materialCode")String materialCode);
}

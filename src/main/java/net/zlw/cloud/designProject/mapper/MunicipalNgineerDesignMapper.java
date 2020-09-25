package net.zlw.cloud.designProject.mapper;

import net.zlw.cloud.designProject.model.MunicipalNgineerDesign;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MunicipalNgineerDesignMapper extends Mapper<MunicipalNgineerDesign> {
    @Select(
            "SELECT\n" +
                    " project_cost,\n" +
                    " design_basic_cost\n" +
                    "FROM\n" +
                    " municipal_ngineer_design \n" +
                    "WHERE\n" +
                    " id IN (\n" +
                    " ( SELECT id FROM municipal_ngineer_design WHERE project_cost > #{cost} ORDER BY project_cost ASC LIMIT 0, 1 ),\n" +
                    " ( SELECT id FROM municipal_ngineer_design WHERE project_cost < #{cost} ORDER BY project_cost DESC LIMIT 0, 1 ),\n" +
                    " ( SELECT DISTINCT project_cost FROM municipal_ngineer_design WHERE project_cost = #{cost} ) \n" +
                    " ) \n" +
                    "GROUP BY\n" +
                    " project_cost DESC \n" +
                    " LIMIT 0,2"
    )
    List<MunicipalNgineerDesign> designMoney(@Param("cost") BigDecimal cost);
}

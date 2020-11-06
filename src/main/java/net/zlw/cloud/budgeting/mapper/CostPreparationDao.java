package net.zlw.cloud.budgeting.mapper;

import net.zlw.cloud.budgeting.model.CostPreparation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface CostPreparationDao extends Mapper<CostPreparation> {

    @Select("SELECT * FROM cost_preparation WHERE id =#{id}")
    CostPreparation findIdByStatus(@Param("id")String id);
}

package net.zlw.cloud.VisaChange.mapper;

import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface VisaApplyChangeInformationMapper extends Mapper<VisaApplyChangeInformation> {
    @Select(
            "SELECT  " +
                    "amount_visa_change " +
                    " FROM  " +
                    "  `visa_change_information`   " +
                    "WHERE  " +
                    "  base_project_id = #{id}   ORDER BY change_num desc limit 1"
    )

    String amountVisaChangeSum(@Param("id") String id);
    @Select(
            "SELECT  " +
                    "sum(contract_amount)  " +
                    "FROM  " +
                    "visa_change_information  " +
                    "where  " +
                    "base_project_id = #{id}"
    )
    String contractAmount(@Param("id") String id);
    @Select(
            "SELECT  " +
                    "max(change_num)  " +
                    "FROM  " +
                    "visa_change_information  " +
                    "where  " +
                    "base_project_id = #{id}"
    )
    String changeCount(@Param("id") String id);
}

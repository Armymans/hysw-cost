package net.zlw.cloud.VisaChange.mapper;

import net.zlw.cloud.VisaChange.model.VisaApplyChangeInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface VisaApplyChangeInformationMapper extends Mapper<VisaApplyChangeInformation> {
    @Select(
            "SELECT\n" +
                    "sum(amount_visa_change)\n" +
                    "FROM\n" +
                    "visa_change_information\n" +
                    "where\n" +
                    "base_project_id = #{id}"
    )

    String amountVisaChangeSum(@Param("id") String id);
    @Select(
            "SELECT\n" +
                    "sum(contract_amount)\n" +
                    "FROM\n" +
                    "visa_change_information\n" +
                    "where\n" +
                    "base_project_id = #{id}"
    )
    String contractAmount(@Param("id") String id);
    @Select(
            "SELECT\n" +
                    "count(*)\n" +
                    "FROM\n" +
                    "visa_change_information\n" +
                    "where\n" +
                    "base_project_id = #{id}"
    )
    String changeCount(@Param("id") String id);
}

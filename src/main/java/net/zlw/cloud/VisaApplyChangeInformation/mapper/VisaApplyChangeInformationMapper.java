package net.zlw.cloud.VisaApplyChangeInformation.mapper;

import net.zlw.cloud.VisaApplyChangeInformation.model.VisaApplyChangeInformation;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by xulei on 2020/9/22.
 */
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

package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProgressPaymentInformationDao extends Mapper<ProgressPaymentInformation> {

    @Select(
            "SELECT\n" +
                    "current_payment_Information\n" +
                    "FROM\n" +
                    "`progress_payment_information`\n" +
                    "where\n" +
                    "base_project_id = #{id}\n" +
                    "ORDER BY\n" +
                    "current_payment_Information desc"
    )
    List<String> NewcurrentPaymentInformation(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "sum(current_payment_Information)\n" +
                    "FROM\n" +
                    "`progress_payment_information`\n" +
                    "where\n" +
                    "base_project_id = #{id}\n" +
                    "ORDER BY\n" +
                    "current_payment_Information desc"
    )
    List<String> SumcurrentPaymentInformation(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "current_payment_ratio\n" +
                    "FROM\n" +
                    "`progress_payment_information`\n" +
                    "where\n" +
                    "base_project_id = #{id}\n" +
                    "ORDER BY\n" +
                    "current_payment_ratio desc"
    )
    String currentPaymentRatio(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "sum(cumulative_payment_times)\n" +
                    "FROM\n" +
                    "`progress_payment_information`\n" +
                    "where\n" +
                    "base_project_id = #{id}"
    )
    String cumulativePaymentTimes(@Param("id") String id);
}

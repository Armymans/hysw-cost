package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.progressPayment.model.vo.BaseProjectVo;
import net.zlw.cloud.progressPayment.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.vo.ProgressListVo;
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

    @Select("SELECT\n" +
            "p.id id,\n" +
            "b.cea_num ceaNum," +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "b.progress_payment_status progressPaymentStatus,\n" +
            "b.district district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_category projectCategory,\n" +
            "b.project_nature projectNature,\n" +
            "b.design_category designCategory,\n" +
            "b.water_supply_type waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "(select member_name username from member_manage where id = p.founder_id) username,\n" +
            "bt.outsourcing outsourcing,\n" +
            "bt.name_of_cost_unit nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "p.contract_amount contractAmount,\n" +
            "pt.total_payment_amount totalPaymentAmount,\n" +
            "pt.accumulative_payment_proportion accumulativePaymentProportion,\n" +
            "p.current_payment_Information currentPaymentInformation,\n" +
            "p.current_payment_ratio currentPaymentRatio,\n" +
            "pt.cumulative_number_payment cumulativeNumberPayment,\n" +
            "p.receiving_time receiptTime,\n" +
            "p.compile_time compileTime\n" +
            "from progress_payment_information p \n" +
            "LEFT JOIN base_project b on p.base_project_id = b.id \n" +
            "LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN progress_payment_total_payment pt on p.id = pt.progress_payment_id\n" +
            "where\n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and \n" +
            "(p.project_type = #{projectType} or #{projectType} = '') and\n" +
            "(p.receiving_time > #{startTime} or #{startTime} = '') and \n" +
            "(p.receiving_time < #{endTime} or #{endTime} = '') and \n" +
            "(p.compile_time > #{startTime} or #{startTime} = '') and \n" +
            "(p.compile_time < #{endTime} or #{endTime} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or \n" +
            "b.project_num like concat('%',#{keyword},'%') or \n" +
            "b.project_name like concat ('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat ('%',#{keyword},'%') or \n" +
            "b.customer_name like concat ('%',#{keyword},'%') or \n" +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')\n" +
            ") and \n" +
            "(b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')")
    List<ProgressListVo> searchAllProgress(PageVo pageVo);
}

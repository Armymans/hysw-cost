package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
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
            "SELECT \n" +
                    "current_payment_Information\n" +
                    "FROM \n" +
                    "progress_payment_information\n" +
                    "WHERE\n" +
                    "base_project_id = #{id}\n" +
                    "GROUP BY\n" +
                    "base_project_id\n" +
                    "ORDER BY\n" +
                    "create_time desc"
    )
    List<String> NewcurrentPaymentInformation(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(current_payment_Information)\n" +
                    "FROM\n" +
                    "progress_payment_information\n" +
                    "GROUP BY\n" +
                    "base_project_id\n" +
                    "HAVING\n" +
                    "base_project_id = #{id}"
    )
    List<String> SumcurrentPaymentInformation(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(current_payment_ratio)\n" +
                    "FROM\n" +
                    "progress_payment_information\n" +
                    "GROUP BY\n" +
                    "base_project_id\n" +
                    "HAVING\n" +
                    "base_project_id = #{id}"
    )
    String currentPaymentRatio(@Param("id") String id);

    @Select(
            "SELECT\n" +
                    "SUM(cumulative_payment_times)\n" +
                    "FROM\n" +
                    "progress_payment_information\n" +
                    "GROUP BY\n" +
                    "base_project_id\n" +
                    "HAVING\n" +
                    "base_project_id = #{id}"
    )
    String cumulativePaymentTimes(@Param("id") String id);

    @Select("SELECT\n" +
            "p.id id,\n" +
            "b.id baseId,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus,\n" +
            "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_category projectCategory,\n" +
            "( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType,\n" +
            "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,\n" +
            "(\n" +
            "CASE\n" +
            "b.design_category \n" +
            "WHEN '1' THEN\n" +
            "'市政管道' \n" +
            "WHEN '2' THEN\n" +
            "'管网改造' \n" +
            "WHEN '3' THEN\n" +
            "'新建小区' \n" +
            "WHEN '4' THEN\n" +
            "'二次供水项目' \n" +
            "WHEN '5' THEN\n" +
            "'工商户' \n" +
            "WHEN '6' THEN\n" +
            "'居民装接水' \n" +
            "WHEN '7' THEN\n" +
            "'行政事业' \n" +
            "END \n" +
            ") AS designCategory,\n" +
            "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "(select member_name username from member_manage where id = p.founder_id) username,\n" +
            "( CASE bt.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing,\n" +
            "bt.name_of_cost_unit nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "p.contract_amount contractAmount,\n" +
            "pt.total_payment_amount totalPaymentAmount,\n" +
            "pt.accumulative_payment_proportion accumulativePaymentProportion,\n" +
            "p.current_payment_Information currentPaymentInformation,\n" +
            "p.current_payment_ratio currentPaymentRatio,\n" +
            "pt.cumulative_number_payment cumulativeNumberPayment,\n" +
            "p.receiving_time receivingTime,\n" +
            "p.compile_time compileTime\n" +
            "from progress_payment_information p \n" +
            "LEFT JOIN base_project b on p.base_project_id = b.id \n" +
            "LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN progress_payment_total_payment pt on p.id = pt.progress_payment_id\n" +
            "LEFT JOIN audit_info a on p.id = a.base_project_id\n" +
            "where\n" +
            "(p.del_flag = '0') and \n" +
            "(a.auditor_id = #{uid} or #{uid} = '') and\n" +
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
            "(b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')" +
            "ORDER BY\n" +
            "p.create_time desc")
    List<ProgressListVo> searchAllProgress(PageVo pageVo);


    @Select("SELECT\n" +
            "p.id id,\n" +
            "b.id baseId,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "( CASE b.progress_payment_status WHEN '1' THEN '待审核' WHEN '2' THEN '处理中' WHEN '3' THEN '未通过' WHEN '4' THEN '待确认' WHEN '5' THEN '进行中' WHEN '6' THEN '已完成' END )as  progressPaymentStatus,\n" +
            "( CASE b.district WHEN '1' THEN '芜湖' WHEN '2' THEN '马鞍山' WHEN '3' THEN '江北' WHEN '4' THEN '吴江' END ) AS district,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_category projectCategory,\n" +
            "( CASE p.project_type WHEN '1' THEN '合同内进度款支付' WHEN '2' THEN '合同外进度款支付' END ) AS projectType,\n" +
            "( CASE b.project_nature WHEN '1' THEN '新建' WHEN '2' THEN '改造' END ) AS projectNature,\n" +
            "(\n" +
            "CASE\n" +
            "b.design_category \n" +
            "WHEN '1' THEN\n" +
            "'市政管道' \n" +
            "WHEN '2' THEN\n" +
            "'管网改造' \n" +
            "WHEN '3' THEN\n" +
            "'新建小区' \n" +
            "WHEN '4' THEN\n" +
            "'二次供水项目' \n" +
            "WHEN '5' THEN\n" +
            "'工商户' \n" +
            "WHEN '6' THEN\n" +
            "'居民装接水' \n" +
            "WHEN '7' THEN\n" +
            "'行政事业' \n" +
            "END \n" +
            ") AS designCategory,\n" +
            "( CASE b.water_supply_type WHEN '1' THEN '直供水' WHEN '2' THEN '二次供水' END ) AS waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "(select member_name username from member_manage where id = p.founder_id) username,\n" +
            "( CASE bt.outsourcing WHEN '1' THEN '是' WHEN '2' THEN '否' END )as outsourcing,\n" +
            "bt.name_of_cost_unit nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "p.contract_amount contractAmount,\n" +
            "pt.total_payment_amount totalPaymentAmount,\n" +
            "pt.accumulative_payment_proportion accumulativePaymentProportion,\n" +
            "p.current_payment_Information currentPaymentInformation,\n" +
            "p.current_payment_ratio currentPaymentRatio,\n" +
            "pt.cumulative_number_payment cumulativeNumberPayment,\n" +
            "p.receiving_time receivingTime,\n" +
            "p.compile_time compileTime\n" +
            "from progress_payment_information p \n" +
            "LEFT JOIN base_project b on p.base_project_id = b.id \n" +
            "LEFT JOIN budgeting bt on p.base_project_id = bt.base_project_id\n" +
            "LEFT JOIN progress_payment_total_payment pt on p.id = pt.progress_payment_id\n" +
            "where\n" +
            "(p.del_flag = '0') and \n" +
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
            "(b.progress_payment_status = #{progressStatus} or #{progressStatus} = '')" +
            "ORDER BY\n" +
            "p.create_time desc")
    List<ProgressListVo> searchAllProgress1(PageVo pageVo);

    @Select(
            "SELECT \n" +
                    "amount_outsourcing\n" +
                    "FROM \n" +
                    "progress_payment_information s1,\n" +
                    "base_project s2\n" +
                    "where\n" +
                    "s1.base_project_id = s2.id\n" +
                    "and\n" +
                    "(s2.district=#{district} or  #{district}  = '')\n" +
                    "and\n" +
                    "s1.create_time>=#{startTime}\n" +
                    "and\n" +
                    "(s1.create_time<=#{endTime} or  #{endTime} = '')"
    )
    List<ProgressPaymentInformation> totalexpenditure(CostVo2 costVo2);
}

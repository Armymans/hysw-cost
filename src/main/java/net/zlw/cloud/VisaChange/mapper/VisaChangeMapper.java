package net.zlw.cloud.VisaChange.mapper;


import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.PageVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeListVo;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVo;
import net.zlw.cloud.designProject.model.CostVo2;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface VisaChangeMapper extends Mapper<VisaChange> {


    @Select("select " +
            " distinct v.id," +
            "v.up_and_down_mark upAndDownMark, " +
            "v.base_project_id baseProjectId," +
            "b.cea_num ceaNum, " +
            "b.project_num projectNum, " +
            "(case b.visa_status " +
            " when '1' then '待审核' " +
            " when '2' then '处理中' " +
            " when '3' then '未通过' " +
            " when '4' then '待确认' " +
            " when '5' then '进行中' " +
            " when '6' then '已完成' " +
            " end " +
            ") status, " +
            "(case b.district " +
            "  when '1' then '芜湖' " +
            "  when '2' then '马鞍山' " +
            "  when '3' then '江北' " +
            "  when '4' then '吴江' " +
            "  end " +
            ") district, " +
            "b.construction_unit constructionUnit, " +
            "(case b.project_nature " +
            "  when '1' then '新建' " +
            "  when '2' then '改造' " +
            "  end " +
            ") projectNature, " +
            "(case b.design_category " +
            "  when '1' then '市政管道' " +
            "  when '2' then '管网改造' " +
            "  when '3' then '新建小区' " +
            "  when '4' then '二次供水项目' " +
            "  when '5' then '工商户' " +
            "  when '6' then '居民装接水' " +
            "  when '7' then '行政事业' " +
            "  end " +
            ") designCategory, " +
            "(case b.water_supply_type " +
            " when '1' then '直供水' " +
            " when '2' then '二次供水' " +
            " end " +
            ") waterSupplyType, " +
            "b.customer_name customerName, " +
            "b.water_address waterAddress, " +
            "(case bt.outsourcing " +
            "  when '1' then '是' " +
            "  when '2' then '否' " +
            "  end " +
            ") outsourcing, " +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, " +
            "bt.amount_cost amountCost, " +
            "v.creator_id founderId  " +
            "from  " +
            "visa_change_information v  " +
            "LEFT JOIN base_project b on b.id = v.base_project_id  " +
            "LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id  " +
            "LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id " +
            "where  " +
            "(b.district = #{district} or #{district} = '') and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(v.create_time >= #{startTime} or #{startTime} = '') and  " +
            "(v.create_time <= #{endTime} or #{endTime} = '') and  " +
            "(v.compile_time >= #{startTime} or #{startTime} = '') and  " +
            "(v.compile_time <= #{endTime} or #{endTime} = '') and  " +
            "( " +
            "b.cea_num like concat('%',#{keyword},'%') or  " +
            "b.project_num like concat('%',#{keyword},'%') or " +
            "b.project_name like concat('%',#{keyword},'%') or  " +
            "b.construction_unit like concat('%',#{keyword},'%') or " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%') " +
            ") and  " +
            "b.del_flag = '0' and  " +
            "bt.del_flag = '0' and  " +
            "v.state = '0' and  " +
            "vv.state = '0'")
    List<VisaChangeListVo> findAllVisa(PageVo pageVo);

    @Select("     select   " +
            "      distinct IFNULL(v.id,v2.id) id,  " +
            "     v.up_and_down_mark upAndDownMark,   " +
            "     v.base_project_id baseProjectId,  " +
            "     b.cea_num ceaNum,   " +
            "     b.project_num projectNum,   " +
            "     (case b.visa_status   " +
            "      when '1' then '待审核'   " +
            "      when '2' then '处理中'   " +
            "      when '3' then '未通过'   " +
            "      when '4' then '待确认'   " +
            "      when '5' then '进行中'   " +
            "      when '6' then '已完成'   " +
            "      end   " +
            "     ) status,   " +
            "     (case b.district   " +
            "       when '1' then '芜湖'   " +
            "       when '2' then '马鞍山'   " +
            "       when '3' then '江北'   " +
            "       when '4' then '吴江'   " +
            "       end   " +
            "     ) district,   " +
            "     b.construction_unit constructionUnit,   " +
            "     (case b.project_nature   " +
            "       when '1' then '新建'   " +
            "       when '2' then '改造'   " +
            "       end   " +
            "     ) projectNature,   " +
            "     (case b.design_category   " +
            "       when '1' then '市政管道'   " +
            "       when '2' then '管网改造'   " +
            "       when '3' then '新建小区'   " +
            "       when '4' then '二次供水项目'   " +
            "       when '5' then '工商户'   " +
            "       when '6' then '居民装接水'   " +
            "       when '7' then '行政事业'   " +
            "       end   " +
            "     ) designCategory,   " +
            "     (case b.water_supply_type   " +
            "      when '1' then '直供水'   " +
            "      when '2' then '二次供水'   " +
            "      end   " +
            "     ) waterSupplyType,   " +
            "     b.customer_name customerName,   " +
            "     b.water_address waterAddress,   " +
            "     (case bt.outsourcing   " +
            "       when '1' then '是'   " +
            "       when '2' then '否'   " +
            "       end   " +
            "     ) outsourcing,   " +
            "     (select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit,   " +
            "     bt.amount_cost amountCost,   " +
            "     v.creator_id founderId,  " +
            "     v.contract_amount contractAmountShang,  " +
            "     v.cumulative_change_amount amountVisaChangeAddShang,  " +
            "     v.proportion_contract proportionContractShang,  " +
            "     v2.contract_amount contractAmountXia,  " +
            "     v2.cumulative_change_amount amountVisaChangeAddXia,  " +
            "     v2.proportion_contract proportionContractXia,  " +
            "     v.amount_visa_change currentShang,  " +
            "     v2.amount_visa_change currentXia,  " +
            "     IFNULL(v.completion_time,v2.completion_time) createTime,  " +
            "     IFNULL(v.compile_time,v2.compile_time) compileTime  " +
            "     from    " +
            "     base_project b    " +
            "     LEFT JOIN visa_change_information v on b.id = v.base_project_id    " +
            "     LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id    " +
            "     LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id   " +
            "     left join visa_change_information v2 on b.id = v2.base_project_id   " +
            "     LEFT JOIN audit_info a on a.base_project_id = IFNULL(v.id,v2.id)  " +
            "     where    " +
            "     v.up_and_down_mark = '0' and   " +
            "     v2.up_and_down_mark = '1' and   " +
            "     a.audit_result = '0' and  " +
            "     (b.district = #{district} or #{district} = '') and    " +
            "     (b.project_nature = #{projectNature} or #{projectNature} = '') and    " +
            "     (v.create_time >= #{startTime} or #{startTime} = '') and    " +
            "     (v.create_time <= #{endTime} or #{endTime} = '') and    " +
            "     (v.compile_time >= #{startTime} or #{startTime} = '') and    " +
            "     (v.compile_time <= #{endTime} or #{endTime} = '') and   " +
            "     (b.visa_status = #{status} or #{status} = '') and   " +
            "     (   " +
            "     b.cea_num like concat('%',#{keyword},'%') or    " +
            "     b.project_num like concat('%',#{keyword},'%') or   " +
            "     b.project_name like concat('%',#{keyword},'%') or    " +
            "     b.construction_unit like concat('%',#{keyword},'%') or   " +
            "     b.customer_name like concat ('%',#{keyword},'%') or    " +
            "     bt.name_of_cost_unit like concat  ('%',#{keyword},'%')   " +
            "     ) and    " +
            "     b.del_flag = '0' and    " +
            "     bt.del_flag = '0' and    " +
            "     v.state = '0' and    " +
            "     vv.state = '0' and " +
            "     v2.state = '0'  ")
    List<VisaChangeListVo> findAllVisaCheckLeader(PageVo pageVo);

    @Select("select \n" +
            "             distinct IFNULL(v.id,v2.id) id,\n" +
            "            v.up_and_down_mark upAndDownMark, \n" +
            "            v.base_project_id baseProjectId,\n" +
            "            b.cea_num ceaNum, \n" +
            "            b.project_num projectNum, \n" +
            "            (case b.visa_status \n" +
            "             when '1' then '待审核' \n" +
            "             when '2' then '处理中' \n" +
            "             when '3' then '未通过' \n" +
            "             when '4' then '待确认' \n" +
            "             when '5' then '进行中' \n" +
            "             when '6' then '已完成' \n" +
            "             end \n" +
            "            ) status, \n" +
            "            (case b.district \n" +
            "              when '1' then '芜湖' \n" +
            "              when '2' then '马鞍山' \n" +
            "              when '3' then '江北' \n" +
            "              when '4' then '吴江' \n" +
            "              end \n" +
            "            ) district, \n" +
            "            b.construction_unit constructionUnit, \n" +
            "            (case b.project_nature \n" +
            "              when '1' then '新建' \n" +
            "              when '2' then '改造' \n" +
            "              end \n" +
            "            ) projectNature, \n" +
            "            (case b.design_category \n" +
            "              when '1' then '市政管道' \n" +
            "              when '2' then '管网改造' \n" +
            "              when '3' then '新建小区' \n" +
            "              when '4' then '二次供水项目' \n" +
            "              when '5' then '工商户' \n" +
            "              when '6' then '居民装接水' \n" +
            "              when '7' then '行政事业' \n" +
            "              end \n" +
            "            ) designCategory, \n" +
            "            (case b.water_supply_type \n" +
            "             when '1' then '直供水' \n" +
            "             when '2' then '二次供水' \n" +
            "             end \n" +
            "            ) waterSupplyType, \n" +
            "            b.customer_name customerName, \n" +
            "            b.water_address waterAddress, \n" +
            "            (case bt.outsourcing \n" +
            "              when '1' then '是' \n" +
            "              when '2' then '否' \n" +
            "              end \n" +
            "            ) outsourcing, \n" +
            "            (select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, \n" +
            "            bt.amount_cost amountCost, \n" +
            "            v.creator_id founderId,\n" +
            "\t\t\t\t\t\tv.contract_amount contractAmountShang,\n" +
            "\t\t\t\t\t\tv.cumulative_change_amount amountVisaChangeAddShang,\n" +
            "\t\t\t\t\t\tv.proportion_contract proportionContractShang,\n" +
            "\t\t\t\t\t\tv2.contract_amount contractAmountXia,\n" +
            "\t\t\t\t\t\tv2.cumulative_change_amount amountVisaChangeAddXia,\n" +
            "\t\t\t\t\t\tv2.proportion_contract proportionContractXia,\n" +
            "\t\t\t\t\t\tv.amount_visa_change currentShang,\n" +
            "\t\t\t\t\t\tv2.amount_visa_change currentXia,\n" +
            "\t\t\t\t\t\tIFNULL(v.completion_time,v2.completion_time) createTime,\n" +
            "\t\t\t\t\t\tIFNULL(v.compile_time,v2.compile_time) compileTime\n" +
            "            from  \n" +
            "\t\t\t\t\t\tbase_project b  \n" +
            "            LEFT JOIN visa_change_information v on b.id = v.base_project_id  \n" +
            "            LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id  \n" +
            "            LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id \n" +
            "\t\t\t\t\t\tleft join visa_change_information v2 on b.id = v2.base_project_id \n" +
            "\t\t\t\t\t\tLEFT JOIN audit_info a on a.base_project_id = IFNULL(v2.id,v.id)\n" +
            "            where  \n" +
            "\t\t\t\t\t\tv.up_and_down_mark = '0' and \n" +
            "\t\t\t\t\t\tv2.up_and_down_mark = '1' and \n" +
            "\t\t\t\t\t\ta.audit_result = '0' and ( \n" +
            "\t\t\t\t\t\ta.auditor_id = #{userId} or \n" +
            "\t\t\t\t\t\tIFNULL(v2.creator_id,v.creator_id) = #{userId} ) and \n" +
            "            (b.district = #{district} or #{district} = '') and  \n" +
            "            (b.project_nature = #{projectNature} or #{projectNature} = '') and  \n" +
            "            (v.create_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.create_time <= #{endTime} or #{endTime} = '') and  \n" +
            "            (v.compile_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.compile_time <= #{endTime} or #{endTime} = '') and \n" +
            "\t\t\t\t\t\t(b.visa_status = #{status} or #{status} = '') and \n" +
            "            ( \n" +
            "            b.cea_num like concat('%',#{keyword},'%') or  \n" +
            "            b.project_num like concat('%',#{keyword},'%') or \n" +
            "            b.project_name like concat('%',#{keyword},'%') or  \n" +
            "            b.construction_unit like concat('%',#{keyword},'%') or \n" +
            "            b.customer_name like concat ('%',#{keyword},'%') or  \n" +
            "            bt.name_of_cost_unit like concat  ('%',#{keyword},'%') \n" +
            "            ) and  \n" +
            "            b.del_flag = '0' and  \n" +
            "            bt.del_flag = '0' and  \n" +
            "            v.state = '0' and  \n" +
            "            vv.state = '0' and " +
            "            v2.state = '0' ")
    List<VisaChangeListVo> findAllVisaCheckStaff(PageVo pageVo);

    @Select("\t\tselect \n" +
            "             distinct IFNULL(v.id,v2.id) id,\n" +
            "            v.up_and_down_mark upAndDownMark, \n" +
            "            v.base_project_id baseProjectId,\n" +
            "            b.cea_num ceaNum, \n" +
            "            b.project_num projectNum, \n" +
            "            (case b.visa_status \n" +
            "             when '1' then '待审核' \n" +
            "             when '2' then '处理中' \n" +
            "             when '3' then '未通过' \n" +
            "             when '4' then '待确认' \n" +
            "             when '5' then '进行中' \n" +
            "             when '6' then '已完成' \n" +
            "             end \n" +
            "            ) status, \n" +
            "            (case b.district \n" +
            "              when '1' then '芜湖' \n" +
            "              when '2' then '马鞍山' \n" +
            "              when '3' then '江北' \n" +
            "              when '4' then '吴江' \n" +
            "              end \n" +
            "            ) district, \n" +
            "            b.construction_unit constructionUnit, \n" +
            "            (case b.project_nature \n" +
            "              when '1' then '新建' \n" +
            "              when '2' then '改造' \n" +
            "              end \n" +
            "            ) projectNature, \n" +
            "            (case b.design_category \n" +
            "              when '1' then '市政管道' \n" +
            "              when '2' then '管网改造' \n" +
            "              when '3' then '新建小区' \n" +
            "              when '4' then '二次供水项目' \n" +
            "              when '5' then '工商户' \n" +
            "              when '6' then '居民装接水' \n" +
            "              when '7' then '行政事业' \n" +
            "              end \n" +
            "            ) designCategory, \n" +
            "            (case b.water_supply_type \n" +
            "             when '1' then '直供水' \n" +
            "             when '2' then '二次供水' \n" +
            "             end \n" +
            "            ) waterSupplyType, \n" +
            "            b.customer_name customerName, \n" +
            "            b.water_address waterAddress, \n" +
            "            (case bt.outsourcing \n" +
            "              when '1' then '是' \n" +
            "              when '2' then '否' \n" +
            "              end \n" +
            "            ) outsourcing, \n" +
            "            (select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, \n" +
            "            bt.amount_cost amountCost, \n" +
            "            v.creator_id founderId,\n" +
            "\t\t\t\t\t\tv.contract_amount contractAmountShang,\n" +
            "\t\t\t\t\t\tv.cumulative_change_amount amountVisaChangeAddShang,\n" +
            "\t\t\t\t\t\tv.proportion_contract proportionContractShang,\n" +
            "\t\t\t\t\t\tv2.contract_amount contractAmountXia,\n" +
            "\t\t\t\t\t\tv2.cumulative_change_amount amountVisaChangeAddXia,\n" +
            "\t\t\t\t\t\tv2.proportion_contract proportionContractXia,\n" +
            "\t\t\t\t\t\tv.amount_visa_change currentShang,\n" +
            "\t\t\t\t\t\tv2.amount_visa_change currentXia,\n" +
            "\t\t\t\t\t\tIFNULL(v.completion_time,v2.completion_time) createTime,\n" +
            "\t\t\t\t\t\tIFNULL(v.compile_time,v2.compile_time) compileTime\n" +
            "            from  \n" +
            "\t\t\t\t\t\tbase_project b\n" +
            "             \n" +
            "            LEFT JOIN visa_change_information v  on b.id = v.base_project_id  \n" +
            "            LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id  \n" +
            "            LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id \n" +
            "\t\t\t\t\t\tleft join visa_change_information v2 on b.id = v2.base_project_id  \n" +
            "            where  \n" +
            "\t\t\t\t\t\tv.up_and_down_mark = '0' and \n" +
            "\t\t\t\t\t\tv2.up_and_down_mark = '1' and \n" +
            "\t\t\t\t\t\tIFNULL(v2.creator_id,v.creator_id) = #{userId} and \n" +
            "            (b.district = #{district} or #{district} = '') and  \n" +
            "            (b.project_nature = #{projectNature} or #{projectNature} = '') and  \n" +
            "            (v.create_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.create_time <= #{endTime} or #{endTime} = '') and  \n" +
            "            (v.compile_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.compile_time <= #{endTime} or #{endTime} = '') and \n" +
            "\t\t\t\t\t\t(b.visa_status = #{status} or #{status} = '') and \n" +
            "            ( \n" +
            "            b.cea_num like concat('%',#{keyword},'%') or  \n" +
            "            b.project_num like concat('%',#{keyword},'%') or \n" +
            "            b.project_name like concat('%',#{keyword},'%') or  \n" +
            "            b.construction_unit like concat('%',#{keyword},'%') or \n" +
            "            b.customer_name like concat ('%',#{keyword},'%') or  \n" +
            "            bt.name_of_cost_unit like concat  ('%',#{keyword},'%') \n" +
            "            ) and  \n" +
            "            b.del_flag = '0' and  \n" +
            "            bt.del_flag = '0' and  \n" +
            "            v.state = '0' and  \n" +
            "            vv.state = '0' and " +
            "            v2.state = '0' ")
    List<VisaChangeListVo> findAllVisaProcessing(PageVo pageVo);

    @Select("select \n" +
            "             distinct IFNULL(v.id,v2.id) id,\n" +
            "            v.up_and_down_mark upAndDownMark, \n" +
            "            v.base_project_id baseProjectId,\n" +
            "            b.cea_num ceaNum, \n" +
            "            b.project_num projectNum, \n" +
            "            (case b.visa_status \n" +
            "             when '1' then '待审核' \n" +
            "             when '2' then '处理中' \n" +
            "             when '3' then '未通过' \n" +
            "             when '4' then '待确认' \n" +
            "             when '5' then '进行中' \n" +
            "             when '6' then '已完成' \n" +
            "             end \n" +
            "            ) status, \n" +
            "            (case b.district \n" +
            "              when '1' then '芜湖' \n" +
            "              when '2' then '马鞍山' \n" +
            "              when '3' then '江北' \n" +
            "              when '4' then '吴江' \n" +
            "              end \n" +
            "            ) district, \n" +
            "            b.construction_unit constructionUnit, \n" +
            "            (case b.project_nature \n" +
            "              when '1' then '新建' \n" +
            "              when '2' then '改造' \n" +
            "              end \n" +
            "            ) projectNature, \n" +
            "            (case b.design_category \n" +
            "              when '1' then '市政管道' \n" +
            "              when '2' then '管网改造' \n" +
            "              when '3' then '新建小区' \n" +
            "              when '4' then '二次供水项目' \n" +
            "              when '5' then '工商户' \n" +
            "              when '6' then '居民装接水' \n" +
            "              when '7' then '行政事业' \n" +
            "              end \n" +
            "            ) designCategory, \n" +
            "            (case b.water_supply_type \n" +
            "             when '1' then '直供水' \n" +
            "             when '2' then '二次供水' \n" +
            "             end \n" +
            "            ) waterSupplyType, \n" +
            "            b.customer_name customerName, \n" +
            "            b.water_address waterAddress, \n" +
            "            (case bt.outsourcing \n" +
            "              when '1' then '是' \n" +
            "              when '2' then '否' \n" +
            "              end \n" +
            "            ) outsourcing, \n" +
            "            (select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit, \n" +
            "            bt.amount_cost amountCost, \n" +
            "            v.creator_id founderId,\n" +
            "\t\t\t\t\t\tv.contract_amount contractAmountShang,\n" +
            "\t\t\t\t\t\tv.cumulative_change_amount amountVisaChangeAddShang,\n" +
            "\t\t\t\t\t\tv.proportion_contract proportionContractShang,\n" +
            "\t\t\t\t\t\tv2.contract_amount contractAmountXia,\n" +
            "\t\t\t\t\t\tv2.cumulative_change_amount amountVisaChangeAddXia,\n" +
            "\t\t\t\t\t\tv2.proportion_contract proportionContractXia,\n" +
            "\t\t\t\t\t\tv.amount_visa_change currentShang,\n" +
            "\t\t\t\t\t\tv2.amount_visa_change currentXia,\n" +
            "\t\t\t\t\t\tIFNULL(v.completion_time,v2.completion_time) createTime,\n" +
            "\t\t\t\t\t\tIFNULL(v.compile_time,v2.compile_time) compileTime\n" +
            "            from  \n" +
            "\t\t\t\t\t\tbase_project b\n" +
            "             \n" +
            "            LEFT JOIN visa_change_information v  on b.id = v.base_project_id  \n" +
            "            LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id  \n" +
            "            LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id \n" +
            "\t\t\t\t\t\tleft join visa_change_information v2 on b.id = v2.base_project_id  \n" +
            "            where  \n" +
            "\t\t\t\t\t\tv.up_and_down_mark = '0' and \n" +
            "\t\t\t\t\t\tv2.up_and_down_mark = '1' and \n" +
            "            (b.district = #{district} or #{district} = '') and  \n" +
            "            (b.project_nature = #{projectNature} or #{projectNature} = '') and  \n" +
            "            (v.create_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.create_time <= #{endTime} or #{endTime} = '') and  \n" +
            "            (v.compile_time >= #{startTime} or #{startTime} = '') and  \n" +
            "            (v.compile_time <= #{endTime} or #{endTime} = '') and \n" +
            "\t\t\t\t\t\t(b.visa_status = #{status} or #{status} = '') and \n" +
            "            ( \n" +
            "            b.cea_num like concat('%',#{keyword},'%') or  \n" +
            "            b.project_num like concat('%',#{keyword},'%') or \n" +
            "            b.project_name like concat('%',#{keyword},'%') or  \n" +
            "            b.construction_unit like concat('%',#{keyword},'%') or \n" +
            "            b.customer_name like concat ('%',#{keyword},'%') or  \n" +
            "            bt.name_of_cost_unit like concat  ('%',#{keyword},'%') \n" +
            "            ) and  \n" +
            "            b.del_flag = '0' and  \n" +
            "            bt.del_flag = '0' and  \n" +
            "            v.state = '0' and  \n" +
            "            vv.state = '0' and " +
            "            v2.state = '0' ")
    List<VisaChangeListVo> findAllVisaSuccess(PageVo pageVo);
}

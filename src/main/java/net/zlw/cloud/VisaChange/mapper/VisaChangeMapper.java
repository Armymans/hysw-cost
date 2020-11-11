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


    @Select("select\n" +
            "distinct v.id,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "(case b.visa_status\n" +
            "\twhen '1' then '待审核'\n" +
            "\twhen '2' then '处理中'\n" +
            "\twhen '3' then '未通过'\n" +
            "\twhen '4' then '待确认'\n" +
            "\twhen '5' then '进行中'\n" +
            "\twhen '6' then '已完成'\n" +
            "\tend\n" +
            ") status,\n" +
            "(case b.district\n" +
            "\t\twhen '1' then '芜湖'\n" +
            "\t\twhen '2' then '马鞍山'\n" +
            "\t\twhen '3' then '江北'\n" +
            "\t\twhen '4' then '吴江'\n" +
            "\t\tend\n" +
            ") district,\n" +
            "b.construction_unit constructionUnit,\n" +
            "(case b.project_nature\n" +
            "\t\twhen '1' then '新建'\n" +
            "\t\twhen '2' then '改造'\n" +
            "\t\tend\n" +
            ") projectNature,\n" +
            "(case b.design_category\n" +
            "\t\twhen '1' then '市政管道'\n" +
            "\t\twhen '2' then '管网改造'\n" +
            "\t\twhen '3' then '新建小区'\n" +
            "\t\twhen '4' then '二次供水项目'\n" +
            "\t\twhen '5' then '工商户'\n" +
            "\t\twhen '6' then '居民装接水'\n" +
            "\t\twhen '7' then '行政事业'\n" +
            "\t\tend\n" +
            ") designCategory,\n" +
            "(case b.water_supply_type\n" +
            "\twhen '1' then '直供水'\n" +
            "\twhen '2' then '二次供水'\n" +
            "\tend\n" +
            ") waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "b.water_address waterAddress,\n" +
            "(case bt.outsourcing\n" +
            "\t\twhen '1' then '是'\n" +
            "\t\twhen '2' then '否'\n" +
            "\t\tend\n" +
            ") outsourcing,\n" +
            "(select cost_unit_name from cost_unit_management cum where cum.id = bt.name_of_cost_unit) nameOfCostUnit,\n" +
            "bt.amount_cost amountCost,\n" +
            "v.contract_amount contractAmountShang,\n" +
            "v.cumulative_change_amount amountVisaChangeAddShang,\n" +
            "v.proportion_contract proportionContractShang,\n" +
            "v2.contract_amount contractAmountXia,\n" +
            "v2.cumulative_change_amount amountVisaChangeAddXia,\n" +
            "v2.proportion_contract proportionContractXia,\n" +
            "v.amount_visa_change currentShang,\n" +
            "v2.amount_visa_change currentXia\n" +
            "from \n" +
            "visa_change_information v \n" +
            "LEFT JOIN base_project b on b.id = v.base_project_id and v.up_and_down_mark = '0'\n" +
            "LEFT JOIN budgeting bt on v.base_project_id = bt.base_project_id \n" +
            "LEFT JOIN visa_apply_change_information vv on vv.base_project_id  = v.base_project_id\n" +
            "left join visa_change_information v2 on v.base_project_id = v2.base_project_id and v2.up_and_down_mark = '1'\n" +
            "where \n" +
            "(b.district = #{district} or #{district} = '') and \n" +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and \n" +
            "(v.create_time >= #{startTime} or #{startTime} = '') and \n" +
            "(v.create_time <= #{endTime} or #{endTime} = '') and \n" +
            "(v.compile_time >= #{startTime} or #{startTime} = '') and \n" +
            "(v.compile_time <= #{endTime} or #{endTime} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or \n" +
            "b.project_num like concat('%',#{keyword},'%') or\n" +
            "b.project_name like concat('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat('%',#{keyword},'%') or\n" +
            "b.customer_name like concat ('%',#{keyword},'%') or \n" +
            "bt.name_of_cost_unit like concat  ('%',#{keyword},'%')\n" +
            ") and \n" +
            "b.del_flag = '0' and \n" +
            "bt.del_flag = '0' and \n" +
            "v.state = '0' and \n" +
            "vv.state = '0'")
    List<VisaChangeListVo> findAllVisa(PageVo pageVo);
}

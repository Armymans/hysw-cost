package net.zlw.cloud.VisaChange.mapper;


import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.VisaChange.model.vo.VisaChangeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface VisaChangeMapper extends Mapper<VisaChange> {


    @Select("SELECT\n" +
            "                   s2.id,\n" +
            "                    s3.amount_cost,\n" +
            "                    s1.contract_amount contractAmount,\n" +
            "                    s2.cea_num,\n" +
            "                    s2.project_name,\n" +
            "                    s2.district,\n" +
            "                    s1.amount_visa_change amountVisaChange,\n"+
            "                    s1.proportion_contract proportionContract,\n"+
            "                    s2.construction_unit,\n" +
            "                    s2.project_nature,\n" +
            "                    s2.design_category,\n" +
            "                    s2.water_supply_type,\n" +
            "                    s2.customer_name,\n" +
            "                    s2.water_address,\n" +
            "                    s2.proposer,\n" +
            "                    s1.outsourcing,\n" +
            "                    s3.name_of_cost_unit,\n" +
            "                    s1.`status`,\n" +
            "                    s1.create_time,\n" +
            "                    s1.completion_time,\n" +
            "                    s2.project_name \n" +
            "                    FROM\n" +
            "                    visa_change_information s1 LEFT JOIN base_project s2 on s1.base_project_id = s2.id\n" +
            "                    left join budgeting s3 on s2.id = s3.base_project_id\n" +
            "                    where 1=1 and\n" +
            "                     (s2.district = #{district} or #{district} = '' )\n" +
            "                    and\n" +
            "                     (s2.project_nature = #{projectNature} or #{projectNature} = '')\n" +
            "                    and\n" +
            "                     (s1.`status` =#{status} or #{status} = '')\n" +
            "                  \n" +
            "                   and\n" +
            "                    s1.create_time>= #{createStartTime}\n" +
            "                    and \n" +
            "                    (s1.create_time<=#{createEndTime} or #{createEndTime}  ='')\n" +
            "                    and s1.creator_id = #{loginUserId} and (" +
            "                    s2.cea_num like CONCAT('%',#{keyword},'%') or\n" +
            "                    s2.project_num like CONCAT('%',#{keyword},'%') or\n" +
            "                    s2.project_name like  CONCAT('%',#{keyword},'%') \n" +
            "            ) ")
    List<VisaChangeVO> findAll(VisaChangeVO visaChangeVO);

    /***
     * 删除
     * @param id
     */
    @Update("update  visa_change_information set state = '1' where id = #{id}")
    void deleteById(@Param("id") String id);

    @Select("select * from visa_change_information where id = #{id}")
    VisaChange selectById(@Param("id") String id);

    @Select("SELECT * FROM visa_change_information where base_project_id =#{id} ")
    List<VisaChange> findByBaseProjectId(@Param("id") String id);
}

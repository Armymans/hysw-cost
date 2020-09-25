package net.zlw.cloud.progressPayment.mapper;


import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface BaseProjectDao extends Mapper<BaseProject> {

    @Select("select * from base_project b  " +
            "LEFT JOIN budgeting bb on  b.id = bb.base_project_id " +
            "LEFT JOIN settlement_audit_information sai on b.id = sai.base_project_id " +
            "LEFT JOIN last_settlement_review l on b.id = l.base_project_id " +
            "LEFT JOIN audit_info au on b.id = au.base_project_id  " +
            "where  " +
            "(b.settle_accounts_status = #{settleAccountsStatus} or #{settleAccountsStatus} = '' ) and  " +
            "(b.district = #{district} or #{district} = '' ) and  " +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and  " +
            "(b.sa_whether_account = #{saWhetherAccount} or #{saWhetherAccount} = '') and  " +
            "(IFNULL(l.take_time,sai.take_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(l.take_time,sai.take_time) < #{endTime} or #{endTime} = '') and  " +
            "(IFNULL(l.compile_time,sai.compile_time) > #{startTime} or #{startTime} = '') and  " +
            "(IFNULL(l.compile_time,sai.compile_time) < #{endTime} or #{endTime} = '') and  " +
            "(b.cea_num like concat('%',#{keyword},'%') or  " +
            "b.project_num like concat('%',#{keyword},'%')  or " +
            "b.project_name like concat ('%',#{keyword},'%') or  " +
            "b.construction_unit like concat ('%',#{keyword},'%') or  " +
            "b.customer_name like concat ('%',#{keyword},'%') or  " +
            "bb.name_of_cost_unit like concat  ('%',#{keyword},'%'))")
    List<AccountsVo> findAllAccounts(PageVo PageVo);
}

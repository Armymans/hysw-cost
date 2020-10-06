package net.zlw.cloud.followAuditing.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.followAuditing.model.vo.ReturnTrackVo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TrackAuditInfoDao extends Mapper<TrackAuditInfo> {
    @Select("select \n" +
            "tai.id id,\n" +
            "b.cea_num ceaNum,\n" +
            "b.project_num projectNum,\n" +
            "b.project_name projectName,\n" +
            "b.track_status trackStatus,\n" +
            "b.district district,\n" +
            "b.construction_unit constructionUnit,\n" +
            "b.project_nature projectNature,\n" +
            "b.design_category designCategory,\n" +
            "b.water_supply_type waterSupplyType,\n" +
            "b.customer_name customerName,\n" +
            "b.water_address waterAddress,\n" +
            "b.construction_organization constructionOrganization,\n" +
            "(select writter from track_monthly where track_id = tai.id limit 0,1) writter,\n" +
            "(select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) fillTime ,\n" +
            "tai.outsource outsource, \n" +
            "tai.audit_unit_name_id auditUnitNameId,\n" +
            "tai.cea_total_money ceaTotalMoney\n" +
            "from track_audit_info tai \n" +
            "LEFT JOIN base_project b on tai.base_project_id = b.id\n" +
            "LEFT JOIN track_application_info ta on ta.track_audit = tai.id\n" +
            "LEFT JOIN track_monthly tm on tm.track_id = tai.id\n" +
            "LEFT JOIN audit_info ai on ai.base_project_id = tai.id\n" +
            "where\n" +
            "(b.district = #{district} or #{district} = '' ) and \n" +
            "(b.project_nature = #{projectNature} or #{projectNature} = '') and \n" +
            "(b.water_supply_type = #{waterSupplyType} or #{waterSupplyType} = '' ) and \n" +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) > #{startTime} or #{startTime} = '') and \n" +
            "((select fill_time from track_monthly where track_id = tai.id ORDER BY fill_time desc limit 0,1) < #{endTIme} or #{endTIme} = '') and \n" +
            "(b.design_category = #{designCategory} or #{designCategory} = '') and \n" +
            "(b.construction_organization = #{constructionOrganization} or #{constructionOrganization} = '') and \n" +
            "(\n" +
            "b.cea_num like concat('%',#{keyword},'%') or \n" +
            "b.project_num like concat('%',#{keyword},'%') or \n" +
            "b.project_name like concat ('%',#{keyword},'%') or \n" +
            "b.construction_unit like concat ('%',#{keyword},'%') or \n" +
            "b.customer_name like concat ('%',#{keyword},'%') or \n" +
            "tai.audit_unit_name_id like concat  ('%',#{keyword},'%') or \n" +
            "b.design_category like concat  ('%',#{keyword},'%') or \n" +
            "b.construction_organization like concat  ('%',#{keyword},'%') \n" +
            ") and \n" +
            "tai.`status` = \"0\" and \n" +
            "b.del_flag = \"0\" and \n" +
            "ta.`state` = \"0\" and \n" +
            "tm.`status` = \"0\" and \n" +
            "ai.`status` = \"0\"\n")
    List<ReturnTrackVo> selectTrackList(PageVo pageVo);

    @Select(
            "SELECT \n" +
                    "outsource_money\n" +
                    "FROM \n" +
                    "track_audit_info s1,\n" +
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
    List<TrackAuditInfo> totalexpenditure(CostVo2 costVo2);
}

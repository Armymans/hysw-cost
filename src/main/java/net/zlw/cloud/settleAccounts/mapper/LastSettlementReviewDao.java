package net.zlw.cloud.settleAccounts.mapper;


import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface LastSettlementReviewDao extends Mapper<LastSettlementReview> {

    @Select("SELECT * FROM last_settlement_review WHERE base_project_id = #{key}  and del_flag  ='0'")
    SettlementAuditInformation findOneUp(@Param("key") String key);
}

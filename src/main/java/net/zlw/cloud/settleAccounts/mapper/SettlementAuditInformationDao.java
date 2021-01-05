package net.zlw.cloud.settleAccounts.mapper;

import net.zlw.cloud.designProject.model.CostVo2;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SettlementAuditInformationDao extends Mapper<SettlementAuditInformation> {


    @Select("SELECT * FROM settlement_audit_information WHERE base_project_id = #{key}  and del_flag  ='0'")
    SettlementAuditInformation findOneDown(@Param("key") String key);

}

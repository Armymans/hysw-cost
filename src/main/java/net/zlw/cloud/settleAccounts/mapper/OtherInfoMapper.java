package net.zlw.cloud.settleAccounts.mapper;

import net.zlw.cloud.settleAccounts.model.OtherInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Classname OtherInfoMapper
 * @Description TODO
 * @Date 2020/11/25 18:19
 * @Created by sjf
 */

@org.apache.ibatis.annotations.Mapper
public interface OtherInfoMapper extends Mapper<OtherInfo> {

    @Select("SELECT * FROM other_info WHERE `status` = '0' AND foreign_key = #{baseId}")
    List<OtherInfo> selectOtherList(@Param("baseId") String baseId);
}

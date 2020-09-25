package net.zlw.cloud.depManage.mapper;

import net.zlw.cloud.depManage.domain.DepManage;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xulei on 2020/9/18.
 */
@org.apache.ibatis.annotations.Mapper
public interface DepManageMapper  extends tk.mybatis.mapper.common.Mapper<DepManage> {

    @Select("select * from dep_manage where status = '0'")
    List<DepManage> selectAllByStatus();
}

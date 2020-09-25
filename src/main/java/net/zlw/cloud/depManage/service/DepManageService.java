package net.zlw.cloud.depManage.service;

import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.mapper.DepManageMapper;
import net.zlw.cloud.depManage.mapper.MemberManageMapper;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**  部门管理列表数据  设计层
 * Created by xulei on 2020/9/18.
 */
@Service
@Transactional
public class DepManageService {
    @Resource
    private DepManageMapper depManageMapper;

    @Resource
    private MemberManageMapper manageMapper;

    public List<DepManage> findAll() {
        List<DepManage>  sql2 = depManageMapper.selectAllByStatus();
        if (sql2.size() > 0){
            for (DepManage depManage : sql2) {
                Example example = new Example(DepManage.class);
                example.createCriteria().andEqualTo("depId");
                List<MemberManage> depId = manageMapper.selectByExample(example);
                depManage.setMemberManages(depId);
            }

        }
        return sql2;
    }
}

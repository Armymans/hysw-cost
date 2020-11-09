package net.zlw.cloud.remindSet.service;

import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.remindSet.model.RemindSet;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**  提醒设置列表事物层
 * Created by xulei on 2020/9/20.
 */
@Service
@Transactional
public class RemindSetService {

    @Resource
    private RemindSetMapper remindSetMapper;

    @Resource
    private MemberManageDao memberManageDao;


    public List<RemindSet> findAll() {
        List<RemindSet> list = remindSetMapper.findAllByStatus();
        for (RemindSet remindSet : list) {
            String remeberId = remindSet.getRemeberId();
            String[] split = remeberId.split(",");
            String names = "";
            for (String s : split) {
                MemberManage memberManage = memberManageDao.selectByIdAndStatus(s);
                if(memberManage != null){
                    names += memberManage.getMemberName()  + ",";
                }
            }
            if(names.length() > 0){
                names = names.substring(0 ,names.length() - 1);
            }
            remindSet.setMemberName(names);
        }
        return list;
    }

    public void update(String id, String[] remeberId) {
        String re = "";
        for (String s : remeberId) {
            re += s+",";
        }
        re = re.substring(0 ,re.length() - 1);
        RemindSet remindSet = remindSetMapper.selectByPrimaryKey(id);
        remindSet.setRemeberId(re);

        remindSetMapper.updateByPrimaryKeySelective(remindSet);
    }
}

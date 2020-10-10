package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.snsEmailFile.mapper.SnsInfoMapper;
import net.zlw.cloud.snsEmailFile.model.SnsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Armyman
 * @Description 短信service
 * @Date 2020/10/9 16:43
 **/
@Service
@Transactional
public class SnsInfoService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SnsInfoMapper snsInfoMapper;


    public void saveSnsInfo(SnsInfo snsInfo) {
        snsInfo.setCreateTime(sdf.format(new Date()));
        snsInfoMapper.insert(snsInfo);
    }
}

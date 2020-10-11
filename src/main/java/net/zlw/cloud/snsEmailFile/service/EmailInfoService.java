package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.snsEmailFile.mapper.EmailInfoMapper;
import net.zlw.cloud.snsEmailFile.model.EmailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

/**
 * @Author Armyman
 * @Description 邮件service
 * @Date 2020/10/9 16:44
 **/
@Service
@Transactional
public class EmailInfoService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EmailInfoMapper emailInfoMapper;


    public void save(EmailInfo emailInfo) {
        emailInfoMapper.insertSelective(emailInfo);
    }
}

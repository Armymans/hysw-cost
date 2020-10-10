package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;

/**
 * @Author Armyman
 * @Description 文件service
 * @Date 2020/10/9 16:43
 **/
@Service
@Transactional
public class FileInfoService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FileInfoMapper fileInfoMapper;



}
